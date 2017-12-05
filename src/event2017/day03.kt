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

fun steps(input:Int):Int {
    val dim = Math.sqrt(input.toDouble()).toInt() + 2
    val origin = Pair(dim / 2, dim / 2)
    var cur = Cursor(origin.first, origin.second, 1, Direction.RIGHT)
    val array = kotlin.arrayOfNulls<Int>(dim * dim)
    while (cur.n < input) {
        array[cur.x + cur.y * dim] = cur.n
        when (cur.d) {
            Direction.UP ->
                cur = if (array[(cur.x - 1) + cur.y * dim] == null)
                    cur.copy(n = cur.n + 1, x = cur.x - 1, d = Direction.LEFT)
                else
                    cur.copy(n = cur.n + 1, y = cur.y + 1)
            Direction.LEFT ->
                cur = if (array[cur.x + (cur.y - 1) * dim] == null)
                    cur.copy(n = cur.n + 1, y = cur.y - 1, d = Direction.DOWN)
                else
                    cur.copy(n = cur.n + 1, x = cur.x - 1)
            Direction.DOWN ->
                cur = if (array[(cur.x + 1) + cur.y * dim] == null)
                    cur.copy(n = cur.n + 1, x = cur.x + 1, d = Direction.RIGHT)
                else
                    cur.copy(n = cur.n + 1, y = cur.y - 1)
            Direction.RIGHT ->
                cur = if (array[cur.x + (cur.y + 1) * dim] == null)
                    cur.copy(n = cur.n + 1, y = cur.y + 1, d = Direction.UP)
                else
                    cur.copy(n = cur.n + 1, x = cur.x + 1)
        }
    }
    return Math.abs(origin.first - cur.x) + Math.abs(origin.second - cur.y)
}
