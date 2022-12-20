package co.danhill.aoc.year2022

import co.danhill.aoc.util.Day
import co.danhill.aoc.util.Input
import co.danhill.aoc.util.lines

fun main() = Day20.run("2022/20.txt")

object Day20 : Day {

    private fun Input.parse() = lines.map { it.toInt() }

    override fun part1(input: Input): Any {
        val original = input.parse()
        val mixed = original.withIndex().toMutableList()

        original.forEachIndexed { originalIndex, number ->
            val index = mixed.indexOfFirst { (i, n) -> n == number && i == originalIndex }
            val value = mixed.removeAt(index)
            val newIndex = (((index + number) % mixed.size) + mixed.size) % mixed.size
            mixed.add(newIndex, value)
        }

        val indexOfZero = mixed.indexOfFirst { (_, n) -> n == 0 }
        val a = mixed[(indexOfZero + 1000) % mixed.size].value
        val b = mixed[(indexOfZero + 2000) % mixed.size].value
        val c = mixed[(indexOfZero + 3000) % mixed.size].value
        println("$a + $b + $c")
        return a + b + c
    }

    override fun part2(input: Input): Any {
        val original = input.parse().map { it * 811589153L }
        val mixed = original.withIndex().toMutableList()
        println(original)
        repeat(10) {
            original.forEachIndexed { originalIndex, number ->
                val index = mixed.indexOfFirst { (i, n) -> n == number && i == originalIndex }
                val value = mixed.removeAt(index)
                val newIndex = (((index + number) % mixed.size) + mixed.size) % mixed.size
                mixed.add(newIndex.toInt(), value)
            }
        }

        val indexOfZero = mixed.indexOfFirst { (_, n) -> n == 0L }
        val a = mixed[(indexOfZero + 1000) % mixed.size].value
        val b = mixed[(indexOfZero + 2000) % mixed.size].value
        val c = mixed[(indexOfZero + 3000) % mixed.size].value
        println("$a + $b + $c")
        return a + b + c
    }
}