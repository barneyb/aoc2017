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

//    banner("profiling")
//    val targetRounds = 1_000_000_000
//    val rounds = 100_000
//    val start = System.currentTimeMillis()
//    println("$rounds rounds: " + String(danceSequence("abcdefghijklmnop".toCharArray(), parse(input)).drop(rounds).first()))
//    val elapsed = System.currentTimeMillis() - start
//    println((1.0 * elapsed / 1000).toString() + " sec for " + rounds + " rounds; expecting " + 1.0 * elapsed / rounds * targetRounds / 1000.0 / 60.0 / 60.0 + " hrs for all " + targetRounds)
    // all with 100,000 rounds
    // 134 hours initially
    // 82 hours with all the spins collapsed
    // 1/4 hour with two moves
    // 71 hours with roundFactory
    // 79 hours to make all Moves pure
    // 80 hours with sequence generator

    banner("part 2")
    val assertTwo = check(::wholeDance)
    assertTwo(input, "abocefghijklmndp")
    println("answer: " + wholeDance(input))
}

typealias Move = (CharArray) -> CharArray

private fun parseMove(cmd: String): Move {
    when (cmd[0]) {
        's' -> {
            val n = cmd.drop(1).toInt()
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
            val (i, j) = cmd.drop(1)
                    .split('/')
                    .map { it.toInt() }
            return { dancers ->
                val next = dancers.copyOf()
                next[i] = dancers[j]
                next[j] = dancers[i]
                next
            }
        }
        'p' -> {
            val (a, b) = cmd.drop(1)
                    .split('/')
                    .map { it[0] }
            return { dancers ->
                val i = dancers.indexOf(a)
                val j = dancers.indexOf(b)
                val next = dancers.copyOf()
                next[i] = dancers[j]
                next[j] = dancers[i]
                next
            }
        }
        else -> {
            println("unknown move: '$cmd'")
            return { ds -> ds }
        }
    }
}

private fun parse(input: String): List<Move> {
    // this gobbledegook spreads the spins across subsequent exchanges
    // and aggregates it all into a single spin at the end.
    val (cmds, offset) = input.trim()
            .split(',')
            .fold(Pair(mutableListOf<String>(), 0), { (cmds, o), cmd ->
                when (cmd[0]) {
                    's' -> {
                        val n = cmd.drop(1).toInt()
                        Pair(cmds, (o + n) % 16)
                    }
                    'x' -> {
                        val (i, j) = cmd.drop(1)
                                .split('/')
                                .map { it.toInt() }
                                .map { it - o }
                                .map { if (it < 0) it + 16 else it }
                        cmds.add("x$i/$j")
                        Pair(cmds, o)
                    }
                    else -> {
                        cmds.add(cmd)
                        Pair(cmds, o)
                    }
                }
            })
    if (offset != 0) {
        cmds.add("s" + offset)
    }
    return cmds.map(::parseMove)
}

private fun dance(input: String) =
        String(danceSequence("abcdefghijklmnop".toCharArray(), parse(input))
                .first())

private fun danceSequence(initialDancers: CharArray, moves: List<Move>) =
        generateSequence(initialDancers, { roundDancers ->
            moves.fold(roundDancers, { moveDancers, move ->
                move(moveDancers)
            })
        })
                .drop(1) // seed

private fun wholeDance(input: String): String {
    val seed = "abcdefghijklmnop".toCharArray()
    val loop = listOf(seed) + danceSequence(seed, parse(input))
            .takeWhile { !it.contentEquals(seed) }
            .toList()
    return String(loop[1_000_000_000 % loop.size])
}
