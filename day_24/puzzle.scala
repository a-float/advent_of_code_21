import scala.util.{Try, Success, Failure}

val d0: List[Int] = List(1, 1, 1, 1, 26, 1, 26, 26, 1, 1, 26, 26, 26, 26)
val d1: List[Int] =
  List(14, 13, 13, 12, -12, 12, -2, -11, 13, 14, 0, -12, -13, -6)
val d2: List[Int] = List(8, 8, 3, 10, 8, 8, 8, 5, 9, 3, 4, 9, 2, 7)

class Stack(val maxSize: Int) {
  private val _arr = Array.ofDim[Int](maxSize)
  private var _size = 0

  def push(x: Int) = {
    if (_size == maxSize) {
      throw new RuntimeException("Push onto full stack")
    }
    _arr(_size) = x
    _size += 1
  }

  def top(): Int = {
    if (_size > 0) _arr(_size - 1)
    else 0 // nature of the ALU stack
  }

  def pop(): Try[Int] = {
    if (_size == 0) {
      Failure(new Exception("Pop from empty stack"))
    } else {
      _size -= 1
      Success(_arr(_size))
    }
  }

  override def toString: String =
    "[" + _arr.slice(0, _size).mkString(", ") + "]"
}

// manual testing
def isValidNumber(input: List[Int]): Boolean = {
  val z = new Stack(10)
  var x = 0;
  for (i <- 0 to 13) {
    x = z.top() + d1(i)
    if (d0(i) != 1) {
      z.pop()
      if (x != input(i)) {
        println(s"Failed to get a pop. x=$x w=${input(i)}")
        return false
      } else {
        println(s"Successfully popped using w=${input(i)}")
      }
    }
    if (x != input(i)) {
      println(s"Pushing ${input(i)} + ${d2(i)}")
      z.push(input(i) + d2(i))
    }
    println(z)
  }
  return true
}

def main(args: Array[String]) = {
  val inputString = "16931171414113"
  val input = inputString.toList.map(s => s.asDigit)
  val res = if (isValidNumber(input)) "valid" else "invalid"
  println(s"Model number $inputString is $res")
}