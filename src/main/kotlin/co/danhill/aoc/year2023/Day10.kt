package co.danhill.aoc.year2023

import co.danhill.aoc.util.*

fun main() = Day10.run()

object Day10 : Day(2023, 10) {
    override fun part1(input: Input): Any {
        val grid = input.lines.toCharGrid()
        val start = grid.entries.find { (_, char) -> char == 'S' }!!.key
        val (a, b) = start.cardinalNeighbors.filter { grid.canMove(start, it) }

        var prevA = start
        var prevB = start
        var curA = a
        var curB = b
        var steps = 1
        while (curA != curB && curA != prevB) {
            println("A: $prevA (${grid[prevA]}) -> $curA (${grid[curA]})")
            println("B: $prevB (${grid[prevB]}) -> $curB (${grid[curB]})")
            val nextA = curA.cardinalNeighbors
                .filter { grid.canMove(curA, it) && it != prevA }
                .onEach { println("Candidates: $it") }
                .single()
            val nextB = curB.cardinalNeighbors.single { grid.canMove(curB, it) && it != prevB }
            prevA = curA
            curA = nextA
            prevB = curB
            curB = nextB
            steps++
        }
        return steps
    }

    override fun part2(input: Input): Any {
        return ""
    }

    fun Grid<Char>.canMove(
        from: Point,
        to: Point,
    ): Boolean {
        val fromChar = get(from) ?: error("Invalid from point: $from")
        val toChar = get(to) ?: return false
        if (toChar == '.') return false
        return when {
            // moving right
            from.right == to && fromChar in allowsEast && toChar in allowsWest -> true
            // moving left
            from.left == to && fromChar in allowsWest && toChar in allowsEast -> true
            // moving up
            from.up == to && fromChar in allowsNorth && toChar in allowsSouth -> true
            // moving down
            from.down == to && fromChar in allowsSouth && toChar in allowsNorth -> true
            // all others, false
            else -> false
        }
    }

    private val allowsSouth = setOf('F', '|', '7', 'S')
    private val allowsNorth = setOf('L', '|', 'J', 'S')
    private val allowsEast = setOf('L', '-', 'F', 'S')
    private val allowsWest = setOf('7', '-', 'J', 'S')
}
