package co.danhill.aoc.year2021

import co.danhill.aoc.util.Day
import co.danhill.aoc.util.Input

fun main() {
    Day12.run()
}

object Day12 : Day(2021, 12) {

    override fun part1(input: Input): String {
        val startCave = parseCaves(input.lines)
        val paths = findPaths(listOf(startCave))
        return "${paths.size}"
    }

    override fun part2(input: Input): String {
        val startCave = parseCaves(input.lines)
        val paths = findPaths2(listOf(startCave))
        return "${paths.size}"
    }

    private class Cave(val name: String) {
        val connectedCaves = mutableListOf<Cave>()
        val isStart = name == "start"
        val isEnd = name == "end"
        val isBig = name.first().isUpperCase()

        override fun toString(): String = name
    }

    private fun parseCaves(input: List<String>): Cave {
        val namesToCaves = mutableMapOf<String, Cave>()
        input.forEach { line ->
            val (a, b) = line.split('-')
            val caveA = namesToCaves.getOrPut(a) { Cave(a) }
            val caveB = namesToCaves.getOrPut(b) { Cave(b) }
            caveA.connectedCaves += caveB
            caveB.connectedCaves += caveA
        }
        return namesToCaves["start"]!!
    }

    private fun findPaths(pathSoFar: List<Cave>): List<List<Cave>> {
        val currentCave = pathSoFar.last()
        if (currentCave.isEnd) {
            return listOf(pathSoFar)
        }

        val nextMoves = currentCave.connectedCaves
            .filter { candidate ->
                candidate.isBig || pathSoFar.none { it.name == candidate.name }
            }

        if (nextMoves.isEmpty()) {
            return emptyList()
        }

        val paths = mutableListOf<List<Cave>>()
        nextMoves.forEach { move ->
            paths += findPaths(pathSoFar + move)
        }
        return paths
    }

    private fun findPaths2(pathSoFar: List<Cave>): List<List<Cave>> {
        val currentCave = pathSoFar.last()
        if (currentCave.isEnd) {
            return listOf(pathSoFar)
        }

        val hasVisitedASmallCaveTwice = pathSoFar.filter { !it.isBig }
            .groupBy { it.name }
            .values.any { it.size > 1 }
        val nextMoves = currentCave.connectedCaves
            .filter { candidate ->
                when {
                    candidate.isBig -> true
                    candidate.isStart -> false
                    else -> {
                        when (pathSoFar.count { it.name == candidate.name }) {
                            0 -> true
                            1 -> !hasVisitedASmallCaveTwice
                            else -> false
                        }
                    }
                }
            }

        if (nextMoves.isEmpty()) {
            return emptyList()
        }

        val paths = mutableListOf<List<Cave>>()
        nextMoves.forEach { move ->
            paths += findPaths2(pathSoFar + move)
        }
        return paths
    }
}