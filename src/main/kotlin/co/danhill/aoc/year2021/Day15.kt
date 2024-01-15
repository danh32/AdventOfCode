package co.danhill.aoc.year2021

import co.danhill.aoc.util.*

fun main() {
    Day15.run()
}

object Day15 : Day(2021, 15) {

    private fun Input.parse(): Grid<Int> = lines.toIntGrid()

    override fun part1(input: Input): String {
        val grid = input.parse()
        val start = 0 to 0
        val end = grid.maxX to grid.maxY
        val path = grid.findPath(
            start,
            end,
            movementCost = { _, to -> grid[to]!! },
        )
        val risk = path.sumOf { if (it == start) 0 else grid[it]!! }
        return "$risk"
    }

    override fun part2(input: Input): String {
        val grid = input.parse()
        grid.embiggen()
        val start = 0 to 0
        val end = grid.maxX to grid.maxY
        val path = grid.findPath(
            start,
            end,
            movementCost = { _, to -> grid[to]!! },
        )
        val risk = path.sumOf { if (it == start) 0 else grid[it]!! }
        return "$risk"
    }

    private fun Grid<Int>.embiggen() {
        val maxX = maxX
        val maxY = maxY

        val newMaxX = ((maxX + 1) * 5) - 1
        val newMaxY = ((maxY + 1) * 5) - 1
        for (y in 0..newMaxY) {
            for (x in 0..newMaxX) {
                val originalX = x % (maxX + 1)
                val originalY = y % (maxY + 1)
                val original = getValue(originalX to originalY)
                val tileDistanceX = (x - originalX) / maxX
                val tileDistanceY = (y - originalY) / maxY
                val tileDistance = tileDistanceX + tileDistanceY
                val new = ((original + tileDistance - 1) % 9) + 1
                set(x to y, new)
            }
        }
    }
}