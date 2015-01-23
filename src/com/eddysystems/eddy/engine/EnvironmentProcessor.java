package com.eddysystems.eddy.engine;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.psi.*;
import com.intellij.psi.scope.BaseScopeProcessor;
import com.intellij.psi.scope.ElementClassHint;
import com.intellij.psi.scope.JavaScopeProcessorEvent;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.scope.util.PsiScopesUtil;
import com.intellij.util.SmartList;
import org.jetbrains.annotations.NotNull;
import tarski.Environment.PlaceInfo;
import tarski.Environment;
import tarski.Items.*;
import tarski.Types.ClassType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.eddysystems.eddy.engine.Utility.log;

/**
 * Extract information about the environment at a given place in the code and make it available in a format understood by tarski
 */
class EnvironmentProcessor {
  private static class Shadow<E> {
    public final E e;
    public final int shadowingPriority;

    public Shadow(E e, int p) {
      this.e = e;
      shadowingPriority = p;
    }
  }

  public final PlaceInfo placeInfo;
  public final List<Item> localItems = new ArrayList<Item>();
  public final Map<Item,Integer> scopeItems = new HashMap<Item,Integer>();

  // add to locals what you find in scope. There may be several threads writing to locals at the same time.
  public EnvironmentProcessor(final @NotNull Project project, final @NotNull JavaEnvironment jenv,
                              final Map<PsiElement,Item> locals, final @NotNull PsiElement place,
                              final int lastEdit, final boolean honorPrivate) {
    final Place where = new Place(project,place);

    // Walk up the PSI tree, also processing import statements
    final Processor P = new Processor(where,honorPrivate);
    PsiScopesUtil.treeWalkUp(P, place, place.getContainingFile());
    this.placeInfo = fillLocalInfo(P,where,jenv,locals,lastEdit);
  }

  /**
   * Make the IntelliJ-independent class that is used by the tarski engine to look up possible names, using jenv as a base
   */
  private PlaceInfo fillLocalInfo(final Processor P, final Place where, final JavaEnvironment jenv,
                                  final Map<PsiElement,Item> locals, final int lastEdit) {
    // local variables, parameters, type parameters, as well as protected/private things in scope
    final Converter env = new Converter(jenv,locals);

    log("getting local items...");

    // Register locally visible packages
    for (final Shadow<PsiPackage> spkg : P.packages) {
      final PsiPackage pkg = spkg.e;
      final Item ipkg = env.addContainer(pkg);
      scopeItems.put(ipkg,spkg.shadowingPriority);
    }

    // Register classes
    for (final Shadow<PsiClass> scls : P.classes) {
      final PsiClass cls = scls.e;
      final Item icls = env.addClass(cls, true);
      scopeItems.put(icls,scls.shadowingPriority);
    }

    // register methods
    for (final Shadow<PsiMethod> smethod : P.methods) {
      final PsiMethod method = smethod.e;
      final Item imethod = env.addMethod(method);
      if (!(imethod instanceof ConstructorItem)) {
        scopeItems.put(imethod,smethod.shadowingPriority);
      }
    }

    // then, register values
    for (final Shadow<PsiVariable> svar : P.variables) {
      final PsiVariable var = svar.e;
      if (var instanceof PsiField) {
        final Item ivar = env.addField((PsiField) var);
        scopeItems.put(ivar,svar.shadowingPriority);
      } else {
        // true local variables (parameters or local variables)
        final Item i = env.addLocal(var);
        scopeItems.put(i,svar.shadowingPriority);
      }
    }

    log("added " + locals.size() + " locals");

    synchronized(jenv) {
      // .values is undefined if it is modified during iteration.
      localItems.addAll(locals.values());
    }

    // find out which element we are inside (method, class or interface, or package)
    ParentItem placeItem = null;
    boolean inside_continuable = false;
    boolean inside_breakable = false;
    // walk straight up until we see a method, class, or package
    PsiElement place = where.place;
    while (place != null) {
      // scan the current method for labels, loops, and switch statements
      if (placeItem != null) {
        if (place instanceof PsiLabeledStatement) {
          final PsiLabeledStatement lab = (PsiLabeledStatement)place;
          final boolean continuable = !(lab instanceof PsiSwitchStatement);
          localItems.add(new Label(lab.getLabelIdentifier().getText(),continuable));
        }
        if (place instanceof PsiSwitchStatement) {
          log("inside switch statement: " + place);
          inside_breakable = true;
        }
        if (place instanceof PsiLoopStatement) {
          log("inside loop statement: " + place);
          inside_breakable = true;
          inside_continuable = true;
        }
      }

      // add special "this" and "super" items this for each class we're inside of, with same shadowing priority as the class itself
      if (place instanceof PsiClass && !((PsiClass) place).isInterface()) { // don't make this for interfaces
        final ClassItem c = (ClassItem)env.addClass((PsiClass)place, false);
        assert scopeItems.containsKey(c);
        final int p = scopeItems.get(c);
        final ThisItem ti = new ThisItem(c);
        localItems.add(ti);
        scopeItems.put(ti,p);

        final ClassType s = c.base();
        assert s.item().isClass();
        final SuperItem si = new SuperItem(s);
        localItems.add(si);
        scopeItems.put(si,p);
      }

      if (place instanceof PsiMethod || place instanceof PsiClass || place instanceof PsiPackage) {
        if (placeItem == null) {
          placeItem = (ParentItem)jenv.lookup(place, true);
          assert placeItem != null : "cannot find placeItem " + place + ", possibly in anonymous local class";
        }
      } else if (place instanceof PsiJavaFile) {
        final PsiPackage pkg = Place.getPackage((PsiJavaFile) place, env.project);
        if (pkg == null) {
          // probably we're top-level in a file without package statement, use LocalPackageItem
          if (placeItem == null)
            placeItem = LocalPkg$.MODULE$;
        } else {
          if (placeItem == null) {
            assert jenv.knows(pkg);
            placeItem = env.addContainer(pkg);
          }
        }
        break;
      }
      place = place.getParent();
    }
    assert placeItem != null;

    log("environment (" + localItems.size() + " local items) taken inside " + placeItem);

    return Environment.PlaceInfoJava(placeItem,where.place,inside_breakable,inside_continuable,lastEdit);
  }

  private static final class Processor implements PsiScopeProcessor {
    private final @NotNull Place place;
    private final boolean honorPrivate;

    // State for walking
    private int currentLevel = 0;
    private boolean inStaticScope = false;

    // Things that are in scope (not all these are accessible! things may be private, or not static while we are)
    private final List<Shadow<PsiPackage>> packages = new SmartList<Shadow<PsiPackage>>();
    private final List<Shadow<PsiClass>> classes = new SmartList<Shadow<PsiClass>>();
    private final List<Shadow<PsiVariable>> variables = new SmartList<Shadow<PsiVariable>>();
    private final List<Shadow<PsiMethod>> methods = new SmartList<Shadow<PsiMethod>>();

    Processor(final @NotNull Place place, final boolean honorPrivate) {
      this.place = place;
      this.honorPrivate = honorPrivate;
    }

    // Hints
    private static final ElementClassHint hint = new ElementClassHint() {
      @Override public boolean shouldProcess(final DeclarationKind kind) {
        return kind == DeclarationKind.CLASS
            || kind == DeclarationKind.FIELD
            || kind == DeclarationKind.METHOD
            || kind == DeclarationKind.VARIABLE
            || kind == DeclarationKind.PACKAGE
            || kind == DeclarationKind.ENUM_CONST;
      }
    };
    @Override public <T> T getHint(final @NotNull Key<T> key) {
      return key == ElementClassHint.KEY ? (T)hint : null;
    }

    @Override public boolean execute(final @NotNull PsiElement element, final ResolveState state) {
      // if we are in static scope, a class member has to be declared static for us to see it
      // TODO: we should add these either way, and let the semantics logic take care of static scoping
      if (element instanceof PsiField || element instanceof PsiMethod) {
        if (inStaticScope && !((PsiMember)element).hasModifierProperty(PsiModifier.STATIC))
          return true;
      }

      if (honorPrivate && place.isInaccessible((PsiModifierListOwner)element)) {
        //log("rejecting " + element + " because it is inaccessible");
        return true;
      }

      //log("found element " + element + " at level " + currentLevel);

      if (element instanceof PsiClass)
        classes.add(new Shadow<PsiClass>((PsiClass)element,currentLevel));
      else if (element instanceof PsiVariable)
        variables.add(new Shadow<PsiVariable>((PsiVariable)element,currentLevel));
      else if (element instanceof PsiMethod)
        methods.add(new Shadow<PsiMethod>((PsiMethod)element,currentLevel));
      else if (element instanceof PsiPackage)
        packages.add(new Shadow<PsiPackage>((PsiPackage)element,currentLevel));
      return true;
    }

    @Override public final void handleEvent(final @NotNull Event event, final Object associated) {
      if (event == JavaScopeProcessorEvent.START_STATIC) {
        //log("starting static scope");
        inStaticScope = true;
      } else if (event == JavaScopeProcessorEvent.SET_CURRENT_FILE_CONTEXT) {
        if (associated instanceof PsiAnonymousClass)
          classes.add(new Shadow<PsiClass>((PsiClass)associated,currentLevel));
        //log("switching context: " + associated);
      } else if (event == JavaScopeProcessorEvent.CHANGE_LEVEL) {
        currentLevel++;
        //log("change level to " + currentLevel + ", associated " + associated);
      }
    }
  }
}