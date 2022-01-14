package co.danhill.aoc.util

abstract class Day<I> {

    fun run(filename: String) {
        val input = parseInput(filename)
        println("Part 1: ${part1(input)}")
        println("Part 2: ${part2(input)}")
    }

    private fun parseInput(filename: String): I {
        return readFile(filename).useLines(block = this::parseInput)
    }

    abstract fun parseInput(input: Sequence<String>) : I
    abstract fun part1(input: I): String
    abstract fun part2(input: I): String
}
