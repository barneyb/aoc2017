package event2017

import java.io.File

/**
 *
 *
 * @author barneyb
 */

fun main(args: Array<String>) {
    val input = File("input/2017/day16.txt").readText()

    banner("part 1")
    val assertOne = check(::dance)
    assertOne(input, "ebjpfdgmihonackl")
    println("answer: " + dance(input))

    val dancers = "abcdefghijklmnop".toCharArray()
    val targetRounds = 1000000000
    val rounds = 100000
    val start = System.currentTimeMillis()
    println("$rounds rounds: " + aOneTwoThree(dancers, input, rounds))
    val elapsed = System.currentTimeMillis() - start
    println((1.0 * elapsed / 1000).toString() + " sec for " + rounds + " rounds; expecting " + 1.0 * elapsed / rounds * targetRounds / 1000.0 / 60.0 / 60.0 + " hrs for all " + targetRounds)
    // estimate is 134 hours

//    banner("part 2")
//    val assertTwo = check(::partTwo)
//    assertTwo(exampleInput, 309)
////    assertTwo(input, 290)
//    println("answer: " + partTwo(input))
}

typealias Move = (CharArray) -> CharArray

private fun parseMove(move: String): Move {
    when (move[0]) {
        's' -> {
            val n = move.drop(1).toInt()
            return { dancers ->
                val next = CharArray(16)
                // copy tail -> head
                System.arraycopy(dancers, 16 - n, next, 0, n)
                // copy head -> tail
                System.arraycopy(dancers, 0, next, n, 16 - n)
                next
            }
        }
        'x' -> {
            val (i, j) = move.drop(1)
                    .split('/')
                    .map { it.toInt() }
            return { dancers ->
                val c = dancers[i]
                dancers[i] = dancers[j]
                dancers[j] = c
                dancers
            }
        }
        'p' -> {
            val (a, b) = move.drop(1)
                    .split('/')
                    .map { it[0] }
            return { dancers ->
                val i = dancers.indexOf(a)
                val j = dancers.indexOf(b)
                val c = dancers[i]
                dancers[i] = dancers[j]
                dancers[j] = c
                dancers
            }
        }
        else -> {
            println("unknown move: '$move'")
            return { ds -> ds }
        }
    }
}

private fun swapChar(ds: String, a: Char, b: Char) =
        ds.replace(a, '.')
                .replace(b, a)
                .replace('.', b)

private fun parse(input: String): List<Move> =
        input.trim()
                .split(',')
                .map(::parseMove)

private fun dance(input: String) =
        aOneTwoThree("abcdefghijklmnop".toCharArray(), input)

private fun aOneTwoThree(dancers: CharArray, input: String, rounds: Int = 1): String {
    val moves = parse(input)
    return IntRange(1, rounds).fold(dancers, { ds, _ ->
        moves.fold(ds, { ds, m ->
            m(ds)
        })
    }).joinToString("")
}

//private fun partTwo(input: String) =
//        input.length
