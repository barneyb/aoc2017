package event2017.day20

import event2017.banner
import event2017.check
import event2017.count
import event2017.zip
import java.io.File

/**
 *
 *
 * @author barneyb
 */

fun main(args: Array<String>) {
    val input = File("input/2017/day20.txt").readText()

    banner("part 1")
    val assertOne = check(::partOne)
    assertOne("p=< 3,0,0>, v=< 2,0,0>, a=<-1,0,0>\n" +
            "p=< 4,0,0>, v=< 0,0,0>, a=<-2,0,0>", 0)
    assertOne(input, 344)
    println("answer: " + partOne(input))

    banner("part 2")
    val assertTwo = check(::partTwo)
    assertTwo("p=<-6,0,0>, v=< 3,0,0>, a=< 0,0,0>\n" +
            "p=<-4,0,0>, v=< 2,0,0>, a=< 0,0,0>\n" +
            "p=<-2,0,0>, v=< 1,0,0>, a=< 0,0,0>\n" +
            "p=< 3,0,0>, v=<-1,0,0>, a=< 0,0,0>\n", 1) // particle 3
    assertTwo(input, 404)
//    println("answer: " + partTwo(input))
}

private data class Vector(
        val x: Long,
        val y: Long,
        val z: Long
) {
    val m
        get() = Math.abs(x) + Math.abs(y) + Math.abs(z)

    operator fun plus(v: Vector) =
            Vector(
                    x + v.x,
                    y + v.y,
                    z + v.z
            )

    operator fun minus(v: Vector) =
            Vector(
                    x - v.x,
                    y - v.y,
                    z - v.z
            )
}

private data class Particle(
        val pos: Vector,
        val vel: Vector,
        val acc: Vector
) {
    fun tick(): Particle {
        val newVel = vel + acc
        return Particle(
                pos + newVel,
                newVel,
                acc
        )
    }
}

private fun partOne(input: String): Int =
        parse(input)
                .zip(count())
                .sortedWith(compareBy(
                        { it.first.acc.m },
                        { it.first.vel.m },
                        { it.first.pos.m }
                )
                )
                .first()
                .second

private fun parse(input: String): List<Particle> {
    return input.trim()
            .replace("<", "")
            .replace(">", "")
            .replace(Regex(" *[pva]="), "")
            .split("\n")
            .map { line ->
                val p = line.split(",")
                        .map { it.trim().toLong() }
                Particle(
                        Vector(p[0], p[1], p[2]),
                        Vector(p[3], p[4], p[5]),
                        Vector(p[6], p[7], p[8])
                )
            }
}

private val List<Vector>.bounds
    get() = drop(1).fold(Pair(first(), first()), { (min, max), it ->
        Pair(
                Vector(
                        Math.min(min.x, it.x),
                        Math.min(min.y, it.y),
                        Math.min(min.z, it.z)
                ),
                Vector(
                        Math.max(max.x, it.x),
                        Math.max(max.y, it.y),
                        Math.max(max.z, it.z)
                )
        )
    })

private fun partTwo(input: String) =
        generateSequence(parse(input), { ps ->
            val collisions = ps.groupBy { it.pos }
                    .filter { (_, ps) ->
                        ps.size > 1
                    }
                    .keys
            ps.filter {
                it.pos !in collisions
            }.map {
                it.tick()
            }
        })
                .drop(45)
                .filter {
                    println("hit iteration limit")
                    println("position: " + it.map { it.pos }.bounds)
                    println("velocity: " + it.map { it.vel }.bounds)
                    println("accel: " + it.map { it.acc }.bounds)
                    true
                }
                .first()
                .size
