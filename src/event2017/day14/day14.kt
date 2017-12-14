package event2017.day14

import event2017.banner
import event2017.check
import event2017.day10.knotHash

fun main(args: Array<String>) {
    val input = "jxqlasbh"
    val exampleInput = "flqrgnkx"

    banner("part 1")
    val partOne = check(::blocksUsed)
    partOne(exampleInput, 8108)
    partOne(input, 8140)
    println("answer: " + blocksUsed(input))

}

fun blocksUsed(input: String) =
        IntRange(0, 127)
                .map {
                    knotHash(input + "-" + it)
                            .map { c ->
                                c.toString()
                                        .toInt(16)
                                        .toString(2)
                                        .count {
                                            it == '1'
                                        }
                            }
                            .sum()
                }.sum()
