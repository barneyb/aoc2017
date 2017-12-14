package event2017.day14

import event2017.Point
import event2017.banner
import event2017.check
import event2017.day10.knotHash

fun main(args: Array<String>) {
    val input = "jxqlasbh"
    val exampleInput = "flqrgnkx"

    banner("part 1")
    val partOne = check(::blocksUsed)
    partOne(exampleInput, 8108)
    partOne(input, 8140)
    println("answer: " + blocksUsed(input))

    banner("part 2")
    val partTwo = check(::regionCount)
    partTwo(exampleInput, 1242)
    partTwo(input, 1182)
    println("answer: " + regionCount(input))

    banner("part 2 (pure)")
    val partTwoPure = check(::regionCountPure)
    partTwoPure(input, 1182)
}

val GRID_DIM = 128

fun binaryGrid(input: String) =
        (0 until GRID_DIM)
                .map {
                    knotHash(input + "-" + it)
                            .map { c ->
                                c.toString()
                                        .toInt(16)
                                        .toString(2)
                                        .padStart(4, '0')
                            }
                            .joinToString("")
                }

fun blocksUsed(input: String) =
        binaryGrid(input).map {
            it.count { it == '1' }
        }.sum()

fun Point.adjacent() = listOf(
        up(),
        down(),
        left(),
        right()
)

fun regionCount(input: String): Int {
    val grid = mutableSetOf<Point>()
    binaryGrid(input)
            .forEachIndexed({ y, hash ->
                hash.forEachIndexed({ x, c ->
                    if (c == '1')
                        grid.add(Point(x, y))
                })
            })

    var regionCount = 0
    while (grid.isNotEmpty()) {
        regionCount += 1
        var edges = setOf(grid.first())
        while (edges.isNotEmpty()) {
            edges = edges.fold(setOf(), { next, m ->
                grid.remove(m)
                next + m.adjacent()
                        .filter { grid.contains(it) }
            })
        }
    }
    return regionCount
}


fun regionCountPure(input: String): Int {
    val rawGrid = binaryGrid(input)
            .mapIndexed { y, hash ->
                hash.toList()
                        .zip(0..hash.length)
                        .filter { (c, _) -> c == '1' }
                        .map { (_, x) ->
                            Point(x, y)
                        }
            }
            .flatten()
            .toSet()

    return generateSequence(rawGrid, { grid ->
        generateSequence(Pair(grid, setOf(grid.first())), { (grid, unchecked) ->
            unchecked.fold(Pair(grid, setOf<Point>()), { (grid, next), m ->
                Pair(grid - m, next + m.adjacent()
                        .filter { grid.contains(it) })
            })
        })
                .dropWhile { it.second.isNotEmpty() }
                .first()
                .first
    })
            .takeWhile { it.isNotEmpty() }
            .count()
}
