package co.danhill.aoc.year2021

import co.danhill.aoc.util.Day
import java.math.BigDecimal

fun main() {
    Day06.run("2021/06.txt")
}

object Day06 : Day<MutableList<BigDecimal>>() {

    override fun parseInput(input: Sequence<String>): MutableList<BigDecimal> {
        val days = mutableListOf(
            BigDecimal.ZERO,
            BigDecimal.ZERO,
            BigDecimal.ZERO,
            BigDecimal.ZERO,
            BigDecimal.ZERO,
            BigDecimal.ZERO,
            BigDecimal.ZERO,
            BigDecimal.ZERO,
            BigDecimal.ZERO,
            BigDecimal.ZERO,
        )

        input.first().split(',')
            .map { it.toInt() }
            .forEach { day ->
                days[day]++
            }

        return days
    }

    override fun part1(input: MutableList<BigDecimal>): String {
        return runSimulation(input, 80)
    }

    override fun part2(input: MutableList<BigDecimal>): String {
        return runSimulation(input, 256)
    }

    private fun runSimulation(days: MutableList<BigDecimal>, numDays: Int): String {
        for (i in 0 until numDays) {
            val newFish = days[0]
            days[0] = days[1]
            days[1] = days[2]
            days[2] = days[3]
            days[3] = days[4]
            days[4] = days[5]
            days[5] = days[6]
            days[6] = days[7] + newFish
            days[7] = days[8]
            days[8] = days[9] + newFish
        }
        var sum = BigDecimal.ZERO
        days.forEach { sum += it }
        return "$sum"
    }
}