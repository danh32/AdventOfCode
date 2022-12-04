package co.danhill.aoc.year2022

import co.danhill.aoc.util.Day
import co.danhill.aoc.util.Input
import co.danhill.aoc.util.lines

fun main() = Day04.run("2022/04.txt")

private typealias Assignments = Pair<IntRange, IntRange>

object Day04 : Day<List<Assignments>>() {

    override fun parseInput(input: Input): List<Assignments> {
        return input.lines.map { line ->
            val (e1, e2) = line.split(',').map { range ->
                val (start, end) = range.split('-')
                    .map { it.toInt() }
                start..end
            }
            e1 to e2
        }
    }

    override fun part1(input: List<Assignments>): Any {
        return input.count { (a, b) ->
            a.contains(b) || b.contains(a)
        }
    }

    override fun part2(input: List<Assignments>): Any {
        return input.count { (a, b) -> a.overlaps(b) }
    }

    private fun IntRange.contains(other: IntRange): Boolean {
        return this.contains(other.first) && this.contains(other.last)
    }

    private fun IntRange.overlaps(other: IntRange): Boolean {
        return this.contains(other.first)
                || this.contains(other.last)
                || other.contains(this.first)
                || other.contains(this.last)
    }
}
