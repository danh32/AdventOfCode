package co.danhill.aoc.year2022

import co.danhill.aoc.util.*

fun main() = Day09.run("2022/09.txt")

object Day09 : Day<List<Pair<Char, Int>>>() {

    override fun parseInput(input: Input): List<Pair<Char, Int>> {
        return input.lines.map { line ->
            val (a, b) = line.split(' ')
            a.single() to b.toInt()
        }
    }

    override fun part1(input: List<Pair<Char, Int>>): Any {
        val knots = MutableList(2) { 0 to 0 }
        return runSimulation(input, knots)
    }

    override fun part2(input: List<Pair<Char, Int>>): Any {
        val knots = MutableList(10) { 0 to 0 }
        return runSimulation(input, knots)
    }

    private fun runSimulation(input: List<Pair<Char, Int>>, knots: MutableList<Point>): Int {
        val tailVisited = mutableSetOf(knots.last())

        for ((direction, count) in input) {
            repeat(count) {
                for (i in 0 until knots.size) {
                    val knot = knots[i]
                    knots[i] = if (i == 0) {
                        knot.move(direction)
                    } else {
                        knot.followTarget(knots[i - 1])
                    }
                }
                tailVisited += knots.last()
            }
        }

        return tailVisited.size
    }

    private fun Point.move(direction: Char) = when (direction) {
        'L' -> left
        'U' -> up
        'R' -> right
        'D' -> down
        else -> error("Unknown direction $direction")
    }

    private fun Point.followTarget(target: Point): Point {
        val (targetX, targetY) = target
        return when {
            neighbors.contains(target) -> this
            x == targetX && y == targetY -> this
            x == targetX && y < targetY -> down
            x == targetX && y > targetY -> up
            y == targetY && x < targetX -> right
            y == targetY && x > targetX -> left
            x < targetX && y < targetY -> downRight
            x < targetX && y > targetY -> upRight
            x > targetX && y < targetY -> downLeft
            x > targetX && y > targetY -> upLeft
            else -> error("$this -> $target = ???")
        }
    }
}
