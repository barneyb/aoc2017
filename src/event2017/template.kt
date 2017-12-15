package event2017

import java.io.File

/**
 *
 *
 * @author barneyb
 */

fun main(args: Array<String>) {
    val input = File("input/2017/day.txt").readText()
    val exampleInput = ""

    banner("part 1")
    val assertOne = check(::partOne)
    assertOne(exampleInput, 588)
//    assertOne(input, 619)
    println("answer: " + partOne(input))

//    banner("part 2")
//    val assertTwo = check(::partTwo)
//    assertTwo(exampleInput, 309)
////    assertTwo(input, 290)
//    println("answer: " + partTwo(input))
}

private fun partOne(input: String) =
        input.length

//private fun partTwo(input: String) =
//        input.length
