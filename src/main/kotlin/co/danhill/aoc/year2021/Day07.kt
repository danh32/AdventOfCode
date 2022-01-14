package co.danhill.aoc.year2021

import co.danhill.aoc.util.Day
import kotlin.math.absoluteValue

fun main() {
    Day07.run("2021/07.txt")
}

object Day07 : Day<List<Int>>() {

    override fun parseInput(input: Sequence<String>): List<Int> {
        return input.first().split(',').map { it.toInt() }.sorted()
    }

    override fun part1(input: List<Int>): String {
        val middleIndex = input.size / 2
        val median = input[middleIndex - 1]
        val fuel = input.sumOf { (median - it).absoluteValue }
        return "$fuel"
    }

    override fun part2(input: List<Int>): String {
        val min = input.minByOrNull { it }!!
        val max = input.maxByOrNull { it }!!
        val winningTarget = (min..max).minByOrNull { findScore(it, input) }!!
        val score = findScore(winningTarget, input)
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