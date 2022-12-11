package co.danhill.aoc.util

interface Day {

    fun run(filename: String) {
        println("Day ${javaClass.simpleName.substring(3)}")
        println("Part 1: ${part1(readFile(filename))}")
        println("Part 2: ${part2(readFile(filename))}")
    }

    fun part1(input: Input): Any
    fun part2(input: Input): Any
}
