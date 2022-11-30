package co.danhill.aoc.year2022

import co.danhill.aoc.util.Day

fun main() {
    Day01.run("2022/01.txt")
}

object Day01 : Day<List<Int>>() {

    override fun parseInput(input: Sequence<String>): List<Int> {
        return input.map { it.toInt() }.toList()
    }

    override fun part1(input: List<Int>): String {
        TODO("Not yet implemented")
    }

    override fun part2(input: List<Int>): String {
        TODO("Not yet implemented")
    }
}
