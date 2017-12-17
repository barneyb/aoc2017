package event2017

/**
 *
 *
 * @author barneyb
 */

fun main(args: Array<String>) {
    val input = 335
    val exampleInput = 3

    banner("part 1")
    val assertOne = check(::partOne)
    assertOne(exampleInput, 638)
    assertOne(input, 1282)
    println("answer: " + partOne(input))

//    banner("part 2")
//    val assertTwo = check(::partTwo)
//    assertTwo(exampleInput, 309)
////    assertTwo(input, 290)
//    println("answer: " + partTwo(input))
}

private fun partOne(input: Int): Int {
    val (buffer, pointer) = (1..2017)
            .fold(Pair(mutableListOf(0), 0), { (b, p), i ->
                val next = (p + input) % b.size + 1
                if (next == b.size)
                    b.add(i)
                else
                    b.add(next % b.size, i)
                Pair(b, next)
            })
    return buffer[pointer + 1]
}

//private fun partTwo(input: Int) =
//        input
