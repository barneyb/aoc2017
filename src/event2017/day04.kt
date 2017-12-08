package event2017

import java.io.File

/**
 *
 *
 * @author barneyb
 */
fun main(args: Array<String>) {
    val input = File("input/2017/day04.txt").readText().trim()

    banner("part 1")
    val partOne = check(::countValid)
    partOne("aa bb cc dd ee", 1)
    partOne("aa bb cc dd aa", 0)
    partOne("aa bb cc dd aaa", 1)
    partOne(input, 455)
    println("answer: " + countValid(input))

    banner("part 2")
    val partTwo = check(::countValid2)
    partTwo("abcde fghij", 1)
    partTwo("abcde xyz ecdab", 0)
    partTwo("a ab abc abd abf abj", 1)
    partTwo("iiii oiii ooii oooi oooo", 1)
    partTwo("oiii ioii iioi iiio", 0)
    partTwo(input, 186)
    println("answer: " + countValid2(input))
}

private fun countValid(input:String):Int {
    return process(input) { it }
}

private fun process(input: String, munge:(List<String>) -> List<String>):Int {
    return input.split('\n')
            .map {
                it.split(' ')
            }
            .map(munge)
            .filter {
                it.size == it.distinct().size
            }
            .size
}

fun countValid2(input:String):Int {
    return process(input) {
                it.map {
                    it.toCharArray().sorted().joinToString("")
                }
            }
}
