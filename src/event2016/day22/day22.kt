package event2016.day22

import event2017.Point
import event2017.banner
import event2017.check
import java.io.File

/**
 *
 *
 * @author barneyb
 */

fun main(args: Array<String>) {
    val input = File("input/2016/day22.txt").readText()

    banner("part 1")
    val assertOne = check(::partOne)
    assertOne(input, 864)
    println("answer: " + partOne(input))

//    banner("part 2")
//    val assertTwo = check(::partTwo)
//    assertTwo(exampleInput, 7)
////    assertTwo(input, 290)
//    println("answer: " + partTwo(input))
}

private data class Node(
        val capacity: Int,
        val used: Int
) {
    val available
        get() = capacity - used
}

private fun partOne(input: String): Int {
    val nodes = input.trim()
            .split("\n")
            .drop(2) // headers
            .map {
                val p = it.split(Regex(" +"))
                val (x, y) = p[0].split('-')
                        .drop(1)
                        .map {
                            it.substring(1).toInt()
                        }
                val (cap, u) = p.subList(1, 3)
                        .map {
                            // blindly assume all are the same units
                            it.substring(0, it.length - 1).toInt()
                        }
                Pair(Point(x, y), Node(cap, u))
            }
    return nodes
            .count { (p, n) ->
                n.used > 0 && nodes.any { (p2, n2) ->
                    p != p2 && n.used <= n2.available
                }
            }
}

//private fun partTwo(input: String) =
//        input.length
