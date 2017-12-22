package event2017

import event2017.NodeState.*
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
//    println("answer: " + partOne(input))

    banner("part 2")
    check(partTwoFac(100))(exampleInput, 26)
//    val assertTwo = check(partTwo)
//    assertTwo(exampleInput, 2511944)
////    assertTwo(input, 290)
//    println("answer: " + partTwo(input))
}

private open class Carrier(
        val p: Point,
        val d: Direction
) {
    // psuedo-emulate a data class's copy method
    protected open fun dupe(p: Point, d: Direction) =
            Carrier(p, d)

    fun turnRight() = dupe(p, d.turnRight())

    fun turnLeft() = dupe(p, d.turnLeft())

    fun reverse() = dupe(p, d.reverse())

    fun step() = dupe(p.step(d), d)

    open fun burst(s: GameState) =
            if (p in s.cluster) GameState(
                    s.cluster - p,
                    turnRight().step(),
                    s.infectCount
            ) else GameState(
                    s.cluster + (p to INFECTED),
                    turnLeft().step(),
                    s.infectCount + 1
            )
}

private class EvolvedCarrier(
        p: Point,
        d: Direction
) : Carrier(p, d) {
    // psuedo-emulate a data class's copy method
    override fun dupe(p: Point, d: Direction) =
            EvolvedCarrier(p, d)

    override fun burst(s: GameState) =
            when (s.cluster.getOrDefault(p, CLEAN)) {
                CLEAN -> GameState(
                        s.cluster + (p to WEAK),
                        turnLeft().step(),
                        s.infectCount
                )
                WEAK -> GameState(
                        s.cluster + (p to INFECTED),
                        step(),
                        s.infectCount + 1
                )
                INFECTED -> GameState(
                        s.cluster + (p to FLAGGED),
                        turnRight().step(),
                        s.infectCount
                )
                FLAGGED -> GameState(
                        s.cluster - p,
                        reverse().step(),
                        s.infectCount
                )
            }

}

private enum class NodeState {
    CLEAN, WEAK, INFECTED, FLAGGED
}

private data class GameState(
        val cluster: Cluster,
        val carrier: Carrier,
        val infectCount: Int = 0
) {
    override fun toString(): String {
        val (min, max) = cluster.keys.fold(
                Pair(carrier.p, carrier.p),
                { (min, max), p ->
                    Pair(
                            Point(Math.min(min.x, p.x), Math.min(min.y, p.y)),
                            Point(Math.max(max.x, p.x), Math.max(max.y, p.y))
                    )
                }
        )
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

private fun partOneFac(iterations: Int) =
        partAnyFac(iterations, { o -> Carrier(o, Direction.UP) })

private fun partAnyFac(iterations: Int, carrierFactory: (Point) -> Carrier) = { input: String ->
    val (cluster, origin) = parse(input)
    generateSequence(GameState(cluster, carrierFactory(origin)), { s ->
        s.carrier.burst(s)
    })
            .drop(iterations)
            .first()
            .infectCount
}

private typealias Cluster = Map<Point, NodeState>

private fun parse(input: String): Pair<Cluster, Point> {
    val lines = input.trim().split("\n")
    val o = (lines.size - 1) / 2
    @Suppress("NAME_SHADOWING")
    val infected = lines.foldIndexed(mapOf<Point, NodeState>(), { row, ps, line ->
        // since "up" means "decrease y" in drawn-on-paper-land, but
        // point uses cartesian ruls (up means increase y), just
        // flip the drawing and run with it.
        val flippedRow = lines.size - row - 1
        line.foldIndexed(ps, { col, ps, c ->
            if (c == '#')
                ps + (Point(col, flippedRow) to INFECTED)
            else
                ps
        })
    })
    return Pair(infected, Point(o, o))
}

private val partOne = partOneFac(10000)

private fun partTwoFac(iterations: Int) =
        partAnyFac(iterations, { o -> EvolvedCarrier(o, Direction.UP) })

private val partTwo = partTwoFac(10_000_000)
