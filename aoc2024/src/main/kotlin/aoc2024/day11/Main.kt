package aoc2024.day11

fun main() {
    val input = actualInput.split(" ").map { it.toLong() }
    part1(input) // 186203
    part2(input) // 221291560078593
}

fun part1(input: List<Long>) {
    var current = input
    repeat(25) { current = blink(current) }
    println(current.size)
}

fun part2(input: List<Long>) {
    var current = input.groupingBy { it }.eachCount().mapValues { it.value.toLong() }
    repeat(75) { current = blink2(current) }
    println(current.values.sum())
}

fun blink(input: List<Long>): List<Long> {
    return input.flatMap { stone ->
        if (stone == 0L) {
            listOf(1L)
        } else if (stone.toString().length % 2 == 0) {
            val left = stone.toString().substring(0, stone.toString().length / 2).toLong()
            val right = stone.toString().substring(stone.toString().length / 2).toLong()
            listOf(left, right)
        } else {
            listOf(stone * 2024)
        }
    }
}

fun blink2(counts: Map<Long, Long>): Map<Long, Long> {
    val result = mutableMapOf<Long, Long>()

    counts.forEach { (stone, count) ->
        if (stone == 0L) {
            result[1L] = (result[1L] ?: 0L) + count
        } else if (stone.toString().length % 2 == 0) {
            val left = stone.toString().substring(0, stone.toString().length / 2).toLong()
            val right = stone.toString().substring(stone.toString().length / 2).toLong()
            result[left] = (result[left] ?: 0L) + count
            result[right] = (result[right] ?: 0L) + count
        } else {
            val newStone = stone * 2024
            result[newStone] = (result[newStone] ?: 0L) + count
        }
    }

    return result
}

const val testInput = "125 17"

const val actualInput = "4189 413 82070 61 655813 7478611 0 8"
