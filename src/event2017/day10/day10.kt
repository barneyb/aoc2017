package event2017.day10

import event2017.banner
import event2017.check
import java.io.File

/**
 *
 *
 * @author barneyb
 */

fun main(args: Array<String>) {
    val input = File("input/2017/day10.txt").readText().trim()

    banner("part 1")
    val firstTwoOf5 = firstTwo(5)
    val firstTwoOf256 = firstTwo(256)
    check(firstTwoOf5)("3, 4, 1, 5", 12)
    check(firstTwoOf256)(input, 3770)
    println("answer: " + firstTwoOf256(input))

    banner("part two")
    check(::parse2)("1,2,3", listOf(49, 44, 50, 44, 51, 17, 31, 73, 47, 23))
    val toHex = check { it: Int -> it.toHex() }
    toHex(0, "00")
    toHex(1, "01")
    toHex(9, "09")
    toHex(10, "0a")
    toHex(15, "0f")
    toHex(16, "10")
    toHex(255, "ff")
    val partTwo = check(::knotHash)
    partTwo("", "a2582a3a0e66e6e86e3812dcb672a272")
    partTwo("AoC 2017", "33efeb34ea91902bb2f59c9920caa6cd")
    partTwo("1,2,3", "3efbe78a8d82f29979031a4aa0b16a9d")
    partTwo("1,2,4", "63960835bcdc130f0b66d7ff4f6a5a8e")
    partTwo(input, "a9d0e68649d0174c8756a59ba21d4dc6")
    println("answer: " + knotHash(input))
}

private fun parse(input: String) =
        input.split(",").map { it.trim().toInt() }

private fun firstTwo(size: Int) =
        { input: String ->
            val list = IntRange(0, size - 1).toList()
            val (a, b) = parse(input).fold(State(list), ::tick).list.take(2)
            a * b
        }

private data class State(
        val list: List<Int>,
        val pointer: Int = 0,
        val skip: Int = 0
)

private fun tick(state: State, len: Int): State {
    val newList = if (state.pointer + len >= state.list.size) {
        val tailLen = state.list.size - state.pointer
        val headLen = len - tailLen
        val sublist = state.list.subList(state.pointer, state.list.size)
                .plus(state.list.subList(0, headLen))
                .reversed()
        val middle = state.list.subList(headLen, state.pointer)
        sublist.subList(tailLen, sublist.size)
                .plus(middle)
                .plus(sublist.subList(0, tailLen))
    } else {
        val prefix = state.list.subList(0, state.pointer)
        val sublist = state.list.subList(state.pointer, state.pointer + len)
                .reversed()
        val suffix = state.list.subList(state.pointer + len, state.list.size)
        prefix
                .plus(sublist)
                .plus(suffix)
    }
    return State(newList, (state.pointer + len + state.skip) % state.list.size, state.skip + 1)
}

private fun parse2(input: String) =
        input.map { c -> c.toInt() }
                .plus(listOf(17, 31, 73, 47, 23))

private fun Int.toHex():String {
    val s = toString(16)
    return if (s.length == 1) "0" + s else s
}

fun knotHash(input: String): String {
    val sixteen = IntRange(0, 15)
    val sixtyfour = IntRange(0, 63)
    val lengths = parse2(input)
    val list = IntRange(0, 255).toList()
    val sparse = sixtyfour.fold(State(list), { s, _ ->
        lengths.fold(s, ::tick)
    }).list
    return sixteen.map { block ->
        sixteen.fold(0, { a, num ->
            a xor sparse[block * 16 + num]
        })
    }.joinToString("") {
        it.toHex()
    }
}
