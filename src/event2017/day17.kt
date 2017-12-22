package event2017

import java.io.File

/**
 *
 *
 * @author barneyb
 */

fun main(args: Array<String>) {
    val input = File("input/2017/day17.txt").readText().trim().toInt()
    val exampleInput = 3

    banner("part 1")
    val assertOne = check(::partOne)
    assertOne(exampleInput, 638)
    assertOne(input, 1282)
    println("answer: " + partOne(input))

    banner("part 1 (sequence)")
    val assertOneSeq = check(::partOneSeq)
    assertOneSeq(exampleInput, 638)
    assertOneSeq(input, 1282)
    println("answer: " + partOneSeq(input))

    banner("math sequence!")
    val bad = seq2a(exampleInput)
            .zip(seq2b(exampleInput))
            .take(2017)
            .find { (a, b) ->
                a.second != b.second
            }
    if (bad != null) {
        val (a, b) = bad
        if (a.first != b.first) {
            println("indexes don't match! expected $a got $b")
        }
        println("FAILURE at n=${a.first} expected '${a.second}', but got '${b.second}")
    } else {
        println("PASS!")
    }

    banner("part 2")
    val assertTwo = check(::partTwo)
    assertTwo(input, 27650600)
//    println("answer: " + partTwo(input))
}

private fun partOne(input: Int): Int {
    val (buffer, pointer) = (1..2017)
            .fold(Pair(mutableListOf(0), 0), { (b, p), i ->
                val next = (p + input) % b.size + 1
                if (next == b.size)
                    b.add(i)
                else
                    b.add(next % b.size, i)
                Pair(b, next)
            })
    return buffer[pointer + 1]
}

private fun partOneSeq(input: Int): Int {
    val (buffer, pointer, _) = seq1(input)
            .drop(2017)
            .first()
    return buffer[pointer + 1]
}

private fun seq1(input: Int) =
        generateSequence(Triple(listOf(0), 0, 1), { (b, p, i) ->
            val np = (p + input) % b.size + 1
            val nb = if (np == b.size)
                b + i
            else
                b.subList(0, np % b.size) +
                        i +
                        b.subList(np % b.size, b.size)
            Triple(nb, np, i + 1)
        })

private fun seq2a(input: Int) =
        seq1(input)
                .drop(1)
                .map { (b, _, i) ->
                    Pair(i, b[1])
                }

private fun seq2b(input: Int) =
        generateSequence(Triple(1, 1, 1), { (i, p, value) ->
            val np = (p + input) % i + 1
            Triple(i + 1, np, if (np == 1) i else value)
        })
                .drop(1)
                .map {
                    Pair(it.first, it.third)
                }

private fun partTwo(input: Int) =
        seq2b(input)
                .dropWhile { it.first < 50_000_000 }
                .first()
                .second
