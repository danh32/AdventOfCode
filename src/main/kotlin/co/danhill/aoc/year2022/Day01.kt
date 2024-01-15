package co.danhill.aoc.year2022

import co.danhill.aoc.util.Day
import co.danhill.aoc.util.Input

fun main() = Day01.run()

object Day01 : Day(2022, 1) {

    private fun Input.parse(): List<Int> {
        return groupedText.map { elfHoldings ->
            elfHoldings.split("\n")
                .sumOf { it.toInt() }
        }
    }

    // 70720
    override fun part1(input: Input): Any {
        return input.parse().maxOf { it }
    }

    // 207148
    override fun part2(input: Input): Any {
        return input
            .parse()
            .sortedDescending()
            .take(3)
            .sum()
    }
}
