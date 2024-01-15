package co.danhill.aoc.year2021

import co.danhill.aoc.util.*

fun main() {
    Day09.run()
}

object Day09 : Day(2021, 9) {

    private fun Input.parse(): Grid<Int> {
        val points = gridOf<Int>()
        lines.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                points[x to y] = c.digitToInt()
            }
        }
        return points
    }

    override fun part1(input: Input): String {
        val grid = input.parse()
        val lows = getLows(grid)
        val sum = lows.sumOf { p -> grid[p]!! + 1 }
        return "$sum"
    }

    override fun part2(input: Input): String {
        val grid = input.parse()
        val lows = getLows(grid)
        val basins = getBasins(grid, lows)
        val score = basins.map { it.points.size }
            .sortedDescending()
            .take(3)
            .reduce { a, b -> a * b}
        return "$score"
    }

    private fun getLows(grid: Grid<Int>): List<Point> {
        return grid.mapNotNull { (point, height) ->
            val isLow = point.cardinalNeighbors
                .mapNotNull { grid[it] }
                .all { height < it }
            if (isLow) point else null
        }
    }

    private fun getBasins(grid: Grid<Int>, lows: List<Point>): List<Basin> {
        return lows.map { low ->
            computeBasin(grid, low)
        }
    }

    private fun computeBasin(grid: Grid<Int>, low: Point): Basin {
        val basinPoints = mutableListOf<Point>()
        val visitedPoints = mutableListOf<Point>()
        val candidates = mutableListOf(low)
        while (candidates.isNotEmpty()) {
            val point = candidates.removeFirst()
            if (visitedPoints.contains(point)) continue
            val candidateHeight = grid[point] ?: 9
            if (candidateHeight != 9) {
                basinPoints.add(point)
                candidates.addAll(point.cardinalNeighbors)
            }
            visitedPoints += point
        }
        return Basin(basinPoints)
    }

    private class Basin(val points: List<Point>) {
        override fun toString(): String {
            return points.toString()
        }
    }
}