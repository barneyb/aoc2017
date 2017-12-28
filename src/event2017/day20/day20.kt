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
        val id: Int,
        val pos: Vector,
        val vel: Vector,
        val acc: Vector
) {
    fun tick(): Particle {
        val newVel = vel + acc
        return Particle(
                id,
                pos + newVel,
                newVel,
                acc
        )
    }
}

private fun partOne(input: String): Int = parse(input)
        .sortedWith(compareBy(
                { it.acc.m },
                { it.vel.m },
                { it.pos.m }
        )
        )
        .first()
        .id

private fun parse(input: String): List<Particle> {
    val lines = input.trim()
            .replace("<", "")
            .replace(">", "")
            .replace(Regex(" *[pva]="), "")
            .split("\n")
    return (0..lines.size)
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
}

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
                .first()
                .size
