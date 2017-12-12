package event2017.day12

import event2017.banner
import event2017.check
import java.io.File

fun main(args: Array<String>) {
    val input = File("input/2017/day12.txt").readText().trim()
    val exampleInput = "0 <-> 2\n" +
            "1 <-> 1\n" +
            "2 <-> 0, 3, 4\n" +
            "3 <-> 2, 4\n" +
            "4 <-> 2, 3, 6\n" +
            "5 <-> 6\n" +
            "6 <-> 4, 5"

    banner("part 1")
    val partOne = check(::zerosGroupSize)
    partOne(exampleInput, 6)
    partOne(input, 306)
    println("answer: " + zerosGroupSize(input))
}

fun zerosGroupSize(input: String) =
        parse(input).groupFrom(0).size

typealias NodeMap = Map<Int, List<Int>>

data class Generation(
        val seed: Collection<Int>,
        val visited: Set<Int> = setOf<Int>()
) {
    constructor(seed: Int) : this(listOf(seed)) {}
}

private fun NodeMap.groupFrom(start: Int) =
        generateSequence(Generation(start), { gen ->
            val nextVisited = gen.visited + gen.seed
            val nextSeed = gen.seed.flatMap { id ->
                get(id)!!
            }.filter {
                !nextVisited.contains(it)
            }
            Generation(nextSeed, nextVisited)
        })
                .dropWhile { it.seed.isNotEmpty() }
                .first()
                .visited

private fun parse(input: String) =
        input.split("\n")
                .fold(mapOf<Int, List<Int>>()) { map, it ->
                    val parts = it.split("<->").map {
                        it.trim()
                    }
                    val id = parts[0].toInt()
                    val kids = parts[1].split(",").map { it.trim().toInt() }
                    map + Pair(id, kids)
                }
