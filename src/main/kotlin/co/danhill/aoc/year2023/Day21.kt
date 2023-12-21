package co.danhill.aoc.year2023

import co.danhill.aoc.util.*

fun main() = Day21.run("2023/21.txt")

object Day21 : Day {
    override fun part1(input: Input): Any {
        val grid = input.lines.toCharGrid()
        val reachablePoints = grid.getReachablePoints(64)
        println(reachablePoints)
        for (y in grid.yRange) {
            for (x in grid.xRange) {
                val p = x to y
                print(
                    if (p in reachablePoints) 'O'
                    else grid[p]
                )
            }
            println()
        }
        return reachablePoints.size
    }

    override fun part2(input: Input): Any {
        return ""
    }

    private fun Grid<Char>.getReachablePoints(steps: Int): Set<Point> {
        val (start, _) = this.entries.find { (_, c) -> c == 'S' }!!
        val nextStepQueue = mutableSetOf(start)
        val queue = mutableListOf<Point>()
        repeat(steps) {
            queue += nextStepQueue
            nextStepQueue.clear()
            while (queue.isNotEmpty()) {
                val point = queue.removeFirst()
                nextStepQueue += point.cardinalNeighbors.filter { candidate ->
                    when (val c = this[candidate]) {
                        '#' -> false
                        '.' -> true
                        'S' -> true
                        null -> false
                        else -> error("Unknown value $c at $candidate")
                    }
                }
            }
        }
//        println("Start $start")
//        Search.aStar(
//            start = start,
//            isEnd = { p ->
//                visited += p
//                false
//            },
//            generateNextStates = { p, seq ->
//                if (seq.count() > steps) emptyList()
//                else {
//                    p.cardinalNeighbors
//                        .filter { candidate ->
//                            when (val c = this[candidate]) {
//                                '#' -> false
//                                '.' -> true
//                                'S' -> true
//                                null -> false
//                                else -> error("Unknown value $c at $candidate")
//                            }
//                        }
//                }
//            },
//            movementCost = { _, _ -> 1 },
//            heuristicCostToEndState = { 1 },
//        )
        return nextStepQueue
    }
}
