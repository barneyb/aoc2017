package event2017

import java.io.File

/**
 *
 *
 * @author barneyb
 */

fun main(args: Array<String>) {
    val input = File("input/2017/day16.txt").readText()
    val exampleInput = "s1,x3/4,pe/b"

    banner("part 1")
    val partOneExample = dance("abcde")
    val assertOneExample = check(partOneExample)
    val partOne = dance("abcdefghijklmnop")
    val assertOne = check(partOne)
    assertOneExample(exampleInput, "baedc")
    assertOne(input, "ebjpfdgmihonackl")
    println("answer: " + partOne(input))

//    banner("part 2")
//    val assertTwo = check(::partTwo)
//    assertTwo(exampleInput, 309)
////    assertTwo(input, 290)
//    println("answer: " + partTwo(input))
}

typealias Move = (String) -> String

private fun parseMove(move: String): Move {
    if (move[0] == 's') {
        val s = move.drop(1).toInt()
        return { ds ->
            val n = ds.length - s
            ds.drop(n) + ds.take(n)
        }
    } else if (move[0] == 'x') {
        val (a, b) = move.drop(1)
                .split('/')
                .map { it.toInt() }
        return { ds ->
            swapChar(ds, ds[a], ds[b])
        }
    } else if (move[0] == 'p') {
        val (a, b) = move.drop(1)
                .split('/')
                .map { it[0] }
        return { ds ->
            swapChar(ds, a, b)
        }
    } else {
        println("unknown move: '$move'")
        return { ds -> ds }
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

private fun dance(dancers: String) =
        { input: String ->
            parse(input)
                    .fold(dancers, { ds, m ->
                        m(ds)
                    })
        }

//private fun partTwo(input: String) =
//        input.length
