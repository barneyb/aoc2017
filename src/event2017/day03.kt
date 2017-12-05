package event2017

/**
 *
 *
 * @author barneyb
 */

fun main(args: Array<String>) {
    val input = 361527

    banner("part one (analytical)")
    val partOne = check(::analytical)
    partOne(1, 0)
    partOne(12, 3)
//    partOne(23, 2)
//    partOne(1024, 31)
    partOne(input, 326)
    println("answer: " + steps(input))

    banner("part one (array)")
    val partOneArray = check(::steps)
    partOneArray(1, 0)
    partOneArray(12, 3)
    partOneArray(23, 2)
    partOneArray(1024, 31)
    partOneArray(input, 326)
    println("answer: " + steps(input))

    banner("part two")
    val partTwo = check(::firstLarger)
    partTwo(12, 23)
    partTwo(54, 57)
    partTwo(500, 747)
    println("answer: " + firstLarger(input))

}

fun analytical(input:Int):Int {
    if (input == 1) {
        return 0
    }
    val base = Math.floor(Math.sqrt(input.toDouble())).toInt()
    val halfSpan = base / 2
    val delta = input - base * base
    if (base % 2 == 0) {
        throw UnsupportedOperationException("didn't figure out this quadrants")
    }
    return base - 1 - halfSpan + (delta - halfSpan)
}

enum class Direction {
    UP, DOWN, LEFT, RIGHT
}

data class Cursor(
    val x: Int,
    val y: Int,
    val n: Int,
    val d: Direction
)

typealias Grid = Pair<Int, Array<Int>>

fun makeGrid(input:Int):Grid {
    val dim = Math.sqrt(input.toDouble()).toInt() + 2
    return Pair(dim, Array(dim * dim, { 0 }))
}

fun spiral(grid:Grid, dropUntil:Int, nWork:(Cursor) -> Cursor):Cursor {
    var cur = Cursor(grid.first / 2, grid.first / 2, 1, Direction.RIGHT)
    while (cur.n < dropUntil) {
        grid.second[cur.x + cur.y * grid.first] = cur.n
        when (cur.d) {
            Direction.UP ->
                cur = if (grid.second[(cur.x - 1) + cur.y * grid.first] == 0)
                    nWork(cur.copy(x = cur.x - 1, d = Direction.LEFT))
                else
                    nWork(cur.copy(y = cur.y + 1))
            Direction.LEFT ->
                cur = if (grid.second[cur.x + (cur.y - 1) * grid.first] == 0)
                    nWork(cur.copy(y = cur.y - 1, d = Direction.DOWN))
                else
                    nWork(cur.copy(x = cur.x - 1))
            Direction.DOWN ->
                cur = if (grid.second[(cur.x + 1) + cur.y * grid.first] == 0)
                    nWork(cur.copy(x = cur.x + 1, d = Direction.RIGHT))
                else
                    nWork(cur.copy(y = cur.y - 1))
            Direction.RIGHT ->
                cur = if (grid.second[cur.x + (cur.y + 1) * grid.first] == 0)
                    nWork(cur.copy(y = cur.y + 1, d = Direction.UP))
                else
                    nWork(cur.copy(x = cur.x + 1))
        }
    }
    return cur
}

fun steps(input:Int):Int {
    val grid = makeGrid(input)
    val origin = Pair(grid.first / 2, grid.first / 2)
    val cur = spiral(grid, input, { cur -> cur.copy(n = cur.n + 1) })
    return Math.abs(origin.first - cur.x) + Math.abs(origin.second - cur.y)
}

fun firstLarger(input:Int):Int {
    val grid = makeGrid(input)
    val cur = spiral(grid, input + 1, { cur ->
        cur.copy(n = grid.second[(cur.x + 1) +  cur.y * grid.first]
            + grid.second[ cur.x      + (cur.y + 1) * grid.first]
            + grid.second[(cur.x - 1) +  cur.y * grid.first]
            + grid.second[ cur.x      + (cur.y - 1) * grid.first]
            + grid.second[(cur.x + 1) + (cur.y + 1) * grid.first]
            + grid.second[(cur.x + 1) + (cur.y - 1) * grid.first]
            + grid.second[(cur.x - 1) + (cur.y + 1) * grid.first]
            + grid.second[(cur.x - 1) + (cur.y - 1) * grid.first]
        )
    })
    return cur.n
}
