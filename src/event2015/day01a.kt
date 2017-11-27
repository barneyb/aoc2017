package event2015

import java.io.File

/**
 *
 *
 * @author barneyb
 */

fun main(args: Array<String>) {
    val str = File("input/2015/day01.txt").readText()
    println(str.fold(0, {a, c -> a + if (c == '(') 1 else -1}))
}
