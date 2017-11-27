package event2015

import java.io.File

/**
 *
 *
 * @author barneyb
 */

fun main(args: Array<String>) {
    val str = File("input/2015/day01.txt").readText()
    var f = 0
    var i = 0
    for (c in str) {
        i += 1
        f += if (c == '(') 1 else -1
        if (f < 0) {
            println(i)
            break
        }
    }
}
