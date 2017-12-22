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
    val partOne = check(zerosGroupSize)
    partOne(exampleInput, 6)
    partOne(input, 306)
    println("answer: " + zerosGroupSize(input))

    banner("bidirectionality")
    check(groupSize(5))(exampleInput, 6)

    banner("part 2")
    val partTwo = check(::groupCount)
    partTwo(exampleInput, 2)
    partTwo(input, 200)
    println("answer: " + groupCount(input))
}

private val groupSize = { id: Int ->
    { input: String ->
        parse(input).groupFrom(id).size
    }
}

private val zerosGroupSize = groupSize(0)

private fun groupCount(input: String) =
        generateSequence(parse(input), { map ->
            map - map.groupFrom(map.keys.first())
        }).takeWhile {
            it.isNotEmpty()
        }.count()

private typealias NodeMap = Map<Int, List<Int>>

private data class Generation(
        val seed: Collection<Int>,
        val visited: Set<Int> = setOf<Int>()
) {
    constructor(seed: Int) : this(listOf(seed))
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
