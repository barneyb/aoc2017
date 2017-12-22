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

private data class Carrier(
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
        partAnyFac(iterations, { cluster: Cluster, carrier: Carrier ->
            if (carrier.p in cluster) {
                cluster.remove(carrier.p)
                carrier.turnRight()
            } else {
                cluster.put(carrier.p, INFECTED)
                carrier.infect()
                carrier.turnLeft()
            }
            carrier.step()
        })

private fun partAnyFac(
        iterations: Int,
        burst: (Cluster, Carrier) -> Unit
) = { input: String ->
    val (cluster, origin) = parse(input)
    val carrier = Carrier(origin)
    for (n in 1..iterations) {
        burst(cluster, carrier)
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
        partAnyFac(iterations, { cluster: Cluster, carrier: Carrier ->
            when (cluster.getOrDefault(carrier.p, CLEAN)) {
                CLEAN -> {
                    cluster.put(carrier.p, WEAK)
                    carrier.turnLeft()
                }
                WEAK -> {
                    cluster.put(carrier.p, INFECTED)
                    carrier.infect()
                }
                INFECTED -> {
                    cluster.put(carrier.p, FLAGGED)
                    carrier.turnRight()
                }
                FLAGGED -> {
                    cluster.remove(carrier.p)
                    carrier.reverse()
                }
            }
            carrier.step()
        })

private val partTwo = partTwoFac(10_000_000)
