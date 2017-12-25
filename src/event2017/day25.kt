package event2017

import java.io.File

/**
 *
 *
 * @author barneyb
 */

fun main(args: Array<String>) {
    val input = File("input/2017/day25.txt").readText()
    val exampleInput = "Begin in state A.\n" +
            "Perform a diagnostic checksum after 6 steps.\n" +
            "\n" +
            "In state A:\n" +
            "  If the current value is 0:\n" +
            "    - Write the value 1.\n" +
            "    - Move one slot to the right.\n" +
            "    - Continue with state B.\n" +
            "  If the current value is 1:\n" +
            "    - Write the value 0.\n" +
            "    - Move one slot to the left.\n" +
            "    - Continue with state B.\n" +
            "\n" +
            "In state B:\n" +
            "  If the current value is 0:\n" +
            "    - Write the value 1.\n" +
            "    - Move one slot to the left.\n" +
            "    - Continue with state A.\n" +
            "  If the current value is 1:\n" +
            "    - Write the value 1.\n" +
            "    - Move one slot to the right.\n" +
            "    - Continue with state A.\n"

    banner("part 1")
    val assertOne = check(::partOne)
    assertOne(exampleInput, 3)
//    assertOne(input, 619)
//    println("answer: " + partOne(input))

//    banner("part 2")
//    val assertTwo = check(::partTwo)
//    assertTwo(exampleInput, 309)
////    assertTwo(input, 290)
//    println("answer: " + partTwo(input))
}

private data class Blueprint(
        val initialState: State,
        val checksumCount: Int
)

private data class State(
        var ifFalse: Action?,
        var ifTrue: Action?
)

private data class Action(
        val write: Boolean,
        val left: Boolean,
        var next: State
)

private fun ensureState(states: MutableMap<Char, State>, key: Char, ifFalse: Action? = null, ifTrue: Action? = null): State {
    if (states.containsKey(key)) {
        val s = states[key]!!
        if (s.ifFalse == null)
            s.ifFalse = ifFalse
        if (s.ifTrue == null)
            s.ifTrue = ifTrue
        return s
    } else {
        val s = State(ifFalse, ifTrue)
        states.put(key, s)
        return s
    }
}

private fun parse(input: String): Blueprint {
    val lines = input.trim()
            .split("\n")
            .map { it.trim() }
            .iterator()
    val initial = lines.next()
            .split(' ')
            .last()[0]
    val chksum = lines.next()
            .split(' ')
            .drop(5)
            .first()
            .toInt()

    val states = mutableMapOf<Char, State>()
    while (lines.hasNext()) {
        lines.next() // the blank one
        val label = lines.next()
                .split(' ')
                .last()[0]
        val (ifFalse, ifTrue) = (1..2).map {
            lines.next() // the 'if' line
            Action(
                    lines.next()
                            .split(' ')
                            .last()[0] == '1',
                    lines.next()
                            .split(' ')
                            .last()[0] == 'l',
                    ensureState(states, lines.next()
                            .split(' ')
                            .last()[0])
            )
        }
        ensureState(states, label, ifFalse, ifTrue)
    }

    return Blueprint(states.get(initial)!!, chksum)
}

private fun partOne(input: String) =
        parse(input).checksumCount

private fun partTwo(input: String) =
        input.length
