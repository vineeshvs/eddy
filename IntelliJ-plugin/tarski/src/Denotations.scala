package tarski

import tarski.AST.{AssignOp, BinaryOp, UnaryOp}
import tarski.Items._
import tarski.Types._

object Denotations {

  sealed abstract class Den

  // Types
  case class TypeDen(item: Type) extends Den

  // Callables
  sealed abstract class Callable extends Den with Signature {
    val f: CallableItem
  }
  sealed abstract class NonNewCallable extends Callable {
    def tparams: List[TypeParamItem] = f.tparams
    def params: List[Type] = f.params
  }
  case class MethodDen(obj: Exp, override val f: MethodItem) extends NonNewCallable
  case class LocalMethodDen(override val f: MethodItem) extends NonNewCallable
  case class StaticMethodDen(override val f: StaticMethodItem) extends NonNewCallable
  case class ForwardDen(override val f: ConstructorItem) extends NonNewCallable
  case class NewDen(override val f: ConstructorItem) extends Callable {
    def tparams = f.container.params ++ f.tparams
    def params = f.params
  }

  // Statements
  sealed abstract class Stmt extends Den
  case class EmptyStmt() extends Stmt
  case class VarStmt(t: Type, vs: List[(LocalVariableItem,Option[Exp])]) extends Stmt
  case class ExpStmt(e: Exp) extends Stmt
  case class BlockStmt(b: List[Stmt]) extends Stmt

  // It's all expressions from here
  sealed abstract class Exp extends Den

  sealed abstract class Lit extends Exp
  case class ByteLit(b: Byte, text: String) extends Lit
  case class ShortLit(s: Short, text: String) extends Lit
  case class IntLit(i: Int, text: String) extends Lit
  case class LongLit(l: Long, text: String) extends Lit
  case class BooleanLit(b: Boolean) extends Lit
  case class StringLit(s: String, text: String) extends Lit
  case class FloatLit(f: Float, text: String) extends Lit
  case class DoubleLit(d: Double, text: String) extends Lit
  case class CharLit(c: Char, text: String) extends Lit
  case class NullLit() extends Lit

  // Expressions
  case class ParameterExp(item: ParameterItem) extends Exp
  case class LocalVariableExp(item: LocalVariableItem) extends Exp
  case class EnumConstantExp(item: EnumConstantItem) extends Exp
  case class StaticFieldExp(field: StaticFieldItem) extends Exp
  case class LocalFieldExp(field: FieldItem) extends Exp
  case class FieldExp(obj: Exp, field: FieldItem) extends Exp
  case class ThisExp(t: ThisItem) extends Exp
  case class SuperExp(t: ThisItem) extends Exp // t is the type super is used in, t.base is the type of this expression
  case class CastExp(t: Type, e: Exp) extends Exp
  case class UnaryExp(op: UnaryOp, e: Exp) extends Exp
  case class BinaryExp(op: BinaryOp, e0: Exp, e1: Exp) extends Exp
  case class AssignExp(op: Option[AssignOp], left: Exp, right: Exp) extends Exp
  case class ParenExp(e: Exp) extends Exp
  case class ApplyExp(f: Callable, targs: List[RefType], args: List[Exp]) extends Exp
  case class IndexExp(e: Exp, i: Exp) extends Exp
  case class CondExp(c: Exp, t: Exp, f: Exp, r: Type) extends Exp
  case class ArrayExp(t: Type, i: List[Exp]) extends Exp // t is the inner type
  case class EmptyArrayExp(t: Type, i: List[Exp]) extends Exp // new t[i]

  def typeOf(d: Exp): Type = d match {
    // Literals
    case ByteLit(_,_) => ByteType
    case ShortLit(_,_) => ShortType
    case IntLit(_,_) => IntType
    case LongLit(_,_) => LongType
    case BooleanLit(_) => BooleanType
    case StringLit(_,_) => StringType
    case FloatLit(_,_) => FloatType
    case DoubleLit(_,_) => DoubleType
    case CharLit(_,_) => CharType
    case NullLit() => NullType
    // Names
    case ParameterExp(i) => i.ourType
    case LocalVariableExp(i) => i.ourType
    case EnumConstantExp(i) => i.ourType
    case ThisExp(t) => t.ourType
    case SuperExp(ThisItem(c:NormalClassItem)) => c.base
    case SuperExp(_) => throw new RuntimeException("type error")
    case CastExp(t,_) => t
    case UnaryExp(op,e) => unaryType(op,typeOf(e)) getOrElse (throw new RuntimeException("type error"))
    case BinaryExp(op,x,y) => binaryType(op,typeOf(x),typeOf(y)) getOrElse (throw new RuntimeException("type error"))
    case AssignExp(op,left,right) => typeOf(left)
    case ParenExp(e) => typeOf(e)
    case ApplyExp(f,ts,_) => f match {
      case MethodDen(_,f)     => substitute(f.tparams,ts,f.retVal)
      case LocalMethodDen(f)  => substitute(f.tparams,ts,f.retVal)
      case StaticMethodDen(f) => substitute(f.tparams,ts,f.retVal)
      case NewDen(c) => toType(c.container,ts.take(c.container.arity))
      case ForwardDen(_) => VoidType
    }
    case FieldExp(_,f) => f.ourType
    case LocalFieldExp(f) => f.ourType
    case StaticFieldExp(f) => f.ourType
    case IndexExp(e,i) => typeOf(e) match {
      case ArrayType(t) => t
      case _ => throw new RuntimeException("type error")
    }
    case CondExp(_,_,_,r) => r
    // Arrays
    case ArrayExp(t,_) => ArrayType(t)
    case EmptyArrayExp(t,is) => is.foldLeft(t)((t,i) => ArrayType(t))
  }
}
