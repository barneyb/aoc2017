package event2017.day20

import event2017.banner
import event2017.check
import java.io.File

/**
 *
 *
 * @author barneyb
 */

fun main(args: Array<String>) {
    val input = File("input/2017/day20.txt").readText()
    val exampleInput = "p=< 3,0,0>, v=< 2,0,0>, a=<-1,0,0>\n" +
            "p=< 4,0,0>, v=< 0,0,0>, a=<-2,0,0>"

    banner("part 1")
    val assertOne = check(::partOne)
    assertOne(exampleInput, 0)
    assertOne(input, 344)
    println("answer: " + partOne(input))

//    banner("part 2")
//    val assertTwo = check(::partTwo)
//    assertTwo(exampleInput, 309)
////    assertTwo(input, 290)
//    println("answer: " + partTwo(input))
}

private data class Vector(
        val x: Long,
        val y: Long,
        val z: Long
) {
    val m
        get() = Math.abs(x) + Math.abs(y) + Math.abs(z)
}

private data class Particle(
        val id: Int,
        val pos: Vector,
        val vel: Vector,
        val acc: Vector
)

private fun partOne(input: String): Int {
    val lines = input.trim()
            .replace("<", "")
            .replace(">", "")
            .replace(Regex(" *[pva]="), "")
            .split("\n")
    val particles = (0..lines.size)
            .zip(lines)
            .map { (id, line) ->
                val p = line.split(",")
                        .map { it.trim().toLong() }
                Particle(
                        id,
                        Vector(p[0], p[1], p[2]),
                        Vector(p[3], p[4], p[5]),
                        Vector(p[6], p[7], p[8])
                )
            }
    return particles
            .sortedWith(compareBy(
                    { it.acc.m },
                    { it.vel.m },
                    { it.pos.m }
            )
            )
            .first()
            .id
}

//private fun partTwo(input: String) =
//        input.length
