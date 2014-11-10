package tarski

import tarski.Items.TypeParamItem
import tarski.Types._

// TODO: Handle infinite types correctly.  Some useful examples are here:
// http://stackoverflow.com/questions/3451519/when-does-java-type-inference-produce-an-infinite-type

object Inference {
  // Bounds: 18.1.3.  Each of these refers to an ambient type variable s
  type Var = TypeParamItem
  type Bounds = Map[Var,Bound] // If false is allowed, use Option[Bounds]
  sealed abstract class Bound
  case class Fixed(t: RefType) extends Bound                       // s == t
  case class Bounded(lo: List[RefType], hi: List[RefType]) extends Bound // lo <: s <: hi
  // TODO: capture and throws

  // Construct initial bounds for some type parameters
  // TODO: Handle bounds on type parameters
  def startBounds(vs: List[Var]): Bounds =
    vs.map(v => (v,Bounded(Nil,List(ObjectType)))).toMap

  // TODO: Not sure what I'm actually supposed to do if occurs checks fail
  def occurs(s: Var, t: RefType): Boolean = t match {
    case _:SimpleType => false
    case t:GenericType => t.args exists (occurs(s,_))
    case ArrayType(t) => t match {
      case _:SimpleType => false
      case t:RefType => occurs(s,t)
    }
    case IntersectType(ts) => ts exists (occurs(s,_))
    case ParamType(t) => s == t
  }

  // Is an inference variable fixed yet?
  def isFixed(bs: Bounds, v: Var): Boolean = bs(v) match {
    case Fixed(_) => true
    case Bounded(_,_) => false
  }

  // All inference variables mentioned in a type
  def vars(bs: Bounds, t: RefType): Set[Var] = t match {
    case _:SimpleType => Set()
    case t:GenericType => t.args.toSet flatMap ((a: RefType) => vars(bs,a))
    case ArrayType(t) => t match {
      case _:SimpleType => Set()
      case t:RefType => vars(bs,t)
    }
    case IntersectType(ts) => ts flatMap (vars(bs,_))
    case ParamType(v) => Set(v)
  }

  // Incorporate bounds.  TODO: Optimize
  def matchSupers(bs: Bounds, s: RefType, t: RefType): Option[Bounds] = {
    val sg = supers(s).toList collect {case g:GenericType => g}
    val tg = supers(t).toList collect {case g:GenericType => g}
    forms(bs,sg)((bs,sg) => forms(bs,tg)((bs,tg) =>
      if (sg.d == tg.d) forms(bs,sg.args,tg.args)(equalForm)
      else Some(bs)))
  }
  def incorporateSub(bs: Bounds, s: Var, t: RefType): Option[Bounds] = bs(s) match {
    case Fixed(st) => if (isSubtype(st,t)) Some(bs) else subForm(bs,st,t)
    case Bounded(lo,hi) => forms(bs,lo)(subForm(_,_,t)) flatMap (forms(_,hi)(matchSupers(_,_,t)))
  }
  def incorporateSub(bs: Bounds, s: RefType, t: Var): Option[Bounds] = bs(t) match {
    case Fixed(tt) => if (isSubtype(s,tt)) Some(bs) else subForm(bs,s,tt)
    case Bounded(lo,hi) => forms(bs,hi)(subForm(_,s,_)) flatMap (forms(_,lo)(matchSupers(_,_,s)))
  }
  def incorporateEqual(bs: Bounds, s: Var, t: RefType): Option[Bounds] = bs(s) match {
    case Fixed(u) => if (t == u) Some(bs) else equalForm(bs,t,u)
    case Bounded(lo,hi) =>
      if (occurs(s,t))
        throw new NotImplementedError("not sure what to do about occurs checks")
      else {
        implicit val tenv = Map(s -> t)
        val bs2 = bs+((s,Fixed(t)))
        def sub(bs: Bounds, ub: (Var,Bound)): Option[Bounds] = {
          val (u,b) = ub
          b match {
            case Fixed(v) => incorporateEqual(bs,u,substitute(v))
            case Bounded(lo,hi) => forms(bs,lo)((bs,t) => incorporateSub(bs,substitute(t),u))
              .flatMap(forms(_ ,hi)((bs,t) => incorporateSub(bs,u,substitute(t))))
          }
        }
        forms(bs2,bs2.toList)(sub)
          .flatMap(forms(_,lo)(subForm(_,_,t)))
          .flatMap(forms(_,hi)(subForm(_,t,_)))
      }
  }

  // Does t contain any inference variables?
  def isProper(bs: Bounds, t: Type): Boolean = t match {
    case _: SimpleType => true
    case g: GenericType => g.args forall (isProper(bs,_))
    case ArrayType(t) => isProper(bs,t)
    case IntersectType(ts) => ts forall (isProper(bs,_))
    case ParamType(v) => !bs.contains(v)
  }

  // Turn a compatibility constraint s -> t into bounds: 18.2.2
  def compatForm(bs: Bounds, s: Type, t: Type): Option[Bounds] =
    if (isProper(bs,s) && isProper(bs,t))
      if (looseInvokeContext(s,t)) Some(bs) else None
    else (s,t) match {
      case (VoidType,_)|(_,VoidType) => None
      case (s:PrimType,t) => compatForm(bs,box(s),t)
      case (s:RefType,t:PrimType) => equalForm(bs,s,box(t))
      // TODO: Handle raw type cases (bullets 4 and 5 in 18.2.2)
      case (s:RefType,t:RefType) => subForm(bs,s,t)
    }

  // Turn a subtyping constraint s <: t into bounds: 18.2.3
  def subForm(bs: Bounds, s: RefType, t: RefType): Option[Bounds] =
    if (isProper(bs,s) && isProper(bs,t))
      if (isSubtype(s,t)) Some(bs) else None
    else (s,t) match {
      case (NullType,_) => Some(bs)
      case (_,NullType) => None
      case (ParamType(s),t) if bs contains s => incorporateSub(bs,s,t)
      case (s,ParamType(t)) if bs contains t => incorporateSub(bs,s,t)
      case (s,t:GenericType) => supers(s)
        .collect({case ss: GenericType if ss.d == t.d => ss})
        .headOption
        .flatMap(ss => forms(bs,ss.args,t.args)(containForm))
      case (s,(_:ClassOrObjectType|_:InterfaceType)) => if (supers(s) contains t) Some(bs) else None
      case (s,ArrayType(t)) => s match {
        case ArrayType(s) => (s,t) match {
          case (s:RefType,t:RefType) => subForm(bs,s,t) // Java arrays are covariant, even though they shouldn't be
          case _ => if (s == t) Some(bs) else None
        }
        case _ => None
      }
      case (s,ParamType(_)) => s match {
        case IntersectType(ss) if ss contains t => Some(bs)
        // TODO: Handle case where t has a lower bound
        case _ => None
      }
      case (s,IntersectType(ts)) => forms(bs,ts.toList)(subForm(_,s,_))
    }

  // Turn a containment constraint s <= t into bounds: 18.2.3
  // TODO: This function should take "type arguments", not just types
  def containForm(bs: Bounds, s: RefType, t: RefType): Option[Bounds] =
    equalForm(bs,s,t)

  def equalForm(bs: Bounds, s: RefType, t: RefType): Option[Bounds] =
    if (isProper(bs,s) && isProper(bs,t))
      if (s == t) Some(bs) else None
    else (s,t) match {
      case (ParamType(s),t) if bs contains s => incorporateEqual(bs,s,t)
      case (s,ParamType(t)) if bs contains t => incorporateEqual(bs,t,s)
      case (s:SimpleClassOrInterface,t:SimpleClassOrInterface) => if (s.d == t.d) Some(bs) else None
      case (s:GenericType,t:GenericType) => if (s.d == t.d) forms(bs,s.args,t.args)(equalForm) else None
      case (ArrayType(s),ArrayType(t)) => equalFormArbitrary(bs,s,t)
      case _ => None // IntersectType intentionally skipped as per spec
    }
  def equalFormArbitrary(bs: Bounds, s: Type, t: Type): Option[Bounds] = (s,t) match {
    case (s:RefType,t:RefType) => equalForm(bs,s,t)
    case (s,t) => if (s == t) Some(bs) else None
  }

  // Resolution: 18.4
  def resolve(bs: Bounds, vs: List[Var]): Option[Bounds] = {
    // Determine dependencies, without ensure reflexivity or transitivity
    val depends = bs mapValues {
      case Fixed(t) => vars(bs,t)
      case Bounded(lo,hi) => (lo++hi).toSet.flatMap((t: RefType) => vars(bs,t)) // TODO: Respect capture constraints
    }
    // Iteratively resolve
    def iterate(bs: Bounds, vs: List[Var]): Option[Bounds] = vs match {
      case Nil => Some(bs)
      case v::rest =>
        // Generate a candidate set
        def expand(vs: Set[Var], v: Var): Set[Var] =
          if (vs.contains(v) || isFixed(bs,v)) vs
          else depends(v).foldLeft(vs+v)(expand)
        val alpha = expand(Set(),v)
        // Add new bounds for each alpha.  We compute bounds via bs, not bs2; I believe this matches the spec.
        def freeze(bs2: Bounds, a: Var): Option[Bounds] = bs(a) match {
          case Fixed(_) => Some(bs2)
          case Bounded(lo,hi) => incorporateEqual(bs2, a, lo filter (isProper(bs,_)) match {
            case lop@(_::_) => lub(lop)
            // TODO: Handle throws bounds
            case Nil => glb(hi filter (isProper(bs,_)))
          })
        }
        forms(bs,alpha.toList)(freeze) flatMap (iterate(_,rest))
      }
    iterate(bs,vs)
  }

  // Extract proper types for inference variables
  // TODO: This probably needs to change for infinite types
  def extract(bs: Bounds, ps: List[Var]): List[RefType] = {
    def clean(t: RefType): Boolean = t match {
      case _:SimpleType => true
      case t:GenericType => t.args forall clean
      case ParamType(v) => !bs.contains(v)
      case IntersectType(ts) => ts forall clean
      case ArrayType(t) => t match {
        case t: RefType => clean(t)
        case _ => true
      }
    }
    ps.map(v => bs(v) match {
      case Fixed(t) => assert(clean(t)); t
      case Bounded(_,_) => throw new RuntimeException("can't extract unresolved inference variable")
    })
  }

  // List combinators
  def forms[A](bs: Bounds, ts: List[A])(f: (Bounds,A) => Option[Bounds]): Option[Bounds] = ts match {
    case Nil => Some(bs)
    case t::ts => f(bs,t) flatMap (bs => forms(bs,ts)(f))
  }
  def forms[A,B](bs: Bounds, ss: List[A], ts: List[B])(f: (Bounds,A,B) => Option[Bounds]): Option[Bounds] = (ss,ts) match {
    case (Nil,Nil) => Some(bs)
    case (s::ss,t::ts) => f(bs,s,t) flatMap (bs => forms(bs,ss,ts)(f))
    case _ => throw new RuntimeException("arity mismatch")
  }

  // Can type s be used in a type t context (loose or strict)?
  // TODO: Replace s with an expression to handle poly expressions
  def strictBounds(bs: Bounds, s: Type, t: Type): Option[Bounds] = (s,t) match {
    case (VoidType,_)|(_,VoidType) => None
    case (_:PrimType,_:RefType)|(_:RefType|_:PrimType,_:PrimType) => None
    case (s:RefType,t:RefType) => subForm(bs,s,t)
  }
  def looseBounds(bs: Bounds, s: Type, t: Type): Option[Bounds] = compatForm(bs,s,t)

  // Given a function with type parameters ps and parameter types ts, called with argument types as,
  // is there a satisfying assignment?
  // TODO: Handle variable arity
  // TODO: We do not incorporate return type information as described in 18.5.2.
  type Form = (Bounds,Type,Type) => Option[Bounds]
  def infer(ps: List[Var], ts: List[Type], as: List[Type])(form: Form): Option[List[RefType]] = {
    println(s"infer:\n  ps $ps\n  ts $ts\n  as $as")
    // TODO: Freshen variables ps to handle recursion correctly
    forms(startBounds(ps),as,ts)(form)
      .flatMap(resolve(_,ps))
      .map(extract(_,ps))
  }
}
