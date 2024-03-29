package co.danhill.aoc.util

import kotlin.math.max
import kotlin.math.min

interface Grid<T> : MutableMap<Point, T> {
    val minX: Int
    val maxX: Int
    val minY: Int
    val maxY: Int

    val xRange: IntRange
        get() = minX..maxX

    val yRange: IntRange
        get() = minY..maxY

    fun print(
        xRange: IntRange = minX..maxX,
        yRange: IntRange = minY..maxY,
        toString: (T?) -> String = { it.toString() },
    )

    fun Grid<Int>.sequenceToLeftEdge(start: Point): Sequence<Int> = sequenceBetween(start, 0 to start.y)
    fun Grid<Int>.sequenceToTopEdge(start: Point): Sequence<Int> = sequenceBetween(start, start.x to 0)
    fun Grid<Int>.sequenceToRightEdge(start: Point): Sequence<Int> = sequenceBetween(start, maxX to start.y)
    fun Grid<Int>.sequenceToBottomEdge(start: Point): Sequence<Int> = sequenceBetween(start, start.x to maxY)

    fun Grid<Int>.sequenceBetween(start: Point, end: Point): Sequence<Int> {
        val (startX, startY) = start
        val (endX, endY) = end
        if (startX == endX && startY == endY)
            return sequenceOf(this.getValue(start))

        return sequence {
            when {
                startX == endX -> {
                    val range = if (startY < endY) startY..endY else startY.downTo(endY)
                    for (y in range) {
                        yield(this@sequenceBetween.getValue(startX to y))
                    }
                }

                startY == endY -> {
                    val range = if (startX < endX) startX..endX else startX.downTo(endX)
                    for (x in range) {
                        yield(this@sequenceBetween.getValue(x to startY))
                    }
                }

                else -> error("$start and $end must share a row or column")
            }
        }
    }

    fun contains(point: Point) = point.x in minX..maxX && point.y in minY..maxY
}

private class MapGrid<T>(
    private val defaultValue: ((Point) -> T)? = null,
    private val data: MutableMap<Point, T> = mutableMapOf<Point, T>().let {
        if (defaultValue != null) it.withDefault(defaultValue)
        else it
    }
) : Grid<T>, MutableMap<Point, T> by data {

    override var xRange = 0..0
    override var yRange = 0..0

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
                print(toString(data[x to y]))
            }
            println()
        }
    }
}

fun <T> gridOf(defaultValue: ((Point) -> T)? = null): Grid<T> = MapGrid(defaultValue)
fun <T> gridOf(data: MutableMap<Point, T>): Grid<T> = MapGrid(data = data)

fun Collection<String>.toIntGrid(): Grid<Int> {
    val grid = gridOf<Int>()
    forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            grid[x to y] = c.digitToInt()
        }
    }
    return grid
}

fun Collection<String>.toCharGrid(): Grid<Char> {
    val grid = gridOf<Char>()
    forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            grid[x to y] = c
        }
    }
    return grid
}

fun <T> Grid<T>.findPath(
    start: Point,
    end: Point,
    movementCost: (from: Point, to: Point) -> Int,
    heuristic: (from: Point, to: Point) -> Int = { a, b -> a.manhattanDistanceTo(b) },
    generateNextStates: (Point, Sequence<Point>) -> List<Point> = { point, _ ->
        point.cardinalNeighbors
            .filter { neighbor -> containsKey(neighbor) }
    },
): List<Point> = Search.aStar(
    start = start,
    isEnd = { it == end },
    generateNextStates = generateNextStates,
    movementCost = movementCost,
    heuristicCostToEndState = { from -> heuristic(from, end) }
)
