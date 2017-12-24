package event2017

import java.io.File

/**
 *
 *
 * @author barneyb
 */

fun main(args: Array<String>) {
    val input = File("input/2017/day24.txt").readText()
    val exampleInput = "0/2\n" +
            "2/2\n" +
            "2/3\n" +
            "3/4\n" +
            "3/5\n" +
            "0/1\n" +
            "10/1\n" +
            "9/10"

    banner("part 1")
    val assertOne = check(::partOne)
    assertOne(exampleInput, 31)
    assertOne(input, 1656)
//    println("answer: " + partOne(input))

//    banner("part 2")
//    val assertTwo = check(::partTwo)
//    assertTwo(exampleInput, 309)
////    assertTwo(input, 290)
//    println("answer: " + partTwo(input))
}

private data class Component(
        val a: Int,
        val b: Int
) {
    val strength
        get() = a + b

    fun canConnect(tail: Int) =
            tail == a || tail == b
}

private data class Bridge(
        val components: List<Component> = listOf(),
        val tail: Int = 0
) {
    val strength
        get() = components
                .map { it.strength }
                .sum()

    operator fun plus(c: Component) =
            Bridge(components + c, if (c.a == tail) c.b else c.a)
}

private data class GrowState(
        val maxStrength: Int = 0,
        val tips: Set<Bridge> = setOf(Bridge())
)

private fun partOne(input: String): Int {
    val parts = parse(input)
    return generateSequence(GrowState(), { gs ->
        var ms = gs.maxStrength
        val nextGen = gs.tips.fold(mutableSetOf<Bridge>(), { tips, b ->
            val candidates = parts.filter {
                it.canConnect(b.tail) && it !in b.components
            }
            if (candidates.isEmpty()) {
                ms = Math.max(ms, b.strength)
            }
            else
                tips.addAll(candidates.map {
                    b + it
                })
            tips
        })
        if (gs.tips == nextGen)
            null // nothing grew
        else
            GrowState(ms, nextGen)
    })
            .last()
            .maxStrength
}

private fun parse(input: String) =
        input.trim()
                .split("\n")
                .map {
                    val (a, b) = it
                            .split("/")
                            .map { it.toInt() }
                    Component(a, b)
                }

private fun partTwo(input: String) =
        input.length
