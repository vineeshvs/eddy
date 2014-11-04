package tarski

import tarski.AST.{AssignOp, BinaryOp, UnaryOp}
import tarski.Items.{PackageItem, ArrayType, VoidType, Type}
import tarski.Types._

object Denotations {

  sealed abstract class Den

  // Types
  case class TypeDen(item: Type) extends Den

  // Callables
  sealed abstract class Callable extends Den {
    val f: Items.Callable
    def paramTypes: List[Type] = f.paramTypes
  }
  case class MethodDen(obj: ExpDen, override val f: Items.MethodItem) extends Callable
  case class LocalMethodDen(override val f: Items.MethodItem) extends Callable
  case class StaticMethodDen(override val f: Items.StaticMethodItem) extends Callable
  case class NewDen(override val f: Items.ConstructorItem) extends Callable
  case class ForwardDen(override val f: Items.ConstructorItem) extends Callable

  // Statements
  sealed abstract class StmtDen extends Den
  case class EmptyStmtDen() extends StmtDen
  case class VarStmtDen(t: Type, vs: List[(Items.LocalVariableItem,Option[InitDen])]) extends StmtDen
  case class ExprStmtDen(e: ExpDen) extends StmtDen
  case class BlockStmtDen(b: List[StmtDen]) extends StmtDen

  // Variable initializers
  sealed abstract class InitDen extends Den
  case class ExpInitDen(e: ExpDen) extends InitDen
  case class ArrayInitDen(i: List[InitDen], t: Type) extends InitDen // t is the inner type

  // It's all expressions from here
  sealed abstract class ExpDen extends Den

  sealed abstract class LitDen extends ExpDen
  case class ByteLit(b: Byte, text: String) extends LitDen
  case class ShortLit(s: Short, text: String) extends LitDen
  case class IntLit(i: Int, text: String) extends LitDen
  case class LongLit(l: Long, text: String) extends LitDen
  case class BooleanLit(b: Boolean) extends LitDen
  case class StringLit(s: String, text: String) extends LitDen
  case class FloatLit(f: Float, text: String) extends LitDen
  case class DoubleLit(d: Double, text: String) extends LitDen
  case class CharLit(c: Char, text: String) extends LitDen
  case class NullLit() extends LitDen

  // Expressions
  case class ParameterExpDen(item: Items.ParameterItem) extends ExpDen
  case class LocalVariableExpDen(item: Items.LocalVariableItem) extends ExpDen
  case class EnumConstantExpDen(item: Items.EnumConstantItem) extends ExpDen
  case class CastExpDen(t: Type, e: ExpDen) extends ExpDen
  case class UnaryExpDen(op: UnaryOp, e: ExpDen) extends ExpDen
  case class BinaryExpDen(op: BinaryOp, e0: ExpDen, e1: ExpDen) extends ExpDen
  case class AssignExpDen(op: Option[AssignOp], left: ExpDen, right: ExpDen) extends ExpDen
  case class ParenExpDen(e: ExpDen) extends ExpDen
  case class ApplyExpDen(f: Callable, args: List[ExpDen]) extends ExpDen
  case class FieldExpDen(obj: ExpDen, field: Items.FieldItem) extends ExpDen
  case class LocalFieldExpDen(field: Items.FieldItem) extends ExpDen
  case class StaticFieldExpDen(field: Items.StaticFieldItem) extends ExpDen
  case class IndexExpDen(e: ExpDen, i: ExpDen) extends ExpDen
  case class CondExpDen(c: ExpDen, t: ExpDen, f: ExpDen, r: Type) extends ExpDen

  def typeOf(d: ExpDen): Type = d match {
    // Literals
    case ByteLit(_,_) => Items.ByteType
    case ShortLit(_,_) => Items.ShortType
    case IntLit(_,_) => Items.IntType
    case LongLit(_,_) => Items.LongType
    case BooleanLit(_) => Items.BooleanType
    case StringLit(_,_) => Items.StringType
    case FloatLit(_,_) => Items.FloatType
    case DoubleLit(_,_) => Items.DoubleType
    case CharLit(_,_) => Items.CharType
    case NullLit() => Items.NullType
    // Names
    case ParameterExpDen(i) => i.ourType
    case LocalVariableExpDen(i) => i.ourType
    case EnumConstantExpDen(i) => i.ourType
    case CastExpDen(t,_) => t
    case UnaryExpDen(op,e) => unaryType(op,typeOf(e)) getOrElse (throw new RuntimeException("type error"))
    case BinaryExpDen(op,x,y) => binaryType(op,typeOf(x),typeOf(y)) getOrElse (throw new RuntimeException("type error"))
    case AssignExpDen(op,left,right) => typeOf(left)
    case ParenExpDen(e) => typeOf(e)
    case ApplyExpDen(f,_) => f match {
      case MethodDen(_,f) => f.retVal
      case LocalMethodDen(f) => f.retVal
      case StaticMethodDen(f) => f.retVal
      case NewDen(c) => c.containing
      case ForwardDen(_) => VoidType
    }
    case FieldExpDen(_,f) => f.ourType
    case LocalFieldExpDen(f) => f.ourType
    case StaticFieldExpDen(f) => f.ourType
    case IndexExpDen(e,i) => typeOf(e) match {
      case ArrayType(t) => t
      case _ => throw new RuntimeException("type error")
    }
    case CondExpDen(_,_,_,r) => r
  }

  def typeOf(i: InitDen): Type = i match {
    case ExpInitDen(e) => typeOf(e)
    case ArrayInitDen(_,t) => ArrayType(t)
  }
}
