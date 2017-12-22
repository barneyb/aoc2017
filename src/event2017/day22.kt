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
    println("answer: " + partOne(input))

    banner("part 2")
    check(partTwoFac(100))(exampleInput, 26)
    val assertTwo = check(partTwo)
//    assertTwo(exampleInput, 2511944)
    assertTwo(input, 2511776)
//    println("answer: " + partTwo(input))
}

private open class Carrier(
        var p: Point,
        var d: Direction = Direction.UP,
        var infectCount: Int = 0
) {
    fun turnRight() {
        d = d.turnRight()
    }

    fun turnLeft() {
        d = d.turnLeft()
    }

    fun reverse() {
        d = d.reverse()
    }

    fun step() {
        p = p.step(d)
    }

    fun infect() {
        infectCount += 1
    }

    open fun burst(cluster: Cluster) {
        if (p in cluster) {
            cluster.remove(p)
            turnRight()
        } else {
            cluster.put(p, INFECTED)
            infect()
            turnLeft()
        }
        step()
    }
}

private class EvolvedCarrier(
        p: Point
) : Carrier(p) {

    override fun burst(cluster: Cluster) {
        when (cluster.getOrDefault(p, CLEAN)) {
            CLEAN -> {
                cluster.put(p, WEAK)
                turnLeft()
            }
            WEAK -> {
                cluster.put(p, INFECTED)
                infect()
            }
            INFECTED -> {
                cluster.put(p, FLAGGED)
                turnRight()
            }
            FLAGGED -> {
                cluster.remove(p)
                reverse()
            }
        }
        step()
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
//        val min = Point(-3, -3)
//        val max = Point(5, 4)
        val sb = StringBuilder()
        for (row in max.y downTo min.y) { // flip it back over for display
            for (col in min.x..max.x) {
                val p = Point(col, row)
                sb.append(if (p == carrier.p) '[' else ' ')
                sb.append(when (cluster.getOrDefault(p, CLEAN)) {
                    CLEAN -> '.'
                    WEAK -> 'W'
                    INFECTED -> '#'
                    FLAGGED -> 'F'
                })
                sb.append(if (p == carrier.p) ']' else ' ')
            }
            sb.append('\n')
        }
        return sb.toString()
    }
}

private fun partOneFac(iterations: Int) =
        partAnyFac(iterations, { o -> Carrier(o) })

private fun partAnyFac(iterations: Int, carrierFactory: (Point) -> Carrier) = { input: String ->
    val (cluster, origin) = parse(input)
    val carrier = carrierFactory(origin)
    for (n in 1..iterations) {
        carrier.burst(cluster)
    }
    carrier.infectCount
}

private typealias Cluster = MutableMap<Point, NodeState>

private fun parse(input: String): Pair<Cluster, Point> {
    val lines = input.trim().split("\n")
    val o = (lines.size - 1) / 2
    val infected = mutableMapOf<Point, NodeState>()
    lines.forEachIndexed { row, line ->
        // since "up" means "decrease y" in drawn-on-paper-land, but
        // Point uses cartesian rules (up means increase y), just
        // flip the drawing and run with it.
        val flippedRow = lines.size - row - 1
        line.forEachIndexed { col, c ->
            if (c == '#')
                infected.put(Point(col, flippedRow), INFECTED)
        }
    }
    return Pair(infected, Point(o, o))
}

private val partOne = partOneFac(10000)

private fun partTwoFac(iterations: Int) =
        partAnyFac(iterations, { o -> EvolvedCarrier(o) })

private val partTwo = partTwoFac(10_000_000)
