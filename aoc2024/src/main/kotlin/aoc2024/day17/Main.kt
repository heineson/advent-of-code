package aoc2024.day17

import aoc2024.day17.OpCode.*
import kotlin.math.pow
import kotlin.math.truncate


enum class OpCode { ADV, BXL, BST, JNZ, BXC, OUT, BDV, CDV }

data class State(val program: List<Int>, val ra: Long, val rb: Long, val rc: Long, val ip: Int, val output: String) {
    fun next(): State {
        val (opcode, operand) = program.drop(ip).take(2)

        if (opcode !in OpCode.entries.indices) error("Illegal opcode: $opcode")
        val oc = OpCode.entries[opcode]

        return when (oc) {
            ADV -> State(
                program,
                truncate(ra.toDouble() / 2.0.pow(combo(operand).toDouble())).toLong(),
                rb,
                rc,
                ip + 2,
                output)
            BXL -> State(program, ra, operand.toLong() xor rb, rc, ip + 2, output)
            BST -> State(program, ra, combo(operand) % 8, rc, ip + 2, output)
            JNZ -> if (ra == 0L) {
                State(program, ra, rb, rc, ip + 2, output)
            } else {
                State(program, ra, rb, rc, operand, output)
            }
            BXC -> State(program, ra, rb xor rc, rc, ip + 2, output)
            OUT -> {
                State(program, ra, rb, rc, ip + 2, output + "${combo(operand) % 8},")
            }
            BDV -> State(
                program,
                ra,
                truncate(ra.toDouble() / 2.0.pow(combo(operand).toDouble())).toLong(),
                rc,
                ip + 2,
                output)
            CDV -> State(
                program,
                ra,
                rb,
                truncate(ra.toDouble() / 2.0.pow(combo(operand).toDouble())).toLong(),
                ip + 2,
                output)
        }
    }

    private fun combo(op: Int): Long = when (op) {
        0, 1, 2, 3 -> op.toLong()
        4 -> ra
        5 -> rb
        6 -> rc
        else -> error("Invalid combo operand: $op")
    }
}

fun main() {
    //val state = State(listOf(0,3,5,4,3,0), 2024, 0, 0, 0, "")
    val state = State(listOf(2,4,1,1,7,5,1,5,4,0,0,3,5,5,3,0), 64196994, 0, 0, 0, "")

    part1(state)
}

fun part1(state: State) {
    println("Part1: ${execute(state)}") // 6,4,6,0,4,5,7,2,7
}

fun execute(state: State): String {
    var curr = state
    while (curr.ip < curr.program.size) {
        curr = curr.next()
    }
    return curr.output.trimEnd(',')
}

val testData = """
    Register A: 729
    Register B: 0
    Register C: 0

    Program: 0,1,5,4,3,0
""".trimIndent().lines()

val actualData = """
    Register A: 64196994
    Register B: 0
    Register C: 0

    Program: 2,4,1,1,7,5,1,5,4,0,0,3,5,5,3,0
""".trimIndent().lines()
