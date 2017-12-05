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
}

fun countValid(input:String):Int {
    return input.split('\n')
            .map {
                it.split(' ')
            }
            .filter {
                it.size == it.distinct().size
            }
            .size
}
