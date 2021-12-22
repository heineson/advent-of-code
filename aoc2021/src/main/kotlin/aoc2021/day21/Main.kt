package aoc2021.day21

import aoc2021.circularGet

data class Player(val pos: Int, val score: Int)

var dieCalls = 0
fun turn(ps: Pair<Player, Player>, board: List<Int>, die: Iterator<Int>, limit: Int): Pair<Player, Player> {
    fun playerTurn(p: Player): Player {
        val rolls = listOf(die.next(), die.next(), die.next())
        dieCalls += 3
        val pos = board.circularGet(p.pos + rolls.sum() - 1)
        return Player(pos, p.score + pos)
    }

    val p1 = playerTurn(ps.first)
    val p2 = if (p1.score >= limit) ps.second else playerTurn(ps.second)
    return Pair(p1, p2)
}

fun play(ps: Pair<Player, Player>, board: List<Int>, die: Iterator<Int>): Pair<Player, Player> {
    return if (ps.first.score >= 1000 || ps.second.score >= 1000) {
        ps
    } else {
        play(turn(ps, board, die, 1000), board, die)
    }
}

/// part 2

data class Player2(val pos: Int, val score: Int, val universes: Long)

fun turn2(ps: Pair<Player2, Player2>, board: List<Int>, limit: Int): List<Pair<Player2, Player2>> {
    fun playerTurn(p: Player2): List<Player2> {
        return listOf(1, 2, 3).map { d1 ->
            listOf(1, 2, 3).map { d2 ->
                listOf(1, 2, 3).map { d3 ->
                    val pos = board.circularGet(p.pos + d1 + d2 + d3 - 1)
                    Player2(pos, p.score + pos, p.universes)
                }
            }.flatten()
        }.flatten()
    }
    val p1s: List<Player2> = playerTurn(ps.first)
    val newPs: List<Pair<Player2, Player2>> = p1s.map { p1 ->
        if (p1.score >= limit) listOf(Pair(p1, ps.second))
        else playerTurn(ps.second).map { Pair(p1, it) }
    }.flatten()
    return newPs
}

data class CacheKey(val p1: Pair<Int, Int>, val p2: Pair<Int, Int>)
private val cache = mutableMapOf<CacheKey,  Pair<Player2, Player2>>()

fun play2(ps: Pair<Player2, Player2>, board: List<Int>): Pair<Player2, Player2> {
    return if (ps.first.score >= 21) {
        ps.copy(first = ps.first.copy(universes = ps.first.universes + 1))
    } else if (ps.second.score >= 21) {
        ps.copy(second = ps.second.copy(universes = ps.second.universes + 1))
    } else {
        cache.getOrPut(CacheKey(Pair(ps.first.pos, ps.first.score), Pair(ps.second.pos, ps.second.score))) {
            turn2(ps, board, 21).map { play2(it, board) }.reduce { (accP1, accP2), (p1, p2) ->
                Pair(
                    accP1.copy(universes = accP1.universes + p1.universes),
                    accP2.copy(universes = accP2.universes + p2.universes)
                )
            }
        }
    }
}

fun main() {
    actualData.let { data ->
        val board = List(10) { it + 1 }
        val die = generateSequence(1) { (it % 100) + 1 }.iterator()
        val init = Pair(Player(data.first, 0), Player(data.second, 0))

        val (p1, p2) = play(init, board, die)
        println(dieCalls * if (p1.score >= 1000) p2.score else p1.score) // 1006866
    }

    actualData.let { data ->
        val board = List(10) { it + 1 }
        val init = Pair(Player2(data.first, 0, 0), Player2(data.second, 0, 0))

        val res = play2(init, board)
        println(res) // 273042027784929 wins
    }
}

private val testData = Pair(4, 8)
private val actualData = Pair(3, 7)
