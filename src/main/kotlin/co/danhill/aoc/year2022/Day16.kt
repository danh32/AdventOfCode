package co.danhill.aoc.year2022

import co.danhill.aoc.util.Day
import co.danhill.aoc.util.Input
import java.util.*

fun main() = Day16.run()

object Day16 : Day(2022, 16) {

    private fun Input.parse() = lines
        .map { line ->
            val split = line.split(' ')
            Valve(
                name = split[1],
                flowRate = split[4].removePrefix("rate=").dropLast(1).toInt(),
                tunnels = split.slice(9 until split.size).map { it.removeSuffix(",") },
            )
        }
        .associateBy { it.name }

    override fun part1(input: Input): Any {
//        if (true) error("Faaaack")
        val valves = input.parse()
        val start = valves.getValue("AA")

        val states = PriorityQueue<State>()
        states += State(
            minutesLeft = 30,
            currentValve = start,
            elephantValve = start,
            valves = valves,
            openValves = emptySet(),
            projectedPressureRelieved = 0,
        )
        var bestSoFar = states.first()
        val seenHashes = mutableSetOf<Int>()
        while (states.isNotEmpty()) {
            val next = states.first().also { states.remove(it) }
            if (seenHashes.contains(next.hashCode())) continue
            if (next.projectedPressureRelieved > bestSoFar.projectedPressureRelieved) {
                bestSoFar = next
            }
            if (!next.isEnd) {
                states += next.generateNextStates()
            } else {
                return next.projectedPressureRelieved
            }
            seenHashes += next.hashCode()
        }
        return bestSoFar.projectedPressureRelieved
    }

    override fun part2(input: Input): Any {
//        val valves = input.parse()
//        val start = valves.getValue("AA")
//
//        val states = PriorityQueue<State>()
//        states += State(
//            minutesLeft = 26,
//            currentValve = start,
//            elephantValve = start,
//            valves = valves,
//            openValves = emptySet(),
//            projectedPressureRelieved = 0,
//        )
//
//        var bestSoFar = states.first()
//        val seenHashes = mutableSetOf<Int>()
//        while (states.isNotEmpty()) {
//            val next = states.poll()
//            if (seenHashes.contains(next.hashCode())) continue
//            if (next.projectedPressureRelieved > bestSoFar.projectedPressureRelieved) {
//                bestSoFar = next
//            }
//            if (!next.isEnd) {
//                states += next.generateNextStatesWithElephant()
//            } else {
//                return next.projectedPressureRelieved
//            }
//            seenHashes += next.hashCode()
//        }
//        return bestSoFar.projectedPressureRelieved

        val valves = input.parse()
        val start = valves.getValue("AA")

        val states = PriorityQueue<State>()
        states += State(
            minutesLeft = 26,
            currentValve = start,
            elephantValve = start,
            valves = valves,
            openValves = emptySet(),
            projectedPressureRelieved = 0,
        )
        var bestSoFar = states.first()
        val seenHashes = mutableSetOf<Int>()
        while (states.isNotEmpty()) {
            val next = states.first().also { states.remove(it) }
            if (seenHashes.contains(next.hashCode())) continue
            if (next.projectedPressureRelieved > bestSoFar.projectedPressureRelieved) {
                bestSoFar = next
            }
            if (!next.isEnd) {
                states += next.generateNextStates()
            } else {
                return next.projectedPressureRelieved
            }
            seenHashes += next.hashCode()
        }

        val elephantStart = bestSoFar.copy(
            minutesLeft = 26,
            currentValve = start,
        )
        states.clear()
        states.add(elephantStart)
        bestSoFar = states.first()
        seenHashes.clear()
        while (states.isNotEmpty()) {
            val next = states.first().also { states.remove(it) }
            if (seenHashes.contains(next.hashCode())) continue
            if (next.projectedPressureRelieved > bestSoFar.projectedPressureRelieved) {
                bestSoFar = next
            }
            if (!next.isEnd) {
                states += next.generateNextStates()
            } else {
                return next.projectedPressureRelieved
            }
            seenHashes += next.hashCode()
        }
        return bestSoFar.projectedPressureRelieved
    }

    private fun PriorityQueue<State>.putBest(state: State) {

    }

    private data class State(
        val minutesLeft: Int,
        val currentValve: Valve,
        val elephantValve: Valve,
        val valves: Map<String, Valve>,
        val openValves: Set<String>,
        val projectedPressureRelieved: Int,
    ) : Comparable<State> {

        val searchScore = (Int.MAX_VALUE / 2) - projectedPressureRelieved

        val heuristicScore = (Int.MAX_VALUE / 2) - (unopenedFlowPressure() * (minutesLeft - 1))

        val newScore = projectedPressureRelieved + (unopenedFlowPressure() * (minutesLeft - 1))
        val newestScore = (Int.MAX_VALUE / 2) - newScore

        val isEnd = minutesLeft == 0 || allNonzeroAreOpen()

        fun generateNextStates(): List<State> {
            val nextStates = currentValve.tunnels
                .map { tunnel ->
                    copy(
                        minutesLeft = minutesLeft - 1,
                        currentValve = valves[tunnel]!!,
                    )
                }.toMutableList()

            if (currentValve.canOpen()) {
                nextStates += openCurrentValve()
            }
            return nextStates
        }

        fun generateNextStatesWithElephant(): List<State> {
            val nextStates = currentValve.tunnels
                .map { tunnel ->
                    elephantValve.tunnels.map { et ->
                        copy(
                            minutesLeft = minutesLeft - 1,
                            currentValve = valves[tunnel]!!,
                            elephantValve = valves[et]!!,
                        )
                    }
                }
                .toMutableList()

            val weCanOpen = currentValve.canOpen()
            val elephantCanOpen = elephantValve.canOpen()
            // we open, elephant moves
            if (weCanOpen) {
                val weOpenValves = openValves.toMutableSet().also { it += currentValve.name }
                val weOpenFlowRate = projectedPressureRelieved + (currentValve.flowRate * (minutesLeft - 1))
                nextStates += elephantValve.tunnels.map { et ->
                    copy(
                        minutesLeft = minutesLeft - 1,
                        elephantValve = valves[et]!!,
                        openValves = weOpenValves,
                        projectedPressureRelieved = weOpenFlowRate,
                    )
                }
            }

            // elephant opens, we move
            if (elephantCanOpen) {
                val elephantOpenValves = openValves.toMutableSet().also { it += elephantValve.name }
                val elephantOpenFlowRate = projectedPressureRelieved + (elephantValve.flowRate * (minutesLeft - 1))
                nextStates += currentValve.tunnels.map { tunnel ->
                    copy(
                        minutesLeft = minutesLeft - 1,
                        currentValve = valves[tunnel]!!,
                        openValves = elephantOpenValves,
                        projectedPressureRelieved = elephantOpenFlowRate,
                    )
                }
            }

            // we both open
            if (weCanOpen && elephantCanOpen && currentValve != elephantValve) {
                nextStates += listOf(
                    copy(
                        minutesLeft = minutesLeft - 1,
                        openValves = openValves.toMutableSet().also {
                            it += currentValve.name
                            it += elephantValve.name
                        },
                        projectedPressureRelieved = projectedPressureRelieved + ((currentValve.flowRate + elephantValve.flowRate) * (minutesLeft - 1)),
                    )
                )
            }
            return nextStates.flatten().distinct()
        }


        private fun openCurrentValve(): State {
            if (currentValve.isOpen()) error("Current valve ${currentValve.name} already open!")
            return copy(
                minutesLeft = minutesLeft - 1,
                openValves = openValves.toMutableSet().also { it += currentValve.name },
                projectedPressureRelieved = projectedPressureRelieved + (currentValve.flowRate * (minutesLeft - 1)),
            )
        }

        override fun compareTo(other: State): Int {
            return this.newestScore.compareTo(other.newestScore)
        }

        private fun allNonzeroAreOpen(): Boolean = valves.values.all { it.flowRate == 0 || it.isOpen() }

        private fun unopenedFlowPressure(): Int = valves.values.sumOf { if (it.isOpen()) 0 else it.flowRate }

        private fun Valve.isOpen() = openValves.contains(this.name)
        private fun Valve.canOpen() = !isOpen() && flowRate > 0
    }

    private data class Valve(
        val name: String,
        val flowRate: Int,
        val tunnels: List<String>,
    )
}
