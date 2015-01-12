// Autogenerated by ambiguity.  DO NOT EDIT!
package tarski
import tarski.Tokens._
import tarski.AST._
import tarski.Types._
import tarski.Operators._
import utility.Locations._
import scala.language.implicitConversions

object ParseEddyActions {
  type Range = Long
  @inline private implicit def convert(r: Range): SRange = new SRange(r)
  
  def Commas2_ExpAssignNC0(x0: AExp, x2: List[AExp]): List[AExp] = x0 :: x2
  def ForInfo0(x0: (List[Mod],Option[AExp]), x1: ((String,Int),AExp), r: Range): ForInfo = Foreach(x0._1,x0._2,x1._1._1,x1._1._2,x1._2,r)
  def ForInfo1(x0: List[AStmt], x1: (Option[AExp],List[AExp]), r: Range): ForInfo = For(x0,x1._1,x1._2,r)
  def ExpUnary_ExpJuxt0(x0: AExp, x1: (Group,AExp), r: Range): AExp = CastAExp(x0,x1._2,r)
  def ExpUnary_ExpJuxt1(x0: UnaryOp, x1: AExp, r: Range): AExp = UnaryAExp(x0,x1,r)
  def ExpUnary_ExpJuxt2(x0: AExp, x1: UnaryOp, r: Range): AExp = UnaryAExp(x1,x0,r)
  def ExpUnary_ExpJuxt3(x0: AExp): AExp = x0
  def IfTok__ExpAssignNP0(x1: AExp): AExp = x1
  def ExpUnary_ExpJuxtNP0(x0: AExp, x1: (Group,AExp), r: Range): AExp = CastAExp(x0,x1._2,r)
  def ExpUnary_ExpJuxtNP1(x0: UnaryOp, x1: AExp, r: Range): AExp = UnaryAExp(x0,x1,r)
  def ExpUnary_ExpJuxtNP2(x0: AExp, x1: UnaryOp, r: Range): AExp = UnaryAExp(x1,x0,r)
  def ExpUnary_ExpJuxtNP3(x0: AExp): AExp = x0
  def ExpOrOr_ExpWild0(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(OrOrOp,x0,x2,r)
  def ExpOrOr_ExpWild1(x0: AExp): AExp = x0
  def ExpHighNA0(x0: (AExp,Group), x1: (KList[AExp],Group), r: Range): AExp = ApplyAExp(x0._1,x1._1,Grouped(x0._2,x1._2),r)
  def ExpHighNA1(x0: AExp): AExp = x0
  def MaybeThen__MaybeStmt__ElseTok0(x1: AStmt): AStmt = x1
  def Right__ExpUnary_ExpJuxt0(x0: Group, x1: AExp): (Group,AExp) = (x0,x1)
  def ExpAssign0(x0: AExp, x1: (Option[AssignOp],AExp), r: Range): AExp = AssignAExp(x1._1,x0,x1._2,r)
  def ExpAssign1(x0: AExp): AExp = x0
  def ExpCond_ExpWild__Right0(x0: AExp, x1: Group): (AExp,Group) = (x0,x1)
  def ExpXor_ExpJuxtNP0(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(XorOp,x0,x2,r)
  def ExpXor_ExpJuxtNP1(x0: AExp): AExp = x0
  def ExpAssignNC0(x0: AExp, x1: (Option[AssignOp],AExp), r: Range): AExp = AssignAExp(x1._1,x0,x1._2,r)
  def ExpAssignNC1(x0: AExp): AExp = x0
  def ExpHigh__Left0(x0: AExp, x1: Group): (AExp,Group) = (x0,x1)
  def Type__List1_VarDecl0(x0: AExp, x1: KList[(String,Int,Option[AExp])]): (AExp,KList[(String,Int,Option[AExp])]) = (x0,x1)
  def IdentDims__ForeachSep__ExpAssign0(x0: (String,Int), x1: AExp): ((String,Int),AExp) = (x0,x1)
  def Juxts1_Mod0(x0: Mod, x1: List[Mod]): List[Mod] = x0 :: x1
  def Juxts1_Mod1(x0: Mod): List[Mod] = List(x0)
  def List1_ExpAssignNC0(x0: List[AExp]): KList[AExp] = JuxtList(x0)
  def List1_ExpAssignNC1(x0: List[AExp]): KList[AExp] = CommaList(x0)
  def List1_ExpAssignNC2(x0: AExp): KList[AExp] = SingleList(x0)
  def Juxts2_ExpWild0(x0: AExp, x1: List[AExp]): List[AExp] = x0 :: x1
  def ExpAdd_ExpWild0(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(SubOp,x0,x2,r)
  def ExpAdd_ExpWild1(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(AddOp,x0,x2,r)
  def ExpAdd_ExpWild2(x0: AExp): AExp = x0
  def Option_Ident0(x0: String): Option[String] = Some(x0)
  def Option_Ident1(): Option[String] = None
  def Juxts2_ExpAssignNC0(x0: AExp, x1: List[AExp]): List[AExp] = x0 :: x1
  def ExpCond_ExpWild0(x0: AExp, x1: (AExp,AExp), r: Range): AExp = CondAExp(x0,x1._1,x1._2,r)
  def ExpCond_ExpWild1(x0: AExp): AExp = x0
  def DotTok__Option_TypeArgs__Ident0(x1: Option[Located[KList[AExp]]], x2: String): (Option[Located[KList[AExp]]],String) = (x1,x2)
  def List1_VarDecl0(x0: List[(String,Int,Option[AExp])]): KList[(String,Int,Option[AExp])] = JuxtList(x0)
  def List1_VarDecl1(x0: List[(String,Int,Option[AExp])]): KList[(String,Int,Option[AExp])] = CommaList(x0)
  def List1_VarDecl2(x0: (String,Int,Option[AExp])): KList[(String,Int,Option[AExp])] = SingleList(x0)
  def AssignOp0(): Option[AssignOp] = Some(DivOp)
  def AssignOp1(): Option[AssignOp] = Some(AddOp)
  def AssignOp2(): Option[AssignOp] = Some(UnsignedRShiftOp)
  def AssignOp3(): Option[AssignOp] = Some(LShiftOp)
  def AssignOp4(): Option[AssignOp] = Some(OrOp)
  def AssignOp5(): Option[AssignOp] = Some(AndOp)
  def AssignOp6(): Option[AssignOp] = Some(MulOp)
  def AssignOp7(): Option[AssignOp] = Some(XorOp)
  def AssignOp8(): Option[AssignOp] = None
  def AssignOp9(): Option[AssignOp] = Some(SubOp)
  def AssignOp10(): Option[AssignOp] = Some(RShiftOp)
  def AssignOp11(): Option[AssignOp] = Some(ModOp)
  def ExpCommas0(x0: List[AExp], r: Range): AExp = ArrayAExp(CommaList(x0),NoAround,r)
  def ExpCommas1(x0: AExp): AExp = x0
  def ForInfo__Right0(x0: ForInfo, x1: Group): (ForInfo,Group) = (x0,x1)
  def ExpOrOr_ExpJuxt0(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(OrOrOp,x0,x2,r)
  def ExpOrOr_ExpJuxt1(x0: AExp): AExp = x0
  def ExpMul_ExpJuxtNP0(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(ModOp,x0,x2,r)
  def ExpMul_ExpJuxtNP1(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(DivOp,x0,x2,r)
  def ExpMul_ExpJuxtNP2(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(MulOp,x0,x2,r)
  def ExpMul_ExpJuxtNP3(x0: AExp): AExp = x0
  def ExpWild0(x1: Option[(Bound,AExp)], r: Range): AExp = WildAExp(x1,r)
  def ExpWild1(x0: AExp): AExp = x0
  def ForeachSep__ExpAssign0(x1: AExp): AExp = x1
  def MaybeStmt__ElseTok__Stmt0(x0: AStmt, x2: AStmt): (AStmt,AStmt) = (x0,x2)
  def ForTok__Left__ForInfo0(x1: Group, x2: ForInfo): (Group,ForInfo) = (x1,x2)
  def VarDecl0(x0: (String,Int), x2: AExp): (String,Int,Option[AExp]) = (x0._1,x0._2,Some(x2))
  def VarDecl1(x0: (String,Int)): (String,Int,Option[AExp]) = (x0._1,x0._2,None)
  def WildcardBounds0(x1: AExp): Option[(Bound,AExp)] = Some((Super,x1))
  def WildcardBounds1(x1: AExp): Option[(Bound,AExp)] = Some((Extends,x1))
  def WildcardBounds2(x1: AExp): Option[(Bound,AExp)] = Some((Extends,x1))
  def WildcardBounds3(): Option[(Bound,AExp)] = None
  def PreOp0(): UnaryOp = PreIncOp
  def PreOp1(): UnaryOp = CompOp
  def PreOp2(): UnaryOp = PosOp
  def PreOp3(): UnaryOp = NotOp
  def PreOp4(): UnaryOp = PreDecOp
  def PreOp5(): UnaryOp = NegOp
  def MaybeDo__Stmt0(x1: AStmt): AStmt = x1
  def ExpOrOr_ExpJuxtNP0(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(OrOrOp,x0,x2,r)
  def ExpOrOr_ExpJuxtNP1(x0: AExp): AExp = x0
  def Commas2_ExpCond_ExpJuxt__Right0(x0: List[AExp], x1: Group): (List[AExp],Group) = (x0,x1)
  def StmtHelperBS0(x1: AExp, x3: AStmt, r: Range): AStmt = IfAStmt(x1,x3,NoAround,r)
  def StmtHelperBS1(x1: AExp, r: Range): AStmt = ThrowAStmt(x1,r)
  def StmtHelperBS2(x0: Boolean, x1: AExp, r: Range): AStmt = WhileAStmt(x1,EmptyAStmt,x0,NoAround,r)
  def StmtHelperBS3(x1: AExp, r: Range): AStmt = IfAStmt(x1,EmptyAStmt,NoAround,r)
  def StmtHelperBS4(x1: Option[AExp], r: Range): AStmt = ReturnAStmt(x1,r)
  def StmtHelperBS5(x1: AExp, x3: AExp, r: Range): AStmt = AssertAStmt(x1,Some(x3),r)
  def StmtHelperBS6(x0: (AExp,Around), x1: (AStmt,AStmt), r: Range): AStmt = IfElseAStmt(x0._1,x1._1,x1._2,x0._2,r)
  def StmtHelperBS7(x1: AExp, r: Range): AStmt = IfElseAStmt(x1,EmptyAStmt,HoleAStmt,NoAround,r)
  def StmtHelperBS8(x0: Boolean, x1: (AExp,AStmt), r: Range): AStmt = WhileAStmt(x1._1,x1._2,x0,NoAround,r)
  def StmtHelperBS9(x0: (Group,ForInfo), x1: (Group,AStmt), r: Range): AStmt = ForAStmt(x0._2,x1._2,Grouped(x0._1,x1._1),r)
  def StmtHelperBS10(x1: Option[String], r: Range): AStmt = ContinueAStmt(x1,r)
  def StmtHelperBS11(x1: (AExp,Around), x2: List[AStmt], r: Range): AStmt = SyncAStmt(x1._1,BlockAStmt(x2,r),x1._2,r)
  def StmtHelperBS12(x1: Option[String], r: Range): AStmt = BreakAStmt(x1,r)
  def StmtHelperBS13(x0: List[Mod], x1: (AExp,KList[(String,Int,Option[AExp])]), r: Range): AStmt = VarAStmt(x0,x1._1,x1._2,r)
  def StmtHelperBS14(x0: AExp, x1: (AStmt,AStmt), r: Range): AStmt = IfElseAStmt(x0,x1._1,x1._2,NoAround,r)
  def StmtHelperBS15(x0: AStmt, x1: (Boolean,(AExp,Around)), r: Range): AStmt = DoAStmt(x0,x1._2._1,x1._1,x1._2._2,r)
  def StmtHelperBS16(x0: AExp): AStmt = ExpAStmt(x0)
  def StmtHelperBS17(x0: (Boolean,(AExp,Around)), x1: AStmt, r: Range): AStmt = WhileAStmt(x0._2._1,x1,x0._1,x0._2._2,r)
  def StmtHelperBS18(x1: ForInfo, x3: AStmt, r: Range): AStmt = ForAStmt(x1,x3,NoAround,r)
  def StmtHelperBS19(x0: List[AStmt], r: Range): AStmt = BlockAStmt(x0,r)
  def StmtHelperBS20(x1: AExp, r: Range): AStmt = AssertAStmt(x1,None,r)
  def StmtHelperBS21(x0: (AExp,Around), x1: AStmt, r: Range): AStmt = IfAStmt(x0._1,x1,x0._2,r)
  def ExpEq_ExpJuxt0(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(NeOp,x0,x2,r)
  def ExpEq_ExpJuxt1(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(EqOp,x0,x2,r)
  def ExpEq_ExpJuxt2(x0: AExp): AExp = x0
  def ExpAndAnd_ExpJuxt0(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(AndAndOp,x0,x2,r)
  def ExpAndAnd_ExpJuxt1(x0: AExp): AExp = x0
  def Juxts1_VarDecl0(x0: (String,Int,Option[AExp]), x1: List[(String,Int,Option[AExp])]): List[(String,Int,Option[AExp])] = x0 :: x1
  def Juxts1_VarDecl1(x0: (String,Int,Option[AExp])): List[(String,Int,Option[AExp])] = List(x0)
  def SingleJuxt1_ExpWildNA0(x0: List[AExp]): KList[AExp] = JuxtList(x0)
  def SingleJuxt1_ExpWildNA1(x0: AExp): KList[AExp] = SingleList(x0)
  def ThenTok__MaybeStmt__ElseTok__Stmt0(x1: AStmt, x3: AStmt): (AStmt,AStmt) = (x1,x3)
  def Commas1_StmtHelperBS0(x0: AStmt, x2: List[AStmt]): List[AStmt] = x0 :: x2
  def Commas1_StmtHelperBS1(x0: AStmt): List[AStmt] = List(x0)
  def Option_ExpAssign0(x0: AExp): Option[AExp] = Some(x0)
  def Option_ExpAssign1(): Option[AExp] = None
  def Commas1_Type0(x0: AExp, x2: List[AExp]): List[AExp] = x0 :: x2
  def Commas1_Type1(x0: AExp): List[AExp] = List(x0)
  def ParenExp0(x0: Group, x1: (AExp,Group)): (AExp,Around) = (x1._1,Grouped(x0,x1._2))
  def ExpShift_ExpJuxt0(x0: AExp, x6: AExp, r: Range): AExp = BinaryAExp(UnsignedRShiftOp,x0,x6,r)
  def ExpShift_ExpJuxt1(x0: AExp, x4: AExp, r: Range): AExp = BinaryAExp(RShiftOp,x0,x4,r)
  def ExpShift_ExpJuxt2(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(LShiftOp,x0,x2,r)
  def ExpShift_ExpJuxt3(x0: AExp): AExp = x0
  def Option_TypeArgs0(x0: Located[KList[AExp]]): Option[Located[KList[AExp]]] = Some(x0)
  def Option_TypeArgs1(): Option[Located[KList[AExp]]] = None
  def IfTok__ParenExp0(x1: (AExp,Around)): (AExp,Around) = x1
  def Juxts1_ExpWild0(x0: AExp, x1: List[AExp]): List[AExp] = x0 :: x1
  def Juxts1_ExpWild1(x0: AExp): List[AExp] = List(x0)
  def PostOp0(): UnaryOp = PostDecOp
  def PostOp1(): UnaryOp = PostIncOp
  def ExpHighNP0(x0: Group, x1: (List[AExp],Group), r: Range): AExp = ArrayAExp(CommaList(x1._1), Grouped(x0,x1._2),r)
  def ExpHighNP1(x0: Group, x1: (List[AExp],Group), r: Range): AExp = ArrayAExp(JuxtList(x1._1), Grouped(x0,x1._2),r)
  def ExpHighNP2(x0: Group, x1: (AExp,Group), r: Range): AExp = ArrayAExp(SingleList(x1._1),Grouped(x0,x1._2),r)
  def ExpHighNP3(x0: Group, x1: Group, r: Range): AExp = ArrayAExp(EmptyList, Grouped(x0,x1),r)
  def ExpHighNP4(x0: AExp): AExp = x0
  def Commas1_ExpAssignNC0(x0: AExp, x2: List[AExp]): List[AExp] = x0 :: x2
  def Commas1_ExpAssignNC1(x0: AExp): List[AExp] = List(x0)
  def MaybeThen__Stmt0(x1: AStmt): AStmt = x1
  def Option_Type0(x0: AExp): Option[AExp] = Some(x0)
  def Option_Type1(): Option[AExp] = None
  def Commas1_ExpCond_ExpJuxt0(x0: AExp, x2: List[AExp]): List[AExp] = x0 :: x2
  def Commas1_ExpCond_ExpJuxt1(x0: AExp): List[AExp] = List(x0)
  def ExpAndAnd_ExpWild0(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(AndAndOp,x0,x2,r)
  def ExpAndAnd_ExpWild1(x0: AExp): AExp = x0
  def ExpHigh0(x0: AExp): AExp = x0
  def ExpHigh1(x0: AExp): AExp = x0
  def Juxts0_Mod__Option_Type0(x0: List[Mod], x1: Option[AExp]): (List[Mod],Option[AExp]) = (x0,x1)
  def ExpEq_ExpWild0(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(NeOp,x0,x2,r)
  def ExpEq_ExpWild1(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(EqOp,x0,x2,r)
  def ExpEq_ExpWild2(x0: AExp): AExp = x0
  def Commas2_Type0(x0: AExp, x2: List[AExp]): List[AExp] = x0 :: x2
  def Commas0_StmtHelperBS0(x0: List[AStmt]): List[AStmt] = x0
  def Commas0_StmtHelperBS1(): List[AStmt] = Nil
  def Juxts2_ExpWildNA0(x0: AExp, x1: List[AExp]): List[AExp] = x0 :: x1
  def ExpParens0(x1: List[AExp], x2: Group, r: Range): AExp = ArrayAExp(JuxtList(x1), Grouped(Paren,x2),r)
  def ExpParens1(x1: List[AExp], x2: Group, r: Range): AExp = ArrayAExp(CommaList(x1),Grouped(Paren,x2),r)
  def ExpParens2(x1: Group, r: Range): AExp = ArrayAExp(EmptyList, Grouped(Paren,x1),r)
  def ExpParens3(x1: AExp, x2: Group, r: Range): AExp = ParenAExp(x1, Grouped(Paren,x2),r)
  def MaybeStmt0(x0: AStmt): AStmt = x0
  def MaybeStmt1(): AStmt = HoleAStmt
  def TypeArgs0(x1: KList[AExp], r: Range): Located[KList[AExp]] = Located(x1,r)
  def ExpOr_ExpJuxt0(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(OrOp,x0,x2,r)
  def ExpOr_ExpJuxt1(x0: AExp): AExp = x0
  def Stmt0(x0: AStmt): AStmt = x0
  def Stmt1(x0: AStmt): AStmt = x0
  def Stmt2(): AStmt = EmptyAStmt
  def MaybeParenExp__MaybeDo0(x0: (AExp,Around)): (AExp,Around) = x0
  def Juxts2_VarDecl0(x0: (String,Int,Option[AExp]), x1: List[(String,Int,Option[AExp])]): List[(String,Int,Option[AExp])] = x0 :: x1
  def Juxts1_ExpWildNA0(x0: AExp, x1: List[AExp]): List[AExp] = x0 :: x1
  def Juxts1_ExpWildNA1(x0: AExp): List[AExp] = List(x0)
  def ExpAndAnd_ExpJuxtNP0(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(AndAndOp,x0,x2,r)
  def ExpAndAnd_ExpJuxtNP1(x0: AExp): AExp = x0
  def ExpWildNP0(x1: Option[(Bound,AExp)], r: Range): AExp = WildAExp(x1,r)
  def ExpWildNP1(x0: AExp): AExp = x0
  def MaybeParenExp0(x0: (AExp,Around)): (AExp,Around) = x0
  def MaybeParenExp1(x0: AExp): (AExp,Around) = (x0,NoAround)
  def ExpShift_ExpJuxtNP0(x0: AExp, x6: AExp, r: Range): AExp = BinaryAExp(UnsignedRShiftOp,x0,x6,r)
  def ExpShift_ExpJuxtNP1(x0: AExp, x4: AExp, r: Range): AExp = BinaryAExp(RShiftOp,x0,x4,r)
  def ExpShift_ExpJuxtNP2(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(LShiftOp,x0,x2,r)
  def ExpShift_ExpJuxtNP3(x0: AExp): AExp = x0
  def ExpJuxt0(x0: AExp, x1: KList[AExp], r: Range): AExp = ApplyAExp(x0,x1,NoAround,r)
  def ExpJuxt1(x0: AExp): AExp = x0
  def ExpMul_ExpWild0(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(ModOp,x0,x2,r)
  def ExpMul_ExpWild1(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(DivOp,x0,x2,r)
  def ExpMul_ExpWild2(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(MulOp,x0,x2,r)
  def ExpMul_ExpWild3(x0: AExp): AExp = x0
  def LeftNP0(): Group = Curly
  def LeftNP1(): Group = Brack
  def Juxts1_ExpAssignNC0(x0: AExp, x1: List[AExp]): List[AExp] = x0 :: x1
  def Juxts1_ExpAssignNC1(x0: AExp): List[AExp] = List(x0)
  def ExpAnd_ExpJuxtNP0(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(AndOp,x0,x2,r)
  def ExpAnd_ExpJuxtNP1(x0: AExp): AExp = x0
  def ExpAdd_ExpJuxt0(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(SubOp,x0,x2,r)
  def ExpAdd_ExpJuxt1(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(AddOp,x0,x2,r)
  def ExpAdd_ExpJuxt2(x0: AExp): AExp = x0
  def QuestionTok__ExpAssign__ColonTok__ExpCond_ExpJuxt0(x1: AExp, x3: AExp): (AExp,AExp) = (x1,x3)
  def ExpMul_ExpJuxt0(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(ModOp,x0,x2,r)
  def ExpMul_ExpJuxt1(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(DivOp,x0,x2,r)
  def ExpMul_ExpJuxt2(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(MulOp,x0,x2,r)
  def ExpMul_ExpJuxt3(x0: AExp): AExp = x0
  def List1_Type0(x0: List[AExp]): KList[AExp] = JuxtList(x0)
  def List1_Type1(x0: List[AExp]): KList[AExp] = CommaList(x0)
  def List1_Type2(x0: AExp): KList[AExp] = SingleList(x0)
  def List_ExpAssignNC0(x0: KList[AExp]): KList[AExp] = x0
  def List_ExpAssignNC1(): KList[AExp] = EmptyList
  def ForTok__Left0(x1: Group): Group = x1
  def ExpAssign__Right0(x0: AExp, x1: Group): (AExp,Group) = (x0,x1)
  def Left0(): Group = Curly
  def Left1(): Group = Brack
  def Left2(): Group = Paren
  def WhileUntil0(): Boolean = true
  def WhileUntil1(): Boolean = false
  def Juxts2_ExpWild__Right0(x0: List[AExp], x1: Group): (List[AExp],Group) = (x0,x1)
  def Mod0(x1: String): Mod = Annotation(x1)
  def Mod1(): Mod = Abstract
  def Mod2(): Mod = Strictfp
  def Mod3(): Mod = Private
  def Mod4(): Mod = Transient
  def Mod5(): Mod = Public
  def Mod6(): Mod = Static
  def Mod7(): Mod = Volatile
  def Mod8(): Mod = Synchronized
  def Mod9(): Mod = Protected
  def Mod10(): Mod = Final
  def ExpPrimary0(x0: AExp, x1: Located[KList[AExp]], r: Range): AExp = TypeApplyAExp(x0,x1.x,x1.r,true,r)
  def ExpPrimary1(x0: AExp, x1: (Option[Located[KList[AExp]]],String), r: Range): AExp = FieldAExp(x0,x1._1,x1._2,r)
  def ExpPrimary2(x0: String, r: Range): AExp = NameAExp(x0,r)
  def ExpPrimary3(x0: ALit): AExp = x0
  def ExpAssignNP__DoTok__Stmt0(x0: AExp, x2: AStmt): (AExp,AStmt) = (x0,x2)
  def DoTok__MaybeStmt0(x1: AStmt): AStmt = x1
  def Juxts0_Mod0(x0: List[Mod]): List[Mod] = x0
  def Juxts0_Mod1(): List[Mod] = Nil
  def ExpShift_ExpWild0(x0: AExp, x6: AExp, r: Range): AExp = BinaryAExp(UnsignedRShiftOp,x0,x6,r)
  def ExpShift_ExpWild1(x0: AExp, x4: AExp, r: Range): AExp = BinaryAExp(RShiftOp,x0,x4,r)
  def ExpShift_ExpWild2(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(LShiftOp,x0,x2,r)
  def ExpShift_ExpWild3(x0: AExp): AExp = x0
  def ExpOr_ExpWild0(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(OrOp,x0,x2,r)
  def ExpOr_ExpWild1(x0: AExp): AExp = x0
  def IdentDims0(x0: (String,Int)): (String,Int) = (x0._1,x0._2+1)
  def IdentDims1(x0: String): (String,Int) = (x0,0)
  def ExpRel_ExpJuxt0(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(LtOp,x0,x2,r)
  def ExpRel_ExpJuxt1(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(GtOp,x0,x2,r)
  def ExpRel_ExpJuxt2(x0: AExp, x2: AExp, r: Range): AExp = InstanceofAExp(x0,x2,r)
  def ExpRel_ExpJuxt3(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(LeOp,x0,x2,r)
  def ExpRel_ExpJuxt4(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(GeOp,x0,x2,r)
  def ExpRel_ExpJuxt5(x0: AExp): AExp = x0
  def ExpRel_ExpJuxtNP0(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(LtOp,x0,x2,r)
  def ExpRel_ExpJuxtNP1(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(GtOp,x0,x2,r)
  def ExpRel_ExpJuxtNP2(x0: AExp, x2: AExp, r: Range): AExp = InstanceofAExp(x0,x2,r)
  def ExpRel_ExpJuxtNP3(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(LeOp,x0,x2,r)
  def ExpRel_ExpJuxtNP4(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(GeOp,x0,x2,r)
  def ExpRel_ExpJuxtNP5(x0: AExp): AExp = x0
  def ExpAdd_ExpJuxtNP0(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(SubOp,x0,x2,r)
  def ExpAdd_ExpJuxtNP1(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(AddOp,x0,x2,r)
  def ExpAdd_ExpJuxtNP2(x0: AExp): AExp = x0
  def ExpNew0(x1: Option[Located[KList[AExp]]], x2: AExp, r: Range): AExp = NewAExp(x1,x2,r)
  def ExpNew1(x0: Located[KList[AExp]], x1: AExp, r: Range): AExp = TypeApplyAExp(x1,x0.x,x0.r,false,r)
  def ExpNew2(x0: AExp): AExp = x0
  def Juxts1_Type0(x0: AExp, x1: List[AExp]): List[AExp] = x0 :: x1
  def Juxts1_Type1(x0: AExp): List[AExp] = List(x0)
  def Right__Stmt0(x0: Group, x1: AStmt): (Group,AStmt) = (x0,x1)
  def Commas2_ExpCond_ExpJuxt0(x0: AExp, x2: List[AExp]): List[AExp] = x0 :: x2
  def Juxts2_Type0(x0: AExp, x1: List[AExp]): List[AExp] = x0 :: x1
  def LParenTok__Type0(x1: AExp): AExp = x1
  def Stmts0(x0: AStmt, x2: List[AStmt]): List[AStmt] = x0 :: x2
  def Stmts1(x0: AStmt): List[AStmt] = List(x0)
  def Stmts2(x1: List[AStmt]): List[AStmt] = EmptyAStmt :: x1
  def Stmts3(): List[AStmt] = Nil
  def AssignOp__ExpAssign0(x0: Option[AssignOp], x1: AExp): (Option[AssignOp],AExp) = (x0,x1)
  def ExpRel_ExpWild0(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(LtOp,x0,x2,r)
  def ExpRel_ExpWild1(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(GtOp,x0,x2,r)
  def ExpRel_ExpWild2(x0: AExp, x2: AExp, r: Range): AExp = InstanceofAExp(x0,x2,r)
  def ExpRel_ExpWild3(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(LeOp,x0,x2,r)
  def ExpRel_ExpWild4(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(GeOp,x0,x2,r)
  def ExpRel_ExpWild5(x0: AExp): AExp = x0
  def List_ExpAssignNC__Right0(x0: KList[AExp], x1: Group): (KList[AExp],Group) = (x0,x1)
  def StmtHelper0(x0: Group, x1: (ForInfo,Group), r: Range): AStmt = ForAStmt(x1._1,HoleAStmt,Grouped(x0,x1._2),r)
  def StmtHelper1(x0: AStmt): AStmt = x0
  def StmtHelper2(x1: AExp, x3: AStmt, r: Range): AStmt = IfElseAStmt(x1,x3,HoleAStmt,NoAround,r)
  def StmtHelper3(x1: (AExp,Around), r: Range): AStmt = SyncAStmt(x1._1,HoleAStmt,x1._2,r)
  def StmtHelper4(x1: (AExp,Around), r: Range): AStmt = IfAStmt(x1._1,HoleAStmt,x1._2,r)
  def StmtHelper5(x1: ForInfo, r: Range): AStmt = ForAStmt(x1,HoleAStmt,NoAround,r)
  def StmtHelper6(x0: (AExp,Around), x1: AStmt, r: Range): AStmt = IfElseAStmt(x0._1,x1,HoleAStmt,x0._2,r)
  def StmtHelper7(x0: Boolean, x1: (AExp,Around), r: Range): AStmt = WhileAStmt(x1._1,HoleAStmt,x0,x1._2,r)
  def ExpUnary_ExpWild0(x0: AExp, x1: (Group,AExp), r: Range): AExp = CastAExp(x0,x1._2,r)
  def ExpUnary_ExpWild1(x0: UnaryOp, x1: AExp, r: Range): AExp = UnaryAExp(x0,x1,r)
  def ExpUnary_ExpWild2(x0: AExp, x1: UnaryOp, r: Range): AExp = UnaryAExp(x1,x0,r)
  def ExpUnary_ExpWild3(x0: AExp): AExp = x0
  def IfTok__ParenExp__MaybeThen0(x1: (AExp,Around)): (AExp,Around) = x1
  def ExpJuxtNP0(x0: AExp, x1: KList[AExp], r: Range): AExp = ApplyAExp(x0,x1,NoAround,r)
  def ExpJuxtNP1(x0: AExp): AExp = x0
  def ExpXor_ExpWild0(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(XorOp,x0,x2,r)
  def ExpXor_ExpWild1(x0: AExp): AExp = x0
  def Lit0(x0: DoubleLitTok, r: Range): ALit = DoubleALit(x0.v,r)
  def Lit1(x0: FloatLitTok, r: Range): ALit = FloatALit(x0.v,r)
  def Lit2(x0: LongLitTok, r: Range): ALit = LongALit(x0.v,r)
  def Lit3(x0: StringLitTok, r: Range): ALit = StringALit(x0.v,r)
  def Lit4(x0: CharLitTok, r: Range): ALit = CharALit(x0.v,r)
  def Lit5(x0: IntLitTok, r: Range): ALit = IntALit(x0.v,r)
  def List_Type0(x0: KList[AExp]): KList[AExp] = x0
  def List_Type1(): KList[AExp] = EmptyList
  def ExpOr_ExpJuxtNP0(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(OrOp,x0,x2,r)
  def ExpOr_ExpJuxtNP1(x0: AExp): AExp = x0
  def ExpWildNA0(x1: Option[(Bound,AExp)], r: Range): AExp = WildAExp(x1,r)
  def ExpWildNA1(x0: AExp): AExp = x0
  def WhileUntil__ParenExp0(x0: Boolean, x1: (AExp,Around)): (Boolean,(AExp,Around)) = (x0,x1)
  def Commas1_VarDecl0(x0: (String,Int,Option[AExp]), x2: List[(String,Int,Option[AExp])]): List[(String,Int,Option[AExp])] = x0 :: x2
  def Commas1_VarDecl1(x0: (String,Int,Option[AExp])): List[(String,Int,Option[AExp])] = List(x0)
  def Block0(x1: List[AStmt]): List[AStmt] = x1
  def ExpAnd_ExpWild0(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(AndOp,x0,x2,r)
  def ExpAnd_ExpWild1(x0: AExp): AExp = x0
  def Right0(): Group = Curly
  def Right1(): Group = Brack
  def Right2(): Group = Paren
  def ExpXor_ExpJuxt0(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(XorOp,x0,x2,r)
  def ExpXor_ExpJuxt1(x0: AExp): AExp = x0
  def Ident0(): String = "until"
  def Ident1(): String = "super"
  def Ident2(x0: IdentTok): String = x0.name
  def Ident3(): String = "this"
  def Ident4(): String = "then"
  def Ident5(): String = "in"
  def Commas0_ExpAssignNC0(x0: List[AExp]): List[AExp] = x0
  def Commas0_ExpAssignNC1(): List[AExp] = Nil
  def ExpCond_ExpJuxt0(x0: AExp, x1: (AExp,AExp), r: Range): AExp = CondAExp(x0,x1._1,x1._2,r)
  def ExpCond_ExpJuxt1(x0: AExp): AExp = x0
  def ExpAssignNP0(x0: AExp, x1: (Option[AssignOp],AExp), r: Range): AExp = AssignAExp(x1._1,x0,x1._2,r)
  def ExpAssignNP1(x0: AExp): AExp = x0
  def SemiTok__Option_ExpAssign__SemiTok__Commas0_ExpAssignNC0(x1: Option[AExp], x3: List[AExp]): (Option[AExp],List[AExp]) = (x1,x3)
  def ExpCond_ExpJuxtNP0(x0: AExp, x1: (AExp,AExp), r: Range): AExp = CondAExp(x0,x1._1,x1._2,r)
  def ExpCond_ExpJuxtNP1(x0: AExp): AExp = x0
  def Commas2_VarDecl0(x0: (String,Int,Option[AExp]), x2: List[(String,Int,Option[AExp])]): List[(String,Int,Option[AExp])] = x0 :: x2
  def WhileUntil__MaybeParenExp0(x0: Boolean, x1: (AExp,Around)): (Boolean,(AExp,Around)) = (x0,x1)
  def ExpEq_ExpJuxtNP0(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(NeOp,x0,x2,r)
  def ExpEq_ExpJuxtNP1(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(EqOp,x0,x2,r)
  def ExpEq_ExpJuxtNP2(x0: AExp): AExp = x0
  def ExpAnd_ExpJuxt0(x0: AExp, x2: AExp, r: Range): AExp = BinaryAExp(AndOp,x0,x2,r)
  def ExpAnd_ExpJuxt1(x0: AExp): AExp = x0
  def Option_ExpJuxt0(x0: AExp): Option[AExp] = Some(x0)
  def Option_ExpJuxt1(): Option[AExp] = None
  def Type0(x1: Option[(Bound,AExp)], r: Range): AExp = WildAExp(x1,r)
  def Type1(x0: AExp): AExp = x0
}
