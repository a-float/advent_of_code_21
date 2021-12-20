import java.io.File

typealias BoardSet = MutableSet<Pair<Int, Int>>
data class BoardLimits(val minX: Int, val maxX: Int, val minY: Int, val maxY: Int)

fun readFileAsLines(fileName: String): List<String> = File(fileName).readLines()

fun main(args: Array<String>) {
    val lines = readFileAsLines("data.txt")
    val mapping = lines[0].map { if (it == '.') 0 else 1 }
    var board: BoardSet = mutableSetOf()
    for (i in 2..lines.lastIndex) {
        lines[i].forEachIndexed { idx, chr -> if (chr == '#') board.add(Pair(idx, i - 2)) }
    }
    if(mapping[0] == mapping[511] && mapping[0] == 1){
        println("Infinite lit pixels")
        return
    }
//    showBoard(board)
    repeat(25) {
        board = doubleEnhance(board, mapping)
    }
//    println()
//    showBoard(board)
    println("There are ${board.size} lit pixels")
}

fun getBoardRanges(board: BoardSet): BoardLimits {
    val xs = board.map { it.first }
    val ys = board.map { it.second }
    if(board.isEmpty()){
        return BoardLimits(0,0,0,0)
    }
    val xRange = xs.minOrNull()!!..xs.maxOrNull()!!
    val yRange = ys.minOrNull()!!..ys.maxOrNull()!!
    return BoardLimits(xRange.first, xRange.last, yRange.first, yRange.last)
}

fun getPixel(pos: Pair<Int, Int>, board: BoardSet, limits: BoardLimits, outOfBoundDefault: Int): Int {
    return if ((limits.minX..limits.maxX).contains(pos.first) &&
        (limits.minY..limits.maxY).contains(pos.second)
    ) {
        if (board.contains(pos)) 1 else 0
    } else outOfBoundDefault
}

fun enhance(board: BoardSet, mapping: List<Int>, outOfBoundDefault: Int): BoardSet {
    val newBoard: BoardSet = mutableSetOf()
    val limits = getBoardRanges(board)
    for (y in limits.minY - 2..limits.maxY + 2) {
        for (x in limits.minX - 2..limits.maxX + 2) {
            var mult = 1 shl 8;
            var idx = 0
            for (dy in -1..1) {
                for (dx in -1..1) {
                    idx += mult * getPixel(Pair(x + dx, y + dy), board, limits, outOfBoundDefault)
                    mult /= 2
                }
            }
            if (mapping[idx] == 1) newBoard.add(Pair(x, y))
        }
    }
    return newBoard
}

fun doubleEnhance(board: BoardSet, mapping: List<Int>): BoardSet {
    val afterFirstEnhance = enhance(board, mapping, 0)
    return enhance(afterFirstEnhance, mapping, mapping[0])
}

fun showBoard(board: BoardSet) {
    val limits = getBoardRanges(board)
    for (y in limits.minY - 1..limits.maxY + 1) {
        for (x in limits.minX - 1..limits.maxX + 1) {
            if (board.contains(Pair(x, y))) print('#')
            else print('.')
        }
        println()
    }
}
