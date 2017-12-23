package event2017.duet

import java.util.*

/**
 *
 *
 * @author barneyb
 */
sealed class Value {
    companion object {
        fun parse(s: String) =
                if (s[0].isLetter())
                    Var(s[0])
                else
                    Const(s.toLong())
    }
}

class Const(val n: Long) : Value()
class Var(val name: Char) : Value()

class Wire<T> {

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
    fun remove(): T = list.remove()
    fun first() = list.first()
    fun last() = list.last()

    override fun toString() =
            "Wire($list, $itemCount)"
}

data class Computer(
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
        return if (ins is JumpIns)
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

interface Instruction {
    fun execute(c: Computer): Computer
}

class SendIns(
        val value: Value
) : Instruction {
    override fun execute(c: Computer): Computer {
        c.cout.add(c.get(value))
        return c
    }
}

class SetIns(
        val register: Char,
        val value: Value
) : Instruction {
    override fun execute(c: Computer) =
            c.set(register, value)
}

open class BinaryOpIns(
        val op: (Long, Long) -> Long,
        val register: Char,
        val value: Value
) : Instruction {
    override fun execute(c: Computer) =
            c.set(register, op(c.get(register), c.get(value)))
}

class AddIns(r: Char, v: Value) : BinaryOpIns(Long::plus, r, v)
class MultiplyIns(r: Char, v: Value) : BinaryOpIns(Long::times, r, v)
class ModuloIns(r: Char, v: Value) : BinaryOpIns(Long::rem, r, v)

class ReceiveIns(
        val register: Char
) : Instruction {
    override fun execute(c: Computer) =
            if (c.cin.isEmpty())
                if (c.waiting) c else c.copy(waiting = true)
            else
                (if (c.waiting) c.copy(waiting = false) else c)
                        .set(register, c.cin.remove())

}

interface JumpIns : Instruction

class JumpGTZeroIns(
        val check: Value,
        val offset: Value
) : JumpIns {
    override fun execute(c: Computer) =
            c.copy(pointer = c.pointer +
                    if (c.get(check) > 0)
                        c.get(offset).toInt()
                    else
                        1
            )
}

fun loadDuet(input: String) =
        input.trim()
                .split("\n")
                .map { it.split(" ") }
                .map { cmd ->
                    when (cmd[0]) {
                        "snd" -> SendIns(Value.parse(cmd[1]))
                        "set" -> SetIns(cmd[1][0], Value.parse(cmd[2]))
                        "add" -> AddIns(cmd[1][0], Value.parse(cmd[2]))
                        "mul" -> MultiplyIns(cmd[1][0], Value.parse(cmd[2]))
                        "mod" -> ModuloIns(cmd[1][0], Value.parse(cmd[2]))
                        "rcv" -> ReceiveIns(cmd[1][0])
                        "jgz" -> JumpGTZeroIns(Value.parse(cmd[1]), Value.parse(cmd[2]))
                        else -> throw IllegalArgumentException("Unknown command: $cmd")
                    }
                }
