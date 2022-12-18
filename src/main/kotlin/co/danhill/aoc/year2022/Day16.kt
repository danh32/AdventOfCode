package co.danhill.aoc.year2022

import co.danhill.aoc.util.Day
import co.danhill.aoc.util.Input
import co.danhill.aoc.util.lines

fun main() = Day16.run("2022/16_test.txt")

object Day16 : Day {

    private fun Input.parse() = lines
        .map { line ->
            val split = line.split(' ')
            Valve(
                name = split[1],
                flowRate = split[4].removePrefix("rate=").dropLast(1).toInt(),
                tunnels = split.slice(9 until split.size).map { it.removeSuffix(",") },
                open = false,
            )
        }
        .associateBy { it.name }

    override fun part1(input: Input): Any {
        val valves = input.parse()
        val start = valves.getValue("AA")

        val path = computeBestPath(valves, start, 30)
        for ((action, valve) in path) {
            println("$action ${valve.name}")
        }

//        val toConsider = mutableListOf(start)
//        var minute = 0
//        while (toConsider.isNotEmpty() && minute < 30) {
//            val current = toConsider.removeFirst()
//            minute++
//        }

        return path.score(30)
    }

    override fun part2(input: Input): Any {
        TODO()
    }

    private data class Valve(
        val name: String,
        val flowRate: Int,
        val tunnels: List<String>,
        val open: Boolean,
    ) {
    }

    private enum class Action {
        MOVE_TO,
        OPEN,
        ;
    }

    private fun Map<String, Valve>.allNonzeroAreOpen(): Boolean = values.all { it.flowRate == 0 || it.open }

    private fun Map<String, Valve>.totalFlowRate(): Int = values.sumOf { if (it.open) it.flowRate else 0 }

    private fun Map<String, Valve>.releasedPressure(): Int = values.sumOf { if (it.open) it.flowRate else 0 }

    private fun computeBestPath(
        valves: Map<String, Valve>,
        currentValve: Valve,
        minutesLeft: Int,
    ): List<Pair<Action, Valve>> {
        if (minutesLeft == 0 || valves.allNonzeroAreOpen()) {
            return emptyList()
        }

        val possibilities = mutableListOf<List<Pair<Action, Valve>>>()
        if (!currentValve.open && currentValve.flowRate != 0) {
            val newValves = valves.toMutableMap()
            val newCurrentValve = currentValve.copy(open = true)
            newValves[currentValve.name] = newCurrentValve
            possibilities += listOf(Action.OPEN to currentValve) + computeBestPath(
                valves = newValves,
                currentValve = newCurrentValve,
                minutesLeft = minutesLeft - 1,
            )
        }
        // can open current valve, or move to any others
        currentValve.tunnels.forEach { tunnel ->
            val nextValve = valves.getValue(tunnel)
            possibilities += listOf(Action.MOVE_TO to nextValve) + computeBestPath(
                valves = valves,
                currentValve = nextValve,
                minutesLeft = minutesLeft - 1,
            )
        }

        return possibilities.maxBy { it.score(minutesLeft) }
    }

    private fun List<Pair<Action, Valve>>.score(totalTime: Int): Int {
        return withIndex()
            .sumOf { (index, pair) ->
                val (action, valve) = pair
                when (action) {
                    Action.MOVE_TO -> 0
                    Action.OPEN -> {
                        val minutesLeft = totalTime - index - 1
                        minutesLeft * valve.flowRate
                    }
                }
            }
    }
}
