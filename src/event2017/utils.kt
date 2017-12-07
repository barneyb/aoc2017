package event2017

/**
 *
 *
 * @author barneyb
 */


fun banner(label:String) {
    println("= $label ".padEnd(80, '='))
}

fun <I, R> check(functionUnderTest:(I) -> R): (I, R) -> Unit {
    var run = 0
    return { input:I, expected:R ->
        run += 1
        print("${run.toString().padStart(3)}: ")
        val startedAt = System.currentTimeMillis()
        val actual = functionUnderTest(input)
        val elapsed = System.currentTimeMillis() - startedAt
        if (actual != expected) {
            var vi = "'" + input.toString() + "'"
            if (vi.length > 100) {
                vi = vi.substring(0, 90) + "...' (${vi.length - 90} chars truncated)"
            }
            print("FAIL: Expected '$expected' but got '$actual' (from ${vi.replace("\n", "\n    ")})")
        } else {
            print("PASS")
        }
        println(" (${"%,d".format(elapsed)} ms)")
    }
}

fun <T, R> Iterable<T>.scan(initial: R, operation: (acc: R, T) -> R): Iterable<R> {
    return this.fold(Pair(initial, mutableListOf(initial)), { a, it ->
        val next = operation(a.first, it)
        a.second.plusAssign(next)
        Pair(next, a.second)
    }).second
}
