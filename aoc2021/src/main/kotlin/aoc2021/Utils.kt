package aoc2021

import java.io.File

/****** Input data utils *******/

fun <T> parseLines(lines: List<String>, lineParser: (line: String) -> T): List<T> {
    return lines.map { lineParser(it) }
}

fun <T> readFile(filename: String = "./data.txt", lineParser: (line: String) -> T): List<T> {
    return parseLines(File(filename).readLines(), lineParser)
}

fun readLinesIntoTokens(lines: List<String>, groupSeparatorLine: String = "", tokenSeparator: String = " "): List<List<String>> {
    val groups = mutableListOf<List<String>>()
    var group = mutableListOf<String>()
    lines.forEachIndexed { i, s ->
        run {
            if (s == groupSeparatorLine) {
                groups.add(group)
                group = mutableListOf()
            } else {
                group.addAll(s.split(tokenSeparator))
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

/****** extensions *******/

fun <E> Iterable<E>.updateElement(index: Int, newElem: E) = mapIndexed { i, existing ->  if (i == index) newElem else existing }

fun <E> List<E>.getCircularIndex(index: Int) = if (index < 0) this.size - (-index % this.size) else index % this.size

fun <E> List<E>.circularGet(index: Int) = this[this.getCircularIndex(index)]

fun <E> List<E>.circularSubList(fromIndex: Int, toIndex: Int): List<E> {
    val realFrom = this.getCircularIndex(fromIndex)
    val realTo = this.getCircularIndex(toIndex - 1) + 1
    return if (realTo < realFrom) this.subList(realFrom, this.size) + this.subList(0, realTo)
    else this.subList(realFrom, realTo)
}
