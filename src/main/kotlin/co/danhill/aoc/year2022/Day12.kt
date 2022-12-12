package co.danhill.aoc.year2022

import co.danhill.aoc.util.*

fun main() = Day12.run("2022/12.txt")

object Day12 : Day {

    private fun Input.parse(): Triple<Grid<Char>, Point, Point> {
        val grid = lines.toCharGrid()
        val start = grid.entries.find { (_, c) -> c == 'S' }!!.key
        grid[start] = 'a'
        val end = grid.entries.find { (_, c) -> c == 'E' }!!.key
        grid[end] = 'z'
        return Triple(grid, start, end)
    }

    private val Grid<Char>.movementCost: (Point, Point) -> Int
        get() = { from, to ->
            when (this[from]!! - this[to]!!) {
                in -1..Int.MAX_VALUE -> 1
                else -> Int.MAX_VALUE
            }
        }

    override fun part1(input: Input): Any {
        val (grid, start, end) = input.parse()
        return grid.findPath(start, end, grid.movementCost).size - 1
    }

    override fun part2(input: Input): Any {
        val (grid, _, end) = input.parse()
        return grid.filter { (_, char) -> char == 'a' }
            .mapNotNull { (start, _) -> grid.findPath(start, end, grid.movementCost) }
            .filter { it.isNotEmpty() }
            .minOf { it.size - 1 }
    }
}
