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
    assertOne(input, 633)
    println("answer: " + partOne(input))
}

private data class Blueprint(
        val initialState: State,
        val checksumCount: Int
)

private data class State(
        var ifFalse: Action?,
        var ifTrue: Action?
) {

    operator fun get(b: Boolean) =
            (if (b) ifTrue else ifFalse) as Action

}

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

private class Tape() {
    var curr: Node = Node()
    var head: Node = curr
    var tail: Node = curr

    fun tick(s: State): State {
        val a = s[curr.value]
        curr.value = a.write
        if (a.left) left() else right()
        return a.next
    }

    fun left() {
        var n = curr.left
        if (n == null) {
            n = Node(right = curr)
            curr.left = n
            head = n
        }
        curr = n
    }

    fun right() {
        var n = curr.right
        if (n == null) {
            n = Node(left = curr)
            curr.right = n
            tail = n
        }
        curr = n
    }

    fun countOnes(): Int {
        var c = head as Node?
        var n = 0
        while (c != null) {
            if (c.value)
                n += 1
            c = c.right
        }
        return n
    }

    private class Node(
            var left: Node? = null,
            var right: Node? = null
    ) {
        var value: Boolean = false
    }

}

private fun partOne(input: String): Int {
    val blueprint = parse(input)
    val tape = Tape()
    var state = blueprint.initialState
    (1..blueprint.checksumCount).forEach {
        state = tape.tick(state)
    }
    return tape.countOnes()
}
