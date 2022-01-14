package co.danhill.aoc.year2021

import co.danhill.aoc.util.Day
import co.danhill.aoc.util.Grid
import co.danhill.aoc.util.findPath
import co.danhill.aoc.util.toIntGrid

fun main() {
    Day15.run("2021/15.txt")
}

object Day15 : Day<Grid<Int>>() {

    override fun parseInput(input: Sequence<String>): Grid<Int> {
        return input.toIntGrid()
    }

    override fun part1(input: Grid<Int>): String {
        val start = 0 to 0
        val end = input.maxX to input.maxY
        val path = input.findPath(
            start,
            end,
            movementCost = { input[it]!! },
        )
        val risk = path.sumOf { if (it == start) 0 else input[it]!! }
        return "$risk"
    }

    override fun part2(input: Grid<Int>): String {
        input.embiggen()
        val start = 0 to 0
        val end = input.maxX to input.maxY
        val path = input.findPath(
            start,
            end,
            movementCost = { input[it]!! },
        )
        val risk = path.sumOf { if (it == start) 0 else input[it]!! }
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