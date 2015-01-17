package tarski

import tarski.Mods.Final
import utility.Locations._
import utility.Utility._
import tarski.AST._
import tarski.Base._
import tarski.Denotations._
import tarski.Environment.{Env, PlaceInfo}
import tarski.Items._
import tarski.JavaScores._
import tarski.Lexer._
import tarski.Operators._
import tarski.Pretty._
import tarski.Scores._
import tarski.Tarski.fix
import tarski.TestUtils._
import tarski.Tokens._
import tarski.Types._
import org.testng.annotations.Test
import org.testng.AssertJUnit._
import scala.annotation.tailrec

class TestDen {
  // Default to an empty local environment
  implicit val env = localEnvWithBase()

  def fp(p: Prob) = pp(p) + (if (!trackProbabilities) "" else s"\n${ppretty(p).prefixed("  ")}")

  def testHelper[A](input: String, best: Env => A, margin: Double = .9)(implicit env: Env, convert: A => List[Stmt]): Unit = {
    val fixes = fix(lex(input))
    fixes.strict match {
      case e:EmptyOrBad => throw new RuntimeException(s"Denotation test failed:\ninput: $input\n"+e.error.prefixed("error: "))
      case Best(p,(env,s),rest) =>
        val b = convert(best(env))
        def sh(s: List[Stmt]) = show(s)(prettyStmts(_)(env))
        if (b != s) {
          val ep = probOf(fixes,env => convert(best(env)))
          throw new AssertionError(s"Denotation test failed:\ninput: $input" +
                                   s"\nexpected: ${sh(b)}\nactual  : ${sh(s)}" +
                                   s"\nexpected p = ${fp(ep)}" +
                                   s"\nactual   p = ${fp(p)}" +
                                   s"\nexpected (full): $b\nactual   (full): $s")
        } else if (!rest.below(margin*pp(p))) {
          val n = rest.stream.head
          throw new AssertionError(s"Denotation test failed:\ninput: $input" +
                                   s"\nwanted margin $margin, got ${pp(n.dp)} / ${pp(p)} = ${pp(n.dp) / pp(p)}" +
                                   s"\nbest: ${sh(b)}\nnext: ${sh(n.x._2)}" +
                                   s"\nbest p = ${fp(p)}" +
                                   s"\nnext p = ${fp(n.dp)}")
        } else { // Make sure pretty printing works
          sh(b); ()
        }
    }
  }

  def test[A](input: String, best: A, margin: Double = .9)(implicit env: Env, c: A => List[Stmt]): Unit =
    testHelper(input, env => best, margin=margin)
  def test[A](input: String, x: Name, best: Local => A)(implicit env: Env, c: A => List[Stmt]): Unit =
    testHelper(input, env => best(env.exactLocal(x)))
  def test[A](input: String, x: Name, y: Name, best: (Local,Local) => A)(implicit env: Env, c: A => List[Stmt]): Unit =
    testHelper(input, env => best(env.exactLocal(x),env.exactLocal(y)))

  // Ensure that all options have probability at most bound
  def testFail(input: String, bound: Double = -1)(implicit env: Env): Unit = {
    @tailrec def checkFail[A,B](s: Scored[A], bound: Double = -1)(f: A => B): Unit = if (s.p > bound) s match {
      case s:LazyScored[A] => checkFail(s force bound,bound)(f)
      case Best(p,x,_) => throw new AssertionError(s"\nExpected probability <= $bound, got\n  $p : ${f(x)}")
      case _:EmptyOrBad => ()
    }
    checkFail(fix(lex(input)),bound)(_._2)
  }

  // Ensure that an option doesn't appear
  def testAvoid[A](input: String, bad: A)(implicit env: Env, convert: A => List[Stmt]): Unit = {
    val b = convert(bad)
    val p = probOf(fix(lex(input)),b)
    if (pp(p) >= 0)
      throw new AssertionError(s"Avoidance test failed:\ninput: $input" +
                               s"\nbad: ${show(b)}" +
                               s"\nbad p = ${fp(p)}" +
                               s"\nbad (full): $b")
  }

  def probOf(s: Scored[(Env,List[Stmt])], a: Env => List[Stmt]): Prob = {
    def loop(s: Stream[Alt[(Env,List[Stmt])]]): Prob =
      if (s.isEmpty) Prob("not found",-1)
      else {
        val (env,ss) = s.head.x
        if (a(env) == ss) s.head.dp
        else loop(s.tail)
      }
    loop(s.stream)
  }
  def probOf(s: Scored[(Env,List[Stmt])], a: List[Stmt]): Prob =
    probOf(s,env => a)

  def assertFinal(v: Local) =
    assert(v.isFinal)

  @Test
  def assignExp(): Unit = {
    val x = NormalLocal("x",IntType,isParameter=false,isFinal=false)
    implicit val env = localEnv(x)
    test("x = 1", AssignExp(None,x,1))
  }

  @Test
  def assignExpFinal(): Unit = {
    val x = NormalLocal("x",IntType,isParameter=false,isFinal=true)
    implicit val env = localEnv(x)
    testFail("x = 1")
  }

  @Test
  def longLit() = {
    val x = NormalLocal("x",LongType,isParameter=false,isFinal=false)
    implicit val env = localEnv(x)
    test("x = 2l", AssignExp(None,x,LongLit(2,"2l")))
  }

  @Test
  def bigIntLit() = {
    val x = NormalLocal("x",LongType,isParameter=false,isFinal=false)
    implicit val env = localEnv(x)
    val big = 1099511627776L
    test(s"x = $big", AssignExp(None,x,LongLit(big,s"${big}L")))
  }

  @Test
  def variableStmt() =
    test("x = 1", "x", x => VarStmt(IntType,(x,1)))

  @Test
  def arrayVariableStmtCurly() =
    test("x = {1,2,3,4}", "x", x => VarStmt(ArrayType(IntType),(x,ArrayExp(IntType,List(1,2,3,4)))))

  @Test
  def arrayVariableStmtParen() =
    test("x = (1,2,3,4)", "x", x => VarStmt(ArrayType(IntType),(x,ArrayExp(IntType,List(1,2,3,4)))))

  @Test
  def arrayVariableStmtBare() =
    test("x = 1,2,3,4", "x", x => VarStmt(ArrayType(IntType),(x,ArrayExp(IntType,List(1,2,3,4)))))

  @Test
  def arrayVariableStmtBrack() =
    test("x = [1,2,3,4]", "x", x => VarStmt(ArrayType(IntType),(x,ArrayExp(IntType,List(1,2,3,4)))))

  @Test
  def arrayLiteralAssign(): Unit = {
    val x = NormalLocal("x",ArrayType(IntType),isParameter=false,isFinal=false)
    implicit val env = localEnv(x)
    test("x = {1,2,3}", AssignExp(None,x,ArrayExp(IntType,List(1,2,3))))
  }

  @Test
  def arrayLiteral(): Unit = {
    val Main = NormalClassItem("Main",LocalPkg,Nil,ObjectType,Nil)
    val f = NormalMethodItem("f",Main,Nil,VoidType,List(ArrayType(IntType)),isStatic=true)
    implicit val env = Env(Array(Main,f),Map((Main,2),(f,2)),PlaceInfo(f))
    test("f({1,2,3,4})", ApplyExp(f,List(ArrayExp(IntType,List(1,2,3,4))),auto=false))
  }

  @Test def arrayType() = {
    implicit val env = localEnvWithBase()
    test("int[] x = {1,2,3}", "x", x => VarStmt(ArrayType(IntType),(x,ArrayExp(IntType,List(1,2,3)))))
  }

  @Test def noIndex() = testFail("x = 1[]")

  @Test
  def makeAndSet() =
    test("x = 1; x = 2", "x", x =>
      List(VarStmt(IntType,(x,1)),
           ExpStmt(AssignExp(None,x,2))))

  @Test
  def indexExp(): Unit = {
    val x = NormalLocal("x",ArrayType(CharType),isParameter=false,isFinal=true)
    implicit val env = localEnv(x)
    test("""x[4] = '\n'""", AssignExp(None,IndexExp(x,4),'\n'))
  }

  @Test
  def nestedIndexExpBrack(): Unit = {
    val x = NormalLocal("x",ArrayType(ArrayType(CharType)),isParameter=false,isFinal=true)
    implicit val env = localEnv(x)
    test("""x[4,5] = x[2][5]""", AssignExp(None, IndexExp(IndexExp(x,4),5), IndexExp(IndexExp(x,2),5)))
  }

  @Test
  def nestedIndexExpJuxt(): Unit = {
    val x = NormalLocal("x",ArrayType(ArrayType(CharType)),isParameter=false,isFinal=true)
    implicit val env = localEnv(x)
    test("""x 4 5 = x 2 5""", AssignExp(None, IndexExp(IndexExp(x,4),5), IndexExp(IndexExp(x,2),5)))
  }

  @Test
  def nestedIndexExpMixed(): Unit = {
    val x = NormalLocal("x",ArrayType(ArrayType(CharType)),isParameter=false,isFinal=true)
    implicit val env = Env(Array(x), Map((x,1)))
    test("""x{4,5} = x{2}[5]""", AssignExp(None, IndexExp(IndexExp(x,4),5), IndexExp(IndexExp(x,2),5)))
  }

  @Test
  def nestedIndexExpParen(): Unit = {
    val x = NormalLocal("x",ArrayType(ArrayType(CharType)),isParameter=false,isFinal=true)
    implicit val env = localEnv(x)
    test("""x(4,5) = x(2)(5)""", AssignExp(None, IndexExp(IndexExp(x,4),5), IndexExp(IndexExp(x,2),5)))
  }

  @Test
  def indexOpExp(): Unit = {
    val x = NormalLocal("x",ArrayType(CharType),isParameter=false,isFinal=true)
    implicit val env = localEnv(x)
    test("""x[4] *= '\n'""", AssignExp(Some(MulOp), IndexExp(x,4), '\n'))
  }

  @Test
  def mapExp(): Unit = {
    val main = NormalClassItem("Main",LocalPkg,Nil,ObjectType,Nil)
    val f = NormalMethodItem("f",main,Nil,FloatType,List(ArrayType(IntType)),isStatic=true)
    val x = NormalLocal("x",ArrayType(DoubleType),isParameter=false,isFinal=true)
    val y = NormalLocal("y",ArrayType(DoubleType),isParameter=false,isFinal=false)
    implicit val env = Env(Array(main,f,x,y), Map((main,2),(f,2),(x,1),(y,1)),PlaceInfo(f))
    test("y = f(x)", Nil)
    notImplemented
  }

  @Test
  def cons(): Unit = {
    implicit val env = localEnvWithBase()
    test("x = Object()", "x", x => VarStmt(ObjectType,(x,ApplyExp(NewDen(None,ObjectConsItem),Nil,auto=false))))
  }

  @Test
  def genericConsObject(): Unit = {
    val T = SimpleTypeVar("T")
    lazy val A: ClassItem = NormalClassItem("A",LocalPkg,List(T),constructors=Array(AC))
    lazy val AC = NormalConstructorItem(A,Nil,List(T))
    implicit val env = localEnvWithBase().extend(Array(A,AC),Map((A,3),(AC,3)))
    // should result in A<Object> x = new A<Object>(new Object());
    test("x = A(Object())", "x", x => VarStmt(A.generic(List(ObjectType)),
      (x,ApplyExp(NewDen(None,AC,Some(List(ObjectType))),
                  List(ApplyExp(NewDen(None,ObjectConsItem),Nil,auto=false)),auto=false))))
  }

  @Test
  def varArray() =
    test("int x[]", "x", x => VarStmt(IntType,List((x,1,None))))

  @Test
  def varArrayInit() =
    test("int x[] = {1,2,3}", "x", x => VarStmt(IntType,List((x,1,Some(ArrayExp(IntType,List(1,2,3)))))))

  @Test
  def nullInit() =
    test("x = null", "x", x => VarStmt(ObjectType,(x,NullLit)))

  @Test
  def inheritanceShadowing(): Unit = {
    /* corresponding to
      class Q {}
      class R {}

      class X {
        Q f;
      }
      class Y extends X {
        R f;
      }
      class Z {
        void m(Q d) {}
      }

      Y y;
      m(f); // should resolve to m( ((X)y).f )
     */

    val Q = NormalClassItem("Q", LocalPkg, Nil, ObjectType, Nil)
    val R = NormalClassItem("R", LocalPkg, Nil, ObjectType, Nil)

    val X = NormalClassItem("X", LocalPkg, Nil, ObjectType, Nil, false, Set("f"))
    val Xf = NormalFieldItem("f",Q.simple,X,true)
    val Y = NormalClassItem("Y", LocalPkg, Nil, X.simple, Nil, false, Set("f"))
    val Yf = NormalFieldItem("f",R.simple,Y,true)

    val Z = NormalClassItem("Z", LocalPkg, Nil, ObjectType, Nil)
    val m = NormalMethodItem("m", Z, Nil, VoidType, List(Q.simple), false)
    val y = NormalLocal("y",Y.simple,isParameter=false,isFinal=true)
    implicit val env = Env(Array(X,Y,Z,Xf,Yf,m,y), Map((y,1),(m,2)))

    test("m(f)", ApplyExp(LocalMethodDen(m),List(FieldExp(CastExp(X.simple,y),Xf)),auto=false))
  }

  @Test
  def thisToSuper(): Unit = {
    /* corresponding to
      class Q {}
      class R {}

      class X {
        Q f;
      }
      class Y extends X {
        R f;
        void m(Q d) {}

        ...
        m(f); // should resolve to m( super.f )
      }
     */

    val Q = NormalClassItem("Q", LocalPkg, Nil, ObjectType, Nil)
    val R = NormalClassItem("R", LocalPkg, Nil, ObjectType, Nil)

    val X = NormalClassItem("X", LocalPkg, Nil, ObjectType, Nil, false, Set("f"))
    val Xf = NormalFieldItem("f",Q.simple,X,true)
    val Y = NormalClassItem("Y", LocalPkg, Nil, X.simple, Nil, false, Set("f"))
    val Yf = NormalFieldItem("f",R.simple,Y,true)

    val m = NormalMethodItem("m", Y, Nil, VoidType, List(Q.simple), false)
    val This = ThisItem(Y)
    val Super = SuperItem(Y.base)
    implicit val env = Env(Array(X,Y,Xf,Yf,m,This,Super), Map((This,2),(Super,2),(m,2),(Y,2),(Yf,2),(X,3),(Xf,3)))

    test("m(f)", ApplyExp(LocalMethodDen(m),List(FieldExp(SuperExp(Super),Xf)),auto=false))
  }

  @Test
  def thisExp(): Unit = {
    val X = NormalClassItem("X", LocalPkg, Nil, ObjectType, Nil)
    val Xx = NormalFieldItem("x",IntType,X,isFinal=false)
    val x = NormalLocal("x",StringType,isParameter=false,isFinal=false)
    val t = ThisItem(X)
    implicit val env = Env(Array(X,Xx,x,t), Map((x,1),(X,2),(t,2),(Xx,2)))

    test("x = 1", AssignExp(None,FieldExp(ThisExp(t),Xx),1))
  }

  /*
  @Test
  def relativeNames() = {
    val Z = NormalClassItem("Z", LocalPkg, Nil, ObjectType, Nil)
    val X = NormalClassItem("X", LocalPkg, Nil, SimpleClassType(Z), Nil)
    val S = NormalClassItem("S", LocalPkg, Nil, ObjectType, Nil)
    val Y = NormalClassItem("Y", S, Nil, SimpleClassType(X), Nil)
    val t = MethodItem("t", Y, VoidType, Nil)
    val Zx = FieldItem("x", IntType, Z)
    val Xx = FieldItem("x", IntType, X)
    val Sx = FieldItem("x", IntType, S)
    val Yx = FieldItem("x", IntType, Y)
    val x = Local("x", IntType)

    val Zxden =

    assertEquals(tokens(Zx), List(LParenTok(),LParenTok(), IdentTok("Z"), RParenTok(), ThisTok(), RParenTok(), DotTok(), IdentTok("x"))) // ((Z)this).x
    assertEquals(tokens(Xx), List(SuperTok(), DotTok(), IdentTok("x"))) // super.x
    assertEquals(tokens(Sx), List(IdentTok("S"), DotTok(), ThisTok(), DotTok(), IdentTok("x"))) // S.this.x
    assertEquals(tokens(Yx), List(ThisTok(), DotTok(), IdentTok("x"))) // this.x
    assertEquals(tokens(x), List(IdentTok("x"))) // Local x
  }
  */

  @Test
  def byteLiteral() = test("byte x = 3", "x", x => VarStmt(ByteType,(x,IntLit(3,"3"))))

  @Test
  def intLiteral() = test("int x = 3", "x", x => VarStmt(IntType,(x,IntLit(3,"3"))))

  @Test
  def parens() = test("x = (1)", "x", x => VarStmt(IntType,(x,ParenExp(1))))

  // If statements
  val t = BooleanLit(true)
  val f = NonImpExp(NotOp,t)
  val e = EmptyStmt
  val h = HoleStmt
  def testX(input: String, best: (Stmt,Stmt) => Stmt) = {
    val x = NormalLocal("x",IntType,isParameter=false,isFinal=false)
    implicit val env = extraEnv.extendLocal(Array(x))
    test(input,best(AssignExp(None,x,1),AssignExp(None,x,2)))
  }
  @Test def ifStmt()       = test ("if (true);", IfStmt(t,e))
  @Test def ifHole()       = test ("if (true)", IfStmt(t,h))
  @Test def ifBare()       = test ("if true;", IfStmt(t,e), margin=.99)
  @Test def ifBareHole()   = test ("if true", IfStmt(t,h))
  @Test def ifThen()       = test ("if true then;", IfStmt(t,e))
  @Test def ifThenHole()   = test ("if true then", IfStmt(t,h))
  @Test def ifThenParens() = test ("if (true) then", IfStmt(t,h))
  @Test def ifElse()       = testX("if (true) x=1 else x=2", (a,b) => IfElseStmt(t,a,b))
  @Test def ifElseHole()   = test ("if (true) else", IfElseStmt(t,h,h))
  @Test def ifThenElse()   = testX("if true then x=1 else x=2", (a,b) => IfElseStmt(t,a,b))

  // While and do
  @Test def whileStmt()       = test("while (true);", WhileStmt(t,e))
  @Test def whileHole()       = test("while (true)", WhileStmt(t,h))
  @Test def whileBare()       = test("while true;", WhileStmt(t,e))
  @Test def whileBareHole()   = test("while true", WhileStmt(t,h))
  @Test def untilHole()       = test("until true", WhileStmt(f,h))
  @Test def doWhile()         = test("do; while (true)", DoStmt(e,t))
  @Test def doWhileHole()     = test("do while (true)", DoStmt(h,t))
  @Test def doWhileBare()     = test("do; while true", DoStmt(e,t))
  @Test def doWhileHoleBare() = test("do while true", DoStmt(h,t))
  @Test def doUntil()         = test("do until true", DoStmt(h,f))

  // For
  @Test def forever()     = test("for (;;);", ForStmt(Nil,None,Nil,EmptyStmt))
  @Test def foreverHole() = test("for (;;)", ForStmt(Nil,None,Nil,HoleStmt))
  @Test def forSimple()   = test("for (x=7;true;x++)", "x", x =>
    ForStmt(VarStmt(IntType,(x,7)),Some(t),ImpExp(PostIncOp,x),HoleStmt))
  @Test def forTwo()      = test("for (x=7,y=8.1;true;)", "x", "y", (x,y) =>
    BlockStmt(List(VarStmt(IntType,(x,7)),
                   VarStmt(DoubleType,(y,8.1)),
                   ForStmt(Nil,Some(t),Nil,HoleStmt))))
  @Test def foreach()     = test("for (x : 1,2)", "x", x => {
    assertFinal(x); ForeachStmt(IntType,x,ArrayExp(IntType,List(1,2)),HoleStmt) })

  @Test def sideEffects() = {
    lazy val X: ClassItem = NormalClassItem("X",LocalPkg,constructors=Array(cons))
    lazy val cons = NormalConstructorItem(X,Nil,Nil)
    val f = NormalMethodItem("f",X,Nil,VoidType,Nil,true)

    implicit val env = Env(Array(X,cons,f),Map((f,2),(X,3)),PlaceInfo(f))
    // We are not allowed to discard the possible side effects in the X constructor.
    test("X().f();", List(ExpStmt(ApplyExp(MethodDen(ApplyExp(NewDen(None,cons),Nil,auto=false),f),Nil,auto=false))))
  }

  @Test def sideEffectsCons() = {
    lazy val X: ClassItem = NormalClassItem("X",LocalPkg,constructors=Array(cons))
    lazy val cons = NormalConstructorItem(X,Nil,Nil)
    val Y = NormalClassItem("Y",X,Nil)
    implicit val env = localEnv(X,cons,Y)
    test("X().Y y", "y", y => List(ExpStmt(ApplyExp(NewDen(None,cons),Nil,auto=false)),VarStmt(Y,List((y,0,None)))))
  }

  @Test def sideEffectsFail() = {
    val x = NormalLocal("x",IntType,isParameter=false,isFinal=true)
    implicit val env = localEnv(x)
    testFail("x")
  }

  @Test def sideEffectsSplit() = {
    val x = NormalLocal("x",IntType,isParameter=false,isFinal=false)
    implicit val env = extraEnv.extendLocal(Array(x))
    test("true ? x = 1 : (x = 2)", IfElseStmt(true,AssignExp(None,x,1),AssignExp(None,x,2)))
  }

  @Test def trueTypo() = {
    implicit val env = localEnvWithBase()
    test("x = tru", "x", x => VarStmt(BooleanType,(x,true)))
  }

  // Synchronized
  @Test def sync() = test("synchronized null", SyncStmt(NullLit,HoleStmt))

  // inserting a cast to bool
  @Test def insertIntComparison() = test("if 1 then;", IfStmt(BinaryExp(NeOp,1,0), e))
  @Test def insertRefTypeComparison() = {
    val o = NormalLocal("o",ObjectType,isParameter=false,isFinal=true)
    implicit val env = localEnvWithBase(o)
    test("if o then;", IfStmt(BinaryExp(NeOp,o,NullLit), e))
  }

  @Test def shuffleArgs() = {
    val X = NormalClassItem("X")
    val f = NormalMethodItem("f",X,Nil,VoidType,List(X.simple,DoubleType,StringType,BooleanType),isStatic=true)
    val x = NormalLocal("x",X.simple,isParameter=false,isFinal=true)
    val d = NormalLocal("d",DoubleType,isParameter=false,isFinal=true)
    val s = NormalLocal("s",StringType,isParameter=false,isFinal=true)
    val b = NormalLocal("b",BooleanType,isParameter=false,isFinal=true)
    implicit val env = Env(Array(X,f),Map((X,3),(f,2)),PlaceInfo(f)).extendLocal(Array(x,d,s,b))
    test("f(s,b,d,x)", ApplyExp(f,List(x,d,s,b),auto=false))
  }

  @Test def omittedQualifier(): Unit = {
    val P = Package("com","P")
    val Z = NormalClassItem("Z",P)
    val Y = NormalClassItem("Y")
    val X = NormalClassItem("X")
    val Zx = NormalStaticFieldItem("x", BooleanType, Z, isFinal=false)
    val Yx = NormalStaticFieldItem("x", BooleanType, Y, isFinal=false)
    val Xx = NormalStaticFieldItem("x", BooleanType, X, isFinal=false)
    val f = NormalMethodItem("f", X, Nil, VoidType, Nil, isStatic=true)
    val x = NormalLocal("x", BooleanType, isParameter=false, isFinal=false)
    implicit val env = baseEnv.extend(Array(P,Z,Zx,Y,Yx,X,Xx,f,x),Map((Y,3),(X,3),(Xx,2),(f,2),(x,1))).move(PlaceInfo(f))
    val fixes = fix(lex("x = true"))
    // make sure that local x is the most likely, then X.x (shadowed, but in scope), then Y.x (not in scope), then Z.x (different package)
    def set(e: Exp): List[Stmt] = AssignExp(None,e,true)
    val px  = pp(probOf(fixes,set(x)))
    val pXx = pp(probOf(fixes,set(FieldExp(None,Xx))))
    val pYx = pp(probOf(fixes,set(FieldExp(None,Yx))))
    val pZx = pp(probOf(fixes,set(FieldExp(None,Zx))))

    println(s"probabilities:\n  x   : $px\n  X.x : $pXx\n  Y.x : $pYx\n  Z.x : $pZx")
    assertTrue(s"All probabilities should be positive",px > 0 && pXx > 0 && pYx > 0 && pZx > 0)

    assertTrue(s"Local variable not more likely (x : $px) than shadowed field (X.x : $pXx)", px > pXx)
    assertTrue(s"Same class static field not more likely (X.x : $pXx) than other class field (Y.x : $pYx)", pXx > pYx)
    // The next may not be what we want. only learning will really figure this out
    assertTrue(s"Other class field not more likely (Y.x : $pYx) than other package field (Z.x : $pZx)", pYx > pZx)
  }

  @Test def capture() = {
    val T = SimpleTypeVar("T")
    val S = SimpleTypeVar("S")
    val A = NormalClassItem("A",LocalPkg,List(T))
    val B = NormalClassItem("B")
    val F = NormalClassItem("F")
    val f = NormalMethodItem("f",F,List(S),VoidType,List(S),isStatic=true)
    for (w <- List(WildSub(),WildSub(B),WildSuper(B))) {
      val t = A.generic(List(w))
      val x = NormalLocal("x",t,isParameter=false,isFinal=true)
      implicit val env = localEnv(A,x,F,f)
      test("f(x) // "+w,List(ExpStmt(ApplyExp(TypeApply(f,List(t),hide=true),List(x),auto=false)),
                             CommentStmt(EOLCommentTok("// "+w))))
    }
  }

  // Mismatched parentheses
  @Test def mismatchedParens() = {
    lazy val X: ClassItem = NormalClassItem("X",constructors=Array(cons))
    lazy val cons = NormalConstructorItem(X,Nil,Nil)
    val f = NormalMethodItem("f",X,Nil,VoidType,Nil,isStatic=false)
    implicit val env = localEnv(X,cons,f)
    val best = ApplyExp(MethodDen(ParenExp(ApplyExp(NewDen(None,cons),Nil,auto=false)),f),Nil,auto=false)
    test("((X()).f()",best,margin=1)
    test("((X()).f(",best)
  }

  @Test def omittedEmptyCallParens() = {
    val X = NormalClassItem("X")
    val f = NormalMethodItem("f",X,Nil,VoidType,Nil,isStatic=false)
    implicit val env = localEnv(X,f)
    test("f", ApplyExp(LocalMethodDen(f),Nil,auto=true))
  }

  @Test def constructorForward(): Unit = {
    lazy val Y: ClassItem = NormalClassItem("Y", LocalPkg, constructors = Array(Yc))
    lazy val Yc = NormalConstructorItem(Y,Nil,Nil)
    lazy val X: ClassItem = NormalClassItem("X", LocalPkg, Nil, Y, constructors = Array(Xc,Xc2))
    lazy val Xc = NormalConstructorItem(X, Nil, Nil)
    lazy val Xc2 = NormalConstructorItem(X, Nil, List(IntType))
    val This = ThisItem(X)
    val Super = SuperItem(Y)
    implicit val env = Env(Array(Y,Yc,X,Xc,This,Super),
                           Map((Xc,2),(Xc2,2),(X,2),(Y,3),(Yc,3),(This,2),(Super,2)),
                           PlaceInfo(Xc2))
    test("this()", ApplyExp(ForwardDen(This,Xc),Nil,auto=false))
    test("super()", ApplyExp(ForwardDen(Super,Yc),Nil,auto=false))
    // TODO: This should only work as the first statement of a different constructor, which is not tracked by the PlaceInfo right now
  }

  @Test def illegalConstructorForward(): Unit = {
    // cannot forward to constructor outside of constructor
    val Y = NormalClassItem("Y", LocalPkg)
    val Yc = NormalConstructorItem(Y,Nil,Nil)
    val X = NormalClassItem("X", LocalPkg, Nil, Y)
    val Xc = NormalConstructorItem(X, Nil, Nil)
    val f = NormalMethodItem("f", X, Nil, VoidType, Nil, isStatic = false)
    implicit val env = Env(Array(Y,Yc,X,Xc), Map(f->2,Xc->2,X->2,Y->3,Yc->3), PlaceInfo(f))
    testFail("this()")
    testFail("super()")
  }

  def setupGenericClass(): Env = {
    /**
     * equivalent to:
     *
     * class B<A> {}
     *
     * class X<A,BA extends B<A>> {
     *   <T extends Number> A f(T) {
     *     <caret>
     *   }
     * }
     *
     */

    val A2 = SimpleTypeVar("A")
    val B = NormalClassItem("B", LocalPkg, List(A2))

    val A = SimpleTypeVar("A")
    val BA = NormalTypeVar("BA", B.generic(List(A)), Nil)
    val X = NormalClassItem("X", LocalPkg, List(A,BA))
    val This = ThisItem(X)

    val T = NormalTypeVar("T", NumberItem.simple, Nil)
    val f = NormalMethodItem("f", X, List(T), A, List(T), isStatic=false)
    baseEnv.extend(Array(A,A2,B,BA,X,T,f,This), Map((A,2),(BA,2),(X,2),(This,2),(T,2),(f,2))).move(PlaceInfo(f))
  }

  @Test def genericClass(): Unit = {
    implicit val env = setupGenericClass()
    val X = env.allLocalItems.find(_.name == "X").get.asInstanceOf[NormalClassItem]
    val B = env.allLocalItems.find(_.name == "B").get.asInstanceOf[NormalClassItem]
    test("X<String,B<String>> x = null", "x", x => VarStmt(X.generic(List(StringType,B.generic(List(StringType)))), List((x, 0, Some(NullLit)))))
  }

  @Test def genericMethod(): Unit = {
    implicit val env = setupGenericClass()
    val f = env.allLocalItems.find(_.name == "f").get.asInstanceOf[NormalMethodItem]
    val This = env.allLocalItems.find(_.isInstanceOf[ThisItem]).get.asInstanceOf[ThisItem]
    test("""this.<Integer>f(7)""",ApplyExp(TypeApply(MethodDen(This,f),List(IntType.box),hide=false),List(7),auto=false))
    test("""<Integer>f(7)""",     ApplyExp(TypeApply(LocalMethodDen(f),List(IntType.box),hide=false),List(7),auto=false))
    test("""f<Integer>(7)""",     ApplyExp(TypeApply(LocalMethodDen(f),List(IntType.box),hide=false),List(7),auto=false))
    // These three fail because f's type argument extends Number
    def bad(s: String) = testFail(s,bound=1e-3)
    bad("""this.<String>f("test")""")
    bad("""<String>f("test")""")
    bad("""f<String>("test")""")
  }

  @Test def typeApply(): Unit = {
    /*
     * class S {}
     * class T {}
     * class U {}
     * class X<A extends S, B extends T, C extends U> {}
     * class Y {
     *   void f() {
     *     X<S,T,U> x<caret>
     *   }
     * }
     */
    val S = NormalClassItem("S",LocalPkg)
    val T = NormalClassItem("T",LocalPkg)
    val U = NormalClassItem("U",LocalPkg)
    val AS = NormalTypeVar("A",S,Nil)
    val BT = NormalTypeVar("B",T,Nil)
    val CU = NormalTypeVar("C",U,Nil)
    val X = NormalClassItem("X",LocalPkg,List(AS,BT,CU))
    val Y = NormalClassItem("Y",LocalPkg)
    val f = NormalMethodItem("f",Y,Nil,VoidType,Nil,isStatic=false)

    implicit val env = Env(Array(S,T,U,AS,BT,CU,X,Y,f), Map((X,3),(Y,2),(f,2),(S,3),(T,3),(U,3)), PlaceInfo(f))
    // Until we make some fiddling happen (in which case this test should test probabilities), only A<S,T,U> should work.
    test("X<S,T,U> x", "x", x => VarStmt(X.generic(List(S,T,U)),List((x,0,None))))
    test("A<S,S,U> x", "x", x => VarStmt(X.generic(List(S,T,U)),List((x,0,None)))) // Unlikely, but should work
    def bad(s: String) = testFail(s,bound=1e-4)
    bad("A<S,S,S> x")
    bad("A<S,S,T> x")
    bad("A<S,S,U> x")
    bad("A<S,T,S> x")
    bad("A<S,T,T> x")
    bad("A<S,U,S> x")
    bad("A<S,U,T> x")
    bad("A<S,U,U> x")
    bad("A<T,S,S> x")
    bad("A<T,S,T> x")
    bad("A<T,S,U> x")
    bad("A<T,T,S> x")
    bad("A<T,T,T> x")
    bad("A<T,T,U> x")
    bad("A<T,U,S> x")
    bad("A<T,U,T> x")
    bad("A<T,U,U> x")
    bad("A<U,S,S> x")
    bad("A<U,S,T> x")
    bad("A<U,S,U> x")
    bad("A<U,T,S> x")
    bad("A<U,T,T> x")
    bad("A<U,T,U> x")
    bad("A<U,U,S> x")
    bad("A<U,U,T> x")
    bad("A<U,U,U> x")
  }

  @Test def boxInt() = {
    implicit val env = localEnvWithBase()
    test("Integer x = 1","x",x => VarStmt(IntType.box,(x,1)))
  }

  @Test def boxByte() = {
    implicit val env = localEnvWithBase()
    test("Byte x = 1","x",x => VarStmt(ByteType.box,(x,1)))
  }

  @Test def fizz() = {
    val A = NormalClassItem("A",LocalPkg)
    val fizz = NormalMethodItem("fizz",A,Nil,IntType,List(StringType,IntType.box,DoubleType.box),isStatic=true)
    val x = NormalLocal("x",IntType,isParameter=false,isFinal=true)
    val q = NormalLocal("q",DoubleType,isParameter=false,isFinal=true)
    implicit val env = baseEnv.extend(Array(A,fizz,x,q),Map(A->1,fizz->1,x->1,q->1)).move(PlaceInfo(fizz))
    test("""fizz "s" x q""",ApplyExp(fizz,List(StringLit("s","\"s\""),x,q),auto=false))
  }

  @Test def shadowedParameter() = {
    val A = NormalClassItem("A",LocalPkg)
    val B = NormalClassItem("B",LocalPkg)
    val C = NormalClassItem("C",LocalPkg)
    val f = NormalMethodItem("f",A,Nil,VoidType,List(B),isStatic=true)
    val bx = NormalLocal("x",B,isParameter=false,isFinal=true)
    val cx = NormalLocal("x",C,isParameter=false,isFinal=true)
    def env(bs: Int, cs: Int) = baseEnv.extend(Array(A,B,C,f,bx,cx),Map(bx->bs,cx->cs)).move(PlaceInfo(f))
    def unit(x: Unit) = x
    unit({ implicit val bad = env(bs=2,cs=1); testFail("f x") })
    unit({ implicit val good = env(bs=1,cs=2); test("f x",ApplyExp(f,List(bx),auto=false)) })
  }

  @Test def memberToInfix() = {
    val A = NormalClassItem("A")
    val f = NormalMethodItem("f",A,Nil,A,List(A),isStatic=false)
    val a = NormalLocal("a",A,isParameter=false,isFinal=true)
    implicit val env = baseEnv.extend(Array(f,a),Map(f->2,a->1)).move(PlaceInfo(f))
    def fa(x: Exp) = ApplyExp(MethodDen(x,f),List(a),auto=false)
    test("a f a",fa(a))
    test("a f a f a",fa(fa(a)))
  }

  @Test def postfix() = {
    val A = NormalClassItem("A")
    val f = NormalMethodItem("f",A,Nil,VoidType,Nil,isStatic=false)
    def M(c: ClassItem): Local = NormalLocal(c.name.toLowerCase,c,isParameter=false,isFinal=true)
    val a = NormalLocal("a",A,isParameter=false,isFinal=true)
    implicit val env = baseEnv.extendLocal(Array(a,f)).move(PlaceInfo(f))
    test("a f",ApplyExp(MethodDen(a,f),Nil,auto=true))
  }

  @Test def largeJuxt() = {
    val A = NormalClassItem("A")
    val X = NormalClassItem("X")
    val Y = NormalClassItem("Y")
    val B = NormalClassItem("B")
    val C = NormalClassItem("C")
    val D = NormalClassItem("D")
    val f = NormalMethodItem("f",A,Nil,B,List(X,Y),isStatic=false)
    val g = NormalFieldItem("g",C,B,isFinal=true)
    val h = NormalMethodItem("h",C,Nil,D,Nil,isStatic=false)
    def M(c: ClassItem): Local = NormalLocal(c.name.toLowerCase,c,isParameter=false,isFinal=true)
    val a = M(A)
    val x = M(X)
    val y = M(Y)
    implicit val env = baseEnv.extendLocal(Array(a,x,y,f,g,h)).move(PlaceInfo(f))
    test("a f y x g h",ApplyExp(MethodDen(FieldExp(ApplyExp(MethodDen(a,f),List(x,y),auto=false),g),h),Nil,auto=true))
  }

  @Test def javascriptStyleMember() = {
    val A = NormalClassItem("A")
    val f = NormalFieldItem("f",A,A,isFinal=false)
    val a = NormalLocal("a",A,isParameter=false,isFinal=true)
    implicit val env = baseEnv.extend(Array(A,f,a), Map(A->2,f->2,a->1))
    for (s <- List("a[f] = a","a f = a","""a["f"] = a"""))
      test(s,AssignExp(None,FieldExp(a,f),a))
    test("a f f f = a",AssignExp(None,FieldExp(FieldExp(FieldExp(a,f),f),f),a))
  }

  @Test def newObject() = test("new Object()",ApplyExp(NewDen(None,ObjectConsItem),Nil,auto=false))
  @Test def newObjectBare() = test("new Object",ApplyExp(NewDen(None,ObjectConsItem),Nil,auto=true))

  @Test def newGeneric() = {
    val T = SimpleTypeVar("T")
    val S = SimpleTypeVar("S")
    val B = NormalClassItem("B",LocalPkg)
    val C = NormalClassItem("C",LocalPkg)
    lazy val A: ClassItem = NormalClassItem("A",LocalPkg,List(T),constructors=Array(cons))
    lazy val cons = NormalConstructorItem(A,List(S),Nil)
    implicit val env = localEnv().extend(Array(A,cons,B,C),Map(A->1,B->1,C->1))
    test("x = new<C>A<B>","x",x =>
      VarStmt(A.generic(List(B)),(x,ApplyExp(TypeApply(NewDen(None,cons,Some(List(B))),List(C),hide=false),Nil,auto=true))))
  }

  @Test def classInPackage() = {
    val P = Package("P")
    lazy val A: ClassItem = NormalClassItem("A",P,constructors=Array(cons))
    lazy val cons = NormalConstructorItem(A,Nil,Nil)
    implicit val env = localEnv().extend(Array(P,A,cons),Map.empty)
    val e = ApplyExp(NewDen(None,cons),Nil,auto=false)
    test("P.A()",e)
    test("new P.A()",e)
  }

  @Test def noEmptyNames(): Unit = {
    val is = localEnvWithBase().exactQuery("")
    if (is.nonEmpty) throw new AssertionError(s"Unexpected empty names: ${is map (_.getClass)}")
  }

  @Test def fieldAccess() = {
    val T = NormalClassItem("T", LocalPkg)
    val x = NormalFieldItem("x", IntType, T, isFinal=false)
    val t = NormalLocal("t", T.simple, isParameter=false, isFinal=true)
    implicit val env = localEnv().extendLocal(Array(T,x), 3).extendLocal(Array(t), 1)
    test("t.x = 1",AssignExp(None, FieldExp(t, x), 1))
  }

  @Test def fixType() = test("int x = 1L","x",x => VarStmt(LongType,(x,1L)))
  @Test def fixGarbageType() = test("garbageGarbageGarbage x = 1L","x",x => VarStmt(LongType,(x,1L)))

  @Test def fixTypeGenericRightToLeft() = {
    val S = SimpleTypeVar("S")
    val T = SimpleTypeVar("T")
    lazy val A: ClassItem = NormalClassItem("A",tparams=List(S))
    lazy val B: ClassItem = NormalClassItem("B",tparams=List(T),base=A.generic(List(T)),constructors=Array(cons))
    lazy val cons = DefaultConstructorItem(B)
    val pre = localEnvWithBase().extendLocal(Array(A,B))
    implicit val env = pre.move(PlaceInfo(pre.place.place,lastEdit=SLoc(22)))
    test("A<Integer> x = new B<Long>","x",x =>
      VarStmt(A.generic(List(LongType.box)),(x,ApplyExp(NewDen(None,cons,Some(List(LongType.box))),Nil,auto=true))))
  }

  @Test def fixTypeGenericLeftToRight() = {
    val S = SimpleTypeVar("S")
    val T = SimpleTypeVar("T")
    lazy val A: ClassItem = NormalClassItem("A",tparams=List(S))
    lazy val B: ClassItem = NormalClassItem("B",tparams=List(T),base=A.generic(List(T)),constructors=Array(cons))
    lazy val cons = DefaultConstructorItem(B)
    val pre = localEnvWithBase().extendLocal(Array(A,B))
    implicit val env = pre.move(PlaceInfo(pre.place.place,lastEdit=SLoc(7)))
    test("A<Integer> x = new B<Long>","x",x =>
      VarStmt(A.generic(List(IntType.box)),(x,ApplyExp(NewDen(None,cons,Some(List(IntType.box))),Nil,auto=true))))
  }

  @Test def fillTypeGeneric() = {
    val S = SimpleTypeVar("S")
    val T = SimpleTypeVar("T")
    lazy val A: ClassItem = NormalClassItem("A",tparams=List(S))
    lazy val B: ClassItem = NormalClassItem("B",tparams=List(T),base=A.generic(List(T)),constructors=Array(cons))
    lazy val cons = DefaultConstructorItem(B)
    implicit val env = localEnvWithBase().extendLocal(Array(A,B))
    test("A<Integer> x = new B","x",x =>
      VarStmt(A.generic(List(IntType.box)),(x,ApplyExp(NewDen(None,cons,Some(List(IntType.box))),Nil,auto=true))))
  }

  @Test def fillTypeTernary() = {
    val S = SimpleTypeVar("S")
    val T = SimpleTypeVar("T")
    val U = SimpleTypeVar("U")
    lazy val A: ClassItem = NormalClassItem("A",tparams=List(S))
    lazy val B: ClassItem = NormalClassItem("B",tparams=List(T),base=A.generic(List(T)),constructors=Array(consB))
    lazy val C: ClassItem = NormalClassItem("C",tparams=List(U),base=A.generic(List(U)),constructors=Array(consC))
    lazy val consB = DefaultConstructorItem(B)
    lazy val consC = DefaultConstructorItem(C)
    val f = NormalLocal("f",BooleanType,isParameter=false,isFinal=true)
    implicit val env = localEnvWithBase().extendLocal(Array(f,A,B,C))
    test("A<Integer> x = f ? new B : (new C)","x",x => {
      val is = List(IntType.box)
      val t = A.generic(is)
      VarStmt(t,(x,CondExp(f,ApplyExp(NewDen(None,consB,Some(is)),Nil,auto=true),
                             ParenExp(ApplyExp(NewDen(None,consC,Some(is)),Nil,auto=true)),t)))
    })
  }

  @Test def nullaryGenericFunction() = {
    val A = NormalClassItem("A")
    val T = SimpleTypeVar("T")
    val f = NormalMethodItem("f",A,List(T),VoidType,Nil,isStatic=true)
    implicit val env = localEnv().extendLocal(Array(f))
    test("f()",ApplyExp(TypeApply(MethodDen(None,f),List(ObjectType),hide=true),Nil,auto=false))
  }

  @Test def autoReturn() = {
    val A = NormalClassItem("A")
    val x = NormalLocal("x",A,isParameter=false,isFinal=true)
    val f = NormalMethodItem("f",NormalClassItem("F"),Nil,A,Nil,isStatic=true)
    implicit val env = Env(Array(A,x),Map(A->2,x->1),PlaceInfo(f))
    test("return",ReturnStmt(x))
  }

  @Test def noLabel() = {
    val pre = localEnv()
    def f(b: Boolean, c: Boolean): Unit = {
      implicit val env = pre.move(PlaceInfo(pre.place.place,breakable=b,continuable=c))
      if (b) test("break",BreakStmt(None)) else testFail("break")
      if (c) test("continue",ContinueStmt(None)) else testFail("continue")
    }
    f(false,false); f(false,true)
    f(true ,false); f(true ,true)
  }
  @Test def label() = {
    val pre = localEnv()
    def f(c: Boolean): Unit = {
      val lab = Label("label",continuable=c)
      implicit val env = pre.extendLocal(Array(lab)).move(PlaceInfo(pre.place.place,breakable=true,continuable=c))
      for (name <- List("label","labl")) {
        test(s"break $name",BreakStmt(Some(lab)))
        if (c) test(s"continue $name",ContinueStmt(Some(lab)))
        else testFail(s"continue $name")
      }
    }
    f(false); f(true)
  }

  @Test def ifNull() = {
    val x = NormalLocal("x",ObjectType,isParameter=false,isFinal=true)
    implicit val env = localEnvWithBase(x)
    test("if (x == null)",IfStmt(BinaryExp(EqOp,x,NullLit),HoleStmt))
  }

  @Test def spuriousTypeArgs() = {
    lazy val A: ClassItem = NormalClassItem("A",constructors=Array(DefaultConstructorItem(A)))
    val T = SimpleTypeVar("T")
    val f = NormalMethodItem("f",A,List(T),VoidType,List(T),isStatic=true)
    val Y = NormalClassItem("Y")
    val y = NormalLocal("y",Y,isParameter=false,isFinal=true)
    implicit val env = localEnvWithBase().extend(Array(A,f,y),Map(A->2,y->1))
    test("A.f(y)",ApplyExp(TypeApply(MethodDen(None,f),List(Y),hide=true),List(y),auto=false))
  }

  @Test def explicitStatic() = {
    // Give A a default constructor make sure eddy doesn't call "new A()" unnecessarily
    lazy val A: ClassItem = NormalClassItem("A",constructors=Array(cons))
    lazy val cons = DefaultConstructorItem(A)
    val f = NormalMethodItem("f",A,Nil,VoidType,Nil,isStatic=true)
    val x = NormalStaticFieldItem("f",IntType,A,isFinal=true)
    val y = NormalLocal("y",IntType,isParameter=false,isFinal=false)
    implicit val env = localEnvWithBase().extend(Array(A,f,x,y),Map(A->2,y->1))
    test("A.f()",ApplyExp(MethodDen(None,f),Nil,auto=false))
    test("y = A.x",AssignExp(None,y,FieldExp(None,x)))
    for (auto <- List(false,true)) {
      val a = ApplyExp(NewDen(None,cons),Nil,auto=auto)
      testAvoid("A.f()",ApplyExp(MethodDen(a,f),Nil,auto=false))
      testAvoid("y = A.x",AssignExp(None,y,FieldExp(a,x)))
    }
  }

  @Test def shadowParameter(): Unit = {
    for (parameter <- List(true, false)) {
      lazy val A: ClassItem = NormalClassItem("A",constructors=Array(cons))
      lazy val cons = DefaultConstructorItem(A)
      val f = NormalMethodItem("f",A,Nil,VoidType,List(IntType),isStatic=true)
      val x = NormalLocal("x",IntType,isParameter=parameter,isFinal=true)
      implicit val env = localEnvWithBase().extend(Array(A,f,x),Map(A->2,f->2,x->2))
      val fixes = fix(lex("int x;"))
      assertEquals(parameter, !fixes.strict.isInstanceOf[EmptyOrBad])
    }
  }

  @Test def if0() = {
    val x = NormalLocal("x",ObjectType,isParameter=false,isFinal=true)
    val b = NormalLocal("b",BooleanType,isParameter=false,isFinal=true)
    implicit val env = localEnvWithBase(x,b)
    test("if (x == 0)",IfStmt(BinaryExp(EqOp,x,NullLit),HoleStmt))
    test("if (0 != x)",IfStmt(BinaryExp(NeOp,NullLit,x),HoleStmt))
    test("if (b != 0)",IfStmt(BinaryExp(NeOp,b,false),HoleStmt))
    test("if (0 == b)",IfStmt(BinaryExp(EqOp,false,b),HoleStmt))
  }

  @Test def comment() = test("x = 1 // blah","x",x => List(VarStmt(IntType,(x,1)),CommentStmt(EOLCommentTok("// blah"))))

  @Test def finalVar() = test("final x = 1;","x",x => VarStmt(IntType,(x,1),List(Final)))
  @Test def finalVarType() = test("final int x = 1;","x",x => VarStmt(IntType,(x,1),List(Final)))
}
