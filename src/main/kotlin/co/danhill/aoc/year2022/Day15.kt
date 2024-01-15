package co.danhill.aoc.year2022

import co.danhill.aoc.util.*

fun main() = Day15.run()

object Day15 : Day(2022, 15) {

    private fun Input.parse() = lines.map { line ->
        val (sensorText, beaconText) = line.removePrefix("Sensor at x=").split(": closest beacon is at x=")
        val (sX, sY) = sensorText.split(", y=").map { it.toInt() }
        val (bX, bY) = beaconText.split(", y=").map { it.toInt() }
        (sX to sY) to (bX to bY)
    }

    override fun part1(input: Input): Any {
        val sensors = input.parse()
        val beaconSet = sensors.fold(mutableSetOf<Point>()) { set, (s, b) ->
            set += s
            set += b
            set
        }
        val y = 2_000_000
//        val y = 10
        return (-1_000_000..5_000_000).count { x ->
//        return (-10..30).count { x ->
            val candidate = x to y
            !beaconSet.contains(candidate) && sensors.any { (s, b) ->
                s.manhattanDistanceTo(candidate) <= s.manhattanDistanceTo(b)
            }
        }
    }

    // clever solution stolen from Dan Lew: https://github.com/dlew/advent-of-code/blob/main/2022/src/main/kotlin/Day15.kt
    override fun part2(input: Input): Any {
        val sensors = input.parse()
        val maxX = 4000000 //20
        val maxY = 4000000 //20
        for (y in 0..maxY) {
            var x = 0
            while (x <= maxX) {
                val maxDelta = sensors.maxOf { (s, b) -> s.manhattanDistanceTo(b) - s.manhattanDistanceTo(x to y) }
                if (maxDelta < 0) {
                    return x * 4000000L + y
                }
                x += maxDelta + 1
            }
        }
        error("Distress signal not found")
    }
}
