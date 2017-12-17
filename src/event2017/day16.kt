package event2017

import java.io.File

/**
 *
 *
 * @author barneyb
 */

fun main(args: Array<String>) {
    val input = File("input/2017/day16.txt").readText()
    val moves = parse(input)
    val dancers = "abcdefghijklmnop"

    banner("part 1")
    val assertOne = check(::dance)
    assertOne(input, "ebjpfdgmihonackl")
    println("answer: " + dance(input))

    banner("chaining")
    val ninteen = aOneTwoThree(dancers, moves, expecteds.size - 1)
    val twenty = aOneTwoThree(ninteen, moves)
    val scan = aOneTwoThreeScan(dancers, moves, expecteds.size)
    if (twenty != scan.last()) {
        throw RuntimeException("final/scan don't agree")
    }

    for ((i, r) in scan.drop(1).withIndex()) {
        val iString = (i + 1).toString().padStart(2, ' ')
        if (r == expecteds[i]) {
            println("round $iString passed: $r")
        } else {
            println("round $iString FAILED: $r (expecting ${expecteds[i]})")
        }
    }

    banner("looped")
    val scan2 = aOneTwoThreeScan(dancers, moves, 1_000_000_000 % 30)
    for ((i, r) in scan2.withIndex()) {
        println("${i.toString().padStart(2, ' ')}: $r")
    }

//    banner("profiling")
//    val targetRounds = 1_000_000_000
//    val rounds = 100_000
//    val start = System.currentTimeMillis()
//    println("$rounds rounds: " + aOneTwoThree(dancers, input, rounds))
//    val elapsed = System.currentTimeMillis() - start
//    println((1.0 * elapsed / 1000).toString() + " sec for " + rounds + " rounds; expecting " + 1.0 * elapsed / rounds * targetRounds / 1000.0 / 60.0 / 60.0 + " hrs for all " + targetRounds)
    // all with 100,000 rounds
    // 134 hours initially
    // 82 hours with all the spins collapsed
    // 1/4 hour with two moves
    // 71 hours with roundFactory
    // 79 hours to make all Moves pure

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
        aOneTwoThree("abcdefghijklmnop", parse(input))

private val expecteds = listOf(
        "ebjpfdgmihonackl",
        "dblfiegmjknpohac",
        "anbfdigmcokeplhj",
        "eocfhpgainmdkljb",
        "fbpiengdjmoaklch",
        "dnjceogmlphfkbia",
        "pkjfebgacinhmold",
        "aojifkgehcnbdlpm",
        "apljchgdifnokbme",
        "abocefghijklmndp",
        "ebjpfcgmihdnaokl",
        "cblfiegmjknpdhao",
        "anbfcigmodkeplhj",
        "edofhpgainmckljb",
        "fbpiengcjmdakloh",
        "cnjoedgmlphfkbia",
        "pkjfebgaoinhmdlc",
        "adjifkgehonbclpm",
        "apljohgcifndkbme",
        "abdoefghijklmncp"
)

private fun danceSequence(seed: CharArray, moves: List<Move>) =
        generateSequence(seed, { roundDancers: CharArray ->
            moves.fold(roundDancers, { moveDancers, m ->
                m(moveDancers)
            })
        })

private fun aOneTwoThree(initialDancers: String, moves: List<Move>, rounds: Int = 1) =
        String(danceSequence(initialDancers.toCharArray(), moves)
                .drop(rounds)
                .first())

private fun aOneTwoThreeScan(initialDancers: String, moves: List<Move>, rounds: Int = 1) =
        danceSequence(initialDancers.toCharArray(), moves)
                .take(rounds + 1) // need initial state too!
                .map { String(it) }

private fun wholeDance(input: String): String {
    val initialDancers = "abcdefghijklmnop"
    val seed = initialDancers.toCharArray()
    val moves = parse(input)
    val seq = danceSequence(seed, moves)
    val loopSize = 1 + seq.drop(1).takeWhile { !it.contentEquals(seed) }.count()
    return aOneTwoThree(initialDancers, moves, 1_000_000_000 % loopSize)
}
