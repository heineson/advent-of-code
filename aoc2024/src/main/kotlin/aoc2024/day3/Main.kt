package aoc2024.day3

import aoc2024.readFile

val actualInput: List<String> = readFile("aoc2024/src/main/kotlin/aoc2024/day3/data.txt") { it.trim() }

fun main() {
    val pattern = Regex("""mul\((\d+),(\d+)\)""")
    println(
        pattern.findAll(actualInput.joinToString(""))
            .sumOf { it.groupValues.stream().skip(1).map { v -> v.toInt() }.reduce(1, Int::times) }
    )

    part2()
}

fun part2() {
    val pattern = Regex("""mul\((\d+),(\d+)\)|do\(\)|don't\(\)""")
    val instructions = pattern.findAll(actualInput.joinToString("")).map { it.value }.toList()

    var enabled = true
    var count = 0
    for (i in instructions) {
        if (i == "do()") {
            enabled = true
        }
        if (i == "don't()") {
            enabled = false
        }
        if (enabled && i.startsWith("mul(")) {
            count += pattern.find(i)!!.groupValues.stream().skip(1).map { v -> v.toInt() }.reduce(1, Int::times)
        }
    }

    println(count)
}

val testInput = """
    xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))
""".trimIndent().lines()

val testInput2 = """
    xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))
""".trimIndent().lines()
