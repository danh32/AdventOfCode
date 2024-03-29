package co.danhill.aoc.year2021

import co.danhill.aoc.util.*

fun main() {
    Day11.run()
}

object Day11 : Day(2021, 11) {

    private fun Input.parse(): Grid<Int> {
        val grid = gridOf<Int>()
        lines.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                grid[x to y] = c.digitToInt()
            }
        }
        return grid
    }

    override fun part1(input: Input): String {
        val flashes = (0 until 100).sumOf { step(input.parse()) }
        return "$flashes"
    }

    override fun part2(input: Input): String {
        val grid = input.parse()
        (0..Int.MAX_VALUE).forEach { i ->
            val flashed = step(grid)
            if (flashed == grid.keys.size) {
                return "${i + 1}"
            }
        }

        error("No answer found")
    }

    private fun step(grid: Grid<Int>): Int {
        val toFlash = grid
            .mapNotNull { (p, v) ->
                val newValue = v + 1
                grid[p] = newValue
                if (newValue > 9) p else null
            }
            .toMutableList()

        val hasFlashed = mutableListOf<Point>()
        while (toFlash.isNotEmpty()) {
            val p = toFlash.removeFirst()
            if (hasFlashed.contains(p)) continue
            hasFlashed += p
            toFlash += grid.incrementNeighbors(p)
        }

        hasFlashed.forEach { p ->
            grid[p] = 0
        }
        return hasFlashed.size
    }

    private fun Grid<Int>.incrementNeighbors(point: Point): List<Point> {
        return point.neighbors
            .mapNotNull { neighbor ->
                if (incrementPoint(neighbor)) {
                    neighbor
                } else {
                    null
                }
            }
    }

    private fun Grid<Int>.incrementPoint(point: Point): Boolean {
        val oldValue = this[point] ?: return false
        this[point] = oldValue + 1
        return oldValue == 9
    }

//    private fun incrementAdjacents(board: MutableMap<Pair<Int, Int>, Int>, x: Int, y: Int): List<Pair<Int, Int>> {
//        val needsFlashed = mutableListOf<Pair<Int, Int>>()
//        for (x2 in x - 1..x + 1) {
//            for (y2 in y - 1..y + 1) {
//                if (x2 == x && y2 == y) continue
//                if (incrementPoint(board, x2, y2)) {
//                    needsFlashed += x2 to y2
//                }
//            }
//        }
//        return needsFlashed
//    }

//    private fun incrementPoint(board: MutableMap<Pair<Int, Int>, Int>, x: Int, y: Int): Boolean {
//        val oldValue = board[x to y] ?: return false
//        board[x to y] = oldValue + 1
//        return oldValue == 9
//    }
}