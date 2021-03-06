package aoc2021.day20

import aoc2021.Coord
import aoc2021.Grid2d

fun parse(lines: List<String>): Grid2d<Int> {
    val grid = Grid2d<Int>()
    lines.forEachIndexed { y, l ->
        l.forEachIndexed { x, c ->
            grid[Coord(x, y)] = if (c == '#') 1 else 0
        }
    }
    return grid
}

fun squareNumber(grid: Grid2d<Int>, c: Coord, algo: String, step: Int): Int {
    fun v(c: Coord) = grid[c] ?: if (algo[0] == '#' && step % 2 == 1) 0 else 1

    val bStr = "${v(c.sw())}${v(c.down())}${v(c.se())}${v(c.left())}${v(c)}${v(c.right())}${v(c.nw())}${v(c.up())}${v(c.ne())}"
    val index = bStr.toInt(2)
    val charAtPos = algo.drop(index).take(1)
    return if (charAtPos == "#") 1 else 0
}

fun enhanceStep(grid: Grid2d<Int>, algo: String, step: Int): Grid2d<Int> {
    val newGrid = Grid2d<Int>()
    val (xr, yr) = grid.dimensionRanges()
    (yr.first-1 .. yr.last+1).forEach { y ->
        (xr.first-1..yr.last+1).forEach { x ->
            newGrid[Coord(x, y)] = squareNumber(grid, Coord(x, y), algo, step)
        }
    }
    return newGrid
}

fun main() {
    Pair(actualData1, actualData2).let { data ->
        val grid = parse(data.second)
        val after1 = enhanceStep(grid, data.first, 1)
        println(enhanceStep(after1, data.first, 2).getEntries { it.value == 1 }.size) // 5622
    }

    Pair(actualData1, actualData2).let { data ->
        val grid = parse(data.second)
        val result = (1..50).fold(grid) { acc, i -> enhanceStep(acc, data.first, i) }
        println(result.getEntries { it.value == 1 }.size) // 20395
    }
}

private val testData1 = "..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..###..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###.######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#..#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#......#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#.....####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#.......##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#"
private val testData2 = """
    #..#.
    #....
    ##..#
    ..#..
    ..###
""".trimIndent().lines()

private val actualData1 = "#.#..##..##....#.####.#...##..###..#.#.##..##....###..#.##.#.#.#......##...#..##...#####.##..##...##..#.##.##..###.##.##...##....###.##.#...#.#.##..#..###.#.##.#.##.####.###.#..#######.##..##..#.##..#####.#..###.####.##....####.#....#...#..#..#....#..#...####.....#.##.###.##.##.###..###.##.###...#.##..#.###.##..##..#.##...##....##.#...#..#...#.##.#..#.###...#.#.##...#..#......#.#...#######.###.##.####.#.#.#.#.#.#.#######....##.##.##..##.##....##....##.##..####..#.#.##...###.##...#..##...#####.#.#.##.#.####."

private val actualData2 = """
    ##.##.#.#..##...####..##...#....#....##.##..##..##..##.#..###.....#########...#..##.###....#...##...
    #...#.#####.#.##.#.#####...#.#####..#.##.##..##.....#..#.......#.###..#..#..#####..#.##.##.#...#..##
    ###...###..###.#.####....#######..#..#..#..###..#.######...##.#..#..#..##.#....#.....####.#...##....
    ##.#..###...#..#.#######.#.##..####.###.###...###.#...#.#...######......#.#.#...#.##..........#..#.#
    .....#...###.##..##.#.########.##.#..#..###..#..##...##.###.#.#..#######..##.###.#..#..##.##.#...###
    .#.#.###....###...##.#...##...#.#.#...#.#.##..##.#..#.##.#..####.###...##....###..#......###.......#
    .###.#.##.#.#####..##.###.##....#.##.####...#.#..###.##..####...#..#####.......#.###.###.##....#..#.
    ##...#.##..##...###.#......###....####..###.......###..####.##.####.##.....#.#.##.###.######..#.....
    ###..#...#.#.#...#..#...##..###.#..###.####.#..#..##..##.###.####..##...##..###........#...###..##..
    ...#...#..#..#..######..##..##.#..##...#.#.#...##.#.##.####..#..##.##..#.###.#.#.##.#.##..###.###..#
    #...#...#..#######...##.#....##.##...##....#.##....#.#.....#.#..#..#.#..##.#........##.#.##.....#.#.
    ###.#.....#######.#...####..##...#.#....####.#....#.######.#..###......#.#....##..####.##..#.#.##.##
    ..#..#...##.##.##...#...####....##.#.####...##...#...#..#....#.#.#####....#.#.##......##..#..#...###
    ###..###..#.##...#.####.#..#..#...#.##.######.#.##.##.#######.#...#.####.###...#.#............##..##
    #.####...#..#.##.#....######.####.##........#.#..#...#..##.....#..#...#..###...###.#..#.#.##..##.#.#
    ##.##..##.###.#...#.##.....#.#..##...####...#.#..###..###...#....######..##...#.##...#...#...#...#.#
    ##.#.....##...#.##..#...#.####....#....##.##.#.#.######..##.#.##.####.#.#..#.#.##.#.#...#.....###.##
    ..##...###..###.......#.....#.#..#.#######..##....##...##...#.#.#.#...##.####.....#.##..###..##.....
    #.#..######.####.####.##.##.###.####..#....####..#.#######......##...#...####.##.#######..###..####.
    ##.###...##.##....######.#.##.#...###.##.##.#......###.#..###...#...........#.......###.####.##..#..
    ##.#......#.#.....##.##.###.#####.######..#.#...##.#.#.#..#.#...#..#.#.#.####.##.###.##.######.#.#..
    .#..#.##.###.####.##.##..##...####..#.##.##.##...##...##.####.#####.#..##.#...##..#..##.#..#.#..#.##
    #..###.#.#######.###.......#####.#.#.###.#.##.##.####.#####...#..#....#.#....#...##......#.#..#....#
    .#..###.....##..###...##...####..#..##.##.#..#.#.#..####..#.......##.##...#.###..#...#....#........#
    #.#.#.#...#.###.........###....#...##.##.##..#..#...####...##.###...#.##.##..##....##.#...####.#.###
    ..#.#..#.#.......#.#....####.##.#.#........#.#.##..###.#..#.##..##..##.###.#....#.###.#.##.####.#..#
    ....#.###..#.#######...###.#..#..##.#.#....##.#.#.#.....###.#..###......#....#######......#...#.#.#.
    #.#.#.#...#.#...#######...###.###..##.#...####...#####...#..##......#..##.#........#.#..##.#.###....
    #...##.#..##..#.#..#.#.....######..##...#.##.#.##.##.###..#.##.#..##.###.####...###...#.###..##.#.#.
    #.#.##..#..#..##.....#####........#.##..#.#.#.#.#.#.#.#..#####.#.#.....###..#.###..#.##.##.#######.#
    .##.#.#.##.##.#..#.###..#..##..###..#..#..#.###...###.#.#....##..#.##...##..#..##.#.#.#.#.#####.##.#
    #.#.......###.#...##.##.##.......##.###.#..###.#..#....#.#.####....#.###.##...##.#...##...#..###.##.
    #..###.#.##.#..#####..##.#.########.#.###.####....##.###.##.#..######...#.#..#..###.#...#.#...##...#
    .#.#.#..#.##..####...#.##.#..#.#........##.#.#....##.##.#.....###.#.#.#...#.#####..#.#...##.#..####.
    #.##...########.##.......##.#.####.#...#.##.#######..#...........#.#.###..###...#.####.##.#####..###
    ...#..##.###.#.#.#...###..###....#..#.##..##.####..#.###..##...#..##.....###.##.##.#.####.####..#.#.
    .#.#.#.....##.#.###...###..#.#...###.#.....##....#.#.###..#....##.....#.#...#.#.##..#..#...#.#.#.#.#
    #.#.#.#######.#.##..######...##########.#.##.#.###.##.#..#..#....##......#...##.##..###.##.####.#...
    #..##..##.##....#..###..##.....##....##.#.#...#....##...#..#.#..#...#..####....#.##.....#.##.#......
    ....##..##.#.#........#.#.#...##.##......####.....#.####.#.##....#....#.###.#....#.####.####....#.##
    ...###.#..#..###.####...#.#...#####..#...##..#.#..#..#.####....#...#####.###.##.#####....###.##.####
    ###..##.....##.#.#.#.#.#.#.#.###..##..#..........###..#.###.####...##..#.#.#.#.##.#..###....#.####..
    ...#...#..#.............###.##..##.###.#.######...#.###.##..##.#######...###.###.#..##.####.#..#.###
    #..##.##...###..#####.####..##..#.#.#..##..##..#.....#########.##.#.###...##......#...#....#.###...#
    #..##.#.###..#...###.#.#...#..###.###..##.##.#.#.#....###.####.#...###..##.#...#.##.###.#..##....###
    #.#...####.##.####.#.##...##.##..#.##.#.#..##..##.....#.#.#####.#.##..##..#.##.####.#..##..#.##.##.#
    ..##.##...###.#.......##.......##.#..##.#.#.#.#..####.##..##..#####.#..#..#.####.#.##.#.###..#...###
    ##.##.###...#......##.######.#...#....#.#####.##.#.####.##.##.##.#.#...######....##.##.###..##.###..
    ##.#########..#.##..#..####..#.####.#.#.##..#.##.##.###.####..##..#..#.####..#.#.....#.....#.###.###
    ...###.#.##.##.##.##...#.#..#.####..###.....#...##.###.###..##.#.#..##.######.....#.#.###.##.##..###
    #..#####.#.#....##.#.##..###.#..###...#...####.#####..#.##..#.##..###....#..##.##.###...#.#...#.....
    #...#.#..#.######..##......#.####.###....##.#.##..#..#####....#..###.#.#####...###.#..##.##.....#...
    ##..##.###..#.#..##.##...#######..#...#..#....#.##...##.##.###.#..##.#..##...#.###.....##.#.#.#.####
    ##...##.###..#...#..#..#...#..##.##...###..##..#..####...#.##......##.##...#.#######.####.###....##.
    ...#..##.###...##....#######.###...##..##.##.#.##...#..###.#.##.#..#.#.##..###.##....#.....#.#.#.#..
    ..#..##..#..##.##.#.##..##.####...#.#####..####.#.##.#.#..#......##.#.##.....#.#..##..#.###.#...###.
    #.....#..#...#.#.#.##########....##.####...###......#.#..#..#........#.###..#.####.####.#####.##.###
    .##..#.###.##..##..######.##..###...#.##...#....#....####.###.#.###...#.###..#......#.##.###....#...
    .#..#.##.##..##.###.#..#.#.##.#..##.###.###.#....#.######...#.....###.##..###.####..#.##..#.####.###
    #.##.##.#.#.........#.....#.##..#.##..#.##..#.#..##.##.#..##..##########....#.##.#...#.####.#.#.#.##
    .#.....###.##.#..##...#.#..##.##.#.#.#..####..#####......###..#####..###...###...##.##..#.#####..#.#
    .##..#.###..#.#.###..###..##.#...###.####.######......#..##.#.####.##.#.#....#......#..#..###.##..##
    ###..##....#.#######.###.###.##.###..##...##.#..#.#..#....#.#.####..##..##.#####.#.#####.#...##..#.#
    .##.....##..##.#.#.#..###.#..###.##.##..###.##.####...#.##..#..#.#.#.##.###.#..###....#.###.##.#.#..
    ####.##.###.#.##..#.###.##.#.....#.###.#.####..#..#.####.....###.#.####.#..#...###....###...##.#.#.#
    #.##..#.......###.##.###.##..#..####.###.#..#####.#..##.###.#..###.####....#..#...#.####.#.##....#..
    #....#.#....#.#..###.##..###..##..#####.##...#.#....###..##.#....#####.#....###.#..##.#.....####.#.#
    ##.###.#.##.#.....###.#.......##.###....#.#.##....##.#.##.#.#...#.#.##......#.#.##.#...#...#......##
    .##.#..#.###.###..#..##.#..##.###.....#....#####..#.#..#####....####...##.##.#.####.#....##.#####.#.
    #...##....#####.#.##.##.#####.#.#.##....#.#.##.#....#.....###.#....##..###.####..#..#...#...#...#..#
    ..#..##....##.#.##.#.###..####.#..##....#.##.#..#######...#.##.#....##...###..#..#.#.#..#.##.#.#..#.
    ##..###....#...####.##.###.####.###...##..####..###..##.#.##.....#####...#.#.##...#.#..######.#....#
    .#...######..........#......#.##.#...##..#####...###..###..#...##.#..##.##..#..##.....###.##.##.###.
    .#..##.###.##...#..#.####..#....#.#.#.#.####.##..#.#..#..###..##.#..##..###......##..#.#.##.#.##.###
    .#.#.#..#####..#...#######..####..##.####.#.#####.#...####.####.##.##.#.#...#.....###.##..##.#.##...
    .#...######...###.##.#...#...#...##.#..#...#.#..#.#.##.#.#.#...###.####.##..##.##..###..#.#..#.#.#.#
    .###.#...#.##.###.########......#.###.###.#.###.....###.#..####..#..#.#.#####....#.#...#.#....###..#
    #.#.#...#....#####.##.#.###.##.#..#####.......#..#.##..#.#....###.#..#.##..#..#.#..######...#...###.
    .####.#.#.#..####.#####..###.#.##.##..#######.#.#.###..#.##.###.#....##.####.#.#.##.##.#.###....#..#
    #.#.##..#..##.####.#.#..####..#..#####.########..###.#.###....##...##.####..#......##.#.#.#.#..#...#
    ....#.....###..##...##.##..#####.##..###.#...##....##.#####..####.....####..#..####..###.##....#.###
    .....###..#......#....#.###....#.###.#.#.#.#.###.##.##...###.##.#####...#....#.##..##..##..#.#..##..
    #.##..#.#..#..##..#..###...#..#.######.##.##.#.####...##.#..#..##...##.#.###..###.####.##...#..#....
    ###...###..#..#####..###.#####....##..#..#...#.###.##..##..########.##....#...#..##......##..#.##...
    ..#.#..###..#......#.#...####.....#.#...######..#.....#..###.##..##.#...##.#..##.##..##..##.#####.##
    #.#..#......###..##.#####..#####...##.#.#####.######.#..##....##.##.#.#..#....#.#...##.#.###.#.##.#.
    .####...##.####....##..####.#..####..#.....###.###..#####..###.##.#.####.##.#..#.#.#..#..##..##.#.##
    #.####..#.###.#.#...#.###.##..#......######.#...#####..#.###..#..#####.#.#.##.#..#..##...#.....##.##
    ..#..##......#.#.##.####...#.######..####..##.###.##..#.#..#.########...#.#...###.##..###.#.####..##
    ..#......#..#..#.#.#.#####.#..###...#.#.#...#.##.##.##.##.#.##.#####.####.#..##.######..#.##..#...##
    ###.##.#..#.##.#..###..##.##.#..#.####..#....####.##.#.##...#.##......##.#...#....###.#.....####.###
    ..########.##..##.###..##.###.##..#.#.##.#.#....####.##.####.##.###....#.###.########.#.##.#.#######
    ..#.#.....#..#....#####.#.#..#...##.#.....#.....#..###.##..##.####..#.##...#####..####.....#..#..###
    ##...###.######.##.#..#....#.##.#..#..#.#####..#.######..#..##...##.#..#...##.#.#..#.##.#######...##
    ....#..#.#.#####.###.##.##.#.##..#.#....#.##.###...###.##..#.####..##.#...####.#....#....#.##...##..
    .####....####.##.#..#..####.#..#..##....##.#..##.##..##.###....##..##.........##.#.####......####.#.
    ...#.#.#..####...#.#.#.......#..##.#.#..#.#..##..####..#....###....#..###.##.#..#.###.###.###.##.#..
    ##.#..#.#.####.###.#.#.###.....###...#.###.#.##.#.#..#..#.#..###.....###.#.#...########...#..#.#..##
    ..#..##..#...#.#..##.#.#...#..#..#..###.##.##.#.#.#......#..###.#.....##..#.#.#.#.#...###.#..#.#.##.
    #.####.#..###.###.#....#.##..##.#.##..#.####....#.##.#...##.....#####..#.#..##..#.##..#......##..##.
""".trimIndent().lines()
