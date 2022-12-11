package co.danhill.aoc.year2021

import co.danhill.aoc.util.Day
import co.danhill.aoc.util.Input
import co.danhill.aoc.util.lines
import kotlin.math.absoluteValue

fun main() {
    Day07.run("2021/07.txt")
}

object Day07 : Day {

    private fun Input.parse(): List<Int> {
        return lines.first().split(',').map { it.toInt() }.sorted()
    }

    override fun part1(input: Input): String {
        val parsed = input.parse()
        val middleIndex = parsed.size / 2
        val median = parsed[middleIndex - 1]
        val fuel = parsed.sumOf { (median - it).absoluteValue }
        return "$fuel"
    }

    override fun part2(input: Input): String {
        val parsed = input.parse()
        val min = parsed.minByOrNull { it }!!
        val max = parsed.maxByOrNull { it }!!
        val winningTarget = (min..max).minByOrNull { findScore(it, parsed) }!!
        val score = findScore(winningTarget, parsed)
        return "$score"
    }

    private fun findScore(target: Int, numbers: List<Int>): Int {
        return numbers.sumOf { number -> singleScore(number, target).absoluteValue }
    }

    private fun singleScore(x: Int, y: Int): Int {
        val d = (x - y).absoluteValue
        return (d * (d + 1)) / 2
    }
}