import com.eddysystems.eddy.Eddy;
import com.eddysystems.eddy.EnvironmentProcessor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.StdModuleTypes;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.impl.JavaAwareProjectJdkTableImpl;
import com.intellij.openapi.projectRoots.impl.ProjectJdkImpl;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.LanguageLevelModuleExtension;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.pom.java.LanguageLevel;
import com.intellij.testFramework.LightProjectDescriptor;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tarski.*;
import tarski.Items.Item;
import tarski.Scores.Alt;

import java.util.ArrayList;
import java.util.List;
import static ambiguity.JavaUtils.popScope;
import static ambiguity.JavaUtils.pushScope;

public class Tests extends LightCodeInsightFixtureTestCase {

  // TODO: initialize global environment once for all tests

  static class ProjectDesc implements LightProjectDescriptor {
    @Override
    public ModuleType getModuleType() {
      return StdModuleTypes.JAVA;
    }

    @Override
    public Sdk getSdk() {
      try {
        ProjectJdkImpl jdk = (ProjectJdkImpl) JavaAwareProjectJdkTableImpl.getInstanceEx().getInternalJdk().clone();
        jdk.setName("JDK");
        return jdk;
      } catch (CloneNotSupportedException e) {
        System.out.println("cloning not supported: " + e);
        return null;
      }
    }

    @Override
    public void configureModule(Module module, ModifiableRootModel model, ContentEntry contentEntry) {
      model.getModuleExtension(LanguageLevelModuleExtension.class).setLanguageLevel(LanguageLevel.JDK_1_6);
    }
  }
  static ProjectDesc desc = new ProjectDesc();

  @Override @NotNull
  public LightProjectDescriptor getProjectDescriptor() {
    return desc;
  }

  @Override
  protected String getBasePath() {
    return System.getProperty("data.dir");
  }

  private Eddy makeEddy(@Nullable String special) {
    EnvironmentProcessor.clearGlobalEnvironment();
    System.out.println("Document:");
    System.out.println(myFixture.getEditor().getDocument().getCharsSequence());
    final Eddy eddy = new Eddy(myFixture.getProject());
    eddy.process(myFixture.getEditor(),special);
    return eddy;
  }

  private Eddy setupEddy(@Nullable String special, String... filename) {
    myFixture.configureByFiles(filename);
    return makeEddy(special);
  }

  private Eddy setupEddy(@Nullable String special, final String filename) {
    pushScope("setup eddy");
    try {
      myFixture.configureByFile(filename);
      return makeEddy(special);
    } finally { popScope(); }
  }

  private void dumpResults(final Eddy eddy, final String special) {
    System.out.println("results:");
    final List<Alt<List<String>>> results = eddy.getResults();
    final List<String> strings = eddy.getResultStrings();
    final String sep = "  -------------------------------";
    for (int i=0;i<results.size();i++) {
      final Alt<List<String>> r = results.get(i);
      final String s = strings.get(i);
      if (i >= 4 && (special==null || !special.equals(s))) continue;
      if (i > 0) System.out.println(sep);
      System.out.println("  " + s);
      System.out.println("  " + r);
      System.out.println(JavaScores.ppretty(r.dp()).prefixed("  "));
    }
  }

  private void checkResult(Eddy eddy, String expected) {
    dumpResults(eddy,expected);
    assertTrue("eddy did not find correct solution: " + expected, eddy.getResultStrings().contains(expected));
  }

  private void checkBest(Eddy eddy, String best, double margin) {
    dumpResults(eddy,best);
    final List<String> ss = eddy.getResultStrings();
    final String got = ss.isEmpty() ? "<none>" : ss.get(0);
    assertTrue("eddy wanted best = "+best+", got = "+got, best.equals(got));
    if (ss.size() >= 2) {
      final List<Alt<List<String>>> rs = eddy.getResults();
      final double p0 = rs.get(0).p(),
                   p1 = rs.get(1).p();
      final String m = "wanted margin "+margin+", got "+p1+" / "+p0+" = "+p1/p0;
      System.out.println(m);
      assertTrue(m, p1/p0 < margin);
    }
  }

  private void checkPriority(Eddy eddy, String high, String lo) {
    dumpResults(eddy,null);
    double phi = 0, plo = 0;
    final List<String> strings = eddy.getResultStrings();
    for (int i = 0; i < strings.size(); ++i) {
      double p = eddy.getResults().get(i).p();
      if (strings.get(i).equals(high))
        phi = p;
      else if (strings.get(i).equals(lo))
        plo = p;
    }
    assertTrue("eddy found " + lo + " likelier (" + plo + ") than " + high + " (" + phi + "), but shouldn't.", plo < phi);
  }

  // actual tests
  public void testCreateEddy() throws Exception {
    for (int i = 0; i < 2; i++) {
      System.out.println("iteration " + i);
      setupEddy(null,"dummy.java");
    }
  }

  public void testProbLE1() {
    EnvironmentProcessor.clearGlobalEnvironment();
    Eddy eddy = setupEddy(null,"denote_x.java");
    Base.checkEnv(eddy.getEnv());
    for (Scores.Alt<List<String>> result : eddy.getResults())
      assertTrue("Probability > 1", result.p() <= 1.0);
  }

  public void testTypeVar() {
    Eddy eddy = setupEddy(null,"typeVar.java");
    int As = 0, Bs = 0, Cs = 0;
    for (Item i : eddy.getEnv().allItems()) {
      final String n = i.name();
      if      (n.equals("Avar")) As++;
      else if (n.equals("Bvar")) Bs++;
      else if (n.equals("Cvar")) Cs++;
    }
    System.out.println("As "+As+", Bs "+Bs+", Cs "+Cs);
    assert As==1;
    assert Bs==1;
    assert Cs==0;
  }

  public void testImplicitConstructor() {
    Eddy eddy = setupEddy(null,"ConstructorTest.java");
    boolean Bc = false, Cc = false;
    for (Item i : eddy.getEnv().allItems()) {
      if (!(i instanceof Items.ConstructorItem))
        continue;
      if (((Items.ConstructorItem) i).parent().name().equals("A") || ((Items.ConstructorItem) i).parent().name().equals("B") || ((Items.ConstructorItem) i).parent().name().equals("C"))
        System.out.println("found constructor " + i.name() + " (" + i.qualifiedName() + ") for class " + ((Items.ConstructorItem) i).parent().name() + " info " + ((Items.ConstructorItem) i).params());
      if (i.name().equals("A"))
        throw new AssertionError("found constructor" + i.print() + " which should be private and inAccessible");
      if (i.name().equals("B") && ((Items.ConstructorItem) i).params().isEmpty())
        throw new AssertionError("found constructor" + i.print() + " which is not implicitly declared (another constructor is)");
      if (i.name().equals("B") && ((Items.ConstructorItem) i).params().contains(Types.IntType$.MODULE$))
        Bc = true;
      if (i.name().equals("C"))
        Cc = true;
    }
    assertTrue("constructor (B) not in environment", Bc);
    assertTrue("implicitly defined constructor (C) not in environment", Cc);
  }

  public void testProject() {
    EnvironmentProcessor.clearGlobalEnvironment();
    Eddy eddy = setupEddy(null,"dummy.java",
                          "JSON-java/JSONObject.java",
                          "JSON-java/CDL.java",
                          "JSON-java/Cookie.java",
                          "JSON-java/CookieList.java",
                          "JSON-java/HTTP.java",
                          "JSON-java/HTTPTokener.java",
                          "JSON-java/JSONArray.java",
                          "JSON-java/JSONException.java",
                          "JSON-java/JSONML.java",
                          "JSON-java/JSONString.java",
                          "JSON-java/JSONStringer.java",
                          "JSON-java/JSONTokener.java",
                          "JSON-java/JSONWriter.java",
                          "JSON-java/Kim.java",
                          "JSON-java/Property.java",
                          "JSON-java/README",
                          "JSON-java/XML.java",
                          "JSON-java/XMLTokener.java");
    Base.checkEnv(eddy.getEnv());
  }

  public void testPartialEditTypeConflict() {
    Eddy eddy = setupEddy(null,"partialEditTypeConflict.java");
    checkResult(eddy, "List<NewNewNewType> = new ArrayList<NewNewNewType>();");
  }

  public void testPartialEditTypeConflictPriority() {
    Eddy eddy = setupEddy(null,"partialEditTypeConflict.java");
    // because our cursor is hovering at NewType, this is the one we edited, so it should be higher probability
    checkPriority(eddy, "List<NewNewNewType> = new ArrayList<NewNewNewType>()", "List<OldOldOldType> = new ArrayList<OldOldOldType>()");
  }

  public void testFizz() {
    final String best = "fizz(\"s\", x, q);";
    Eddy eddy = setupEddy(best,"fizz.java");
    checkBest(eddy,best,.9);
  }
}
