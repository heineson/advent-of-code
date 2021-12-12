package aoc2021.day12

import java.util.*

data class Edge(val start: String, val end: String)
data class Graph(val edges: Set<Edge>) {
    val vertices = edges.map { listOf(it.start, it.end) }.flatten().distinct()
    private val allPathsFrom: Map<String, List<String>> by lazy {
        val allEdges = edges.map { listOf(it, Edge(it.end, it.start)) }.flatten().distinct()
        vertices.associateWith { v -> allEdges.filter { it.start == v }.map { it.end } }
    }

    fun neighbors(v: String): Set<String> = allPathsFrom.getValue(v).toSet()
}

fun findPaths(graph: Graph, visited: Set<String>, path: List<String>, endPoint: String, acc: Set<List<String>>): Set<List<String>> {
    val queue = LinkedList<String>()
    queue.add(path.last())

    while (queue.size != 0) {
        val v = queue.poll()
        return if (v == endPoint) {
            acc + listOf(path)
        } else {
            val more = graph.neighbors(v).filter { !visited.contains(it) }.map { n ->
                queue.add(n)
                val newVisited = if (v[0].isUpperCase()) visited else visited + v
                findPaths(graph, newVisited, path + listOf(n), endPoint, acc)
            }.flatten()
            acc + more
        }
    }
    return acc
}

fun main() {
    actualData.let { data ->
        val edges = data.map { it.split('-') }.map { Edge(it[0], it[1]) }
        val graph = Graph(edges.toSet())
        println(findPaths(graph, setOf("start"), listOf("start"), "end", setOf()).size) // 4573
    }
}

val testData = """
    start-A
    start-b
    A-c
    A-b
    b-d
    A-end
    b-end
""".trimIndent().lines()

val actualData = """
    fw-ll
    end-dy
    tx-fw
    tx-tr
    dy-jb
    ZD-dy
    dy-BL
    dy-tr
    dy-KX
    KX-start
    KX-tx
    fw-ZD
    tr-end
    fw-jb
    fw-yi
    ZD-nr
    start-fw
    tx-ll
    ll-jb
    yi-jb
    yi-ll
    yi-start
    ZD-end
    ZD-jb
    tx-ZD
""".trimIndent().lines()
