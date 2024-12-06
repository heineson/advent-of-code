package aoc2024.day6

import aoc2024.Coord
import aoc2024.Grid2d

enum class Dir { U,D,L,R }
data class Guard(val pos: Coord, val dir: Dir)

fun Guard.move(grid: Grid2d<Boolean>): Guard {
    val next = when (dir) {
        Dir.D -> pos.down()
        Dir.U -> pos.up()
        Dir.L -> pos.left()
        Dir.R -> pos.right()
    }

    if (grid[next] == null) {
        return this.copy(pos = next)
    }

    if (grid[next] == false) {
        return this.copy(pos = next)
    }

    // Turn 90 deg and move
    val nextDir = when (dir) {
        Dir.U -> Dir.L
        Dir.D -> Dir.R
        Dir.L -> Dir.D
        Dir.R -> Dir.U
    }
    return this.copy(dir = nextDir).move(grid)
}

fun main() {
    part1(actualInput) // 4722
    part2(actualInput) // 1602
}

fun part1(input: List<String>) {
    val grid = Grid2d<Boolean>()
    var guardPos = Coord(0,0)
    input.let { data ->
        data.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                if (char == '^') guardPos = Coord(x, y)
                grid[Coord(x, y)] = (char == '#')
            }
        }
    }
    var guard = Guard(guardPos, Dir.D)

    val visited = mutableSetOf(guardPos)
    val (xs, ys) = grid.dimensionRanges()
    while (guard.pos.x in xs && guard.pos.y in ys) {
        guard = guard.move(grid)
        visited.add(guard.pos)
    }
    println(visited.size - 1) // Last pos outside of grid
}

fun part2(input: List<String>) {
    val grid = Grid2d<Boolean>()
    var guardPos = Coord(0,0)
    input.let { data ->
        data.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                if (char == '^') guardPos = Coord(x, y)
                grid[Coord(x, y)] = (char == '#')
            }
        }
    }

    fun test(grid: Grid2d<Boolean>): Boolean {
        var guard = Guard(guardPos, Dir.D)
        val visited = mutableSetOf(guardPos)

        val (xs, ys) = grid.dimensionRanges()
        var steps = 0;
        while (guard.pos.x in xs && guard.pos.y in ys) {
            guard = guard.move(grid)
            visited.add(guard.pos)

            if (++steps > xs.last*ys.last) {
                return true
            }
        }
        return false
    }

    fun variant(grid: Grid2d<Boolean>, coord: Coord): Grid2d<Boolean> = grid.copy().also { it[coord] = true }

    println(grid.getCoords()
        .filter { coord -> grid[coord] == false }
        .count { coord -> test(variant(grid, coord)) })
}


val testInput = """
    ....#.....
    .........#
    ..........
    ..#.......
    .......#..
    ..........
    .#..^.....
    ........#.
    #.........
    ......#...
""".trimIndent().lines()

val actualInput = """
    ..#.....................#............#....#...........#...............................#.....#.....................................
    .................#............................#.............##..#...............................#....#..#.........#...............
    .............................................##.........................................#....................#...............#...#
    ...##....................#...............#.....#....................#......##..............#............................#....#....
    .................................#..#..............#...........#.............#..#........................#............#...........
    .................#.........................#...........................#........................#........................#........
    ...#....................................#....#.........#................#...............#.....#................#.....#............
    .............#.#..........................................................#...#..................#....#.......#.....#...#.........
    .....#........................#....##...#............................................................#............................
    ..................#...#.........................................#..................#.............................#.#........#.....
    ......#...............................#.#..........##..#...........#.........................#.......#....................#.......
    .................................#..............#...............#........#..........#.........#............#.........#.......#...#
    ..#..#............#...#.....................#...............#............#.#........................#........#...............#....
    ..............#..#.#........................#.........#...........##.......#..........#.....................#..............#...#..
    ..........................#.................##...................................#....#.........#.................................
    ..................#..#........#....#...............#..............#.......#.......#......................................#..#...#.
    ..........#.#.................#.........#...................#.......................##......#....................#.......#........
    ..........#.........#...#......................................................................#.....#............#........#......
    ...##.............#...........................................................#..#....................##.#...#.#..#.........#..#..
    .....#......#.............................#.......#...#...#...............#.....#............#........#..#.#......................
    ........#................#.........#.......................................................................#..........#...........
    .......#........................................#..........#...............................#..#...................................
    ..................##.......#.......#.....#............#.......#...#.........#....##...............#..#.............#..........#...
    .......#...........#...........#...........................#.............#.........#..#........................#.##...............
    .....................................................................#........#....#...........................................#..
    ....#...........#......#.........#....................................#.................................#......#..................
    ...#........................................#..#...............................#.....................#.#......#...................
    ..#.....#.................................#.................#....#............................................#......#............
    ...................................................##...........#.......#.............#.................#.......#.................
    ..........................................................##..............#....#....#.............#...............................
    ........................................................#.....................................#................................#..
    ..............##....#..#..........#...........#......#..#....#.......#................#............................#..#...#.......
    ..........................#....................#.................................................#................................
    #................................................#.....................#..#...............##........#...#...#.........#...........
    ...........#.................................#..#....................#.#.......#..........................#.......#.......#.......
    ..............................#......#......#.................#.................#.................#.#.............#..#.#..........
    .......................#....................#....................................#.....#.#.......#............................#...
    ...............................................................................................#.#................................
    #..................#.....#...............................#..............#...#.....................................................
    ..........................................................#.................................................#.....................
    ..........#.........................#.....................................................#.........#..........#................#.
    ........#..............#..#......................................#.........#............#.................................#.......
    .......#.........................................................#.........#................#...............................#.....
    ...........#............................................#...#.............................#.....^................................#
    ................#.............#...#..............................#..................................#.............................
    .........................................................................................................#........................
    .................................................#........#.......................#.#.....#......................#................
    .........#.........#...#....................................................#.....................................................
    ##.##..#...................................#......................................................................................
    .................#...........................................................................................#........#...........
    .........................................................................#........................#....#......................#...
    ..............#......................#........#..........................#.........#..............................................
    .......................#............................................................................##............................
    .....#.................#........#...#...............#.............................#......................#........................
    ....#...................................................................................#............##...........................
    ................#............................#............................................#.......................................
    .......................................................................................................#......#...................
    ......................................................................................#...#.......................................
    ...............................................#........#..........#............................#..............#.......#..........
    ..............#.....................#........#..#............................#...#..#.#...................................#.......
    ..........#...............................#............................#...........................................#..............
    ....#............#.#....#......................................................................#..................................
    .......................#.......................#................................#..............#.....................#.........#..
    ...........#.........................#.............#.#........#........#..........................................#...............
    .......#......................#....................#...............................#......................#...........#...........
    ............#.....................................................................................................................
    ....#.......#............................................................#.........#.............#..........#....#..........#.....
    .......#.........................................................................#..................#..#....#.......#.............
    ............................#....#.......#......#.......#.....................#.................#.................................
    .......................................................#.............#..........................#.................................
    ....................................#.....................................##......................#............................#..
    ..............#...............#....................#.......................#................#......................#..............
    ....................#...........................#...................................................................#.............
    ...#..........................#...........................#............................................................#..........
    ..............#.................#.............................#..#.......#.......................#.##.............................
    ....................#.....#...#..............................................#......#...........#.................#..#....#.......
    #......................................#.....................#..#..#.#...........#.............#.................................#
    .....................#...........................................................#..........#............##..........#............
    .......#..................................#...............#..........#....#...................................................#...
    .............#.................#...........................#..............#.................................................#.....
    ...............................#......................................##...................................#.#......#..........#..
    .................##....................#.................................................#......................#................#
    ........#....................................#..........#.........................#..................#......................#.....
    ...#....#...........................#..............................#.#..........................#.................................
    .#...................................#..#......#.................................................................#................
    .................................#...........#................#.#........................#....................................##..
    #..#.....##...............#................................................................#....#......#.........................#
    .#....#......................................................#.........................#.......................................#..
    .#..............................................#.................................................................#...............
    ...............#..........................................#...#...............................#.#..#.#........#...................
    ..............#................................#.......#..................................#.........................#........#....
    .......................##..................................................#......................................................
    ..............................#...............................##..................................................................
    #....................#...........................................................#.............................#..................
    #..................................................................#...#..........................................................
    #..#........#...................................#..#..........##..............#...................................................
    .......................#.....................#....................................................................................
    .....................#...........#.............................................................#........................#....#....
    ...............#............................................................................##...#..........................#.....
    ...#......................................#.......................................................................................
    ....#...........................#.........#.................................................#..........#...#........#....#........
    ......#....#.#............................#..........................................#........................................#...
    ...........#...#.............................................#......................#..................#.....................#....
    ...#..........#........#..........#.......#..............#.............................#.......................#................#.
    ...........#...............#...........................................................#..........................................
    ..#..........##.................................................#...#...............#.......................#...............##....
    .......................#..............#.............#......................................................#..................#...
    ..............#..#........##..........#...................................................#.............##....................#...
    ...................#..................................................#................#......#.....#.........#.#...#.............
    ..................#.................................................#.................................................#.........#.
    ......................................#.#........#.....#...#..........#..............#.........................#...#..............
    .................................#..............#..#.....................#................#.............#..#..#...................
    .........#.......#.#..............#..#...............#.................#..................................#..#....................
    .............................#....#.......##..................................................................................##..
    .....................................................................................#........#.......#......#....................
    ........#...........#..........#..........#............................#...............#........#.#...........................#...
    .#.................#...#..............#...#...................#........#...............#..........................................
    .........#............................................##........#......#.....................#.......................#............
    .......................................#..........#..#......#........#..........#..........................#....................#.
    ....#......#..........................#....................#.......................................#.....###.......#..............
    .......#..................#....#.......................................................................#..........................
    ....#........................#.............................................................#.......#..........................#...
    ......................#.................................#..........#................#..#.........................#................
    ...........#....................................................#..............#...........................#......................
    .....#................#........#...#....#........#............#...............#.......................#..#.........#..............
    #..................................................#...........................#....#...........#.................................
    ....#.#.....................................#.#...............#................#....................................#.............
    ............#....................#.#.#...............................................#...................##.......................
    ...##..............#........................#.............#.......#...........#.#.##..............................#....##.........
    ......#...........#....................................................#.........................##...............................
""".trimIndent().lines()
