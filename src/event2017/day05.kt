package event2017

import java.io.File

/**
 *
 *
 * @author barneyb
 */
fun main(args: Array<String>) {
    val input = File("input/2017/day05.txt").readText().trim()
    val exampleInput = "0\n" +
            "3\n" +
            "0\n" +
            "1\n" +
            "-3"

    banner("part one")
    val partOne = check(::steps1)
    partOne(exampleInput, 5)
    partOne(input, 375042)
    println("answer: " + steps1(input))

    banner("part one (pure)")
    val partOnePure = check(::steps1pure)
    partOnePure(exampleInput, 5)
//    partOnePure(input, 375042)
//    println("answer: " + steps1pure(input))

    banner("part two")
    val partTwo = check(::steps2)
    partTwo(exampleInput, 10)
    partTwo(input, 28707598)
    println("answer: " + steps2(input))

    banner("part two (pure)")
    val partTwoPure = check(::steps2pure)
    partTwoPure(exampleInput, 10)
//    partTwoPure(input, 28707598)
//    println("answer: " + steps2pure(input))
}

private data class ProcState(
        val ins: IntArray,
        val increment: (Int) -> Int,
        val pointer: Int = 0
)

private fun IntArray.munge(index: Int, delta: Int):IntArray {
    val result = clone()
    result[index] += delta
    return result
}

private fun ProcState.incomplete() =
        pointer >= 0 && pointer < ins.size

private fun ProcState.tick():ProcState {
    val offset = ins[pointer];
    return ProcState(ins.munge(pointer, increment(offset)), increment, pointer + offset)
}

private fun steps1pure(input:String) =
        stepspureInternal(input) { _ -> 1 }

private fun steps2pure(input:String) =
        stepspureInternal(input) { offset -> if (offset >= 3) -1 else 1 }

private fun stepspureInternal(input: String, increment:(Int) -> Int) =
        generateSequence(0, { it + 1})
                .zip(generateSequence(ProcState(parse(input), increment), { state ->
                    state.tick()
                }))
                .dropWhile { (_, state) ->
                    state.incomplete()
                }.first().first

private fun steps1(input:String) =
        stepsInternal(input) { it + 1 }

private fun steps2(input:String) =
        stepsInternal(input) { if (it >= 3) it - 1 else it + 1 }

private fun stepsInternal(input:String, munge:(Int) -> Int):Int {
    val instructions = parse(input)
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

private fun parse(input: String): IntArray {
    return input.split('\n')
            .map {
                it.trim().toInt()
            }.toIntArray()
}
