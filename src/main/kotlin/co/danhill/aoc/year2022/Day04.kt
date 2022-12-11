package co.danhill.aoc.year2022

import co.danhill.aoc.util.Day
import co.danhill.aoc.util.Input
import co.danhill.aoc.util.lines

fun main() = Day04.run("2022/04.txt")

private typealias Assignments = Pair<IntRange, IntRange>

object Day04 : Day {

    private fun Input.parse(): List<Assignments> {
        return lines.map { line ->
            val (e1, e2) = line.split(',').map { assignment ->
                val (start, end) = assignment.split('-').map { it.toInt() }
                start..end
            }
            e1 to e2
        }
    }

    override fun part1(input: Input): Any {
        return input.parse().count { (a, b) ->
            a in b || b in a
        }
    }

    override fun part2(input: Input): Any {
        return input.parse().count { (a, b) -> a.overlaps(b) }
    }

    private operator fun IntRange.contains(other: IntRange): Boolean {
        return other.first in this && other.last in this
    }

    private fun IntRange.overlaps(other: IntRange): Boolean {
        return other.first in this || other.last in this || first in other || last in other
    }
}
