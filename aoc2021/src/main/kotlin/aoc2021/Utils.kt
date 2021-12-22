package aoc2021

import java.io.File
import kotlin.math.abs

/****** Input data utils *******/

fun <T> parseLines(lines: List<String>, lineParser: (line: String) -> T): List<T> {
    return lines.map { lineParser(it) }
}

fun <T> readFile(filename: String = "./data.txt", lineParser: (line: String) -> T): List<T> {
    return parseLines(File(filename).readLines(), lineParser)
}

fun readLinesIntoTokens(lines: List<String>, groupSeparatorLine: String = "", tokenSeparator: String = " ", tokenSeparatorRegex: Regex? = null, trimLines: Boolean = false): List<List<String>> {
    val groups = mutableListOf<List<String>>()
    var group = mutableListOf<String>()
    lines.forEachIndexed { i, s ->
        run {
            val line = if (trimLines) s.trim() else s
            if (line == groupSeparatorLine) {
                groups.add(group)
                group = mutableListOf()
            } else {
                if (tokenSeparatorRegex != null) {
                    group.addAll(line.split(tokenSeparatorRegex))
                } else {
                    group.addAll(line.split(tokenSeparator))
                }
                // In case final line is not a "separator line"
                if (i == lines.size - 1) {
                    groups.add(group)
                }
            }
        }
    }

    return groups
}

fun readFileIntoTokens(filename: String = "./data.txt", groupSeparatorLinePattern: String = "", tokenSeparator: String = " "): List<List<String>> {
    return readLinesIntoTokens(File(filename).readLines(), groupSeparatorLinePattern, tokenSeparator)
}

/****** collection extensions *******/

fun <E> Iterable<E>.updateElement(index: Int, newElem: E) = mapIndexed { i, existing ->  if (i == index) newElem else existing }

fun <E> List<E>.getCircularIndex(index: Int) = if (index < 0) this.size - (-index % this.size) else index % this.size

fun <E> List<E>.circularGet(index: Int) = this[this.getCircularIndex(index)]

// Returns a view of the portion of this list between the specified fromIndex (inclusive) and toIndex (exclusive).
fun <E> List<E>.circularSubList(fromIndex: Int, toIndex: Int): List<E> {
    val realFrom = this.getCircularIndex(fromIndex)
    val realTo = this.getCircularIndex(toIndex - 1) + 1
    return if (realTo < realFrom) this.subList(realFrom, this.size) + this.subList(0, realTo)
    else this.subList(realFrom, realTo)
}

fun <E> Map<E, Long>.inc(key: E, valueToAdd: Long): Map<E, Long> =
    if (key in keys) mapValues { e -> if (key == e.key) e.value + valueToAdd else e.value }
    else this + mapOf(key to valueToAdd)

fun <E> MutableMap<E, Long>.mutableInc(key: E, valueToAdd: Long) {
    this[key] = this.getOrDefault(key, 0) + valueToAdd
}

/****** Math utils ******/

// Greatest common divisor
tailrec fun gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)
fun gcd(ints: Collection<Int>): Int = ints.reduce { a, b -> gcd(a, b) }
fun List<Int>.gcd(): Int = gcd(this)

// Least common multiple
fun lcm(a: Int, b: Int): Int = abs(a * b) / gcd(a, b)
fun lcm(ints: Collection<Int>): Int = ints.reduce { a, b -> lcm(a, b) }
fun List<Int>.lcm(): Int = lcm(this)
