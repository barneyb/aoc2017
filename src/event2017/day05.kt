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
    val partOne = check(::steps1)
    partOne("0\n" +
            "3\n" +
            "0\n" +
            "1\n" +
            "-3", 5)
    partOne(input, 375042)
    println("answer: " + steps1(input))

    banner("part two")
    val partTwo = check(::steps2)
    partTwo("0\n" +
            "3\n" +
            "0\n" +
            "1\n" +
            "-3", 10)
    partTwo(input, 28707598)
    println("answer: " + steps2(input))
}

private fun steps1(input:String):Int {
    return stepsInternal(input) { it + 1 }
}
private fun steps2(input:String):Int {
    return stepsInternal(input) { if (it >= 3) it - 1 else it + 1 }
}

private fun stepsInternal(input:String, munge:(Int) -> Int):Int {
    val instructions = input.split('\n')
            .map {
                it.trim().toInt()
            }.toIntArray()
    var pointer = 0
    var steps = 0
    while (pointer >= 0 && pointer < instructions.size) {
        val jump = instructions[pointer]
        instructions[pointer] = munge(instructions[pointer])
        pointer += jump
        steps += 1
    }
    return steps
}
