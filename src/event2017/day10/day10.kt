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
    val firstTwo_5 = firstTwo(5)
    val firstTwo_256 = firstTwo(256)
    check(firstTwo_5)("3, 4, 1, 5", 12)
    check(firstTwo_256)(input, 3770)
    println("answer: " + firstTwo_256(input))
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
