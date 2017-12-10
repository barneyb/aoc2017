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

    banner("part two")
    val partTwo = check(::countGarbage)
    partTwo("<>", 0)
    partTwo("<random characters>", 17)
    partTwo("<{o\"i!a,<{i<a>", 10)
    partTwo(input, 5101)
    println("answer: " + countGarbage(input))
}

private fun removeGarbage(input: String): Pair<String, Int> {
    var inGarbage = false
    var canceled = false
    var garbageChars = 0
    return Pair(input.filter { c ->
        if (canceled) {
            canceled = false
            false
        } else if (c == '!') {
            canceled = true
            false
        } else if (inGarbage) {
            if (c == '>') {
                inGarbage = false
            } else {
                garbageChars += 1
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
    }, garbageChars)
}

private fun scoreGroups(input: String) =
    removeGarbage(input).first.fold(Pair(0, 0), { p, c ->
        if (c == '{')
            Pair(p.first + 1, p.second)
        else if (c == '}')
            Pair(p.first - 1, p.second + p.first)
        else
            p
    }).second

private fun countGarbage(input: String) =
        removeGarbage(input).second
