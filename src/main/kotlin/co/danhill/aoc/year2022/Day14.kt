package co.danhill.aoc.year2022

import co.danhill.aoc.util.*

fun main() = Day14.run()

object Day14 : Day(2022, 14) {
    private val sandOrigin = 500 to 0

    private fun Input.parse() = lines
        .fold(gridOf(mutableMapOf<Point, CavePoint>().withDefault { CavePoint.AIR })) { grid, line ->
            line.split(" -> ")
                .map { rockLine ->
                    val (x, y) = rockLine.split(',')
                    x.toInt() to y.toInt()
                }
                .windowed(2, 1, false)
                .forEach { (from, to) ->
                    for (x in minOf(from.x, to.x)..maxOf(from.x, to.x)) {
                        for (y in minOf(from.y, to.y)..maxOf(from.y, to.y)) {
                            grid[x to y] = CavePoint.ROCK
                        }
                    }
                }
            grid
        }.apply { this[sandOrigin] = CavePoint.SAND_ORIGIN }

    override fun part1(input: Input): Any {
        val cave = input.parse()
        while (cave.generateSand(sandOrigin)) {
//            cave.print()
        }
        return cave.sandCount()
    }

    override fun part2(input: Input): Any {
        val cave = input.parse()
        cave.addFloor()
        while (cave.generateSand(sandOrigin)) {
//            cave.print()
        }
        return cave.sandCount()
    }

    private enum class CavePoint(val isSolid: Boolean, val display: String) {
        SAND_ORIGIN(false, "+"),
        SAND(true, "o"),
        ROCK(true, "#"),
        AIR(false, "."),
        ;

        override fun toString(): String = display
    }

    private fun Grid<CavePoint>.sandCount(): Int = values.count { it == CavePoint.SAND }

    private fun Grid<CavePoint>.generateSand(origin: Point): Boolean {
        var sand = origin
        fun Point.canMoveDown(): Boolean = !(this@generateSand[this.down] ?: CavePoint.AIR).isSolid
        fun Point.canMoveDownLeft(): Boolean = !(this@generateSand[this.downLeft] ?: CavePoint.AIR).isSolid
        fun Point.canMoveDownRight(): Boolean = !(this@generateSand[this.downRight] ?: CavePoint.AIR).isSolid
        while (true) {
            sand = when {
                sand.canMoveDown() -> sand.down
                sand.canMoveDownLeft() -> sand.downLeft
                sand.canMoveDownRight() -> sand.downRight
                else -> break
            }

            if (sand.x !in minX..maxX || sand.y !in minY..maxY) {
                // shape is now saturated, and we're flowing out the bottom of the cave
                return false
            }
        }
        this[sand] = CavePoint.SAND
        return sand != origin
    }

    private fun Grid<CavePoint>.addFloor() {
        val floorY = maxY + 2
        for (x in 0..1000) this[x to floorY] = CavePoint.ROCK
    }
}
