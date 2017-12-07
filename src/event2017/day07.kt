package event2017

import java.io.File

/**
 *
 *
 * @author barneyb
 */
fun main(args: Array<String>) {
    val input = File("input/2017/day07.txt").readText().trim()

    banner("part one")
    val partOne = check(::nameOfRoot)
    partOne("pbga (66)\n" +
            "xhth (57)\n" +
            "ebii (61)\n" +
            "havc (66)\n" +
            "ktlj (57)\n" +
            "fwft (72) -> ktlj, cntj, xhth\n" +
            "qoyq (66)\n" +
            "padx (45) -> pbga, havc, qoyq\n" +
            "tknk (41) -> ugml, padx, fwft\n" +
            "jptl (61)\n" +
            "ugml (68) -> gyxo, ebii, jptl\n" +
            "gyxo (61)\n" +
            "cntj (57)", "tknk")
    partOne(input, "hlhomy")
    println("answer: " + nameOfRoot(input))
}

private data class Program(
        val name: String,
        val weight: Int,
        val kids: List<String>
)

private fun parse(input: String): List<Program> {
    return input.split('\n')
            .map { line ->
                val (a, b) = line.split(")")
                val (name, weight) = a.split("(")
                val kids = if (b.trim().length > 0) {
                    val (_, kids) = b.split(">")
                     kids.split(",").map {
                         it.trim()
                     }
                } else {
                    listOf<String>()
                }
                val p = Program(name.trim(), weight.toInt(), kids)
                p
            }
}

private data class Node(
        val name: String,
        val weight: Int,
        val kids: List<Node>
)

private fun List<Program>.toNode(): Node {
    return generateSequence(Pair(mapOf<String, Node>(), this), { (nameMap, remaining) ->
        val (leaves, branches) = remaining.partition {
            it.kids.all {
                nameMap.containsKey(it)
            }
        }
        Pair(leaves.fold(nameMap, { nm, p ->
            val n = Node(
                    p.name,
                    p.weight,
                    p.kids.map {
                        nameMap.get(it)!!
                    }
            )
            nm.filterKeys{ k ->
                ! p.kids.contains(k)
            }.plus(Pair(p.name, n))
        }), branches)
    }).dropWhile {
        it.second.isNotEmpty()
    }.first().first.values.first()
}

private fun nameOfRoot(input: String): String {
    return parse(input).toNode().name
}
