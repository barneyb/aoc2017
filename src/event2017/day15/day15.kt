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

    banner("part 2")
    val partTwo = check(::filteredMatches)
    partTwo(exampleInput, 309)
    partTwo(input, 290)
//    println("answer: " + filteredMatches(input))
}

typealias Generator = Sequence<Long>
typealias GenPair = Pair<Generator, Generator>

private fun parse(input: String): GenPair {
    val seeds = input.trim().split("\n")
            .map { it.trim().split(" ").last().toLong() }
    return Pair(generator(seeds[0], 16807),
            generator(seeds[1], 48271))
}

private fun generator(seed: Long, factor: Long) =
        generateSequence(seed, { n ->
            n * factor % 2147483647
        })
                .drop(1) // don't want the seed
                .map {
                    // get the last 16 bits
                    it and 0xffff
                }

private fun judge(p: GenPair, rounds: Int) =
        p.first
                .zip(p.second)
                .take(rounds)
                .count { (a, b) ->
                    a == b
                }

fun matches(input: String) =
        judge(parse(input), 40_000_000)

fun filteredMatches(input: String): Int {
    val p = parse(input)
    return judge(Pair(p.first.filter { it % 4 == 0L },
            p.second.filter { it % 8 == 0L }), 5_000_000)
}
