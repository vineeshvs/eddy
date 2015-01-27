package tarski

import tarski.Denotations.Stmt
import tarski.Environment.{ThreeEnv, PlaceInfo, Env}
import tarski.Items.{Value, TypeItem, Item}
import tarski.Scores._
import tarski.JavaScores._
import tarski.Semantics._
import tarski.Tokens._
import tarski.Tries.{LazyTrie, DTrie, Trie}
import utility.Locations._
import scala.annotation.tailrec
import scala.collection.JavaConverters._

object Tarski {

  def makeTrie(jvalues: java.util.Collection[Item]): Trie[Item] = {
    Trie(jvalues.asScala)
  }

  def makeTrie(jvalues: Array[Item]): Trie[Item] = {
    Trie(jvalues)
  }

  def makeDTrie(jvalues: java.util.Collection[Item]): DTrie[Item] = {
    DTrie(jvalues.asScala)
  }

  def environment(sTrie: LazyTrie[Item], dTrie: DTrie[Item], vTrie: Trie[Item],
                  dByItem: java.util.Map[TypeItem,Array[Value]],
                  vByItem: java.util.Map[TypeItem,Array[Value]],
                  scope: java.util.Map[Item,Integer], place: PlaceInfo,
                  checkThread: Runnable): Env = {
    println("environment with " + dTrie.values.length + " local items, " + vTrie.values.length + " scope items taken at " + place)
    new ThreeEnv(sTrie, dTrie, vTrie, dByItem, vByItem, scope.asScala.toMap.mapValues(_.intValue), place, checkThread)
  }

  def print(is: Iterable[Alt[Item]]): Unit = {
    is foreach { case Alt(p,i) =>
      println(s"  $p => " + i)
    }
  }

  // (show(s),s.toString,fullFormat,abbrevFormat) for a list of statements s.
  // Once we convert Stmt to ShowStmts, Env can be discarded.
  case class ShowStmts(show: String, den: String, full: String, abbrev: String)

  // Java and Scala result types
  type JList[A] = java.util.List[A]
  type  Results =  List[Alt[ShowStmts]]
  type JResults = JList[Alt[ShowStmts]]

  abstract class Take {
    // Accept some results, returning true if we've had enough.
    def take(rs: JResults): Boolean
  }

  // Feed results to a take instance until it's satisfied
  def fixTake(tokens: java.util.List[Loc[Token]], env: Env,
              format: (String,ShowFlags) => String, take: Take): Unit = {
    val toks = tokens.asScala.toList
    val r = fix(toks)(env)
    val sp = spaces(toks)

    println("input: " + Tokens.print(toks map (_.x))(abbrevShowFlags))

    // Take elements until we have enough, merging duplicates and adding their probabilities if found
    @tailrec def mergeTake(s: Stream[Alt[ShowStmts]], m: Map[String,Alt[ShowStmts]], notify: Boolean): Unit = {
      env.checkThread() // check if the thread was interrupted (as the probabilities decline, we hardly ever do env lookups)
      val rs = (m.values.toList sortBy (-_.p)).asJava
      val done = notify && take.take(rs) // if we shouldn't notify take, it gets no say in whether to continue, there's no new information.
      if (!done && s.nonEmpty) {
        val Alt(p,b) = s.head
        val a = b.abbrev
        println(s"found in stream: $p: $a")
        if (m contains a)
          mergeTake(s.tail, m, notify=false)
        else
          mergeTake(s.tail, m + ((a,Alt(p,b))), notify=true)
      } else if (!done && m.isEmpty) {
        // first time we get called, m is empty. If s is empty too, notify once with empty rs
        take.take(rs)
      }
    }

    val sc = r map (ss => {
      val abbrev   = show(ss)(Pretty.prettyStmts,abbrevShowFlags).trim
      val sentinel = show(ss)(Pretty.prettyStmts,sentinelShowFlags).trim
      val tokens   = insertSpaces(Pretty.tokens(ss)(Pretty.prettyStmts),sp)
      val full     = Tokens.print(tokens map (_.x))(fullShowFlags)
      ShowStmts(show=abbrev,den=ss.toString,
        full=format(full,fullShowFlags),
        abbrev=ShowFlags.replaceSentinels(format(sentinel,sentinelShowFlags)).replaceAll("""\s+"""," "))
    })
    // Complain if there's an error
    if (trackErrors) sc.strict match {
      case e:EmptyOrBad => println("fixJava failed:\n"+e.error.prefixed("error: "))
      case _:Best[_] => ()
    }
    mergeTake(sc.stream, Map.empty, notify=false)
  }

  def fix(tokens: List[Loc[Token]])(implicit env: Env): Scored[List[Stmt]] = {
    val asts = Mismatch.repair(prepare(tokens)) flatMap (ts => {
      val asts = ParseEddy.parse(ts)
      for (a <- asts; n = asts.count(a==_); if n > 1)
        throw new RuntimeException(s"AST duplicated $n times: $a")
      uniform(Pr.parse,asts,"Parse failed")
    })
    asts flatMap (denoteStmts(_)(env))
  }
}
