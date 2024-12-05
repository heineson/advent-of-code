package aoc2024.day5

fun main() {
    part1(actualInput, actualInput2) // 4959
}

val after = mutableMapOf<Int, List<Int>>()
val before = mutableMapOf<Int, List<Int>>()

fun part1(input1: List<String>, updates: List<String>) {
    val rules = input1.map { line -> line.split("|") }.map { Pair(it[0].toInt(), it[1].toInt()) }

    rules.forEach { (f, s) ->
        run {
            if (after.containsKey(f)) {
                after[f] = after[f]!!.plus(s)
            } else {
                after[f] = listOf(s)
            }

            if (before.containsKey(s)) {
                before[s] = before[s]!!.plus(f)
            } else {
                before[s] = listOf(f)
            }
        }
    }

    val correctUpdates = updates
        .map { it.split(",").map { i -> i.toInt() } }
        .filter { validUpdate(it) }

//    println(correctUpdates)
    println(correctUpdates.sumOf { it[it.size / 2] })
}

fun validUpdate(pages: List<Int>): Boolean {
    println(pages)
    return pages.withIndex().all {
        page -> testPage(page.value, pages.subList(0, page.index), pages.subList(page.index + 1, pages.size))
     }
}

fun testPage(page: Int, b: List<Int>, a: List<Int>): Boolean {
    println("$b, $page, $a")

    return before[page]?.containsAll(b) ?: true && after[page]?.containsAll(a) ?: true
}

val testInput1 = """
    47|53
    97|13
    97|61
    97|47
    75|29
    61|13
    75|53
    29|13
    97|29
    53|29
    61|53
    97|53
    61|29
    47|13
    75|47
    97|75
    47|61
    75|61
    47|29
    75|13
    53|13
""".trimIndent().lines()
val testInput2 = """
    75,47,61,53,29
    97,61,53,29,13
    75,29,13
    75,97,47,61,53
    61,13,29
    97,13,75,29,47
""".trimIndent().lines()

val actualInput = """
    87|26
    31|18
    31|47
    16|39
    16|74
    16|34
    57|62
    57|87
    57|24
    57|59
    29|57
    29|69
    29|15
    29|43
    29|33
    34|52
    34|89
    34|42
    34|98
    34|29
    34|92
    42|22
    42|98
    42|47
    42|33
    42|29
    42|17
    42|82
    17|31
    17|28
    17|16
    17|34
    17|11
    17|69
    17|71
    17|81
    53|29
    53|52
    53|11
    53|12
    53|73
    53|58
    53|17
    53|89
    53|82
    71|11
    71|43
    71|16
    71|59
    71|75
    71|19
    71|24
    71|37
    71|48
    71|57
    13|17
    13|12
    13|59
    13|68
    13|39
    13|69
    13|58
    13|75
    13|15
    13|71
    13|28
    18|53
    18|94
    18|22
    18|69
    18|98
    18|12
    18|29
    18|47
    18|33
    18|52
    18|83
    18|54
    74|56
    74|12
    74|52
    74|54
    74|86
    74|82
    74|83
    74|26
    74|18
    74|87
    74|94
    74|17
    74|98
    47|77
    47|33
    47|75
    47|17
    47|73
    47|94
    47|29
    47|53
    47|26
    47|89
    47|22
    47|12
    47|98
    47|52
    26|89
    26|57
    26|17
    26|69
    26|53
    26|29
    26|75
    26|94
    26|33
    26|15
    26|98
    26|77
    26|71
    26|73
    26|52
    69|68
    69|56
    69|71
    69|34
    69|28
    69|81
    69|11
    69|32
    69|37
    69|57
    69|19
    69|24
    69|39
    69|59
    69|48
    69|74
    56|17
    56|47
    56|89
    56|82
    56|26
    56|92
    56|54
    56|53
    56|12
    56|73
    56|52
    56|29
    56|94
    56|77
    56|87
    56|32
    56|58
    75|42
    75|59
    75|57
    75|74
    75|16
    75|31
    75|85
    75|81
    75|34
    75|11
    75|48
    75|37
    75|28
    75|38
    75|24
    75|62
    75|68
    75|19
    38|89
    38|47
    38|98
    38|73
    38|92
    38|53
    38|52
    38|17
    38|58
    38|29
    38|22
    38|26
    38|33
    38|77
    38|87
    38|15
    38|94
    38|83
    38|82
    82|94
    82|17
    82|75
    82|28
    82|83
    82|71
    82|12
    82|22
    82|62
    82|89
    82|11
    82|33
    82|15
    82|52
    82|98
    82|92
    82|69
    82|58
    82|54
    82|77
    33|48
    33|37
    33|74
    33|81
    33|68
    33|19
    33|85
    33|69
    33|24
    33|57
    33|39
    33|34
    33|71
    33|31
    33|11
    33|16
    33|59
    33|28
    33|43
    33|62
    33|56
    73|24
    73|19
    73|57
    73|68
    73|33
    73|11
    73|22
    73|59
    73|69
    73|39
    73|15
    73|81
    73|37
    73|85
    73|74
    73|71
    73|43
    73|62
    73|16
    73|31
    73|48
    73|28
    85|29
    85|42
    85|94
    85|34
    85|86
    85|89
    85|26
    85|48
    85|19
    85|53
    85|18
    85|31
    85|38
    85|59
    85|52
    85|82
    85|74
    85|39
    85|87
    85|81
    85|56
    85|32
    85|47
    83|85
    83|68
    83|34
    83|59
    83|48
    83|33
    83|57
    83|37
    83|28
    83|81
    83|69
    83|19
    83|24
    83|43
    83|71
    83|75
    83|15
    83|39
    83|62
    83|73
    83|22
    83|16
    83|11
    83|31
    86|83
    86|29
    86|94
    86|89
    86|54
    86|42
    86|15
    86|53
    86|18
    86|87
    86|82
    86|47
    86|73
    86|38
    86|12
    86|26
    86|17
    86|52
    86|92
    86|22
    86|58
    86|98
    86|77
    86|13
    32|58
    32|15
    32|38
    32|29
    32|98
    32|82
    32|53
    32|26
    32|86
    32|92
    32|17
    32|83
    32|13
    32|54
    32|47
    32|52
    32|94
    32|87
    32|73
    32|18
    32|42
    32|12
    32|89
    32|77
    48|94
    48|12
    48|77
    48|47
    48|54
    48|87
    48|86
    48|42
    48|56
    48|53
    48|52
    48|89
    48|92
    48|82
    48|26
    48|18
    48|17
    48|32
    48|74
    48|13
    48|29
    48|58
    48|98
    48|38
    39|92
    39|32
    39|26
    39|87
    39|53
    39|42
    39|29
    39|56
    39|38
    39|52
    39|18
    39|81
    39|77
    39|86
    39|48
    39|89
    39|54
    39|74
    39|94
    39|47
    39|24
    39|34
    39|82
    39|19
    98|59
    98|62
    98|68
    98|73
    98|16
    98|83
    98|75
    98|11
    98|13
    98|12
    98|71
    98|37
    98|28
    98|31
    98|22
    98|69
    98|39
    98|15
    98|33
    98|57
    98|43
    98|85
    98|58
    98|17
    24|48
    24|77
    24|54
    24|86
    24|74
    24|56
    24|47
    24|53
    24|42
    24|92
    24|38
    24|82
    24|87
    24|19
    24|12
    24|52
    24|98
    24|29
    24|13
    24|94
    24|18
    24|32
    24|26
    24|89
    68|81
    68|38
    68|42
    68|32
    68|19
    68|31
    68|26
    68|59
    68|34
    68|53
    68|47
    68|87
    68|82
    68|86
    68|74
    68|43
    68|48
    68|16
    68|24
    68|56
    68|39
    68|85
    68|37
    68|18
    62|56
    62|47
    62|37
    62|24
    62|26
    62|31
    62|74
    62|86
    62|42
    62|68
    62|87
    62|59
    62|85
    62|19
    62|16
    62|48
    62|81
    62|39
    62|38
    62|32
    62|18
    62|28
    62|43
    62|34
    28|85
    28|74
    28|47
    28|32
    28|16
    28|38
    28|37
    28|34
    28|18
    28|24
    28|42
    28|39
    28|43
    28|68
    28|26
    28|87
    28|31
    28|56
    28|59
    28|81
    28|53
    28|19
    28|86
    28|48
    43|85
    43|74
    43|53
    43|48
    43|26
    43|31
    43|32
    43|37
    43|39
    43|82
    43|16
    43|59
    43|87
    43|94
    43|38
    43|86
    43|81
    43|18
    43|42
    43|24
    43|34
    43|19
    43|56
    43|47
    81|82
    81|47
    81|42
    81|18
    81|92
    81|38
    81|94
    81|34
    81|53
    81|52
    81|48
    81|54
    81|19
    81|98
    81|24
    81|86
    81|26
    81|56
    81|77
    81|32
    81|74
    81|87
    81|29
    81|89
    54|16
    54|17
    54|77
    54|57
    54|85
    54|75
    54|58
    54|73
    54|83
    54|12
    54|13
    54|92
    54|15
    54|11
    54|98
    54|28
    54|62
    54|69
    54|68
    54|43
    54|71
    54|22
    54|33
    54|37
    59|31
    59|24
    59|86
    59|39
    59|87
    59|26
    59|81
    59|94
    59|34
    59|38
    59|32
    59|48
    59|42
    59|89
    59|19
    59|82
    59|56
    59|74
    59|47
    59|18
    59|53
    59|29
    59|52
    59|54
    12|16
    12|34
    12|68
    12|73
    12|39
    12|31
    12|11
    12|58
    12|28
    12|71
    12|81
    12|33
    12|59
    12|22
    12|83
    12|75
    12|17
    12|15
    12|57
    12|69
    12|43
    12|62
    12|37
    12|85
    11|42
    11|48
    11|38
    11|18
    11|74
    11|34
    11|81
    11|24
    11|47
    11|16
    11|56
    11|68
    11|57
    11|37
    11|32
    11|43
    11|39
    11|86
    11|59
    11|28
    11|62
    11|19
    11|31
    11|85
    92|58
    92|28
    92|12
    92|73
    92|68
    92|75
    92|71
    92|33
    92|16
    92|69
    92|17
    92|83
    92|11
    92|62
    92|37
    92|43
    92|59
    92|98
    92|15
    92|85
    92|13
    92|57
    92|22
    92|77
    52|54
    52|13
    52|12
    52|75
    52|29
    52|37
    52|43
    52|92
    52|73
    52|11
    52|15
    52|62
    52|69
    52|22
    52|28
    52|77
    52|83
    52|57
    52|17
    52|71
    52|58
    52|98
    52|68
    52|33
    22|69
    22|48
    22|57
    22|43
    22|33
    22|16
    22|56
    22|74
    22|68
    22|81
    22|59
    22|37
    22|31
    22|71
    22|32
    22|24
    22|75
    22|85
    22|39
    22|34
    22|62
    22|19
    22|11
    22|28
    37|89
    37|39
    37|48
    37|86
    37|59
    37|19
    37|74
    37|53
    37|16
    37|26
    37|82
    37|85
    37|81
    37|24
    37|31
    37|56
    37|42
    37|87
    37|94
    37|18
    37|38
    37|34
    37|47
    37|32
    77|16
    77|59
    77|33
    77|31
    77|22
    77|98
    77|11
    77|13
    77|12
    77|85
    77|75
    77|17
    77|68
    77|57
    77|83
    77|71
    77|15
    77|28
    77|62
    77|37
    77|73
    77|43
    77|58
    77|69
    58|75
    58|19
    58|81
    58|11
    58|37
    58|43
    58|68
    58|71
    58|31
    58|28
    58|62
    58|34
    58|16
    58|69
    58|33
    58|57
    58|85
    58|83
    58|73
    58|39
    58|59
    58|24
    58|15
    58|22
    94|28
    94|52
    94|69
    94|54
    94|11
    94|33
    94|29
    94|83
    94|71
    94|92
    94|58
    94|75
    94|73
    94|17
    94|98
    94|57
    94|12
    94|22
    94|62
    94|68
    94|13
    94|15
    94|89
    94|77
    15|19
    15|57
    15|31
    15|69
    15|16
    15|59
    15|37
    15|48
    15|62
    15|22
    15|56
    15|75
    15|34
    15|74
    15|81
    15|68
    15|85
    15|33
    15|11
    15|24
    15|28
    15|71
    15|43
    15|39
    89|77
    89|11
    89|71
    89|15
    89|54
    89|69
    89|33
    89|83
    89|75
    89|28
    89|29
    89|98
    89|62
    89|57
    89|12
    89|17
    89|13
    89|22
    89|68
    89|92
    89|43
    89|73
    89|58
    89|52
    19|56
    19|29
    19|89
    19|74
    19|98
    19|38
    19|86
    19|52
    19|48
    19|82
    19|87
    19|13
    19|94
    19|32
    19|54
    19|17
    19|12
    19|47
    19|26
    19|18
    19|42
    19|77
    19|92
    19|53
    87|22
    87|83
    87|52
    87|58
    87|92
    87|29
    87|13
    87|15
    87|77
    87|73
    87|89
    87|33
    87|71
    87|69
    87|12
    87|94
    87|53
    87|98
    87|75
    87|82
    87|11
    87|54
    87|17
    31|89
    31|53
    31|19
    31|32
    31|56
    31|86
    31|94
    31|81
    31|48
    31|92
    31|34
    31|24
    31|38
    31|87
    31|42
    31|29
    31|26
    31|54
    31|39
    31|82
    31|52
    31|74
    16|38
    16|53
    16|82
    16|89
    16|24
    16|56
    16|31
    16|32
    16|47
    16|52
    16|26
    16|85
    16|18
    16|48
    16|81
    16|94
    16|87
    16|59
    16|19
    16|42
    16|86
    57|38
    57|48
    57|37
    57|56
    57|68
    57|16
    57|81
    57|43
    57|86
    57|31
    57|32
    57|18
    57|28
    57|74
    57|47
    57|85
    57|39
    57|34
    57|42
    57|19
    29|37
    29|17
    29|73
    29|12
    29|16
    29|77
    29|68
    29|54
    29|83
    29|13
    29|62
    29|58
    29|92
    29|75
    29|11
    29|28
    29|22
    29|98
    29|71
    34|77
    34|38
    34|82
    34|86
    34|18
    34|19
    34|74
    34|56
    34|13
    34|87
    34|26
    34|24
    34|47
    34|94
    34|54
    34|32
    34|48
    34|53
    42|89
    42|18
    42|83
    42|58
    42|87
    42|54
    42|53
    42|38
    42|13
    42|94
    42|77
    42|15
    42|26
    42|12
    42|73
    42|52
    42|92
    17|57
    17|62
    17|68
    17|75
    17|83
    17|59
    17|58
    17|73
    17|43
    17|39
    17|15
    17|22
    17|37
    17|33
    17|85
    17|24
    53|75
    53|22
    53|98
    53|57
    53|13
    53|71
    53|77
    53|54
    53|83
    53|15
    53|94
    53|33
    53|69
    53|92
    53|62
    71|74
    71|86
    71|28
    71|62
    71|34
    71|42
    71|18
    71|39
    71|68
    71|32
    71|56
    71|81
    71|31
    71|85
    13|22
    13|73
    13|43
    13|11
    13|37
    13|16
    13|33
    13|57
    13|85
    13|31
    13|62
    13|83
    13|81
    18|82
    18|38
    18|89
    18|77
    18|87
    18|15
    18|17
    18|58
    18|13
    18|73
    18|26
    18|92
    74|38
    74|53
    74|77
    74|89
    74|47
    74|42
    74|29
    74|32
    74|13
    74|58
    74|92
    47|82
    47|15
    47|13
    47|92
    47|54
    47|69
    47|58
    47|87
    47|83
    47|71
    26|92
    26|12
    26|22
    26|82
    26|54
    26|83
    26|58
    26|11
    26|13
    69|62
    69|43
    69|75
    69|31
    69|42
    69|16
    69|85
    69|86
    56|38
    56|42
    56|86
    56|98
    56|18
    56|13
    56|83
    75|43
    75|32
    75|18
    75|56
    75|86
    75|39
    38|54
    38|71
    38|13
    38|69
    38|12
    82|57
    82|73
    82|13
    82|29
    33|75
    33|32
    33|86
    73|34
    73|75
    85|24
""".trimIndent().lines()

val actualInput2 = """
    89,52,29,54,92,77,13,17,58,83,73,15,22,69,71,75,11,57,62,28,68
    71,15,39,83,59,17,37,34,62,11,28,75,16,69,85,58,43,68,33
    98,13,12,17,58,83,73,15,22,33,75,11,57,62,28,68,37,16,85,59,31
    48,73,33,31,16,75,81,71,15,24,34
    31,81,71,68,34,24,74,19,33,57,16,48,69
    62,28,68,43,37,16,85,59,31,39,81,24,19,48,74,56,32,86,42,18,38,47,87
    39,81,34,24,19,48,74,32,86,18,47,87,53,94,89,52,29,54,92
    94,89,52,29,13,17,73,69,57,62,28
    31,39,34,74,56,26,89,52,54
    33,57,68,43,37,16,59,31,19,48,32
    22,11,73,16,59,37,68,24,62,75,28,81,39,33,43,34,57,31,19,48,69,85,15
    39,68,11,34,74,85,56,62,69,57,32,75,59,81,16,28,37,19,86,24,31
    28,68,37,16,85,59,31,81,34,24,19,48,32,38,47,87,26
    86,42,18,38,47,87,26,53,82,94,89,52,29,54,92,77,98,13,12,17,58,83,73
    54,92,98,13,12,17,73,22,71,57,68
    85,39,81,34,24,19,48,74,56,32,86,42,38,47,87,26,53,82,94,89,52
    54,53,42,83,17,92,73,89,12,22,18
    38,47,87,26,82,89,52,29,92,77,98,12,17,58,83,15,22,33,69
    11,43,34,28,86,75,39,48,19,57,37,56,42,24,32,18,85
    17,62,58,11,33,43,75,13,92,71,15,57,73,37,98,68,22,69,83,16,77
    37,81,15,11,73,28,24,19,48
    82,94,89,29,92,77,98,13,12,17,83,73,33,75,62
    89,53,33,98,15,77,12,54,94,11,73,92,83,17,71,58,57,75,69,29,22,52,82
    98,13,58,15,22,33,69,71,75,11,57,28,68,43,37,16,31
    33,37,81,71,31,57,12,85,68
    39,24,87,85,18,34,31,94,48,59,53,26,82,19,81,42,86,89,32,47,56,16,74
    28,57,37,83,19,16,75,81,31,43,34,85,68,24,33,22,73,62,71,69,15,39,59
    77,98,13,12,17,58,83,15,22,33,71,75,11,57,62,28,68,43,37,16,59
    52,92,56,29,54,86,53,74,12,19,48,87,94,38,42,26,77
    16,43,85,32,26,38,19,34,37,74,56
    94,47,87,33,13,18,29
    54,92,77,13,12,83,15,22,33,69,71,75,57,62,43,37,16
    19,48,74,56,32,86,18,38,47,87,26,53,82,94,89,52,29,54,92,77,98,13,12
    34,53,24,42,85,39,48,59,68,47,37
    58,54,38,87,56,12,98,86,94
    52,29,54,92,77,98,13,12,17,58,83,73,71,57,62,68,43
    47,18,98,74,89,32,56,58,94,38,82,54,86,42,77,13,52,26,29,92,53,12,87
    22,33,69,71,75,11,28,16,85,59,31,39,81,34,24,19,74
    68,85,59,31,39,81,34,24,19,48,74,56,32,86,42,18,38,47,87,26,53
    82,42,52,85,39,56,38,32,47,26,87,24,34,59,81,48,18,89,53,94,31
    42,18,38,47,87,26,53,82,89,52,29,54,92,77,98,13,12,58,83,73,22
    13,17,58,83,73,15,22,69,75,57,28,68,43,37,16,85,59,31,39
    69,71,75,11,57,62,28,68,43,37,85,59,31,39,81,34,24,48,74,32,86
    53,29,54,92,98,13,12,17,58,83,73,15,33,69,71,75,11
    98,82,38,12,87,69,53
    15,53,92,13,69,26,52
    29,54,92,98,17,73,15,22,71,75,11,43,37
    73,15,22,33,69,71,75,11,28,68,43,59,31,39,81,34,24,19,48
    43,37,16,85,31,81,34,24,56,32,42,18,26,53,82
    16,58,83,43,37,54,69,73,92
    58,28,52,98,68,29,69,12,33,57,73,75,62,54,15,43,13,77,83,22,92,71,17
    16,75,28,42,86,24,57,37,71,85,31,39,68,11,59
    92,77,98,13,12,17,58,73,22,69,11,28,68,37,85
    57,62,16,85,59,34,48,18,47
    56,85,42,32,48,43,62,57,11,74,18,81,24,39,31,86,16,19,38
    89,77,52,47,53
    82,29,77,98,12,17,83,15,22,75,62
    92,89,52,22,53,75,57
    54,98,13,12,17,83,15,22,33,11,62,28,43,37,16
    56,32,86,42,18,47,87,26,82,94,89,52,29,54,92,77,98,13,17,58,83
    53,82,54,89,38,29,47,39,81,42,32
    62,33,77,13,17,82,52,57,15,98,12
    28,37,16,59,31,39,81,19,18,38,26
    54,56,86,32,52,19,39,34,26,87,29
    28,85,31,19,86,47,26
    73,58,16,83,62,57,22,28,59,34,68,69,17,43,33,71,75,85,81,39,11
    32,86,38,26,53,82,94,89,52,29,54,12,17,83,73
    12,58,83,73,15,22,33,69,71,75,11,57,62,28,68,43,37,16,85,59,31,39,81
    43,16,85,59,31,39,34,24,19,48,74,56,32,86,42,18,38,47,87,53,82
    42,38,47,87,53,82,94,89,52,92,77,98,12,17,58,83,73,15,22
    83,73,15,22,69,71,75,11,57,62,68,43,37,16,85,59,31,81,34,24,19
    13,82,52,92,77,94,58,11,12,98,22,53,89,73,69,54,83,17,15,29,33
    58,83,15,22,33,71,75,11,62,28,68,43,37,16,85,59,31,39,81,34,24
    33,57,28,12,75,89,11,17,22,94,92,13,73,15,69,77,98,62,71,52,83,54,58
    62,52,94,15,57,28,54
    28,68,39,34,19,74,32,18,38
    37,16,31,81,19,56,18,87,53,82,94
    87,26,53,82,94,89,52,29,77,98,12,58,83,22,75
    89,29,92,98,33
    37,24,56,32,86,42,47,53,94
    33,75,11,43,16,59,39,81,24,48,74
    87,53,82,94,89,29,54,92,98,12,17,58,83,73,15,22,69,71,75
    62,28,68,43,37,85,59,31,39,81,34,24,19,48,74,56,32,86,42,18,38,47,87
    17,33,29,94,53,75,87
    54,68,77,73,15,71,57,29,83,17,98,75,69,92,43,22,52,28,62
    92,26,38,18,54,13,87,12,29,17,77,42,53,52,73,15,22,82,94,58,89,83,98
    43,16,85,59,39,34,56,32,38,87,26
    34,24,19,74,56,86,42,47,53,82,89,29,77
    87,26,53,82,94,89,29,77,13,17,58,83,73,22,75
    69,75,57,24,19,74,86
    33,58,13,98,29,73,18,52,94,82,22,17,53,54,92,47,87,38,26,12,15,89,77
    15,22,33,75,11,57,68,43,37,16,85,59,81,34,24,19,74
    19,74,56,81,86,18,48,16,59,38,53,26,94,42,85,87,37,34,39
    74,86,42,87,53,82,94,89,54
    58,87,12,29,32,13,73,53,47,77,38,18,83
    71,16,24,59,81,19,31,34,48,56,11,85,42,28,39,57,32,37,74,86,68
    43,34,16,59,68,28,74,38,81,62,19,18,86,32,87,31,85
    13,12,15,22,33,71,57,62,28,68,43,37,16,85,39
    52,13,26,22,71,47,58,15,33,54,53,98,89,12,73,83,87,29,17,69,77,94,92
    73,47,98,53,54,13,38,18,52,26,12,83,32
    47,33,53,17,94,83,82,87,26,73,12,54,98,52,22,18,15,92,13,29,38
    31,39,19,48,74,32,86,42,18,38,47,87,82,89,52,29,54
    15,22,69,57,37,31,74
    59,31,39,74,86,26,53,82,29
    28,43,16,85,31,34,48,32,86,87,26
    71,37,11,12,69,62,22,75,73,16,83,85,58,98,59,33,15,57,13,68,43,28,77
    22,33,71,28,37,85,59,39,81,34,19,48,56
    86,42,18,47,87,26,53,82,94,89,52,29,54,92,77,98,13,12,17,58,83,73,15
    15,22,69,71,92,62,73,13,54,17,75,29,28,94,33,83,52,77,89,98,58,12,57
    43,68,28,98,16,62,11,83,92,12,22,73,75,69,15,71,17,58,54,77,57,33,37
    18,52,92,54,13
    62,16,81,86,42,38,87
    98,94,87,38,89,13,42,73,58,32,12,47,53,83,52,82,92,86,26,29,18
    68,37,59,39,42,38,87,26,53
    33,17,22,47,94,12,29,52,15,18,82,58,13,26,54,83,38
    83,68,77,62,16,12,59,28,75,15,85,98,73,33,22,37,57,11,13
    24,75,39,81,85,71,48,59,15,34,62,11,57,31,28,16,69,33,19,37,73
    17,83,73,69,71,11,62,85,59,81,34
    82,94,29,92,77,13,12,17,58,83,73,15,22,33,69,71,11,57,62
    22,28,43,12,13,83,33,92,98,52,17,69,57,73,71,58,68,11,62
    57,28,85,59,34,24,74,86,42,18,38
    31,11,43,59,28,24,18,32,34,16,48,86,56,39,57,75,81
    89,54,13,17,73,22,71,28,68
    37,85,43,39,58,17,13,59,11,68,75
    17,12,22,98,33,94,11,28,52
    19,48,74,56,32,86,42,18,38,47,87,26,53,82,94,89,52,29,54,92,77,98,13
    53,82,92,77,13,12,17,58,83,33,57
    29,54,92,77,98,13,12,17,58,83,73,15,22,33,69,75,11,28,68,43,37
    19,48,74,32,47,87,26,94,54,92,12
    89,29,53,15,22,73,52,83,71,58,12
    42,18,47,87,26,53,89,29,92,77,98,12,17,58,83,15,22
    58,83,69,75,62,31,81
    87,16,28,85,37,81,74,39,34,68,31
    29,54,94,13,22,98,73,52,69
    48,39,19,82,43,34,56,31,42,87,18,16,24,86,47,38,26,37,53,32,81
    52,54,92,77,13,12,58,69,11,62,68
    75,57,34,39,71,68,19,56,33,24,16,74,28,85,48,59,32,37,43,11,62,31,69
    52,77,89,38,13,54,29,92,74,86,47,42,87,12,19
    86,53,19,56,54,87,38,29,48,24,74,81,52,77,18
    94,12,98,13,87,74,82,77,18
    47,52,54,92,77,13,12,17,58,83,33,69,71
    48,32,86,42,18,87,53,89,52,29,92,77,98,12,17
    29,77,98,12,17,33,69,11,28,68,37
    37,75,11,22,56,16,57
    87,19,53,82,52,94,39,18,59,31,85
    42,18,38,87,26,82,94,89,52,29,92,77,98,13,58,83,73,15,22
    24,48,56,42,18,47,54,98,13
    38,53,13,12,58,73,15,33,69
    58,73,69,28,16,85,31,39,24
    16,31,81,19,48,42,18,94,89
    12,52,11,92,73,53,33,75,69,13,89,77,57
    16,85,59,31,39,34,24,19,48,74,56,32,86,42,18,38,47,87,26,53,82,94,89
    77,98,13,12,17,58,33,69,71,75,11,57,28,68,43,16,59
    75,12,52,13,83,17,58,26,54,11,53,94,71,29,77,82,69,22,73,98,89
    75,57,62,28,68,43,85,39,34,24,19,48,56,86,18
    48,32,87,82,12,74,18,54,89,86,56,42,17,29,38,13,94,26,77,52,47,92,53
    34,19,33,31,24,75,37,69,81,48,22,43,85,57,16,28,56,62,11
    13,12,17,83,22,33,11,62,28,68,43,31,39
    15,33,43,37,62,48,39,69,59,24,31,57,73,68,34,16,81,11,85,19,28,22,75
    85,33,48,31,32,34,43,62,56,75,69,68,28
    32,28,69,43,85,33,81,16,68,59,48,57,75
    18,38,87,26,94,89,29,92,77,98,13,17,58,83,73,22,33
    58,73,11,57,62,37,16,59,31,39,24
    57,33,37,98,71,62,11,13,28,16,92,54,68,69,17,15,73,77,12
    82,32,83,18,92,98,53,26,17,86,38,47,94,58,42,56,12,89,54,29,77,87,13
    83,69,85,62,75,37,34,71,31,39,15,11,73,59,22,33,58
    57,62,28,68,43,16,59,31,34,24,19,48,74,56,42,18,47
    56,43,82,86,53,85,48,39,38,19,16,37,24,26,31,59,47,18,74
    77,98,12,58,83,73,22,33,69,71,75,11,57,62,28,68,43,37,16,85,59
    74,24,11,32,37,81,86,48,16,62,28,85,19,69,31
    89,56,81,32,47,94,86,77,18,26,82
    85,33,28,24,15,39,68,48,75,34,19,16,59,57,31,11,81,74,37,62,69
    83,73,69,75,62,28,43,37,16
    83,26,53,11,71,13,17
    56,48,18,89,94,38,39,32,47,24,87,59,74,19,34,31,85,53,82
    37,16,31,81,34,19,74,56,42,18,47,82,94
    71,89,11,92,13,73,83,77,26,53,69,33,15,29,98,12,17,54,22,52,82,58,75
    12,17,58,83,73,15,22,33,69,71,57,62,28,68,43,37,16,85,31,39,81
    47,42,32,54,77,18,19,92,26,89,52
    28,37,16,85,59,24,32,86,42,18,26
    33,75,11,57,68,43,16,31,34,19,48,56,32
    68,43,59,39,81,24,19
    74,82,24,47,42,13,26,77,89,54,56
    89,38,19,34,98,92,87,32,56,74,94,48,26,53,18,54,47,86,42,29,24,77,82
    57,56,81,75,11,32,24,39,31,42,68,37,19,43,28,16,48,85,34,86,59,71,62
    47,87,53,94,77,17,58
    13,85,71,43,68,12,22,37,77,58,92,28,16,69,75
    39,48,86,42,18
    77,98,13,12,83,73,22,69,71,75,11,57,62,28,68,43,37,16,59
    54,82,18,29,94,32,34
    59,38,48,37,81,42,16,34,56,24,31,53,74,87,32,19,85,94,82,26,39,86,18
""".trimIndent().lines()
