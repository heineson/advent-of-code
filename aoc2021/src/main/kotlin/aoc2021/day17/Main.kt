package aoc2021.day17

import aoc2021.Coord
import aoc2021.Vect

fun step(data: Pair<Coord, Vect>): Pair<Coord, Vect> {
    val (pos, v) = data
    val newVx = if (v.dx == 0) 0 else if (v.dx < 0) v.dx + 1 else v.dx - 1
    return Pair(pos + v, Vect(newVx, v.dy - 1))
}

fun inTarget(c: Coord, target: Pair<IntRange, IntRange>): Boolean = c.x in target.first && c.y in target.second

const val LIMIT = 2000
fun highestY(initial: Pair<Coord, Vect>, target: Pair<IntRange, IntRange>): Int? {
    val seq: Sequence<Pair<Coord, Vect>> = generateSequence(initial) { step(it) }
    var i = 0
    val cs = seq.takeWhile {
        i++
        !inTarget(it.first, target) && i <= LIMIT
    }.toList()
    return if (cs.size == LIMIT)
        null
    else
        cs.maxOf { it.first.y }
}

fun main() {
    actualData.let { data ->
        val xs = data.first.split("..")
        val ys = data.second.split("..")
        val xr = IntRange(xs[0].toInt(), xs[1].toInt())
        val yr = IntRange(ys[0].toInt(), ys[1].toInt())

        val res = mutableListOf<Int>()
        (0..xr.last).forEach { xv ->
            (yr.first..-yr.first).forEach { yv ->
                highestY(Pair(Coord(0, 0), Vect(xv, yv)), Pair(xr, yr))?.also { res.add(it) }
            }
        }
        println(res.maxOf { it }) // 8256
        println(res.size) // 2326
    }
}

private val testData =
    "x=20..30, y=-10..-5".split(", ").let { Pair(it[0].replaceFirst("x=", ""), it[1].replaceFirst("y=", "")) }

private val actualData =
    "x=150..171, y=-129..-70".split(", ").let { Pair(it[0].replaceFirst("x=", ""), it[1].replaceFirst("y=", "")) }
