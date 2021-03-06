/* EddyPlugin: Toplevel plugin object (one per project) */

package com.eddysystems.eddy;

import com.eddysystems.eddy.engine.ChangeTracker;
import com.eddysystems.eddy.engine.EddyPsiListener;
import com.eddysystems.eddy.engine.JavaEnvironment;
import com.eddysystems.eddy.engine.TypeNameItemNamePair;
import com.intellij.ide.PowerSaveMode;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.RuntimeInterruptedException;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.progress.PerformInBackgroundOption;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiTreeChangeListener;
import com.intellij.util.PathUtil;
import com.intellij.util.ResourceUtil;
import com.intellij.util.messages.MessageBusConnection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tarski.Memory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static com.eddysystems.eddy.engine.Utility.log;
import static utility.JavaUtils.isDebug;

public class EddyPlugin implements ProjectComponent {
  @NotNull final private Application app;
  @NotNull final private EddyApplicationListener listener;
  private Project project;

  // Find our "install" key, or create it if necessary
  static private String _install = null;
  private static String installKey() {
    if (_install == null) {
      final PropertiesComponent props = PropertiesComponent.getInstance();
      final String name = "com.eddysystems.Props.install";
      final String atName = "com.eddysystems.Props.installedAt";
      _install = props.getValue(name);
      if (_install == null) {
        _install = tarski.Crypto.randomKey();
        props.setValue(name,_install);
      }
      if (props.getValue(atName) == null) {
        // backfill installation date to now
        props.setValue(atName, String.format(Locale.US, "%f", GregorianCalendar.getInstance().getTimeInMillis() / 1000.));
      }
    }
    return _install;
  }

  private static String ideaVersion() {
    return ApplicationInfo.getInstance().getBuild().asString();
  }

  // Basic information for logging
  public static Memory.Info basics(final @Nullable Project project) {
    return Memory.basics(installKey(), getVersion() + " - " + getBuild(), project != null ? project.getName() : null, ideaVersion());
  }

  private static void checkLogging() {
    // if no logging preference saved, make the user select one
    final PropertiesComponent props = PropertiesComponent.getInstance();
    final String name = "com.eddysystems.Props.checkedLogging";
    String checked = props.getValue(name);

    log("logging state: " + checked);

    if (checked == null || !checked.equals("true")) {

      // we haven't checked before, check now and save to Preferences
      final Object done = new Object();
      final Runnable showRunner = new Runnable() {
          @Override
          public void run() {
            final TOSDialog d = new TOSDialog();
            d.showAndGet();
            Preferences.getData().setLogPreference(d.logging());
            Preferences.save();
            synchronized (done) {
              done.notifyAll();
            }
          }
      };

      // go to dispatch to show dialog
      if (!ApplicationManager.getApplication().isDispatchThread()) {
        // we have to wait manually, because getting the current modality isn't a thing that 13 lets us do outside of
        // dispatch
        ApplicationManager.getApplication().invokeLater(showRunner);
        try {
          synchronized (done) {
            done.wait();
          }
        } catch (InterruptedException e) {
          throw new RuntimeInterruptedException(e);
        }
      } else {
        showRunner.run();
      }
      props.setValue(name, "true");
    }
  }

  static private Properties _properties = null;
  public static Properties getProperties() {
    if (_properties == null) {
      _properties = new Properties();

      try {
        final String pathname = PathUtil.getJarPathForClass(EddyPlugin.class);
        final File path = new File(pathname);
        if (path.isDirectory()) {
          log("looking for resources in directory: " + pathname);
          _properties.load(new FileInputStream(new File(path, "eddy.properties")));
        } else {
          final URL url = ResourceUtil.getResource(EddyPlugin.class, "", "eddy.properties");
          _properties.load(url.openStream());
        }
      } catch (IOException e) {
        log("cannot read version information: " + e);
      }
    }
    return _properties;
  }

  public static String getVersion() {
    return getProperties().getProperty("version");
  }

  public static String getBuild() {
    return getProperties().getProperty("build");
  }

  public Project getProject() { return project; }
  private EddyInjector injector;
  private EddyWidget widget = new EddyWidget(this);

  private static Map<Project,EddyPlugin> projectMap = new HashMap<Project, EddyPlugin>();
  public static EddyPlugin getInstance(Project project) {
    return projectMap.get(project);
  }

  private PsiTreeChangeListener psiListener = null;
  private JavaEnvironment env = null;
  public JavaEnvironment getEnv() { return env; }

  private boolean initializing = false;
  private Object initLock = new Object();
  public boolean isInitialized() { return env != null && env.initialized(); }
  public boolean isReady() { return isInitialized() && !PowerSaveMode.isEnabled(); }

  public void dropEnv() {
    if (env != null) {
      env.dispose();
      env = null;
    }
  }

  public void initEnv(@Nullable ProgressIndicator indicator) {
    if (indicator != null)
      indicator.setIndeterminate(true);

    if (psiListener != null) {
      PsiManager.getInstance(project).removePsiTreeChangeListener(psiListener);
    }
    dropEnv();

    final ChangeTracker<String> nameTracker = new ChangeTracker<String>();
    final ChangeTracker<TypeNameItemNamePair> fieldTracker = new ChangeTracker<TypeNameItemNamePair>();
    final ChangeTracker<TypeNameItemNamePair> methodTracker = new ChangeTracker<TypeNameItemNamePair>();
    if (ApplicationManager.getApplication().isHeadlessEnvironment()) {
      // free env before allocating the new one
      env = new JavaEnvironment(project, nameTracker,fieldTracker,methodTracker);
      psiListener = new EddyPsiListener(nameTracker,fieldTracker,methodTracker);
      PsiManager.getInstance(project).addPsiTreeChangeListener(psiListener);
      env.updateSync(indicator);
    } else {
      final StatusBar sbar = WindowManager.getInstance().getStatusBar(project);
      String err = "";

      try {
        // can't have changes between when we make the environment and when we register the psi listener
        app.runReadAction(new Runnable() { @Override public void run() {
          env = new JavaEnvironment(project, nameTracker, fieldTracker, methodTracker);
          psiListener = new EddyPsiListener(nameTracker, fieldTracker, methodTracker);
          PsiManager.getInstance(project).addPsiTreeChangeListener(psiListener);
        }});
        err = env.updateSync(indicator);
        if (!err.isEmpty()) {
          dropEnv();
          log("error during initialization: " + err);
        }
      } finally {
        initializing = false;
        if (sbar != null) {
          if (err.isEmpty())
            sbar.setInfo("eddy initialized.");
          else
            sbar.setInfo("eddy library scan aborted, " + err);
        }
      }
    }
  }

  public EddyPlugin(Project project) {
    assert !projectMap.containsKey(project);

    app = ApplicationManager.getApplication();
    this.project = project;

    log("available memory: total " + Runtime.getRuntime().totalMemory() + ", max " + Runtime.getRuntime().maxMemory() + ", free " + Runtime.getRuntime().freeMemory());

    projectMap.put(project, this);
    injector = new EddyInjector(project);
    listener = new EddyApplicationListener();
    app.addApplicationListener(listener);
  }

  public EddyWidget getWidget() {
    return widget;
  }

  // returns immediately
  public void requestInit() {
    if (isInitialized())
      return;

    synchronized (initLock) {
      if (initializing)
        return;
      initializing = true;
    }

    // schedule initialization if necessary
    final Runnable init = new Runnable() { @Override public void run() {
      ProgressManager.getInstance().run(new Task.Backgroundable(project, "Initializing eddy...", true, new PerformInBackgroundOption() {
        @Override public boolean shouldStartInBackground() { return true; }
        @Override public void processSentToBackground() { }
      }) {
        @Override
        public void run(@NotNull final ProgressIndicator indicator) {
          // either testing or in background
          assert (app.isHeadlessEnvironment() || !app.isDispatchThread());
          initEnv(indicator);
        }
      });
    }};

    if (app.isDispatchThread()) {
      init.run();
    } else {
      app.invokeLater(init, project.getDisposed());
    }
  }

  public static void initAll() {
    for (EddyPlugin plugin : projectMap.values()) {
      plugin.initComponent();
    }
  }

  public void initComponent() {

    log("eddy starting" + (isDebug() ? " (debug)" : "") + ": installation " + installKey() + " version " + getVersion() + " build " + getBuild());
    log("now is " + String.format("%f", GregorianCalendar.getInstance().getTimeInMillis() / 1000.));

    if (!app.isHeadlessEnvironment())
      checkLogging();

    final MessageBusConnection connection = project.getMessageBus().connect();

    // register our injector
    connection.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER,injector);

    // register for Powersave notifications
    connection.subscribe(PowerSaveMode.TOPIC,new PowerSaveMode.Listener() {
      @Override
      public void powerSaveStateChanged () {
        widget.update();
        if (PowerSaveMode.isEnabled()) {
          EddyThread.kill();
        }
      }
    });

    // initialize the global environment
    if(!app.isHeadlessEnvironment()) {
      StartupManager.getInstance(project).runWhenProjectIsInitialized(new Runnable() {
        @Override
        public void run() {
          widget.requestInstall();
          requestInit();
        }
      });
    }
  }

  public void disposeComponent() {
    log("disposing plugin.");
    assert app.isDispatchThread();
    projectMap.remove(project);
    final StatusBar sbar = WindowManager.getInstance().getStatusBar(project);

    if (sbar != null)
      sbar.removeWidget(widget.ID());

    if (psiListener != null)
      PsiManager.getInstance(project).removePsiTreeChangeListener(psiListener);

    app.removeApplicationListener(listener);
  }

  @NotNull
  public String getComponentName() {
    return "EddyPlugin";
  }

  public void projectOpened() {
    // called when project is opened
  }

  public void projectClosed() {
    // called when project is being closed
  }

}
