package co.danhill.aoc.year2023

import co.danhill.aoc.util.*

fun main() = Day23.run()

object Day23 : Day(2023, 23) {
    override fun part1(input: Input): Any {
        val grid = input.lines.toCharGrid()
        val start: Point = 1 to 0
        val end: Point = grid.maxX - 1 to grid.maxY

        val completedPaths = mutableListOf<MutableList<Point>>()
        val growablePaths = mutableListOf<MutableList<Point>>()
        growablePaths.add(mutableListOf(start))

        while (growablePaths.isNotEmpty()) {
            val path = growablePaths.removeFirst()
            val last = path.last()
            if (last == end) {
                completedPaths += path
                continue
            }
            val currentChar = grid.getValue(last)
            if (currentChar == '.') {
                val nextSteps = last.cardinalNeighbors.filter { p ->
                    p != path.getOrNull(path.size - 2) && grid.canMove(last, p)
                }
                when (nextSteps.size) {
                    0 -> {}
                    1 -> {
                        path += nextSteps.single()
                        growablePaths += path
                    }
                    in 2..4 -> {
                        nextSteps.forEach { nextStep ->
                            growablePaths.add((path + nextStep).toMutableList())
                        }
                    }
                    else -> error ("Invalid number of steps ${nextSteps.size} from point $last")
                }
            } else {
                // path is forced by the slope
                path += last.step(Direction.fromChar(currentChar))
                if (grid[path.last()] == '#') error("Sloped into a wall")
                growablePaths += path
            }
        }

        completedPaths.forEach { println(it.size - 1) }
        return completedPaths.maxOf { it.size } - 1
    }

    override fun part2(input: Input): Any {
        val grid = input.lines.toCharGrid()
        val start: Point = 1 to 0
        val end: Point = grid.maxX - 1 to grid.maxY

        var longestPath = emptyList<Point>()
        val growablePaths = mutableListOf<MutableList<Point>>()
        growablePaths.add(mutableListOf(start))

        while (growablePaths.isNotEmpty()) {
            val path = growablePaths.removeFirst()
            val last = path.last()
            if (last == end) {
                if (path.size > longestPath.size) {
                    longestPath = path
                    println("New longest path size = ${longestPath.size}")
                }
                continue
            }
            val nextSteps = last.cardinalNeighbors.filter { p ->
                grid[p]?.isWalkable() == true && !path.contains(p)
            }
            when (nextSteps.size) {
                0 -> {}
                1 -> {
                    path += nextSteps.single()
                    growablePaths += path
                }
                in 2..4 -> {
                    nextSteps.forEach { nextStep ->
                        growablePaths.add((path + nextStep).toMutableList())
                    }
                }
                else -> error ("Invalid number of steps ${nextSteps.size} from point $last")
            }
        }

        for (y in grid.yRange) {
            for (x in grid.xRange) {
                val p = x to y
                print(
                    if (longestPath.contains(p)) 'O'
                    else grid[p]
                )
            }
            println()
        }
        return longestPath.size - 1
    }

    private fun Grid<Char>.canMove(from: Point, to: Point): Boolean {
        val f = getValue(from)
        if (!f.isWalkable()) error("Un-walkable at $from")
        return when (f) {
            '.' -> when (val t = this[to]) {
                '.' -> true
                '<' -> to != from.right
                '^' -> to != from.down
                '>' -> to != from.left
                'v' -> to != from.up
                '#' -> false
                null -> false
                else -> error ("Invalid char $t")
            }
            '<' -> to == from.left
            '^' -> to == from.up
            '>' -> to == from.right
            'v' -> to == from.down
            else -> error ("Invalid char $f")
        }
    }

    private fun Char.isWalkable() = when (this) {
        '#' -> false
        '.',
        '<',
        '^',
        '>',
        'v' -> true
        else -> error("Unknown tile $this")
    }
}
