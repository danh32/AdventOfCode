package co.danhill.aoc.year2022

import co.danhill.aoc.util.Day
import co.danhill.aoc.util.Input
import co.danhill.aoc.util.groupedText

fun main() = Day01.run("2022/01.txt")

object Day01 : Day<List<Int>>() {

    override fun parseInput(input: Input): List<Int> {
        return input
            .groupedText
            .map { elfHoldings ->
                elfHoldings.split("\n")
                    .sumOf { it.toInt() }
            }
    }

    // 70720
    override fun part1(input: List<Int>): Any {
        return input.maxOf { it }
    }

    // 207148
    override fun part2(input: List<Int>): Any {
        return input
            .sortedDescending()
            .take(3)
            .sum()
    }
}
