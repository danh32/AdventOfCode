package co.danhill.aoc.year2023

import co.danhill.aoc.util.Day
import co.danhill.aoc.util.Input
import co.danhill.aoc.util.groupedText

fun main() = Day05.run("2023/05_test.txt")

object Day05 : Day {
    override fun part1(input: Input): Any {
        val groups = input.groupedText
        val seedNumbers = groups.first().split(' ').mapNotNull { it.toIntOrNull() }
        val mapRanges = groups.subList(1, groups.size)
            .map { group ->
                val lines = group.split('\n')
                MapRange(
                    name = lines.first(),
                    ranges = lines.subList(1, lines.size)
                        .map { line ->
                            val (destStart, sourceStart, length) = line.split(' ').map { it.toInt() }
                            MapRange.Range(sourceStart, destStart, length)
                        },
                )
            }

        return seedNumbers.minOf { seedNumber ->
            mapRanges.fold(seedNumber) { id, map -> map.map(id) }
        }
    }

    override fun part2(input: Input): Any {
        return ""
    }

    data class MapRange(
        val name: String,
        val ranges: List<Range>,
    ) {
        data class Range(
            val sourceStart: Int,
            val destinationStart: Int,
            val length: Int,
        ) {
            val sourceRange = sourceStart..(sourceStart + length)
            val destinationRange = destinationStart..(destinationStart + length)

            fun map(sourceId: Int): Int {
                return if (sourceId in sourceRange) {
                    destinationStart + sourceId - sourceStart
                } else {
                    error("$sourceId not in range $sourceRange")
                }
            }
        }

        fun map(sourceId: Int): Int {
            val range = ranges.firstOrNull { sourceId in it.sourceRange }
            return range?.map(sourceId) ?: sourceId
        }
    }
}
