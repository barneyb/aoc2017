package event2017

import java.io.File

/**
 *
 *
 * @author barneyb
 */

fun main(args: Array<String>) {
    val input = File("input/2017/day16.txt").readText()
    val dancers = "abcdefghijklmnop".toCharArray()

//    banner("part 1")
//    val assertOne = check(::dance)
//    assertOne(input, "ebjpfdgmihonackl")
//    println("answer: " + dance(input))

    aOneTwoThree(dancers, input, 20)

//    val targetRounds = 1000000000
//    val rounds = 100000
//    val start = System.currentTimeMillis()
//    println("$rounds rounds: " + aOneTwoThree(dancers, input, rounds))
//    val elapsed = System.currentTimeMillis() - start
//    println((1.0 * elapsed / 1000).toString() + " sec for " + rounds + " rounds; expecting " + 1.0 * elapsed / rounds * targetRounds / 1000.0 / 60.0 / 60.0 + " hrs for all " + targetRounds)
    // 134 hours initially
    // 82 hours with all the spins collapsed

//    banner("part 2")
//    val assertTwo = check(::partTwo)
//    assertTwo(exampleInput, 309)
////    assertTwo(input, 290)
//    println("answer: " + partTwo(input))
}

typealias Move = (CharArray) -> CharArray

private fun parseMove(cmd: String, offset: Int): Move {
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
                    .map {
                        val n = it.toInt() - offset
                        if (n < 0) n + 16 else n
                    }
            return { dancers ->
                val c = dancers[i]
                dancers[i] = dancers[j]
                dancers[j] = c
                dancers
            }
        }
        'p' -> {
            val (a, b) = cmd.drop(1)
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
            println("unknown move: '$cmd'")
            return { ds -> ds }
        }
    }
}

private fun parse(input: String): List<Move> {
    val (moves, offset) = input.trim()
            .split(',')
            .fold(Pair(mutableListOf<Move>(), 0), { (ms, o), cmd ->
                if (cmd[0] == 's') {
                    val n = cmd.drop(1).toInt()
                    Pair(ms, (o + n) % 16)
                } else {
                    ms.add(parseMove(cmd, o))
                    Pair(ms, o)
                }
            })
    if (offset != 0) {
        moves.add(parseMove("s" + offset, offset))
    }
    return moves
}

private fun dance(input: String) =
        aOneTwoThree("abcdefghijklmnop".toCharArray(), input)

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
).map { it.toCharArray() }

private fun aOneTwoThree(initialDancers: CharArray, input: String, rounds: Int = 1): String {
    val moves = parse(input)
    return IntRange(1, rounds).fold(initialDancers, { roundDancers, i ->
        val r =
                moves.fold(roundDancers, { moveDancers, m ->
                    m(moveDancers)
                })
        if (r.contentEquals(expecteds[i - 1])) {
            println("round ${i.toString().padStart(2, ' ')} passed: ${String(r)}")
        } else {
            println("round ${i.toString().padStart(2, ' ')} failed: ${String(r)} (expecting ${String(expecteds[i - 1])})")
        }
        r
    }).joinToString("")
}

//private fun partTwo(input: String) =
//        input.length
