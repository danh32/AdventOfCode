package co.danhill.aoc.util

abstract class Day<I> {

    fun run(filename: String) {
        val input = parseInput(filename)
        println("Part 1: ${part1(input)}")
        println("Part 2: ${part2(input)}")
    }

    private fun parseInput(filename: String): I {
        return parseInput(readFile(filename))
    }

    abstract fun parseInput(input: Input) : I
    abstract fun part1(input: I): Any
    abstract fun part2(input: I): Any
}
