package event2017

import java.io.File

/**
 *
 *
 * @author barneyb
 */

fun main(args: Array<String>) {
    val input = File("input/2017/day09.txt").readText().trim()

    banner("part 1")
    val partOne = check(::scoreGroups)
    partOne("{}", 1)
    partOne("{{{}}}", 6)
    partOne("{{},{}}", 5)
    partOne("{{{},{},{{}}}}", 16)
    partOne("{<a>,<a>,<a>,<a>}", 1)
    partOne("{{<ab>},{<ab>},{<ab>},{<ab>}}", 9)
    partOne("{{<a!>},{<a!>},{<a!>},{<ab>}}", 3)
    partOne(input, 10616)
    println("answer: " + scoreGroups(input))
}

private fun removeGarbage(input: String): String {
    var inGarbage = false
    var escaped = false
    return input.filter { c ->
        if (escaped) {
            escaped = false
            false
        } else if (c == '!') {
            escaped = true
            false
        } else if (inGarbage) {
            if (c == '>') {
                inGarbage = false
            }
            false
        } else {
            if (c == '<') {
                inGarbage = true
                false
            } else {
                true
            }
        }
    }
}

private fun scoreGroups(input: String): Int {
    return removeGarbage(input).fold(Pair(0, 0), { p, c ->
        if (c == '{')
            Pair(p.first + 1, p.second)
        else if (c == '}')
            Pair(p.first - 1, p.second + p.first)
        else
            p
    }).second
}
