package aoc2024.day18

import aoc2024.Coord
import aoc2024.Grid2d
import java.util.stream.IntStream.range
import kotlin.streams.toList

fun main() {
    testData.let { lines ->
        val coords = lines.map { line -> line.split(",").map { it.toInt() } }.map { Coord(it[0], it[1]) }
        println("Part 1 test: ${part1(coords, 7, 15)}")
        println("Part 2 test: ${part2(coords, 7, 15)}")
    }

    actualData.let { lines ->
        val coords = lines.map { line -> line.split(",").map { it.toInt() } }.map { Coord(it[0], it[1]) }
        println("Part 1: ${part1(coords, 71, 1024)}") // 286
        println("Part 2: ${part2(coords, 71, 1024)}") // 20,64
    }
}

fun part1(coords: List<Coord>, size: Int, bytes: Int): Int {
    val grid = Grid2d(size, size, '.')
    val startPos = Coord(0, 0)
    val targetPos = Coord(size - 1, size - 1)

    coords.take(bytes).forEach { grid[it] = '#' }

    val queue = ArrayDeque<Pair<Coord, Int>>()
    val visited = mutableSetOf<Coord>()

    queue.add(startPos to 0)
    visited.add(startPos)

    while (queue.isNotEmpty()) {
        val (current, distance) = queue.removeFirst()

        if (current == targetPos) {
            return distance
        }

        for (neighbor in grid.cardinalNeighborsWithinLimits(current) { grid[it] != '#' && it !in visited }) {
            queue.add(neighbor to distance + 1)
            visited.add(neighbor)
        }
    }

    return -1
}

fun part2(coords: List<Coord>, size: Int, skip: Int): Coord? {
    val list = range(skip + 1, coords.size).parallel().takeWhile {
        val res = part1(coords, size, it)
        res != -1
    }.toList()
    return list.lastOrNull()?.let { coords[it] }
}

val testData = """
    5,4
    4,2
    4,5
    3,0
    2,1
    6,3
    2,4
    1,5
    0,6
    3,3
    2,6
    5,1
    1,2
    5,5
    2,5
    6,5
    1,4
    0,4
    6,4
    1,1
    6,1
    1,0
    0,5
    1,6
    2,0
""".trimIndent().lines()

val actualData = """
    44,51
    65,43
    65,40
    35,54
    66,67
    41,59
    55,55
    23,2
    45,57
    69,36
    34,53
    37,2
    38,1
    37,55
    41,67
    58,55
    3,26
    9,14
    5,28
    54,51
    56,69
    48,47
    46,53
    64,29
    61,56
    69,64
    45,7
    69,38
    39,54
    50,47
    27,14
    33,1
    67,50
    9,19
    56,57
    64,53
    15,21
    63,50
    3,42
    63,65
    43,52
    9,26
    59,31
    1,29
    30,59
    59,53
    13,0
    65,35
    5,24
    19,35
    47,9
    4,21
    24,11
    53,66
    35,2
    26,9
    56,51
    70,29
    69,56
    53,55
    69,67
    58,67
    8,9
    37,62
    55,9
    35,3
    69,35
    55,15
    59,69
    51,9
    61,5
    5,29
    66,45
    5,23
    63,37
    59,17
    69,55
    63,61
    17,18
    57,8
    15,17
    63,60
    53,61
    33,3
    67,68
    23,3
    9,20
    19,7
    1,5
    53,9
    68,47
    21,14
    32,13
    19,5
    6,19
    25,9
    43,58
    4,7
    25,16
    67,37
    0,29
    59,65
    47,55
    8,1
    11,19
    49,5
    57,48
    59,59
    65,39
    36,9
    43,5
    66,29
    55,54
    53,5
    1,35
    6,11
    68,31
    70,69
    20,21
    61,8
    65,46
    47,0
    49,41
    3,11
    30,25
    55,11
    29,4
    33,62
    15,13
    1,18
    1,37
    39,60
    65,53
    67,48
    67,49
    52,49
    25,3
    55,43
    0,21
    13,6
    41,53
    38,9
    17,17
    47,41
    41,6
    45,54
    56,17
    1,38
    6,23
    67,56
    2,11
    69,49
    1,33
    32,5
    41,50
    66,61
    5,21
    21,11
    3,29
    59,16
    49,55
    22,9
    34,1
    15,10
    57,3
    69,57
    18,3
    43,59
    24,15
    65,25
    53,11
    35,56
    39,12
    53,52
    65,30
    38,51
    25,7
    33,10
    63,36
    7,14
    5,25
    50,37
    51,5
    1,23
    53,6
    47,3
    61,41
    27,3
    61,26
    51,7
    39,61
    13,25
    1,31
    35,62
    50,43
    29,6
    53,15
    61,63
    45,5
    11,29
    27,6
    31,14
    55,41
    29,5
    5,5
    25,11
    61,35
    4,13
    34,17
    11,21
    69,69
    63,55
    50,45
    49,46
    1,13
    57,39
    63,47
    55,62
    37,61
    1,32
    7,30
    13,9
    51,47
    49,1
    19,23
    2,45
    51,62
    51,53
    57,63
    54,45
    47,58
    33,59
    3,37
    51,59
    39,3
    19,4
    61,60
    9,21
    67,69
    45,56
    53,57
    1,0
    23,13
    5,1
    27,7
    45,51
    56,45
    33,64
    56,13
    7,33
    20,13
    36,5
    57,55
    9,16
    57,65
    45,48
    47,48
    59,45
    65,29
    11,27
    55,67
    69,63
    17,2
    63,41
    10,27
    9,27
    69,47
    25,0
    57,51
    55,53
    65,48
    15,20
    43,9
    55,47
    62,37
    35,59
    22,17
    25,24
    18,7
    63,38
    40,63
    69,58
    59,43
    3,1
    34,7
    42,59
    41,51
    65,27
    7,21
    9,39
    51,49
    7,5
    25,2
    32,63
    8,3
    33,9
    54,9
    67,61
    67,38
    62,33
    17,1
    44,55
    2,27
    7,19
    2,47
    1,9
    7,9
    41,1
    39,10
    61,25
    35,61
    63,59
    21,13
    29,11
    58,51
    37,3
    17,12
    7,3
    7,29
    37,59
    65,69
    67,31
    19,13
    61,66
    23,9
    43,49
    50,9
    43,8
    61,64
    49,8
    15,2
    57,43
    23,15
    64,43
    39,52
    33,57
    8,29
    57,53
    11,6
    65,31
    65,67
    28,17
    9,31
    41,56
    22,1
    51,11
    66,69
    43,6
    1,19
    33,13
    53,41
    68,35
    43,3
    65,51
    44,3
    57,17
    61,12
    61,65
    44,1
    57,13
    13,10
    61,53
    9,15
    11,9
    29,12
    64,65
    45,41
    67,59
    55,12
    67,33
    56,41
    10,1
    5,14
    19,10
    53,43
    69,31
    3,21
    57,14
    37,47
    35,9
    10,5
    11,24
    66,41
    40,1
    59,39
    65,64
    63,70
    64,67
    13,35
    7,35
    4,11
    17,7
    43,60
    53,3
    32,55
    60,49
    39,48
    63,33
    7,6
    63,53
    69,37
    61,48
    45,45
    61,62
    16,7
    54,3
    13,11
    67,29
    47,59
    23,12
    32,3
    11,25
    63,63
    69,39
    29,7
    49,51
    62,43
    55,39
    3,51
    5,17
    45,3
    17,9
    13,1
    55,33
    47,2
    53,14
    13,19
    61,61
    1,39
    29,15
    5,35
    39,5
    1,1
    53,64
    47,56
    48,55
    59,67
    23,7
    59,63
    68,29
    31,13
    28,13
    30,3
    3,3
    31,17
    43,57
    51,4
    69,34
    3,47
    9,3
    69,54
    17,15
    61,34
    3,43
    39,9
    57,10
    58,43
    63,31
    52,55
    47,49
    25,6
    48,53
    38,57
    17,8
    63,69
    69,65
    50,7
    61,54
    63,67
    55,6
    65,52
    17,3
    51,51
    67,47
    57,5
    10,11
    57,41
    69,44
    29,61
    5,2
    9,11
    17,13
    52,45
    52,47
    57,15
    53,63
    1,7
    54,47
    7,16
    47,7
    9,1
    67,42
    39,1
    53,10
    41,69
    52,13
    57,60
    61,46
    67,60
    5,9
    54,61
    11,32
    55,68
    6,27
    26,3
    66,55
    49,66
    69,32
    61,3
    26,11
    5,44
    57,47
    3,19
    61,58
    12,11
    31,6
    11,34
    49,47
    68,41
    0,5
    61,38
    41,57
    68,45
    39,58
    1,10
    63,29
    55,59
    69,66
    9,24
    63,57
    65,54
    11,3
    48,5
    37,49
    40,7
    2,13
    57,31
    61,51
    33,58
    55,20
    49,53
    35,5
    67,43
    25,1
    61,13
    2,7
    6,9
    20,9
    5,7
    40,3
    7,13
    43,53
    67,65
    66,65
    37,57
    2,21
    13,17
    65,37
    13,3
    50,51
    31,10
    5,18
    35,7
    65,32
    7,26
    7,18
    11,8
    56,65
    65,58
    53,53
    55,44
    9,9
    13,8
    67,35
    38,7
    32,7
    69,29
    22,15
    58,63
    27,13
    29,10
    19,3
    62,67
    15,7
    57,59
    40,51
    61,47
    69,59
    31,19
    27,5
    59,11
    68,67
    15,5
    68,53
    44,43
    60,35
    61,11
    33,63
    40,47
    7,1
    39,51
    62,69
    18,11
    31,55
    44,59
    63,46
    67,67
    37,9
    3,28
    5,32
    11,18
    65,38
    16,21
    3,16
    55,45
    59,56
    66,35
    18,1
    11,11
    61,37
    9,13
    9,23
    48,43
    3,5
    8,19
    3,9
    13,5
    47,61
    5,43
    7,12
    39,55
    46,59
    31,9
    45,59
    27,4
    11,7
    17,5
    7,27
    12,15
    11,20
    12,3
    25,17
    2,3
    13,13
    64,57
    55,3
    3,4
    37,4
    31,3
    44,7
    51,65
    65,61
    69,52
    9,34
    25,5
    24,13
    59,57
    11,1
    3,30
    37,63
    22,21
    42,49
    7,25
    1,24
    39,59
    17,11
    14,13
    19,6
    61,40
    7,15
    13,7
    51,39
    47,57
    4,25
    1,45
    25,15
    9,7
    7,34
    48,11
    66,57
    57,7
    51,55
    57,61
    39,53
    14,1
    31,7
    59,66
    3,23
    41,49
    65,49
    68,39
    37,5
    49,50
    11,15
    59,33
    11,22
    15,4
    23,5
    39,63
    54,11
    21,3
    41,4
    30,63
    13,15
    5,13
    67,39
    61,14
    61,69
    61,42
    42,55
    49,3
    21,8
    23,10
    50,55
    59,35
    69,43
    6,29
    21,15
    45,1
    5,38
    54,15
    55,37
    5,15
    42,11
    46,5
    67,57
    60,13
    47,47
    63,39
    51,3
    1,20
    15,23
    37,58
    35,58
    36,7
    27,9
    49,11
    47,1
    57,49
    50,3
    8,11
    65,26
    2,33
    61,59
    37,53
    6,7
    65,41
    57,64
    16,5
    55,26
    55,4
    13,4
    16,13
    56,63
    54,59
    54,55
    0,7
    7,23
    37,1
    5,10
    19,9
    49,49
    66,33
    65,55
    23,11
    61,31
    27,11
    57,70
    10,13
    62,11
    55,66
    58,11
    69,41
    18,17
    51,52
    14,7
    59,62
    58,13
    3,27
    51,60
    67,52
    57,37
    64,35
    4,33
    44,49
    55,16
    68,61
    15,15
    52,3
    41,61
    0,39
    37,50
    59,51
    3,34
    4,5
    47,44
    59,60
    9,4
    27,1
    46,47
    35,60
    45,55
    53,50
    65,63
    8,5
    30,53
    37,7
    35,64
    51,35
    14,33
    31,8
    39,49
    51,41
    49,7
    3,15
    55,13
    53,13
    57,52
    3,8
    1,15
    4,19
    8,23
    3,25
    62,57
    53,7
    3,31
    33,61
    69,61
    36,53
    61,39
    57,9
    15,25
    57,18
    26,13
    7,31
    61,45
    34,5
    41,54
    56,3
    2,17
    63,43
    63,62
    69,33
    69,51
    59,13
    29,9
    59,41
    55,19
    11,2
    33,5
    25,18
    1,11
    45,61
    37,54
    62,55
    42,65
    22,5
    42,3
    41,52
    31,63
    9,33
    30,15
    52,7
    23,4
    57,42
    3,17
    45,53
    57,67
    64,61
    3,38
    39,57
    65,59
    43,51
    40,57
    45,37
    63,64
    1,3
    0,43
    6,3
    3,36
    1,36
    62,51
    12,17
    3,35
    16,9
    25,13
    35,63
    53,45
    68,63
    55,40
    68,59
    69,45
    4,15
    55,42
    65,33
    21,9
    5,11
    19,1
    46,51
    3,22
    60,43
    70,49
    31,5
    47,6
    64,41
    13,12
    7,22
    7,50
    48,61
    48,51
    1,41
    28,7
    3,41
    45,49
    36,63
    42,1
    5,36
    52,41
    39,4
    5,33
    60,67
    48,7
    24,23
    57,11
    27,17
    48,3
    11,37
    47,51
    38,61
    1,26
    9,25
    64,33
    11,5
    21,7
    63,35
    1,27
    24,7
    15,9
    59,61
    9,22
    15,3
    43,1
    56,7
    67,55
    7,7
    44,45
    63,45
    55,7
    59,58
    29,55
    57,45
    67,53
    31,62
    57,58
    52,9
    7,17
    4,31
    51,45
    9,8
    58,45
    70,47
    21,4
    55,17
    67,63
    55,65
    33,7
    41,5
    3,39
    5,31
    53,47
    59,55
    55,51
    59,68
    35,57
    65,47
    56,55
    27,8
    11,13
    2,41
    3,2
    29,13
    51,61
    59,15
    38,47
    45,67
    45,4
    4,1
    50,11
    51,54
    47,53
    69,53
    65,50
    51,63
    67,45
    11,31
    1,25
    3,40
    63,49
    39,7
    23,1
    30,11
    21,1
    59,37
    57,69
    34,3
    69,27
    9,29
    41,60
    58,53
    70,41
    38,15
    55,5
    43,55
    60,53
    60,45
    43,46
    49,35
    24,5
    1,43
    15,1
    67,51
    37,11
    20,1
    59,9
    68,25
    57,29
    45,15
    54,37
    17,29
    17,52
    4,43
    29,23
    13,46
    41,65
    65,16
    43,41
    17,23
    11,46
    34,67
    35,37
    5,47
    35,40
    41,29
    12,69
    15,67
    53,35
    42,43
    47,22
    35,49
    17,40
    13,59
    49,59
    33,29
    47,31
    11,54
    27,65
    69,3
    67,7
    53,29
    24,45
    57,28
    21,25
    62,21
    31,29
    23,59
    41,47
    22,55
    22,43
    67,26
    29,41
    41,25
    59,23
    43,35
    33,56
    7,69
    23,25
    31,1
    23,65
    41,45
    31,39
    47,63
    25,45
    67,13
    40,41
    46,45
    27,43
    23,49
    37,19
    67,27
    66,23
    46,31
    37,13
    35,47
    19,30
    20,47
    9,38
    64,7
    29,30
    19,45
    56,21
    35,66
    5,45
    33,49
    39,34
    47,65
    17,31
    13,51
    45,22
    45,42
    55,23
    27,55
    37,28
    41,37
    51,36
    57,33
    29,29
    35,53
    1,62
    49,69
    47,32
    4,59
    23,51
    61,33
    15,50
    37,24
    1,57
    46,27
    11,43
    29,35
    20,25
    37,29
    18,41
    31,34
    29,37
    46,29
    23,57
    9,69
    43,68
    37,65
    49,18
    4,61
    58,1
    20,33
    21,12
    46,65
    63,21
    31,27
    27,51
    5,64
    27,48
    26,59
    29,24
    18,35
    15,40
    6,59
    41,21
    46,35
    10,57
    29,19
    45,70
    62,17
    59,36
    25,66
    42,47
    26,49
    53,21
    59,21
    13,55
    51,40
    17,49
    47,34
    31,50
    2,57
    27,37
    55,49
    28,69
    64,25
    34,37
    57,24
    61,17
    43,11
    59,10
    27,42
    9,42
    13,58
    55,35
    35,46
    19,31
    28,65
    15,45
    31,47
    29,45
    67,41
    7,46
    41,63
    39,39
    67,20
    67,21
    37,18
    6,43
    47,33
    57,26
    26,45
    27,45
    35,39
    40,17
    48,21
    65,23
    42,33
    9,37
    29,25
    63,16
    53,24
    31,18
    25,37
    67,11
    43,39
    31,46
    3,55
    13,44
    14,49
    9,35
    55,57
    33,51
    37,69
    7,59
    1,17
    68,3
    64,13
    31,23
    33,53
    43,40
    2,67
    28,47
    43,65
    47,35
    63,3
    35,20
    19,26
    35,17
    11,33
    51,34
    20,29
    12,59
    43,14
    57,34
    33,39
    42,27
    7,61
    45,17
    11,63
    33,14
    4,55
    39,38
    9,43
    27,53
    14,37
    17,65
    31,45
    55,29
    10,47
    19,51
    29,17
    25,69
    18,25
    11,62
    65,21
    23,38
    1,66
    30,49
    9,61
    63,23
    23,39
    11,49
    47,67
    6,69
    31,48
    14,21
    21,57
    24,59
    18,29
    47,27
    25,21
    55,69
    63,7
    35,13
    23,63
    13,33
    15,11
    15,39
    25,36
    43,20
    65,1
    23,21
    20,55
    47,14
    45,36
    51,67
    22,41
    17,47
    16,31
    60,5
    43,36
    8,37
    43,15
    21,5
    10,53
    45,62
    51,17
    57,23
    22,31
    15,16
    31,32
    23,20
    55,0
    25,44
    55,24
    43,47
    5,57
    62,23
    8,61
    27,34
    20,57
    40,33
    15,65
    43,7
    24,29
    44,27
    27,63
    5,63
    57,21
    61,24
    23,29
    49,21
    55,25
    27,50
    17,25
    7,48
    5,53
    37,43
    13,41
    21,70
    8,45
    34,33
    58,47
    6,55
    3,57
    70,23
    59,1
    13,18
    33,60
    46,39
    35,27
    11,41
    33,55
    42,67
    19,48
    17,51
    17,50
    44,31
    68,17
    39,30
    1,63
    29,21
    9,47
    55,27
    33,27
    53,51
    21,21
    9,63
    25,33
    7,58
    53,38
    46,19
    18,69
    21,49
    40,39
    5,37
    53,67
    39,31
    33,12
    7,57
    1,49
    1,55
    19,36
    29,43
    13,50
    55,63
    51,37
    1,56
    41,41
    29,53
    24,63
    27,33
    70,9
    24,19
    2,65
    39,37
    36,35
    25,52
    32,27
    9,51
    25,19
    45,63
    15,33
    19,21
    49,13
    39,13
    65,15
    3,54
    11,57
    49,24
    41,14
    15,29
    15,27
    1,60
    31,25
    30,39
    3,58
    11,44
    13,53
    27,38
    35,33
    43,31
    53,34
    61,67
    25,27
    13,63
    21,59
    17,21
    21,55
    48,17
    5,51
    24,49
    54,49
    50,29
    31,43
    53,19
    62,31
    43,45
    19,41
    17,33
    31,59
    65,28
    26,25
    3,65
    51,25
    59,27
    20,69
    31,60
    47,11
    24,35
    39,23
    48,29
    17,44
    33,50
    61,49
    67,22
    15,26
    15,55
    52,25
    51,0
    67,10
    43,19
    45,33
    39,11
    13,49
    49,33
    3,70
    33,34
    51,13
    25,31
    32,67
    14,45
    45,34
    17,53
    43,21
    53,36
    55,31
    3,13
    1,48
    18,15
    17,69
    29,36
    11,48
    45,31
    29,39
    60,7
    22,27
    41,38
    21,47
    67,8
    49,19
    43,37
    41,33
    53,49
    21,53
    25,60
    33,45
    49,31
    13,22
    53,30
    37,12
    37,31
    27,61
    29,26
    5,52
    33,26
    35,70
    33,69
    39,25
    9,55
    29,1
    5,3
    69,15
    31,22
    9,32
    53,20
    23,33
    31,33
    6,63
    64,15
    69,23
    49,39
    48,27
    41,3
    7,38
    59,47
    65,10
    7,66
    17,24
    67,9
    25,22
    0,63
    7,37
    9,36
    9,17
    69,25
    27,39
    42,25
    11,23
    57,25
    31,67
    51,20
    35,55
    11,17
    9,57
    11,67
    35,29
    5,27
    16,57
    27,36
    21,63
    10,51
    52,17
    34,11
    3,45
    49,67
    37,37
    43,12
    53,18
    5,66
    26,17
    17,67
    19,60
    28,19
    31,70
    27,20
    11,68
    8,67
    33,38
    15,47
    51,29
    33,23
    47,23
    11,30
    45,69
    33,16
    42,19
    8,55
    25,67
    23,19
    69,7
    35,31
    28,29
    67,17
    13,31
    26,53
    19,66
    27,57
    45,65
    33,11
    5,65
    1,69
    22,67
    13,65
    51,38
    3,53
    70,27
    17,30
    25,59
    19,64
    21,19
    36,33
    36,27
    53,56
    65,24
    39,18
    37,36
    15,64
    15,28
    53,28
    45,27
    33,47
    31,69
    23,67
    5,59
    53,59
    35,19
    29,22
    47,43
    59,7
    35,42
    54,31
    21,45
    25,53
    20,51
    69,8
    12,39
    29,54
    10,45
    16,67
    29,58
    61,15
    63,2
    27,15
    57,36
    60,31
    30,43
    21,51
    21,38
    21,42
    63,18
    29,47
    17,57
    67,15
    65,3
    16,47
    13,54
    37,45
    45,14
    48,57
    25,55
    55,32
    47,17
    19,19
    41,39
    7,52
    7,45
    51,23
    53,22
    23,37
    11,56
    68,15
    50,33
    35,67
    33,24
    23,64
    15,51
    15,36
    26,61
    57,32
    15,35
    39,17
    16,39
    11,65
    13,14
    33,17
    43,25
    40,45
    63,25
    19,32
    41,8
    44,67
    3,59
    41,19
    36,13
    14,17
    17,39
    23,54
    25,63
    28,33
    19,47
    16,33
    29,65
    33,20
    31,49
    65,2
    34,45
    27,2
    11,36
    48,41
    45,25
    3,69
    42,69
    43,17
    61,29
    65,7
    31,51
    40,27
    14,63
    27,31
    10,17
    59,25
    37,17
    4,49
    19,29
    25,51
    26,21
    15,63
    64,9
    24,27
    69,5
    30,67
    3,7
    51,69
    19,57
    4,51
    48,35
    34,23
    59,3
    26,31
    37,23
    7,65
    66,7
    68,11
    42,17
    52,67
    24,41
    49,17
    33,67
    29,69
    69,19
    39,64
    14,57
    49,43
    19,22
    53,39
    47,25
    33,35
    63,9
    20,19
    58,5
    35,25
    60,3
    19,53
    22,65
    38,31
    56,49
    19,44
    67,3
    13,28
    27,41
    31,56
    50,1
    14,25
    27,23
    26,67
    65,5
    17,58
    15,61
    48,25
    35,15
    29,16
    39,67
    31,37
    57,2
    54,39
    38,13
    29,40
    23,52
    39,47
    21,29
    36,67
    25,39
    3,67
    40,13
    12,25
    59,38
    17,22
    44,63
    9,59
    47,29
    52,31
    29,67
    3,33
    21,16
    12,35
    29,59
    2,51
    47,5
    53,37
    44,19
    28,39
    45,35
    15,49
    7,39
    56,35
    31,21
    17,62
    15,59
    63,10
    63,11
    49,57
    3,49
    35,51
    43,30
    11,38
    36,43
    13,29
    47,13
    9,62
    43,23
    50,27
    12,41
    63,13
    29,3
    35,69
    12,65
    46,25
    16,27
    7,43
    47,68
    63,19
    41,7
    56,29
    58,15
    63,51
    45,9
    25,61
    36,15
    33,30
    61,22
    45,24
    59,30
    53,31
    5,69
    17,27
    27,29
    58,33
    59,40
    35,22
    47,15
    16,45
    37,40
    14,65
    44,9
    27,67
    35,36
    36,17
    33,19
    52,27
    11,45
    64,5
    41,43
    39,69
    9,53
    41,17
    43,42
    17,59
    45,29
    37,39
    21,39
    21,65
    19,37
    21,23
    65,17
    11,35
    35,1
    33,65
    47,19
    37,15
    13,21
    49,30
    20,59
    62,7
    13,70
    1,47
    61,7
    15,69
    5,67
    53,23
    39,27
    49,60
    5,56
    51,66
    37,67
    13,69
    19,39
    33,21
    7,67
    16,15
    17,61
    49,14
    15,68
    25,35
    69,9
    21,67
    46,17
    24,57
    23,68
    1,51
    43,38
    35,48
    39,16
    0,55
    65,65
    19,33
    60,9
    5,41
    67,2
    29,44
    53,69
    41,31
    17,37
    63,17
    7,42
    8,49
    7,32
    6,49
    15,37
    13,57
    33,33
    35,43
    39,29
    51,15
    29,66
    23,27
    23,61
    61,21
    25,47
    18,59
    41,35
    31,52
    30,35
    69,17
    27,59
    13,30
    49,27
    15,31
    11,53
    65,57
    15,56
    30,19
    23,62
    27,27
    24,31
    23,47
    23,23
    23,31
    29,46
    28,61
    17,41
    36,19
    7,40
    38,65
    11,66
    27,69
    37,21
    45,11
    59,18
    7,41
    59,49
    51,43
    19,49
    15,43
    55,61
    49,37
    8,65
    39,68
    61,9
    7,51
    41,32
    37,25
    19,43
    26,29
    19,56
    69,1
    43,34
    67,23
    11,69
    37,35
    69,13
    19,27
    27,19
    60,19
    32,21
    32,35
    11,47
    35,41
    66,5
    28,57
    42,63
    44,39
    51,1
    18,43
    14,53
    27,68
    41,11
    44,17
    18,19
    49,29
    52,43
    35,65
    19,15
    53,33
    27,25
    45,23
    37,51
    67,14
    9,52
    1,61
    50,19
    65,19
    27,32
    38,41
    31,35
    25,46
    53,42
    33,25
    25,29
    16,61
    21,46
    43,61
    13,37
    2,53
    13,38
    59,20
    63,20
    3,63
    13,43
    67,0
    35,23
    6,35
    21,27
    20,45
    39,43
    12,49
    7,11
    9,68
    15,42
    43,27
    41,15
    19,63
    61,23
    66,13
    25,65
    18,67
    69,12
    23,48
    27,26
    17,54
    53,68
    33,28
    21,52
    22,45
    16,63
    21,61
    29,63
    49,68
    44,65
    59,4
    51,19
    51,57
    19,69
    22,49
    37,68
    49,23
    58,21
    47,10
    34,9
    33,41
    25,64
    23,50
    25,54
    37,32
    39,42
    41,9
    29,28
    25,41
    39,21
    45,19
    19,65
    22,29
    47,38
    21,28
    50,15
    5,40
    49,65
    67,19
    36,49
    61,57
    53,2
    32,37
    43,63
    10,41
    69,21
    19,55
    20,39
    64,1
    57,38
    69,18
    17,38
    11,51
    20,63
    57,35
    21,24
    41,55
    51,70
    61,55
    19,61
    40,25
    57,27
    61,1
    39,44
    39,36
    27,21
    41,24
    21,37
    1,53
    51,58
    27,47
    57,57
    39,33
    37,33
    23,55
    40,69
    21,43
    37,46
    49,70
    46,67
    64,21
    41,13
    13,27
    45,12
    50,23
    39,66
    45,13
    19,17
    19,11
    63,1
    19,38
    17,48
    61,28
    22,59
    19,62
    37,44
    21,34
    13,39
    61,43
    37,38
    2,69
    27,64
    12,29
    53,27
    31,61
    2,63
    23,40
    23,26
    59,26
    32,39
    31,11
    43,33
    53,32
    49,45
    63,5
    21,31
    53,25
    17,43
    4,45
    23,32
    5,62
    13,45
    19,14
    57,22
    13,26
    51,27
    69,11
    39,28
    15,19
    65,20
    7,54
    5,46
    31,57
    39,45
    34,51
    7,63
    17,55
    46,9
    29,33
    26,41
    39,22
    67,25
    66,17
    69,6
    47,21
    10,29
    62,3
    16,35
    63,15
    67,5
    62,27
    7,49
    17,19
    20,41
    45,39
    61,27
    34,29
    47,64
    11,61
    51,12
    33,46
    10,61
    19,25
    23,45
    47,42
    21,69
    41,27
    59,29
    22,23
    33,43
    41,22
    31,53
    9,41
    29,31
    49,61
    9,45
    5,55
    33,44
    27,62
    1,21
    23,35
    47,37
    25,25
    44,23
    30,69
    61,19
    22,57
    0,51
    31,15
    53,65
    60,1
    46,13
    25,23
    65,13
    51,16
    17,66
    0,15
    29,51
    9,60
    39,19
    5,19
    39,15
    41,20
    33,15
    51,26
    4,67
    43,67
    51,21
    48,39
    9,49
    22,35
    32,43
    43,16
    21,33
    47,45
    51,31
    28,53
    48,37
    19,20
    57,1
    35,35
    14,41
    23,53
    31,24
    57,19
    21,41
    41,10
    15,60
    70,17
    43,13
    41,30
    35,21
    52,59
    47,16
    55,1
    47,62
    24,33
    35,26
    19,50
    39,41
    30,65
    37,41
    5,39
    51,22
    65,44
    49,25
    20,67
    9,65
    27,56
    39,65
    30,31
    11,55
    5,49
    43,43
    9,58
    14,47
    49,63
    23,69
    67,24
    68,5
    47,20
    45,47
    65,45
    11,59
    5,61
    60,51
    36,23
    63,27
    50,59
    41,36
    55,21
    36,39
    55,58
    7,47
    45,43
    17,63
    32,19
    34,41
    33,48
    30,1
    11,39
    1,65
    43,28
    47,69
    32,41
    39,35
    13,60
    33,68
    49,9
    3,48
    43,69
    12,63
    65,12
    14,67
    12,55
    9,40
    49,64
    65,9
    68,21
    26,57
    38,25
    31,31
    41,23
    43,29
    59,19
    28,51
    19,54
    49,12
    15,24
    63,48
    13,61
    23,17
    29,49
    5,60
    27,24
    60,29
    27,49
    35,45
    17,35
    69,2
    15,32
    26,39
    29,27
    1,67
    23,41
    21,36
    23,43
    62,5
    35,11
    48,65
    25,68
    17,45
    13,47
    31,0
    31,65
    59,5
    13,23
    15,53
    27,35
    41,62
    59,22
    58,39
    53,17
    16,55
    54,23
    15,52
    33,31
    49,32
    9,64
    24,39
    13,67
    25,43
    38,27
    25,49
    36,31
    15,41
    65,11
    15,57
    67,1
    39,20
    29,57
    5,68
    25,57
    31,30
    19,59
    49,15
    31,41
    21,35
    59,24
    32,53
    3,61
    47,39
    7,53
    51,33
    45,21
    1,59
    37,27
    38,21
    57,30
    22,61
    21,17
    33,37
    7,55
    29,42
    9,5
    9,67
    52,63
    19,67
    53,1
    44,36
    34,34
    60,18
    62,64
    38,2
    22,40
    25,40
    28,45
    22,13
    20,37
    28,9
    28,64
    68,0
    0,6
    34,38
    63,40
    66,66
    12,16
    21,56
    26,15
    0,54
    20,44
    52,10
    70,55
    54,33
    22,47
    69,20
    32,36
    21,48
    28,40
    0,14
    2,50
    5,70
    28,8
    15,6
    58,9
    36,51
    46,28
    8,14
    64,6
    0,56
    58,29
    64,55
    26,51
    17,4
    30,54
    14,24
    68,26
    14,35
    68,22
    26,2
    30,38
    17,16
    28,11
    37,14
    62,16
    23,42
    26,68
    42,7
    60,20
    40,49
    14,12
    68,12
    26,44
    28,37
    35,50
    70,43
    66,58
    48,56
    46,30
    34,48
    16,14
    62,15
    41,2
    56,6
    31,16
    50,70
    24,20
    64,58
    59,14
    6,6
    19,2
    10,66
    25,20
    54,46
    41,16
    55,50
    10,28
    42,42
    38,45
    26,34
    59,8
    12,36
    10,23
    58,35
    47,66
    60,46
    40,54
    16,8
    36,16
    56,39
    70,36
    30,4
    35,32
    54,63
    8,36
    38,10
    30,70
    4,41
    0,48
    25,10
    34,52
    20,4
    56,44
    67,4
    38,44
    18,37
    6,60
    60,47
    6,66
    27,18
    35,12
    37,8
    8,48
    18,53
    30,50
    36,50
    35,24
    22,3
    55,8
    20,24
    70,65
    4,68
    25,42
    8,68
    42,5
    30,55
    70,60
    18,33
    23,36
    35,8
    40,28
    34,13
    42,4
    26,32
    54,67
    29,2
    39,6
    68,48
    0,16
    8,35
    18,5
    38,12
    32,10
    48,13
    70,57
    19,0
    46,23
    42,36
    38,35
    39,14
    11,70
    34,59
    24,40
    70,64
    70,52
    45,28
    24,21
    4,6
    66,62
    62,29
    25,34
    34,21
    36,37
    4,54
    39,70
    46,6
    26,58
    30,27
    36,56
    44,5
    59,6
    65,18
    64,17
    38,24
    57,12
    34,12
    66,42
    15,18
    30,13
    69,60
    21,40
    25,4
    54,66
    26,26
    34,39
    31,58
    62,39
    12,23
    2,48
    32,1
    28,32
    62,52
    32,59
    50,18
    17,6
    44,12
    42,46
    48,15
    8,15
    62,13
    28,15
    70,22
    35,30
    48,22
    2,62
    41,64
    10,38
    64,44
    41,68
    48,1
    70,37
    8,39
    24,52
    58,42
    25,8
    6,57
    18,46
    67,28
    36,24
    70,5
    63,42
    6,67
    18,48
    38,20
    14,0
    10,37
    26,24
    70,34
    58,60
    61,36
    64,26
    24,25
    62,46
    44,64
    55,10
    25,14
    4,26
    4,34
    14,44
    42,14
    37,20
    10,33
    37,26
    34,40
    10,46
    52,16
    3,66
    4,35
    66,53
    67,58
    31,44
    70,31
    38,59
    68,28
    40,11
    6,38
    52,29
    28,35
    52,37
    30,5
    14,9
    18,28
    43,48
    68,49
    38,39
    46,16
    13,32
    12,13
    53,8
    52,0
    60,28
    68,9
    52,39
    36,30
    40,66
    13,20
    27,60
    18,10
    46,32
    8,31
    39,40
    70,59
    58,56
    50,34
    60,40
    70,32
    64,52
    30,29
    58,62
    32,56
    35,10
    38,53
    51,18
    56,62
    18,6
    37,30
    44,37
    67,66
    10,30
    1,70
    66,16
    50,36
    28,58
    30,45
    0,70
    28,44
    20,3
    62,28
    30,30
    15,22
    17,0
    48,40
    6,36
    30,23
    13,24
    54,22
    68,6
    67,30
    32,2
    36,11
    37,16
    0,49
    62,25
    8,13
    22,32
    40,43
    44,32
    38,28
    0,18
    30,37
    29,50
    46,69
    32,25
    46,10
    70,1
    1,4
    56,28
    62,18
    55,28
    36,25
    14,36
    38,58
    66,47
    20,16
    50,32
    16,41
    58,2
    54,20
    55,2
    1,58
    30,17
    60,16
    64,64
    68,24
    32,20
    66,44
    50,64
    36,18
    14,40
    12,52
    24,64
    28,50
    14,50
    36,3
    48,0
    50,60
    50,30
    63,24
    70,3
    27,12
    30,0
    42,35
    60,38
    54,34
    31,26
    22,46
    4,63
    38,50
    58,31
    49,56
    16,36
    8,20
    4,66
    62,48
    2,4
    6,54
    13,34
    36,8
    63,14
    38,17
    26,12
    20,32
    52,38
    11,52
    46,1
    48,19
    38,33
    44,28
    56,56
    54,58
    43,4
    64,50
    32,46
    45,18
    14,26
    63,54
    42,8
    68,20
    56,24
    14,31
    34,35
    70,30
    67,18
    0,67
    40,2
    58,52
    23,28
    58,27
    50,68
    10,49
    40,65
    52,4
    20,27
    62,10
    56,50
    60,14
    59,0
    62,6
    48,28
    57,20
    38,48
    20,35
    10,55
    62,20
    68,16
    42,68
    24,55
    61,70
    12,2
    53,4
    62,1
    15,14
    70,4
    37,34
    63,32
    49,0
    0,52
    60,52
    63,66
    3,62
    20,17
    12,32
    62,50
    65,68
    10,19
    14,28
    9,0
    26,30
    4,27
    39,56
    42,29
    60,24
    68,7
    59,42
    51,48
    29,8
    54,43
    49,36
    24,28
    30,7
    6,61
    55,34
    9,12
    31,40
    20,64
    50,0
    66,21
    19,18
    4,2
    30,47
    2,5
    7,20
    33,4
    24,1
    7,60
    40,24
    46,20
    49,4
    36,34
    8,18
    50,40
    12,7
    6,39
    46,46
    29,14
    50,38
    22,30
    61,32
    58,69
    32,38
    11,42
    0,22
    56,54
    10,31
    49,62
    65,4
    24,14
    44,33
    47,18
    7,28
    16,49
    42,60
    64,20
    50,48
    36,61
    1,44
    56,25
    26,38
    36,59
    14,34
    32,34
    29,0
    8,41
    67,40
    60,65
    19,70
    0,19
    4,64
    29,38
    26,50
    24,48
    25,56
    60,10
    3,68
    8,2
    56,34
    18,64
    51,64
    42,32
    59,46
    14,39
    0,31
    2,37
    40,37
    52,22
    16,70
    1,52
    58,59
    2,26
    6,64
    42,45
    12,18
    54,1
    45,46
    39,26
    52,33
    2,61
    65,66
    36,0
    42,23
    24,46
    64,36
    45,30
    43,56
    60,57
    44,20
    61,68
    16,3
    44,56
    57,40
    12,58
    10,26
    42,58
    31,54
    12,31
    48,34
    59,44
    70,11
    1,54
    40,18
    70,63
    27,10
    2,20
    46,49
    26,69
    30,10
    31,20
    32,70
    30,64
    52,48
    10,9
    38,6
    52,28
    24,24
    48,24
    70,46
    1,6
    44,21
    20,66
    12,53
    5,48
    51,68
    40,23
    63,56
    4,3
    64,4
    51,10
    14,11
    65,60
    53,70
    2,2
    34,61
    24,2
    48,59
    67,12
    8,52
    26,4
    9,44
    9,48
    44,2
    41,48
    21,32
    24,70
    20,15
    25,28
    26,1
    21,30
    4,56
    17,14
    4,36
    12,22
    14,62
    54,42
    52,1
    2,1
    28,2
    31,42
    14,8
    11,58
    49,20
    52,44
    42,13
    54,57
    70,19
    2,52
    3,20
    47,28
    70,12
    52,23
    3,0
    64,0
    70,40
    22,22
    39,24
    32,69
    22,48
    19,52
    36,69
    27,46
    22,19
    63,30
    16,52
    64,69
    28,1
    43,0
    22,42
    50,56
    35,38
    4,37
    62,4
    28,27
    51,44
    22,8
    19,58
    12,9
    58,30
    51,8
    15,46
    60,17
    6,1
    16,59
    6,16
    10,4
    0,68
    14,58
    27,22
    69,62
    0,53
    36,57
    24,68
    20,68
    54,53
    2,25
    2,8
    52,53
    33,22
    27,52
    66,15
    6,47
    31,38
    54,19
    68,34
    50,6
    23,70
    1,42
    64,27
    41,58
    33,42
    60,56
    32,18
    12,68
    42,38
    54,62
    52,20
    36,22
    47,24
    34,46
    49,26
    9,66
    25,30
    38,14
    46,34
    56,66
    52,6
    36,70
    58,40
    35,52
    28,43
    16,50
    8,42
    61,10
    42,31
    67,16
    21,2
    54,70
    10,50
    26,55
    34,16
    34,8
    12,6
    32,45
    26,6
    56,68
    69,24
    42,9
    45,40
    48,8
    64,14
    30,62
    13,42
    46,4
    16,25
    48,31
    46,58
    40,40
    46,68
    48,20
    56,4
    8,40
    10,14
    20,31
    61,20
    34,24
    68,68
    14,18
    1,46
    62,65
    46,55
    18,55
    24,66
    48,10
    16,38
    11,14
    21,22
    22,38
    28,22
    2,24
    15,66
    22,50
    19,28
    68,51
    58,25
    14,15
    64,56
    46,48
    38,67
    56,9
    12,62
    43,24
    36,10
    60,0
    2,10
    28,60
    53,44
    18,39
    28,42
    6,10
    68,44
    18,49
    68,32
    27,66
    0,44
    4,10
    29,68
    40,9
    61,44
    24,32
    9,70
    10,69
    8,56
    65,14
    25,48
    0,33
    3,32
    42,57
    40,15
    60,21
    22,69
    21,10
    20,36
    64,51
    12,1
    40,46
    48,6
    22,44
    8,24
    18,12
    54,4
    54,10
    22,39
    34,62
    64,40
    18,52
    0,25
    0,57
    38,55
    64,45
    0,35
    36,55
    0,41
    45,10
    64,16
    24,58
    56,58
    64,42
    12,46
    22,10
    38,52
    20,56
    54,41
    62,53
    4,60
    64,12
    10,7
    59,32
    20,2
    32,14
    42,50
    56,1
    50,49
    4,39
    7,24
    38,3
    69,68
    66,4
    67,32
    42,21
    4,53
    35,44
    20,53
    68,18
    0,65
    38,63
    70,53
    64,54
    35,4
    54,65
    53,0
    58,70
    63,52
    20,50
    32,16
    32,54
    47,36
    50,61
    34,27
    65,56
    65,0
    50,39
    16,10
    22,70
    10,36
    32,66
    38,8
    37,0
    4,14
    28,49
    4,12
    48,67
    59,70
    42,48
    28,56
    2,31
    64,24
    7,70
    43,18
    2,12
    10,32
    62,44
    26,70
    30,68
    4,46
    58,50
    49,58
    45,50
    8,59
    41,0
    10,24
    18,60
    60,54
    0,69
    26,5
    56,18
    60,15
    10,58
    5,12
    0,9
    22,34
    14,51
    3,60
    18,70
    69,50
    64,48
    69,10
    4,42
    8,38
    40,22
    30,60
    15,0
    28,3
    27,40
    56,70
    5,30
    42,26
    2,28
    39,8
    34,60
    34,26
    34,49
    4,29
    34,36
    33,52
    40,32
    54,8
    3,46
    10,68
    42,70
    8,26
    34,66
    15,8
    21,44
    5,26
    44,40
    50,31
    40,4
    68,10
    43,54
    4,62
    52,66
    16,22
    32,32
    62,0
    6,46
    28,23
    42,52
    53,26
    10,25
    42,54
    56,67
    60,26
    19,34
    60,30
    46,37
    10,56
    12,10
    66,25
    60,69
    19,46
    23,16
""".trimIndent().lines()
