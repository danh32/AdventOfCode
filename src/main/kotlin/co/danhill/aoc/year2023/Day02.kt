package co.danhill.aoc.year2023

import co.danhill.aoc.util.Day
import co.danhill.aoc.util.Input
import kotlin.math.max

fun main() = Day02.run()

object Day02 : Day(2023, 2) {
    override fun part1(input: Input): Any {
        val limits = mapOf(
            "red" to 12,
            "green" to 13,
            "blue" to 14,
        )
        return input.lines.sumOf { line ->
            val (game, rounds) = line.split(": ")
            val gameId = game.split(" ")[1]
            val map = rounds.split("; ")
                .fold(mutableMapOf<String, Int>()) { map, round ->
                    val draws = round.split(", ")
                    for (draw in draws) {
                        val (amount, color) = draw.split(" ")
                        map.compute(color) { _, amt ->
                            max(amt ?: 0, amount.toInt())
                        }
                    }
                    map
                }

            val passes = map.entries.all { (color, amount) ->
                amount <= limits.getValue(color)
            }
            if (passes) gameId.toInt() else 0
        }
    }

    override fun part2(input: Input): Any {
        return input.lines.sumOf { line ->
            val (_, rounds) = line.split(": ")
            val map = rounds.split("; ")
                .fold(mutableMapOf<String, Int>()) { map, round ->
                    val draws = round.split(", ")
                    for (draw in draws) {
                        val (amount, color) = draw.split(" ")
                        map.compute(color) { _, amt ->
                            max(amt ?: 0, amount.toInt())
                        }
                    }
                    map
                }

            val power = map.values.fold(1) { power, value -> power * value }
            power
        }
    }
}
