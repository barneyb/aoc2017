package event2017

import java.io.File

/**
 *
 *
 * @author barneyb
 */
fun main(args: Array<String>) {
    val input = File("input/2017/day08.txt").readText().trim()
    val exampleInput = "b inc 5 if a > 1\n" +
            "a inc 1 if b < 5\n" +
            "c dec -10 if a >= 1\n" +
            "c inc -20 if c == 10"

    banner("part one")
    val partOne = check(::maxFinalValue)
    partOne(exampleInput, 1)
    partOne(input, 3880)
    println("answer: " + maxFinalValue(input))
}

private enum class Arithmetic(val text: String, val op: (Int, Int) -> Int) {
    INC("inc", Int::plus),
    DEC("dec", Int::minus);

    fun eval(left: Int, right: Int) =
            op(left, right)

}

private enum class Comparison(val text: String, val op: (Int, Int) -> Boolean) {
    LT("<", { a, b -> a < b }),
    LTE("<=", { a, b -> a <= b }),
    GT(">", { a, b -> a > b }),
    GTE(">=", { a, b -> a >= b }),
    EQ("==", Int::equals),
    NEQ("!=", { a, b -> a != b });

    fun eval(left: Int, right: Int) =
            op(left, right)

}

private data class Instruction(
        val register: String,
        val op:Arithmetic,
        val offset:Int,
        val condition: Condition
) {
    fun eval(proc: Processor) =
            if (condition.eval(proc))
                proc.set(register, op.eval(proc[register], offset))
            else
                proc
}

private data class Condition(
        val register: String,
        val op:Comparison,
        val value:Int
) {
    fun eval(proc: Processor) =
            op.eval(proc[register], value)
}

private class Processor(val registers: Map<String, Int> = mapOf()) {

    operator fun get(register:String) =
            registers.getOrDefault(register, 0)

    operator fun set(register:String, value:Int) =
            Processor(registers.plus(Pair(register, value)))

    fun maxValue() =
            registers.values.max() ?: 0
}

private fun parse(input: String): List<Instruction> =
        input.split("\n")
                .map { line ->
                    // b inc 5 if a > 1
                    // 0 1   2 3  4 5 6
                    val parts = line.split(" ")
                    Instruction(
                            parts[0],
                            Arithmetic.values().find { it.text == parts[1] }!!,
                            parts[2].toInt(),
                            // part 3 is noise
                            Condition(
                                    parts[4],
                                    Comparison.values().find { it.text == parts[5] }!!,
                                    parts[6].toInt()
                            )
                    )
                }

private fun maxFinalValue(input: String): Int =
        parse(input).fold(Processor(), { p, i -> i.eval(p) }).maxValue()
