/* TestParse: Unit tests for eddy's ambiguous parser
 *
 * Most of the whole engine tests in TestDen check only the best option,
 * or the fact that the best option is well separated from the second best.
 * However, sometimes we want to check all the parses produced, or verify
 * that a given parse is never produced, etc.  The parsing tests are also
 * a necessity when developing or debugging the grammar and parser generator.
 */

package tarski

import utility.Locations._
import utility.Utility._
import tarski.AST._
import tarski.Arounds._
import tarski.Lexer._
import tarski.Operators._
import tarski.Scores._
import tarski.JavaScores.pp
import tarski.TestUtils._
import tarski.Tokens._
import tarski.Types._
import org.testng.annotations.Test
import org.testng.AssertJUnit._
import tarski.Expand._
import scala.language.implicitConversions

class TestParse {
  implicit val r = SRange.unknown
  val a = SGroup.unknown
  val parens = YesAround(Paren,Paren,a)
  val bracks = YesAround(Brack,Brack,a)
  val curlys = YesAround(Curly,Curly,a)
  def commas[A](x0: A, x1: A, xs: A*): CommaList[A] = CommaList2(x0::x1::xs.toList,List.fill(xs.size+1)(r))
  def juxts [A](x0: A, x1: A, xs: A*): JuxtList[A]  = JuxtList2 (x0::x1::xs.toList)
  def noLoc[A](x: Loc[A]): Loc[A] = Loc(x.x,r)
  def clean(s: String): String = s.replaceAllLiterally(",SRange.unknown","").replaceAllLiterally("SRange.unknown,","")
  implicit val showFlags = abbrevShowFlags

  case class Flags(loc: Boolean)
  implicit val f = Flags(loc=false)

  @Test def lexer(): Unit = {
    // Utilities
    def spaced(ts: List[Token]): List[Token] = ts match {
      case Nil|List(_) => ts
      case x :: xs => x :: WhitespaceTok(" ") :: spaced(xs)
    }
    def check(name: String, cons: String => Token, options: String) =
      assertEquals(spaced(splitWhitespace(options) map cons),lex(options) map (_.x))

    assertEquals(spaced(List(AbstractTok,FinalTok,DoTok)),lex("abstract final do") map (_.x))
    check("ints",IntLitTok,"0 1 17 0x81 07_43 0b1010_110")
    check("longs",LongLitTok,"0l 1L 17l 0x81L 07_43l 0b1010_110L")
    check("floats",FloatLitTok,"0f 5F 5.3f .4e-8F 0x4.aP1_7f")
    check("doubles",DoubleLitTok,"0d 5D 5.3 5.3d .4e-8 .4e-8D 0x4.aP1_7 0x4.aP1_7d")
    check("chars",CharLitTok,"""'x' '\t' '\n' '\0133'""")
    check("strings",StringLitTok,""""xyz" "\n\b\r\t" "\0\1\2"""")

    // We lex >> into GtTok RShiftSepTok GtTok.  Make sure we don't screw it up.
    def prep(s: String, ts: Token*) =
      assertEquals(ts.toList,prepare(lex(s)) map (_.x))
    prep(">",GtTok)
    prep(">>",GtTok,RShiftSepTok,GtTok)
    prep(">>>",GtTok,UnsignedRShiftSepTok,GtTok,UnsignedRShiftSepTok,GtTok)
    prep("> >",GtTok,GtTok)
    prep("> > >",GtTok,GtTok,GtTok)
    prep("> >>",GtTok,GtTok,RShiftSepTok,GtTok)

    // Comments
    def com(s: String, ts: Token*) = assertEquals(ts.toList,lex(s) map (_.x))
    com("/* blah */",CCommentTok("/* blah */"))
    com("/* 0 */ /* 1 */",CCommentTok("/* 0 */"),WhitespaceTok(" "),CCommentTok("/* 1 */"))
    com(""""/**/"""",StringLitTok(""""/**/""""))
  }

  @Test def pretty(): Unit = {
    def check(s: String, e: AExp) = assertEquals(s,show(e))
    def add(x: AExp, y: AExp) = BinaryAExp(AddOp,r,x,y)
    def mul(x: AExp, y: AExp) = BinaryAExp(MulOp,r,x,y)

    check("1 + 2 + 3",     add(add(1,2),3))
    check("1 + (2 + 3)", add(1,add(2,3)))
    check("1 + 2 * 3",     add(1,mul(2,3)))
    check("1 * 2 + 3",     add(mul(1,2),3))
    check("1 * (2 + 3)", mul(1,add(2,3)))
    check("(1 + 2) * 3", mul(add(1,2),3))
  }

  def prep(s: String)(implicit f: Flags): List[Loc[Token]] = prepare(if (f.loc) lex(s) else lex(s) map noLoc)

  def parseExpand(ts: List[Loc[Token]]): Scored[List[AStmt]] =
    uniform(Pr.parse,ParseEddy.parse(ts),"Parse failed") flatMap (Expand.expand(_))

  def testAST(s: String, ss: List[AStmt]*)(implicit f: Flags): Unit = {
    val tokens = prep(s)
    println(s"tokens = $tokens")
    val asts = parseExpand(tokens).stream.toList.map(_.x)
    for (e <- ss if !asts.contains(e)) {
      println()
    }
    assertSetsEqual(ss,asts,clean=clean)
  }

  def testASTPossible(s: String, ss: List[AStmt])(implicit f: Flags): Unit = {
    val tokens = prep(s)
    println(s"tokens = $tokens")
    val asts = parseExpand(tokens).stream.toList.map(_.x)
    assertIn(ss,asts.toSet)
  }

  def testBest(s: String, ss: List[AStmt])(implicit f: Flags): Unit = {
    val tokens = prep(s)
    val asts = Mismatch.repair(tokens) flatMap parseExpand
    asts.best match {
      case Left(e) => throw new RuntimeException("\n"+e.prefixed("error: "))
      case Right(ast) => assertEquals(ss,ast)
    }
  }

  // Warning: Extremely brittle
  def testCount(count: Int, s: String)(implicit f: Flags): Unit = {
    val asts = parseExpand(prep(s)).stream.toList.map(_.x)
    if (asts.size != count)
      throw new RuntimeException(s"\nCount test failed: expected $count, got ${asts.size}\n\n${asts mkString "\n"}")
  }

  @Test def hole() = testAST("",Nil)
  @Test def x() = testAST("x",NameAExp("x",r))

  @Test
  def nestApply() = {
    implicit val f = Flags(loc=true)
    def r(lo: Int, hi: Int) = SRange(SLoc(lo),SLoc(hi))
    def a(lo: Int, hi: Int) = YesAround(Paren,Paren,SGroup.approx(r(lo,hi)))
    testAST("x = A(Object())",
      AssignAExp(None,r(2,3),NameAExp("x",r(0,1)),
        ApplyAExp(NameAExp("A",r(4,5)),ApplyAExp(NameAExp("Object",r(6,12)),EmptyList,a(12,14)),a(5,15))))
  }

  @Test
  def primTypes() =
    for (t <- List(VoidType,ByteType,ShortType,IntType,LongType,FloatType,DoubleType,CharType))
      testAST(show(t)+" x",
        VarAStmt(Nil,t,AVarDecl("x",r,0,None)),
        ApplyAExp(t,"x",NoAround(r)))

  @Test
  def varArray() =
    testAST("int x[]",VarAStmt(Nil,IntType,AVarDecl("x",r,1,None)),
                      ApplyAExp("int",SingleList(ApplyAExp("x",EmptyList,bracks)),NoAround(r)))

  @Test def varField() = testAST("X().Y y",
    ApplyAExp("X",juxts(FieldAExp(ArrayAExp(EmptyList,parens),r,None,"Y",r),"y"),NoAround(r)),
    ApplyAExp(FieldAExp(ApplyAExp("X",EmptyList,parens),r,None,"Y",r),SingleList("y"),NoAround(r)),
    VarAStmt(Nil,FieldAExp(ApplyAExp("X",EmptyList,parens),r,None,"Y",r),AVarDecl("y",r,0,None)))

  // Precedence
  def add(x: AExp, y: AExp) = BinaryAExp(AddOp,r,x,y)
  def mul(x: AExp, y: AExp) = BinaryAExp(MulOp,r,x,y)
  @Test def addMul() = testAST("1 + 2 * 3", add(1,mul(2,3)))
  @Test def mulAdd() = testAST("1 * 2 + 3", add(mul(1,2),3))

  // Compound statements
  val t = NameAExp("true",r)
  val e = EmptyAStmt(r)
  val es = SemiAStmt(e,r)
  val h = HoleAStmt(r)
  val he = HoleAStmt(SRange.empty)
  @Test def ifStmt()      = testAST("if (true);",IfAStmt(r,t,parens,es))
  @Test def ifBare()      = testAST("if true;",SemiAStmt(IfAStmt(r,t,NoAround(r),e),r),
                                               SemiAStmt(IfElseAStmt(r,t,NoAround(r),e,r,h),r))
  @Test def ifElseHole()  = testAST("if (true) else", IfElseAStmt(r,t,parens,he,r,h))
  @Test def whileBare()   = testAST("while true;", SemiAStmt(WhileAStmt(r,false,t,NoAround(r),e),r))
  @Test def doWhileBare() = testAST("do; while true", DoAStmt(r,es,r,false,t,NoAround(r)))
  @Test def whileHole()   = testAST("while true", WhileAStmt(r,false,t,NoAround(r),h), WhileAStmt(r,false,t,NoAround(r),e))
  @Test def untilHole()   = testAST("until true", WhileAStmt(r,true,t,NoAround(r),h), WhileAStmt(r,true,t,NoAround(r),e),
                                                  ApplyAExp("until",SingleList(true),NoAround(r)),
                                                  VarAStmt(Nil,"until",AVarDecl("true",r,0,None)))
  @Test def forever()     = testAST("for (;;);", ForAStmt(r,For(Nil,r,None,r,Nil),parens,es))
  @Test def foreverHole() = testAST("for (;;)", ForAStmt(r,For(Nil,r,None,r,Nil),parens,h))
  @Test def forSimple()   = testAST("for (x=7;true;x++)",
    ForAStmt(r,For(AssignAExp(None,r,"x",7),r,Some(t),r,UnaryAExp(PostIncOp,r,"x")),parens,h))

  @Test def staticMethodOfObject() = testASTPossible("(X()).f();",
    SemiAStmt(ExpAStmt(ApplyAExp(FieldAExp(ParenAExp(ApplyAExp("X",EmptyList,parens),parens),r,None,"f",r),EmptyList,parens)),r))

  @Test def weirdParens() = testAST("([{)]}",
    ParenAExp(ArrayAExp(SingleList(ArrayAExp(EmptyList,YesAround(Curly,Paren,a))),bracks),YesAround(Paren,Curly,a)),
    ParenAStmt(ArrayAExp(SingleList(ArrayAExp(EmptyList,YesAround(Curly,Paren,a))),bracks),YesAround(Paren,Curly,a)))

  @Test def thisForward() = testAST("this()",ApplyAExp("this",EmptyList,parens))
  @Test def superForward() = testAST("super()",ApplyAExp("super",EmptyList,parens))

  @Test def arrayVar() = {
    val rhs = ArrayAExp(commas(1,2,3),curlys)
    testAST("int[] x = {1,2,3}",
      VarAStmt(Nil,ApplyAExp("int",EmptyList,bracks),AVarDecl("x",r,0,Some(r,rhs))),
      AssignAExp(None,r,ApplyAExp(ApplyAExp("int",EmptyList,bracks),SingleList("x"),NoAround(r)),rhs))
  }

  @Test def genericType() =
    testASTPossible("X<String,A<String>> x = null",
      VarAStmt(Nil,TypeApplyAExp(NameAExp("X",r),commas("String",TypeApplyAExp("A","String",a,true)),a,true),
        AVarDecl("x",r,0,Some(r,NameAExp("null",r)))))

  // We lex >> into GtTok GtNoSepTok GtTok.  Make sure we don't screw it up.
  @Test def rshift() = testAST("x >> y",BinaryAExp(RShiftOp,r,"x","y"))
  @Test def urshift() = testAST("x >>> y",BinaryAExp(UnsignedRShiftOp,r,"x","y"))
  @Test def rshiftSep() = testAST("x > > y")
  @Test def urshiftSep() = testAST("x >> > y")

  @Test def juxt() = testAST("a b c",
    ApplyAExp("a",juxts("b","c"),NoAround(r)),
    VarAStmt(Nil,"a",juxts(AVarDecl("b",r,0,None),AVarDecl("c",r,0,None))))

  @Test def twoTypeArgs() =
    testAST("new<C>A<B>",NewAExp(None,r,Some(Grouped(SingleList("C"),a)),TypeApplyAExp("A","B",a,true)),
                         NewAExp(None,r,None,TypeApplyAExp(TypeApplyAExp("A","C",a,false),"B",a,true)),
                         TypeApplyAExp(NewAExp(None,r,Some(Grouped(SingleList("C"),a)),"A"),"B",a,true))

  @Test def verboseArray() = testASTPossible("new int[]{1,2,3}",
    ApplyAExp(NewAExp(None,r,None,"int",List(Grouped(None,a))),commas(1,2,3),curlys))

  @Test def booleanEqTrue() = testAST("boolean x = true;",
    SemiAStmt(VarAStmt(Nil,"boolean",AVarDecl("x",r,0,Some(r,"true":Option[AExp]))),r),
    SemiAStmt(VarAStmt(Nil,"boolean",juxts(AVarDecl("x",r,0,Some((r,None))),AVarDecl("true",r,0,None))),r),
    SemiAStmt(AssignAExp(None,r,ApplyAExp("boolean","x",NoAround(r)),"true"),r))

  @Test def tryFinallyStmt() = testAST("try x = 1 finally ",
    TryAStmt(r,AssignAExp(None,r,"x",1), Nil, Some((r,HoleAStmt(SRange.empty)))))
  @Test def tryFinallyStmt2() = testAST("try { x = 1 } finally { x = 2 } ",
    TryAStmt(r,BlockAStmt(List(ExpAStmt(AssignAExp(None,r,"x",1))),a), Nil, Some((r,BlockAStmt(List(ExpAStmt(AssignAExp(None,r,"x",2))),a)))))

  @Test def tryCatchStmt() = testAST("try x = 1 catch ... ",
    TryAStmt(r,AssignAExp(None,r,"x",1), List((CatchInfo(r,Nil,None,None,NoAround(r),colon=false),HoleAStmt(SRange.empty))), None))
  @Test def tryCatchStmt2() = testAST("try { x = 1 } catch (final Exception e) ",
    TryAStmt(r,BlockAStmt(List(ExpAStmt(AssignAExp(None,r,"x",1))),a),
             List((CatchInfo(r,List(Loc(Mods.Final,r)),Some(NameAExp("Exception",r)),Some("e"),Around(Loc(Arounds.Paren,r),Loc(Arounds.Paren,r)),colon=false),
             HoleAStmt(SRange.empty))), None))
  @Test def tryCatchStmt3() = testAST("try { x = 1 } catch (e: Exception) { x = 2 }",
    TryAStmt(r,BlockAStmt(List(ExpAStmt(AssignAExp(None,r,"x",1))),a),
             List((CatchInfo(r,Nil,Some(NameAExp("Exception",r)),Some("e"),Around(Loc(Arounds.Paren,r),Loc(Arounds.Paren,r)),colon=true),
             BlockAStmt(List(ExpAStmt(AssignAExp(None,r,"x",2))),a))), None))
  @Test def tryCatchFinallyStmt() = testAST("try x = 1 finally ",
    TryAStmt(r,AssignAExp(None,r,"x",1), Nil, Some(r,HoleAStmt(SRange.empty))))

  // A complicated example
  @Test def complicated() = {
    implicit val f = Flags(loc=true)
    testCount(1,"while (!tokens.isEmpty() && tokens.get(tokens.size()-1).x() instanceof WhitespaceTok) tokens.remove(tokens.size()-1)")
  }
}
