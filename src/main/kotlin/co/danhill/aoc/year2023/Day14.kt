package co.danhill.aoc.year2023

import co.danhill.aoc.util.*

fun main() = Day14.run()

object Day14 : Day(2023, 14) {
    override fun part1(input: Input): Any {
        val grid = input.lines.toCharGrid()
        grid.tiltNorth()
        return grid.load()
    }

    override fun part2(input: Input): Any {
        val grid = input.lines.toCharGrid()
        repeat(1_000_000_000) {
            println(it)
            grid.cycle()
        }
        return grid.load()
    }

    private fun Grid<Char>.cycle() {
        tiltNorth()
        tiltWest()
        tiltSouth()
        tiltEast()
    }

    private fun Grid<Char>.tiltNorth() {
        for (x in minX..maxX) {
            for (y in minY..maxY) {
                when (get(x to y)) {
                    '.' -> continue
                    '#' -> continue
                    'O' -> moveRock(x to y) { it.up }
                }
            }
        }
    }

    private fun Grid<Char>.tiltWest() {
        for (y in minY..maxY) {
            for (x in minX..maxX) {
                when (get(x to y)) {
                    '.' -> continue
                    '#' -> continue
                    'O' -> moveRock(x to y) { it.left }
                }
            }
        }
    }

    private fun Grid<Char>.tiltSouth() {
        for (x in minX..maxX) {
            for (y in minY..maxY) {
                when (get(x to y)) {
                    '.' -> continue
                    '#' -> continue
                    'O' -> moveRock(x to y) { it.down }
                }
            }
        }
    }

    private fun Grid<Char>.tiltEast() {
        for (y in minY..maxY) {
            for (x in minX..maxX) {
                when (get(x to y)) {
                    '.' -> continue
                    '#' -> continue
                    'O' -> moveRock(x to y) { it.right }
                }
            }
        }
    }

    private fun Grid<Char>.moveRock(
        start: Point,
        moveFunction: (Point) -> Point,
    ) {
        if (get(start) != 'O') error("Invalid rock ${get(start)}")
        put(start, '.')
        var lastPoint = start
        var currentPoint = start
        while (get(currentPoint) == '.' && contains(currentPoint)) {
            lastPoint = currentPoint
            currentPoint = moveFunction(currentPoint)
        }
        put(lastPoint, 'O')
    }



    private fun Grid<Char>.load(): Int {
        return entries
            .filter { (_, c) -> c == 'O' }
            .sumOf { (p, _) ->
                (maxY - p.y) + 1
            }
    }
}
