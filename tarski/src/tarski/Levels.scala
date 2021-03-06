/* Levels: Java language levels */

package tarski

object Levels {
  // To avoid Java confusion
  def getLevel(l: LangLevel): Int = l.level

  case class LangLevel(level: Int) extends AnyVal {
    def >=(x: LangLevel): Boolean = level >= x.level
  }

  val Unknown = LangLevel(-1)
  val Java1_3 = LangLevel(3)
  val Java1_4 = LangLevel(4)
  val Java5 = LangLevel(5)
  val Java6 = LangLevel(6)
  val Java7 = LangLevel(7)
  val Java8 = LangLevel(8)
}
