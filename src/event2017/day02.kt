package event2017

import java.io.File

/**
 *
 *
 * @author barneyb
 */


fun main(args: Array<String>) {
    val input = File("input/2017/day02.txt").readText().trim()

    banner("part 1")
    val partOne = check(::checksum)
    partOne("5 1 9 5\n" +
            "7 5 3\n" +
            "2 4 6 8", 18)
    partOne(input, 53978)
    println("answer: " + checksum(input))
}

typealias Sheet = List<List<Int>>;

fun parse(sheet: String):Sheet {
    return sheet
            .replace('\t', ' ')
            .trim()
            .split('\n')
            .map { row ->
                row
                        .trim()
                        .split(' ')
                        .map { cell -> cell.toInt() }
            }
}

fun checksum(sheet: String):Int {
    return parse(sheet)
            .map { row ->
                row.max()!! - row.min()!!
            }
            .sum()
}
