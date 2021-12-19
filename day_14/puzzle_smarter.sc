import scala.collection.mutable.ListBuffer
import scala.io.Source

val file = Source.fromFile("C:\\Users\\mati8\\Desktop\\AdventOfCode\\day_14\\puzzle_data.txt")
val lines = file.getLines.toList
file.close()
var poly: List[Char] = lines.take(1).head.toList
val insertMap = lines.drop(2)
  .map(i => i.split(" -> "))
  .map(si => si.head -> si(1).head)
  .toMap

@inline def doStep(): Unit = {
  val nextPoly = new ListBuffer[Char]()
  nextPoly += poly.head
  poly.zip(poly.drop(1)).foreach(
    pair => {
      nextPoly += insertMap(pair.productIterator.mkString(""))
      nextPoly += pair._2
    }
  )
  poly = nextPoly.toList
  println(poly.mkString(""))
}

@inline def getScore: Int = {
  val max = poly.groupBy(identity).maxBy(_._2.size)._2.size
  val min = poly.groupBy(identity).minBy(_._2.size)._2.size
  max-min
}

for (_ <- 1 to 40) {
  doStep()
}
println(getScore)