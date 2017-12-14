package event2017

import java.io.File

/**
 *
 *
 * @author barneyb
 */
fun main(args: Array<String>) {
    val input = File("input/2017/day07.txt").readText().trim()
    val exampleInput = "pbga (66)\n" +
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
            "cntj (57)"

    banner("part one (cancel)")
    val partOneCancel = check(::nameOfRootCancel)
    partOneCancel(exampleInput, "tknk")
    partOneCancel(input, "hlhomy")
    println("answer: " + nameOfRootCancel(input))

    banner("part one (parse/cancel)")
    val partOneParseCancel = check(::partOneParseCancel)
    partOneParseCancel(exampleInput, "tknk")
    partOneParseCancel(input, "hlhomy")
    println("answer: " + partOneParseCancel(input))

    banner("part one (tree)")
    val partOne = check(::nameOfRootTree)
    partOne(exampleInput, "tknk")
    partOne(input, "hlhomy")
    println("answer: " + nameOfRootTree(input))

    banner("part two")
    val partTwo = check(::correctedWeight)
    partTwo(exampleInput, 60)
    partTwo(input, 1505)
    println("answer: " + correctedWeight(input))
}

private fun nameOfRootCancel(input: String): String {
    val lines = input.trim().split("\n")
    val names = mutableSetOf<String>()
    lines.forEach {
        names.add(it.split(" ").first())
    }
    lines.filter { it.contains("->") }
            .forEach {
                val (_, kids) =it.split(">")
                names.removeAll(kids.split(",")
                        .map { it.trim() })
            }
    return names.first()
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

private fun partOneParseCancel(input: String): String {
    val progs = parse(input)
    val names = mutableSetOf<String>()
    progs.forEach {
        names.add(it.name)
    }
    progs.forEach {
        names.removeAll(it.kids)
    }
    return names.first()
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
            } + Pair(p.name, n)
        }), branches)
    }).dropWhile {
        it.second.isNotEmpty()
    }.first().first.values.first()
}

private fun nameOfRootTree(input: String): String {
    return parse(input).toNode().name
}

private fun correctedWeight(input: String): Int {
    val root = parse(input).toNode()
    val weightMap = mutableMapOf<Node, Int>()
    fun walk(n: Node, depth:Int = 0) {
        n.kids.forEach { walk(it, depth + 1) }
        val kidWeight = if (n.kids.isEmpty()) 0 else n.kids.map { weightMap[it]!! }.sum()
        weightMap.put(n, n.weight + kidWeight)
    }
    walk(root)
    val (weird, others) = weightMap
            .filter { (n, _) ->
                n.kids.isNotEmpty() && n.kids
                        .map {
                            weightMap[it]!!
                        }
                        .distinct()
                        .size != 1
            }
            .keys
            .first()    // we can do this because the tree traversal is
                        // depth-first, the map maintains insertion order, and
                        // we want the leafiest node
            .kids
            .map { Pair(weightMap[it]!!, it) }
            .groupBy { it.first }
            .map { (w, ns) ->
                Pair(w, ns.map { it.second })
            }
            .sortedBy {
                it.second.size
            }
    return weird.second.first().weight + (others.first - weird.first)
}
