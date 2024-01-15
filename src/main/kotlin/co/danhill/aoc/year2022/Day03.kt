package co.danhill.aoc.year2022

import co.danhill.aoc.util.Day
import co.danhill.aoc.util.Input

fun main() = Day03.run()

typealias Rucksack = String

object Day03 : Day(2022, 3) {

    override fun part1(input: Input): Any {
        return input.lines.sumOf { rucksack ->
            rucksack.compartmentOne
                .first { rucksack.compartmentTwo.contains(it) }
                .priority
        }
    }

    override fun part2(input: Input): Any {
        return input.lines.chunked(3)
            .sumOf { rucksacks ->
                rucksacks.first()
                    .first { item ->
                        rucksacks.subList(1, rucksacks.size).all { it.contains(item) }
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
