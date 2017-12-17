package event2017

import java.io.File

/**
 *
 *
 * @author barneyb
 */

fun main(args: Array<String>) {
    val input = File("input/2017/day16.txt").readText()
    val dancers = "abcdefghijklmnop"

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
    val (cmds, offset) = input.trim()
            .split(',')
            .fold(Pair(mutableListOf<String>(), 0), { (cmds, o), cmd ->
                if (cmd[0] == 's') {
                    val n = cmd.drop(1).toInt()
                    Pair(cmds, (o + n) % 16)
                } else if (cmd[0] == 'x') {
                    val (i, j) = cmd.drop(1)
                            .split('/')
                            .map { it.toInt() }
                            .map { it - o }
                            .map { if (it < 0) it + 16 else it }
                    cmds.add("x$i/$j")
                    Pair(cmds, o)
                } else {
                    cmds.add(cmd)
                    Pair(cmds, o)
                }
            })
    if (offset != 0) {
        cmds.add("s" + offset)
    }
    return cmds.map(::parseMove)
}

private fun dance(input: String) =
        aOneTwoThree("abcdefghijklmnop", input)

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

private fun aOneTwoThree(initialDancers: String, input: String, rounds: Int = 1): String {
    val moves = parse(input)
    return String(IntRange(1, rounds).fold(initialDancers.toCharArray(), { roundDancers, i ->
        val r =
                moves.fold(roundDancers, { moveDancers, m ->
                    m(moveDancers)
                })
        val iString = i.toString().padStart(2, ' ')
        val rString = String(r)
        if (rString == expecteds[i - 1]) {
            println("round $iString passed: $rString")
        } else {
            println("round $iString failed: $rString (expecting ${expecteds[i - 1]})")
        }
        r
    }))
}
