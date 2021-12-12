import java.io.File

val fileName = "puzzle_data.txt"
val crabs = File(fileName).readLines()[0].split(",").map { it.trim().toInt() }.toMutableList()

fun getNthElement(arr: MutableList<Int>, from: Int, to: Int, n: Int): Int {
    val picked = arr[to]
    var leftWall = from
    arr.subList(from, to).forEachIndexed() { i, num ->
        if (num <= picked) {
            arr[leftWall] = num.also { arr[from + i] = arr[leftWall] }
            leftWall += 1
        }
    }
    arr[leftWall] = picked.also { arr[to] = arr[leftWall] }
    if (leftWall == n) return arr[n]
    else if (leftWall > n) return getNthElement(arr, 0, leftWall - 1, n)
    else return getNthElement(arr, leftWall + 1, to, n)
}

fun getFuelUsage(arr: List<Int>, targetPos: Int): Int {
    return arr.map { Math.abs(it - targetPos) }.sum()
}

fun getNewFuelUsage(arr: List<Int>, targetPos: Int): Int {
    return arr.map {
        val diff = Math.abs(it - targetPos);
        ((1 + diff) * diff) / 2
    }.sum()
}

// puzzle 1
// val pos = getNthElement(crabs, 0, crabs.lastIndex, crabs.size / 2 - 1)
// println("pos = $res and total fuel cost = ${getFuelUsage(crabs, pos)}")

// puzzle 2
val pos = Math.floor(crabs.sum()/crabs.size.toDouble()).toInt()
println("pos = $pos and new total fuel cost = ${getNewFuelUsage(crabs, pos)}")

//for(pos in 400..600) {
//    println("$pos\t${getNewFuelUsage(crabs, pos)}")
//}
// best 485     99634572