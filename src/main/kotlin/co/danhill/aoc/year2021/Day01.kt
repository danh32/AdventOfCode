package co.danhill.aoc.year2021

import co.danhill.aoc.util.Day

fun main() {
    Day01.run("2021/01.txt")
}

object Day01 : Day<List<Int>>() {

    override fun parseInput(input: Sequence<String>): List<Int> {
        return input.map { it.toInt() }.toList()
    }

    override fun part1(input: List<Int>): String {
        return input
            .fold(0 to Int.MAX_VALUE) { (count, previous), current ->
                val newCount = if (current > previous) count + 1 else count
                newCount to current
            }.first
            .toString()
    }

    override fun part2(input: List<Int>): String {
        val windowSize = 3
        val depths = input.toList()
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
