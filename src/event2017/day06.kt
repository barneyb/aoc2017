package event2017

import java.io.File

/**
 *
 *
 * @author barneyb
 */
fun main(args: Array<String>) {
    val input = File("input/2017/day06.txt").readText().trim()

    banner("part one")
    val partOne = check(::cyclesToReentrance)
    partOne("0\t2\t7\t0", 5)
    partOne(input, 6681)
    println("answer: " + cyclesToReentrance(input))

    banner("part two")
    val partTwo = check(::cyclesInLoop )
    partTwo("0\t2\t7\t0", 4)
//    partTwo(input, 6681)
    println("answer: " + cyclesInLoop(input))
}

fun cyclesToReentrance(input:String):Int {
    val banks = parse(input)
    val l = banks.size
    val uniquer = mutableSetOf<String>()
    var counter = 0
    do {
        uniquer.add(banks.toString())
        var i = banks.indexOf(banks.max()!!)
        var blocks = banks[i]
        banks[i] = 0
        while (blocks > 0) {
            i += 1
            banks[i % l] += 1
            blocks -= 1
        }
        counter += 1
    } while (! uniquer.contains(banks.toString()))
    return counter
}

fun cyclesInLoop(input:String):Int {
    return input.length
}

private fun parse(input: String) =
        input.split('\t').map({ it.toInt() }).toMutableList()

