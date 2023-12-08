package co.danhill.aoc.year2023

import co.danhill.aoc.util.Day
import co.danhill.aoc.util.Input
import co.danhill.aoc.util.lines

fun main() = Day06.run("2023/06.txt")

object Day06 : Day {
    // 281600
    override fun part1(input: Input): Any {
        val (timeLine, distanceLine) = input.lines
        val times = timeLine.getNumbers()
        val distances = distanceLine.getNumbers()
        val maxTime = times.max()
        val distancesTable = (0..maxTime).map { endTime ->
            (0..endTime).map { holdTime ->
                holdTime * (endTime - holdTime)
            }
        }
        return times.indices
            .map { i ->
                val time = times[i]
                val distance = distances[i]
                distancesTable[time].count { it > distance }
            }
            .fold(1) { acc, count -> acc * count }
    }

    override fun part2(input: Input): Any {
        return ""
    }

    private fun String.getNumbers() = split(' ')
        .mapNotNull { it.trim().toIntOrNull() }
}
