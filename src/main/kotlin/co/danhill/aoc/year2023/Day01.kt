package co.danhill.aoc.year2023

import co.danhill.aoc.util.Day
import co.danhill.aoc.util.Input

fun main() = Day01.run()

object Day01 : Day(2023, 1) {
    override fun part1(input: Input): Any {
        return input.lines.sumOf { line ->
            val first = line.first { it.isDigit() }
            val last = line.last { it.isDigit() }
            "$first$last".toInt()
        }
    }

    override fun part2(input: Input): Any {
        return input.lines.sumOf { line ->
            val first = line.getFirstDigit(indices = line.indices)
            val last = line.getFirstDigit(indices = line.indices.reversed())
            "$first$last".toInt()
        }
    }

    private fun String.getFirstDigit(indices: IntProgression): Int? {
        for (i in indices) {
            if (this[i].isDigit()) return this[i].digitToInt()
            else spelledDigitAt(i)?.let { return it }
        }
        return null
    }

    private val digitPairs = mapOf(
        "one" to 1,
        "two" to 2,
        "three" to 3,
        "four" to 4,
        "five" to 5,
        "six" to 6,
        "seven" to 7,
        "eight" to 8,
        "nine" to 9,
        "zero" to 0,
    )

    private fun String.spelledDigitAt(index: Int): Int? {
        for ((spelled, digit) in digitPairs) {
            if (containsAt(spelled, index)) return digit
        }
        return null
    }

    private fun String.containsAt(other: String, index: Int): Boolean {
        var i = index
        for (c in other) {
            val char = this.getOrNull(i) ?: return false
            if (char == c) {
                i++
                continue
            } else {
                return false
            }
        }
        return true
    }
}