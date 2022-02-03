import java.io.File

data class StepResult(val newFloor: Seafloor, val moveCount: Long)

class Seafloor(_sizeX: Int, _sizeY: Int) {
    private val sizeX = _sizeX
    private val sizeY = _sizeY
    private val board: Array<Char> = Array(sizeY * sizeX) { '.' }

    fun get(x: Int, y: Int): Char {
        return board[(y % sizeY) * sizeX + (x % sizeX)]
    }

    fun set(x: Int, y: Int, c: Char) {
        board[(y % sizeY) * sizeX + (x % sizeX)] = c
    }

    fun show(): Unit {
        for (y in 0 until sizeY) {
            for (x in 0 until sizeX) {
                print(get(x, y))
            }
            println()
        }
    }

    fun step(): StepResult {
        var moves: Long = 0
        val newFloor = Seafloor(this.sizeX, this.sizeY)
        for (y in 0 until sizeY) {
            for (x in 0 until sizeX) {
                if (get(x, y) == '>') {
                    if (get(x + 1, y) == '.') {
                        newFloor.set(x + 1, y, '>')
                        moves++
                    } else {
                        newFloor.set(x, y, '>')
                    }
                }
            }
        }
        for (y in 0 until sizeY) {
            for (x in 0 until sizeX) {
                if (get(x, y) == 'v') {
                    if (get(x, y + 1) != 'v' && newFloor.get(x, y + 1) == '.') {
                        newFloor.set(x, y + 1, 'v')
                        moves++
                    } else {
                        newFloor.set(x, y, 'v')
                    }
                }
            }
        }
        return StepResult(newFloor, moves)
    }
}

fun readFileAsLines(fileName: String): List<String> = File(fileName).readLines()

fun main(args: Array<String>) {
    val lines = readFileAsLines("data.txt")
    var floor = Seafloor(lines[0].length, lines.size)
    lines.forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            floor.set(x, y, c)
        }
    }
    var steps: Long = 0
    while (true) {
        val res = floor.step()
        steps++
        if (res.moveCount == 0L) {
            break
        }
        floor = res.newFloor
    //    println("Move count = ${res.moveCount}")
    //    floor.show()
    }
    println("Stops after $steps steps")
}