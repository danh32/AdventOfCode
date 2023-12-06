package co.danhill.aoc.year2023

import co.danhill.aoc.util.Day
import co.danhill.aoc.util.Input
import co.danhill.aoc.util.lines

fun main() = Day05.run("2023/05.txt")

object Day05 : Day {
    override fun part1(input: Input): Any {
        return input.lines.count { it.isNice() }
    }

    override fun part2(input: Input): Any {
        return ""
    }

    private fun String.isNice() = vowelsCount > 2 && containsDoubleLetter && noForbiddenClusters

    private val vowels = setOf('a', 'e', 'i', 'o', 'u')
    private val String.vowelsCount: Int
        get() = count { c -> vowels.contains(c) }

    private val String.containsDoubleLetter: Boolean
        get() = windowed(2, 1)
            .any { chunk -> chunk[0] == chunk[1] }

    private val forbiddenClusters = setOf("ab", "cd", "pq", "xy")
    private val String.noForbiddenClusters: Boolean
        get() = windowed(2, 1)
            .none { chunk -> forbiddenClusters.contains(chunk) }
}
