package co.danhill.aoc.year2022

import co.danhill.aoc.util.Day
import co.danhill.aoc.util.Input
import co.danhill.aoc.util.lines

fun main() = Day03.run("2022/03.txt")

typealias Rucksack = String

object Day03 : Day<List<Rucksack>>() {

    override fun parseInput(input: Input): List<Rucksack> {
        return input.lines
    }

    override fun part1(input: List<Rucksack>): Any {
        return input.sumOf { rucksack ->
            rucksack.compartmentOne
                .first { rucksack.compartmentTwo.contains(it) }
                .priority
        }
    }

    override fun part2(input: List<Rucksack>): Any {
        return input.chunked(3)
            .sumOf { rucksacks ->
                rucksacks.first()
                    .first { item ->
                        rucksacks.takeLast(2).all { it.contains(item) }
                    }
                    .priority
            }
    }

    private val Rucksack.compartmentOne: String
        get() = this.substring(0, this.length/2)

    private val Rucksack.compartmentTwo: String
        get() = this.substring(this.length/2)

    private const val priorityString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"

    private val Char.priority: Int
        get() = priorityString.indexOf(this) + 1
}
