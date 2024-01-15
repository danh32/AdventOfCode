package co.danhill.aoc.year2021

import co.danhill.aoc.util.Day
import co.danhill.aoc.util.Input

fun main() {
    Day03.run()
}

object Day03 : Day(2021, 3) {

    override fun part1(input: Input): String {
        val lines = input.lines
        val gamma = lines.first().indices
            .map { i -> lines.findMostCommonAtIndex(i, tieBreaker = '1') }
            .joinToString(separator = "")
            .toInt(2)

        val epsilon = lines.first().indices
            .map { i -> lines.findLeastCommonAtIndex(i, tieBreaker = '1') }
            .joinToString(separator = "")
            .toInt(2)

        return "$gamma * $epsilon = ${gamma * epsilon}"
    }

    override fun part2(input: Input): String {
        val lines = input.lines
        val oxygenBinary = search(lines, 0, this::oxygenBitCriteriaSelector)
        val oxygen = Integer.parseInt(oxygenBinary, 2)
        val co2Binary = search(lines, 0, this::co2BitCriteriaSelector)
        val co2 = Integer.parseInt(co2Binary, 2)
        return "$oxygen * $co2 = ${oxygen * co2}"
    }

    private fun oxygenBitCriteriaSelector(input: List<String>, index: Int): Char {
        return input.findMostCommonAtIndex(index, tieBreaker = '1')
    }

    private fun co2BitCriteriaSelector(input: List<String>, index: Int): Char {
        return input.findLeastCommonAtIndex(index, tieBreaker = '1')
    }

    private fun search(
        input: List<String>,
        index: Int,
        bitCriteriaSelector: (List<String>, Int) -> Char,
    ): String {
        if (input.size == 1) {
            return input.single()
        }
        val bitCriteria = bitCriteriaSelector(input, index)
        val filtered = input.filter { it[index] == bitCriteria }
        val newIndex = index + 1
        return search(filtered, newIndex, bitCriteriaSelector)
    }

    private fun List<String>.findMostCommonAtIndex(index: Int, tieBreaker: Char): Char {
        val sum = sumOf { it[index].digitToInt() }
        val half = size / 2
        return when {
            sum > half -> '1'
            sum < half -> '0'
            sum == half && size % 2 == 1 -> '0'
            sum == half && size % 2 == 0 -> tieBreaker
            else -> error("wtf")
        }
    }

    private fun List<String>.findLeastCommonAtIndex(index: Int, tieBreaker: Char): Char {
        return when (findMostCommonAtIndex(index, tieBreaker)) {
            '0' -> '1'
            '1' -> '0'
            else -> error("wtf")
        }
    }
}