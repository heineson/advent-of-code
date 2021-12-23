package aoc2021.day23

import aoc2021.Coord
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

// Rules
// 1. Never stop immediately outside room
// 2. Never move into a room not their own
// 3. Never move into own room if it contains other letters
// 4. If stop in hallway, stay until it can move into room

enum class Type { A, B, C, D }
//data class Pod(val type: Type, val steps: Int = 0) {
//    private val stopped = false
//    fun location(b: Board): Coord {
//        return b.inHallway(this) ?:
//    }
//    fun possibleMoves(b: Board) {
//
//    }
//}
//data class Room(val type: Type, var bottom: Pod?, var top: Pod?) {
//    fun inRoom(p: Pod): Coord? = if ()
//}
//
//data class Board(val rooms: Set<Room>) {
//    private val hallway: MutableMap<Coord, Pod?> = (1..10).associate { i -> Coord(i, 0) to null }.toMutableMap()
//    fun inHallway(p: Pod): Coord? = hallway.entries.find { it.value == p }?.key
//}
//
//private fun Board.isFinished() = rooms.all { room -> room.top != null && room.top!!.type == room.type && room.bottom != null && room.bottom!!.type == room.type }
//
//fun step(board: Board): Board {
//    if (board.isFinished()) {
//        board
//    }
//}

private val SLOTS = mapOf(
    Coord(0,0) to Type.values().toSet(),
    Coord(1, 0) to Type.values().toSet(),
    Coord(2, 0) to setOf(),
    Coord(3, 0) to Type.values().toSet(),
    Coord(4, 0) to setOf(),
    Coord(5, 0) to Type.values().toSet(),
    Coord(6, 0) to setOf(),
    Coord(7, 0) to Type.values().toSet(),
    Coord(8, 0) to setOf(),
    Coord(9, 0) to Type.values().toSet(),
    Coord(10, 0) to Type.values().toSet(),

    Coord(2, 1) to setOf(Type.A),
    Coord(2, 2) to setOf(Type.A),

    Coord(4, 1) to setOf(Type.B),
    Coord(4, 2) to setOf(Type.B),

    Coord(6, 1) to setOf(Type.C),
    Coord(6, 2) to setOf(Type.C),

    Coord(8, 1) to setOf(Type.D),
    Coord(8, 2) to setOf(Type.D),
)

private val HALLWAY = setOf(Coord(0,0), Coord(1, 0), Coord(3, 0), Coord(5, 0), Coord(7, 0), Coord(9, 0), Coord(10, 0))
private val ROOMS: Map<Type, Set<Coord>> = mapOf(
    Type.A to setOf(Coord(2, 1), Coord(2, 2)),
    Type.B to setOf(Coord(4, 1), Coord(4, 2)),
    Type.C to setOf(Coord(6, 1), Coord(6, 2)),
    Type.D to setOf(Coord(8, 1), Coord(8, 2)),
)
private val SLOTS2: Map<Type, Set<Coord>> = mapOf(
    Type.A to HALLWAY + ROOMS.getValue(Type.A),
    Type.B to HALLWAY + ROOMS.getValue(Type.B),
    Type.C to HALLWAY + ROOMS.getValue(Type.C),
    Type.D to HALLWAY + ROOMS.getValue(Type.D),
)

fun GameState.bottomPosInRoomFilledWithSameType(room: Collection<Coord>, p: PlayerState): Boolean {
    val other = playerStates.single { p.type == it.type && p.name != it.name }
    return other.pos == Coord(room.first().x, 2)
}
fun Coord.isBetweenX(a: Coord, b: Coord) = x > min(a.x, b.x) && x < max(a.x, b.x)

data class PlayerState(val name: String, val type: Type, val pos: Coord, val steps: Int, val stopped: Boolean) {
    fun nextMoves(g: GameState): Set<Coord> {
        val room = ROOMS.getValue(type)
        // don't move if current pos is in own room, bottom (or top if another of same type is in bottom)
        val blocked = g.playerStates.filter { it.name != name }.map { it.pos }.toSet()
        if (type in SLOTS.getValue(pos) && (pos.y == 2 || (pos.y == 1 && g.playerStates.singleOrNull { it.pos == Coord(pos.x, 2) && it.type == type } != null))) {
            return emptySet()
        }


        if (stopped) {
            // only to room if path is not blocked and room empty or contains one of same type
            val target: Coord? = (room.filter { it.y == if (g.bottomPosInRoomFilledWithSameType(room, this)) 1 else 2 } - blocked).singleOrNull()
            return target?.let { if (blocked.any { it.isBetweenX(pos, target) }) emptySet() else setOf(target) - pos } ?: emptySet()
        } else {
            // anywhere allowed (mind blocked paths)
            if (pos.y == 2 && Coord(pos.x, 1) in blocked) return emptySet()
            val allowed = (SLOTS2.getValue(type) - blocked)
                .filter { a -> blocked.none { it.y == 0 && it.isBetweenX(pos, a) } }
            return allowed.toSet() - pos
        }
    }

    fun updateWith(target: Coord): PlayerState {
        val newStopped = stopped || target.y == 0
        val addedSteps: Int = abs(target.x - pos.x) + if (pos.y == 0) target.y else pos.y + target.y
        return copy(steps = steps + addedSteps, stopped = newStopped, pos = target)
    }
}
data class GameState(val playerStates: Set<PlayerState>) {
    fun updateWith(p: PlayerState): GameState =
        copy(playerStates = playerStates.map {
            if (it.name == p.name) p else it
        }.toSet())

    fun getEnergyCost(): Long = playerStates.sumOf { p ->
        when (p.type) {
            Type.A -> p.steps.toLong()
            Type.B -> p.steps * 10L
            Type.C -> p.steps * 100L
            Type.D -> p.steps * 1000L
        }
    }
}

fun GameState.isFinished() = playerStates.all { p ->
    when (p.type) {
        Type.A -> p.pos in setOf(Coord(2, 1), Coord(2, 2))
        Type.B -> p.pos in setOf(Coord(4, 1), Coord(4, 2))
        Type.C -> p.pos in setOf(Coord(6, 1), Coord(6, 2))
        Type.D -> p.pos in setOf(Coord(8, 1), Coord(8, 2))
    }
}
private val cache = mutableMapOf<GameState, GameState>()
fun step(g: GameState): GameState {
    return if (g.isFinished()) g
    else {
        cache.getOrPut(g) {
            g.playerStates.associateWith { it.nextMoves(g) }.map { (p, moves) ->
                moves.map { step(g.updateWith(p.updateWith(it))) }
            }.flatten().fold(g) { acc, gs ->
                if (!acc.isFinished() && !gs.isFinished()) acc
                else if (acc.isFinished() && !gs.isFinished()) {
                    acc
                } else if (!acc.isFinished() && gs.isFinished()) gs
                else if (acc.getEnergyCost() < gs.getEnergyCost()) acc else gs
            }
        }
    }
}

fun main() {
    println(step(actualData).getEnergyCost())
}
/*
    #############
    #...........#
    ###B#C#B#D###
      #A#D#C#A#
      #########
 */
private val testData = GameState(setOf(
    PlayerState("A1", Type.A, Coord(2, 2), 0, false),
    PlayerState("A2", Type.A, Coord(8, 2), 0, false),
    PlayerState("B1", Type.B, Coord(2, 1), 0, false),
    PlayerState("B2", Type.B, Coord(6, 1), 0, false),
    PlayerState("C1", Type.C, Coord(4, 1), 0, false),
    PlayerState("C2", Type.C, Coord(6, 2), 0, false),
    PlayerState("D1", Type.D, Coord(8, 1), 0, false),
    PlayerState("D2", Type.D, Coord(4, 2), 0, false),
))

/*
    #############
    #...........#
    ###C#B#D#A###
      #B#D#A#C#
      #########
 */
private val actualData = GameState(setOf(
    PlayerState("A1", Type.A, Coord(8, 1), 0, false),
    PlayerState("A2", Type.A, Coord(6, 2), 0, false),
    PlayerState("B1", Type.B, Coord(4, 1), 0, false),
    PlayerState("B2", Type.B, Coord(2, 2), 0, false),
    PlayerState("C1", Type.C, Coord(2, 1), 0, false),
    PlayerState("C2", Type.C, Coord(8, 2), 0, false),
    PlayerState("D1", Type.D, Coord(6, 1), 0, false),
    PlayerState("D2", Type.D, Coord(4, 2), 0, false),
))
