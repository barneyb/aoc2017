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

private fun gen(input:String):Sequence<IntArray> {
    return generateSequence(parse(input).toIntArray(), { prev ->
        val banks = prev.clone()
        val l = banks.size
        val i = banks.indexOf(banks.max()!!)
        val blocks = banks[i]
        banks[i] = 0
        (i + 1).rangeTo(i + blocks)
                .forEach {
                    banks[it % l] += 1
                }
        banks
    })
}

private fun cyclesToReentrance(input:String):Int {
    val uniquer = mutableSetOf<String>()
    return gen(input)
            .map { it.joinToString(",") }
            .takeWhile { banks ->
                val result = ! uniquer.contains(banks)
                uniquer.add(banks)
                result
            }
            .count()
}

private fun cyclesInLoop(input:String):Int {
    return input.length
}

private fun parse(input: String) =
        input.split('\t').map({ it.toInt() })

