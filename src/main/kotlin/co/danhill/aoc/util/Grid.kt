package co.danhill.aoc.util

import kotlin.math.max
import kotlin.math.min

interface Grid<T> : MutableMap<Point, T> {
    val minX: Int
    val maxX: Int
    val minY: Int
    val maxY: Int
    fun print(
        xRange: IntRange = minX..maxX,
        yRange: IntRange = minY..maxY,
        toString: (T?) -> String = { it.toString() },
    )
}

private class MapGrid<T>(
    private val data: MutableMap<Point, T> = mutableMapOf()
) : Grid<T>, MutableMap<Point, T> by data {

    private var xRange = 0..0
    private var yRange = 0..0

    override val minX: Int
        get() = xRange.first

    override val maxX: Int
        get() = xRange.last

    override val minY: Int
        get() = yRange.first

    override val maxY: Int
        get() = yRange.last

    override fun put(key: Point, value: T): T? {
        updateRanges(key)
        return data.put(key, value)
    }

    private fun updateRanges(point: Point) {
        val (x, y) = point
        if (x !in xRange) {
            xRange = min(minX, x)..max(maxX, x)
        }
        if (y !in yRange) {
            yRange = min(minY, y)..max(maxY, y)
        }
    }

    override fun print(
        xRange: IntRange,
        yRange: IntRange,
        toString: (T?) -> String,
    ) {
        for (y in yRange) {
            for (x in xRange) {
                print(toString(get(x to y)))
            }
            println()
        }
    }
}

fun <T> gridOf(): Grid<T> = MapGrid()

fun Sequence<String>.toIntGrid(): Grid<Int> {
    val grid = gridOf<Int>()
    forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            grid[x to y] = c.digitToInt()
        }
    }
    return grid
}
