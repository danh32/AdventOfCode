package co.danhill.aoc.year2022

import co.danhill.aoc.util.*
import kotlin.math.max
import kotlin.math.min

fun main() = Day23.run("2022/23.txt")

object Day23 : Day {

    private fun Input.parse() = lines.toCharGrid()

    override fun part1(input: Input): Any {
        val map = input.parse()
        val directions = mutableListOf(Directions.NORTH, Directions.SOUTH, Directions.WEST, Directions.EAST)
        var rounds = 0
        while (map.moveElves(directions) && rounds < 10) {
            directions.rotate()
            rounds++
        }
        return map.groundCount()
    }

    override fun part2(input: Input): Any {
        val map = input.parse()
        val directions = mutableListOf(Directions.NORTH, Directions.SOUTH, Directions.WEST, Directions.EAST)
        var rounds = 0
        while (map.moveElves(directions)) {
            directions.rotate()
            rounds++
        }

        return rounds
    }

    private fun Grid<Char>.getElves(): List<Point> = mapNotNull { (point, char) -> if (char == '#') point else null }

    private fun Grid<Char>.moveElves(directions: List<Directions>): Boolean {
        val moves = getProposals(directions)
        for ((current, next) in moves) {
            this[next] = '#'
            this[current] = '.'
        }
        return moves.isNotEmpty()
    }

    private fun Grid<Char>.getProposals(directions: List<Directions>): List<Pair<Point, Point>> {
        return getElves()
            .mapNotNull { elf ->
                val clears = directions.map { it.isClear(this, elf) }
                if (clears.all { it }) {
                    null
                } else {
                    val directionIndex = clears.indexOfFirst { it }
                    if (directionIndex >= 0) {
                        elf to directions[directionIndex].step(elf)
                    } else null
                }
            }
            .removeCollisions()
    }

    private fun List<Pair<Point, Point>>.removeCollisions(): List<Pair<Point, Point>> {
        return filter { (_, proposal) ->
            count { it.second == proposal } == 1
        }
    }

    private fun MutableList<Directions>.rotate() {
        this += removeFirst()
    }

    private fun Grid<Char>.groundCount(): Int {
        val elves = getElves()
        var minX = Int.MAX_VALUE
        var minY = Int.MAX_VALUE
        var maxX = Int.MIN_VALUE
        var maxY = Int.MIN_VALUE
        for ((x, y) in elves) {
            minX = min(x, minX)
            minY = min(y, minY)
            maxX = max(x, maxX)
            maxY = max(y, maxY)
        }
        var groundCount = 0
        for (y in minY..maxY) {
            for (x in minX..maxX) {
                val c = this[x to y]
                if (c == '.' || c == null) groundCount++
            }
        }

        return groundCount
    }

    private enum class Directions(
        val isClear: Grid<Char>.(Point) -> Boolean,
        val step: (Point) -> Point,
    ) {
        NORTH(
            isClear = { isNorthClear(it) },
            step = { it.up },
        ),
        SOUTH(
            isClear = { isSouthClear(it) },
            step = { it.down },
        ),
        WEST(
            isClear = { isWestClear(it) },
            step = { it.left },
        ),
        EAST(
            isClear = { isEastClear(it) },
            step = { it.right },
        ),
        ;
    }

    private fun Grid<Char>.isPointClear(point: Point): Boolean = this[point] != '#'

    private fun Grid<Char>.isNorthClear(point: Point): Boolean {
        return isPointClear(point.upLeft) && isPointClear(point.up) && isPointClear(point.upRight)
    }

    private fun Grid<Char>.isSouthClear(point: Point): Boolean {
        return isPointClear(point.downLeft) && isPointClear(point.down) && isPointClear(point.downRight)
    }

    private fun Grid<Char>.isWestClear(point: Point): Boolean {
        return isPointClear(point.downLeft) && isPointClear(point.left) && isPointClear(point.upLeft)
    }

    private fun Grid<Char>.isEastClear(point: Point): Boolean {
        return isPointClear(point.downRight) && isPointClear(point.right) && isPointClear(point.upRight)
    }
}
