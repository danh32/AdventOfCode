package co.danhill.aoc.year2022

import co.danhill.aoc.util.*

fun main() = Day08.run("2022/08.txt")

object Day08 : Day<Grid<Int>>() {
    override fun parseInput(input: Input): Grid<Int> {
        return input.lines
            .toIntGrid()
    }

    override fun part1(input: Grid<Int>): Any {
        return input.keys.count { input.isTreeVisibleFromOutside(it) }
    }

    override fun part2(input: Grid<Int>): Any {
        return input.keys.maxOf { point ->
            input.scenicScore(point)
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
