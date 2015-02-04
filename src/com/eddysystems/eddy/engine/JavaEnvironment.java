package com.eddysystems.eddy.engine;

import com.eddysystems.eddy.EddyThread;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.IndexNotReadyException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.*;
import com.intellij.psi.impl.java.stubs.index.JavaFullClassNameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.ProjectScope;
import com.intellij.psi.search.PsiShortNamesCache;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.stubs.StubIndexImpl;
import com.intellij.util.Processor;
import com.intellij.util.indexing.IdFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import scala.NotImplementedError;
import tarski.*;

import java.util.*;
import java.util.concurrent.locks.Lock;

import static com.eddysystems.eddy.engine.Utility.log;
import static com.eddysystems.eddy.engine.Utility.logError;
import static java.lang.Thread.sleep;
import static utility.JavaUtils.popScope;
import static utility.JavaUtils.pushScope;

// a class storing information about the environment.
public class JavaEnvironment {

  public static class NoJDKError extends RuntimeException {
    NoJDKError(String s) {
      super("No JDK found: " + s);
    }
  }

  static class PsiGenerator implements Tries.Generator<Items.Item> {

    static final int cacheSize = 10000;
    final LRUCache<String, Items.Item[]> cache = new LRUCache<String, Items.Item[]>(cacheSize);

    final Project project;
    final GlobalSearchScope scope;
    final boolean checkVisibility;
    final PsiShortNamesCache psicache;
    final IdFilter filter = new IdFilter() { @Override public boolean containsFileId(int id) { return true; } };
    final Converter converter;

    // true if there's a chance that element is visible from outside its file. Only elements that are private or
    // inside private or anonymous elements or that are local are not potentially visible.
    boolean possiblyVisible(PsiModifierListOwner element) {
      PsiElement container = null;
      try {
        container = Place.containing(element, project);
      } catch (Place.UnexpectedContainerError e) {
        log(e.getMessage());
        return false;
      }

      // anything toplevel in a package is at most protected
      if (container instanceof PsiPackage) {
        return true;
      }

      // anything private is out
      if (element.hasModifierProperty(PsiModifier.PRIVATE)) {
        return false;
      }

      // everything else, depends on the container
      if (container instanceof PsiModifierListOwner) {
        return possiblyVisible((PsiModifierListOwner)container);
      } else
        return false;
    }

    PsiGenerator(Project project, GlobalSearchScope scope, Converter conv, boolean checkVisibility) {
      this.project = project;
      this.scope = scope;
      this.checkVisibility = checkVisibility;
      this.psicache = PsiShortNamesCache.getInstance(project);
      converter = conv;
    }

    private Items.Item[] generate(String s) {
      final EddyThread thread = EddyThread.getEddyThread();
      final List<Items.Item> results = new ArrayList<Items.Item>();

      final Processor<PsiClass> classProc = new Processor<PsiClass>() {
      @Override
      public boolean process(PsiClass cls) {
        if (thread != null && thread.canceled())
          return false;
        if (!checkVisibility || possiblyVisible(cls))
          results.add(converter.addClass(cls, false));
        return true;
      }
      };

      final Processor<PsiMethod> methodProc = new Processor<PsiMethod>() {
      @Override
      public boolean process(PsiMethod method) {
        if (thread != null && thread.canceled())
          return false;
        if (!checkVisibility || possiblyVisible(method) && !Converter.isConstructor(method))
          results.add(converter.addMethod(method));
        return true;
      }
      };

      final Processor<PsiField> fieldProc = new Processor<PsiField>() {
      @Override
      public boolean process(PsiField fld) {
        if (thread != null && thread.canceled())
          return false;
        if (!checkVisibility || possiblyVisible(fld))
          results.add(converter.addField(fld));
        return true;
      }
      };

      if (thread != null) thread.pushSoftInterrupts();
      try {
        psicache.processClassesWithName(s, classProc, scope, filter);
        psicache.processMethodsWithName(s, methodProc, scope, filter);
        psicache.processFieldsWithName(s, fieldProc, scope, filter);
      } finally {
        if (thread != null) thread.popSoftInterrupts();
      }
      return results.toArray(new Items.Item[results.size()]);
    }

    @Override @NotNull
    public Items.Item[] lookup(String s) {
      Items.Item[] result = cache.get(s);

      if (result != null)
        return result;
      else
        result = generate(s);

      // add to cache
      cache.put(s, result);
      return result;
    }
  }

  @NotNull final Project project;
  @NotNull final Converter converter;

  private boolean _initialized = false;
  public boolean initialized() {
    return _initialized;
  }

  // all these are possibly accessed by several threads. Modifying or non-atomic read access requiring consistency to any
  // of items, localItems, addedItems has to be synchronized(this). If the caller is inside an IntelliJ write action
  // (such as anything called from the PsiListener), synchronization is not necessary because all other accessing threads
  // are inside IntelliJ read actions, and will not run during a write action)

  // Cached items. The contents lives only as long as a single action, so no special care is needed to make sure these things are up to date.
  Map<PsiElement, Items.Item> items = new HashMap<PsiElement, Items.Item>();

  // pre-computed data structures to enable fast creation of appropriate scala Env instances

  // when a local env is created, it contains:
  //  - A global lookup object (created from the global list of all names and a generator object
  // able to translate a name into a list of items)
  //  - A project lookup object (same as the global one, but recomputed each time from
  //  - A byItem map for local items
  int[] nameTrie;
  Tries.LazyTrie<Items.Item> trie = null;

  // map types to items of that type. This is built once for the project, and should be rebuilt
  // continuously in a low-priority background thread.
  Map<String, List<String>> pByItem = null;

  public JavaEnvironment(@NotNull Project project) {
    this.project = project;
    converter = new Converter(this, items);
  }

  public void initialize(@Nullable ProgressIndicator indicator) {
    pushScope("make base environment");
    try {
      // add base items
      DumbService.getInstance(project).runReadActionInSmartMode(new Runnable() {
        @Override
        public void run() {
          addBase();
        }
      });

      String[] fieldNames = DumbService.getInstance(project).runReadActionInSmartMode( new Computable<String[]>() { @Override public String[] compute() {
        return PsiShortNamesCache.getInstance(project).getAllFieldNames();
      }});

       // make global name lookup trie (read actions taken care of inside)
      prepareNameTrie(fieldNames);

      // make trie for name lookup
      trie = new Tries.LazyTrie<Items.Item>(nameTrie, new PsiGenerator(project, ProjectScope.getLibrariesScope(project), converter, false));

      if (indicator != null)
        indicator.setIndeterminate(false);

      // takes care of read action business inside (this is where all the work happens)
      makeProjectValuesByItem(fieldNames, indicator);

      if (indicator != null)
        indicator.setIndeterminate(true);

      _initialized = true;
    } finally { popScope(); }
  }

  synchronized boolean knows(PsiElement elem) {
    return items.containsKey(elem);
  }

  @Nullable
  synchronized Items.Item lookup(PsiElement elem) {
    // Everything in addedItems is also in localItems.
    return items.get(elem);
  }

  private void addBase() {
    pushScope("add base");
    try {
      final GlobalSearchScope scope = ProjectScope.getAllScope(project);

      // Extra things don't correspond to PsiElements
      final Set<Items.Item> extra =  new HashSet<Items.Item>();
      Collections.addAll(extra, Base.extraEnv().allItems());

      // Add classes and packages
      final JavaPsiFacade facade = JavaPsiFacade.getInstance(project);
      for (Items.Item item : tarski.Base.baseEnv().allItems()) {
        if (extra.contains(item) || item instanceof Items.ConstructorItem)
          continue;
        final String name = item.qualified();
        PsiElement psi;
        if (item instanceof Items.Package)
          psi = facade.findPackage(name);
        else if (item instanceof Items.ClassItem)
          psi = facade.findClass(name,scope);
        else
          throw new NotImplementedError("Unknown base type "+item.getClass());
        if (psi == null)
          throw new NoJDKError("Couldn't find " + name);
        //log("adding base item " + item + " for " + psi + "@" + psi.hashCode() + " original " + psi.getOriginalElement().hashCode());
        converter.put(psi, item);
      }

      // Add constructors
      for (Items.Item item : tarski.Base.baseEnv().allItems()) {
        if (!(item instanceof Items.ConstructorItem))
          continue;
        final String clsName = ((Items.ConstructorItem)item).parent().qualified();
        final PsiClass cls = facade.findClass(clsName,scope);
        assert cls != null;
        final PsiMethod[] cons = cls.getConstructors();
        if (cons.length != 1)
          log("found " + cons.length + " constructors for object " + cls);
        converter.put(cons[0],item);
      }

      // Add class members
      for (Items.Item item : tarski.Base.baseEnv().allItems()) {
        if (extra.contains(item) || !(item instanceof Items.ClassItem))
          continue;
        final String name = item.qualified();
        converter.addClassMembers(facade.findClass(name, scope), (Items.ClassItem) item);
       }
    } finally { popScope(); }
  }

  private void prepareNameTrie(String[] fieldNames) {
    pushScope("prepare lazy trie");
    try {
      String[] classNames = DumbService.getInstance(project).runReadActionInSmartMode( new Computable<String[]>() { @Override public String[] compute() {
        return PsiShortNamesCache.getInstance(project).getAllClassNames();
      }});
      String[] methodNames = DumbService.getInstance(project).runReadActionInSmartMode( new Computable<String[]>() { @Override public String[] compute() {
        return PsiShortNamesCache.getInstance(project).getAllMethodNames();
      }});

      String[] allNames = new String[classNames.length + fieldNames.length + methodNames.length];
      System.arraycopy(classNames, 0, allNames, 0, classNames.length);
      System.arraycopy(fieldNames, 0, allNames, classNames.length, fieldNames.length);
      System.arraycopy(methodNames, 0, allNames, classNames.length+fieldNames.length, methodNames.length);

      // there may be duplicates, but we don't particularly care
      Arrays.sort(allNames);
      nameTrie = JavaTrie.makeTrieStructure(allNames);
    } finally { popScope(); }
  }

  // TODO: this should use the same pause mechanism as EddyThread (counted)
  private static boolean _writeActionWaiting = false;
  public static synchronized void writeActionWaiting() {
    _writeActionWaiting = true;
  }
  private static synchronized boolean checkClearWriteActionWaiting() {
    if (_writeActionWaiting) {
      _writeActionWaiting = false;
      return true;
    } else
      return false;
  }

  private Map<String, Set<String>> makeProjectValuesByItem(String[] fieldNames, @Nullable ProgressIndicator indicator) {
    pushScope("make project values by item");

    // Prepare to grab index write locks to avoid crazy deadlocks.  This is a terrible hack around broken OPC.
    final Lock fqnLock = ((StubIndexImpl)StubIndex.getInstance())
      .getWriteLock(JavaFullClassNameIndex.getInstance().getKey());

    try {
      final Map<String,Set<String>> result = new HashMap<String,Set<String>>();

      final GlobalSearchScope scope = ProjectScope.getContentScope(project);
      final PsiShortNamesCache cache = PsiShortNamesCache.getInstance(project);
      final IdFilter filter = IdFilter.getProjectIdFilter(project, false);
      final Processor<PsiField> proc = new Processor<PsiField>() {

        final Stack<PsiType> work = new Stack<PsiType>();
        final Set<PsiType> seen = new HashSet<PsiType>();

        @Override
        public boolean process(final PsiField f) {
          fqnLock.lock();
          try {
            // put this field into the string map for its type and all its supertypes
            String name = f.getName();
            seen.clear();
            work.clear();
            work.push(f.getType());
            while (!work.isEmpty()) {
              PsiType t = work.pop();
              // we don't want generics in here
              if (t instanceof PsiClassType) {
                // never add java.lang.Object
                if (((PsiClassType)t).getClassName().equals("Object")) {
                  PsiClass tc = ((PsiClassType)t).resolve();
                  if (tc != null && "java.lang.Object".equals(tc.getQualifiedName()))
                      continue;
                }
                t = ((PsiClassType)t).rawType();
              }

              // add to map
              String type = t.getCanonicalText();
              log("found field " + f + " with type " + type);
              Set<String> values = result.get(type);
              if (values == null) {
                values = new HashSet<String>();
                result.put(type,values);
              }
              values.add(name);

              for (final PsiType s : t.getSuperTypes()) {
                if (!seen.contains(s)) {
                  seen.add(s);
                  work.push(s);
                }
              }
            }

          } catch (AssertionError e) {
            // If we're in the Scala plugin, log and squash the error.  Otherwise, rethrow.
            if (utility.Utility.fromScalaPlugin(e)) logError("makeProjectValuesByItem()",e);
            else throw e;
          } finally {
            fqnLock.unlock();
          }
          return true;
        }
      };

      final Utility.SmartReadLock lock = new Utility.SmartReadLock(project);
      int i = 0;
      final double n = fieldNames.length;
      try {
        for (final String s : fieldNames) {
          if (indicator != null) {
            indicator.checkCanceled();
            indicator.setFraction(i++/n);
          }

          // pretend we're doing a big read action here, if we get stumped by a dumb mode, repeat until it passes
          boolean done = false;
          while (!done) {
            done = true;
            lock.acquire();
            try {
              cache.processFieldsWithName(s, proc, scope, filter);
            } catch (AssertionError e) {
              // If we're in the Scala plugin, log and squash the error. Don't retry. Otherwise, rethrow.
              if (utility.Utility.fromScalaPlugin(e)) {
                logError("makeProjectValuesByItem()",e);
              }
              else throw e;
            } catch (IndexNotReadyException e) {
              // we entered a dumb mode while processing this name, simply try again
              done = false;
            } finally {
              // only release the lock if a write action is trying to start
              if (checkClearWriteActionWaiting()) {
                lock.release();
                // yield to other threads to start the write action
                try {
                  sleep(0);
                } catch (InterruptedException e) {
                  throw new ProcessCanceledException();
                }
              }
            }
          }
        }

        return result;

      } finally {
        // make sure the lock is released
        lock.release();
      }
    } finally {
      popScope();
    }
  }

  // get a combined environment at the given place
  Environment.Env getLocalEnvironment(@NotNull PsiElement place, final int lastEdit) {
    // ep will fill scopeItems (and it has its own store for special non-psi items and constructors)
    final EnvironmentProcessor ep = new EnvironmentProcessor(project, this, items, place, lastEdit);

    final ValueByItemQuery vbi = new ValueByItemQuery() {
      final Map<Items.TypeItem, Items.Value[]> cache = new HashMap<Items.TypeItem, Items.Value[]>();
      final PsiShortNamesCache psicache = PsiShortNamesCache.getInstance(project);
      final GlobalSearchScope scope = ProjectScope.getContentScope(project);
      final IdFilter filter = new IdFilter() { @Override public boolean containsFileId(int id) { return true; } };
      final List<Items.Value> result = new ArrayList<Items.Value>();
      final EddyThread thread = EddyThread.getEddyThread();
      final Map<Items.TypeItem, Items.Value[]> vByItem = JavaItems.valuesByItem(ep.scopeItems);

      final Processor<PsiField> proc = new Processor<PsiField>() {
        @Override
        public boolean process(PsiField psiField) {
          // convert
          // check if type is really the type we want
          if (thread != null && thread.canceled())
            return false;
          result.add(converter.addField(psiField));
          return true;
        }
      };

      @Override
      public Items.Value[] query(Items.TypeItem type) {
        if (cache.containsKey(type))
          return cache.get(type);

        result.clear();

        // look up names in project
        String ts = type.qualified();
        if (pByItem.containsKey(ts)) {
          if (thread != null) thread.pushSoftInterrupts();
          for (String name : pByItem.get(ts)) {
            psicache.processFieldsWithName(name, proc, scope, filter);
          }
          if (thread != null) thread.popSoftInterrupts();
        }

        // filter and return
        int count = 0;
        for (int i = 0; i < result.size(); ++i) {
          if (result.get(i).item() == type)
            count++;
        }

        Items.Value[] varr = vByItem.get(type);
        Items.Value[] rarr = varr == null ? new Items.Value[count] : Arrays.copyOf(varr, varr.length + count);
        count = varr == null ? 0 : varr.length;
        for (int i = 0; i < result.size(); ++i) {
          Items.Value vi = result.get(i);
          if (vi.item() == type)
            rarr[count++] = vi;
        }

        cache.put(type, rarr);
        return rarr;
      }
    };

    return Tarski.environment(trie, vbi, ep.scopeItems, ep.placeInfo);
  }


}
