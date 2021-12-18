package aoc2021.day18

import kotlin.math.ceil
import kotlin.math.floor

sealed class SInt {
    class Literal(var v: Int) : SInt() {
        override fun toString(): String = v.toString()
    }

    class Pair(var left: SInt, var right: SInt) : SInt() {
        override fun toString(): String = "[$left,$right]"
    }
}

fun parse(l: String): SInt {
    val data = l.removePrefix("[").removeSuffix("]")
    var p = 0
    val part1 = data.takeWhile { c ->
        if (c == '[') p++
        if (c == ']') p--
        c != ',' || p != 0
    }
    val part2 = data.removePrefix(part1).drop(1) // drop the comma

    return SInt.Pair(
        if (part1[0].isDigit()) SInt.Literal(part1.toInt()) else parse(part1),
        if (part2[0].isDigit()) SInt.Literal(part2.toInt()) else parse(part2)
    )
}

fun SInt.findFirst(pred: (si: SInt) -> Boolean): SInt? {
    return if (pred(this)) this
    else when (this) {
        is SInt.Literal -> null
        is SInt.Pair -> this.left.findFirst(pred) ?: this.right.findFirst(pred)
    }
}

fun findParent(root: SInt, n: SInt): SInt.Pair? {
    return root.findFirst { it is SInt.Pair && (it.left == n || it.right == n) } as SInt.Pair?
}

fun findClosestNumber(root: SInt, n: SInt, left: Boolean = true): SInt.Literal? {
    fun lVal(x: SInt): SInt.Literal? = if (x is SInt.Literal) x else lVal((x as SInt.Pair).left)
    fun rVal(x: SInt): SInt.Literal? = if (x is SInt.Literal) x else rVal((x as SInt.Pair).right)

    val p = findParent(root, n) ?: return null
    if (left) {
        if (p.right == n) {
            if (p.left is SInt.Literal) {
                return p.left as SInt.Literal
            }
            return rVal(p.left)
        }
        return findClosestNumber(root, p)
    }
    if (p.left == n) {
        if (p.right is SInt.Literal) {
            return p.right as SInt.Literal
        }
        return lVal(p.right)
    }
    return findClosestNumber(root, p, false)
}

fun explode(n: SInt): Triple<Int, SInt.Literal, Int> {
    check(n is SInt.Pair && n.left is SInt.Literal && n.right is SInt.Literal) { "Can only explode pairs of literals" }
    return Triple(
        (n.left as SInt.Literal).v,
        SInt.Literal(0),
        (n.right as SInt.Literal).v
    )//.also { println("$n -> $it") }
}

fun split(n: SInt): SInt {
    check(n is SInt.Literal && n.v >= 10) { "Can only split regular numbers >= 10" }
    return SInt.Pair(
        SInt.Literal(floor(n.v.toDouble() / 2).toInt()),
        SInt.Literal(ceil(n.v.toDouble() / 2).toInt())
    )//.also { println("$n -> $it") }
}

fun reduceStep(root: SInt) {
    root.findFirst { it is SInt.Pair && it.level(root) == 4 }?.let { found ->
        val p = findParent(root, found) ?: throw IllegalStateException("$found should have parent")
        val (ln, new, rn) = explode(found)
        findClosestNumber(root, found)?.let { it.v = it.v + ln }
        findClosestNumber(root, found, false)?.let { it.v = it.v + rn }
        if (p.left == found) p.left = new
        else p.right = new
    }
        ?: root.findFirst { it is SInt.Literal && it.v > 9 }?.let {
            val p = findParent(root, it) ?: throw IllegalStateException("$it should have parent")
            if (p.left == it) p.left = split(it)
            else p.right = split(it)
        }
}

private fun SInt.level(root: SInt): Int {
    tailrec fun helper(curr: SInt, count: Int): Int {
        return if (curr == root) count
        else helper(findParent(root, curr)!!, count + 1)
    }
    return helper(this, 0)
}

fun reduce(root: SInt): SInt {
    var base = root.toString()
    reduceStep(root)
    while (base != root.toString()) {
        base = root.toString()
        reduceStep(root)
    }
    return root
}

fun SInt.add(o: SInt) = reduce(SInt.Pair(this, o))

fun SInt.magnitude(): Long = when (this) {
    is SInt.Literal -> v.toLong()
    is SInt.Pair -> 3 * left.magnitude() + 2 * right.magnitude()
}

fun main() {
    actualData.map { parse(it) }.let { data ->
        println(data.reduce { acc, s -> acc.add(s) }.magnitude())
    }

    actualData.let { data ->
        val max = data.maxOfOrNull { n1 ->
            data.filter { n1 != it }.maxOf { n2 ->
                parse(n1).add(parse(n2)).magnitude().also { println("$n1 + $n2 = $it") }
            }
        }
        println(max) // 4749
    }

}

private val testData = """
    [[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]
    [[[5,[2,8]],4],[5,[[9,9],0]]]
    [6,[[[6,2],[5,6]],[[7,6],[4,7]]]]
    [[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]
    [[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]
    [[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]
    [[[[5,4],[7,7]],8],[[8,3],8]]
    [[9,3],[[9,9],[6,[4,9]]]]
    [[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]
    [[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]
""".trimIndent().lines()

private val actualData = """
    [[[[7,7],2],[[9,2],4]],[[[9,1],5],[[9,6],[6,4]]]]
    [[[2,0],[8,[9,4]]],[[1,0],0]]
    [8,[[[9,5],7],[9,7]]]
    [[[[1,3],[1,8]],[[8,8],5]],[[7,[4,0]],2]]
    [[[[7,8],3],[9,3]],5]
    [[5,[[9,3],4]],[[[0,1],7],[6,[8,3]]]]
    [[[[1,6],[4,1]],[0,3]],[9,[[4,3],[3,2]]]]
    [[[[7,9],8],4],[[[9,0],1],[[9,8],[0,5]]]]
    [[[8,7],[6,1]],[[[1,3],[6,6]],[5,[4,5]]]]
    [[[[9,8],[2,1]],[[2,3],2]],5]
    [6,3]
    [[[9,1],6],[[[7,1],[6,8]],[[8,3],[6,4]]]]
    [4,[[8,[7,1]],[8,[7,2]]]]
    [[[1,6],[9,[0,8]]],[[6,7],[2,[4,5]]]]
    [[[[1,8],[9,2]],5],[[[8,6],[2,1]],[0,6]]]
    [[[[0,2],4],[4,[3,6]]],7]
    [[[[7,5],5],7],[[[6,0],4],[5,0]]]
    [[2,1],[[[3,0],[1,4]],7]]
    [[[[9,4],[2,8]],9],[[[9,1],[7,3]],[1,[2,1]]]]
    [[[[4,2],3],[6,4]],[[6,0],[1,5]]]
    [2,6]
    [[4,6],[[2,2],[3,0]]]
    [[[[6,4],[0,7]],[0,8]],[[[6,7],2],7]]
    [[8,[[4,0],[8,4]]],1]
    [[3,[6,6]],[[[6,4],[1,5]],[4,0]]]
    [[[9,5],[5,[4,0]]],[[1,[0,6]],[[5,8],0]]]
    [[[[6,1],8],[3,7]],[[[6,4],0],[[4,8],4]]]
    [[[[3,1],3],[[3,6],[3,8]]],[[[6,7],0],2]]
    [[4,1],[[[4,8],7],[3,0]]]
    [[[[0,6],[1,3]],[[0,8],[1,9]]],3]
    [[0,[3,1]],[[[0,0],6],[[7,6],3]]]
    [[6,[[5,4],7]],[8,[5,5]]]
    [[[6,3],[[8,9],6]],2]
    [9,[[8,3],7]]
    [[[1,[3,0]],[[3,7],5]],[[5,8],[[3,7],[8,6]]]]
    [[[[6,1],2],[[7,8],[3,9]]],[[[3,6],[6,8]],[5,5]]]
    [[[[6,8],[7,1]],[8,1]],[[[1,6],9],[[3,3],[7,9]]]]
    [[[[6,9],0],[5,6]],3]
    [[[9,6],[[0,5],[2,0]]],[[[6,7],7],[2,6]]]
    [[0,[5,8]],[[1,[4,6]],[4,6]]]
    [[[[3,3],4],[0,1]],[[[6,5],0],[2,3]]]
    [0,4]
    [[5,5],[[[6,5],8],7]]
    [[[[7,3],[9,1]],[[9,0],2]],[[7,[8,3]],[[9,5],[7,3]]]]
    [[[[1,2],[7,7]],[9,0]],[0,7]]
    [[[0,[8,6]],[1,3]],[[6,6],9]]
    [[[0,2],[4,7]],0]
    [[[9,[9,6]],1],[[[1,5],[1,7]],[[5,1],[8,1]]]]
    [[[6,9],4],0]
    [[[[4,9],6],5],[7,[3,[9,8]]]]
    [[6,[6,[5,7]]],[0,[[7,4],8]]]
    [[4,[5,0]],[2,3]]
    [[[[8,6],9],[3,[1,2]]],[1,[8,[3,8]]]]
    [[[8,4],[7,2]],9]
    [[[[6,3],[6,2]],[2,[0,0]]],[[[6,4],[1,6]],[[3,5],6]]]
    [7,[[[2,4],0],[9,[9,9]]]]
    [[[9,2],8],[[2,[9,9]],[9,[7,4]]]]
    [1,[[0,7],[[1,6],0]]]
    [[[[5,5],4],8],[[9,[6,5]],[[7,4],7]]]
    [[[[7,6],4],[8,4]],[2,[1,[5,1]]]]
    [[[2,[1,2]],7],[7,[[9,9],3]]]
    [1,[[3,[9,9]],[5,6]]]
    [[3,[[1,8],4]],[[9,[6,9]],2]]
    [[[2,[4,5]],[1,[9,0]]],[4,1]]
    [[[7,[5,9]],[7,7]],[[3,[4,0]],[2,[0,0]]]]
    [[[0,[9,8]],0],[8,[7,1]]]
    [[[6,6],[0,[4,8]]],3]
    [[1,[[8,2],[9,9]]],3]
    [[2,[5,[6,7]]],[[5,3],3]]
    [[2,[[5,0],[8,5]]],[[7,[0,5]],[[5,7],3]]]
    [[[[9,4],[4,0]],[6,[7,8]]],[[7,6],1]]
    [[0,2],6]
    [[[7,5],[[7,4],[4,1]]],[3,[[6,6],[5,5]]]]
    [[3,[[0,7],8]],[[1,7],[5,0]]]
    [[9,[[9,7],[3,0]]],6]
    [[[[7,9],2],[3,[5,4]]],[[[9,4],[5,8]],[[5,0],[4,2]]]]
    [[[[4,3],6],7],[[2,6],[5,[0,1]]]]
    [[1,[3,5]],[[4,[5,0]],1]]
    [[[9,[3,9]],8],[9,[[2,9],[2,2]]]]
    [[[0,[5,0]],[[4,4],3]],6]
    [[[9,3],[[2,4],[8,4]]],[[[6,8],[3,6]],[[4,6],[1,2]]]]
    [[[[8,2],[3,2]],[4,[1,1]]],[[[7,2],1],[[9,9],[0,5]]]]
    [[[6,3],[[3,6],9]],[6,5]]
    [8,[[[8,7],3],[4,3]]]
    [[[[8,3],3],[[6,1],9]],[[[2,4],[5,9]],[[9,7],1]]]
    [[[2,[6,4]],[[0,1],3]],[[[1,2],9],[4,7]]]
    [7,9]
    [[[3,[1,4]],5],[[4,[5,1]],8]]
    [[[[7,6],4],0],[5,5]]
    [[4,[[5,2],5]],[[[0,4],[6,1]],[[3,0],[4,9]]]]
    [[[[8,6],[6,1]],9],[[[4,1],2],[[9,2],3]]]
    [[[6,1],[[8,9],[9,0]]],[[[4,4],[3,0]],[[4,2],[9,9]]]]
    [1,[[[8,8],3],7]]
    [[1,[4,[6,8]]],[1,[7,0]]]
    [6,[[3,[2,4]],[[4,5],[5,3]]]]
    [8,[[9,[6,0]],[[2,5],0]]]
    [[5,[0,8]],[[7,1],[[5,9],2]]]
    [[[5,8],[[1,1],4]],[[0,1],[4,3]]]
    [[3,[1,[7,3]]],[[[6,4],9],[[2,8],[0,1]]]]
    [[[6,[2,5]],5],[0,[[5,3],[4,2]]]]
""".trimIndent().lines()
