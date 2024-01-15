package co.danhill.aoc.year2023

import co.danhill.aoc.util.Day
import co.danhill.aoc.util.Input

fun main() = Day08.run()

object Day08 : Day(2023, 8) {
    // 17263
    override fun part1(input: Input): Any {
        val lines = input.lines.toList()
        val sequence = lines.first()
        val map = lines.subList(2, lines.size)
            .map { line ->
                val start = line.substring(0, 3)
                val left = line.substring(7, 10)
                val right = line.substring(12, 15)
                Node(start, left, right)
            }
            .associateBy { it.start }

        var currentNode: Node = map.getValue("AAA")
        var stepCount = 0
        while (currentNode.start != "ZZZ") {
            val direction = sequence[stepCount % sequence.length]
            currentNode = map.getValue(
                when (direction) {
                    'L' -> currentNode.leftDestination
                    'R' -> currentNode.rightDestination
                    else -> error("Unknown direction $direction")
                }
            )
            stepCount++
        }
        return stepCount
    }

    override fun part2(input: Input): Any {
        val lines = input.lines.toList()
        val sequence = lines.first()
        val map = lines.subList(2, lines.size)
            .map { line ->
                val start = line.substring(0, 3)
                val left = line.substring(7, 10)
                val right = line.substring(12, 15)
                Node(start, left, right)
            }
            .associateBy { it.start }

        var startNodes = map.keys
            .filter { it.last() == 'A' }
        var stepCount = 0
        while (!startNodes.all { it.last() == 'Z' }) {
            println(startNodes)
            val direction = sequence[stepCount % sequence.length]
            val nextNodes = startNodes.map { currentNodeName ->
                val currentNode = map.getValue(currentNodeName)
                when (direction) {
                    'L' -> currentNode.leftDestination
                    'R' -> currentNode.rightDestination
                    else -> error("Unknown direction $direction")
                }
            }
            startNodes = nextNodes
            stepCount++
        }
        return stepCount
    }

    data class Node(
        val start: String,
        val leftDestination: String,
        val rightDestination: String,
    )
}
