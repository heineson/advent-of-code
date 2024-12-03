package aoc2024.day3

import aoc2024.readFile

val pattern = Regex("""mul\((\d+),(\d+)\)""")
val actualInput: List<String> = readFile("aoc2024/src/main/kotlin/aoc2024/day3/data.txt") { it.trim() }

fun main() {
    println(
        pattern.findAll(actualInput.joinToString(""))
            .sumOf { it.groupValues.stream().skip(1).map { v -> v.toInt() }.reduce(1, Int::times) }
    )
}

val testInput = """
    xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))
""".trimIndent().lines()

