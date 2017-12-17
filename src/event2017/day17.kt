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
    val buffer = mutableListOf<Int>()
    buffer.add(0)
    var pointer = 0
    for (i in 1..2017) {
        pointer = (pointer + input) % buffer.size + 1
        if (pointer == buffer.size) {
            buffer.add(i)
        } else {
            buffer.add(pointer % buffer.size, i)
        }
    }
    return buffer[pointer + 1]
}

//private fun partTwo(input: Int) =
//        input
