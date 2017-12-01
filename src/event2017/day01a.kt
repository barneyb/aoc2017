package event2017

import java.io.File

/**
 *
 *
 * @author barneyb
 */

fun main(args: Array<String>) {
    val input = File("input/2017/day01.txt").readText().trim()
    println(captcha("1122"))
    println(captcha("1111"))
    println(captcha("1234"))
    println(captcha("91212129"))
    println(captcha(input))
}

fun captcha(str:String):Int {
    return str.fold(Pair(str.last(), 0), { a, c ->
        Pair(c, a.second + if (c == a.first) (c.toInt() - 48) else 0)
    }).second
}
