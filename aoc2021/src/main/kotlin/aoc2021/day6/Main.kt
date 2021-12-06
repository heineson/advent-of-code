package aoc2021.day6

fun cycle(groups: Map<Int, Long>): Map<Int, Long> {
    val result = mutableMapOf<Int, Long>()
    groups.entries.forEach { (key, count) ->
        when (key) {
            0 -> {
                result[6] = result.getOrDefault(6, 0) + count
                result[8] = result.getOrDefault(8, 0) + count
            }
            else -> result[key - 1] = result.getOrDefault(key - 1, 0) + count
        }
    }
    return result
}

fun main() {
    var current = actualData.groupingBy { it }.eachCount().mapValues { it.value.toLong() }
    (1..80).map {
        current = cycle(current)
    }
    println("After 80 days: ${current.values.sum()}") // 350149
    println("\nPart2:\n")

    current = actualData.groupingBy { it }.eachCount().mapValues { it.value.toLong() }
    (1..256).map {
        current = cycle(current)
    }
    println("After 256 days: ${current.values.sum()}") // 1590327954513
}

val testData = "3,4,3,1,2".split(",").map { it.toInt() }
val actualData = "4,1,4,1,3,3,1,4,3,3,2,1,1,3,5,1,3,5,2,5,1,5,5,1,3,2,5,3,1,3,4,2,3,2,3,3,2,1,5,4,1,1,1,2,1,4,4,4,2,1,2,1,5,1,5,1,2,1,4,4,5,3,3,4,1,4,4,2,1,4,4,3,5,2,5,4,1,5,1,1,1,4,5,3,4,3,4,2,2,2,2,4,5,3,5,2,4,2,3,4,1,4,4,1,4,5,3,4,2,2,2,4,3,3,3,3,4,2,1,2,5,5,3,2,3,5,5,5,4,4,5,5,4,3,4,1,5,1,3,4,4,1,3,1,3,1,1,2,4,5,3,1,2,4,3,3,5,4,4,5,4,1,3,1,1,4,4,4,4,3,4,3,1,4,5,1,2,4,3,5,1,1,2,1,1,5,4,2,1,5,4,5,2,4,4,1,5,2,2,5,3,3,2,3,1,5,5,5,4,3,1,1,5,1,4,5,2,1,3,1,2,4,4,1,1,2,5,3,1,5,2,4,5,1,2,3,1,2,2,1,2,2,1,4,1,3,4,2,1,1,5,4,1,5,4,4,3,1,3,3,1,1,3,3,4,2,3,4,2,3,1,4,1,5,3,1,1,5,3,2,3,5,1,3,1,1,3,5,1,5,1,1,3,1,1,1,1,3,3,1".split(",").map { it.toInt() }
