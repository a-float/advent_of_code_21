import scala.collection.{MapView, mutable}
import scala.collection.mutable.ListBuffer
import scala.io.Source

val file = Source.fromFile("C:\\Users\\mati8\\Desktop\\AdventOfCode\\day_14\\puzzle_data.txt")
val lines = file.getLines.toList
file.close()
val poly: List[Char] = lines.take(1).head.toList
val insertMap = lines.drop(2)
  .map(i => i.split(" -> "))
  .map(si => (si.head(0), si.head(1)) -> si(1).head)
  .toMap

var polyMap: Map[(Char, Char), Long] = poly.zip(poly.drop(1))
  .groupBy(identity)
  .mapValues(_.size.toLong).toMap

@inline def doStep(): Unit = {
  val nextPoly = new mutable.HashMap[(Char, Char), Long]().withDefaultValue(0)
  polyMap.foreach(
    pair => {
//      println(pair)
      nextPoly((pair._1._1, insertMap(pair._1))) += pair._2
      nextPoly((insertMap(pair._1), pair._1._2)) += pair._2
    }
  )
  polyMap = nextPoly.toMap
}

@inline def getScore: Long = {
  val letterCounts = mutable.HashMap[Char, Long]().withDefaultValue(0)
  polyMap.foreach(pair => {
    letterCounts(pair._1._1) += pair._2
    letterCounts(pair._1._2) += pair._2
  })
  letterCounts(poly.head) += 1
  letterCounts(poly.last) += 1
  val max = letterCounts.maxBy(pair => pair._2)._2
  val min = letterCounts.minBy(pair => pair._2)._2
  max/2-min/2
}

for (_ <- 1 to 40) {
  doStep()
}
println(getScore)