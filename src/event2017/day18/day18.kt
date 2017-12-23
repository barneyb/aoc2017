package event2017.day18

import event2017.banner
import event2017.duet.Computer
import event2017.duet.ReceiveIns
import event2017.duet.Wire
import event2017.duet.loadDuet
import java.io.File

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

private fun partOne(input: String) =
        generateSequence(Computer(instructions = loadDuet(input)), { c ->
            val ins = c.instructions[c.pointer]
            if (ins is ReceiveIns)
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

private fun partTwo(input: String): Int {
    val instructions = loadDuet(input)
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
