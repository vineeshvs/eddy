package tarski

import org.apache.commons.lang.StringEscapeUtils.unescapeJava

import AST._
import Types._
import Environment._
import Items._
import tarski.Denotations._
import Scores._
import ambiguity.Utility.notImplemented

object Semantics {
  /*
  // TODO: Add back bias?
  // Bias score for a AST tree node (given an instance), given the environment
  def bias(node: Node)(implicit env: JavaEnvironment): Score = node match {
    case _ => ZeroScore
  }
  def withBias[A](n: Node, ds: Scored[A]): Scored[A] = {
    println("      scores for " + n + ": " + ds)
    ds
  }
  */

  // Literals
  def denoteLit(x: ALit): Scored[Lit] = {
    def f[A,B](v: String, c: String => A)(t: (A,String) => B) = t(c(v.replaceAllLiterally("_","")),v)
    single(x match {
      case IntALit(v) =>    f(v,_.toInt)(IntLit)
      case LongALit(v) =>   f(v,_.toLong)(LongLit)
      case FloatALit(v) =>  f(v,_.toFloat)(FloatLit)
      case DoubleALit(v) => f(v,_.toDouble)(DoubleLit)
      case BoolALit(b) =>   BooleanLit(b)
      case CharALit(v) =>   CharLit(unescapeJava(v.slice(1,v.size-1)).charAt(0),v)
      case StringALit(v) => StringLit(unescapeJava(v.slice(1,v.size-1)),v)
      case NullALit() =>    NullLit()
    })
  }

  // Types
  // TODO: The grammar currently rules out stuff like (1+2).A matching as a type.  Maybe we want this?
  def denote(n: AType)(implicit env: Env): Scored[Type] = n match {
    case NameAType(n) => typeScores(n)
    case FieldAType(x,f) => for (t <- denote(x); fi <- typeFieldScores(t,f)) yield fi
    case ModAType(Annotation(_),t) => throw new NotImplementedError("Types with annotations")
    case ModAType(_,_) => fail
    case ArrayAType(t) => denote(t) map ArrayType
    case ApplyAType(_,_) => throw new NotImplementedError("Generics not implemented (ApplyType): " + n)
    case WildAType(_) => throw new NotImplementedError("Type bounds not implemented (WildType): " + n)
  }

  def denoteType(n: AExp)(implicit env: Env): Scored[Type] =
    denote(n) collect {
      case t: TypeDen => t.item
      case e: Exp => typeOf(e) // Allow expressions to be used as types
    }

  // TODO: Something is local if it can be referred to without qualifiers
  def isLocal(i: NamedItem): Boolean = false

  // Are we contained in the given type, or in something contained in the given type?
  def containedIn(i: NamedItem, t: TypeItem): Boolean = i match {
    case f: Member => f.container == t || containedIn(f.container,t)
    case _ => false
  }

  def denoteValue(i: Value)(implicit env: Env): Scored[Exp] = i match {
    case i: ParameterItem => single(ParameterExp(i))
    case i: LocalVariableItem => single(LocalVariableExp(i))
    case i: EnumConstantItem => single(EnumConstantExp(i))
    case i: FieldItem =>
      if (isLocal(i))
        single(LocalFieldExp(i))
      else {
        val c = toType(i.container)
        for (obj <- objectsOfType(c) if !containedIn(obj,i.container); objden <- denoteValue(obj)) yield FieldExp(objden, i)
      }
    case i: StaticFieldItem => single(StaticFieldExp(i))
  }

  // Expressions
  def denote(e: AExp)(implicit env: Env): Scored[Den] = e match {
    case NameAExp(n) => scores(n) flatMap {
      case i: Value => denoteValue(i)

      // Callables
      case i: MethodItem =>
        if (isLocal(i))
          single(LocalMethodDen(i))
        else
          for (obj <- objectsOfType(toType(i.container)); objden <- denoteValue(obj)) yield MethodDen(objden, i)
      case i: StaticMethodItem => single(StaticMethodDen(i))
      case i: ConstructorItem => single(ForwardDen(i))
      case i: TypeParamItem => single(TypeDen(ParamType(i)))
      case _: TypeItem => fail // Expression cannot return types
      case _: PackageItem => notImplemented
      case _: AnnotationItem => notImplemented
    }
    case x: ALit => denoteLit(x)
    case ParenAExp(x) => denote(x) // Java doesn't allow parentheses around types, but we do

    // x is either a type or an expression, f is an inner type, method, or field
    case FieldAExp(x,ts,f) => if (ts.isDefined) throw new NotImplementedError("Generics not implemented (FieldExp): " + e) else
      for (d <- denote(x);
           t <- d match {
             case t: TypeDen => single(t.item)
             case e: Exp => single(typeOf(e))
             case _ => fail
           };
           fi <- fieldScores(t,f);
           r <- (d,fi) match {
             case (_, f: TypeItem) => single(TypeDen(toType(f)))
             case (_, f: EnumConstantItem) => single(EnumConstantExp(f))
             case (_, f: StaticFieldItem) => single(StaticFieldExp(f))
             case (e: Exp, f: FieldItem) => single(FieldExp(e,f))
             case (_, f: StaticMethodItem) => single(StaticMethodDen(f))
             case (e: Exp, f: MethodItem) => single(MethodDen(e,f))
             case (_, f: PackageItem) => throw new NotImplementedError("FieldExp: packages not implemented: " + e)
             case (_, f: ConstructorItem) => throw new NotImplementedError("FieldExp: ConstructorItem")
             case (_, f: LocalItem) => fail // can't qualify locals, parameters, TODO: return something with a low score
             case _ => fail
           })
        yield r

    case MethodRefAExp(x,ts,f) => throw new NotImplementedError("MethodRefs not implemented: " + e)
    case NewRefAExp(x,t) => throw new NotImplementedError("NewRef not implemented: " + e)
    case TypeApplyAExp(x,ts) => throw new NotImplementedError("Generics not implemented (TypeApplyExp): " + e)
    case NewAExp(ts,e) => throw new NotImplementedError("new expression not implemented: " + e)
    case WildAExp(b) => throw new NotImplementedError("wildcard expressions not implemented: " + e)

    case ApplyAExp(f,xsn,_) => {
      val xsl = xsn.list map denoteExp
      val n = xsl.size
      def call(f: Callable): Scored[Exp] =
        if (f.paramTypes.size != n) fail
        else {
          val filtered = (f.paramTypes zip xsl) map {case (p,xs) => xs.filter(x => looseInvokeContext(typeOf(x),p))}
          for (xl <- product(filtered)) yield ApplyExp(f,xl)
        }
      def index(f: Exp, ft: ArrayType): Scored[Exp] = {
        def hasDims(t: Type, d: Int): Boolean = d==0 || (t match {
          case ArrayType(t) => hasDims(t,d-1)
          case _ => false
        })
        if (!hasDims(ft,n)) fail
        else {
          val filtered = xsl map (xs => xs.filter(x => unbox(typeOf(x)) match {
            case Some(p: PrimType) => promote(p) == IntType
            case _ => false
          }))
          for (xl <- product(filtered)) yield xl.foldLeft(f)(IndexExp)
        }
      }
      denote(f) flatMap {
        case a: Exp => typeOf(a) match {
          case t: ArrayType => index(a,t)
          case _ => fail
        }
        case c: Callable => call(c)
        case _ => fail
      }
    }

    case UnaryAExp(op,x) =>
      for (x <- denoteExp(x);
           if unaryLegal(op,typeOf(x)))
        yield UnaryExp(op,x)

    case BinaryAExp(op,x,y) => {
      val dy = denoteExp(y);
      for (x <- denoteExp(x);
           y <- dy;
           if binaryLegal(op,typeOf(x),typeOf(y)))
        yield BinaryExp(op,x,y)
    }

    case CastAExp(t,x) =>
      for (t <- denote(t);
           x <- denoteExp(x);
           if castsTo(typeOf(x),t))
        yield CastExp(t,x)

    case CondAExp(c,x,y) =>
      for (c <- denoteExp(c);
           if isToBoolean(typeOf(c));
           x <- denoteExp(x);
           tx = typeOf(x);
           y <- denoteExp(y);
           ty = typeOf(y))
        yield CondExp(c,x,y,condType(tx,ty))

    case AssignAExp(op,x,y) =>
      for (x <- denoteExp(x);
           if isVariable(x);
           xt = typeOf(x);
           y <- denoteExp(y);
           yt = typeOf(y);
           t <- option(assignOpType(op,xt,yt)))
        yield AssignExp(op,x,y)

    case ArrayAExp(xs,a) =>
      for (is <- product(xs.list map denoteExp))
        yield ArrayExp(condTypes(is map typeOf),is)
  }

  def denoteExp(n: AExp)(implicit env: Env): Scored[Exp] =
    denote(n) collect {case e: Exp => e}

  def isVariable(e: Exp): Boolean = e match {
    // in java, we can only assign to actual variables, never to values returned by functions or expressions.
    // TODO: implement final, private, protected
    case _: Lit => false
    case ParameterExp(i) => true // TODO: check for final
    case LocalVariableExp(i) => true // TODO: check for final
    case EnumConstantExp(_) => false
    case CastExp(_,_) => false // TODO: java doesn't allow this, but I don't see why we shouldn't
    case UnaryExp(_,_) => false // TODO: java doesn't allow this, but we should. Easy for ++,--, and -x = 5 should translate to x = -5
    case BinaryExp(_,_,_) => false
    case AssignExp(_,_,_) => false
    case ParenExp(x) => isVariable(x)
    case ApplyExp(_,_) => false
    case FieldExp(obj, field) => true // TODO: check for final, private, protected
    case LocalFieldExp(field) => true // TODO: check for final
    case StaticFieldExp(field) => true // TODO: check for final, private, protected
    case IndexExp(a, i) => isVariable(a)
    case CondExp(_,_,_,_) => false // TODO: java doesn't allow this, but (x==5?x:y)=10 should be turned into an if statement
    case ArrayExp(_,_) => false
    case EmptyArrayExp(_,_) => false
  }

  // Statements
  def denoteStmt(s: AStmt)(env: Env): Scored[(Env,Stmt)] = s match {
    case EmptyAStmt() => single((env,EmptyStmt()))
    case VarAStmt(mod,t,v) => notImplemented
    case ExpAStmt(e) => {
      val exps = denoteExp(e)(env) map ExpStmt
      val stmts = e match {
        case AssignAExp(None,NameAExp(x),y) =>
          for {y <- denoteExp(y)(env);
               t = typeOf(y);
               (env,x) <- env.newVariable(x,t)}
            yield (env,VarStmt(t,List((x,Some(y)))))
        case _ => fail
      }
      exps.map((env,_)) ++ stmts
    }
    case BlockAStmt(b) => denoteStmts(b)(env) map {case (e,ss) => (e,BlockStmt(ss))}
    case AssertAStmt(cond: AExp, msg: Option[AExp]) => notImplemented
    case BreakAStmt(label: Option[Name]) => notImplemented
    case ContinueAStmt(label: Option[Name]) => notImplemented
    case ReturnAStmt(e: Option[AExp]) => notImplemented
    case ThrowAStmt(e: AExp) => notImplemented
    case SyncAStmt(e: AExp, b: Block) => notImplemented
  }

  def denoteStmts(s: List[AStmt])(env: Env): Scored[(Env,List[Stmt])] =
    productFoldLeft(env)(s map denoteStmt)
}
