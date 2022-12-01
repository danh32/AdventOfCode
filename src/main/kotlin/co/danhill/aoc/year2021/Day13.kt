package co.danhill.aoc.year2021

import co.danhill.aoc.util.*

fun main() {
    Day13.run("2021/13.txt")
}

object Day13 : Day<Pair<MutableSet<Point>, List<Day13.Fold>>>() {

    override fun parseInput(input: Input): Pair<MutableSet<Point>, List<Fold>> {
        val list = input.lines
        val emptyIndex = list.indexOf("")
        val points = list.subList(0, emptyIndex)
            .map { line ->
                val (x,y) = line.split(',')
                x.toInt() to y.toInt()
            }
            .toMutableSet()

        val folds = list.subList(emptyIndex + 1, list.size)
            .map { line ->
                val (axis, value) = line.removePrefix("fold along ").split('=')
                Fold(FoldAxis.fromString(axis), value.toInt())
            }

        return points to folds
    }

    override fun part1(input: Pair<MutableSet<Point>, List<Fold>>): String {
        val (points, folds) = input
        val firstFold = folds.first()
        processFold(points, firstFold)
        return "${points.size}"
    }

    override fun part2(input: Pair<MutableSet<Point>, List<Fold>>): String {
        val (points, folds) = input

        folds.forEach { processFold(points, it) }

        val maxX = points.maxOf { it.x }
        val maxY = points.maxOf { it.y }
        for (y in 0..maxY) {
            for (x in 0..maxX) {
                print(
                    if (points.contains(x to y)) '#'
                    else '.'
                )
            }
            println()
        }
        return "${points.size}"
    }

    enum class FoldAxis {
        X, Y;

        companion object {
            fun fromString(value: String): FoldAxis = when (value) {
                "x" -> X
                "y" -> Y
                else -> error("Unknown axis: $value")
            }
        }
    }

    data class Fold(
        val axis: FoldAxis,
        val line: Int,
    )

    private fun processFold(points: MutableSet<Point>, fold: Fold) {
        when (fold.axis) {
            FoldAxis.Y -> {
                val foldY = fold.line
                points.filter { it.y > foldY }
                    .forEach { (x, y) ->
                        points.remove(x to y)
                        points.add(x to (foldY - (y - foldY)))
                    }
            }
            FoldAxis.X -> {
                val foldX = fold.line
                points.filter { it.x > foldX }
                    .forEach { (x, y) ->
                        points.remove(x to y)
                        points.add((foldX - (x - foldX)) to y)
                    }
            }
        }
    }
}