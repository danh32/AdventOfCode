package co.danhill.aoc.util

import kotlin.system.measureTimeMillis

abstract class Day(
    private val year: Int,
    private val day: Int,
) {

    private val filename = "$year/${day.toString().padStart(2, '0')}.txt"

    fun run() {
        println("$year Day $day")
        var time: Long
        var result: Any

//        val foo = readFile(filename)
//        println("${foo.path} vs ${foo.absolutePath}")

        time = measureTimeMillis {
            result = try {
                part1(RemoteInput(year, day))
            } catch (t: Throwable) {
                t.printStackTrace()
                "Errored out."
            }
        }
        println("Part 1: $result")
        println("\t($time millis)")

        time = measureTimeMillis {
            result = part2(FileInput(filename))
        }
        println("Part 2: $result")
        println("\t($time millis)")
    }

    abstract fun part1(input: Input): Any
    abstract fun part2(input: Input): Any
}
