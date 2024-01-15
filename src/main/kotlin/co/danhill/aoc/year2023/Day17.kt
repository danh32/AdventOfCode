package co.danhill.aoc.year2023

import co.danhill.aoc.util.*

fun main() = Day17.run()

object Day17 : Day(2023, 17) {
    override fun part1(input: Input): Any {
        val grid = input.lines.toIntGrid()
        val path = grid.findPath(
            start = grid.minX to grid.minY,
            end = grid.maxX to grid.maxY,
            movementCost = { _, to -> grid[to]!! },
            generateNextStates = { point, sequence ->
                val disallowedDirection = sequence.disallowedDirection()
                val candidates = point.cardinalNeighbors
                    .filter { grid.contains(it) }
                    .filter { candidate -> point.directionTo(candidate)!! != disallowedDirection }
//                println(candidates)
                candidates
            }
        )
        for (y in grid.minY..grid.maxY) {
            for (x in grid.minX..grid.maxX) {
                if (path.contains(x to y)) print('X')
                else print(grid[x to y])
            }
            println()
        }
        return path.drop(1).sumOf {
//            println(it)
            grid.getValue(it)
        }
    }

    override fun part2(input: Input): Any {
        return ""
    }

    private fun Sequence<Point>.disallowedDirection(): Direction? {
        val directions = take(4)
            .windowed(2, 1)
            .map { (from, to) -> to.directionTo(from)!! }
            .toList()
        val mustTurn = directions.size == 3 && directions.all { it == directions[0] }
        return if (mustTurn) directions[0] else null
    }
}
