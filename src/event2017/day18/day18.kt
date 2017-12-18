package event2017.day18

import event2017.banner
import java.io.File
import java.util.*

/**
 *
 *
 * @author barneyb
 */

fun main(args: Array<String>) {
    val input = File("input/2017/day18.txt").readText()

    banner("part 1")
    val assertOne = event2017.check(::partOne)
    assertOne("set a 1\n" +
            "add a 2\n" +
            "mul a a\n" +
            "mod a 5\n" +
            "snd a\n" +
            "set a 0\n" +
            "rcv a\n" +
            "jgz a -1\n" +
            "set a 1\n" +
            "jgz a -2\n", 4)
    assertOne(input, 3423)
    println("answer: " + partOne(input))

    banner("part 2")
    val assertTwo = event2017.check(::partTwo)
    assertTwo("snd 1\n" +
            "snd 2\n" +
            "snd p\n" +
            "rcv a\n" +
            "rcv b\n" +
            "rcv c\n" +
            "rcv d", 3)
    assertTwo(input, 7493)
    println("answer: " + partTwo(input))
}

private sealed class Value {
    companion object {
        fun parse(s: String) =
                if (s[0].isLetter())
                    Var(s[0])
                else
                    Const(s.toLong())
    }
}

private class Const(val n: Long) : Value()
private class Var(val name: Char) : Value()

private class Wire<T>() {

    private val list = LinkedList<T>()
    var itemCount = 0
        private set
    val size
        get() =
            list.size

    fun add(value: T) {
        itemCount += 1
        list.add(value)
    }

    fun isEmpty() = list.isEmpty()
    fun remove() = list.remove()
    fun first() = list.first()
    fun last() = list.last()

    override fun toString() =
            "Wire($list, $itemCount)"
}

private data class Computer(
        val id: Long = 0,
        val cin: Wire<Long> = Wire(),
        val waiting: Boolean = false,
        val cout: Wire<Long> = cin,
        val pointer: Int = 0,
        val registers: Map<Char, Long> = mapOf('p' to id),
        val instructions: List<Instruction>
) {

    val terminated
        get() =
            pointer < 0 || pointer >= instructions.size

    fun get(v: Value) =
            when (v) {
                is Const -> v.n
                is Var -> get(v.name)
            }

    fun get(register: Char) = registers[register] ?: 0

    fun set(register: Char, v: Value) =
            copy(registers = registers + Pair(register, get(v)))

    fun set(register: Char, v: Long) =
            copy(registers = registers + Pair(register, v))

    fun tick(): Computer {
        val ins = instructions[pointer]
        return if (ins is Jump && ins.willJump(this))
            ins.execute(this)
        else {
            val next = ins.execute(this)
            if (next.waiting) {
                next
            } else {
                next.copy(pointer = pointer + 1)
            }
        }
    }
}

private sealed class Instruction {
    abstract fun execute(c: Computer): Computer
}

private class Send(
        val value: Value
) : Instruction() {
    override fun execute(c: Computer): Computer {
        c.cout.add(c.get(value))
        return c
    }
}

private class Set(
        val register: Char,
        val value: Value
) : Instruction() {
    override fun execute(c: Computer) =
            c.set(register, value)
}

private open class BinaryOp(
        val op: (Long, Long) -> Long,
        val register: Char,
        val value: Value
) : Instruction() {
    override fun execute(c: Computer) =
            c.set(register, op(c.get(register), c.get(value)))
}

private class Add(r: Char, v: Value) : BinaryOp(Long::plus, r, v)
private class Multiply(r: Char, v: Value) : BinaryOp(Long::times, r, v)
private class Modulo(r: Char, v: Value) : BinaryOp(Long::rem, r, v)

private class Receive(
        val register: Char
) : Instruction() {
    override fun execute(c: Computer) =
            if (c.cin.isEmpty())
                if (c.waiting) c else c.copy(waiting = true)
            else
                (if (c.waiting) c.copy(waiting = false) else c)
                        .set(register, c.cin.remove())

}

private interface Jump {
    fun willJump(c: Computer): Boolean
}

private class JumpGTZero(
        val check: Value,
        val offset: Value
) : Instruction(), Jump {
    override fun execute(c: Computer) =
            c.copy(pointer = c.pointer + c.get(offset).toInt())

    override fun willJump(c: Computer) =
            c.get(check) > 0
}

private fun partOne(input: String) =
        generateSequence(Computer(instructions = parse(input)), { c ->
            val ins = c.instructions[c.pointer]
            if (ins is Receive)
                if (c.get(ins.register) != 0L)
                    null
                else
                    c.copy(pointer = c.pointer + 1)
            else
                c.tick()
        })
                .last()
                .cout
                .last()

private fun parse(input: String) =
        input.trim()
                .split("\n")
                .map { it.split(" ") }
                .map { cmd ->
                    when (cmd[0]) {
                        "snd" -> Send(Value.parse(cmd[1]))
                        "set" -> Set(cmd[1][0], Value.parse(cmd[2]))
                        "add" -> Add(cmd[1][0], Value.parse(cmd[2]))
                        "mul" -> Multiply(cmd[1][0], Value.parse(cmd[2]))
                        "mod" -> Modulo(cmd[1][0], Value.parse(cmd[2]))
                        "rcv" -> Receive(cmd[1][0])
                        "jgz" -> JumpGTZero(Value.parse(cmd[1]), Value.parse(cmd[2]))
                        else -> throw IllegalArgumentException("Unknown command: $cmd")
                    }
                }

private fun partTwo(input: String): Int {
    val instructions = parse(input)
    val one2zero = Wire<Long>()
    val zero2one = Wire<Long>()
    generateSequence(Triple(0,
            Computer(id = 0, instructions = instructions, cin = one2zero, cout = zero2one),
            Computer(id = 1, instructions = instructions, cin = zero2one, cout = one2zero)
    ), { (t, z, o) ->
        if ((z.waiting || z.terminated) && (o.waiting || o.terminated)) {
            null // we're done
        } else {
            Triple(t + 1, z.tick(), o.tick())
        }
    })
            .last() // to consume it
    return one2zero.itemCount
}
