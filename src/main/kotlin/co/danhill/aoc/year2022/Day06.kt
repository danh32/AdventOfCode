package co.danhill.aoc.year2022

import co.danhill.aoc.util.Day
import co.danhill.aoc.util.Input
import co.danhill.aoc.util.text

fun main() = Day06.run("2022/06.txt")

object Day06 : Day {
    override fun part1(input: Input): Any {
        return input.text.startOfPacketMarkerIndex
    }

    override fun part2(input: Input): Any {
        return input.text.startOfMessageMarkerIndex
    }

    private val String.startOfPacketMarkerIndex: Int
        get() = findRunOfUniqueChars(4)

    private val String.startOfMessageMarkerIndex: Int
        get() = findRunOfUniqueChars(14)

    private fun String.findRunOfUniqueChars(count: Int): Int {
        return withIndex()
            .windowed(count)
            .first { indexedChars ->
                indexedChars.distinctBy { it.value }.size == count
            }
            .last()
            .index + 1
    }
}
