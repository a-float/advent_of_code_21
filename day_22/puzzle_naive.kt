import java.io.File

typealias Matrix3D = Array<Array<Array<Boolean>>>

data class Cuboid(val isOn: Boolean, val xRange: IntRange, val yRange: IntRange, val zRange: IntRange)

fun readFileAsLines(fileName: String): List<String> = File(fileName).readLines()
fun lineToCuboid(line: List<String>): Cuboid {
    val isOn = line[0] == "on"
    val xRange = line[2].toInt()..line[3].toInt()
    val yRange = line[5].toInt()..line[6].toInt()
    val zRange = line[8].toInt()..line[9].toInt()
//    println("$isOn $xRange $yRange $zRange")
    return Cuboid(isOn, xRange, yRange, zRange)
}

fun markMatrix(cuboid: Cuboid, space: Matrix3D) {
    for (x in cuboid.xRange) {
        for (y in cuboid.yRange) {
            for (z in cuboid.zRange) {
                space[x + 50][y + 50][z + 50] = cuboid.isOn
            }
        }
    }
}

val cubes: Matrix3D = Array(110) { Array(110) { Array(110) { false } } }

fun main(args: Array<String>) {
    val cuboids = readFileAsLines("data.txt")
        .map { it.split(" ", ",", "=", "..") }
        .map { lineToCuboid(it) }
    cuboids.filter {
        it.xRange.first >= -50 && it.xRange.last <= 50 &&
                it.yRange.first >= -50 && it.yRange.last <= 50 &&
                it.zRange.first >= -50 && it.zRange.last <= 50
    }.forEach {
        println(it)
        markMatrix(it, cubes)
    }
    var totalOn = 0
    cubes.forEach { x ->
        x.forEach { y ->
            y.forEach { z ->
                if (z) totalOn += 1
            }
        }
    }
    println("There are $totalOn getCubes that are on")
}