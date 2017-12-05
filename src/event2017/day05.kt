package event2017

import java.io.File

/**
 *
 *
 * @author barneyb
 */
fun main(args: Array<String>) {
    val input = File("input/2017/day05.txt").readText().trim()

    banner("part one")
    val partOne = check(::steps)
    partOne("0\n" +
            "3\n" +
            "0\n" +
            "1\n" +
            "-3", 5)
    partOne(input, 375042)
    println("answer: " + steps(input))
}

private fun steps(input:String):Int {
    val instructions = input.split('\n')
            .map {
                it.trim().toInt()
            }.toIntArray()
    var pointer = 0
    var steps = 0
    while (pointer >= 0 && pointer < instructions.size) {
        val jump = instructions[pointer]
        instructions[pointer] += 1
        pointer += jump
        steps += 1
    }
    return steps;
}
