package aoc2021

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sqrt

/****** 2d map utils ********/

fun toDegrees(rad: Double): Double = (rad * 180) / PI
fun toRadians(deg: Double): Double = (deg * PI) / 180

data class Coord(val x: Int, val y: Int) {
    operator fun plus(v: Vect): Coord = Coord(x + v.dx, y + v.dy)
    operator fun plus(c: Coord): Coord = Coord(x + c.x, y + c.y)

    fun up(): Coord = Coord(x, y + 1)
    fun ne(): Coord = Coord(x + 1, y + 1)
    fun right(): Coord = Coord(x + 1, y)
    fun se(): Coord = Coord(x + 1, y - 1)
    fun down(): Coord = Coord(x, y - 1)
    fun sw(): Coord = Coord(x - 1, y - 1)
    fun left(): Coord = Coord(x - 1, y)
    fun nw(): Coord = Coord(x - 1, y + 1)

    fun manhattan(other: Coord): Int {
        return abs(x - other.x) + abs(y - other.y)
    }

    fun cardinalNeighbors(): List<Coord> = listOf(up(), left(), down(), right())
    fun surroundingNeighbors(): List<Coord> = listOf(
        up(),
        ne(),
        right(),
        se(),
        down(),
        sw(),
        left(),
        nw()
    )

    fun inDirectionWhile(d: Vect, predicate: (c: Coord) -> Boolean): List<Coord> {
        val seq: Sequence<Coord> = generateSequence(this + d, { it + d })
        return seq.takeWhile { predicate(it) }.toList()
    }

    fun swap(): Coord = copy(x = this.y, y = this.x)

    override fun toString(): String {
        return "($x, $y)"
    }
}

enum class Rotation { CW, CCW }
data class Vect(val dx: Int, val dy: Int) {
    operator fun plus(other: Vect): Vect = Vect(this.dx + other.dx, this.dy + other.dy)
    operator fun times(v: Int): Vect = Vect(dx * v, dy * v)

    fun length(): Double = sqrt(this.dx.toDouble()*this.dx + this.dy*this.dy)

    fun rotate90(d: Rotation, steps: Int = 1): Vect {
        return when (steps % 4) {
            0 -> this.copy()
            1 -> if (d == Rotation.CW) Vect(dy, -dx) else Vect(-dy, dx)
            2 -> Vect(-dx, -dy)
            3 -> if (d == Rotation.CCW) Vect(dy, -dx) else Vect(-dy, dx)
            else -> throw IllegalStateException("$steps % 4 should never end up here")
        }
    }
}

open class Grid2d<T> {
    private val data = mutableMapOf<Coord, T>()

    operator fun set(c: Coord, d: T) {
        data[c] = d
    }

    operator fun get(c: Coord): T? {
        return data[c]
    }

    fun getValue(c: Coord): T = data.getValue(c)
    fun getCoords() = data.keys
    fun getValues() = data.values
    fun getEntries() = data.entries

    fun getDimensionRanges(): Pair<IntRange, IntRange> {
        val xr: IntRange = (data.keys.minOfOrNull { it.x } ?: 0)..(data.keys.maxOfOrNull { it.x } ?: 0)
        val yr: IntRange = (data.keys.minOfOrNull { it.y } ?: 0)..(data.keys.maxOfOrNull { it.y } ?: 0)
        return Pair(xr, yr)
    }

    fun getSides(): List<List<Pair<Coord, T>>> {
        val (xr, yr) = getDimensionRanges()
        return listOf(
            data.entries.filter { it.key.y == yr.last }.map { Pair(it.key, it.value) },
            data.entries.filter { it.key.x == xr.last }.map { Pair(it.key, it.value) },
            data.entries.filter { it.key.y == yr.first }.map { Pair(it.key, it.value) },
            data.entries.filter { it.key.x == xr.first }.map { Pair(it.key, it.value) }
        )
    }

    open fun printElement(e: T): Char {
        return if (e is Int || e is Long) {
            if (e != 0) '#' else '.'
        } else if (e is Boolean) {
            if (e) '#' else '.'
        } else if (e is Char) {
            e
        } else {
            '.'
        }
    }

    override fun toString(): String {
        val (xr, yr) = getDimensionRanges()
        var r = ""
        for (yi in yr) {
            for (xi in xr) {
                r += data[Coord(xi, yi)]?.let { printElement(it) } ?: '.'
            }
            r += "\n"
        }
        return r
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Grid2d<*>) return false

        if (data != other.data) return false

        return true
    }

    override fun hashCode(): Int {
        return data.hashCode()
    }
}

/****** 3d map utils ********/

data class Coord3(val x: Int, val y: Int, val z: Int) {
    fun surroundingNeighbors(): List<Coord3> {
        val coords = mutableListOf<Coord3>()
        for (xi in x-1..x+1) {
            for (yi in y-1..y+1) {
                for (zi in z-1..z+1) {
                    if (!(xi == x && yi == y && zi == z)) {
                        coords.add(Coord3(xi, yi, zi))
                    }
                }
            }
        }
        return coords
    }
}

/****** 4d map utils ********/

data class Coord4(val x: Int, val y: Int, val z: Int, val w: Int) {
    fun surroundingNeighbors(): List<Coord4> {
        val coords = mutableListOf<Coord4>()
        for (xi in x-1..x+1) {
            for (yi in y-1..y+1) {
                for (zi in z-1..z+1) {
                    for (wi in w-1..w+1) {
                        if (!(xi == x && yi == y && zi == z && wi == w)) {
                            coords.add(Coord4(xi, yi, zi, wi))
                        }
                    }
                }
            }
        }
        return coords
    }
}

/****** Hexagonal grid utils ********/

// Since they do not align in a traditional 2d grid, the step number is 2 between orthogonal tiles
data class HexCoord(val x: Int, val y: Int) {
    fun e() = HexCoord(x + 2, y)
    fun se() = HexCoord(x + 1, y - 1)
    fun sw() = HexCoord(x - 1, y - 1)
    fun w() = HexCoord(x - 2, y)
    fun nw() = HexCoord(x - 1, y + 1)
    fun ne() = HexCoord(x + 1, y + 1)

    fun surroundingNeighbors() = listOf(e(), se(), sw(), w(), nw(), ne())
}
