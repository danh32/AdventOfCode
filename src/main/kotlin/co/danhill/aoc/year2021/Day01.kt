package co.danhill.aoc.year2021

import co.danhill.aoc.util.Day
import co.danhill.aoc.util.Input

fun main() {
    Day01.run()
}

object Day01 : Day(2021, 1) {

    private fun Input.parse(): List<Int> = lines.map { it.toInt() }

    override fun part1(input: Input): String {
        return input
            .parse()
            .fold(0 to Int.MAX_VALUE) { (count, previous), current ->
                val newCount = if (current > previous) count + 1 else count
                newCount to current
            }.first
            .toString()
    }

    override fun part2(input: Input): String {
        val windowSize = 3
        val depths = input.parse().toList()
        val windowSums = (0..depths.size - windowSize).map { i ->
            (i until i + windowSize).sumOf { j -> depths[j] }
        }
        return windowSums
            .fold(0 to Int.MAX_VALUE) { (count, previous), current ->
                val newCount = if (current > previous) count + 1 else count
                newCount to current
            }.first
            .toString()
    }
}
