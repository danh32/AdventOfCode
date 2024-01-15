package co.danhill.aoc.year2021

import co.danhill.aoc.util.Day
import co.danhill.aoc.util.Input
import java.math.BigDecimal

fun main() {
    Day06.run()
}

object Day06 : Day(2021, 6) {

    private fun Input.parse(): MutableList<BigDecimal> {
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

        lines.first().split(',')
            .map { it.toInt() }
            .forEach { day ->
                days[day]++
            }

        return days
    }

    override fun part1(input: Input): String {
        return runSimulation(input.parse(), 80)
    }

    override fun part2(input: Input): String {
        return runSimulation(input.parse(), 256)
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