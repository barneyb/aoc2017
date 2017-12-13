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

}

fun severity(input: String) =
        parse(input)
                .filter { it.depth % it.period == 0 }
                .sumBy { it.severity }

data class Layer(
        val depth: Int,
        val range: Int,
        val period: Int,
        val severity: Int
) {
    constructor(depth: Int, range: Int) : this(
            depth,
            range,
            2 * (range - 2) + 2,
            depth * range
    )
}

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
