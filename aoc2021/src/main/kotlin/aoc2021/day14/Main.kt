package aoc2021.day14

import aoc2021.mutableInc

fun step(template: String, rules: Map<String, String>): String {
    val pairs = template.windowed(2)
    return pairs.fold("X") { acc, pair -> acc.dropLast(1) + pair[0] + rules[pair] + pair[1] }
}

fun step2(pairFrequencies: Map<String, Long>, letterFrequencies: Map<Char, Long>, translator: Map<String, String>): Pair<Map<String, Long>, Map<Char, Long>> {
    val newLetters = mutableMapOf<Char, Long>()
    val newPairs = pairFrequencies.map { e ->
        val newSection = translator.getValue(e.key)
        newLetters.mutableInc(newSection[1], e.value)
        newSection.windowed(2).associateWith { e.value }
    }
    val newPairFreq = newPairs.fold(emptyMap<String, Long>()) { acc, map ->
        (acc.keys + map.keys).associateWith { acc.getOrDefault(it, 0L) + map.getOrDefault(it, 0L) }
    }
    val newLetterFreq = (letterFrequencies.keys + newLetters.keys).associateWith { key -> letterFrequencies.getOrDefault(key, 0) + newLetters.getOrDefault(key, 0) }
    return Pair(newPairFreq, newLetterFreq)
}

fun main() {
    actualData.let { data ->
        val ruleMap = data.second.associate { l -> l.split(" -> ").let { Pair(it[0], it[1]) } }
        val result = (0..9).fold(data.first) { acc, _ -> step(acc, ruleMap) }//.also { println(it) }
        val occurrences = result.groupingBy { it }.eachCount()
        println("${occurrences.maxOf { it.value } - occurrences.minOf { it.value }}") // 3411
    }

    actualData.let { data ->
        val ruleMap = data.second.associate { l -> l.split(" -> ").let { Pair(it[0], it[1]) } }
        val initialPairFreq = data.first.windowed(2).groupingBy { it }.eachCount().mapValues { it.value.toLong() }
        val initialLetterFreq = data.first.groupingBy { it }.eachCount().mapValues { it.value.toLong() }
        val translator = ruleMap.mapValues { it.key[0] + it.value + it.key[1] }

        val result = (0..39).fold(Pair(initialPairFreq, initialLetterFreq)) { acc, _ -> step2(acc.first, acc.second, translator) }
        println("${result.second.maxOf { it.value } - result.second.minOf { it.value }}") // 7477815755570
    }
}

val testData = Pair(
    "NNCB", """
    CH -> B
    HH -> N
    CB -> H
    NH -> C
    HB -> C
    HC -> B
    HN -> C
    NN -> C
    BH -> H
    NC -> B
    NB -> B
    BN -> B
    BB -> N
    BC -> B
    CC -> N
    CN -> C
""".trimIndent().lines()
)

val actualData = Pair(
    "SNPVPFCPPKSBNSPSPSOF", """
    CF -> N
    NK -> B
    SF -> B
    HV -> P
    FN -> S
    VV -> F
    FO -> F
    VN -> V
    PV -> P
    FF -> P
    ON -> S
    PB -> S
    PK -> P
    OO -> P
    SP -> F
    VF -> H
    OV -> C
    BN -> P
    OH -> H
    NC -> F
    BH -> N
    CS -> C
    BC -> N
    OF -> N
    SN -> B
    FP -> F
    FV -> K
    HP -> H
    VB -> P
    FH -> F
    HF -> P
    BB -> O
    HH -> S
    PC -> O
    PP -> B
    VS -> B
    HC -> H
    NS -> N
    KF -> S
    BO -> V
    NP -> S
    NF -> K
    BS -> O
    KK -> O
    VC -> V
    KP -> K
    CK -> P
    HN -> F
    KN -> H
    KH -> N
    SB -> S
    NO -> K
    HK -> H
    BF -> V
    SV -> B
    CV -> P
    CO -> P
    FC -> O
    CP -> H
    CC -> N
    CN -> P
    SK -> V
    SS -> V
    VH -> B
    OS -> N
    FB -> H
    NB -> N
    SC -> K
    NV -> H
    HO -> S
    SO -> P
    PH -> C
    VO -> O
    OB -> O
    FK -> S
    PN -> P
    VK -> O
    NH -> N
    OC -> B
    BP -> O
    PF -> F
    KB -> K
    KV -> B
    PO -> N
    NN -> K
    CH -> O
    KC -> P
    OP -> V
    VP -> F
    OK -> P
    FS -> K
    CB -> S
    HB -> N
    KS -> O
    BK -> C
    BV -> O
    SH -> H
    PS -> N
    HS -> K
    KO -> N
""".trimIndent().lines()
)
