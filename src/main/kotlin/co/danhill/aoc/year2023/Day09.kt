package co.danhill.aoc.year2023

import co.danhill.aoc.util.Day
import co.danhill.aoc.util.Input
import co.danhill.aoc.util.lines

fun main() = Day09.run("2023/09.txt")

object Day09 : Day {
    override fun part1(input: Input): Any {
        val histories = input.lines.map { line ->
            line.split(' ')
                .map { it.toInt() }
        }
        return histories.sumOf { history ->
            val pyramid = mutableListOf<MutableList<Int>>()
            var currentSequence = history.toMutableList()
            while (!currentSequence.all { it == 0 }) {
                pyramid += currentSequence
                currentSequence = currentSequence.windowed(2, 1)
                    .map { (a, b) -> b - a }
                    .toMutableList()
            }
            pyramid += currentSequence

            var pyramidIndex = pyramid.size - 1
            while (pyramidIndex >= 0) {
                val sequence = pyramid[pyramidIndex]
                val toAdd = pyramid.getOrNull(pyramidIndex + 1)?.last() ?: 0
                sequence += sequence.last() + toAdd
                pyramidIndex--
            }

            pyramid.first().last()
        }
    }

    override fun part2(input: Input): Any {
        val histories = input.lines.map { line ->
            line.split(' ')
                .map { it.toInt() }
        }
        return histories.sumOf { history ->
            val pyramid = mutableListOf<MutableList<Int>>()
            var currentSequence = history.toMutableList()
            while (!currentSequence.all { it == 0 }) {
                pyramid += currentSequence
                currentSequence = currentSequence.windowed(2, 1)
                    .map { (a, b) -> b - a }
                    .toMutableList()
            }
            pyramid += currentSequence

            var pyramidIndex = pyramid.size - 1
            while (pyramidIndex >= 0) {
                val sequence = pyramid[pyramidIndex]
                val toSubtract = pyramid.getOrNull(pyramidIndex + 1)?.first() ?: 0
                sequence.add(0, sequence.first() - toSubtract)
                pyramidIndex--
            }

            pyramid.first().first()
        }
    }
}
