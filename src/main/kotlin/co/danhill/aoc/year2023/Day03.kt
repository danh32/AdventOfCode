package co.danhill.aoc.year2023

import co.danhill.aoc.util.*

fun main() = Day03.run()

object Day03 : Day(2023, 3) {
    override fun part1(input: Input): Any {
        val grid = input.lines.toCharGrid()
        return grid
            // symbol points
            .mapNotNull { (p, c) -> if (c.isSymbol()) p else null }
            // adjacent number origins
            .flatMap { point ->
                point.neighbors
                    // adjacent digits
                    .filter { grid[it]?.isDigit() == true }
                    // adjacent number origins
                    .map { grid.getNumberOrigin(it) }
            }
            // de-duplicated
            .toSet()
            .sumOf { p -> grid.readNumber(p) }
    }

    override fun part2(input: Input): Any {
        val grid = input.lines.toCharGrid()
        return grid
            // asterisk points
            .mapNotNull { (p, c) -> if (c == '*') p else null }
            // gear powers
            .mapNotNull { asteriskPoint ->
                val adjacentNumberOrigins = asteriskPoint.neighbors
                    // adjacent digits
                    .filter { grid[it]?.isDigit() == true }
                    // adjacent number origins
                    .map { grid.getNumberOrigin(it) }
                    // de-duplicated
                    .toSet()
                if (adjacentNumberOrigins.size == 2) {
                    // multiplied
                    adjacentNumberOrigins.fold(1) { mult, p -> mult * grid.readNumber(p) }
                } else {
                    null
                }
            }
            // summed
            .sum()
    }

    private fun Char.isSymbol(): Boolean = when {
        this == '.' -> false
        isDigit() -> false
        else -> true
    }

    private fun Grid<Char>.getNumberOrigin(point: Point): Point {
        if (this[point]?.isDigit() != true) error("Not a digit at $point")
        var current = point.left
        while (this[current]?.isDigit() == true) {
            current = current.left
        }
        return current.right
    }

    private fun Grid<Char>.readNumber(point: Point): Int {
        if (this[point]?.isDigit() != true) error("Not a digit at $point")
        return buildString {
            var current = point
            while (this@readNumber[current]?.isDigit() == true) {
                append(this@readNumber[current]!!)
                current = current.right
            }
        }.toInt()
    }
}
