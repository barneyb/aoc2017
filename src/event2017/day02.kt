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
    val partOne = check(checksum1)
    partOne("5 1 9 5\n" +
            "7 5 3\n" +
            "2 4 6 8", 18)
    partOne(input, 53978)
    println("answer: " + checksum1(input))

    banner("part 2")
    val partTwo = check(checksum2)
    partTwo("5 9 2 8\n" +
            "9 4 7 3\n" +
            "3 8 6 5", 9)
    println("answer: " + checksum2(input))
}

typealias Row = List<Int>
typealias Sheet = List<Row>;

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

val checksumFactory = { chksum: (Row) -> Int ->
    { sheet: String ->
        parse(sheet)
                .map(chksum)
                .sum()
    }
}

val checksum1 = checksumFactory { row ->
    row.max()!! - row.min()!!
}

val checksum2 = checksumFactory { row ->
    row.max()!! - row.min()!!
}

