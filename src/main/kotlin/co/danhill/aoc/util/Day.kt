package co.danhill.aoc.util

abstract class Day<I> {

    fun run(filename: String) {
        println("Part 1: ${part1(parseInput(filename))}")
        println("Part 2: ${part2(parseInput(filename))}")
    }

    private fun parseInput(filename: String): I {
        return parseInput(readFile(filename))
    }

    abstract fun parseInput(input: Input) : I
    abstract fun part1(input: I): Any
    abstract fun part2(input: I): Any
}
