package co.danhill.aoc.year2021

import co.danhill.aoc.util.*

fun main() {
    // 6007, 19349
    Day05.run()
}

object Day05 : Day(2021, 5) {

    override fun part1(input: Input): String {
        val grid = gridOf<Int>()
        input.lines.map { line ->
            val split = line.split(" -> ", ",")
            val x1 = split[0].toInt()
            val y1 = split[1].toInt()
            val x2 = split[2].toInt()
            val y2 = split[3].toInt()
            grid.addLine(x1, y1, x2, y2)
        }
        grid.printRange()
        val count = grid.values.count { it > 1 }
        return "$count"
    }

    override fun part2(input: Input): String {
        val grid = gridOf<Int>()
        input.lines.map { line ->
            val split = line.split(" -> ", ",")
            val x1 = split[0].toInt()
            val y1 = split[1].toInt()
            val x2 = split[2].toInt()
            val y2 = split[3].toInt()
            grid.addLine(x1, y1, x2, y2, allowDiagonals = true)
        }
        grid.printRange()
        val count = grid.values.count { it > 1 }
        return "$count"
    }

    private fun Grid<Int>.addLine(x1: Int, y1: Int, x2: Int, y2: Int, allowDiagonals: Boolean = false) {
        if (x1 == x2) {
            val range = if (y1 < y2) y1..y2 else y2..y1
            range.forEach { y ->
                addAtPoint(x1 to y)
            }
            return
        }

        if (y1 == y2) {
            val range = if (x1 < x2) x1..x2 else x2..x1
            range.forEach { x ->
                addAtPoint(x to y1)
            }
            return
        }

        if (allowDiagonals) {
            var currentX = x1
            var currentY = y1
            do {
                addAtPoint(currentX to currentY)
                if (x1 <= x2) currentX++ else currentX--
                if (y1 <= y2) currentY++ else currentY--
            } while (currentX != x2 && currentY != y2)

            addAtPoint(currentX to currentY)
        }
    }

    private fun Grid<Int>.addAtPoint(point: Point) {
        putIfAbsent(point, 0)
        this[point] = this[point]!! + 1
    }

    private fun Grid<Int>.printRange() = print(
        xRange = 0..20,
        yRange = 0..20,
    ) { value ->
        value?.toString() ?: "."
    }
}