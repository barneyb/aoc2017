package event2017

import java.io.File
import java.util.*

/**
 *
 *
 * @author barneyb
 */

fun main(args: Array<String>) {
    val input = File("input/2017/day21.txt").readText()
    val exampleInput = "../.# => ##./#../...\n" +
            ".#./..#/### => #..#/..../..../#..#\n"

    val splits = mapOf(
            "#./.." to listOf("#./.."),
            ".#./..#/###" to listOf(
                    ".#./..#/###"
            ),
            "#..#/..../..../#..#" to listOf(
                    "#./..",
                    ".#/..",
                    "../#.",
                    "../.#"
            ),
            "##.##./#..#../....../##.##./#..#../......" to listOf(
                    "##/#.",
                    ".#/.#",
                    "#./..",
                    "../##",
                    "../.#",
                    "../#.",
                    "#./..",
                    ".#/..",
                    "../.."
            ),
            "###...###/...###.../###...###/" + "#.##.##.#/.#..#..#./#.##.##.#/" + "#.#.#.#.#/.#.#.#.#./#.#.#.#.#" to listOf(
                    "###/.../###",
                    ".../###/...",
                    "###/.../###",
                    "#.#/.#./#.#",
                    "#.#/.#./#.#",
                    "#.#/.#./#.#",
                    "#.#/.#./#.#",
                    ".#./#.#/.#.",
                    "#.#/.#./#.#"
            )
    )
    banner("Image.split")
    val assertSplit = check({ it: String -> Image(it).split().map { it.toString() } })
    splits.forEach { (i, o) ->
        assertSplit(i, o)
    }

    banner("List<Image>.stitch")
    val assertStitch = check({ it: List<String> -> it.map { Image(it) }.stitch().toString() })
    splits.forEach { (i, o) ->
        assertStitch(o, i)
    }

    banner("Image.flip")
    val assertFlip = check({ it: String -> Image(it).flip().toString() })
    assertFlip(".#./..#/###", "###/..#/.#.")
    assertFlip("#./..", "../#.")

    banner("Image.rotations")
    val assertRotations = check({ it: String -> Image(it).rotations().map { it.toString() } })
    assertRotations(
            "#./" +
                    "..",
            listOf(
                    "#./" +
                            "..",

                    ".#/" +
                            "..",

                    "../" +
                            ".#",

                    "../" +
                            "#."
            )
    )
    assertRotations(
            ".#./" +
                    "..#/" +
                    "###",
            listOf(
                    ".#./" +
                            "..#/" +
                            "###",

                    "#../" +
                            "#.#/" +
                            "##.",

                    "###/" +
                            "#../" +
                            ".#.",

                    ".##/" +
                            "#.#/" +
                            "..#"
            ))

    banner("part 1")
    check(partZero)(exampleInput, 12)
    check(partOne)(input, 142)
    println("answer: " + partOne(input))

    banner("part 2")
    check(partTwo)(input, 1879071)
//    println("answer: " + partTwo(input))
}

private data class Image(
        private val pixels: BooleanArray
) {

    constructor(pixels: List<Boolean>) :
            this(pixels.toBooleanArray())

    constructor(first: Boolean, vararg rest: Boolean) :
            this(booleanArrayOf(first) + rest)

    constructor(grid: String) :
            this(grid
                    .filter { it == '.' || it == '#' }
                    .map { it == '#' }
                    .toBooleanArray())

    val dim = Math.sqrt(pixels.size.toDouble()).toInt()

    fun litCount() = pixels.count { it }

    fun get(r: Int, c: Int) = pixels[r * dim + c]

    fun split() =
            if (dim % 2 == 0)
                (0 until dim step 2).flatMap { r ->
                    (0 until dim step 2).map { c ->
                        Image(
                                get(r, c),
                                get(r, c + 1),
                                get(r + 1, c),
                                get(r + 1, c + 1)
                        )
                    }
                }
            else
                (0 until dim step 3).flatMap { r ->
                    (0 until dim step 3).map { c ->
                        Image(
                                get(r, c),
                                get(r, c + 1),
                                get(r, c + 2),
                                get(r + 1, c),
                                get(r + 1, c + 1),
                                get(r + 1, c + 2),
                                get(r + 2, c),
                                get(r + 2, c + 1),
                                get(r + 2, c + 2)
                        )
                    }
                }

    fun variations() =
            rotations() + flip().rotations()

    fun flip() =
            Image(
                    ((dim - 1) downTo 0).flatMap { r ->
                        (0 until dim).map { c ->
                            get(r, c)
                        }
                    }
            )

    fun rotations() =
            if (dim % 2 == 0)
                generateSequence(this, { p ->
                    Image(
                            p.get(1, 0),
                            p.get(0, 0),
                            p.get(1, 1),
                            p.get(0, 1)
                    )
                })
                        .take(4)
                        .toList()
            else
                generateSequence(this, { p ->
                    Image(
                            p.get(2, 0),
                            p.get(1, 0),
                            p.get(0, 0),
                            p.get(2, 1),
                            p.get(1, 1),
                            p.get(0, 1),
                            p.get(2, 2),
                            p.get(1, 2),
                            p.get(0, 2)
                    )
                })
                        .take(4)
                        .toList()

    override fun toString() =
            (0 until dim).map { r ->
                (0 until dim).map { c ->
                    if (get(r, c)) '#' else '.'
                }.joinToString("")
            }.joinToString("/")

    fun toGridString() =
            toString().replace('/', '\n')

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Image) return false

        if (!Arrays.equals(pixels, other.pixels)) return false

        return true
    }

    override fun hashCode(): Int {
        return Arrays.hashCode(pixels)
    }

}

private fun List<Image>.stitch(): Image {
    val gdim = Math.sqrt(size.toDouble()).toInt()
    val pdim = first().dim
    return Image((0 until gdim).flatMap { gr ->
        (0 until pdim).flatMap { pr ->
            (0 until gdim).flatMap { gc ->
                (0 until pdim).map { pc ->
                    get(gr * gdim + gc).get(pr, pc)
                }
            }
        }
    })
}

private val initial = Image(".#./..#/###")

private fun partAny(iterations: Int) = { input: String ->
    val rulebook = parse(input)
    generateSequence(initial, {
        it.split().map { p ->
            p.variations().map {
                rulebook[it]
            }
                    .filter { it != null }
                    .first()!!
        }
                .stitch()
    })
            .drop(iterations)
            .first()
            .litCount()
}

private fun parse(input: String): Map<Image, Image> {
    return input.trim().split("\n")
            .map {
                val (src, dest) = it.split("=>")
                        .map { Image(it) }
                Pair(src, dest)
            }
            .toMap()
}

private val partZero = partAny(2)
private val partOne = partAny(5)
private val partTwo = partAny(18)
