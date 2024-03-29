package event2017.day23

import event2017.banner
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
    val assertOne = event2017.check(::partOne)
    assertOne(input, 3025)
    println("answer: " + partOne(input))

    banner("part 2")
//    val assertTwo = check(::partTwo)
//    assertTwo(input, 916)
    println("answer (debug): " + runOnASIC(true))
//    println("answer: " + runOnASIC())
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
    /* output for a hundred iterations or so (of the expected ~1000)
b=105700, c=122700, d=105700, e=105700, f=0, g=-17000, h=1
b=105717, c=122700, d=105717, e=105717, f=0, g=-16983, h=2
b=105734, c=122700, d=105734, e=105734, f=0, g=-16966, h=3
b=105751, c=122700, d=105751, e=105751, f=1, g=-16949, h=3
b=105768, c=122700, d=105768, e=105768, f=0, g=-16932, h=4
b=105785, c=122700, d=105785, e=105785, f=0, g=-16915, h=5
b=105802, c=122700, d=105802, e=105802, f=0, g=-16898, h=6
b=105819, c=122700, d=105819, e=105819, f=0, g=-16881, h=7
b=105836, c=122700, d=105836, e=105836, f=0, g=-16864, h=8
b=105853, c=122700, d=105853, e=105853, f=0, g=-16847, h=9
b=105870, c=122700, d=105870, e=105870, f=0, g=-16830, h=10
b=105887, c=122700, d=105887, e=105887, f=0, g=-16813, h=11
b=105904, c=122700, d=105904, e=105904, f=0, g=-16796, h=12
b=105921, c=122700, d=105921, e=105921, f=0, g=-16779, h=13
b=105938, c=122700, d=105938, e=105938, f=0, g=-16762, h=14
b=105955, c=122700, d=105955, e=105955, f=0, g=-16745, h=15
b=105972, c=122700, d=105972, e=105972, f=0, g=-16728, h=16
b=105989, c=122700, d=105989, e=105989, f=0, g=-16711, h=17
b=106006, c=122700, d=106006, e=106006, f=0, g=-16694, h=18
b=106023, c=122700, d=106023, e=106023, f=0, g=-16677, h=19
b=106040, c=122700, d=106040, e=106040, f=0, g=-16660, h=20
b=106057, c=122700, d=106057, e=106057, f=0, g=-16643, h=21
b=106074, c=122700, d=106074, e=106074, f=0, g=-16626, h=22
b=106091, c=122700, d=106091, e=106091, f=0, g=-16609, h=23
b=106108, c=122700, d=106108, e=106108, f=0, g=-16592, h=24
b=106125, c=122700, d=106125, e=106125, f=0, g=-16575, h=25
b=106142, c=122700, d=106142, e=106142, f=0, g=-16558, h=26
b=106159, c=122700, d=106159, e=106159, f=0, g=-16541, h=27
b=106176, c=122700, d=106176, e=106176, f=0, g=-16524, h=28
b=106193, c=122700, d=106193, e=106193, f=0, g=-16507, h=29
b=106210, c=122700, d=106210, e=106210, f=0, g=-16490, h=30
b=106227, c=122700, d=106227, e=106227, f=0, g=-16473, h=31
b=106244, c=122700, d=106244, e=106244, f=0, g=-16456, h=32
b=106261, c=122700, d=106261, e=106261, f=1, g=-16439, h=32
b=106278, c=122700, d=106278, e=106278, f=0, g=-16422, h=33
b=106295, c=122700, d=106295, e=106295, f=0, g=-16405, h=34
b=106312, c=122700, d=106312, e=106312, f=0, g=-16388, h=35
b=106329, c=122700, d=106329, e=106329, f=0, g=-16371, h=36
b=106346, c=122700, d=106346, e=106346, f=0, g=-16354, h=37
b=106363, c=122700, d=106363, e=106363, f=1, g=-16337, h=37
b=106380, c=122700, d=106380, e=106380, f=0, g=-16320, h=38
b=106397, c=122700, d=106397, e=106397, f=1, g=-16303, h=38
b=106414, c=122700, d=106414, e=106414, f=0, g=-16286, h=39
b=106431, c=122700, d=106431, e=106431, f=0, g=-16269, h=40
b=106448, c=122700, d=106448, e=106448, f=0, g=-16252, h=41
b=106465, c=122700, d=106465, e=106465, f=0, g=-16235, h=42
b=106482, c=122700, d=106482, e=106482, f=0, g=-16218, h=43
b=106499, c=122700, d=106499, e=106499, f=0, g=-16201, h=44
b=106516, c=122700, d=106516, e=106516, f=0, g=-16184, h=45
b=106533, c=122700, d=106533, e=106533, f=0, g=-16167, h=46
b=106550, c=122700, d=106550, e=106550, f=0, g=-16150, h=47
b=106567, c=122700, d=106567, e=106567, f=0, g=-16133, h=48
b=106584, c=122700, d=106584, e=106584, f=0, g=-16116, h=49
b=106601, c=122700, d=106601, e=106601, f=0, g=-16099, h=50
b=106618, c=122700, d=106618, e=106618, f=0, g=-16082, h=51
b=106635, c=122700, d=106635, e=106635, f=0, g=-16065, h=52
b=106652, c=122700, d=106652, e=106652, f=0, g=-16048, h=53
b=106669, c=122700, d=106669, e=106669, f=1, g=-16031, h=53
b=106686, c=122700, d=106686, e=106686, f=0, g=-16014, h=54
b=106703, c=122700, d=106703, e=106703, f=1, g=-15997, h=54
b=106720, c=122700, d=106720, e=106720, f=0, g=-15980, h=55
b=106737, c=122700, d=106737, e=106737, f=0, g=-15963, h=56
b=106754, c=122700, d=106754, e=106754, f=0, g=-15946, h=57
b=106771, c=122700, d=106771, e=106771, f=0, g=-15929, h=58
b=106788, c=122700, d=106788, e=106788, f=0, g=-15912, h=59
b=106805, c=122700, d=106805, e=106805, f=0, g=-15895, h=60
b=106822, c=122700, d=106822, e=106822, f=0, g=-15878, h=61
b=106839, c=122700, d=106839, e=106839, f=0, g=-15861, h=62
b=106856, c=122700, d=106856, e=106856, f=0, g=-15844, h=63
b=106873, c=122700, d=106873, e=106873, f=0, g=-15827, h=64
b=106890, c=122700, d=106890, e=106890, f=0, g=-15810, h=65
b=106907, c=122700, d=106907, e=106907, f=1, g=-15793, h=65
b=106924, c=122700, d=106924, e=106924, f=0, g=-15776, h=66
b=106941, c=122700, d=106941, e=106941, f=0, g=-15759, h=67
b=106958, c=122700, d=106958, e=106958, f=0, g=-15742, h=68
b=106975, c=122700, d=106975, e=106975, f=0, g=-15725, h=69
b=106992, c=122700, d=106992, e=106992, f=0, g=-15708, h=70
b=107009, c=122700, d=107009, e=107009, f=0, g=-15691, h=71
b=107026, c=122700, d=107026, e=107026, f=0, g=-15674, h=72
b=107043, c=122700, d=107043, e=107043, f=0, g=-15657, h=73
b=107060, c=122700, d=107060, e=107060, f=0, g=-15640, h=74
b=107077, c=122700, d=107077, e=107077, f=1, g=-15623, h=74
b=107094, c=122700, d=107094, e=107094, f=0, g=-15606, h=75
b=107111, c=122700, d=107111, e=107111, f=0, g=-15589, h=76
b=107128, c=122700, d=107128, e=107128, f=0, g=-15572, h=77
b=107145, c=122700, d=107145, e=107145, f=0, g=-15555, h=78
b=107162, c=122700, d=107162, e=107162, f=0, g=-15538, h=79
b=107179, c=122700, d=107179, e=107179, f=0, g=-15521, h=80
b=107196, c=122700, d=107196, e=107196, f=0, g=-15504, h=81
b=107213, c=122700, d=107213, e=107213, f=0, g=-15487, h=82
b=107230, c=122700, d=107230, e=107230, f=0, g=-15470, h=83
b=107247, c=122700, d=107247, e=107247, f=0, g=-15453, h=84
b=107264, c=122700, d=107264, e=107264, f=0, g=-15436, h=85
b=107281, c=122700, d=107281, e=107281, f=0, g=-15419, h=86
b=107298, c=122700, d=107298, e=107298, f=0, g=-15402, h=87
b=107315, c=122700, d=107315, e=107315, f=0, g=-15385, h=88
b=107332, c=122700, d=107332, e=107332, f=0, g=-15368, h=89
b=107349, c=122700, d=107349, e=107349, f=0, g=-15351, h=90
b=107366, c=122700, d=107366, e=107366, f=0, g=-15334, h=91
     */
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

