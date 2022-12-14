package co.danhill.aoc.util

import kotlin.system.measureTimeMillis

interface Day {

    fun run(filename: String) {
        println("Day ${javaClass.simpleName.substring(3)}")
        var time: Long
        var result: Any

        time = measureTimeMillis {
            result = part1(readFile(filename))
        }
        println("Part 1: $result")
        println("\t($time millis)")

        time = measureTimeMillis {
            result = part2(readFile(filename))
        }
        println("Part 2: $result")
        println("\t($time millis)")
    }

    fun part1(input: Input): Any
    fun part2(input: Input): Any
}
