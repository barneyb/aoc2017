package event2017.day15

import event2017.banner
import event2017.check
import java.io.File

/**
 *
 *
 * @author barneyb
 */

fun main(args: Array<String>) {
    val input = File("input/2017/day15.txt").readText()
    val exampleInput = "generator a is 65\n" +
            "generator b is 8921"

    banner("part 1")
    val partOne = check(::matches)
    partOne(exampleInput, 588)
    partOne(input, 619)
//    println("answer: " + matches(input))
}

private fun parse(input: String) =
        input.trim().split("\n").map { it.trim().split(" ").last().toLong() }

private fun generator(seed: Long, factor: Long) =
    generateSequence(seed, { n ->
        n * factor % 2147483647
    })
            .drop(1) // don't want the seed
            .map {
                // get the last 16 bits
                it and 0xffff
            }

fun matches(input: String): Int {
    val p = parse(input)
    return generator(p.first(), 16807)
            .zip(generator(p.last(), 48271))
            .take(40_000_000)
            .count {
                (a, b) -> a == b
            }
}
