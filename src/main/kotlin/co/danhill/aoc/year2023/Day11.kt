package co.danhill.aoc.year2023

import co.danhill.aoc.util.*
import kotlin.math.max
import kotlin.math.min

fun main() = Day11.run("2023/11.txt")

object Day11 : Day {
    override fun part1(input: Input): Any {
        return input.lines.toCharGrid().calculateDistances(cost = 2L)
    }

    override fun part2(input: Input): Any {
        return input.lines.toCharGrid().calculateDistances(cost = 1000000L)
    }

    private fun Grid<Char>.calculateDistances(cost: Long): Long {
        val rowsToExpand = (minY..maxY).filter { y ->
            (minX..maxX).all { x -> this[x to y] == '.' }
        }
        val columnsToExpand = (minX..maxX).filter { x ->
            (minY..maxY).all { y -> this[x to y] == '.' }
        }
        val galaxies = entries.filter { (_, c) -> c == '#' }.map { (p, _) -> p }

        val distances = galaxies.mapIndexed { index, (startX, startY) ->
            if (index < galaxies.size) {
                galaxies.subList(index + 1, galaxies.size).map { (endX, endY) ->
                    val minX = min(startX, endX)
                    val maxX = max(startX, endX)
                    val minY = min(startY, endY)
                    val maxY = max(startY, endY)
                    val expandedRows = rowsToExpand.count { row -> row in minY..maxY }
                    val expandedColumns = columnsToExpand.count { col -> col in minX..maxX }
                    val expandedCount = expandedRows + expandedColumns
                    (maxX - minX) + (maxY - minY) - expandedCount + (expandedCount * cost)
                }
            } else {
                emptyList()
            }
        }
        return distances.flatten().sum()
    }
}
