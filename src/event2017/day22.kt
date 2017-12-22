package event2017

import java.io.File

/**
 *
 *
 * @author barneyb
 */

fun main(args: Array<String>) {
    val input = File("input/2017/day22.txt").readText()
    val exampleInput = "..#\n" +
            "#..\n" +
            "..."

    banner("part 1")
    check(partOneFac(7))(exampleInput, 5)
    check(partOneFac(70))(exampleInput, 41)
    val assertOne = check(partOne)
    assertOne(exampleInput, 5587)
    assertOne(input, 5399)
    println("answer: " + partOne(input))

//    banner("part 2")
//    val assertTwo = check(::partTwo)
//    assertTwo(exampleInput, 309)
////    assertTwo(input, 290)
//    println("answer: " + partTwo(input))
}

private data class Carrier(
        val p: Point,
        val d: Direction = Direction.UP
) {

    fun turnRight() = Carrier(p, d.turnRight())

    fun turnLeft() = Carrier(p, d.turnLeft())

    fun step() = Carrier(p.step(d), d)

}

private data class State(
        val cluster: Cluster,
        val carrier: Carrier,
        val infectCount: Int = 0
) {
    override fun toString(): String {
        val (min, max) = cluster.fold(Pair(carrier.p, carrier.p), { (min, max), p ->
            Pair(Point(Math.min(min.x, p.x), Math.min(min.y, p.y)), Point(Math.max(max.x, p.x), Math.max(max.y, p.y)))
        })
        val sb = StringBuilder()
        for (row in min.y downTo max.y) { // flip it back over for display
            for (col in min.x..max.x) {
                val p = Point(col, row)
                sb.append(if (p == carrier.p) '[' else ' ')
                sb.append(if (p in cluster) '#' else '.')
                sb.append(if (p == carrier.p) ']' else ' ')
            }
            sb.append('\n')
        }
        return sb.toString()
    }
}

private fun partOneFac(iterations: Int) = { input: String ->
    val (cluster, origin) = parse(input)
    generateSequence(State(cluster, Carrier(origin)), { s ->
        if (s.carrier.p in s.cluster) {
            State(
                    s.cluster - s.carrier.p,
                    s.carrier.turnRight().step(),
                    s.infectCount
            )
        } else {
            State(
                    s.cluster + s.carrier.p,
                    s.carrier.turnLeft().step(),
                    s.infectCount + 1
            )
        }
    })
            .drop(iterations)
            .first()
            .infectCount
}

private typealias Cluster = Set<Point>

private fun parse(input: String): Pair<Cluster, Point> {
    val lines = input.trim().split("\n")
    val o = (lines.size - 1) / 2
    @Suppress("NAME_SHADOWING")
    val infected = lines.foldIndexed(setOf<Point>(), { row, ps, line ->
        // since "up" means "decrease y" in drawn-on-paper-land, but
        // point uses cartesian ruls (up means increase y), just
        // flip the drawing and run with it.
        val flippedRow = lines.size - row - 1
        line.foldIndexed(ps, { col, ps, c ->
            if (c == '#')
                ps + Point(col, flippedRow)
            else
                ps
        })
    })
    return Pair(infected, Point(o, o))
}

private val partOne = partOneFac(10000)

private fun partTwo(input: String) =
        input.length
