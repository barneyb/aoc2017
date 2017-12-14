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

fun Int.toPoint() = Point(this / GRID_DIM, this % GRID_DIM)

fun Point.toIndex() = this.x * GRID_DIM + this.y

fun Point.inGrid() = this.x >= 0 && this.y >= 0 &&
        this.x < GRID_DIM && this.y < GRID_DIM

fun Point.adjacent() = listOf(
        up(),
        down(),
        left(),
        right()
)

fun regionCount(input: String): Int {
    val rawGrid = mutableSetOf<Int>()
    binaryGrid(input)
            .forEachIndexed({ y, hash ->
                hash.forEachIndexed({ x, c ->
                    if (c == '1')
                        rawGrid.add(x * GRID_DIM + y)
                })
            })

    return generateSequence(rawGrid, { grid ->
        val start = grid.first()
        generateSequence(setOf(start), { unchecked ->
            unchecked.fold(setOf(), { next, m ->
                grid.remove(m)
                next + m.toPoint()
                        .adjacent()
                        .filter {
                            it.inGrid()
                        }
                        .map {
                            it.toIndex()
                        }
                        .filter {
                            grid.contains(it)
                        }
            })
        })
                .dropWhile { it.isNotEmpty() }
                .first() // to consume it
        grid
    })
            .takeWhile {
                it.isNotEmpty()
            }
            .count()
}
