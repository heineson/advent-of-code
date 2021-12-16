package aoc2021.day16

val hexToBinary = mapOf(
    '0' to "0000",
    '1' to "0001",
    '2' to "0010",
    '3' to "0011",
    '4' to "0100",
    '5' to "0101",
    '6' to "0110",
    '7' to "0111",
    '8' to "1000",
    '9' to "1001",
    'A' to "1010",
    'B' to "1011",
    'C' to "1100",
    'D' to "1101",
    'E' to "1110",
    'F' to "1111",
)

fun readInput(hex: String): String = hex.map { hexToBinary.getValue(it) }.joinToString("")

fun version(packet: String): Int = packet.take(3).toInt(2)
fun id(packet: String): Int = packet.take(3).toInt(2)
fun literalData(data: String, version: Int, id: Int): Pair<Int, Packet> {
    var done = false
    val numbers = data
        .windowed(5, 5)
        .takeWhile {
            val curr = done
            done = it[0] == '0'
            !curr
        }
    return Pair(
        numbers.sumOf { it.length },
        Packet(version, id, numbers.joinToString("") { it.drop(1) }.toLong(2), emptyList())
    )
}

fun getSubpackets(data: String): String {
  val bits = data.take(15).toInt(2)
  return data.drop(15).take(bits)
}
fun containedPacketsCount(data: String): Int = data.take(11).toInt(2)

data class Packet(val version: Int, val id: Int, val literal: Long?, val packets: List<Packet>)

fun readPacket(packet: String): Pair<Int, Packet> {
    val version = version(packet)
    val id = id(packet.drop(3))
    val data = packet.drop(6)
    return when (id) {
        4 -> literalData(data, version, id)
        0 -> subpackets(data, version, id).let { Pair(it.first, it.second.copy(literal = it.second.packets.mapNotNull { s -> s.literal }.sum())) }
        1 -> subpackets(data, version, id).let { Pair(it.first, it.second.copy(literal = it.second.packets.mapNotNull { s -> s.literal }.reduce { acc, l -> acc * l })) }
        2 -> subpackets(data, version, id).let { Pair(it.first, it.second.copy(literal = it.second.packets.mapNotNull { s -> s.literal }.minOrNull()?.toLong())) }
        3 -> subpackets(data, version, id).let { Pair(it.first, it.second.copy(literal = it.second.packets.mapNotNull { s -> s.literal }.maxOrNull()?.toLong())) }
        5 -> subpackets(data, version, id).let { Pair(it.first, it.second.copy(literal = it.second.packets.mapNotNull { s -> s.literal }.reduce { acc, l -> if (acc > l) 1L else 0L } )) }
        6 -> subpackets(data, version, id).let { Pair(it.first, it.second.copy(literal = it.second.packets.mapNotNull { s -> s.literal }.reduce { acc, l -> if (acc < l) 1L else 0L } )) }
        7 -> subpackets(data, version, id).let { Pair(it.first, it.second.copy(literal = it.second.packets.mapNotNull { s -> s.literal }.reduce { acc, l -> if (acc == l) 1L else 0L } )) }
        else -> throw IllegalStateException()
    }.also { println("$packet = $it") }
}

private fun subpackets(data: String, version: Int, id: Int): Pair<Int, Packet> {
    var readBits = 0
    return when (data.take(1).toInt(2).also { readBits++ }) {
        0 -> getSubpackets(data.drop(readBits)).also { readBits += 15 }.let { subpackets ->
            var subpacketReadBits = 0
            val results = mutableListOf<Packet>()
            while (subpacketReadBits < subpackets.length) {
                val r = readPacket(subpackets.drop(subpacketReadBits))
                subpacketReadBits += r.first + 6
                results.add(r.second)
            }
            readBits += subpacketReadBits
            Pair(readBits, Packet(version, id, null, results))
        }
        else -> {
            val packets = containedPacketsCount(data.drop(readBits)).also { readBits += 11 }
            val results = mutableListOf<Packet>()
            (1..packets).forEach {
                val r = readPacket(data.drop(readBits))
                readBits += r.first + 6
                results.add(r.second)
            }
            Pair(readBits, Packet(version, id, null, results))
        }
    }
}

fun <T> subpacketOp(packet: Packet, op: (p: Packet) -> T): List<T> =
    listOf(op(packet)) + packet.packets.flatMap { subpacketOp(it, op) }
fun versionCnt(packet: Packet): Int = subpacketOp(packet) { it.version }.sum()

fun main() {
    actualData.let { data ->
        val input = readInput(data)
        val packets = readPacket(input)
        println("${packets}")
        println(versionCnt(packets.second)) // 938
    }

    actualData.let { data ->
        val input = readInput(data)
        val packets = readPacket(input)
        println(packets.second.literal) // 1495959086337
    }
}

private val testData = """
    9C0141080250320F1802104A08
""".trimIndent()


private val actualData = """
    820D4100A1000085C6E8331F8401D8E106E1680021708630C50200A3BC01495B99CF6852726A88014DC9DBB30798409BBDF5A4D97F5326F050C02F9D2A971D9B539E0C93323004B4012960E9A5B98600005DA7F11AFBB55D96AFFBE1E20041A64A24D80C01E9D298AF0E22A98027800BD4EE3782C91399FA92901936E0060016B82007B0143C2005280146005300F7840385380146006900A72802469007B0001961801E60053002B2400564FFCE25FEFE40266CA79128037500042626C578CE00085C718BD1F08023396BA46001BF3C870C58039587F3DE52929DFD9F07C9731CC601D803779CCC882767E668DB255D154F553C804A0A00DD40010B87D0D6378002191BE11C6A914F1007C8010F8B1122239803B04A0946630062234308D44016CCEEA449600AC9844A733D3C700627EA391EE76F9B4B5DA649480357D005E622493112292D6F1DF60665EDADD212CF8E1003C29193E4E21C9CF507B910991E5A171D50092621B279D96F572A94911C1D200FA68024596EFA517696EFA51729C9FB6C64019250034F3F69DD165A8E33F7F919802FE009880331F215C4A1007A20C668712B685900804ABC00D50401C89715A3B00021516E164409CE39380272B0E14CB1D9004632E75C00834DB64DB4980292D3918D0034F3D90C958EECD8400414A11900403307534B524093EBCA00BCCD1B26AA52000FB4B6C62771CDF668E200CC20949D8AE2790051133B2ED005E2CC953FE1C3004EC0139ED46DBB9AC9C2655038C01399D59A3801F79EADAD878969D8318008491375003A324C5A59C7D68402E9B65994391A6BCC73A5F2FEABD8804322D90B25F3F4088F33E96D74C0139CF6006C0159BEF8EA6FBE3A9CEC337B859802B2AC9A0084C9DCC9ECD67DD793004E669FA2DE006EC00085C558C5134001088E308A20
""".trimIndent()
