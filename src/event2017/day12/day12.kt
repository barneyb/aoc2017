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

fun zerosGroupSize(input: String): Int {
    val nodeMap = input.split("\n")
            .fold(mapOf<Int, List<Int>>()) { map, it ->
                val parts = it.split("<->").map {
                    it.trim()
                }
                val id = parts[0].toInt()
                val kids = parts[1].split(",").map { it.trim().toInt() }
                map + Pair(id, kids)
            }
    val visited = mutableSetOf<Int>()
    var seed = listOf(0)
    while (true) {
        visited.addAll(seed)
        val nextSeed = seed.flatMap { id ->
            nodeMap[id]!!
        }.filter {
            ! visited.contains(it)
        }
        if (nextSeed.isEmpty())
            break
        seed = nextSeed
    }
    return visited.size
}
