package event2017.day13

import event2017.banner
import event2017.check
import java.io.File

fun main(args: Array<String>) {
    val input = File("input/2017/day13.txt").readText().trim()
    val exampleInput = "0: 3\n" +
            "1: 2\n" +
            "4: 4\n" +
            "6: 4"

    banner("part 1")
    val partOne = check(::severity)
    partOne(exampleInput, 24)
    partOne(input, 1728)
    println("answer: " + severity(input))

    banner("part 2")
    val partTwo = check(::delay)
    partTwo(exampleInput, 10)
    partTwo(input, 3946838)
    println("answer: " + delay(input))

}

fun severity(input: String) =
        parse(input)
                .filter { it.caught() }
                .sumBy { it.severity }

data class Layer(
        val depth: Int,
        val range: Int
) {
    val period
        get() = 2 * (range - 2) + 2

    val severity
        get() = depth * range
}

fun Layer.caught(delay: Int = 0) = (delay + depth) % period == 0

private fun parse(input: String): List<Layer> {
    return input.split("\n")
            .map {
                it.split(':')
                        .map { it.trim().toInt() }
            }
            .map { (d, r) ->
                Layer(d, r)
            }
}

fun delay(input: String) =
        generateSequence(Pair(0, parse(input)), { p ->
            Pair(p.first + 1, p.second)
        })
                .dropWhile { p ->
                    p.second.any {
                        it.caught(p.first)
                    }
                }
                .first()
                .first
