package aoc2024.day16

import aoc2024.Coord
import aoc2024.Grid2d

enum class Direction {
    NORTH,
    EAST,
    SOUTH,
    WEST;
}

fun main() {
//    testData.let { data ->
//        val grid = Grid2d(data) { it }
//        println("Part 1: ${part1(grid)}")
//    }
    actualData.let { data ->
        val grid = Grid2d(data) { it }
        println("Part 1: ${part1(grid)}") // 115500
    }
}

fun part1(grid: Grid2d<Char>): Int {
    val start = grid.getCoords().first { grid[it] == 'S' }

    data class State(val position: Coord, val direction: Direction, val cost: Int)

    val queue: ArrayDeque<State> = ArrayDeque()
    val visited = mutableMapOf<Coord, Int>()
    queue.add(State(start, Direction.EAST, 0))

    var currentLowest = Int.MAX_VALUE

    while (queue.isNotEmpty()) {
        val (currentPos, currentDir, currentCost) = queue.removeFirst()

        if (currentCost >= currentLowest) continue

        if (grid.getValue(currentPos) == 'E') {
            currentLowest = minOf(currentLowest, currentCost)
            continue
        }

        if (currentPos in visited.keys && visited[currentPos]!! <= currentCost) continue
        visited[currentPos] = currentCost

        for (neighbor in grid.cardinalNeighborsWithinLimits(currentPos) { grid[it] != '#' }) {
            val nextDir = turn(currentPos, neighbor, currentDir) ?: currentDir
            val nextCost = if (nextDir != currentDir) currentCost + 1001 else currentCost + 1
            queue.add(State(neighbor, nextDir, nextCost))
        }
    }

    val g = grid.copy()
    visited.keys.forEach { g[it] = 'x'}
    println(g)

    return currentLowest
}

fun turn(currentPos: Coord, nextPos: Coord, currentDir: Direction): Direction? {
    if (currentDir == Direction.NORTH || currentDir == Direction.SOUTH) {
        if (nextPos.x == currentPos.x) return null
        return if (nextPos.x < currentPos.x) Direction.WEST else Direction.EAST
    }

    if (nextPos.y == currentPos.y) return null
    return if (nextPos.y < currentPos.y) Direction.NORTH else Direction.SOUTH
}

val testData = """
    #################
    #...#...#...#..E#
    #.#.#.#.#.#.#.#.#
    #.#.#.#...#...#.#
    #.#.#.#.###.#.#.#
    #...#.#.#.....#.#
    #.#.#.#.#.#####.#
    #.#...#.#.#.....#
    #.#.#####.#.###.#
    #.#.#.......#...#
    #.#.###.#####.###
    #.#.#...#.....#.#
    #.#.#.#####.###.#
    #.#.#.........#.#
    #.#.#.#########.#
    #S#.............#
    #################
""".trimIndent().lines()

val actualData = """
    #############################################################################################################################################
    #.#.........#.#.....#.....#...#...................#.........#.......#.....#.......#.........#...#.............#.............#.........#...#E#
    #.#.#.#####.#.#.#.###.#.#.#.#.###.###.#########.#.#######.#.###.###.#.###.#.###.#.#.#.#####.#.#.#.#####.###.###.#########.#.#.#.#####.#.#.#.#
    #.#.#.....#...#.#.....#.#...#.......#...#...#...#.#.......#.....#.#...#.#.#.#...#.#.#.....#...#.#.#.....................#.#.#.#.#...#.#.#...#
    #.#.#####.###.#.#######.#####.###.#.###.#.#.#.###.#.#############.#####.#.#.#.###.#######.#######.#.###.#.#.#.#######.#.#.###.#.#.#.#.#.###.#
    #...#.....#.......#...#.......#...#.#...#.#...#.#.#.#...#.....#.....#...............#...#.#.......#...#.#.#.#.....#...#.#.....#.#.#.#.#...#.#
    #.###.#.###.#.#.###.#.#######.#.#.#.#.###.#####.#.#.#.#.###.#.#.#.#.###.#.###.#.###.#.#.#.#.#####.#####.#.###.###.#.#.#########.#.#.#.###.###
    #.#.#.#.#.....#.....#...#...#.#.....#...#.....#.#.#.#.#...#.#.#...#.....#.....#...#...#.#.#...#.........#...........#.#...#.....#.#.#...#...#
    #.#.#.###.###.#.#######.#.#.#.#.#.#########.#.#.#.#.#.###.#.#.###.#####.#######.#.#####.#.###.###.#####.#####.###.#####.#.#.#####.#.###.#.#.#
    #...#.....#...#.....#.#...#...#.#.............#.#...#.#.....#.....#.....#...#...#...#...#...#.#...#...#.....#...#.....................#.#.#.#
    ###.#######.#.#.###.#.###.###.#.###.#######.###.#####.###########.###.#.#.#.#####.#.#.#####.#.#.#.#.#.#.###.#.#.###.#.#.#######.#.#.###.###.#
    #...........#.......#...#.....#.....#.....................#.........#.#...#.....#.#.#.....#.#...#...#.....#...#...#.#...#.#.....#.#.....#...#
    #.#.#.###############.#.#.#.#########.###.#.#####.###.#.#.#.###.#.#.#.#########.#.#######.#.#.###.#######.###.###.#.#####.#.#.###.#######.#.#
    #...#.#.......#...#.#.#...#.......#...#.#.........#...#.#.#...................#.#.......#.#...#...#.......#.....#.#.#...#...#...#.#.......#.#
    #.###.#.#.###.#.#.#.#.###.#######.#.###.###.#######.###.#.#.#.###.###.#####.#.#.#####.###.#.###.#.###.###.#.#.#.#.#.#.#.###.###.#.#.#.###.#.#
    #...#.#.#.#.#.#...#.#.#.....#.#...#...#...#...#...#.....#.#.#...#.........#.#.#.......#...#.#.#...#...#...#.#.#...#...#...#...#...#...#...#.#
    #####.###.#.#.#.#.#.#.#.#.#.#.#.#.###.#.#.###.#.#.#.#####.#.###.#########.#.#########.#.###.#.###.#.#######.#.###.#.#####.#####.#####.#.###.#
    #.....#...#.#...#...#.#.#.#...#.#...#...#.....#.#.#.....#.#...#.....#.....#.........#.#.#.....#...#.#.......#.......#.....#...#.........#...#
    #.#####.###.###.#####.###.###.#.#####.#######.#.#.#######.###.#####.#.#######.#.#####.#.#####.#.###.#.###.###.#######.#####.#.#######.###.###
    #.#.#...#.....#.#...#...#.....#...............#.#.......#.#.#.#.....#.........#...#...#.....#.#.#...#.#.....#.#.....#.......#.#.......#.#.#.#
    #.#.#.#.###.#.#.#.#.###.#.#.#.#####.#########.#.#######.#.#.#.#.###############.#.#.#######.###.###.#.#.###.#.#.###.#########.#.#####.#.#.#.#
    #.#...#...#.#.#.#.#...#...#.#...#...........#.#...#...#.#.#...#.........#.......#...#.....#.........#...#...#.#...#.#.....#...#.....#.#.#.#.#
    #.#######.#.###.#.###.#####.###.#.#########.#.###.#.#.#.#.#####.#######.#.#############.#.#######.#.#####.###.###.#.#.#.#.#.#.#####.#.#.#.#.#
    #...#...............#...........#.#.#.......#...#.#.#.#...#...#...#...#...#.......#.....#.....#...#...#...#.#.#...#...#.#.#.#...#...#.#.....#
    #.#.#.#######.#####.#############.#.#.###########.#.#.#####.#.###.#.#.#####.#.###.###.###.#####.#.###.#.###.#.#########.#.#.#####.###.#######
    #.#.#.#.....#.....#.#.......#.......#.#...........#.#...........#.#.#.....#.#...#...#.#...#...#.#...#.#.#.#...#...#...#.#.#.........#.#.....#
    #.#.#.#.#.#.#.#####.#.###.#.#.#######.#.#####.#####.###########.#.#####.#.###.#.###.#.#####.#.#.###.#.#.#.#.###.#.#.#.#.#.###########.#.#.#.#
    #.#.#...#...#.#.....#.#...#.#.#.#.....#...#.#.#.........#.......#.......#.#.......#.#...#...#.....#.#...#...#...#...#...#...#...#...#.....#.#
    #.#.#.###.#.###.#######.###.#.#.#.#######.#.#.#.#########.#.#############.#.###.###.###.#.#########.###.#.###.#####.#######.#.#.#.#.#.###.#.#
    #...#...#.#.#...#...#...#...#...#...#.#...#.#...#...#.....#.#.........#...#...#.....#...#.#.........#...#.....#...#.#.....#.#.#.#.#.#...#...#
    ###.###.#.###.###.#.#.#########.###.#.#.###.#####.###.#.#####.#######.###.#.#.#.#####.###.#.#.#.###.#.###.#####.#.###.###.#.###.#.#.#####.###
    #...#...#.........#...#.....#.....#.#.#...#.......#...#.#...#...#...#...#.#.#.....#...#...#.#.......#.#...#.....#.....#...#.....#.#.....#...#
    #.#######.#######.#####.###.#.#####.#.#.#.#.#####.#.#####.#.###.#.#.###.#.#.#.###.#.###.###.#########.#####.#############.#####.#.#####.#.#.#
    #.............#.#.#.....#.#.#.......#...#.#...#.#.#.......#...#...#.#...#.#.#...#.#.#.....#.#.......#.#.....#...#...#...#.#.....#...#...#.#.#
    #####.#######.#.#.#.#####.#.#.#.#####.###.###.#.#.#########.#####.###.###.#.#.#.#.#.#.#####.#.#####.#.#.#.#.#.#.#.#.#.#.#.#########.#.###.#.#
    #...#.....#...#...#...........#.#...#.....#...#...#...#...#.......#...#...#.#.......#.#.#...#.#.#...#.#.#...#.#...#.#.#.#.....#...#.#...#...#
    #.#.#######.#.#####.###.#####.#.#.###.#####.###.#.#.#.###.#########.###.###.#########.#.#.###.#.#.###.#.#####.#####.#.#.#####.#.#.#.###.#.###
    #.#...#.....#.#.....#...#...#.#.#.....#.....#...#...#...#.........#.#.....#.#.......#.#.#.......#.....#.........#...#.#.#.......#...........#
    #.###.#.#####.#.###.#.###.#.###.#####.#.#####.#.#######.#.#######.#.#####.#.#.#####.#.#.###.###.#######.#.#######.###.#.#########.###.#.#.#.#
    #.#.....#...#.#.#.#.....#.#...#.....#...#.....#.#.......#.#.......#...#...#.......#.#.#...#.....#.........#.....#.#...#.......#.#.#...#.#.#.#
    #.#######.#.###.#.#######.###.#####.###.#.#######.#########.#####.###.###.#.###.###.#.###.#################.###.#.#.#########.#.#.#.###.###.#
    #.......#.#...#.#...#.....#.#.#.....#.#.....#...............#...#.#.#...#.#.#.......#.#.....#.....#.......#.#.....#.#.......#.#.#.#...#.....#
    #.#####.#.###.#.#.###.#.###.#.#.#####.#.###.#.###############.###.#.###.#.###.#.###.#.#.###.###.#.#.#####.#.#.###.#.#.#####.#.#.#.#.#.###.#.#
    #.......#...#...#...#.#...#...#.#.....#.#.#...#...#.......#.......#.#...#...#.#.#...#.#.#.#...#.#...#...#.#.#...#...#...#.#...#...#.#.#...#.#
    #.#.#.#####.#######.#.#.#.###.#.#.#####.#.#####.#.#.#####.#.#######.#.#####.#.#.#.###.#.#.###.#.#####.###.#.###.#.#####.#.#####.#####.#.#.#.#
    #.#.#...#...........#...#...#.#.#.............#.#.#...#...#...#.....#...#.#...#...#.#...#...#.#.#.......#...#.#.#.#.....#.#...#...#...#.#.#.#
    #.#.###.#.#################.#.#.#.#######.#.#.#.#####.#.#####.#.#####.#.#.###.#.###.###.#.###.#.###.###.#.###.#.###.#####.#.#.###.#.#.#.#.#.#
    #.....#...#.....#...#.....#.#...#...#.......#.#.....#.#.....#...#...#.#.#.....#.......#...#...#...#.#.#.........#...#.#.....#...#.#.........#
    #####.#####.###.#.#.#.###.#.###.###.#.#######.###.#.#.#####.###.#.#.###.#.###########.#.#.#.#####.#.#.###########.###.#.#####.#.#.#.###.#.#.#
    #...#.#...#...#...#...#.#.#.....#.....#.......#...#.....#.........#...#.#.#.........#...#.#.......#.#...#...#.....#.......#...#.#.#.....#.#.#
    #.#.#.#.#.###.#########.#.#.###.#.###########.#.###.###.#############.#.#.#.#######.#.#############.#.#.#.#.#.#.#.#.#######.###.#.#.#####.#.#
    #.#...#.#.#...#...#...#...#...#.#...#...#...#.#...#...#.........#...#.#.#.........#...#...........#...#.#.#.#.#...#...#...#...............#.#
    #.#####.#.#.###.#.#.#.#.#######.#####.#.#.#.#.###.#####.#.#####.###.#.#.###########.###.#########.#####.#.#.#.###.#.###.#.#.#.#.#.#.#.###.###
    #.#.....#.#...#.#.#.#...#.....#.....#.#.#.#.#.....#...#...#...#.#...#...#.........#...#...#.....#.....#.#.#.#.....#.#...#.#.#...#.#.#.#.....#
    #.###.###.###.#.#.#.#####.###.#####.#.#.#.#.#######.#.#####.#.#.#.###.###.#######.#######.#.#####.#####.#.#.###.#.###.###.#.#####.#.#.#.#.#.#
    #.....#...#...#.#...#.....#.#.....#.#.#...#.#.......#.......#...#.....#...#...#...#.....#.#.#.....#...#.#.#.....#.....#...#.#.....#.#...#...#
    ###.###.###.###.#########.#.#####.#.#.#####.#.#####.###########.#.#####.#####.#.###.#.###.#.#.#####.#.#.#########.#####.###.#.###.###.###.#.#
    #.....#...#...#.....#.....#.....#.#.......#.#...#...#.........#.....#...#.....#.#...#.....#.#...#...#...#.......#.....#...#.#.#.....#.#...#.#
    #.#.#####.###.#####.#.#########.#.#########.###.#####.#######.#####.#.###.###.#.#.#########.###.#.#######.#####.###.#####.#.#.#.#.#.###.#.#.#
    #.#...#.....#.....#...#...#.....#...........#...#...#.#...#...#...#.#.....#.#.#...#.....#.....#...#.............#.#.......#...#.#.#...#...#.#
    #.#.#.#.#########.#.###.#.#.###.#######.#####.#.#.#.#.#.###.#.#.#.#########.#.#######.#.###.#######.#.#####.#####.#.#.#####.###.#.###.###.#.#
    #...#...#...#...#.#...#.#.#.#...#...........#.#.#.#...#.#...#...#.#.........#...#.....#...#.........#...#.#.#...#...#...#...#...#...#...#.#.#
    ###.#####.#.#.#.#.#.#.#.#.#.#####.#.#######.#.#.#.#####.#.###.###.#.#.#.#######.###.#####.#.###########.#.#.#.#.###.###.#.###.#####.###.#.#.#
    #...#...#.#.#.#.#.#.#...#.#...#...#.#.....#...#.#.#.....#...........#.#.......#.#...#.#...#.#...........#.#...#...#...#.#...#.....#...#...#.#
    #.#.#.#.#.#.#.#.#.#.#####.#.#.#.###.#.###.#####.#.###.#.#############.###.###.#.#.###.#.#####.###########.#######.#####.###.#####.###.#####.#
    #.#...#...#.#.#...#.#.#...#.#.#.#...#.#.#.#.....#.#...#...#.....#...#.....#.#.#...#.#...#.....#.....#.........#...#.....#.#.#...#...#.....#.#
    #.#.#######.#.###.#.#.#.#####.#.#.#.#.#.#.#####.#.#.#######.###.#.#.###.###.#.#####.#.#.#.#####.#.###.#.###.###.###.#####.#.#.###.#####.#.###
    #...#.....#.#...#.#.#.#.#...#...#.#.#.#.#...#...#.#.#.....#.#.#...#...#...........#.#.....#.......#...#.......#.#...#.#.....#.....#...#.#...#
    #.#.#.###.#.###.###.#.#.#.#.#.###.#.#.#.###.#.#.#.#.#.###.#.#.#######.###.###.###.#.#######.###.#.#.#.#######.#.###.#.#.###########.#.#####.#
    #.#.#...#.#...#...#...#...#.#.....#.......#...#.#...#...#...#.......#.#...#...#.....#.......#.#...#...#.....#...#...#.#...........#.#.....#.#
    #.#.###.#####.###.###.#####.###.#########.#####.###.###.#####.#.###.#.#.#.#.#######.#.#.#####.###.###.#.###.###.#.###.###########.#.#####.#.#
    #.#...#.........#...#.#.#...#...#.....#.#...#...#...#...#...#.#...#.#...#.#.#...#.#...#.........#.#...#...#.......#.........#...#.....#.#.#.#
    #.#.#.#########.#.###.#.#.#######.###.#.#.###.###.###.###.#.#.###.#######.#.#.#.#.#####.#####.###.#.#######.#.###.#.#######.#.#.#.###.#.#.#.#
    #.#.#.#.....#.....#.....#.........#...#.#.#...#...#.#.#...#.#...#.......#.#...#.#...#...#.....#...#...#...#...#...#.....#...#.#...#...#...#.#
    #.#.#.#.###.#######.#######.#######.###.#.#.###.###.#.#.###.###.#######.#.#####.#.#.#.###.#####.#####.#.#.###.###.#####.#.#.#.#####.###.###.#
    #...#...#.#.........#.....#.....#.#.#.....#.#...#.......#...#.#...#...#...#...#.#.#.#...#.#.....#.....#.#...#...#.......#.#.#.........#.#...#
    ###.#####.###########.###.#####.#.#.#######.#.###.#.#####.###.###.###.#######.#.#.#####.#.#.#####.#####.###.###.#######.#.#.#.###.###.#.#.###
    #...................#.#...#.......#.......#.....#.#.....#...#...#.#...#.......#.#.......#.....#...#.......#...#...#...#...#.#...#...#...#...#
    #.#.#.###.#.#.#.###.#.###.#######.#######.#.###.#######.###.#.#.#.#.#.###.#####.#.###########.#.#######.#.###.#.#.#.#.###.#.###.#.#.#.#####.#
    #.#.....#.#.#...#...#...#.......#.#.....#.....#...........#.#...#...#...#.#.....#.......#...#.#.#.......#...#.#.....#...#.#.#.............#.#
    #.###.#.#.#.###.#.#.###.###.#.#.###.###.#.###.###.#.#.###.#.#.#########.#.#.#.###########.#.###.#.#.#######.#.#########.#.#.#.###.#.#####.#.#
    #...#...#.#...#.#.....#...#...#.....#.#.#...#.#.....#.#...#.#.........#...#.#.#...........#.....#.#.#.......#.......#...#.#.#.#...#...#.#.#.#
    #.#.#.#######.#.#.###.###.###########.#.###.#.#.#####.#####.###.#####.###.#.###.#################.#.#.#.###########.#.###.###.#.#####.#.#.#.#
    #.#.#.#.......#.#...#...#.#.....#.......#...#.#.....#.#...#...#.#.....#...#...#...#.............#.#.#.#.........#...#.#...#...#.#.....#.....#
    ###.#.#.#######.#####.#.#.#.###.###.#########.#####.#.#.#.#.#.#.#.#####.#.###.###.#.#.###.#.###.###.#.#.#####.###.###.###.#.#####.###.#####.#
    #...#.#.....#...#.....#.#...#.#...#.....#.....#...#...#.#...#.#.#.#...#...#.#.#.....#...#.#...#.....#.#...#.#.....#...#...#.#.....#...#...#.#
    #.#.#.#####.#.###.#####.#####.###.#####.#.#####.#.#####.#.###.#.###.#.###.#.#.#.#######.#.###.#######.###.#.#.#######.#.#.#.#.#.#######.#.###
    #.#.#.#.....#.....#...#.#.....#.#.....#...#.....#...#...#.....#.....#...#.#.#.#.........#.#...#.........#.....#.....#.#.#.#.#.#.....#...#...#
    #.###.#.#############.#.###.#.#.#.###.###.#.#######.#.#########.#.#.#.###.#.#.###########.#.###.#######.#####.###.#.#.#.#.#.#######.#.#####.#
    #.#...#...#...........#.#...#.#.#...#.....#.......#.#.....#.....#.......#...#...........#...#.......#.....#...#...#...#.#.#...#...#...#...#.#
    #.#.#.#.#.#######.###.#.#.#.#.#.###.#########.#.#.#.#####.#.#######.###.###.#####.###.#######.#####.#######.###.#######.#.###.#.#.###.#.#.#.#
    #.#.#...#.#.......#...#.#...#.....#.............#.#...#.....#.......#.#.#.......#.....#.....#.#...#.#...#.......#...#...#...#.#.#...#...#.#.#
    #.#.#.###.#.#######.###.###.#.#####.#.#.###.#####.###.#.###.#.#######.#.#.#######.#.###.###.#.#.#.#.#.#.#.###.###.#.#.###.###.#.###.#####.#.#
    #...#.............#...#.....#.#...#.#.#.#.#...#...#.#...#.#...#.......#...#...#.......#.#.#...#.#.#...#...#.....#.#...#...#.................#
    #.###.#####.#.###.###.#.#######.#.###.#.#.#.###.###.#####.#.###.#.###.#####.#.#.###.###.#.#######.#############.#.#########.#########.#####.#
    #.....#.....#...#.#...#.#.......#...#...#.#...#...#.......#.#...#.#.#...#...#...#...#...#.........#.............#...........#.......#.....#.#
    #####.#.###.###.#.#.###.#.#########.###.#.###.###.###.###.#.#.#.#.#.#.###.#######.###.###.#####.#.###.###.#######.###########.#####.#####.#.#
    #.......#.#.....#.#...#.#...#.........#.....#.#...#.....#.#.#.....#.#.#...#...#.#.#...#.#...#...#...#.#...#...#...#.......#.......#.#...#...#
    #.###.#.#.#######.###.#.###.#.#######.#.#####.#.###.###.#.#.#.###.#.#.#.###.#.#.#.#.###.#.###.#####.###.###.#.#.###.#####.#.###.###.#.#.###.#
    #.#.#.#.#...#.....#...#...#.#.#.....#.#.#.....#.....#.#.#.#.#.....#.#.#.....#.#...#.#...#.#...#...#.....#...#.....#.#.....#.#.....#.#.#.#...#
    #.#.#.#.###.#.###.#######.#.###.###.#.###.#.#.#######.#.#.#.#.#####.#.#######.#####.#.#.#.#.###.#####.#############.#.###.#.#####.#.###.#.###
    #.#...#...#.#...#.#.....#.#.......#.#...#...#.#.......#.#...#.......#.......#.......#.#.#.#.#...#.....#.............#.#...#.................#
    #.###.###.#.###.###.###.#.#.#####.#.###.###.###.#.#####.#.#.#####.#####.###.#####.#.#.#.#.#.#.#.#.#####.#############.#.###.#.#####.#.#.###.#
    #.......#...#.#.......#...#...#...#...#.#...#...#.....#.#...#.....#.....#.#.#...#.#.#.#...#.#.#...#.....#.........#...#...#.#...#...#.#...#.#
    #.###.#.###.#.###########.#.#.#######.#.#.###.#.#####.#.#.#####.###.###.#.#.###.#.#.#.#####.#.#.#.#.#####.#.#####.#.#.###.#.###.#.###.#####.#
    #...#.#...#...#.#.........#.#.........#.#...........#.#.#.....#...#...#.#...#...#...#.#.....#.#...#...#.#.#...#...#.....#.#.#...#...#...#...#
    ###.#.###.###.#.#.#####.#.#.###.#.#####.#############.#.#####.#.#####.#.#.###.#######.#.#####.#######.#.#.###.#.###.#####.#.#.#####.#.#.#.###
    #...#...#.....#.#.#...#.#.#...#...#...#.......................#.#.....#.#...#.#.......#.#.....#.....#.#...#...#.#...#...#...........#.#.#...#
    #.###.#.#######.#.###.#.#.###.#####.#####.#######.###.#.#.#####.#.#####.###.#.#.#######.#######.###.#.#.###.###.#.#.#.#.#.###.#.#####.#.###.#
    #.#...#.#.......#...#...#...#.#...........#...........#.#.....#.#.#.........#.#.....#.#.........#.....#.#...#...#.#...#.#.....#.#.....#...#.#
    #.#.#.###.###.#####.#.#####.#.#############.###.#######.#####.#.#.#####.#####.#####.#.###########.#.#.###.###.###.#####.#######.#.#####.#.#.#
    #...........#.....#.#...#.....#.....#.........#.#.......#...#.#.#.....#...#.........#.......#...#.#.#.....#.#.#...#.#.....#...#.#.#...#.#.#.#
    #.###.#.#########.#.###.#####.#.###.#.#########.#.#######.###.#.#####.###.#.#####.#####.###.#.###.#########.#.#.###.#.#####.#.#.#.#.#.#.###.#
    #.....#.#.......#.#...#.#...#.#.#.#.......#.....#...#.........#...#...#.#...............#...#.........#.....#.#.#...#...#...#.#.....#.#.#...#
    #####.###.#####.#.###.#.#.#.#.#.#.#########.#######.#.#.###########.###.#.#####.###.#####.#####.###.###.###.#.#.###.###.#.#.#.###.###.#.#.###
    #...#.....#...#.....#.#...#.#.#...#.#.....#.#.......#.#.............#.#...#.....#...#...#.........#...#...#.#.#.......#...#.#...#.....#...#.#
    #.#.#########.###.###.#####.#.###.#.#.###.#.#.#######.#.###########.#.#.###.###.#.###.#.#########.###.#.###.#.#####.###.#.#.###.#.###.###.#.#
    #.#...........#...#...#.#...#...#...#.#...#.....#.....#.....#.....#.....#...#...#...#.#.....#...#.#.#...#...#.....#.#...#.#.#...#.......#.#.#
    #.#######.#####.###.###.#.#####.###.#.#.###.###.#.#########.#.###.#.#####.###.#######.#####.#.###.#.###.#.#######.###.#.#.#.#.###.#.#.#.#.#.#
    #...#...#.#.......#...#.#...#...#...#.#.#.....#.#...........#.#...#...#.....#.#.......#...#.#...#...#...#...#.........#.#.#...#...#.....#.#.#
    ###.###.#.#.#########.#.###.#.###.###.#.#####.#.#############.#.#####.#####.#.#.#######.###.#.#.#.###.#.###.#.###########.#.###.#.#.#####.#.#
    #.#...#.#.#.#.#.....#.#.#.........#...#.......#.#.........#...#.....#.......#.#...........#.#.#...#...#.#...#.#.#.........#.#...#...#.....#.#
    #.###.#.#.#.#.#.###.#.#.#.#########.#########.#.#####.#####.#######.#########.###.#####.#.#.#####.#.###.#.###.#.#.###.###.#.###.#.###.#####.#
    #...#.#.#.........#...#...#.......#...#.#.....#.#...#...#...#.....#.....#...#...#...#...#.#.#...#.#...#.#.......#.#...#...#.....#...#.#.....#
    #.#.#.#.#####.###.#####.###.#####.#.#.#.#.#.#.#.#.#.###.#.###.###.###.#.#.#.###.###.#.#.#.#.#.#.#.###.#.#.#######.#.###.#.#######.#.#.#.###.#
    #.#.#...#...#...#...#.......#...#.#...#...#...#.#.#...#.#.#...#...#...#.#.#...#...#.....#.#.........#.#.#.#.....#.#.#...#.....#.....#.#...#.#
    #.#####.#.#.#.#.###.#########.###.#.#.###.#####.#.###.#.#.#.###.###.#####.###.###.#####.#.#####.###.#.#.#.#.#.#.#.#.#.#####.#.###.#.#.#####.#
    #.....#.#.#...#...#.#...#.........#.#...#.#...#.....#.#...#...#...#.....#.#.#...#.#.....#.#.....#.....#.#.#.....#.#...#.....#.#...#.........#
    #.#.#.#.#.#.###.###.#.#.###.###########.#.###.#####.#.#.###.#####.#####.#.#.#.###.#.#####.#.#####.#.###.###.#.###.###.#.###.#.#.#.#.#######.#
    #.#.#.#...#...#...#.#.#...#.............#.......#...#.#.#...#...#...#...#...#.....#.#.............#...........................#.....#...#...#
    ###.#####.#.#####.#.#.###.#.###################.###.#.#.#.###.#.###.#.#.###.#######.###############.#.#.#######.###.###.###.#######.#.#.#####
    #...#.....#.#...#.#...#...#.#...#.........#.....#...#.#.#...#.#.....#.#.#.#.#...............#.......#...#...#.#...#.................#.#.....#
    #.#.#.#######.#.#.#####.#.###.#.#.###.###.#.#####.###.#.#####.###.#.#.#.#.#.#########.#####.#.#####.#.#.#.#.#.###.#######.#.#.###.###.#####.#
    #.....#...#...#.......#.#...........#...#.#.#.....#...#...#...#...#.#.#.#.#.#.........#...#.#...#...#.#...#...#...#.......#.#...#.....#.....#
    #.#####.#.#.#########.#.###.###.#######.###.###.###.#####.#.###.#.#.#.#.#.#.#.#######.#.#.#.###.#.#.#.#######.#.###.###.#.#.###.#######.#####
    #.#.....#.#...#.#.....#.....#...#.....#.........#...#.....#...#.........................#.#...#.#...#.......#.#...#...#.#.....#.......#.....#
    #.#.#####.###.#.#.###########.###.###.###########.###.#######.#.#.###.#.###########.#.###.#.###.###.#.#####.#####.###.#.###.#.#.###########.#
    #.#...#.#.#...#.#...........#.....#...#...........#.#.#.....#.#...#.#.#.#...............#.#.....#...#...#.#.......#...#.#...#...#...........#
    #.###.#.#.#.###.###########.#######.###.###########.#.#.#.#.#.###.#.#.###.#####.#####.###.#######.#####.#.#########.###.#.#.#####.#########.#
    #S....#...........................#.....#.....................#.....#.........#...................#.................#.....#.......#.........#
    #############################################################################################################################################
""".trimIndent().lines()
