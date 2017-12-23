package event2017

import event2017.duet.Computer
import event2017.duet.MultiplyIns
import event2017.duet.loadDuet
import java.io.File

/**
 *
 *
 * @author barneyb
 */

fun main(args: Array<String>) {
    val input = File("input/2017/day23.txt").readText()

    banner("part 1")
    val assertOne = check(::partOne)
    assertOne(input, 3025)
    println("answer: " + partOne(input))

    banner("part 2")
//    val assertTwo = check(::partTwo)
//    assertTwo(input, 290)
    println("answer (debug): " + runOnASIC(true))
    println("answer: " + runOnASIC())
}

private fun partOne(input: String) =
        generateSequence(Pair(0, Computer(instructions = loadDuet(input))), { (mulCount, comp) ->
            if (comp.terminated)
                null
            else
                Pair(mulCount + (if (comp.curr() is MultiplyIns) 1 else 0), comp.tick())
        })
                .last()
                .first

private fun runOnASIC(debug: Boolean = false): Int {
    var b = 57
    var c = b
    var d: Int
    var e: Int
    var f: Int
    var g: Int
    var h = 0

    if (!debug) {
        b *= 100
        b += 100000
        c = b
        c += 17000
    }
    do {
        f = 1
        d = 2
        do {
            e = 2
            do {
                g = d
                g *= e
                g -= b
                if (g == 0) {
                    f = 0
                }
                e += 1
                g = e
                g -= b
            } while (g != 0)
            d += 1
            g = d
            g -= b
        } while (g != 0)
        if (f == 0) {
            h += 1
        }
        g = b
        g -= c
        println("b=$b, c=$c, d=$d, e=$e, f=$f, g=$g, h=$h")
        if (g == 0) {
            return h
        }
        b += 17
    } while (true)
}

private fun partTwo(input: String) =
        generateSequence(Pair(1L,
                Computer(instructions = loadDuet(input))
                        .set('a', 1)), { (tc, comp) ->
            if (tc % 50_000_000 == 0L) {
                println("${tc / 1_000_000} M ) $comp")
            }
            if (comp.terminated)
                null
            else
                Pair(tc + 1, comp.tick())
        })
                .last()
                .second
                .get('h')

