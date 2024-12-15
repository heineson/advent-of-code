package aoc2024.day15

import aoc2024.Coord
import aoc2024.Grid2d
import aoc2024.Vect
import aoc2024.day15.Dir.*

enum class Dir {
    UP, DOWN, LEFT, RIGHT;

    fun vect(): Vect {
        return when (this) {
            DOWN -> Vect(0, 1)
            UP -> Vect(0, -1)
            LEFT -> Vect(-1, 0)
            RIGHT -> Vect(1, 0)
        }
    }

    companion object {
        fun of(s: String) = when (s) {
            "^" -> UP
            "v" -> DOWN
            "<" -> LEFT
            ">" -> RIGHT
            else -> error("Unknown dir $s")
        }
    }
}

fun main() {
    actualData.let { data ->
        val warehouse = Grid2d(data.takeWhile { it.isNotBlank() }) { it }
        val moves = data.dropWhile { it.isNotBlank() }.drop(1)
            .fold(listOf<Dir>()) { acc, s -> acc + s.map { Dir.of(it.toString()) } }
        val startPos = warehouse.getCoords().first { warehouse[it] == '@' }
        warehouse[startPos] = '.'

        println("Part 1: ${part1(warehouse, moves, startPos)}") // 1412971
    }

    actualData.let { data ->
        val warehouse2 = data.takeWhile { it.isNotBlank() }
            .map { line -> line.toCharArray().fold("") { acc, c ->
                acc + when (c) {
                    '#' -> "##"
                    '.' -> ".."
                    'O' -> "[]"
                    '@' -> "@."
                    else -> error("Should not happen")
                }
            } }
            .let { Grid2d(it) { it } }
        val moves2 = data.dropWhile { it.isNotBlank() }.drop(1)
            .fold(listOf<Dir>()) { acc, s -> acc + s.map { Dir.of(it.toString()) } }
        val startPos2 = warehouse2.getCoords().first { warehouse2[it] == '@' }
        warehouse2[startPos2] = '.'

        println("Part 2: ${part2(warehouse2, moves2, startPos2)}") // 1429299
    }
}

fun part2(warehouse: Grid2d<Char>, moves: List<Dir>, startPos: Coord): Int {
    var pos = startPos

    fun freeSpace(nextPos: Coord, m: Dir): List<Coord> {
        val v = m.vect()
        val coords = listOf(nextPos) + nextPos.inDirectionWhile(v) { warehouse[it] != '#' }
        return coords.filter { warehouse[it] == '.' }
    }

    fun shiftHorizontally(nextPos: Coord, m: Dir) {
        val v = m.vect()
        val coords = listOf(nextPos) + nextPos.inDirectionWhile(v) { warehouse[it] != '.' }
        coords.reversed().map {
            val n = it + v
            warehouse[n] = warehouse[it]!!
        }
        warehouse[nextPos] = '.'
    }

    fun move(m: Dir) {
        val nextPos = pos + m.vect()

        if (warehouse[nextPos] == '#') return
        if (warehouse[nextPos] == '.') {
            pos = nextPos
            return
        }

        if (m == LEFT || m == RIGHT) {
            val ch = if (m == LEFT) ']' else '['
            val free = freeSpace(nextPos, m)
            if (warehouse[nextPos] == ch && free.isEmpty()) return
            if (warehouse[nextPos] == ch) {
                shiftHorizontally(nextPos, m)
            }
            pos = nextPos
        } else {
            fun expandBox(c: Coord): List<Coord> {
                return if (warehouse[c] !in listOf('[', ']')) listOf(c)
                else listOf(c, if (warehouse[c] == '[') c.right() else c.left())
            }

            fun loop(line: List<Coord>, bs: List<Coord>): Pair<List<Coord>, List<Coord>> {
                if (line.any { warehouse[it] == '#' }) return Pair(emptyList(), emptyList())
                if (line.all { warehouse[it] == '.' }) return Pair(line, bs)

                val nextLine = line.flatMap { if(warehouse[it] in listOf('.', '#')) listOf(it) else expandBox(it + m.vect()) }
                return loop(nextLine, bs + line.filter { warehouse[it] in listOf('[', ']') })
            }

            val (_, boxCoords) = loop(expandBox(nextPos), listOf())
            if (boxCoords.isEmpty()) return

            val nextBoxCoords = boxCoords.map { Pair(it + m.vect(), warehouse[it]) }

            boxCoords.forEach { warehouse[it] = '.' }
            nextBoxCoords.forEach { warehouse[it.first] = it.second!! }
            pos = nextPos
        }
    }

    moves.forEach { move(it) }

    return warehouse.getCoords().filter { warehouse[it] == '[' }
        .sumOf { it.x + 100*it.y }
        .also {
            val wh = warehouse.copy()
            wh[pos] = '@'
            println(wh)
        }
}

fun part1(warehouse: Grid2d<Char>, moves: List<Dir>, startPos: Coord): Int {
    var pos = startPos

    fun freeSpace(nextPos: Coord, m: Dir): List<Coord> {
        val v = when (m) {
            DOWN -> Vect(0, 1)
            UP -> Vect(0, -1)
            LEFT -> Vect(-1, 0)
            RIGHT -> Vect(1, 0)
        }
        val coords = listOf(nextPos) + nextPos.inDirectionWhile(v) { warehouse[it] != '#' }
        return coords.filter { warehouse[it] == '.' }
    }

    fun move(m: Dir) {
        val nextPos = when (m) {
            DOWN -> pos.up()
            UP -> pos.down()
            LEFT -> pos.left()
            RIGHT -> pos.right()
        }
        if (warehouse[nextPos] == '#') return
        val free = freeSpace(nextPos, m)
        if (warehouse[nextPos] == 'O' && free.isEmpty()) return
        if (warehouse[nextPos] == 'O') {
            warehouse[free.first()] = 'O'
            warehouse[nextPos] = '.'
        }
        pos = nextPos
    }

    moves.forEach { move(it) }

    return warehouse.getCoords().filter { warehouse[it] == 'O' }
        .sumOf { it.x + 100*it.y }
        .also {
            val wh = warehouse.copy()
            wh[pos] = '@'
            println(wh)
        }
}

val testData = """
    ########
    #..O.O.#
    ##@.O..#
    #...O..#
    #.#.O..#
    #...O..#
    #......#
    ########

    <^^>>>vv<v>>v<<
""".trimIndent().lines()

val testData2 = """
    ##########
    #..O..O.O#
    #......O.#
    #.OO..O.O#
    #..O@..O.#
    #O#..O...#
    #O..O..O.#
    #.OO.O.OO#
    #....O...#
    ##########

    <vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
    vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
    ><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
    <<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
    ^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
    ^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
    >^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
    <><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
    ^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
    v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
""".trimIndent().lines()

val testData3 = """
    #######
    #...#.#
    #.....#
    #..OO@#
    #..O..#
    #.....#
    #######

    <vv<<^^<<^^
""".trimIndent().lines()

val actualData = """
    ##################################################
    #.#..OO.OO...O..OO..#.....O....#OO.#....#..OO....#
    #O..O......O#...O.....#..OO..O....O...OO##O..O#..#
    ##.O.O...O.#.O...O#O...O.#O..#..#....#O.O.OOO...##
    ###...O...O.OO..OO....O.........OO..OO..#..O...#O#
    #OOOOO...O.OO.O..OO.O.....O.O...OO..O#......O.O.##
    #..#..#..##.OOO.#O..O....................O#.O..#.#
    #.....O.O.O.OO..#.#.O...OOO.O.#.........O.#......#
    #........#....O..O.OOO.OOO..O...O.O.OOO.O...O.O.O#
    #..O..O........O..O#...O.O....#..O......O..O.#O..#
    #O....O.O...O.........#...#.....OO....O......OOOO#
    #..O.....OOO#O..O.O.#.............O.#OO....OO.O.O#
    #...OO.....#....OO......O#..OOO....O.O..#OO......#
    #....O#.......O....#.#.O..#.OO#O..O...OO..#O#O..O#
    #..O..O.........O..#.OO.#O......OO.OO......O...#.#
    #...............#...#.#...O#............O.......O#
    #OO...O.OOOO#O.....O........OO.OOO.OO....OO#.#...#
    #.O.#O..OO.OO..O..#..#..#..O...OOOO...........O#.#
    #...OO......O...O..O...O.O.O#..OOO......O#..O....#
    ##..OO....OO.....#.O...O..OO.O.O..O##..O....#....#
    #......O...O....#OO..#.OO.O.O.O.#.OO...O....OO.#.#
    ##.....#.OO.O.............#O......OOO#..........O#
    #.........O...#OO.....#.....O..#.....OO.O.#O.....#
    #.........#O...OO..O...#.OOO......O....O...O.O.#.#
    #....O..#...OO.OOO......@..#..O.O..O.O.......#O.O#
    #.OO.#O.O..#..O.......OO.....#.O.O...##O.......#.#
    #..O....O.O.....#.O.OO..O...#OOO.O...OO.O..#.....#
    #.#.O.OO.#...O.....#...O#.O..#O#O.OO............##
    #....#.O....O..O...OO...........O.O.OO.O.OO..#.O.#
    #O..O.O.O.OOO.O..O...#O.O..OOO.O#O...#.O...OO.O.O#
    #OO....#.....O.O..#.#.O.O..........OO.#..O#......#
    #..O......O#..O...##O.O..O.O.#...O.OO.O....O..O..#
    #.#..O.O.....#O.O...OOOO....O#O.OO#....O......OOO#
    ##..#...OO###.O..O.OOO.O.O.........#..O..O.O..O..#
    ##...OOOOO.OOOO#..O##...O.....O.O..OO....OOO#....#
    #....O.O..OO..#O..#...O........OOO.O.##.O.#.##..O#
    #.....OO..OO..O...#.....O.......#..........O.#.#O#
    ##..#.....#.O...O#..O.....#.....#..#...OO....O...#
    #.OO...O.OO..#O#...#...O.O.##..O..#O#.........O..#
    #......O.O..O.OO.#.O.O..O...OOO....#.O.OO.O..O...#
    #..#...O...O..OOO..............O.#...O..O....O.###
    #........OOO...O..#.....OO...OO....O......#.OOO#O#
    #.##.OO..O.....##.OO.OO.#.O.##..#..........OO....#
    #..O.O..O.O.#.OOO...#....O..#..#.O.O....#.O..O..##
    #O..O..O..O.O.............OOO.O..O..O..O..O.OO##.#
    #...O#OO..OO...O.....O..#O.O...O...O.OO...OO.....#
    #..O...#..#.O....#.....#.OOO#........O.....#.O...#
    #.OO...OOOO.....#O......#.....O..O.O.O.#.........#
    #.....O.O#.O..OO......O..O.O.O....##O#..O.#...#..#
    ##################################################

    vvv>>><v^v<v>^<v>^^v<>v>^>^^>v<v^^^<^<<>v<^vvv<^<v^><><^^<v>^^^^^><<>v<vvv^>^^<^vv<>v^v<^<<^<<v^vvv>>><>v<>vv>^<><^^v^>^>^^^<^^^>^^<<<><v>vv^^<>><v^^<vv>^vv^v^<v^>>>>^v<^><vv<^<<<<>><^^^v^v<>v>^v^<^^>>^^>^<<^v<^<>v<>v<vv><^<^<^><>>><<<>>v>>>>v>v^<><<<><>^<>^<^v>><v^v>v<^^<<>vv^v^<v<<^v<v<^^vvv<^<^v<^<^^>v^>v^>vv<v>>>vv>>^>^v<v<>>><v<<^<>v<>v^^^v<>>v^<><<vv>vv^<^v^^>^vv<>^<^vvv<<<v<>^^><vv<<<>>^><>>>>^^^^^vv>><>^vv<>v><v>v^^^>>v<<><v^v<^<^^vv<<^v>>v^<v<^<<^><>vv<vvv^><><^v^v<^><>>>^^>^>^^<v<<^>>vv<<v^^><^<vv<vv<<vv<<><^^^<v<<^<<^>>>><<<>^<<vv>^<>vv^<<^>^<^<v^v>v^<<><>>v^<>vvv>^^<>^<v^<v>v^><v<^v>^vv<>^<^<>^<^><<<v^><v^>^>^^<^<v^^><<^v^^>>>v<>vv<<v<<<^><vv<>><>>^><vvv^vv>^<^vv>v<^^>^>>^>v^<<<>><^^vv>^^<^^>>^<<v>><>v^><^^v^^v>^<<>>v<v^v^><<><^>^>^<^<>^<<v>vvv^^^<<>>><v^<>^^><^v^>^<v>^>v><>><^>v^><^<><vv^<<<^><<>vv><^^><v<^v>v><<<><<>^><v^<<>^><v>^^v<>v>^^^<><><^v>>v><<^<v>>v^<^<>^^v>v>^>>>v><>^^>^^v>v^^v<v^>>vvvv^v^<^vv^>>^^<v<v><^^^><><v<>>v>v<v^>^vv<>>^^>^vvv>v^^v>>^<^>>^>v^>>v><vvvv^>v
    v<^<><v^<v<vv<<v>v^<>^><vv<<<<v^>^>v<^^>v<v>v<>>^>>v<^^^v^^>v^><v>^^>>>^>v>>>v^><><<><v^v^vv^^^^v<<^>>v<^><<v^>v^>^^<v<<><^v<><v^^<vv^^<v^>^>>^v<v^v^<v^v<<^<^>v^<><<v><vvv^<^^^^<><><^^^vv^^>vv^>v<v^^<<^^<<<<<>^v^vv^vv^>>>vv<^<vv<^v<><^^>>v<<<<^^<v>>>v^><>^>>vv>>v<<><v>>v>>><<v^<v>>^<>v><^<<^<<<<v^^^vv<^>v<v<^><><<>^v^vv><v>^vv><<^v^v>vv^v<v>>v>^>v>>><<><><><v^<v>^<>^<>^v^v<>vv>>^<>><>^v^v<<^>v<<v<^>><>>^^v>v^vv><vv>^^>^v^>v>><<v><vvvv><>>>^<^v>v<<>>v^<<v>^<><v^v<><^^v<>^vv>^>v<^vvv<><>>vv>^^<v>^v<<v^^^<><>>>^<<>vv<><v><<>v<v>v>>^v>>v<vvv><<^>^^<^<>>>>v<^v^^^<<<<<^>^>>>^<^<^><vv<>^>^^v^><v^>>^<<^>^<v^^vv>v<>v><v^><>>>vvv><>v<vv^>>v<<><^<>>v>>><vv<>v>><vvvvv^><<<^><>>^^><v^<^^^v<^<^>^<v^<v^^^><^<v>v>^<<^<>^>v^<v>vvvv>v>>>v^vvvv>><>^>>><>vvv^<^<v>^^^>>^vv>>^v>>^>^^>^>>vv^^>><^<>>^^^^<^v><>^>^v>>><^vvv^vvvv<>^><^vv>>^^v<>^<<^v<^^<v>>v^<v>^><^><vv<<^<<<v^vv<<>v<v^^<><>^^>^v<<^<><v^v^><v>^><<v>^vv>^v^^v^><<vv>v><vvv><>^<>v>v>^<<^<<>vvv>>>><<>vv<^<><>^<^v><^<vvv^<>^^>>vv>v<>v>^^^vv<><<^>v<v^v
    <>^<^^v<vv>^>>^<^>^<v^vv<v<<v<^>>v^^v>^^v>v<<<<^<<v^<v>>v>v^v<^^<><<^v>^^^<v^^><v>vv<^^^<>>^<><^v><>><><^>^<<<v<<v>v<<v^vv^v<v>^^>v<v>><>v<v<<^>v>^<<v><<>^^^>^v<vvv<^<>><<>>v<^<>^v<>v>v>v<<<><^<^<>^<^<v>^<v>>>^<v>>>v<^v<^^^^>^<v>^><^><<><v>^>vv^>^v<<>>>v^v<>^<v<><><><<><<>^>v<>><<>^v<^<<<^v^>v^^^^>>><<^v>>>^><^v^^<<^^v><>>v^^^>>v<>^>vv<vvv^>>>>v<v<v^<^<>>^v<vv>^>^v^vvv<<^^><v<^v^v<vv^v>><^^<^>vv<v^^v><>>v^>>v<<>v^^<<^>>><<><><<<>^v>><<vvv<v^v<^<>>>>><^<<^<^><v>v>v^>v^v><v>v<^<^>v>><>v^<<v<<><^>v<^>v<^<v^v<>v^v^v>vvv<<><<>><^>vv^<^^^^<>>^v>>v^v>>v<v<<<v<><v^<>>v>>^><^>^^><^^v<<<><^>^v^>><><^>v><<v^>>><>^^v<>^<^v>v><<><>^<><<<^v>^^><>>^>><^v^v>>^><>v>v<^>><<><v>v^>^>v^vv>vv<^v^^v><<vv>v>vvv>^><v>v<>v>^^^v^>>^>>v^^>^>vv>v><^<>><^v>v>>>^<v<^vvv<vv>^>>^<^>^<<>>v<<^^<v><<^<^<<vv>^<>>^<>v<>>^v>vv<>^v<v>^>>v>^v^>^^<><><vv<>^v<^><<vvv^<><><<>^<<^>>>vvvv^v^v^<^<v<vvv<v>^vv<<>v><>^><>^v>v>>v>^v>v>^^^v<v<^vv<><><vvvv>^><^v><>>>v^<^v^>^>>>>><><<<>vv><v^>^v^>v<>>>v^>vvv<>v^v<v<^><v<<v^vv^^><v^<v^<^v
    >>>v<v^<<><^>>v^<>v<<^>>>^>><<<<>><v>><v<>>>v<>><vv^<>vvv>v<v>v^^<<^>^<<^>^v^^^<<>v>^<><^^<vv<>v<^>^<^^>^^^vv>>^<v<v>v^vv>^><v<^^v^v><<<^^v<v<v^><v^v>>v>^>v>vv>>>^^vv>^vv<^>^><v^v<>vv^<v^<<<><>vv>>vv>v>v^>>^vv^>v^<^>^><<>v>>><<>><v>^>>>^<<vvv>>^v<<<><>v^v^<vvv>vv<>^<<>^v><vv><v^>v>v^>^<<v<^<^<<v<<^^^<^v<vvv>^>v<<<v>vvv>^><<^<v<v>^>vv^<>v>^^^^<><^>^<<<<v<<^vv<<^v<>v<<<v^<^vv^><<vv<>vv^v>v><<>vv^><v>^vv<^v<^><^^v^^^^vvv<>><^<<<>>v>^^>^<^^^><<>><<^^^^vv^<<><<<v>^v^^v>vv><>v<>^<><^<<><><>^<v>v<>^vv^^<><<^<<<v<<<<>v><><>v<>>^v^^<^<><vv<>>><v<^v<^v<v^v<^^^^^<vv<<v^<<^<<vv<<vvv><^v^^<>^v<^<><><<v>v^^^v^>^<<v>>v>v^vvv^>>^>^<>>vvv><v<v><^^><v^<<v^^<<>v><<<v^<><^><^v^<<^<v<<<<v^<^^v^<>v>v><<v>^v<>>v<v^v^v^>^<v^^^>><^^^^v^<^^<<^<>^<v^><vvv><v<><><^^^v>v>><^<v<<>^^vv<vvv>>>>v<vv^<v>vv^^><<>^vv<^^<^<v>>vvvvv^v^>>vv<v>v<v<^v<>v^^<v^vv<v^>^^<<v^<<><^<<>v>v>^^<v>^^>v<>v<vvv<>v>v<v^>^<vv^<>>v^v<<^^v^>^<vv^v<<^vvv^^<>^v<><v<^<vv<>vvvv<v<^v>^>>><<v^v^<<^vv^v>^>vv^^>>>^>^<^>^v^^<><>^<<v<<^v<^<^v^<^^vv^^vv
    >>v<^><<v^>>^<<^^v>^<<^<<v<vvvv<<^^<^v>^^<<<vv><v>^<^^^^v>v>^<^><^^<>^vv<v<>>v><^<vv>v<>^^^><>vvv<>vv>>vvv^>^><<<<<>^>v^>v^^<^<^><><<^<<><<^<>v>^>>v<^<^^<<^^<>>>>^v><><v^^^v^v>>>vv>v<^^v<^^>>>>^^^^<>^v>^^<^^>^^vvvv^v>v<<^v>^v<>^>^^v>^><>v>vv>^v>^^v^><<<^^>^v^v^v^v>^>^>v<^<^>>v^<<><v><^^^^>^>>>v<<v^^^<><^v<>v^>>>><^v<>>v><vv^>>>vv^<>>v>vv>^>^v>>^><>v<v<>v<^>>v<>^>^<<^v^>v<>><^^<^vv><^v<<<v^v^<^^^^v<^><<vv<<>^vvv<^v^v^^>^>^v^^v>^<><<vvv>v>^^^^><v<<vv>v^<v<^^vv<<v<^<<<<<^v<vv<v>^<<>>vv^v^><^^^^<v<v>^^^v^v^^vv<v^v^<^>>^^<<<^v>vvvvv<><<>^<v^<^v>vv^v^<v<v^>^^<v>>>>>v^<<>^><v<^^><<v^><>^^>>v>v^<>v^^>>>>><>v^v>^^v<<^<<<<><v^<<vv^>>v<<^<<v<v^>>^^<><v>v>>^vv<<<v<v><v^vvvv>vv^<v<<^vvv^^^^v>^<^>^<<><v<<^>>^><>^v<v>^<v><><<v<<<^<v^>^^v^v><v<<><v<vv^^<v<<^^>^<<^<vv<>v>^>^<<<^^^^>^<^^vv<<v<<vv>>>>v><<>>><vv><<>>>v><<v>^>><>v<v>v^^^<>^><^v><>v<><>v>>^^<<<>><^^<><^><>>v>vv^<v^>>>v<<<>v<^^<^^<v^><><>v^<v>^^>>v^>^>vv>^^^<vv>^v<<<v<^^^>v<^<<^^v^vv>^>^v<^v>^<vv>v^v^^^v<^v>^>v<<vv^<<^^^vv<<^>vv^<v^vv><<<v>>
    <^>v^>>><<^v<^>>v^v>vv<^v^>>>^>^>v<>v>v<^^><<>^>^v<><^^v^>^>><^>^>v^<>^><<>>>^>>v><><<^^v<<^^>^>^v>vvv>>>vv>v<^^v^^v<vv<^>^<vv><v<vv^^<<><^<>v^vv>>^^vvv^<vv^<<><<<v^^<>v^^v<^<v^>>>^>^^v^^^^^<<vv><^<<v^v<v>>v>v<><^<<v<>>^>v><v>v<>vv<>vv<<^<v><>^^<>v><v<^>^^>^^vv<v^^<<<>>^^v<>^^>^vv<>v<>^v^v^v<>v<>^v^^^v<^<><v^v^>>^v<<<^>v>^^^v<>v<v^><^v>v<<>v^vv<>>><^<v^>v^<v^^v>^v^<<^<v><v^<^^>^>v<^>><>^^^vv<>^v<v^<^^<<>vv^><v>v<v<vv^v>>v<><<>^>^^>^<>v>>^^>v>>><<<^v^v<^^^v>>v>v<<>>^>><v^v<>^v<>v<><<<^>>v>vv^<v>^>><^v^><><>v>>^v>^>^<<v^v>v<vv^vvv^<^>v<^>^<v<vvv<^^<<<><v^><<>^>v^vv^^^v<vv<v^<>v^<^^<<><<v<<v^>^><>^<>>>^v<vvv<v<<<>vv<v>^>>v>>v>>^<v^vv<^^^<>>v<^^^vv<^>v<<><<<v^<^^^^<^>v<<>>>^<^vv>>>><>^<<<v<>v^<>^><v<<>>^>^^^<^vv<<<v<v^^>v>>v>^^>^<<<^<>^>><v^v<^<^>>^vv^<^^vv<v<vvv<^^^>^vv>>>^^v<v<^<<<^vv<><^><>><<<^<<>^<<>v<<^v><>^^^<<>v><^>^><><^^^<^vv<^>^<<^>>>>^^^<>v<v<v>><><>>^^vvv><^v<>^<v^^<^v<v<vvv^<<vv<<v<^v^v<v>^^vv>>>v<<<>vv<vv<v^>>^vv^v^><^<^<^<v><v><^<v<<v^^>><><<v><^><vv^^<<v><vv<><<^vvvv<v^vv>
    ><<<<>^>>>^><><<vv^<<<vv<v^<v<<><<^<v<^>>^><>^><<>><>^<v<><^<^^^^vv^>v^><>v^^<vv>>v<^<v^^^^^^>v>^>v<v>><><^vv>v>>>>v^v^^v<^^<v>v>>v^v><<>v>v^^><^<v<<><><<^>>^vv><<^v^vv^vv<<^vv>vv<^^v<^^vv<v^<<>v<>v^<^^v^<vvvv^v><vvv<vv^><vv<v>v>v<>^>>><v>v>>vv^^>vv<>>^<>^>^><v<>>>>><>>v><<>>^<<>vv^<>^^>><<^^^<><^<v<v>^<<<>v<v^v>v>^>vv>>^^vv^>><>><>v^v>><^^<>>^^>^^>^^^^vvv^>v>^>v><>>vv^><^^v<>v<v<vv><>>^^>>vv^^^v^^v^^<>v<^<v<v^^vv<vv^><^>v>>^<<^^><^<^vv<^><^^>^<^><v<^^^>>><^^<^^v<v>^>^vvv<<vv>^<^<>^v<<>^^v<^^^<><>>v^v^><>>vv^^^^^^<<^^^v^^^v><>v<vv>vv<^^^^<^v<v>^^^><v^<^v^v>^>><vv><v>vv>>^<>^^><<>>>^v<>>v^><^<^><^^><<v<v^>v<<<^^<v>>>vv<v^v^^^^^^^>>^^><^v^<vv<<<>^^v>v>v<vv^^<v^^^>^^^v<v>v^^>^v><>^>v>v^^v<><<>>>v>^^<v^>>vv^<^^^^<^^<>^^<^v>^><>v<>^^^<vv><<vvv^^<<^v>>vv<>vv<v^^>>^<v>><^<v>>^v^^<<v<>>^^>><v^^>vvv^v<^v<^^^v^^>v^><v^vv<>vvv<<^v>v<^>v><vvvv<^v>v<>>>^<><>^^^>^<<vv>^v>v<<<^<^>>v<<vv^>>^^<<>^^^>>>v>>^<^^^<<>v>vv<^v><<v>>v^^<<^<vv<<><^<<^^>^v><><>v^v>>^^>vv<v<vv<v<v>v<>vv>v^><^v<^>^^^><><<>^^v<v<v^
    ><^<<v^><<<<<<^>>^^<^^<^^^>^vvv>^<>^vv<><^vvv><vv^v<<v>>v^v>v>>v^vv^^^^^vv<><v>v<>v^vv>><^v^>^<>v<><v<<v^v<<^v<<<^<^vv>>>><^><^>>^>^<vvvv><<>>v<^<v>>^<>vv<>vvv^>>v^^>vvv<vvv^>>><v^<>^<><>v^v<^<v><>^^<^^vv<<>>^><>v<<>^<<>^^>>^>><^<v^>^<<>v^><vv^v<^^>^<>^>>^v^<<>>^<<<<^>><<^^^<^^<v^>v^^vv><<^^<>v<<v<^<^><^>><>v^>^v<^v>>>^>vvv<v^>vv^<><<>>v<^v>v<><v>>><<<^^<^<v^<v<<><vv<><v<^<vv>v^>^v^^<^^<>^<v<<<v^vv^^>v>^>^v<>^>v^<^^<>^^>vv^v^v<^v<v<<^>^>v<<>v^>v>><^v<>^<>><<^^v><>^^>>v^v><<<vv<^^<>v>^>>^^vv>^^>>v<<v<vvvv^^><v>^>>v>v^^^v><<v^vv><>><^<>^<>>v^v<^^v<vv<<^<<>v<>v>vv<>^>vv^>^v<<v<vv^<><>>^vv<^<v>>v<vv^^^^vv^^<^>>>>><^^<<^<<v>v>vv>><^v^v<^<vv>><>^^>v><^><^>>v^^>^^>>^^<v<<v>>v<<<><>>v^>vv>><<^<^v<^v<^<v<<>vv>v<<><v<^^^>^^<<v<v^>>><^v^>^>v<<>>>>^>>><v^^>v^>><>><v>>vv<>^>vv>v>>>^<v<^vv^><vv<^^vv^><>^<v><^>v<>><<v<<v^<v>v><^>v>>><<v^^<<^>>>^vv^>><^>>vv^v>^>^v>^<^v^>^<>v^>><<v<>^v^^><vvv>^><>>>^v<^<>v<>^<<><^>^>^v<^<v>v>>^<^^^v<<<^^<^v>^>^>^>^>^v^<^>^>^<v^><>^<v<^><>>v>^>v<<><<<v<^v>^<<^v<<<<^<<><
    ^<>^v^<^<><>^>><v<^<>^><<<>^v>v^^v><<>>^^^^v^^^>^^v>v^v^^>><<^<^v>^<>>^><v^>vv^vv^^v<vv<^>v>v<v>^>v>>v<>><v^>>v><^^<>^^<^^v<^>vvv^>^<>^^v>>^v>>>>v>vv<^^^^<>vv^><>^^vv<^<^>^<>^v>^vv><^^<^v><<>v^^v<vvvv<>v>>v<^^>^<<>^<>>><<>v>^><<^>^^v^<v<vv<><v><>>^<^v><v<>>><^>vvvvv^^v^>^^^>v<<<><<^^^<v>^<vv>>>^<<^<<<^<vv><^>^^><<<v<>v^^>vvv^v^^vv^>>><v<^vvvv>^<<<<>><v>v<v^<<^<^<<^^<^vv>^>>>^v^^>v^^vv>^^<^v^<v>^<vv>v^>^>><<v>^vvv>v<v<<>^<v>>><>^^<v^>^>^v>v<^>>^^>^>v^vv^>^<>^<^v^<^<v<v<>>><<><><>v^<>v^v^v><^^v^v^<vvv>^<>>v^<><<v^v^^v>v^^<><v^>v>^><^v<<<v<^v>v<^<>v><<><>v^<<v^<^v^v<<^><^<>v<v><^<^<vv^>^^>^vv^<^^<><v><^>^v>^><^v<>v<<<<<<^<<^v<^^><^<^<<>><<>^<v<<<<^>vvv>v^v<<>v<^<>><<><<^^<<v<><<vvv^<^^v>vv<v<vv<vv^<<><vv<<<><vvv>>v^^<vvv<v>vv^><><^>^>^<<v^vv>^>>vv^<v>><^<>>>^>^v^v^^^<^v<^^v^<^><v<v^^>v>>^^>vv>><^<>v><vv^^^^^^^><v^vv>v^v><^v<><v<<v>v<^^>vv<v<v>v^^^^^<>vv^v^<^>>v^^^vvv^>>v^<v<<<>v<>v<<^>>>v>v^v><>>^v^^^v<>>^><>><>vv<>^>>>v<>><>^<<>v<v<v^vv>v^^<><>v><<^<^vv<^v^>^><v^^^<>v<>^^>><<>><v>^v>vv^v
    ><v^v<v<^>>vvv<><>><^^vv>vv>><>><><>><>^vvv>>>><v>^^>^^^v>>><^<<v^v>><^^><^v<><v<^<<<>><<v<^v^v<^<v^<vv<^^^^^v<<vv><^>^>v>>>^<v^<<^<<^^^>>^v<>><^vv>v^^>v>^^<v^^><>v>>^v^><>>^>v<<>>^vv><v<^^v<<v<v><v^<><^>>v^v>^<<>>^v^^vv>>^<<<v>^>>vv><vv>>^>^^v^vvvvv>>>vv><>^<<v^^v^<>v^v>>^<v<<v>v<^>vv^>^>v>><^v^v>>^<v><>v>^><^<^<v^v>^^v^<><^<<<^v^^<><v<v<vv^v>v^^<^<v<v<^<><^v<<<>v^>v^<<>vv>>v^><^<^^vv^<^<<v^v<<^>^<^<^>v^^<v<><>><^^v>^<<<^v<>vvv^>vv>>>>v^vvvv^^<^^^v^<<v<v<><<v<vv^<v<v<>v<v<v^^v^vv><<^<<>>v<>^<>vv>^^vvv^>>>v>>>v^v>vvv<>v<<^^v><<<><<>>v>vv<<v<^^vvv<<<v^v<vv<vv>>vvv>>^v<>>^vvv><v^<>^<v><<^<v<>v<<v<^>>v<>v^^^<>vv^>><^><^>v<^^<>^>v>>>><vv^<>^^^v<^^<^<>v<v>v<v^<v<^v<^v<><><v<><<><>^>>>>^>v<^^vv^><v<v^>^<^^>>>^<v^^vv<<<^^>^^^>>^<^<>^v<<><vv^<^^^^v^<v<><^<v<vv<>^v><<^<<^v><>><^<>v><^<>^<^v^<>^^>^v^^^^>^<<vvv<^<<v>^^<^>^<<>v<<v<v>>>>^>v>><v<v<>^<<^v><<>v^^v^>v^^<^v^^><v<<v<vv^<v^<<<^v>>^<vv>>v^v^v^^<^v>><><><>v>>vvv>^<<><^>vv<^v>^<>><><<^>^>><<<<^^^vv><v<>v>>^<vvv>>^v><>^^^v>v><<>v<v>v>^v^^v>^v
    >v<^^>>^^<v<><><<>^<>>>v^^^<><^v<v^v>><>>^<^<><v^^vv^<<<>>><>>vv^>^vv^<^vv><v^>><^^v^vvvv^v<^v^^<^>>v^<vvv><>^v<v^<<<>v<<<>^v<v>v^>^^v>><<>^v^<vv>><<<vv^v>^^v<<^<<^<>^vvvv<>v^>^>vv<v>^vvv<<^><<^><<v^^^v^<>><^>>><^^><v>^v^v<^<>v^^<^vv<^^>vvv<v<^^v^<>v<>v<<^>>>><v^vvv>^>v<<<v<<>v>>v<^<<>^v<v^<vv<^v><v>>^>>><v>><<<>>^<<<>vv<<<>^>><>^<^>><<>^<vv>^><>>v<^<^<>^><<>^v^<^vv<<>v^>>>^>>^>^v>^<^>v>>^^v>v>>>^v^^^><^<^v^>vvv>v<v^<vvv><<^>^>><<>^^^>v>^<>v<><v<v<^<^^v>><vv>v^^v^>>^v<^^vv><><v^<v<<^v>vv><<><<<v^<v<^><v<^vv>v^>vv^<v^<^<vv^^<v^><^>^^><<v><<^^<>v^<<v^>v^<^><v^v>>><>v><v>>>v><<vv^v>v^>^<>^<vv<>vv>v>v><v^>>^>v^<vv^<>^><<>>>>>><vvv><><v<^>><<<^<^^<<v^><>^vv^>>vv>vv<>>><<^^<><<^<^>vv<><^>vv^^v>v^^v^^v<^^<v<vv^<v>v^^>><<^<v><<<>><<>v<>>^>>>^^^>v<<>v><>><^>>v>vvv>v<>><>>v>^<><>>v^>><>>vv<^>^>>>^>^><vv^<^>v<^vvv><><v<<<>>^<<^>^^^^vv><^><^>><>^<^<>><>^^v^^<v>>>>^vv^vv>^<^vv<<v><>>^<<>^^^v<><<^^>>v>vvv><^>>^^<<>>v<>v^<^>>><v<v^^<<><><>^v><<<>v^vv<>^<v^><v>^><^^>^^<>^>^<^^v><^v<v<<>^<<^>^^v<<<v<<v
    v<<<>v^<<v><v^v^^<vv<v>^v<<^<^v><^v<>^^v><v^^>^<vv<^v<v>>>>^v<<vv><>><^^vv^><<<^<<^v>v><><>vv>^<^vvv<^v>^<v<v<<<>>><>>^vvv<v^^<>v^<>v^><<<>><<<<^^><<v^vv^v^v<vv^^v<><<v<<^^<v^^><><^v^>>vv>vv>v^^^vv<>>v<vv>v<>><<^^^<vv^>v^><v<<v<<vv^<<>>>vv<><^^>^>vv^^><vvv^<><^<>>^><v<<<^>v^<><^^>vv<<vv<vv<^^^vvv^>><><<v>^vv>v<<^>vv^^>^v>v>v>v>v<>>vv<<>>^><<v>><<>^<vv^<>vvv<>^v>^^v<^v<v><<v^<^>v>^^v<<>^v<>>vv<^^^<^<><<v^<^>vv^^v^>>vv>^^>vv>^>vv^>^^v^>v<<^>vv>v>^<^<v><<v>>>v><^<<^^<>^v^><^<<v<v<vv^v^^^>v^v<<<v<v>^^<><^>^>>>v<v<^<><<v>^v^><<^<v>^<v>^v^<<^><v>v^<>^vv><vv^^^<^><<<vvv<>v>v<<><v>^vv<v<^<>>>^^v^^v<<^v>v>>^>^<v^>vv^v>v>^<>>v><v>>>><^v<v^>v<<vv^<v>>><>^<<>><<<>><>>>^^>^<vv>>v<>v^v^^<<><^^><v^vv>v<vvv^<v^>^v>v>v>^>^v><^<v<><>>v<^>^>>>v^>v><v^<>v>^><v<^^>^<^>>>>^^^^<>^<>>^v^<>v<v^v<<^v^^^v<v<><^^><<^^vv<>^vv<vv<>><><^<^^><^><v<<^>>><>^^>v<<<>^vv<>^>v>>^^><v>^<^>>v<<<vv><v^<^^<<><^<>^v^<vv^<<^^<^v^>^<^^^<^><>><>>^<>^<^vv<^vv^^>v><^>v><^>><^<^v><v<<^^^^v<vv<^>^v><<^<>>^<vv<vvv^v^vv^<vv>>>v<<<>v<^v>
    ^^<<v<^>^v^>>v>v>><v<>^><>vv>v<<^^^<>^><vv^v>>v><<^>^<<^^>vvv<v>^>v>^^vv<^v<v<v^<<<<vvv>v^^<<^><v><^v><vv^vv<^^^>>>v^^vv^><<>>v^<vvv<<<^^>^<>>>^<>^<><>><<><^v^vv^^><^^<>>^<vvv<^^>>vv^>^^<vvv^<^><^^^vv>>>>v^<^<^<>^<^>>^v^^>v^^^v<>><^>^<>^>v^v>vvv^>^<^^>v>^>>v^<^<<>v>vv<v^^>v<^^vvv<>vv^^v^<<>^v>>^<^<v<>^^><>^^v><>v^^^<^>vv<<>vvvv^^><><v>^<>^>>^<v^v<>^>^v>>^^v<><>v^<<><<<v<vv^<>v>>^v><>>v^v<^v<^v<v<>vv^v<^vvvv<^><^v^<>^>^^>v><^^^vv><<^^>v^<^>><>v>v>v^^vv<^<^^<^<v<v<<vvvv^<><<<<<^<>^><>^^><^^v^v>v^><^v^>^<^><^vv><>^vv<><^^^^v^>^><><><><><^v^v<<<>^<<^v>><<<>>>^^<<vv^v><v^<<^>v^v>^<^^<<>vvv<>><<>v><<^><vv<^><v>^>>>^^<>v>v>^>><^>v>v<^<>v<v<><vvv^v>><^^^<>v<>^vv><v><<v^vv<^<v<v^<<vv<^v^v<<^v<v>v<>^^v>v>^^<<<^<><v><vvvvv<>vv>><v<v^<>^>^^>><>^^^<^>^<>^>^<<>v>v>^^vvv^<>v<^><^^^>v<<^>>^^<<^^<v><vv<v>vvvv>v<<<<>v>^^^^<<<v^>v^^>><^><^^>^v^>v<<^<v^><><>>^^^^>^^^^v^^<<^^><<><v<<v<v^v^><><^<^^<>^<v<^<^^^v<>>^><v^vv>><^v<<<>^>^vv^>v>><^v>^<>^^^^<v<>v>>^<vv<^>v<v>v<>>><>^<<<<<<^v<>^>><^^>><v^>>><<v<^^><<
    ^^v>>v^<^<vv^^^><^>v^>><<<<>^^^vv<>v<^v^v<<<v>>>vv^<^vv<^v^^<<v>^^<^v^><>v<>^^vv><^<^v>>vvv<^>v>^><<v>^<>^^>v>>>>^>v^v<v<>>v^v<v^^<<><><>v<>v<^^<<^v>v^<v><vvvv>>v<^v^v<<v^<<><<><^><^vv^v<>>v^>v^^v><^v^>^<<v>^>>vv<vv^<><^^^v^>^^^v^^^<<v<>v>^>^<>^v^><^v^>^^<<>^v<v<^v^v^>>><vvv<^v^>^^^v^<<^^v>^v^^^>>v^<v^vv><>v<v^>^v<<<<v^<^<v>vv>><^^v^v<<^^v<vv><>>v<^<>vv>>^<<v<v^><v^^v>>>^>^>v^>vvv>^^<^>>^<^v<<><v^<v^>^>^^vvvv>^v^<vv>>v>v<^v^<^<^vvv^<>v>^vvvv>>^>^<>v<<<^^^<^>v>^^<^>>v<v<<v<^>^><v<v^<^^v<v<^^<>vv>v<<<vv^vv<^><<><<^^v<^<>>v<<^^<<<><v>v>><^<>>^>>v^><vv<v<>v^v^>^<^vv<^<^>v^^>^^vv<>^vvv>><<^v>>>>vvvvv^^>>^^><vv>>>v^v<><^<>v<vv>^>vvv<<<<>>>^<<>vvvv<v<^<vv^>vv<>vv<v>>v^>^^>^v<>>>><>^v<v<>^<v<^^<>><><<vv<<<<^><<>><v^vv<<v<v><<<v^<<^^>^^^<<<>v^v><v<<<>vv<v>^<v<v<<v>><^<^vv>>^^<vv^^<>v<^^<>>vv<>^^^v>v^<<<><vv>v>vv<<^<<^<><v<^v^><>vv^<<^>^^^<^^<v^^>v><>^<<v^<^<<<<<<v><^>>v<<v>>^^^>>^<<v<vvvv^<v<v>v>v>vv<<v<<v^v>vv>v><><^^^v<^^>^vv<<v^<^<vvv<^<>v>^v>>vvv><>>>v<<>>>>^^<vv^>>>^v^>>v^^>><<^^v^>^<><^<v
    v<>^v^<^>^>v<v>v>>>v^<v>>>v><vvv^><^>^vv^>>v<^<v><v^>>>>v^v^>^^^<>^>^^vvv>v<<^vv>^<>vv<<><><^>>^^^<^^^v<^>^v^^^^>v<><<<>>>vv^^<v>^<^^^>vv<<vv<><>v^<><v^>^<v<>^<<<^<v^^v<<^<vv<><v>v^<<><^^vv<<<<<^<>^>>v<<<vv^<^^v>v^<<v<^v>><^^^>^<^^<<v>><^^vv^^v>^>>>vvvv>vv^^^<>^^v>>vvvvv>><><v<>^v^>vvv>^><^><^^v>v>v<<v>v^<<^v<v<<>>v>><v>^v^<<^vvv^<><>v^><v^<v>^>^vvv^v^^^vv^>^<<<v<v^^^>><^^^<^><<><^>^^v<v<vvv^^v^v>^><v>>><>^^v<<vv><v>>>>>>v>>>^<><v<>v>^vv<vv><^^v>vv>>^><<^^v>^>vv<>v^^<^>>v><<>>^<><><>v>^<v<>>v>>v<<^v>>>><v<>>>v><<><v><^<<><><v^^v^>^v>^>^^v^>v<v>^^>^><^vv^<v<><<v>^<<^^>>^><^^<<v^<^^^<>>>v^^v^^v<vvv<<<^^v^>^>>^<<^^vv<<^<v<<^^v>v<v<<^<<vv>>>vv><^vvv<>^>><>>><>v<vv>>v>><>vvv<^><vvv<v<>^v^>^^vv>^<<<^^vv>v<v<v^<^v^<^v>v^>vv><v^<^^><^^<>^v^<^<><<^>>v>vv<<^^^v^^v>v^v<>^v>>>>>^^^><v^^^^^^vv^vv<<>^<<<<<>^^<v<^v<>^vvv^>v^<<>^<>^v^>^<v^^v<^v<<^<>v><vv<<>>v>>v<vv>><v^v^<><<vv<>>>>>v^>^<<>>><^v^>>>v><>v^>^>^^^>vv><v<v^^v^<><><^v>>^^<vv<<>><>>vv<<>v<>v^^>^<v^<<v<<^^^v>>^^^<><^v<<^><^>><>^v^<<>^<<><vv^
    vv<<^v<>vv^>v^<<<v<v<><<^vv^>v>^<<<>>>vv^><<<v^^^vv><^^><^^<^<v>>>>v<^v<>^<>v^v^vv<^vv^^^<<v<<^v^^^>vv>>v<^>v>^v^>v^^v^^v><><^<<^^^>vv>^<v>^v<vvv^v>v^^<vv<^v>>v^v<>v>vvv^v<>><^<^^<>vv<>>^^<^^^^<>>^>vv>^vv>vv<v^v^v>v>>^>^v^>v<^^<<^>><<<v<v^<^^v><<vv<>^>^<v^^<v<^<>v^v<^>>vv>^>^vv^^vv^<^v<v><v^^<^<>^<>v>>>^v><v><v<v<^<^>^<vv^><^^>>>>>vv^>v^<>><v<>><><^^>>^>^^v^<>><>vvvvv<^><>^vv>>^>>v^v^^<^<v>>^vvv><<<^<<>v>>v<<<<<^>v^^^v<>^v<v>>><v<<^vv<^>>>>>^>>^^^<^<<<v<<^>>vvv>v<v>^^^^><^^^v>vv<v^v^^<v>^^<>^>v<>v<><^v>^^<^<<^^>>>vvv^>v^<v^^<<v^v>^v>><v^<>^v<^^>^<>^<>^v<<>^^v>^v^^v^v>^>>vvv^>><v<^>^^><><^^>>^<^<^vv>>vv><^<<vvvv^^^vv^>><v^^v<^>^v<>v><vvvv^<^^<<>v>><v>><<<v<v>>^>v<>>v^<<<^v<v>v<^v>>>v>v>>>v^v>^^v>^^v^<<<<<>v^<v>>^>vv><^>v<^>>>>>^>v>><vvv<v>>>>v<v^^<^^^><v<v^^v<v^v<^<>^>^^>>v>^^v>>>^<v<<>^<>><v>^<^v<><>^v<^<><<<^v^v^^<<^<>>>><>>^<^v<><vv>>>^>vv>^^^v^>>v>><<<>^vvv^v^v>vv<<<^<<^>><>^v>>^v>^>vv^^v><^^vv^^<>>^v>^^<<^v<v^<><v<><^><<v>><<v<v<^<<<<v^<<v>>v>^<^^<^><^^vv^<<<><>v>>^v^<>^>vvv>>vv<<v
    ><v<vv>>><<<^v^>>v>vv><><^><v^^>v^<<<vvv^<>^v^^^^<^<>vvv^<vv>^^>^^v^v^>^>^v<v<>><<>^v^^v><v<><vv<v^>v>>>v^^>^<>v>v>^vv<<>vvvv^v>^<>^<^^vvvvv>>>v>^^^<>>v<^>vvv<<v><v>^v^vvv<>v<>^v^v>v^^<v^v^v^<^v>vvv^vv^^^<>>^^<^>vvvvv<<^^v>^>v<^^vv^>>>^v^^<v^<>^>>>>>^<vv<<<><v><v>>^v>^vv^<<<<>v>vv<<^<^>>>vv><v^<v^v<<<^>v>^<<><v^vvv><<^vv>^^>>>v>><^^><v<v>v>v>^>v^>>v^v>^<><<^<<<>>^>v<^<v<<^><<v<v>><vv^<>>vv><^>^v<v^<<>>v<vv>^><<^<>v><^<>^<<^^<<<^v^>^^><vv^^^>^><^^^^<vv>v^^v><<v>>vv^>><v^v^^>v^<v^^>^^>^^^^<<^>^vvv<^>v^^>>>>><<>v>v^v><^>><v<vvv^<^>^^^>v^<<^<^^<>><^^v<><<<>^><<>v^<<^^>^v>v<<<<<>><^^^^>v>><^v>v>>^>^v^v>^vv>^vv<<<^><v<^v>^v<^<><^><^>^><<vv>>>^^^^vv><<v><^^v<v^v^>^<>v>^^^<v<v>><^^^>v^^<>><^<<>>^>>v>^v>>vv^^v>v<v><v^^>><vv>^^<<^vvv^v<^vvv><v><v<><<><<<^<v^^<v<^>vvv>v<<<<^>>^^<vv<>>^v^vv<^v><<>><vv^^><vv>vv><v^v^vvv>^v^v>><vv^<>v^vv>v><v>^^v<^^^<^<vv^vv<vv>v<>v<>v^><vv^^vv>^v>v<^^>>>v><>><^^>^^><^^<>>><^v^>v<vv><<v<<^v<^v><^^^>^v<>v>^v^><vvvv^v<>>><<<v>vv^^<^v>>^<<>v>>>>^>v<^<^^^vvvv>^<<<<^>^<>
    v<^vvv>vv<^v<v^^v<vvv>^^>^<v<^vv><>v<>vv^>^^><^^<<<^<>v><v<>><^<>^v^<><><<<><<v<<>vv><<<vv^vv^>><v^<^<^>><v><<<v<<v^<^<vv<<v^^v^vv>^v<^^v<<^>>^<>^<<^>v>>v^>><<^>vv^v<vvv>v^<^^^^<<^^^>><>v^^v><vv^^v<^>^v^v>v^^^v>v^>>^>v^^^>^>vv^vv^<><^vv^^<^v>v>v>^^^v<v^>vv><>v>v<<v^^>^<v^<>>v>>><><^<<v^v>^^<><v^v<<v<^<<^<<vv<^^v<v<vv>>>^v^^^v<^^>^>>>^v^<v^>>>>^>>v<>^^<<<vvvv^><<^^^<><<<><><>>^v<>^>>^^><<><<v<<<^v^v^^^<<<<>v>v>><<><v<v>>^vv^>v<<<<^><v>^<<v>v<<v<^>^<^^><<>^v^>>vvv<<>><<vv>^v^^^^<^^><>v^<^>>>^v^<^<v<vvvv^>v<v^v<v>^v^v>v<<^^v^<<<v^><v>>v><^<vv>>v<>^vvv<<<<v^^>vv>^>>^<<^<<>v>^^vv<^<vvv^^<^v<v^<>>v<>><<>>><<v^<<^^v>><^<<^v><^<^^^<^>>>>v>>^<<<vv><v<vv^<><<>^vvvv>v>v>>v><^v^>v>v>^v>vv^<<vvvvv^<>^^<v><^vv<v>^>v><<v>>^>^vv<<<^<^>vv^<<><><>v^><vv><<>v^v>v>>v^^^<<v<^>v>v^^<<v<>>^>>^<v<v^v<>>>>>^<>^v<<><v<<^>^^^^<^v^^^<>>^^><v>>>v<><<<^<^<v<^>^vvv<^^><^v>^^vv<<^<<<^v^^>v<<vv^<v>v>><^v>vvv<v>>^>v<>>><<><<><<>v><v^^><><<<^<^<>>v^>>^v<>vvvv^<vv>>v<vv^v^v^^><<v^<<>^>v^v<<>v^v<v<<^<>><<>>><<^vv<<v>v^<^v
    ^<^^^<><^^v<v><<^<<^<<>>v^<<vv<<^^><>^^^<>><>>>^<vvvvv^<>v<v^^>><vvv^<>v^>^<><v^^>>^><>>^v>^vvv^^vv<v>>v^<v<vvv>v<>^>>vv^^>v<vv<<<>v^^^<<>>>>v<<^v^^^>^<^^><>><<vv<<><>>v^vv>^><<><^^>><vv^>>>^<^v^<<<>^^v<>vv>>><^^^<v>v<>><<vv<>v^>v>>v<>^^><>^>v<^>^vvv^>^>^vv<^>^>^v^>v<<v<<^^^>>>>v>>v<<<<^<^<v><><vv^>>><>vv<<v^v><<^^v<^<>^v^v^>vv^>v^vvv><<v^<<^^v<<^^<<><vvv^<>vv>v<<vvv>^<v^v<>v>^^vvv><^v<<v^>><^vv^^^^v>>>>v<<>>>v^>^^<<>^^v^<^<^vv^>>><v^>>>^v^>v^^<^>vvv<>^^v^<v<<v^><>^^<><>^^>^^vv<><^>v^<><^vv^v>^>v<>v^^v<^>v><<v<<<<v^^^><vvvv^<v<>^v>^^v<<^<vv^v^<<<vv>v^v<^v^^<<^>>v^v>^v^>^<<^>v<<><^<^^vv>^v<vvv>v^^<^>^<^<^vv^^v>^<^^v<><^<^^vv<v^v<<vvvvv^v><v<<^^vvv^v^^>><>vv><<<><^>^v<>vv>vv^>^v><v<v<v<><v<<^<>^>>vv^v><^>>v><>^^v>^<<><vv>^<^v><>>>^<vv>^^vv^>><>>v<<><>vvv>^^v><^<>>>^>v><^>><^v>^<<v<^vv>^v^<^v<<v<v^>>v^v<^^v<^<v>>>>^v^>v^>^>>v<^^><<><><^>^>>^>^^^^^v^^<vv^>v^<>vvv^><v>^<><vv><>v<<><vv>v^<>v<^^^vv<v^<>>>>^vv>v<<>v<>^>>>v>^v>>^<<<><^vv^>>>>v<^v<^>v>><<v<<vv<<>^<><<>^v^<>vvv>vv<><v>>v^><<<<v^>
    >vv>v<^vv^^v>v>^^^<>vvv<^<>vv^^vvv>>v^<><^v<^v>>>>>^<<v<^^^><vv>^<^>v^^>v<>>>v^v>^^v^>^v<v>v<><>^^<^^^^>^^<<><^<>^^>v><^v>v^>vvv>>^>v><^<<>^v<v<>^^vv><^^<v<^v>>>^v>>>^<>v^^><v^<^<<<<<v^<^vvvv<v<v<<v><v>^<vv<<<v>>v^^<>v>^<>>^>v^<^<^^v^^<<v<^>^vvv>^<^v^<^<<vv<v^>>v<<vvv>>><v<v<vv^v><><>>>v><v><v<<^^>^<><><><>^<^<^^<<<v<v>>^vv^>>>v^>^vv^>^>^<v>><^v<><><>>>>v<^v^>vvv>v^<^^^>v^<^<^v<v^v<^<v<v>>>>v^v>>>^>vv^><^^>vv<^^<v>>^>^^<vv<^<vvv^v>vv^<><>>vvv><vv^<^vvv><^^>>v^^^<>v^^<<>v>>^<^v<>^>>vv<^vv>^^>>v^v^<<^^>^<<^^^<>v^^^^^<^^v<>^^><<><^^<^^>><^>>>>vv<v<v^<><>>^<vv><<vv>vvv^^vv^<><v<>^<v^vv><^vv>v<>>^>^<v<>v<>^<>^><^<^>vvv^^^<>v<>^v^v<^^^vv<><^<v>v^<^^<v>^<<<<>>^><<^v>vv<<^v<v<>v^v>vv<^v>^<<^^>vvv<>v^><^^>^^<<<<^vv<v<<^^<^<<v^>>v<^v>vv<><^v^vv^<<>^<>>>^>>>^><<v><^<><><^<^<>>v<^>v<^><<v<v^><><v^v^<v^vv^v<v><<<>^^><vv^^v>^vv>^>v^<v>><<<<^>^^<<^v<<<^>vv><<<>^^<^^^<v<>>v^>^>v<>v<^><<<v>^>^^>v^^<vvv<>^>vv>v<>^>>^v<^<><<>>>v^v<><<vv^><^<>>><vvv<vv<v>vv^><^<^>>^<>><<<^<^vv^<<^>v^<<><>>^>>v><<^>><^v<>^
""".trimIndent().lines()
