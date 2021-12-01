package aoc2021

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class UtilsKtTest {

    @Test
    fun testReadLinesIntoTokens() {
        val data = readLinesIntoTokens(lines1)

        assertThat(data).containsExactly(
            listOf("abc"),
            listOf("a", "b", "c"),
            listOf("ab", "ac"),
            listOf("a", "a", "a", "a"),
            listOf("b"),
            listOf("abc")
        )
    }

    @Test
    fun circularGet() {
        val list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9)

        assertThat(list.circularGet(-11)).isEqualTo(8)
        assertThat(list.circularGet(-1)).isEqualTo(9)
        assertThat(list.circularGet(0)).isEqualTo(1)
        assertThat(list.circularGet(8)).isEqualTo(9)
        assertThat(list.circularGet(9)).isEqualTo(1)
        assertThat(list.circularGet(19)).isEqualTo(2)
    }

    @Test
    fun circularSubList() {
        val list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9)

        assertThat(list.circularSubList(0, 9)).isEqualTo(list)
        assertThat(list.circularSubList(-2, 9)).isEqualTo(listOf(8, 9))
        assertThat(list.circularSubList(5, 12)).isEqualTo(listOf(6, 7, 8, 9, 1, 2, 3))
        assertThat(list.circularSubList(7, 2)).isEqualTo(listOf(8, 9, 1, 2))
    }

    private val lines1 = """
    abc

    a
    b
    c

    ab
    ac

    a
    a
    a
    a

    b
    
    abc
    """.trimIndent().lines()
}

