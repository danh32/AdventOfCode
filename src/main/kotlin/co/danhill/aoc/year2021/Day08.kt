package co.danhill.aoc.year2021

import co.danhill.aoc.util.Day

fun main() {
    Day08.run("2021/08.txt")
}

object Day08 : Day<List<String>>() {

    override fun parseInput(input: Sequence<String>): List<String> {
        return input.toList()
    }

    override fun part1(input: List<String>): String {
        val count = input
            .map { line ->
                val (_, outputs) = line.split(" | ")
                outputs.split(' ')
            }
            .flatten()
            .sumOf { output ->
                when (output.length) {
                    2 -> 1
                    3 -> 1
                    4 -> 1
                    7 -> 1
                    else -> 0
                } as Int
            }
        return "$count"
    }

    override fun part2(input: List<String>): String {
        val lines = input
            .map { line ->
                val (i, o) = line.split(" | ")
                val inputs = i.split(' ').map { word -> word.toCharArray().toList().sorted().joinToString("") }
                val outputs = o.split(' ').map { word -> word.toCharArray().toList().sorted().joinToString("") }
                inputs to outputs
            }

        val sum = lines
            .map { line ->
                val answerKey = processLine(line.first)
                line.second
                    .map { output ->
                        println("Searching for $output")
                        answerKey[output]
                    }
                    .joinToString(separator = "")
                    .toInt()
            }
            .sum()
        return "$sum"
    }

    private fun processLine(inputs: List<String>): Map<String, Int> {
        println("processLine $inputs")
        val answerKey = mutableListOf(
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
        )
        answerKey[1] = inputs.find { it.length == 2 }!!
        answerKey[4] = inputs.find { it.length == 4 }!!
        answerKey[7] = inputs.find { it.length == 3 }!!
        answerKey[8] = inputs.find { it.length == 7 }!!

        answerKey[3] = inputs.find { it.length == 5 && it.contains(answerKey[1][0]) && it.contains(answerKey[1][1]) }!!
        val uniqueFourChars = answerKey[4].filter { !answerKey[1].contains(it) }
        answerKey[5] = inputs.find { candidate -> candidate.length == 5 && uniqueFourChars.all { candidate.contains(it) } }!!
        answerKey[2] = inputs.find { it.length == 5 && it != answerKey[3] && it != answerKey[5] }!!
        answerKey[9] = inputs.find { candidate -> candidate.length == 6 && answerKey[4].all { candidate.contains(it) } }!!
        answerKey[6] = inputs.find { candidate -> candidate.length == 6 && candidate != answerKey[9] && answerKey[5].all { candidate.contains(it) }}!!
        answerKey[0] = inputs.find { it.length == 6 && it != answerKey[6] && it != answerKey[9] }!!
        println("answerKey = $answerKey")
        return mapOf(
            answerKey[0] to 0,
            answerKey[1] to 1,
            answerKey[2] to 2,
            answerKey[3] to 3,
            answerKey[4] to 4,
            answerKey[5] to 5,
            answerKey[6] to 6,
            answerKey[7] to 7,
            answerKey[8] to 8,
            answerKey[9] to 9,
        )
    }
}