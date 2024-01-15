package co.danhill.aoc.year2023

import co.danhill.aoc.util.*

fun main() = Day18.run()

object Day18 : Day(2023, 18) {
    override fun part1(input: Input): Any {
        val grid = gridOf { '.' }
        var currentPoint = Point(0, 0)
        grid[currentPoint] = '#'
        input.lines.forEach { line ->
            val (dir, num) = line.split(' ')
            val direction = Direction.fromChar(dir.single())
            repeat(num.toInt()) {
                currentPoint = currentPoint.step(direction)
                grid[currentPoint] = '#'
            }
        }
        grid.floodFill()
        return grid.size
    }

    override fun part2(input: Input): Any {
        return ""
    }

    private fun Grid<Char>.floodFill() {
        val points = mutableSetOf(findFloodFillPoint())

        val visited = mutableSetOf<Point>()

        while (points.isNotEmpty()) {
            val point = points.first()
            points.remove(point)
            this[point] = '#'
            visited += point
            points += point.neighbors.filter { this[it] == null && !visited.contains(it) }
        }
    }

    private fun Grid<Char>.findFloodFillPoint(): Point {
        val y = minY + 1
        var hasSeenFirstSide = false
        for (x in minX..maxX) {
            val point = x to y
            if (hasSeenFirstSide && this[point] == null) {
                return point
            } else if (this[point] == '#') {
                hasSeenFirstSide = true
            }
        }
        error("No flood fill point found!")
    }
}
