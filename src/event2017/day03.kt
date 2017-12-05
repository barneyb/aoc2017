package event2017

/**
 *
 *
 * @author barneyb
 */

fun main(args: Array<String>) {
    val input = 361527

    banner("part one")
    val partOne = check(::steps)
    partOne(1, 0)
    partOne(12, 3)
    partOne(23, 2)
    partOne(1024, 31)
    partOne(input, 326)
    println("answer: " + steps(input))

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
