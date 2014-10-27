// Autogenerated by ambiguity.  DO NOT EDIT!
package ambiguity
import tarski.Tokens._
import tarski.AST._

object ParseEddy {
  def parse(input: List[Token]): List[Stmt] = {
    type R = (Int,Int)
    import scala.collection.mutable
    val debug = false
    
    // Functions for matching tokens
    val array = input.toArray
    def tok(i: Int) = array(i)
    def P_PlusEqTok(i: Int) = array(i) match { case t: PlusEqTok => List(t); case _ => Nil }
    def P_OrTok(i: Int) = array(i) match { case t: OrTok => List(t); case _ => Nil }
    def P_SynchronizedTok(i: Int) = array(i) match { case t: SynchronizedTok => List(t); case _ => Nil }
    def P_ModTok(i: Int) = array(i) match { case t: ModTok => List(t); case _ => Nil }
    def P_CommaTok(i: Int) = array(i) match { case t: CommaTok => List(t); case _ => Nil }
    def P_SuperTok(i: Int) = array(i) match { case t: SuperTok => List(t); case _ => Nil }
    def P_CompTok(i: Int) = array(i) match { case t: CompTok => List(t); case _ => Nil }
    def P_AbstractTok(i: Int) = array(i) match { case t: AbstractTok => List(t); case _ => Nil }
    def P_ProtectedTok(i: Int) = array(i) match { case t: ProtectedTok => List(t); case _ => Nil }
    def P_StringLitTok(i: Int) = array(i) match { case t: StringLitTok => List(t); case _ => Nil }
    def P_DotTok(i: Int) = array(i) match { case t: DotTok => List(t); case _ => Nil }
    def P_NotTok(i: Int) = array(i) match { case t: NotTok => List(t); case _ => Nil }
    def P_ColonTok(i: Int) = array(i) match { case t: ColonTok => List(t); case _ => Nil }
    def P_InstanceofTok(i: Int) = array(i) match { case t: InstanceofTok => List(t); case _ => Nil }
    def P_ReturnTok(i: Int) = array(i) match { case t: ReturnTok => List(t); case _ => Nil }
    def P_FinalTok(i: Int) = array(i) match { case t: FinalTok => List(t); case _ => Nil }
    def P_FloatLitTok(i: Int) = array(i) match { case t: FloatLitTok => List(t); case _ => Nil }
    def P_UnsignedRShiftTok(i: Int) = array(i) match { case t: UnsignedRShiftTok => List(t); case _ => Nil }
    def P_LeTok(i: Int) = array(i) match { case t: LeTok => List(t); case _ => Nil }
    def P_IdentTok(i: Int) = array(i) match { case t: IdentTok => List(t); case _ => Nil }
    def P_TransientTok(i: Int) = array(i) match { case t: TransientTok => List(t); case _ => Nil }
    def P_UnsignedRShiftEqTok(i: Int) = array(i) match { case t: UnsignedRShiftEqTok => List(t); case _ => Nil }
    def P_LtTok(i: Int) = array(i) match { case t: LtTok => List(t); case _ => Nil }
    def P_IntLitTok(i: Int) = array(i) match { case t: IntLitTok => List(t); case _ => Nil }
    def P_ColonColonTok(i: Int) = array(i) match { case t: ColonColonTok => List(t); case _ => Nil }
    def P_AndAndTok(i: Int) = array(i) match { case t: AndAndTok => List(t); case _ => Nil }
    def P_RShiftEqTok(i: Int) = array(i) match { case t: RShiftEqTok => List(t); case _ => Nil }
    def P_ContinueTok(i: Int) = array(i) match { case t: ContinueTok => List(t); case _ => Nil }
    def P_VolatileTok(i: Int) = array(i) match { case t: VolatileTok => List(t); case _ => Nil }
    def P_RCurlyTok(i: Int) = array(i) match { case t: RCurlyTok => List(t); case _ => Nil }
    def P_NewTok(i: Int) = array(i) match { case t: NewTok => List(t); case _ => Nil }
    def P_LCurlyTok(i: Int) = array(i) match { case t: LCurlyTok => List(t); case _ => Nil }
    def P_XorEqTok(i: Int) = array(i) match { case t: XorEqTok => List(t); case _ => Nil }
    def P_EqTok(i: Int) = array(i) match { case t: EqTok => List(t); case _ => Nil }
    def P_PrivateTok(i: Int) = array(i) match { case t: PrivateTok => List(t); case _ => Nil }
    def P_BreakTok(i: Int) = array(i) match { case t: BreakTok => List(t); case _ => Nil }
    def P_LParenTok(i: Int) = array(i) match { case t: LParenTok => List(t); case _ => Nil }
    def P_MulTok(i: Int) = array(i) match { case t: MulTok => List(t); case _ => Nil }
    def P_DivTok(i: Int) = array(i) match { case t: DivTok => List(t); case _ => Nil }
    def P_DoubleLitTok(i: Int) = array(i) match { case t: DoubleLitTok => List(t); case _ => Nil }
    def P_PlusPlusTok(i: Int) = array(i) match { case t: PlusPlusTok => List(t); case _ => Nil }
    def P_SemiTok(i: Int) = array(i) match { case t: SemiTok => List(t); case _ => Nil }
    def P_StaticTok(i: Int) = array(i) match { case t: StaticTok => List(t); case _ => Nil }
    def P_LBrackTok(i: Int) = array(i) match { case t: LBrackTok => List(t); case _ => Nil }
    def P_XorTok(i: Int) = array(i) match { case t: XorTok => List(t); case _ => Nil }
    def P_MinusMinusTok(i: Int) = array(i) match { case t: MinusMinusTok => List(t); case _ => Nil }
    def P_RBrackTok(i: Int) = array(i) match { case t: RBrackTok => List(t); case _ => Nil }
    def P_NeTok(i: Int) = array(i) match { case t: NeTok => List(t); case _ => Nil }
    def P_RParenTok(i: Int) = array(i) match { case t: RParenTok => List(t); case _ => Nil }
    def P_AndEqTok(i: Int) = array(i) match { case t: AndEqTok => List(t); case _ => Nil }
    def P_CharLitTok(i: Int) = array(i) match { case t: CharLitTok => List(t); case _ => Nil }
    def P_MinusEqTok(i: Int) = array(i) match { case t: MinusEqTok => List(t); case _ => Nil }
    def P_GtTok(i: Int) = array(i) match { case t: GtTok => List(t); case _ => Nil }
    def P_BoolLitTok(i: Int) = array(i) match { case t: BoolLitTok => List(t); case _ => Nil }
    def P_StrictfpTok(i: Int) = array(i) match { case t: StrictfpTok => List(t); case _ => Nil }
    def P_EqEqTok(i: Int) = array(i) match { case t: EqEqTok => List(t); case _ => Nil }
    def P_ExtendsTok(i: Int) = array(i) match { case t: ExtendsTok => List(t); case _ => Nil }
    def P_GeTok(i: Int) = array(i) match { case t: GeTok => List(t); case _ => Nil }
    def P_ThrowTok(i: Int) = array(i) match { case t: ThrowTok => List(t); case _ => Nil }
    def P_NullLitTok(i: Int) = array(i) match { case t: NullLitTok => List(t); case _ => Nil }
    def P_QuestionTok(i: Int) = array(i) match { case t: QuestionTok => List(t); case _ => Nil }
    def P_AssertTok(i: Int) = array(i) match { case t: AssertTok => List(t); case _ => Nil }
    def P_AndTok(i: Int) = array(i) match { case t: AndTok => List(t); case _ => Nil }
    def P_RShiftTok(i: Int) = array(i) match { case t: RShiftTok => List(t); case _ => Nil }
    def P_OrEqTok(i: Int) = array(i) match { case t: OrEqTok => List(t); case _ => Nil }
    def P_LShiftEqTok(i: Int) = array(i) match { case t: LShiftEqTok => List(t); case _ => Nil }
    def P_LShiftTok(i: Int) = array(i) match { case t: LShiftTok => List(t); case _ => Nil }
    def P_ModEqTok(i: Int) = array(i) match { case t: ModEqTok => List(t); case _ => Nil }
    def P_PlusTok(i: Int) = array(i) match { case t: PlusTok => List(t); case _ => Nil }
    def P_OrOrTok(i: Int) = array(i) match { case t: OrOrTok => List(t); case _ => Nil }
    def P_LongLitTok(i: Int) = array(i) match { case t: LongLitTok => List(t); case _ => Nil }
    def P_AtTok(i: Int) = array(i) match { case t: AtTok => List(t); case _ => Nil }
    def P_MulEqTok(i: Int) = array(i) match { case t: MulEqTok => List(t); case _ => Nil }
    def P_PublicTok(i: Int) = array(i) match { case t: PublicTok => List(t); case _ => Nil }
    def P_MinusTok(i: Int) = array(i) match { case t: MinusTok => List(t); case _ => Nil }
    def P_DivEqTok(i: Int) = array(i) match { case t: DivEqTok => List(t); case _ => Nil }
    
    // Allocate one sparse array per nonterminal
    val P_CommaTok__Commas_Type = mutable.Map[R,List[(CommaTok,List[Type])]]()
    val P_Exp__LParenTok = mutable.Map[R,List[(Exp,LParenTok)]]()
    val P_Stmts__RCurlyTok = mutable.Map[R,List[(List[Stmt],RCurlyTok)]]()
    val P_Type__List1_VarDecl = mutable.Map[R,List[(Type,KList[(NameDims,Option[Exp])])]]()
    val P_Exp__ColonTok__Exp = mutable.Map[R,List[(Exp,ColonTok,Exp)]]()
    val P_List_Type__GtTok = mutable.Map[R,List[(KList[Type],GtTok)]]()
    val P_Exp__RParenTok__Block = mutable.Map[R,List[(Exp,RParenTok,List[Stmt])]]()
    val P_AssignOp = mutable.Map[R,List[Option[AssignOp]]]()
    val P_WildcardBounds = mutable.Map[R,List[Option[(Bound,Type)]]]()
    val P_PreOp = mutable.Map[R,List[UnaryOp]]()
    val P_RParenTok__Block = mutable.Map[R,List[(RParenTok,List[Stmt])]]()
    val P_Exp__Block = mutable.Map[R,List[(Exp,List[Stmt])]]()
    val P_Option_IdentTok = mutable.Map[R,List[Option[IdentTok]]]()
    val P_Option_TypeArgs__IdentTok = mutable.Map[R,List[(Option[KList[Type]],IdentTok)]]()
    val P_PostOp = mutable.Map[R,List[UnaryOp]]()
    val P_BinaryOp = mutable.Map[R,List[BinaryOp]]()
    val P_TypeArgs = mutable.Map[R,List[KList[Type]]]()
    val P_Option_TypeArgs = mutable.Map[R,List[Option[KList[Type]]]]()
    val P_Option_SemiTok = mutable.Map[R,List[Option[SemiTok]]]()
    val P_BinaryOp__Exp = mutable.Map[R,List[(BinaryOp,Exp)]]()
    val P_AssertTok__Exp = mutable.Map[R,List[(AssertTok,Exp)]]()
    val P_CommaTok__Commas_VarDecl = mutable.Map[R,List[(CommaTok,List[(NameDims,Option[Exp])])]]()
    val P_Mod = mutable.Map[R,List[Mod]]()
    val P_Exp__QuestionTok = mutable.Map[R,List[(Exp,QuestionTok)]]()
    val P_AssignOp__Exp = mutable.Map[R,List[(Option[AssignOp],Exp)]]()
    val P_LBrackTok__RBrackTok = mutable.Map[R,List[(LBrackTok,RBrackTok)]]()
    val P_IdentDims = mutable.Map[R,List[NameDims]]()
    val P_VarDecl = mutable.Map[R,List[(NameDims,Option[Exp])]]()
    val P_Juxts_VarDecl = mutable.Map[R,List[List[(NameDims,Option[Exp])]]]()
    val P_Commas_VarDecl = mutable.Map[R,List[List[(NameDims,Option[Exp])]]]()
    val P_List1_VarDecl = mutable.Map[R,List[KList[(NameDims,Option[Exp])]]]()
    val P_List_Exp__RBrackTok = mutable.Map[R,List[(KList[Exp],RBrackTok)]]()
    val P_LParenTok__Type = mutable.Map[R,List[(LParenTok,Type)]]()
    val P_Exp__DotTok = mutable.Map[R,List[(Exp,DotTok)]]()
    val P_CommaTok__Commas_Exp = mutable.Map[R,List[(CommaTok,List[Exp])]]()
    val P_Lit = mutable.Map[R,List[Lit]]()
    val P_Exp = mutable.Map[R,List[Exp]]()
    val P_Option_TypeArgs__Exp = mutable.Map[R,List[(Option[KList[Type]],Exp)]]()
    val P_Juxts_Exp = mutable.Map[R,List[List[Exp]]]()
    val P_Option_Exp = mutable.Map[R,List[Option[Exp]]]()
    val P_Commas_Exp = mutable.Map[R,List[List[Exp]]]()
    val P_List1_Exp = mutable.Map[R,List[KList[Exp]]]()
    val P_List_Exp = mutable.Map[R,List[KList[Exp]]]()
    val P_Exp__RParenTok = mutable.Map[R,List[(Exp,RParenTok)]]()
    val P_Block = mutable.Map[R,List[List[Stmt]]]()
    val P_StmtHelper = mutable.Map[R,List[Stmt]]()
    val P_Stmt = mutable.Map[R,List[Stmt]]()
    val P_Stmts = mutable.Map[R,List[List[Stmt]]]()
    val P_EqTok__Exp = mutable.Map[R,List[(EqTok,Exp)]]()
    val P_Exp__ColonColonTok = mutable.Map[R,List[(Exp,ColonColonTok)]]()
    val P_DotTok__IdentTok = mutable.Map[R,List[(DotTok,IdentTok)]]()
    val P_Exp__LBrackTok = mutable.Map[R,List[(Exp,LBrackTok)]]()
    val P_List_Exp__RParenTok = mutable.Map[R,List[(KList[Exp],RParenTok)]]()
    val P_ColonTok__Exp = mutable.Map[R,List[(ColonTok,Exp)]]()
    val P_SynchronizedTok__LParenTok = mutable.Map[R,List[(SynchronizedTok,LParenTok)]]()
    val P_RParenTok__Exp = mutable.Map[R,List[(RParenTok,Exp)]]()
    val P_Type = mutable.Map[R,List[Type]]()
    val P_Commas_Type = mutable.Map[R,List[List[Type]]]()
    val P_Juxts_Type = mutable.Map[R,List[List[Type]]]()
    val P_List1_Type = mutable.Map[R,List[KList[Type]]]()
    val P_List_Type = mutable.Map[R,List[KList[Type]]]()
    val P_Option_TypeArgs__NewTok = mutable.Map[R,List[(Option[KList[Type]],NewTok)]]()
    
    // Parse bottom up for each nonterminal
    val n = input.length
    // Parse null productions
    for (lo <- 0 to n) {
      P_CommaTok__Commas_Type((lo,lo)) = List()
      P_Exp__LParenTok((lo,lo)) = List()
      P_Stmts__RCurlyTok((lo,lo)) = List()
      P_Type__List1_VarDecl((lo,lo)) = List()
      P_Exp__ColonTok__Exp((lo,lo)) = List()
      P_List_Type__GtTok((lo,lo)) = List()
      P_Exp__RParenTok__Block((lo,lo)) = List()
      P_AssignOp((lo,lo)) = List()
      P_WildcardBounds((lo,lo)) = List(None)
      P_PreOp((lo,lo)) = List()
      P_RParenTok__Block((lo,lo)) = List()
      P_Exp__Block((lo,lo)) = List()
      P_Option_IdentTok((lo,lo)) = List(None)
      P_Option_TypeArgs__IdentTok((lo,lo)) = List()
      P_PostOp((lo,lo)) = List()
      P_BinaryOp((lo,lo)) = List()
      P_TypeArgs((lo,lo)) = List()
      P_Option_TypeArgs((lo,lo)) = List(None)
      P_Option_SemiTok((lo,lo)) = List(None)
      P_BinaryOp__Exp((lo,lo)) = List()
      P_AssertTok__Exp((lo,lo)) = List()
      P_CommaTok__Commas_VarDecl((lo,lo)) = List()
      P_Mod((lo,lo)) = List()
      P_Exp__QuestionTok((lo,lo)) = List()
      P_AssignOp__Exp((lo,lo)) = List()
      P_LBrackTok__RBrackTok((lo,lo)) = List()
      P_IdentDims((lo,lo)) = List()
      P_VarDecl((lo,lo)) = List()
      P_Juxts_VarDecl((lo,lo)) = List()
      P_Commas_VarDecl((lo,lo)) = List()
      P_List1_VarDecl((lo,lo)) = List()
      P_List_Exp__RBrackTok((lo,lo)) = List()
      P_LParenTok__Type((lo,lo)) = List()
      P_Exp__DotTok((lo,lo)) = List()
      P_CommaTok__Commas_Exp((lo,lo)) = List()
      P_Lit((lo,lo)) = List()
      P_Exp((lo,lo)) = List()
      P_Option_TypeArgs__Exp((lo,lo)) = List()
      P_Juxts_Exp((lo,lo)) = List()
      P_Option_Exp((lo,lo)) = List(None)
      P_Commas_Exp((lo,lo)) = List()
      P_List1_Exp((lo,lo)) = List()
      P_List_Exp((lo,lo)) = List(EmptyList())
      P_Exp__RParenTok((lo,lo)) = List()
      P_Block((lo,lo)) = List()
      P_StmtHelper((lo,lo)) = List()
      P_Stmt((lo,lo)) = List()
      P_Stmts((lo,lo)) = List(Nil)
      P_EqTok__Exp((lo,lo)) = List()
      P_Exp__ColonColonTok((lo,lo)) = List()
      P_DotTok__IdentTok((lo,lo)) = List()
      P_Exp__LBrackTok((lo,lo)) = List()
      P_List_Exp__RParenTok((lo,lo)) = List()
      P_ColonTok__Exp((lo,lo)) = List()
      P_SynchronizedTok__LParenTok((lo,lo)) = List()
      P_RParenTok__Exp((lo,lo)) = List()
      P_Type((lo,lo)) = List()
      P_Commas_Type((lo,lo)) = List()
      P_Juxts_Type((lo,lo)) = List()
      P_List1_Type((lo,lo)) = List()
      P_List_Type((lo,lo)) = List(EmptyList())
      P_Option_TypeArgs__NewTok((lo,lo)) = List()
    }
    // Parse nonnull productions
    for (lo <- n to 0 by -1; hi <- lo+1 to n) {
      if (debug) println("\nparsing: "+array.slice(lo,hi).mkString(" "))
      def d[A](non: String, p: mutable.Map[R,List[A]]) = if (debug && !p((lo,hi)).isEmpty) println(s"  $non = "+p((lo,hi)).mkString(" "))
      def t[A,C](p: Int => List[A])(f: A => C) = if (lo+1==hi) p(lo).map(f) else Nil
      def n[A,C](p: R   => List[A])(f: A => C) = p(lo,hi).map(f)
      def s[A,C](p: R   => List[A])(f: A => C) = if (lo<hi) p(lo,hi).map(f) else Nil
      def tt[A,B,C](pa: Int => List[A], pb: Int => List[B])(f: (A,B) => C) = if (lo+2==hi) for (a <- pa(lo); b <- pb(lo+1)) yield f(a,b) else Nil
      def tn[A,B,C](pa: Int => List[A], pb: R   => List[B])(f: (A,B) => C) = if (lo<hi) for (a <- pa(lo); b <- pb((lo+1,hi))) yield f(a,b) else Nil
      def ts[A,B,C](pa: Int => List[A], pb: R   => List[B])(f: (A,B) => C) = if (lo+1<hi) for (a <- pa(lo); b <- pb((lo+1,hi))) yield f(a,b) else Nil
      def nt[A,B,C](pa: R   => List[A], pb: Int => List[B])(f: (A,B) => C) = if (lo<hi) for (b <- pb(hi-1); a <- pa((lo,hi-1))) yield f(a,b) else Nil
      def st[A,B,C](pa: R   => List[A], pb: Int => List[B])(f: (A,B) => C) = if (lo+1<hi) for (b <- pb(hi-1); a <- pa((lo,hi-1))) yield f(a,b) else Nil
      def nn[A,B,C](pa: R   => List[A], pb: R   => List[B])(f: (A,B) => C) = (for (m <- lo to hi; a <- pa((lo,m)); b <- pb((m,hi))) yield f(a,b)).toList
      def sn[A,B,C](pa: R   => List[A], pb: R   => List[B])(f: (A,B) => C) = (for (m <- lo+1 to hi; a <- pa((lo,m)); b <- pb((m,hi))) yield f(a,b)).toList
      def ns[A,B,C](pa: R   => List[A], pb: R   => List[B])(f: (A,B) => C) = (for (m <- lo to hi-1; a <- pa((lo,m)); b <- pb((m,hi))) yield f(a,b)).toList
      def ss[A,B,C](pa: R   => List[A], pb: R   => List[B])(f: (A,B) => C) = (for (m <- lo+1 to hi-1; a <- pa((lo,m)); b <- pb((m,hi))) yield f(a,b)).toList
      P_CommaTok__Commas_Type((lo,hi)) = ts(P_CommaTok,P_Commas_Type)((x,y) => (x,y)); d("CommaTok__Commas_Type",P_CommaTok__Commas_Type)
      P_Exp__LParenTok((lo,hi)) = st(P_Exp,P_LParenTok)((x,y) => (x,y)); d("Exp__LParenTok",P_Exp__LParenTok)
      P_Stmts__RCurlyTok((lo,hi)) = nt(P_Stmts,P_RCurlyTok)((x,y) => (x,y)); d("Stmts__RCurlyTok",P_Stmts__RCurlyTok)
      P_Type__List1_VarDecl((lo,hi)) = ss(P_Type,P_List1_VarDecl)((x,y) => (x,y)); d("Type__List1_VarDecl",P_Type__List1_VarDecl)
      P_Exp__ColonTok__Exp((lo,hi)) = ss(P_Exp,P_ColonTok__Exp)((x,y) => (x,y._1,y._2)); d("Exp__ColonTok__Exp",P_Exp__ColonTok__Exp)
      P_List_Type__GtTok((lo,hi)) = nt(P_List_Type,P_GtTok)((x,y) => (x,y)); d("List_Type__GtTok",P_List_Type__GtTok)
      P_Exp__RParenTok__Block((lo,hi)) = ss(P_Exp,P_RParenTok__Block)((x,y) => (x,y._1,y._2)); d("Exp__RParenTok__Block",P_Exp__RParenTok__Block)
      P_AssignOp((lo,hi)) = t(P_EqTok)(x => None) ::: t(P_MulEqTok)(x => Some(MulOp())) ::: t(P_DivEqTok)(x => Some(DivOp())) ::: t(P_ModEqTok)(x => Some(ModOp())) ::: t(P_PlusEqTok)(x => Some(AddOp())) ::: t(P_MinusEqTok)(x => Some(SubOp())) ::: t(P_LShiftEqTok)(x => Some(LShiftOp())) ::: t(P_RShiftEqTok)(x => Some(RShiftOp())) ::: t(P_UnsignedRShiftEqTok)(x => Some(UnsignedRShiftOp())) ::: t(P_AndEqTok)(x => Some(AndOp())) ::: t(P_XorEqTok)(x => Some(XorOp())) ::: t(P_OrEqTok)(x => Some(OrOp())); d("AssignOp",P_AssignOp)
      P_WildcardBounds((lo,hi)) = ts(P_ExtendsTok,P_Type)((x,y) => Some((Extends(),y))) ::: ts(P_ColonTok,P_Type)((x,y) => Some((Extends(),y))) ::: ts(P_SuperTok,P_Type)((x,y) => Some((Super(),y))); d("WildcardBounds",P_WildcardBounds)
      P_PreOp((lo,hi)) = t(P_PlusPlusTok)(x => PreIncOp()) ::: t(P_MinusMinusTok)(x => PreDecOp()) ::: t(P_PlusTok)(x => PosOp()) ::: t(P_MinusTok)(x => NegOp()) ::: t(P_CompTok)(x => CompOp()) ::: t(P_NotTok)(x => NotOp()); d("PreOp",P_PreOp)
      P_RParenTok__Block((lo,hi)) = ts(P_RParenTok,P_Block)((x,y) => (x,y)); d("RParenTok__Block",P_RParenTok__Block)
      P_Exp__Block((lo,hi)) = ss(P_Exp,P_Block)((x,y) => (x,y)); d("Exp__Block",P_Exp__Block)
      P_Option_IdentTok((lo,hi)) = t(P_IdentTok)(x => Some(x)); d("Option_IdentTok",P_Option_IdentTok)
      P_Option_TypeArgs__IdentTok((lo,hi)) = nt(P_Option_TypeArgs,P_IdentTok)((x,y) => (x,y)) ::: nt(P_Option_TypeArgs,P_IdentTok)((x,y) => (x,y)); d("Option_TypeArgs__IdentTok",P_Option_TypeArgs__IdentTok)
      P_PostOp((lo,hi)) = t(P_PlusPlusTok)(x => PostIncOp()) ::: t(P_MinusMinusTok)(x => PostDecOp()); d("PostOp",P_PostOp)
      P_BinaryOp((lo,hi)) = t(P_MulTok)(x => MulOp()) ::: t(P_DivTok)(x => DivOp()) ::: t(P_ModTok)(x => ModOp()) ::: t(P_PlusTok)(x => AddOp()) ::: t(P_MinusTok)(x => SubOp()) ::: t(P_LShiftTok)(x => LShiftOp()) ::: t(P_RShiftTok)(x => RShiftOp()) ::: t(P_UnsignedRShiftTok)(x => UnsignedRShiftOp()) ::: t(P_LtTok)(x => LtOp()) ::: t(P_GtTok)(x => GtOp()) ::: t(P_LeTok)(x => LeOp()) ::: t(P_GeTok)(x => GeOp()) ::: t(P_InstanceofTok)(x => InstanceofOp()) ::: t(P_EqEqTok)(x => EqOp()) ::: t(P_NeTok)(x => NeOp()) ::: t(P_AndTok)(x => AndOp()) ::: t(P_XorTok)(x => XorOp()) ::: t(P_OrTok)(x => OrOp()) ::: t(P_AndAndTok)(x => AndAndOp()) ::: t(P_OrOrTok)(x => OrOrOp()); d("BinaryOp",P_BinaryOp)
      P_TypeArgs((lo,hi)) = ts(P_LtTok,P_List_Type__GtTok)((x,y) => y._1); d("TypeArgs",P_TypeArgs)
      P_Option_TypeArgs((lo,hi)) = s(P_TypeArgs)(x => Some(x)); d("Option_TypeArgs",P_Option_TypeArgs)
      P_Option_SemiTok((lo,hi)) = t(P_SemiTok)(x => Some(x)); d("Option_SemiTok",P_Option_SemiTok)
      P_BinaryOp__Exp((lo,hi)) = ss(P_BinaryOp,P_Exp)((x,y) => (x,y)); d("BinaryOp__Exp",P_BinaryOp__Exp)
      P_AssertTok__Exp((lo,hi)) = ts(P_AssertTok,P_Exp)((x,y) => (x,y)); d("AssertTok__Exp",P_AssertTok__Exp)
      P_CommaTok__Commas_VarDecl((lo,hi)) = ts(P_CommaTok,P_Commas_VarDecl)((x,y) => (x,y)); d("CommaTok__Commas_VarDecl",P_CommaTok__Commas_VarDecl)
      P_Mod((lo,hi)) = tt(P_AtTok,P_IdentTok)((x,y) => Annotation(y.name)) ::: t(P_PublicTok)(x => Public()) ::: t(P_ProtectedTok)(x => Protected()) ::: t(P_PrivateTok)(x => Private()) ::: t(P_AbstractTok)(x => Abstract()) ::: t(P_StaticTok)(x => Static()) ::: t(P_FinalTok)(x => Final()) ::: t(P_StrictfpTok)(x => Strictfp()) ::: t(P_TransientTok)(x => Transient()) ::: t(P_VolatileTok)(x => Volatile()) ::: t(P_SynchronizedTok)(x => Synchronized()); d("Mod",P_Mod)
      P_Exp__QuestionTok((lo,hi)) = st(P_Exp,P_QuestionTok)((x,y) => (x,y)); d("Exp__QuestionTok",P_Exp__QuestionTok)
      P_AssignOp__Exp((lo,hi)) = ss(P_AssignOp,P_Exp)((x,y) => (x,y)); d("AssignOp__Exp",P_AssignOp__Exp)
      P_LBrackTok__RBrackTok((lo,hi)) = tt(P_LBrackTok,P_RBrackTok)((x,y) => (x,y)) ::: tt(P_LBrackTok,P_RBrackTok)((x,y) => (x,y)); d("LBrackTok__RBrackTok",P_LBrackTok__RBrackTok)
      P_IdentDims((lo,hi)) = t(P_IdentTok)(x => (x.name,0)) ::: ss(P_IdentDims,P_LBrackTok__RBrackTok)((x,y) => (x._1,x._2+1)); d("IdentDims",P_IdentDims)
      P_VarDecl((lo,hi)) = s(P_IdentDims)(x => (x,None)) ::: ss(P_IdentDims,P_EqTok__Exp)((x,y) => (x,Some(y._2))); d("VarDecl",P_VarDecl)
      P_Juxts_VarDecl((lo,hi)) = s(P_VarDecl)(x => List(x)) ::: ss(P_VarDecl,P_Juxts_VarDecl)((x,y) => x :: y); d("Juxts_VarDecl",P_Juxts_VarDecl)
      P_Commas_VarDecl((lo,hi)) = s(P_VarDecl)(x => List(x)) ::: ss(P_VarDecl,P_CommaTok__Commas_VarDecl)((x,y) => x :: y._2); d("Commas_VarDecl",P_Commas_VarDecl)
      P_List1_VarDecl((lo,hi)) = s(P_Commas_VarDecl)(x => CommaList(x)) ::: s(P_Juxts_VarDecl)(x => JuxtList(x)); d("List1_VarDecl",P_List1_VarDecl)
      P_List_Exp__RBrackTok((lo,hi)) = nt(P_List_Exp,P_RBrackTok)((x,y) => (x,y)); d("List_Exp__RBrackTok",P_List_Exp__RBrackTok)
      P_LParenTok__Type((lo,hi)) = ts(P_LParenTok,P_Type)((x,y) => (x,y)); d("LParenTok__Type",P_LParenTok__Type)
      P_Exp__DotTok((lo,hi)) = st(P_Exp,P_DotTok)((x,y) => (x,y)); d("Exp__DotTok",P_Exp__DotTok)
      P_CommaTok__Commas_Exp((lo,hi)) = ts(P_CommaTok,P_Commas_Exp)((x,y) => (x,y)); d("CommaTok__Commas_Exp",P_CommaTok__Commas_Exp)
      P_Lit((lo,hi)) = t(P_IntLitTok)(x => IntLit(x.v)) ::: t(P_LongLitTok)(x => LongLit(x.v)) ::: t(P_FloatLitTok)(x => FloatLit(x.v)) ::: t(P_DoubleLitTok)(x => DoubleLit(x.v)) ::: t(P_BoolLitTok)(x => BoolLit(x.v)) ::: t(P_CharLitTok)(x => CharLit(x.v)) ::: t(P_StringLitTok)(x => StringLit(x.v)) ::: t(P_NullLitTok)(x => NullLit()); d("Lit",P_Lit)
      P_Exp((lo,hi)) = t(P_IdentTok)(x => NameExp(x.name)) ::: s(P_Lit)(x => LitExp(x)) ::: ts(P_LParenTok,P_Exp__RParenTok)((x,y) => ParenExp(y._1)) ::: ss(P_Exp__DotTok,P_Option_TypeArgs__IdentTok)((x,y) => FieldExp(x._1,y._1,y._2.name)) ::: ss(P_Exp__LBrackTok,P_List_Exp__RBrackTok)((x,y) => IndexExp(x._1,y._1)) ::: ss(P_Exp__ColonColonTok,P_Option_TypeArgs__IdentTok)((x,y) => MethodRefExp(x._1,y._1,y._2.name)) ::: ss(P_Exp__ColonColonTok,P_Option_TypeArgs__NewTok)((x,y) => NewRefExp(x._1,y._1)) ::: ss(P_Exp,P_TypeArgs)((x,y) => TypeApplyExp(x,y)) ::: ss(P_Exp__LParenTok,P_List_Exp__RParenTok)((x,y) => ApplyExp(x._1,y._1)) ::: ss(P_Exp,P_Juxts_Exp)((x,y) => ApplyExp(x,JuxtList(y))) ::: ts(P_NewTok,P_Option_TypeArgs__Exp)((x,y) => NewExp(y._1,y._2)) ::: tn(P_QuestionTok,P_WildcardBounds)((x,y) => WildExp(y)) ::: ss(P_Exp,P_PostOp)((x,y) => UnaryExp(y,x)) ::: ss(P_PreOp,P_Exp)((x,y) => UnaryExp(x,y)) ::: ss(P_LParenTok__Type,P_RParenTok__Exp)((x,y) => CastExp(x._2,y._2)) ::: ss(P_Exp,P_BinaryOp__Exp)((x,y) => BinaryExp(x,y._1,y._2)) ::: ss(P_Exp__QuestionTok,P_Exp__ColonTok__Exp)((x,y) => CondExp(x._1,y._1,y._3)) ::: ss(P_Exp,P_AssignOp__Exp)((x,y) => AssignExp(x,y._1,y._2)); d("Exp",P_Exp)
      P_Option_TypeArgs__Exp((lo,hi)) = ns(P_Option_TypeArgs,P_Exp)((x,y) => (x,y)); d("Option_TypeArgs__Exp",P_Option_TypeArgs__Exp)
      P_Juxts_Exp((lo,hi)) = s(P_Exp)(x => List(x)) ::: ss(P_Exp,P_Juxts_Exp)((x,y) => x :: y); d("Juxts_Exp",P_Juxts_Exp)
      P_Option_Exp((lo,hi)) = s(P_Exp)(x => Some(x)); d("Option_Exp",P_Option_Exp)
      P_Commas_Exp((lo,hi)) = s(P_Exp)(x => List(x)) ::: ss(P_Exp,P_CommaTok__Commas_Exp)((x,y) => x :: y._2); d("Commas_Exp",P_Commas_Exp)
      P_List1_Exp((lo,hi)) = s(P_Commas_Exp)(x => CommaList(x)) ::: s(P_Juxts_Exp)(x => JuxtList(x)); d("List1_Exp",P_List1_Exp)
      P_List_Exp((lo,hi)) = s(P_List1_Exp)(x => x); d("List_Exp",P_List_Exp)
      P_Exp__RParenTok((lo,hi)) = st(P_Exp,P_RParenTok)((x,y) => (x,y)); d("Exp__RParenTok",P_Exp__RParenTok)
      P_Block((lo,hi)) = ts(P_LCurlyTok,P_Stmts__RCurlyTok)((x,y) => y._1); d("Block",P_Block)
      P_StmtHelper((lo,hi)) = ss(P_Mod,P_Type__List1_VarDecl)((x,y) => VarStmt(x,y._1,y._2)) ::: s(P_Block)(x => BlockStmt(x)) ::: s(P_Exp)(x => ExpStmt(x)) ::: ts(P_AssertTok,P_Exp)((x,y) => AssertStmt(y,None)) ::: ss(P_AssertTok__Exp,P_ColonTok__Exp)((x,y) => AssertStmt(x._2,Some(y._2))) ::: tn(P_BreakTok,P_Option_IdentTok)((x,y) => BreakStmt(y.map(_.name))) ::: tn(P_ContinueTok,P_Option_IdentTok)((x,y) => ContinueStmt(y.map(_.name))) ::: tn(P_ReturnTok,P_Option_Exp)((x,y) => ReturnStmt(y)) ::: ts(P_ThrowTok,P_Exp)((x,y) => ThrowStmt(y)) ::: ts(P_SynchronizedTok,P_Exp__Block)((x,y) => SyncStmt(y._1,y._2)) ::: ss(P_SynchronizedTok__LParenTok,P_Exp__RParenTok__Block)((x,y) => SyncStmt(y._1,y._3)); d("StmtHelper",P_StmtHelper)
      P_Stmt((lo,hi)) = t(P_SemiTok)(x => EmptyStmt()) ::: sn(P_StmtHelper,P_Option_SemiTok)((x,y) => x); d("Stmt",P_Stmt)
      P_Stmts((lo,hi)) = sn(P_Stmt,P_Stmts)((x,y) => x :: y); d("Stmts",P_Stmts)
      P_EqTok__Exp((lo,hi)) = ts(P_EqTok,P_Exp)((x,y) => (x,y)); d("EqTok__Exp",P_EqTok__Exp)
      P_Exp__ColonColonTok((lo,hi)) = st(P_Exp,P_ColonColonTok)((x,y) => (x,y)) ::: st(P_Exp,P_ColonColonTok)((x,y) => (x,y)); d("Exp__ColonColonTok",P_Exp__ColonColonTok)
      P_DotTok__IdentTok((lo,hi)) = tt(P_DotTok,P_IdentTok)((x,y) => (x,y)); d("DotTok__IdentTok",P_DotTok__IdentTok)
      P_Exp__LBrackTok((lo,hi)) = st(P_Exp,P_LBrackTok)((x,y) => (x,y)); d("Exp__LBrackTok",P_Exp__LBrackTok)
      P_List_Exp__RParenTok((lo,hi)) = nt(P_List_Exp,P_RParenTok)((x,y) => (x,y)); d("List_Exp__RParenTok",P_List_Exp__RParenTok)
      P_ColonTok__Exp((lo,hi)) = ts(P_ColonTok,P_Exp)((x,y) => (x,y)) ::: ts(P_ColonTok,P_Exp)((x,y) => (x,y)); d("ColonTok__Exp",P_ColonTok__Exp)
      P_SynchronizedTok__LParenTok((lo,hi)) = tt(P_SynchronizedTok,P_LParenTok)((x,y) => (x,y)); d("SynchronizedTok__LParenTok",P_SynchronizedTok__LParenTok)
      P_RParenTok__Exp((lo,hi)) = ts(P_RParenTok,P_Exp)((x,y) => (x,y)); d("RParenTok__Exp",P_RParenTok__Exp)
      P_Type((lo,hi)) = t(P_IdentTok)(x => NameType(x.name)) ::: ss(P_Mod,P_Type)((x,y) => ModType(x,y)) ::: ss(P_Type,P_LBrackTok__RBrackTok)((x,y) => ArrayType(x)) ::: ss(P_Type,P_TypeArgs)((x,y) => ApplyType(x,y)) ::: ss(P_Type,P_DotTok__IdentTok)((x,y) => FieldType(x,y._2.name)) ::: tn(P_QuestionTok,P_WildcardBounds)((x,y) => WildType(y)); d("Type",P_Type)
      P_Commas_Type((lo,hi)) = s(P_Type)(x => List(x)) ::: ss(P_Type,P_CommaTok__Commas_Type)((x,y) => x :: y._2); d("Commas_Type",P_Commas_Type)
      P_Juxts_Type((lo,hi)) = s(P_Type)(x => List(x)) ::: ss(P_Type,P_Juxts_Type)((x,y) => x :: y); d("Juxts_Type",P_Juxts_Type)
      P_List1_Type((lo,hi)) = s(P_Commas_Type)(x => CommaList(x)) ::: s(P_Juxts_Type)(x => JuxtList(x)); d("List1_Type",P_List1_Type)
      P_List_Type((lo,hi)) = s(P_List1_Type)(x => x); d("List_Type",P_List_Type)
      P_Option_TypeArgs__NewTok((lo,hi)) = nt(P_Option_TypeArgs,P_NewTok)((x,y) => (x,y)); d("Option_TypeArgs__NewTok",P_Option_TypeArgs__NewTok)
    }
    
    // All done!
    P_Stmt((0,n))
  }
}
