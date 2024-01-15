package co.danhill.aoc.year2022

import co.danhill.aoc.util.*

fun main() = Day08.run()

object Day08 : Day(2022, 8) {
    private fun Input.parse(): Grid<Int> = lines.toIntGrid()

    override fun part1(input: Input): Any {
        val parsed = input.parse()
        return parsed.keys.count { parsed.isTreeVisibleFromOutside(it) }
    }

    override fun part2(input: Input): Any {
        val parsed = input.parse()
        return parsed.keys.maxOf { point ->
            parsed.scenicScore(point)
        }
    }

    private fun Grid<Int>.isTreeVisibleFromOutside(point: Point): Boolean {
        val height = this.getValue(point)
        fun Sequence<Int>.isVisibleFromOutside(): Boolean = drop(1).all { it < height }

        if (sequenceToLeftEdge(point).isVisibleFromOutside()) return true
        if (sequenceToTopEdge(point).isVisibleFromOutside()) return true
        if (sequenceToRightEdge(point).isVisibleFromOutside()) return true
        return sequenceToBottomEdge(point).isVisibleFromOutside()
    }

    private fun Grid<Int>.scenicScore(point: Point): Int {
        val height = getValue(point)
        fun Sequence<Int>.viewingDistance(): Int {
            var count = 0
            for (currentHeight in this.drop(1)) {
                count++
                if (currentHeight >= height) return count
            }
            return count
        }
        val leftViewingDistance = sequenceToLeftEdge(point).viewingDistance()
        val topViewingDistance = sequenceToTopEdge(point).viewingDistance()
        val rightViewingDistance = sequenceToRightEdge(point).viewingDistance()
        val bottomViewingDistance = sequenceToBottomEdge(point).viewingDistance()
        return leftViewingDistance * topViewingDistance * rightViewingDistance * bottomViewingDistance
    }
}
