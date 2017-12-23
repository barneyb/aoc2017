package event2017

import event2017.duet.Computer
import event2017.duet.MultiplyIns
import event2017.duet.loadDuet
import java.io.File

/**
 *
 *
 * @author barneyb
 */

fun main(args: Array<String>) {
    val input = File("input/2017/day23.txt").readText()

    banner("part 1")
    val assertOne = check(::partOne)
    assertOne(input, 3025)
    println("answer: " + partOne(input))

    banner("part 2")
    val assertTwo = check(::partTwo)
    assertTwo(input, 290)
    println("answer: " + partTwo(input))
}

private fun partOne(input: String) =
        generateSequence(Pair(0, Computer(instructions = loadDuet(input))), { (mulCount, comp) ->
            if (comp.terminated)
                null
            else
                Pair(mulCount + (if (comp.curr() is MultiplyIns) 1 else 0), comp.tick())
        })
                .last()
                .first

private fun partTwo(input: String) =
        input.length

