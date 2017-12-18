package event2017.day18

import event2017.banner
import java.io.File

/**
 *
 *
 * @author barneyb
 */

fun main(args: Array<String>) {
    val input = File("input/2017/day18.txt").readText()
    val exampleInput = "set a 1\n" +
            "add a 2\n" +
            "mul a a\n" +
            "mod a 5\n" +
            "snd a\n" +
            "set a 0\n" +
            "rcv a\n" +
            "jgz a -1\n" +
            "set a 1\n" +
            "jgz a -2\n"

    banner("part 1")
    val assertOne = event2017.check(::partOne)
    assertOne(exampleInput, 4)
    assertOne(input, 3423)
    println("answer: " + partOne(input))

//    banner("part 2")
//    val assertTwo = check(::partTwo)
//    assertTwo(exampleInput, 309)
////    assertTwo(input, 290)
//    println("answer: " + partTwo(input))
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

private data class Computer(
        val speaker: Long = 0,
        val pointer: Int = 0,
        val registers: Map<Char, Long> = mapOf(),
        val instructions: List<Instruction>
) {
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
        else
            ins.execute(this)
                    .copy(pointer = pointer + 1)
    }
}

private sealed class Instruction {
    abstract fun execute(c: Computer): Computer
}

private class PlaySound(
        val value: Value
) : Instruction() {
    override fun execute(c: Computer) =
            c.copy(speaker = c.get(value))
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

private class RecoverSound(
        val value: Value
) : Instruction() {
    override fun execute(c: Computer) =
            c

    fun willRecover(c: Computer) =
            c.get(value) != 0L
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
            if (ins is RecoverSound && ins.willRecover(c))
                null
            else
                c.tick()
        })
                .last()
                .speaker

private fun parse(input: String) =
        input.trim()
                .split("\n")
                .map { it.split(" ") }
                .map { cmd ->
                    when (cmd[0]) {
                        "snd" -> PlaySound(Value.parse(cmd[1]))
                        "set" -> Set(cmd[1][0], Value.parse(cmd[2]))
                        "add" -> Add(cmd[1][0], Value.parse(cmd[2]))
                        "mul" -> Multiply(cmd[1][0], Value.parse(cmd[2]))
                        "mod" -> Modulo(cmd[1][0], Value.parse(cmd[2]))
                        "rcv" -> RecoverSound(Value.parse(cmd[1]))
                        "jgz" -> JumpGTZero(Value.parse(cmd[1]), Value.parse(cmd[2]))
                        else -> throw IllegalArgumentException("Unknown command: $cmd")
                    }
                }

//private fun partTwo(input: String) =
//        input.length
