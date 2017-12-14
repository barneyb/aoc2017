package event2017.day11

import event2017.banner
import event2017.check
import java.io.File

fun main(args: Array<String>) {
    val input = File("input/2017/day11.txt").readText().trim()

    banner("part 1")
    val partOne = check(::stepsAway)
    partOne("ne,ne,ne", 3)
    partOne("ne,ne,sw,sw", 0)
    partOne("ne,ne,s,s", 2)
    partOne("se,sw,se,sw,sw", 3)
    partOne(input, 764)
    println("answer: " + stepsAway(input))

    banner("part 2")
    val partTwo = check(::maxStepsAway)
    partTwo("ne,ne,ne", 3)
    partTwo("ne,ne,sw,sw", 2)
    partTwo("ne,ne,s,s", 2)
    partTwo("se,sw,se,sw,sw", 3)
    partTwo(input, 1532)
    println("answer: " + maxStepsAway(input))
}

val combines = listOf(
        Pair(Pair("n", "se"), "ne"),
        Pair(Pair("ne", "s"), "se"),
        Pair(Pair("se", "sw"), "s"),
        Pair(Pair("s", "nw"), "sw"),
        Pair(Pair("sw", "n"), "nw"),
        Pair(Pair("nw", "ne"), "n")
)

val cancels = listOf(
        Pair("n", "s"),
        Pair("ne", "sw"),
        Pair("se", "nw"),
        Pair("s", "n"),
        Pair("sw", "ne"),
        Pair("nw", "se")
)

fun stepsAway(input: String) =
        stepsAwayInternal(input
                .split(","))

private fun stepsAwayInternal(steps:List<String>): Int {
    val groups = steps
            .groupBy { it }
            .mapValues { (_, v) -> v.size }
    val combined = combines.fold(groups, { gs, pp ->
        val p = pp.first
        val d = pp.second
        val max = Math.min(
                gs.getOrDefault(p.first, 0),
                gs.getOrDefault(p.second, 0)
        )
        if (max > 0)
            (gs + Pair(d, gs.getOrDefault(d, 0) + max))
                    .mapValues { (dir, count) ->
                        if (dir == p.first || dir == p.second) count - max else count
                    }
        else
            gs
    })
    val canceled = cancels.fold(combined, { gs, p ->
        val max = Math.min(
                gs.getOrDefault(p.first, 0),
                gs.getOrDefault(p.second, 0)
        )
        if (max > 0)
            gs.mapValues { (dir, count) ->
                if (dir == p.first || dir == p.second) count - max else count
            }
        else
            gs
    })
    return canceled.map { (_, count) -> count }.sum()
}

fun maxStepsAway(input: String): Int {
    val agg = input.split(",")
            .fold(Pair(listOf<String>(), 0), { (path, max), step ->
                val afterStep = path + step
                Pair(afterStep, Math.max(max, stepsAwayInternal(afterStep)))
            })
    return agg.second
}
