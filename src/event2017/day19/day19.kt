package event2017.day19

import event2017.Direction
import event2017.Direction.*
import event2017.Point
import event2017.banner
import event2017.check
import java.io.File

/**
 *
 *
 * @author barneyb
 */

fun main(args: Array<String>) {
    val input = File("input/2017/day19.txt").readText()
    val exampleInput = "" +
            "     |          \n" +
            "     |  +--+    \n" +
            "     A  |  C    \n" +
            " F---|----E|--+ \n" +
            "     |  |  |  D \n" +
            "     +B-+  +--+ \n"

    banner("part 1")
    val assertOne = check(::partOne)
    assertOne(exampleInput, "ABCDEF")
    assertOne(input, "VEBTPXCHLI")
    println("answer: " + partOne(input))

    banner("part 2")
    val assertTwo = check(::partTwo)
    assertTwo(exampleInput, 38)
    assertTwo(input, 18702)
    println("answer: " + partTwo(input))
}

private data class Walker(
        val point: Point,
        val dir: Direction = UP,
        val path: String = ""
)

private operator fun List<String>.contains(p: Point) =
        p.y in 0..(size - 1) && p.x in 0..(this[p.y].length)

private operator fun List<String>.get(p: Point) =
        if (p in this) this[p.y][p.x] else ' '

fun Char.isHorizontalPath() = this == '-' || this.isLetter()
fun Char.isVerticalPath() = this == '|' || this.isLetter()

private fun partOne(input: String): String =
        genPath(input)
                .last()
                .path

private fun genPath(input: String): Sequence<Walker> {
    // because Point works in a normal Cartesian space, not the
    // origin-at-top-left version used for the diagram, the vertical axis
    // appears to work backwards. Point.up should be read as "move the point
    // one step along the vertical axis in the positive direction", which in
    // this case actually means toward the bottom of the diagram.
    val diag = input
            .split("\n")
            .filter { !it.all { it.isWhitespace() } }
    return generateSequence(Walker(Point(diag.first().indexOf('|'), 0)), { (p, d, path) ->
        val c = diag[p]
        if (c == ' ') { // we reached the end
            null
        } else if (c == '+') { // have to turn
            if (d == UP || d == DOWN)
                if (diag[p.left()].isHorizontalPath())
                    Walker(p.left(), LEFT, path)
                else
                    Walker(p.right(), RIGHT, path)
            else // left or right
                if (diag[p.up()].isVerticalPath())
                    Walker(p.up(), UP, path)
                else
                    Walker(p.down(), DOWN, path)
        } else { // take another step?
            Walker(p + d, d, if (c.isLetter()) path + c else path)
        }
    })
}

private fun partTwo(input: String) =
        // have to substract one because we take the step past the end
        genPath(input)
                .count() - 1
