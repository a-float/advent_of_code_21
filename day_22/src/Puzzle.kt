import java.io.File

data class Cuboid(val isOn: Boolean, val xRange: IntRange, val yRange: IntRange, val zRange: IntRange)

fun readFileAsLines(fileName: String): List<String> = File(fileName).readLines()
fun lineToCuboid(line: List<String>): Cuboid {
    val isOn = line[0] == "on"
    val xRange = line[2].toInt()..line[3].toInt()
    val yRange = line[5].toInt()..line[6].toInt()
    val zRange = line[8].toInt()..line[9].toInt()
    return Cuboid(isOn, xRange, yRange, zRange)
}

// Correct for edges. If the cut has x=1..2, its neighbour should not have x=0..1, because of double x=1 cube
fun correctRange(idx: Int, range: IntRange): IntRange {
    return when (idx) {
        0 -> range.first until range.last
        2 -> (range.first + 1)..range.last
        else -> range
    }
}
// split a cuboid into up to 26 smaller cuboids whose union equals original cuboid minus the cut
fun splitCuboid(cuboid: Cuboid, cut: Cuboid): List<Cuboid> {
    val res: MutableList<Cuboid> = mutableListOf()
    val xSteps = listOf(cuboid.xRange.first, cut.xRange.first, cut.xRange.last, cuboid.xRange.last).sorted()
    val ySteps = listOf(cuboid.yRange.first, cut.yRange.first, cut.yRange.last, cuboid.yRange.last).sorted()
    val zSteps = listOf(cuboid.zRange.first, cut.zRange.first, cut.zRange.last, cuboid.zRange.last).sorted()
    for (ix in 0 until xSteps.lastIndex) {
        for (iy in 0 until ySteps.lastIndex) {
            for (iz in 0 until zSteps.lastIndex) {
                if (ix == iy && iy == iz && iz == 1) continue   // remove the cut
                if ((ix != 1 && xSteps[ix] == xSteps[ix + 1]) ||    // no place for a thin cuboid at the edge
                    (iy != 1 && ySteps[iy] == ySteps[iy + 1]) ||
                    (iz != 1 && zSteps[iz] == zSteps[iz + 1])
                ) continue
                if (cuboid.xRange.first <= xSteps[ix] && xSteps[ix + 1] <= cuboid.xRange.last &&   // in cuboid bounds
                    cuboid.yRange.first <= ySteps[iy] && ySteps[iy + 1] <= cuboid.yRange.last &&
                    cuboid.zRange.first <= zSteps[iz] && zSteps[iz + 1] <= cuboid.zRange.last
                ) {
                    res.add(
                        Cuboid(
                            true,
                            correctRange(ix, xSteps[ix]..xSteps[ix + 1]),
                            correctRange(iy, ySteps[iy]..ySteps[iy + 1]),
                            correctRange(iz, zSteps[iz]..zSteps[iz + 1])
                        )
                    )
                }
            }
        }
    }
    return res.toList();
}

// returns true if two cuboids intersect
fun intersects(cub1: Cuboid, cub2: Cuboid): Boolean {
    return !(cub1.xRange.last < cub2.xRange.first
            || cub1.xRange.first > cub2.xRange.last
            || cub1.zRange.last < cub2.zRange.first
            || cub1.zRange.first > cub2.zRange.last
            || cub1.yRange.last < cub2.yRange.first
            || cub1.yRange.first > cub2.yRange.last)
}

// adds cuboid to the list, preserving its trait of containing only non-intersecting ON cuboids
fun addCuboid(cuboid: Cuboid, list: MutableList<Cuboid>) {
    val toRemove = mutableListOf<Int>()
    val toAdd = mutableListOf<Cuboid>()
    for (i in 0..list.lastIndex) {
        if (intersects(list[i], cuboid)) {
            toRemove.add(i)
            toAdd.addAll(splitCuboid(list[i], cuboid))
        }
    }
    toRemove.asReversed().forEach {
        list.removeAt(it)
    }
    toAdd.forEach { addCuboid(it, list) }
    if (cuboid.isOn) list.add(cuboid) // no collisions here
}

fun main(args: Array<String>) {
    val cuboids = readFileAsLines("data.txt")
        .map { it.split(" ", ",", "=", "..") }
        .map { lineToCuboid(it) }

    val cuboidsOn: MutableList<Cuboid> = mutableListOf()
    cuboids.forEach { cub ->
        addCuboid(cub, cuboidsOn)
    }
    var totalOn: Long = 0
    cuboidsOn.forEach {
        totalOn += it.xRange.count().toLong() * it.yRange.count().toLong() * it.zRange.count().toLong()
    }
    println("There are $totalOn cubes that are on")
}