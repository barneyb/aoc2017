package event2017

import java.io.File

/**
 *
 *
 * @author barneyb
 */

fun main(args: Array<String>) {
    val input = File("input/2017/day01.txt").readText().trim()

    banner("part 1")
    val partOne = check(captcha)
    partOne("1122", 3)
    partOne("1111", 4)
    partOne("1234", 0)
    partOne("91212129", 9)
    partOne(input, 1089)
    println("answer: " + captcha(input))

    banner("part 2")
    val partTwo = check(captcha2)
    partTwo("1212", 6)
    partTwo("1221", 0)
    partTwo("123425", 4)
    partTwo("123123", 12)
    partTwo("12131415", 4)
    partTwo(input, 1156)
    println("answer: " + captcha2(input))
}

private val captcha = captchaFactory { it.last().toString().plus(it) }

private val captcha2 = captchaFactory { it.substring(it.length / 2).plus(it) }

private fun captchaFactory(pairer:(String) -> String) = { input:String ->
    input.zip(pairer(input))
            .filter { it.first == it.second }
            .map { it.first }
            .map { it.toInt() - 48 } // codepoint of '0'
            .sum()
}
