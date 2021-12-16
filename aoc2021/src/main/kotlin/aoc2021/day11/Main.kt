package aoc2021.day11

import aoc2021.Coord
import aoc2021.Grid2d

class OctoGrid : Grid2d<Int>() {
    override fun printElement(e: Int): Char {
        return e.toString()[0]
    }
}

fun step(grid: Grid2d<Int>): Long {
    val flashed = mutableSetOf<Coord>()

    grid.getEntries().forEach { (c, e) ->
        grid[c] = e + 1
    }
    while (grid.getEntries { (c, v) -> v > 9 && c !in flashed }.isNotEmpty()) {
        grid.getEntries().forEach { (c, e) ->
            if (e > 9 && c !in flashed) {
                flashed.add(c)
                grid.surroundingNeighborsWithinLimits(c) { it !in flashed }
                    .forEach { grid[it] = grid.getValue(it) + 1 }
            }
        }
    }

    flashed.forEach { c ->
        grid[c] = 0
    }

    return flashed.size.toLong()
}

fun main() {
    actualData.let { data ->
        val grid = OctoGrid()
        data.forEachIndexed { y, line ->
            line.forEachIndexed { x, char -> grid[Coord(x, y)] = char.toString().toInt() }
        }

        val count = (0..99).fold(0L) { acc, _ -> acc + step(grid) }
        println(grid)
        println(count) // 1785
    }
    println("\npart2\n")
    actualData.let { data ->
        val grid = OctoGrid()
        data.forEachIndexed { y, line ->
            line.forEachIndexed { x, char -> grid[Coord(x, y)] = char.toString().toInt() }
        }
        var steps = 1
        while (step(grid) != 100L) {
            steps++
        }
        println("All flash at step $steps") // 354
    }
}

private val testData = """
    5483143223
    2745854711
    5264556173
    6141336146
    6357385478
    4167524645
    2176841721
    6882881134
    4846848554
    5283751526
""".trimIndent().lines()

private val actualData = """
    5723573158
    3154748563
    4783514878
    3848142375
    3637724151
    8583172484
    7747444184
    1613367882
    6228614227
    4732225334
""".trimIndent().lines()
