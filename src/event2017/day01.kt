package event2017

import java.io.File

/**
 *
 *
 * @author barneyb
 */

val CHAR_ZERO = 48

fun main(args: Array<String>) {
    val input = File("input/2017/day01.txt").readText().trim()

    banner("part 1")
    check(::captcha, "1122", 3)
    check(::captcha, "1111", 4)
    check(::captcha, "1234", 0)
    check(::captcha, "91212129", 9)
    println("answer: " + captcha(input))

    banner("part 2")
    check(::captcha2, "1212", 6)
    check(::captcha2, "1221", 0)
    check(::captcha2, "123425", 4)
    check(::captcha2, "123123", 12)
    check(::captcha2, "12131415", 4)
    println("answer: " + captcha2(input))
}

fun banner(label:String) {
    println("= ${label} ".padEnd(80, '='))
}

fun <T> check(functionUnderTest:(String) -> T, input:String, expected:T) {
    val actual = functionUnderTest(input)
    if (actual != expected)
        println("Expected '$expected' but got '$actual' (from '$input')");
}

fun captcha(str:String):Int {
    return str.fold(Pair(str.last(), 0), { a, c ->
        Pair(c, a.second + if (c == a.first) (c.toInt() - CHAR_ZERO) else 0)
    }).second
}

fun captcha2(str:String):Int {
    val pivot = str.length / 2
    return str.foldIndexed(0, { i, a, c ->
        a + if (c == str.get((i + pivot) % str.length)) (c.toInt() - CHAR_ZERO) else 0
    })
}
