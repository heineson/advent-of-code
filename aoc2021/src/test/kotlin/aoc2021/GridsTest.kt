package aoc2021

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.math.sqrt

internal class GridsTest {
    @Test
    fun testCoord() {
        val c = Coord(5, 5)

        assertThat(c + Vect(6, 7)).isEqualTo(Coord(11, 12))

        assertThat(c.up()).isEqualTo(Coord(5, 6))
        assertThat(c.right()).isEqualTo(Coord(6, 5))
        assertThat(c.down()).isEqualTo(Coord(5, 4))
        assertThat(c.left()).isEqualTo(Coord(4, 5))

        assertThat(c.manhattan(Coord(10, 14))).isEqualTo(14)

        assertThat(c.inDirectionWhile(Vect(1, 2)) { it.x <= 10 }).containsExactly(
            Coord(6, 7),
            Coord(7, 9),
            Coord(8, 11),
            Coord(9, 13),
            Coord(10, 15),
        )
    }

    @Test
    fun testCoordsTo() {
        val start = Coord(2, 0)
        assertThat(start.coordsTo(Coord(6, 2))).isEqualTo(listOf(
            Coord(2,0),
            Coord(3,0),
            Coord(4,1),
            Coord(5,1),
            Coord(6,2)
        ))
        assertThat(start.coordsTo(Coord(6, 2), roundDown = false)).isEqualTo(listOf(
            Coord(2,0),
            Coord(3,1),
            Coord(4,1),
            Coord(5,2),
            Coord(6,2)
        ))
        assertThat(start.coordsTo(Coord(4, 4))).isEqualTo(listOf(
            Coord(2,0),
            Coord(2,1),
            Coord(3,2),
            Coord(3,3),
            Coord(4,4)
        ))
        assertThat(start.coordsTo(Coord(4, 4))).isEqualTo(Coord(4,4).coordsTo(start).reversed())
    }

    @Test
    fun testVect() {
        assertThat(Vect(1, 1).length()).isEqualTo(sqrt(2.0))
        assertThat(Vect(4, 3).length()).isEqualTo(5.0)

        assertThat(Vect(1, 0).rotate90(Rotation.CW)).isEqualTo(Vect(0, -1))
        assertThat(Vect(1, 0).rotate90(Rotation.CW, 2)).isEqualTo(Vect(-1, 0))
        assertThat(Vect(1, 0).rotate90(Rotation.CW, 3)).isEqualTo(Vect(0, 1))
        assertThat(Vect(1, 0).rotate90(Rotation.CW, 4)).isEqualTo(Vect(1, 0))

        assertThat(Vect(1, 0).rotate90(Rotation.CCW)).isEqualTo(Vect(0, 1))
    }

    @Test
    fun testCoord3_GetSurroundingNeighbors() {
        val c = Coord3(0, 0, 0)
        val n = c.surroundingNeighbors()

        assertThat(n).hasSize(26)
        assertThat(n.toSet()).hasSize(26)
        assertThat(n).doesNotContain(c)
        assertThat(n.map { it.x }.maxOrNull()).isEqualTo(1)
        assertThat(n.map { it.x }.minOrNull()).isEqualTo(-1)
        assertThat(n.map { it.y }.maxOrNull()).isEqualTo(1)
        assertThat(n.map { it.y }.minOrNull()).isEqualTo(-1)
        assertThat(n.map { it.z }.maxOrNull()).isEqualTo(1)
        assertThat(n.map { it.z }.minOrNull()).isEqualTo(-1)
    }

    @Test
    fun testCoord4_GetSurroundingNeighbors() {
        val c = Coord4(0, 0, 0, 0)
        val n = c.surroundingNeighbors()

        assertThat(n).hasSize(80)
        assertThat(n.toSet()).hasSize(80)
        assertThat(n).doesNotContain(c)
        assertThat(n.map { it.x }.maxOrNull()).isEqualTo(1)
        assertThat(n.map { it.x }.minOrNull()).isEqualTo(-1)
        assertThat(n.map { it.y }.maxOrNull()).isEqualTo(1)
        assertThat(n.map { it.y }.minOrNull()).isEqualTo(-1)
        assertThat(n.map { it.z }.maxOrNull()).isEqualTo(1)
        assertThat(n.map { it.z }.minOrNull()).isEqualTo(-1)
        assertThat(n.map { it.w }.maxOrNull()).isEqualTo(1)
        assertThat(n.map { it.w }.minOrNull()).isEqualTo(-1)
    }
}
