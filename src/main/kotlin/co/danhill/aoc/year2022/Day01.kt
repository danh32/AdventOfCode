package co.danhill.aoc.year2022

import co.danhill.aoc.util.Day

fun main() {
    Day01.run("2022/01.txt")
}

object Day01 : Day<List<Int>>() {

    override fun parseInput(input: Sequence<String>): List<Int> {
        return input
            .map { it.toIntOrNull() }
            .fold(mutableListOf(0)) { sumsList, nextInt ->
                if (nextInt == null) {
                    sumsList += 0
                } else {
                    sumsList[sumsList.lastIndex] += nextInt
                }
                sumsList
            }
    }

    // 70720
    override fun part1(input: List<Int>): Any {
        return input.maxOf { it }
    }

    // 207148
    override fun part2(input: List<Int>): Any {
        return input
            .sortedDescending()
            .take(3)
            .sum()
    }
}
