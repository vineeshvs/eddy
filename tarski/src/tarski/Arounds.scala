/* Arounds: Different types of grouping syntax and lists */

package tarski

import utility.Locations._

object Arounds {
  // A grouping token type (without side), or AnyGroup if we don't know.
  sealed abstract class Group
  case object Paren extends Group
  case object Brack extends Group
  case object Curly extends Group
  case object AnyGroup extends Group

  // Possibly grouping information around some syntax
  sealed abstract class Around extends HasRange {
    def a: SGroup
    def isNo: Boolean
    def isParens: Boolean
    def isBracks: Boolean
    def isCurlys: Boolean
  }
  // No grouped (e.g., juxtaposition apply)
  case class NoAround(r: SRange) extends Around {
    def a = SGroup.approx(r)
    def isNo = true
    def isParens = false
    def isBracks = false
    def isCurlys = false
  }
  // Explicit grouping, with possibly different types on either side
  case class YesAround(L: Group, R: Group, a: SGroup) extends Around {
    def r = a.lr
    def isNo = false
    def isParens = L==Paren && R==Paren
    def isBracks = L==Brack && R==Brack
    def isCurlys = L==Curly && R==Curly
  }
  case object Around {
    def apply(L: Loc[Group], R: Loc[Group]): YesAround =
      YesAround(L.x,R.x,SGroup(L.r,R.r))
    def apply(L: Group, Lr: SRange, R: Loc[Group]): YesAround =
      YesAround(L,R.x,SGroup(Lr,R.r))
  }

  // Lists with knowledge of their separators
  sealed abstract class KList[+A] {
    def list: List[A]
    def map[B](f: A => B): KList[B]
    def size = list.size
  }
  sealed trait CommaList[+A] extends KList[A]
  sealed trait AndList[+A] extends KList[A]
  sealed trait JuxtList[+A] extends KList[A]
  sealed trait CommaList1[+A] extends CommaList[A] { def preComma[B >: A](x: B, s: SRange): CommaList2[B] }
  sealed trait AndList1[+A] extends AndList[A] { def preAnd[B >: A](x: B, s: SRange): AndList2[B] }
  case object EmptyList extends CommaList[Nothing] with AndList[Nothing] {
    def list = Nil
    def map[B](f: Nothing => B) = EmptyList
  }
  case class SingleList[+A](x: A) extends CommaList1[A] with AndList1[A] with JuxtList[A] {
    def preComma[B >: A](y: B, s: SRange) = CommaList2(List(y,x),List(s))
    def preAnd[B >: A](y: B, s: SRange) = AndList2(List(y,x),List(s))
    def list = List(x)
    def map[B](f: A => B) = SingleList(f(x))
  }
  case class CommaList2[+A](list: List[A], seps: List[SRange]) extends CommaList1[A] {
    def preComma[B >: A](x: B, s: SRange) = CommaList2(x::list,s::seps)
    def map[B](f: A => B) = CommaList2(list map f,seps)
  }
  case class AndList2[+A](list: List[A], seps: List[SRange]) extends AndList1[A] {
    def preAnd[B >: A](x: B, s: SRange) = AndList2(x::list,s::seps)
    def map[B](f: A => B) = AndList2(list map f,seps)
  }
  case class JuxtList2[+A](list: List[A]) extends JuxtList[A] {
    def map[B](f: A => B) = JuxtList2(list map f)
  }
}
