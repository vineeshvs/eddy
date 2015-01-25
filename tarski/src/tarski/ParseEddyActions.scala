// Autogenerated by ambiguity.  DO NOT EDIT!
package tarski
import tarski.Tokens._
import tarski.AST._
import tarski.Mods._
import tarski.Arounds._
import tarski.Operators._
import utility.Locations._
import utility.Locations._
import scala.language.implicitConversions

object ParseEddyActions {
  type Range = Long
  @inline private implicit def convert(r: Range): SRange = new SRange(r)

  def Commas2_ExpAssignNC0(x1: AExp, x3: CommaList1[AExp], x2r: Range): CommaList2[AExp] = x3.preComma(x1,x2r)
  def ForInfo0(x1: (List[Loc[Mod]],Option[AExp],Loc[String]), x2: (List[SGroup],Long,AExp)): ForInfo = Foreach(x1._1,x1._2,x1._3.x,x1._3.r,x2._1,x2._2,x2._3)
  def ForInfo1(x1: CommaList[AStmt], x2: (Long,Option[AExp],Long,CommaList[AExp])): ForInfo = For(x1,x2._1,x2._2,x2._3,x2._4)
  def ExpUnary_ExpJuxt0(x1: (Long,AExp), x2: (Loc[Group],AExp)): AExp = CastAExp(x1._2,Around(Paren,x1._1,x2._1),x2._2)
  def ExpUnary_ExpJuxt1(x1: Loc[UnaryOp], x2: AExp): AExp = UnaryAExp(x1.x,x1.r,x2)
  def ExpUnary_ExpJuxt2(x1: AExp, x2: Loc[UnaryOp]): AExp = UnaryAExp(x2.x,x2.r,x1)
  def ExpUnary_ExpJuxt3(x1: AExp): AExp = x1
  def Dims0(x2: Loc[Group], x3: List[SGroup], x1r: Range): List[SGroup] = SGroup(x1r,x2.r) :: x3
  def Dims1(): List[SGroup] = Nil
  def ExpUnary_ExpJuxtNP0(x1: (Long,AExp), x2: (Loc[Group],AExp)): AExp = CastAExp(x1._2,Around(Paren,x1._1,x2._1),x2._2)
  def ExpUnary_ExpJuxtNP1(x1: Loc[UnaryOp], x2: AExp): AExp = UnaryAExp(x1.x,x1.r,x2)
  def ExpUnary_ExpJuxtNP2(x1: AExp, x2: Loc[UnaryOp]): AExp = UnaryAExp(x2.x,x2.r,x1)
  def ExpUnary_ExpJuxtNP3(x1: AExp): AExp = x1
  def ExpOrOr_ExpWild0(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(OrOrOp,x2r,x1,x3)
  def ExpOrOr_ExpWild1(x1: AExp): AExp = x1
  def ExpHighNA0(x1: (AExp,Loc[Group]), x2: (KList[AExp],Loc[Group])): AExp = ApplyAExp(x1._1,x2._1,Around(x1._2,x2._2))
  def ExpHighNA1(x1: AExp): AExp = x1
  def MaybeThen__MaybeStmt__ElseTok0(x2: AStmt, x3r: Range): (AStmt,Long) = (x2,x3r)
  def Right__ExpUnary_ExpJuxt0(x1: Loc[Group], x2: AExp): (Loc[Group],AExp) = (x1,x2)
  def Right__MaybeStmt0(x1: Loc[Group], x2: AStmt): (Loc[Group],AStmt) = (x1,x2)
  def ExpAssign0(x1: AExp, x2: (Loc[Option[AssignOp]],AExp)): AExp = AssignAExp(x2._1.x,x2._1.r,x1,x2._2)
  def ExpAssign1(x1: AExp): AExp = x1
  def ArrayInteriorN10(x1: List[AExp]): KList[AExp] = JuxtList(x1)
  def ArrayInteriorN11(x1: CommaList2[AExp]): KList[AExp] = x1
  def ArrayInteriorN12(): KList[AExp] = EmptyList
  def ExpXor_ExpJuxtNP0(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(XorOp,x2r,x1,x3)
  def ExpXor_ExpJuxtNP1(x1: AExp): AExp = x1
  def ExpAssignNC0(x1: AExp, x2: (Loc[Option[AssignOp]],AExp)): AExp = AssignAExp(x2._1.x,x2._1.r,x1,x2._2)
  def ExpAssignNC1(x1: AExp): AExp = x1
  def ExpHigh__Left0(x1: AExp, x2: Loc[Group]): (AExp,Loc[Group]) = (x1,x2)
  def Option_Type__List1_VarDecl0(x1: Option[AExp], x2: KList[AVarDecl]): (Option[AExp],KList[AVarDecl]) = (x1,x2)
  def Juxts1_Mod0(x1: Loc[Mod], x2: List[Loc[Mod]]): List[Loc[Mod]] = x1 :: x2
  def Juxts1_Mod1(x1: Loc[Mod]): List[Loc[Mod]] = List(x1)
  def List1_ExpAssignNC0(x1: List[AExp]): KList[AExp] = JuxtList(x1)
  def List1_ExpAssignNC1(x1: CommaList2[AExp]): KList[AExp] = x1
  def List1_ExpAssignNC2(x1: AExp): KList[AExp] = SingleList(x1)
  def Juxts2_ExpWild0(x1: AExp, x2: List[AExp]): List[AExp] = x1 :: x2
  def ExpAdd_ExpWild0(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(SubOp,x2r,x1,x3)
  def ExpAdd_ExpWild1(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(AddOp,x2r,x1,x3)
  def ExpAdd_ExpWild2(x1: AExp): AExp = x1
  def Option_Ident0(x1: Loc[String]): Option[Loc[String]] = Some(x1)
  def Option_Ident1(): Option[Loc[String]] = None
  def Juxts2_ExpAssignNC0(x1: AExp, x2: List[AExp]): List[AExp] = x1 :: x2
  def ExpCond_ExpWild0(x1: AExp, x2: (Long,AExp,Long,AExp)): AExp = CondAExp(x1,x2._1,x2._2,x2._3,x2._4)
  def ExpCond_ExpWild1(x1: AExp): AExp = x1
  def DotTok__Option_TypeArgs__Ident0(x2: Option[Grouped[KList[AExp]]], x3: Loc[String], x1r: Range): (Long,Option[Grouped[KList[AExp]]],Loc[String]) = (x1r,x2,x3)
  def List1_VarDecl0(x1: List[AVarDecl]): KList[AVarDecl] = JuxtList(x1)
  def List1_VarDecl1(x1: CommaList2[AVarDecl]): KList[AVarDecl] = x1
  def List1_VarDecl2(x1: AVarDecl): KList[AVarDecl] = SingleList(x1)
  def Option_Type__Ident0(x1: Option[AExp], x2: Loc[String]): (Option[AExp],Loc[String]) = (x1,x2)
  def AssignOp0(r: Range): Loc[Option[AssignOp]] = Loc(Some(OrOp),r)
  def AssignOp1(r: Range): Loc[Option[AssignOp]] = Loc(Some(AddOp),r)
  def AssignOp2(r: Range): Loc[Option[AssignOp]] = Loc(Some(SubOp),r)
  def AssignOp3(r: Range): Loc[Option[AssignOp]] = Loc(Some(ModOp),r)
  def AssignOp4(r: Range): Loc[Option[AssignOp]] = Loc(Some(AndOp),r)
  def AssignOp5(r: Range): Loc[Option[AssignOp]] = Loc(None,r)
  def AssignOp6(r: Range): Loc[Option[AssignOp]] = Loc(Some(UnsignedRShiftOp),r)
  def AssignOp7(r: Range): Loc[Option[AssignOp]] = Loc(Some(RShiftOp),r)
  def AssignOp8(r: Range): Loc[Option[AssignOp]] = Loc(Some(MulOp),r)
  def AssignOp9(r: Range): Loc[Option[AssignOp]] = Loc(Some(DivOp),r)
  def AssignOp10(r: Range): Loc[Option[AssignOp]] = Loc(Some(LShiftOp),r)
  def AssignOp11(r: Range): Loc[Option[AssignOp]] = Loc(Some(XorOp),r)
  def ExpCommas0(x1: CommaList2[AExp]): AExp = ArrayAExp(x1,NoAround(x1.list.head.r union x1.list.last.r))
  def ExpCommas1(x1: AExp): AExp = x1
  def EllipsisTok__Right__MaybeStmt0(x2: Loc[Group], x3: AStmt): (Loc[Group],AStmt) = (x2,x3)
  def ForInfo__Right0(x1: ForInfo, x2: Loc[Group]): (ForInfo,Loc[Group]) = (x1,x2)
  def ExpOrOr_ExpJuxt0(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(OrOrOp,x2r,x1,x3)
  def ExpOrOr_ExpJuxt1(x1: AExp): AExp = x1
  def ExpMul_ExpJuxtNP0(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(ModOp,x2r,x1,x3)
  def ExpMul_ExpJuxtNP1(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(DivOp,x2r,x1,x3)
  def ExpMul_ExpJuxtNP2(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(MulOp,x2r,x1,x3)
  def ExpMul_ExpJuxtNP3(x1: AExp): AExp = x1
  def ExpWild0(x2: Option[WildBound], x1r: Range): AExp = WildAExp(x1r,x2)
  def ExpWild1(x1: AExp): AExp = x1
  def Juxts0_Mod__Option_Type__Ident0(x1: List[Loc[Mod]], x2: (Option[AExp],Loc[String])): (List[Loc[Mod]],Option[AExp],Loc[String]) = (x1,x2._1,x2._2)
  def ForeachSep__ExpAssign0(x1: Long, x2: AExp): (Long,AExp) = (x1,x2)
  def CatchBlocks0(x1: (CatchInfo,AStmt), x2: List[(CatchInfo,AStmt)]): List[(CatchInfo,AStmt)] = x1 :: x2
  def CatchBlocks1(): List[(CatchInfo,AStmt)] = Nil
  def MaybeStmt__ElseTok__Stmt0(x1: AStmt, x3: AStmt, x2r: Range): (AStmt,Long,AStmt) = (x1,x2r,x3)
  def CatchBlocks__Option_FinallyBlock0(x1: List[(CatchInfo,AStmt)], x2: Option[(SRange,AStmt)]): (List[(CatchInfo,AStmt)],Option[(SRange,AStmt)]) = (x1,x2)
  def ForTok__Left__ForInfo0(x2: Loc[Group], x3: ForInfo, x1r: Range): (Long,Loc[Group],ForInfo) = (x1r,x2,x3)
  def VarDecl0(x1: Loc[String], x2: (List[SGroup],Long,AExp)): AVarDecl = AVarDecl(x1.x,x1.r,x2._1,Some((x2._2:SRange,x2._3)))
  def VarDecl1(x1: Loc[String], x2: List[SGroup]): AVarDecl = AVarDecl(x1.x,x1.r,x2,None)
  def WildcardBounds0(x2: AExp, x1r: Range): Option[WildBound] = Some(WildBound(Super,x1r,x2))
  def WildcardBounds1(x2: AExp, x1r: Range): Option[WildBound] = Some(WildBound(Extends,x1r,x2))
  def WildcardBounds2(x2: AExp, x1r: Range): Option[WildBound] = Some(WildBound(Extends,x1r,x2))
  def WildcardBounds3(): Option[WildBound] = None
  def PreOp0(r: Range): Loc[UnaryOp] = Loc(NegOp,r)
  def PreOp1(r: Range): Loc[UnaryOp] = Loc(NotOp,r)
  def PreOp2(r: Range): Loc[UnaryOp] = Loc(PreDecOp,r)
  def PreOp3(r: Range): Loc[UnaryOp] = Loc(PosOp,r)
  def PreOp4(r: Range): Loc[UnaryOp] = Loc(PreIncOp,r)
  def PreOp5(r: Range): Loc[UnaryOp] = Loc(CompOp,r)
  def MaybeDo__Stmt0(x2: AStmt): AStmt = x2
  def ExpOrOr_ExpJuxtNP0(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(OrOrOp,x2r,x1,x3)
  def ExpOrOr_ExpJuxtNP1(x1: AExp): AExp = x1
  def StmtHelperBS0(x1: AExp, x2: KList[AVarDecl]): AStmt = VarAStmt(Nil,Some(x1),x2)
  def StmtHelperBS1(x2: ForInfo, x4: AStmt, x1r: Range): AStmt = ForAStmt(x1r,x2,NoAround(x2.r),x4)
  def StmtHelperBS2(x2: AExp, x1r: Range): AStmt = ThrowAStmt(x1r,x2)
  def StmtHelperBS3(x1: Loc[Boolean], x2: (AExp,AStmt)): AStmt = WhileAStmt(x1.r,x1.x,x2._1,NoAround(x2._2.r),x2._2)
  def StmtHelperBS4(x2: AExp, x4: AExp, x1r: Range, x3r: Range): AStmt = AssertAStmt(x1r,x2,Some((x3r:SRange,x4)))
  def StmtHelperBS5(x2: Option[Loc[String]], x1r: Range): AStmt = BreakAStmt(x1r,x2)
  def StmtHelperBS6(x1: (Long,AStmt), x2: (Loc[Boolean],(AExp,Around))): AStmt = DoAStmt(x1._1,x1._2,x2._1.r,x2._1.x,x2._2._1,x2._2._2)
  def StmtHelperBS7(x2: (AExp,Around), x3: AStmt, x1r: Range): AStmt = SyncAStmt(x1r,x2._1,x2._2,x3)
  def StmtHelperBS8(x2: AExp, x1r: Range): AStmt = AssertAStmt(x1r,x2,None)
  def StmtHelperBS9(x1: AStmt): AStmt = x1
  def StmtHelperBS10(x1: List[Loc[Mod]], x2: (Option[AExp],KList[AVarDecl])): AStmt = VarAStmt(x1,x2._1,x2._2)
  def StmtHelperBS11(x1: (Long,AStmt), x2: (List[(CatchInfo,AStmt)],Option[(SRange,AStmt)])): AStmt = TryAStmt(x1._1,x1._2,x2._1,x2._2)
  def StmtHelperBS12(x1: (Loc[Boolean],(AExp,Around)), x2: AStmt): AStmt = WhileAStmt(x1._1.r,x1._1.x,x1._2._1,x1._2._2,x2)
  def StmtHelperBS13(x2: PreIf, x1r: Range): AStmt = x2(x1r)
  def StmtHelperBS14(x1: AExp): AStmt = ExpAStmt(x1)
  def StmtHelperBS15(x1: Loc[Boolean], x2: AExp): AStmt = { val er = x2.r; WhileAStmt(x1.r,x1.x,x2,NoAround(er),EmptyAStmt(er.after)) }
  def StmtHelperBS16(x2: Option[AExp], x1r: Range): AStmt = ReturnAStmt(x1r,x2)
  def StmtHelperBS17(x2: Option[Loc[String]], x1r: Range): AStmt = ContinueAStmt(x1r,x2)
  def StmtHelperBS18(x1: (Long,Loc[Group],ForInfo), x2: (Loc[Group],AStmt)): AStmt = ForAStmt(x1._1,x1._3,Around(x1._2,x2._1),x2._2)
  def ExpEq_ExpJuxt0(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(NeOp,x2r,x1,x3)
  def ExpEq_ExpJuxt1(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(EqOp,x2r,x1,x3)
  def ExpEq_ExpJuxt2(x1: AExp): AExp = x1
  def ThenTok__MaybeStmt__ElifTok__AfterIf0(x2: AStmt, x3: ElifTok, x4: PreIf, x3r: Range): (AStmt,Loc[ElifTok],PreIf) = (x2,Loc(x3,x3r),x4)
  def ExpAndAnd_ExpJuxt0(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(AndAndOp,x2r,x1,x3)
  def ExpAndAnd_ExpJuxt1(x1: AExp): AExp = x1
  def Juxts1_VarDecl0(x1: AVarDecl, x2: List[AVarDecl]): List[AVarDecl] = x1 :: x2
  def Juxts1_VarDecl1(x1: AVarDecl): List[AVarDecl] = List(x1)
  def SingleJuxt1_ExpWildNA0(x1: List[AExp]): KList[AExp] = JuxtList(x1)
  def SingleJuxt1_ExpWildNA1(x1: AExp): KList[AExp] = SingleList(x1)
  def AfterIf0(x1: PreIf): PreIf = x1
  def AfterIf1(x1: PreIf): PreIf = x1
  def ThenTok__MaybeStmt__ElseTok__Stmt0(x2: AStmt, x4: AStmt, x3r: Range): (AStmt,Long,AStmt) = (x2,x3r,x4)
  def Commas1_StmtHelperBS0(x1: AStmt, x3: CommaList1[AStmt], x2r: Range): CommaList1[AStmt] = x3.preComma(x1,x2r)
  def Commas1_StmtHelperBS1(x1: AStmt): CommaList1[AStmt] = SingleList(x1)
  def Option_ExpAssign0(x1: AExp): Option[AExp] = Some(x1)
  def Option_ExpAssign1(): Option[AExp] = None
  def ArrayInterior__Right0(x1: KList[AExp], x2: Loc[Group]): (KList[AExp],Loc[Group]) = (x1,x2)
  def Commas1_Type0(x1: AExp, x3: CommaList1[AExp], x2r: Range): CommaList1[AExp] = x3.preComma(x1,x2r)
  def Commas1_Type1(x1: AExp): CommaList1[AExp] = SingleList(x1)
  def ParenExp0(x1: Loc[Group], x2: (AExp,Loc[Group])): (AExp,Around) = (x2._1,Around(x1,x2._2))
  def TryTok__Stmt0(x2: AStmt, x1r: Range): (Long,AStmt) = (x1r,x2)
  def FinallyBlock0(x2: AStmt, x1r: Range): (SRange,AStmt) = (x1r,x2)
  def ExpShift_ExpJuxt0(x1: AExp, x7: AExp, x2r: Range, x6r: Range): AExp = BinaryAExp(UnsignedRShiftOp,x2r union x6r,x1,x7)
  def ExpShift_ExpJuxt1(x1: AExp, x5: AExp, x2r: Range, x4r: Range): AExp = BinaryAExp(RShiftOp,x2r union x4r,x1,x5)
  def ExpShift_ExpJuxt2(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(LShiftOp,x2r,x1,x3)
  def ExpShift_ExpJuxt3(x1: AExp): AExp = x1
  def Ident__MaybeStmt0(x1: Loc[String], x2: AStmt): (Loc[String],AStmt) = (x1,x2)
  def CatchTok__Left__Ident__ColonTok__Juxts0_Mod0(x1: (Long,Loc[Group]), x2: (Loc[String],List[Loc[Mod]])): (Long,Loc[Group],Loc[String],List[Loc[Mod]]) = (x1._1,x1._2,x2._1,x2._2)
  def Option_TypeArgs0(x1: Grouped[KList[AExp]]): Option[Grouped[KList[AExp]]] = Some(x1)
  def Option_TypeArgs1(): Option[Grouped[KList[AExp]]] = None
  def Juxts1_ExpWild0(x1: AExp, x2: List[AExp]): List[AExp] = x1 :: x2
  def Juxts1_ExpWild1(x1: AExp): List[AExp] = List(x1)
  def PostOp0(r: Range): Loc[UnaryOp] = Loc(PostDecOp,r)
  def PostOp1(r: Range): Loc[UnaryOp] = Loc(PostIncOp,r)
  def ExpHighNP0(x1: Loc[Group], x2: (KList[AExp],Loc[Group])): AExp = ArrayAExp(x2._1,Around(x1,x2._2))
  def ExpHighNP1(x1: AExp): AExp = x1
  def Commas1_ExpAssignNC0(x1: AExp, x3: CommaList1[AExp], x2r: Range): CommaList1[AExp] = x3.preComma(x1,x2r)
  def Commas1_ExpAssignNC1(x1: AExp): CommaList1[AExp] = SingleList(x1)
  def MaybeThen__Stmt0(x2: AStmt): AStmt = x2
  def Option_Type0(x1: AExp): Option[AExp] = Some(x1)
  def Option_Type1(): Option[AExp] = None
  def MaybeStmt__ElifTok__AfterIf0(x1: AStmt, x2: ElifTok, x3: PreIf, x2r: Range): (AStmt,Loc[ElifTok],PreIf) = (x1,Loc(x2,x2r),x3)
  def Ident__Right__MaybeStmt0(x1: Loc[String], x2: (Loc[Group],AStmt)): (Loc[String],Loc[Group],AStmt) = (x1,x2._1,x2._2)
  def Commas1_ExpCond_ExpJuxt0(x1: AExp, x3: CommaList1[AExp], x2r: Range): CommaList1[AExp] = x3.preComma(x1,x2r)
  def Commas1_ExpCond_ExpJuxt1(x1: AExp): CommaList1[AExp] = SingleList(x1)
  def ExpAndAnd_ExpWild0(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(AndAndOp,x2r,x1,x3)
  def ExpAndAnd_ExpWild1(x1: AExp): AExp = x1
  def ExpHigh0(x1: AExp): AExp = x1
  def ExpHigh1(x1: AExp): AExp = x1
  def ExpEq_ExpWild0(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(NeOp,x2r,x1,x3)
  def ExpEq_ExpWild1(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(EqOp,x2r,x1,x3)
  def ExpEq_ExpWild2(x1: AExp): AExp = x1
  def Dims__ForeachSep__ExpAssign0(x1: List[SGroup], x2: (Long,AExp)): (List[SGroup],Long,AExp) = (x1,x2._1,x2._2)
  def Commas2_Type0(x1: AExp, x3: CommaList1[AExp], x2r: Range): CommaList2[AExp] = x3.preComma(x1,x2r)
  def Commas0_StmtHelperBS0(x1: CommaList1[AStmt]): CommaList[AStmt] = x1
  def Commas0_StmtHelperBS1(): CommaList[AStmt] = EmptyList
  def Juxts2_ExpWildNA0(x1: AExp, x2: List[AExp]): List[AExp] = x1 :: x2
  def ExpParens0(x2: KList[AExp], x3: Loc[Group], x1r: Range): AExp = ArrayAExp(x2,Around(Paren,x1r,x3))
  def ExpParens1(x2: AExp, x3: Loc[Group], x1r: Range): AExp = ParenAExp(x2,Around(Paren,x1r,x3))
  def MaybeStmt0(x1: AStmt): AStmt = x1
  def MaybeStmt1(): AStmt = HoleAStmt(SRange.empty)
  def AfterIfB0(x1: AExp, x2: (AStmt,Long,AStmt)): PreIf = PreIf(ir => IfElseAStmt(ir,x1,NoAround(x1.r),x2._1,x2._2,x2._3))
  def AfterIfB1(x1: AExp): PreIf = { val er = x1.r; PreIf(ir => IfAStmt(ir,x1,NoAround(er),EmptyAStmt(er.after))) }
  def AfterIfB2(x1: (AExp,Around), x2: AStmt): PreIf = PreIf(ir => IfAStmt(ir,x1._1,x1._2,x2))
  def AfterIfB3(x1: (AExp,Around), x2: (AStmt,Long,AStmt)): PreIf = PreIf(ir => IfElseAStmt(ir,x1._1,x1._2,x2._1,x2._2,x2._3))
  def AfterIfB4(x1: (AExp,Around), x2: (AStmt,Loc[ElifTok],PreIf)): PreIf = PreIf(ir => IfElseAStmt(ir,x1._1,x1._2,x2._1,x2._2.r,x2._3(x2._2.r)))
  def AfterIfB5(x1: AExp, x2: AStmt): PreIf = PreIf(ir => IfAStmt(ir,x1,NoAround(x1.r),x2))
  def AfterIfB6(x1: AExp, x2: (AStmt,Loc[ElifTok],PreIf)): PreIf = PreIf(ir => IfElseAStmt(ir,x1,NoAround(x1.r),x2._1,x2._2.r,x2._3(x2._2.r)))
  def AfterIfB7(x1: AExp, x3: AStmt): PreIf = PreIf(ir => IfAStmt(ir,x1,NoAround(x1.r),x3))
  def AfterIfB8(x1: AExp): PreIf = { val er = x1.r; PreIf(ir => IfElseAStmt(ir,x1,NoAround(er),EmptyAStmt(er.after),er.after,HoleAStmt(er.after))) }
  def TypeArgs0(x2: KList[AExp], x1r: Range, x4r: Range): Grouped[KList[AExp]] = Grouped(x2,SGroup(x1r,x4r))
  def ExpOr_ExpJuxt0(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(OrOp,x2r,x1,x3)
  def ExpOr_ExpJuxt1(x1: AExp): AExp = x1
  def Stmt0(x1: AStmt, x2r: Range): AStmt = SemiAStmt(x1,x2r)
  def Stmt1(x1: AStmt): AStmt = x1
  def Stmt2(x1r: Range): AStmt = SemiAStmt(EmptyAStmt(x1r.before),x1r)
  def MaybeParenExp__MaybeDo0(x1: (AExp,Around), x2: Option[SRange]): ((AExp,Around),Option[SRange]) = (x1,x2)
  def Juxts2_VarDecl0(x1: AVarDecl, x2: List[AVarDecl]): List[AVarDecl] = x1 :: x2
  def MaybeThen0(x1r: Range): Option[SRange] = Some(x1r)
  def MaybeThen1(): Option[SRange] = None
  def Juxts1_ExpWildNA0(x1: AExp, x2: List[AExp]): List[AExp] = x1 :: x2
  def Juxts1_ExpWildNA1(x1: AExp): List[AExp] = List(x1)
  def ExpAndAnd_ExpJuxtNP0(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(AndAndOp,x2r,x1,x3)
  def ExpAndAnd_ExpJuxtNP1(x1: AExp): AExp = x1
  def ExpWildNP0(x2: Option[WildBound], x1r: Range): AExp = WildAExp(x1r,x2)
  def ExpWildNP1(x1: AExp): AExp = x1
  def ParenExp__MaybeThen0(x1: (AExp,Around)): (AExp,Around) = x1
  def MaybeParenExp0(x1: (AExp,Around)): (AExp,Around) = x1
  def MaybeParenExp1(x1: AExp): (AExp,Around) = (x1,NoAround(x1.r))
  def DimExps0(x1: (Long,Option[AExp]), x2: (Loc[Group],List[Grouped[Option[AExp]]])): List[Grouped[Option[AExp]]] = Grouped(x1._2,SGroup(x1._1,x2._1.r)) :: x2._2
  def DimExps1(): List[Grouped[Option[AExp]]] = Nil
  def ExpShift_ExpJuxtNP0(x1: AExp, x7: AExp, x2r: Range, x6r: Range): AExp = BinaryAExp(UnsignedRShiftOp,x2r union x6r,x1,x7)
  def ExpShift_ExpJuxtNP1(x1: AExp, x5: AExp, x2r: Range, x4r: Range): AExp = BinaryAExp(RShiftOp,x2r union x4r,x1,x5)
  def ExpShift_ExpJuxtNP2(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(LShiftOp,x2r,x1,x3)
  def ExpShift_ExpJuxtNP3(x1: AExp): AExp = x1
  def ExpJuxt0(x1: AExp, x2: KList[AExp]): AExp = ApplyAExp(x1,x2,NoAround(x2.list.head.r union x2.list.last.r))
  def ExpJuxt1(x1: AExp): AExp = x1
  def ExpMul_ExpWild0(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(ModOp,x2r,x1,x3)
  def ExpMul_ExpWild1(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(DivOp,x2r,x1,x3)
  def ExpMul_ExpWild2(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(MulOp,x2r,x1,x3)
  def ExpMul_ExpWild3(x1: AExp): AExp = x1
  def LeftNP0(r: Range): Loc[Group] = Loc(Curly,r)
  def LeftNP1(r: Range): Loc[Group] = Loc(Brack,r)
  def Juxts1_ExpAssignNC0(x1: AExp, x2: List[AExp]): List[AExp] = x1 :: x2
  def Juxts1_ExpAssignNC1(x1: AExp): List[AExp] = List(x1)
  def ExpAnd_ExpJuxtNP0(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(AndOp,x2r,x1,x3)
  def ExpAnd_ExpJuxtNP1(x1: AExp): AExp = x1
  def Option_FinallyBlock0(x1: (SRange,AStmt)): Option[(SRange,AStmt)] = Some(x1)
  def Option_FinallyBlock1(): Option[(SRange,AStmt)] = None
  def ExpAdd_ExpJuxt0(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(SubOp,x2r,x1,x3)
  def ExpAdd_ExpJuxt1(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(AddOp,x2r,x1,x3)
  def ExpAdd_ExpJuxt2(x1: AExp): AExp = x1
  def QuestionTok__ExpAssign__ColonTok__ExpCond_ExpJuxt0(x2: AExp, x4: AExp, x1r: Range, x3r: Range): (Long,AExp,Long,AExp) = (x1r,x2,x3r,x4)
  def ExpMul_ExpJuxt0(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(ModOp,x2r,x1,x3)
  def ExpMul_ExpJuxt1(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(DivOp,x2r,x1,x3)
  def ExpMul_ExpJuxt2(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(MulOp,x2r,x1,x3)
  def ExpMul_ExpJuxt3(x1: AExp): AExp = x1
  def Type__Right__MaybeStmt0(x1: AExp, x2: (Loc[Group],AStmt)): (AExp,Loc[Group],AStmt) = (x1,x2._1,x2._2)
  def CatchTok__Left0(x2: Loc[Group], x1r: Range): (Long,Loc[Group]) = (x1r,x2)
  def List1_Type0(x1: List[AExp]): KList[AExp] = JuxtList(x1)
  def List1_Type1(x1: CommaList2[AExp]): KList[AExp] = x1
  def List1_Type2(x1: AExp): KList[AExp] = SingleList(x1)
  def List_ExpAssignNC0(x1: KList[AExp]): KList[AExp] = x1
  def List_ExpAssignNC1(): KList[AExp] = EmptyList
  def ForTok__Left0(x2: Loc[Group], x1r: Range): (Long,Loc[Group]) = (x1r,x2)
  def ExpAssign__Right0(x1: AExp, x2: Loc[Group]): (AExp,Loc[Group]) = (x1,x2)
  def Left0(r: Range): Loc[Group] = Loc(Curly,r)
  def Left1(r: Range): Loc[Group] = Loc(Brack,r)
  def Left2(r: Range): Loc[Group] = Loc(Paren,r)
  def WhileUntil0(x1r: Range): Loc[Boolean] = Loc(true,x1r)
  def WhileUntil1(x1r: Range): Loc[Boolean] = Loc(false,x1r)
  def Mod0(x1r: Range): Loc[Mod] = Loc(Transient,x1r)
  def Mod1(x1r: Range): Loc[Mod] = Loc(Abstract,x1r)
  def Mod2(x2: Loc[String], x1r: Range, r: Range): Loc[Mod] = Loc(Annotation(x1r,x2.x,x2.r),r)
  def Mod3(x1r: Range): Loc[Mod] = Loc(Strictfp,x1r)
  def Mod4(x1r: Range): Loc[Mod] = Loc(Final,x1r)
  def Mod5(x1r: Range): Loc[Mod] = Loc(Protected,x1r)
  def Mod6(x1r: Range): Loc[Mod] = Loc(Static,x1r)
  def Mod7(x1r: Range): Loc[Mod] = Loc(Private,x1r)
  def Mod8(x1r: Range): Loc[Mod] = Loc(Public,x1r)
  def Mod9(x1r: Range): Loc[Mod] = Loc(Synchronized,x1r)
  def Mod10(x1r: Range): Loc[Mod] = Loc(Volatile,x1r)
  def ExpPrimary0(x1: AExp, x2: Grouped[KList[AExp]]): AExp = TypeApplyAExp(x1,x2.x,x2.a,after=true)
  def ExpPrimary1(x1: AExp, x2: (Long,Option[Grouped[KList[AExp]]],Loc[String])): AExp = FieldAExp(x1,x2._1,x2._2,x2._3.x,x2._3.r)
  def ExpPrimary2(x1: Loc[String]): AExp = NameAExp(x1.x,x1.r)
  def ExpPrimary3(x1: ALit): AExp = x1
  def ExpAssignNP__DoTok__Stmt0(x1: AExp, x3: AStmt): (AExp,AStmt) = (x1,x3)
  def NewTok__Option_TypeArgs0(x2: Option[Grouped[KList[AExp]]], x1r: Range): (Long,Option[Grouped[KList[AExp]]]) = (x1r,x2)
  def DoTok__MaybeStmt0(x2: AStmt, x1r: Range): (Long,AStmt) = (x1r,x2)
  def Juxts0_Mod0(x1: List[Loc[Mod]]): List[Loc[Mod]] = x1
  def Juxts0_Mod1(): List[Loc[Mod]] = Nil
  def CatchTok__Ident__ColonTok__Juxts0_Mod0(x2: Loc[String], x4: List[Loc[Mod]], x1r: Range): (Long,Loc[String],List[Loc[Mod]]) = (x1r,x2,x4)
  def ExpShift_ExpWild0(x1: AExp, x7: AExp, x2r: Range, x6r: Range): AExp = BinaryAExp(UnsignedRShiftOp,x2r union x6r,x1,x7)
  def ExpShift_ExpWild1(x1: AExp, x5: AExp, x2r: Range, x4r: Range): AExp = BinaryAExp(RShiftOp,x2r union x4r,x1,x5)
  def ExpShift_ExpWild2(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(LShiftOp,x2r,x1,x3)
  def ExpShift_ExpWild3(x1: AExp): AExp = x1
  def ExpOr_ExpWild0(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(OrOp,x2r,x1,x3)
  def ExpOr_ExpWild1(x1: AExp): AExp = x1
  def ExpRel_ExpJuxt0(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(GtOp,x2r,x1,x3)
  def ExpRel_ExpJuxt1(x1: AExp, x3: AExp, x2r: Range): AExp = InstanceofAExp(x1,x2r,x3)
  def ExpRel_ExpJuxt2(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(LtOp,x2r,x1,x3)
  def ExpRel_ExpJuxt3(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(GeOp,x2r,x1,x3)
  def ExpRel_ExpJuxt4(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(LeOp,x2r,x1,x3)
  def ExpRel_ExpJuxt5(x1: AExp): AExp = x1
  def Juxts0_Mod__Type0(x1: List[Loc[Mod]], x2: AExp): (List[Loc[Mod]],AExp) = (x1,x2)
  def ExpRel_ExpJuxtNP0(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(GtOp,x2r,x1,x3)
  def ExpRel_ExpJuxtNP1(x1: AExp, x3: AExp, x2r: Range): AExp = InstanceofAExp(x1,x2r,x3)
  def ExpRel_ExpJuxtNP2(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(LtOp,x2r,x1,x3)
  def ExpRel_ExpJuxtNP3(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(GeOp,x2r,x1,x3)
  def ExpRel_ExpJuxtNP4(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(LeOp,x2r,x1,x3)
  def ExpRel_ExpJuxtNP5(x1: AExp): AExp = x1
  def ExpAdd_ExpJuxtNP0(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(SubOp,x2r,x1,x3)
  def ExpAdd_ExpJuxtNP1(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(AddOp,x2r,x1,x3)
  def ExpAdd_ExpJuxtNP2(x1: AExp): AExp = x1
  def ExpNew0(x1: (Long,Option[Grouped[KList[AExp]]]), x2: (AExp,List[Grouped[Option[AExp]]])): AExp = NewAExp(x1._1,x1._2,x2._1,x2._2)
  def ExpNew1(x1: Grouped[KList[AExp]], x2: AExp): AExp = TypeApplyAExp(x2,x1.x,x1.a,after=false)
  def ExpNew2(x1: AExp): AExp = x1
  def Right__DimExps0(x1: Loc[Group], x2: List[Grouped[Option[AExp]]]): (Loc[Group],List[Grouped[Option[AExp]]]) = (x1,x2)
  def Ident__ColonTok__Juxts0_Mod0(x1: Loc[String], x3: List[Loc[Mod]]): (Loc[String],List[Loc[Mod]]) = (x1,x3)
  def Juxts1_Type0(x1: AExp, x2: List[AExp]): List[AExp] = x1 :: x2
  def Juxts1_Type1(x1: AExp): List[AExp] = List(x1)
  def Right__Stmt0(x1: Loc[Group], x2: AStmt): (Loc[Group],AStmt) = (x1,x2)
  def Commas2_ExpCond_ExpJuxt0(x1: AExp, x3: CommaList1[AExp], x2r: Range): CommaList2[AExp] = x3.preComma(x1,x2r)
  def Juxts2_Type0(x1: AExp, x2: List[AExp]): List[AExp] = x1 :: x2
  def AfterIfA0(x1: AExp, x3: AStmt, x4r: Range): PreIf = PreIf(ir => IfElseAStmt(ir,x1,NoAround(x1.r),x3,x4r,HoleAStmt(x4r.after)))
  def AfterIfA1(x1: (AExp,Around), x2: (AStmt,Long)): PreIf = PreIf(ir => IfElseAStmt(ir,x1._1,x1._2,x2._1,x2._2,HoleAStmt(x2._2.after)))
  def AfterIfA2(x1: (AExp,Around), x2: Option[SRange]): PreIf = PreIf(ir => IfAStmt(ir,x1._1,x1._2,HoleAStmt(x1._2.r.union(x2).after)))
  def LParenTok__Type0(x2: AExp, x1r: Range): (Long,AExp) = (x1r,x2)
  def CatchTok__Left__Juxts0_Mod__Type0(x1: (Long,Loc[Group]), x2: (List[Loc[Mod]],AExp)): (Long,Loc[Group],List[Loc[Mod]],AExp) = (x1._1,x1._2,x2._1,x2._2)
  def Stmts0(x1: AStmt, x3: List[AStmt], x2r: Range): List[AStmt] = SemiAStmt(x1,x2r) :: x3
  def Stmts1(x1: AStmt): List[AStmt] = List(x1)
  def Stmts2(x2: List[AStmt], x1r: Range): List[AStmt] = SemiAStmt(EmptyAStmt(x1r.before),x1r) :: x2
  def Stmts3(): List[AStmt] = Nil
  def AssignOp__ExpAssign0(x1: Loc[Option[AssignOp]], x2: AExp): (Loc[Option[AssignOp]],AExp) = (x1,x2)
  def ExpRel_ExpWild0(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(GtOp,x2r,x1,x3)
  def ExpRel_ExpWild1(x1: AExp, x3: AExp, x2r: Range): AExp = InstanceofAExp(x1,x2r,x3)
  def ExpRel_ExpWild2(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(LtOp,x2r,x1,x3)
  def ExpRel_ExpWild3(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(GeOp,x2r,x1,x3)
  def ExpRel_ExpWild4(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(LeOp,x2r,x1,x3)
  def ExpRel_ExpWild5(x1: AExp): AExp = x1
  def List_ExpAssignNC__Right0(x1: KList[AExp], x2: Loc[Group]): (KList[AExp],Loc[Group]) = (x1,x2)
  def StmtHelper0(x1: AStmt): AStmt = x1
  def StmtHelper1(x2: PreIf, x1r: Range): AStmt = x2(x1r)
  def StmtHelper2(x1: (Long,Loc[Group]), x2: (ForInfo,Loc[Group])): AStmt = ForAStmt(x1._1,x2._1,Around(x1._2,x2._2),HoleAStmt(x2._2.r.after))
  def StmtHelper3(x2: (AExp,Around), x1r: Range): AStmt = SyncAStmt(x1r,x2._1,x2._2,HoleAStmt(x2._2.r.after))
  def StmtHelper4(x1: Loc[Boolean], x2: ((AExp,Around),Option[SRange])): AStmt = WhileAStmt(x1.r,x1.x,x2._1._1,x2._1._2,HoleAStmt(x2._1._2.r.union(x2._2).after))
  def StmtHelper5(x2: ForInfo, x1r: Range): AStmt = { val ir = x2.r; ForAStmt(x1r,x2,NoAround(ir),HoleAStmt(ir.after)) }
  def ExpUnary_ExpWild0(x1: (Long,AExp), x2: (Loc[Group],AExp)): AExp = CastAExp(x1._2,Around(Paren,x1._1,x2._1),x2._2)
  def ExpUnary_ExpWild1(x1: Loc[UnaryOp], x2: AExp): AExp = UnaryAExp(x1.x,x1.r,x2)
  def ExpUnary_ExpWild2(x1: AExp, x2: Loc[UnaryOp]): AExp = UnaryAExp(x2.x,x2.r,x1)
  def ExpUnary_ExpWild3(x1: AExp): AExp = x1
  def Dims__EqTok__ExpCommas0(x1: List[SGroup], x3: AExp, x2r: Range): (List[SGroup],Long,AExp) = (x1,x2r,x3)
  def ExpJuxtNP0(x1: AExp, x2: KList[AExp]): AExp = ApplyAExp(x1,x2,NoAround(x2.list.head.r union x2.list.last.r))
  def ExpJuxtNP1(x1: AExp): AExp = x1
  def ExpXor_ExpWild0(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(XorOp,x2r,x1,x3)
  def ExpXor_ExpWild1(x1: AExp): AExp = x1
  def Lit0(x1: IntLitTok, r: Range): ALit = IntALit(x1.s,r)
  def Lit1(x1: DoubleLitTok, r: Range): ALit = DoubleALit(x1.s,r)
  def Lit2(x1: FloatLitTok, r: Range): ALit = FloatALit(x1.s,r)
  def Lit3(x1: LongLitTok, r: Range): ALit = LongALit(x1.s,r)
  def Lit4(x1: StringLitTok, r: Range): ALit = StringALit(x1.s,r)
  def Lit5(x1: CharLitTok, r: Range): ALit = CharALit(x1.s,r)
  def LBrackTok__Option_ExpAssign0(x2: Option[AExp], x1r: Range): (Long,Option[AExp]) = (x1r,x2)
  def List_Type0(x1: KList[AExp]): KList[AExp] = x1
  def List_Type1(): KList[AExp] = EmptyList
  def MaybeDo0(x1r: Range): Option[SRange] = Some(x1r)
  def MaybeDo1(): Option[SRange] = None
  def ExpOr_ExpJuxtNP0(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(OrOp,x2r,x1,x3)
  def ExpOr_ExpJuxtNP1(x1: AExp): AExp = x1
  def ExpWildNA0(x2: Option[WildBound], x1r: Range): AExp = WildAExp(x1r,x2)
  def ExpWildNA1(x1: AExp): AExp = x1
  def WhileUntil__ParenExp0(x1: Loc[Boolean], x2: (AExp,Around)): (Loc[Boolean],(AExp,Around)) = (x1,x2)
  def CatchTok__Juxts0_Mod__Type0(x2: List[Loc[Mod]], x3: AExp, x1r: Range): (Long,List[Loc[Mod]],AExp) = (x1r,x2,x3)
  def Commas1_VarDecl0(x1: AVarDecl, x3: CommaList1[AVarDecl], x2r: Range): CommaList1[AVarDecl] = x3.preComma(x1,x2r)
  def Commas1_VarDecl1(x1: AVarDecl): CommaList1[AVarDecl] = SingleList(x1)
  def Block0(x2: List[AStmt], x3: Loc[Group], x1r: Range): AStmt = BlockAStmt(x2,SGroup(x1r,x3.r))
  def Block1(x1: StmtTok, x1r: Range): AStmt = TokAStmt(x1,x1r)
  def ExpAnd_ExpWild0(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(AndOp,x2r,x1,x3)
  def ExpAnd_ExpWild1(x1: AExp): AExp = x1
  def Right0(r: Range): Loc[Group] = Loc(Curly,r)
  def Right1(r: Range): Loc[Group] = Loc(Brack,r)
  def Right2(r: Range): Loc[Group] = Loc(Paren,r)
  def ExpXor_ExpJuxt0(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(XorOp,x2r,x1,x3)
  def ExpXor_ExpJuxt1(x1: AExp): AExp = x1
  def ArrayInterior0(x1: KList[AExp]): KList[AExp] = x1
  def ArrayInterior1(x1: AExp): KList[AExp] = SingleList(x1)
  def CatchBlock0(x1: (Long,Loc[Group],List[Loc[Mod]],AExp), x2: (Loc[String],Loc[Group],AStmt)): (CatchInfo,AStmt) = (CatchInfo(x1._1,x1._3,Some(x1._4),Some(x2._1),Around(x1._2,x2._2),false),x2._3)
  def CatchBlock1(x1: (Long,Loc[String],List[Loc[Mod]]), x2: (AExp,AStmt)): (CatchInfo,AStmt) = (CatchInfo(x1._1,x1._3,Some(x2._1),Some(x1._2),NoAround(x1._2.r union x2._1.r),true),x2._2)
  def CatchBlock2(x1: (Long,Loc[Group],Loc[String],List[Loc[Mod]]), x2: (AExp,Loc[Group],AStmt)): (CatchInfo,AStmt) = (CatchInfo(x1._1,x1._4,Some(x2._1),Some(x1._3),Around(x1._2,x2._2),true),x2._3)
  def CatchBlock3(x3: AStmt, x1r: Range, x2r: Range): (CatchInfo,AStmt) = (CatchInfo(x1r,Nil,None,None,NoAround(x2r),false),x3)
  def CatchBlock4(x1: (Long,Loc[Group]), x2: (Loc[Group],AStmt)): (CatchInfo,AStmt) = (CatchInfo(x1._1,Nil,None,None,Around(x1._2,x2._1),false),x2._2)
  def CatchBlock5(x1: (Long,List[Loc[Mod]],AExp), x2: (Loc[String],AStmt)): (CatchInfo,AStmt) = (CatchInfo(x1._1,x1._2,Some(x1._3),Some(x2._1),NoAround(x1._2.headOption.map(_.r).getOrElse(x1._3.r) union x2._1.r),false),x2._2)
  def Ident0(x1r: Range): Loc[String] = Loc("until",x1r)
  def Ident1(x1r: Range): Loc[String] = Loc("in",x1r)
  def Ident2(x1r: Range): Loc[String] = Loc("then",x1r)
  def Ident3(x1: ElifTok, x1r: Range): Loc[String] = Loc(x1.s,x1r)
  def Ident4(x1: IdentTok, x1r: Range): Loc[String] = Loc(x1.name,x1r)
  def Ident5(x1r: Range): Loc[String] = Loc("super",x1r)
  def Ident6(x1r: Range): Loc[String] = Loc("this",x1r)
  def ForeachSep0(x1r: Range): Long = x1r
  def ForeachSep1(x1r: Range): Long = x1r
  def Commas0_ExpAssignNC0(x1: CommaList1[AExp]): CommaList[AExp] = x1
  def Commas0_ExpAssignNC1(): CommaList[AExp] = EmptyList
  def Type__MaybeStmt0(x1: AExp, x2: AStmt): (AExp,AStmt) = (x1,x2)
  def ExpCond_ExpJuxt0(x1: AExp, x2: (Long,AExp,Long,AExp)): AExp = CondAExp(x1,x2._1,x2._2,x2._3,x2._4)
  def ExpCond_ExpJuxt1(x1: AExp): AExp = x1
  def ExpAssignNP0(x1: AExp, x2: (Loc[Option[AssignOp]],AExp)): AExp = AssignAExp(x2._1.x,x2._1.r,x1,x2._2)
  def ExpAssignNP1(x1: AExp): AExp = x1
  def SemiTok__Option_ExpAssign__SemiTok__Commas0_ExpAssignNC0(x2: Option[AExp], x4: CommaList[AExp], x1r: Range, x3r: Range): (Long,Option[AExp],Long,CommaList[AExp]) = (x1r,x2,x3r,x4)
  def ExpCond_ExpJuxtNP0(x1: AExp, x2: (Long,AExp,Long,AExp)): AExp = CondAExp(x1,x2._1,x2._2,x2._3,x2._4)
  def ExpCond_ExpJuxtNP1(x1: AExp): AExp = x1
  def ExpPrimary__DimExps0(x1: AExp, x2: List[Grouped[Option[AExp]]]): (AExp,List[Grouped[Option[AExp]]]) = (x1,x2)
  def Commas2_VarDecl0(x1: AVarDecl, x3: CommaList1[AVarDecl], x2r: Range): CommaList2[AVarDecl] = x3.preComma(x1,x2r)
  def WhileUntil__MaybeParenExp0(x1: Loc[Boolean], x2: (AExp,Around)): (Loc[Boolean],(AExp,Around)) = (x1,x2)
  def ExpEq_ExpJuxtNP0(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(NeOp,x2r,x1,x3)
  def ExpEq_ExpJuxtNP1(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(EqOp,x2r,x1,x3)
  def ExpEq_ExpJuxtNP2(x1: AExp): AExp = x1
  def ExpAnd_ExpJuxt0(x1: AExp, x3: AExp, x2r: Range): AExp = BinaryAExp(AndOp,x2r,x1,x3)
  def ExpAnd_ExpJuxt1(x1: AExp): AExp = x1
  def Option_ExpJuxt0(x1: AExp): Option[AExp] = Some(x1)
  def Option_ExpJuxt1(): Option[AExp] = None
  def Type0(x2: Option[WildBound], x1r: Range): AExp = WildAExp(x1r,x2)
  def Type1(x1: AExp): AExp = x1
}
