package co.danhill.aoc.year2022

import co.danhill.aoc.util.Day
import co.danhill.aoc.util.Input
import co.danhill.aoc.util.lines

fun main() = Day20.run("2022/20.txt")

object Day20 : Day {

    private fun Input.parse() = lines.map { it.toLong() }

    override fun part1(input: Input): Any {
        return input.parse()
            .mix()
            .groveCoordinateSum()
    }

    override fun part2(input: Input): Any {
        return input.parse()
            .map { it * 811589153L }
            .mix(10)
            .groveCoordinateSum()
    }

    private fun List<Long>.mix(times: Int = 1): List<Long> {
        val mixed = this.withIndex().toMutableList()
        repeat(times) {
            this.forEachIndexed { originalIndex, number ->
                val index = mixed.indexOfFirst { (i, n) -> n == number && i == originalIndex }
                val value = mixed.removeAt(index)
                val newIndex = (((index + number) % mixed.size) + mixed.size) % mixed.size
                mixed.add(newIndex.toInt(), value)
            }
        }
        return mixed.map { it.value }
    }

    private fun List<Long>.groveCoordinateSum(): Long {
        val indexOfZero = this.indexOfFirst { it == 0L }
        val a = this[(indexOfZero + 1000) % this.size]
        val b = this[(indexOfZero + 2000) % this.size]
        val c = this[(indexOfZero + 3000) % this.size]
        return a + b + c
    }
}
