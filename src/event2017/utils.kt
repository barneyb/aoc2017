package event2017

/**
 *
 *
 * @author barneyb
 */


fun banner(label: String = "") {
    println((
            if (label.trim().length == 0)
                ""
            else
                "= $label "
            ).padEnd(80, '='))
}

private var check_counter = 0

fun <I, R> check(functionUnderTest: (I) -> R): (I, R) -> Boolean {
    val check = ++check_counter
    var run = 0
    return { input: I, expected: R ->
        run += 1
        print("${check.toString().padStart(2)}.${run.toString().padEnd(3)}: ")
        val startedAt = System.currentTimeMillis()
        val actual = functionUnderTest(input)
        val elapsed = System.currentTimeMillis() - startedAt
        val pass = actual == expected
        if (pass) {
            print("PASS")
        } else {
            var vi = "'" + input.toString() + "'"
            if (vi.length > 100) {
                vi = vi.substring(0, 90) + "...' (${vi.length - 90} chars truncated)"
            }
            print("FAIL: Expected '$expected' but got '$actual' (from ${vi.replace("\n", "\n    ")})")
        }
        println(" (${"%,d".format(elapsed)} ms)")
        pass
    }
}

fun count(start: Int = 0, step: Int = 1) =
        generateSequence(start, { it + step })

fun <T, R> Iterable<T>.scan(initial: R, operation: (acc: R, T) -> R): List<R> =
        this.fold(Pair(initial, mutableListOf(initial)), { (acc, scan), it ->
            val next = operation(acc, it)
            scan.add(next)
            Pair(next, scan)
        })
                .second

enum class Direction {
    UP, DOWN, LEFT, RIGHT;

    fun turnRight() =
            when (this) {
                UP -> RIGHT
                RIGHT -> DOWN
                DOWN -> LEFT
                LEFT -> UP
            }

    fun turnLeft() =
            when (this) {
                UP -> LEFT
                LEFT -> DOWN
                DOWN -> RIGHT
                RIGHT -> UP
            }

    fun reverse() =
            when (this) {
                UP -> DOWN
                LEFT -> RIGHT
                DOWN -> UP
                RIGHT -> LEFT
            }
}

data class Point(
        val x: Int,
        val y: Int
) {

    fun up()    = Point(x, y + 1)
    fun down()  = Point(x, y - 1)
    fun left()  = Point(x - 1, y)
    fun right() = Point(x + 1, y)

    operator fun plus(d: Direction) =
            when (d) {
                Direction.UP -> up()
                Direction.DOWN -> down()
                Direction.LEFT -> left()
                Direction.RIGHT -> right()
            }

}
