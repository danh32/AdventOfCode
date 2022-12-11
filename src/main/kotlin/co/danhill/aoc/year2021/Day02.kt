package co.danhill.aoc.year2021

import co.danhill.aoc.util.Day
import co.danhill.aoc.util.Input
import co.danhill.aoc.util.lines

fun main() {
    Day02.run("2021/02.txt")
}

object Day02 : Day {

    data class Step(
        val direction: Direction,
        val magnitude: Int,
    )

    enum class Direction {
        FORWARD,
        DOWN,
        UP,
    }

    private fun Input.parse(): List<Step> {
        return lines
            .map { line ->
                val (direction, magnitude) = line.split(' ')
                Step(
                    direction = Direction.valueOf(direction.uppercase()),
                    magnitude = magnitude.toInt(),
                )
            }
    }

    override fun part1(input: Input): String {
        var depth = 0
        var position = 0
        input.parse().forEach { (direction, magnitude) ->
            when (direction) {
                Direction.FORWARD -> position+= magnitude
                Direction.DOWN -> depth+= magnitude
                Direction.UP -> depth-= magnitude
            }
        }
        return (depth * position).toString()
    }

    override fun part2(input: Input): String {
        var depth = 0
        var position = 0
        var aim = 0
        input.parse().forEach { (direction, magnitude) ->
            when (direction) {
                Direction.DOWN -> aim+= magnitude
                Direction.UP -> aim-= magnitude
                Direction.FORWARD -> {
                    position+= magnitude
                    depth+= aim * magnitude
                }
            }
        }
        return (depth * position).toString()
    }
}