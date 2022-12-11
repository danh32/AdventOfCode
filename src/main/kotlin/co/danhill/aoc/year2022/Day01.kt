package co.danhill.aoc.year2022

import co.danhill.aoc.util.Day
import co.danhill.aoc.util.Input
import co.danhill.aoc.util.groupedText

fun main() = Day01.run("2022/01.txt")

object Day01 : Day {

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
