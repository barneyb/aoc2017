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

private fun dance(dancers: String) =
        { input: String ->
            input.trim()
                    .split(',')
                    .fold(dancers, { ds, m ->
                        when (m[0]) {
                            's' -> {
                                val n = ds.length -  m.drop(1).toInt()
                                ds.drop(n) + ds.take(n)
                            }
                            'x' -> {
                                val (a, b) = m.drop(1)
                                        .split('/')
                                        .map { it.toInt() }
                                        .map { ds[it] }
                                swapChar(ds, a, b)
                            }
                            'p' -> {
                                val (a, b) = m.drop(1)
                                        .split('/')
                                        .map { it[0] }
                                swapChar(ds, a, b)
                            }
                            else -> {
                                println("unknown move: '$m'")
                                ds
                            }
                        }
                    })
        }

private fun swapChar(ds: String, a: Char, b: Char): String {
    return ds.replace(a, '.')
            .replace(b, a)
            .replace('.', b)
}

//private fun partTwo(input: String) =
//        input.length
