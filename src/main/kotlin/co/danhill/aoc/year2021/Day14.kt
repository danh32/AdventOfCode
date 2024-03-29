package co.danhill.aoc.year2021

import co.danhill.aoc.util.Day
import co.danhill.aoc.util.Input

fun main() {
    Day14.run()
}

object Day14 : Day(2021, 14) {

    override fun part1(input: Input): String {
        val lines = input.lines
        val template = lines.first().toMutableList()
        val instructions = mutableMapOf<Pair<Char, Char>, Char>()
        lines.subList(2, lines.size)
            .map { line ->
                val (from, to) = line.split(" -> ")
                val a = from[0]
                val b = from[1]
                instructions[a to b] = to[0]
            }

        for (i in 0 until 10) {
            step(template, instructions)
        }

        val counts = template.groupBy { it }
        val max = counts.maxOf { it.value.size }
        val min = counts.minOf { it.value.size }

        return "$max - $min = ${max - min}"
    }

    override fun part2(input: Input): String {
        val lines = input.lines
        val template = lines.first()
        val pairCounts = mutableMapOf<Pair<Char, Char>, Long>()
        template.windowed(2).forEach { p ->
            pairCounts[p[0] to p[1]] = 1L
        }

        val instructions = mutableMapOf<Pair<Char, Char>, Char>()
        lines.subList(2, lines.size)
            .map { line ->
                val (from, to) = line.split(" -> ")
                val a = from[0]
                val b = from[1]
                instructions[a to b] = to[0]
            }

        for (i in 0 until 40) {
//            println("Step $i")
//            println("$pairCounts")
            step2(pairCounts, instructions)
        }
//        println(pairCounts)

        val counts = mutableMapOf<Char, Long>()
        pairCounts.forEach { (pair, count) ->
            val a = pair.first
            val b = pair.second
            counts[a] = counts.getOrDefault(a, 0L) + count
            counts[b] = counts.getOrDefault(b, 0L) + count
        }
        val first = template.first()
        counts[first] = counts.getOrDefault(first, 0) + 1
        val last = template.last()
        counts[last] = counts.getOrDefault(last, 0) + 1

//        println(counts)
        val max = counts.maxOf { it.value } / 2L
        val min = counts.minOf { it.value } / 2L

        return "$max - $min = ${max - min}"
    }

    private fun step(template: MutableList<Char>, instructions: Map<Pair<Char, Char>, Char>) {
        var i = 0
        while (i < template.size - 1) {
            val a = template[i]
            val b = template[i + 1]
            instructions[a to b]?.let { c ->
                template.add(i + 1, c)
                i++
            }
            i++
        }
    }

    private fun step2(
        pairCounts: MutableMap<Pair<Char, Char>, Long>,
        instructions: Map<Pair<Char, Char>, Char>,
    ) {
        val original = pairCounts.toMap()
        original.forEach { (pair, count) ->
            instructions[pair]?.let { c ->
//                println("Rule $pair -> $c")
                val a = pair.first
                val b = pair.second
                pairCounts.compute(a to c) { _, currentCount ->
                    (currentCount ?: 0L) + count
                }
                pairCounts.compute(c to b) { _, currentCount ->
                    (currentCount ?: 0L) + count
                }
                pairCounts.compute(pair) { _, currentCount ->
                    (currentCount ?: 0L) - count
                }
            }
        }
    }
}