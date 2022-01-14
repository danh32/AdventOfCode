package co.danhill.aoc.year2021

import co.danhill.aoc.util.Day
import java.math.BigDecimal

fun main() {
    Day10.run("2021/10.txt")
}

object Day10 : Day<List<String>>() {

    override fun parseInput(input: Sequence<String>): List<String> {
        return input.toList()
    }

    override fun part1(input: List<String>): String {
        val sum = input.sumOf { isCorrupted(it) }
        return "$sum"
    }

    override fun part2(input: List<String>): String {
        val completionStrings = input
            .filter { isCorrupted(it) == 0 }
            .map { getCompletionLine(it) }

        completionStrings.forEach {
            println(it.joinToString(""))
        }
        val scores = completionStrings
            .map { completionString ->
                var score = BigDecimal.ZERO
                for (char in completionString) {
                    score = score.multiply(BigDecimal(5))
                    score = score.plus(scoreCompletionChar(char))
                }
                score
            }
        println(scores)
        val sorted = scores.sorted()
        val middle = sorted[sorted.size / 2]
        return "$middle"
    }

    private fun isCorrupted(line: String): Int {
        val processing = mutableListOf<Char>()
        line.forEach { char ->
            when (char) {
                '(',
                '{',
                '[',
                '<' -> processing += char

                ')' -> if (processing.last() == '(') processing.removeLast() else return errorScore[char]!!
                '}' -> if (processing.last() == '{') processing.removeLast() else return errorScore[char]!!
                ']' -> if (processing.last() == '[') processing.removeLast() else return errorScore[char]!!
                '>' -> if (processing.last() == '<') processing.removeLast() else return errorScore[char]!!
            }
        }
        return 0
    }

    private fun getCompletionLine(line: String): List<Char> {
        val processing = mutableListOf<Char>()
        line.forEach { char ->
            when (char) {
                '(',
                '{',
                '[',
                '<' -> processing += char

                ')' -> if (processing.last() == '(') processing.removeLast() else error("")
                '}' -> if (processing.last() == '{') processing.removeLast() else error("")
                ']' -> if (processing.last() == '[') processing.removeLast() else error("")
                '>' -> if (processing.last() == '<') processing.removeLast() else error("")
            }
        }
        return processing
            .reversed()
            .map { char ->
                when (char) {
                    '(' -> ')'
                    '{' -> '}'
                    '[' -> ']'
                    '<' -> '>'
                    else -> error(char)
                }
            }
    }

    private fun scoreCompletionChar(char: Char): BigDecimal {
        return when (char) {
            ')' -> BigDecimal(1)
            ']' -> BigDecimal(2)
            '}' -> BigDecimal(3)
            '>' -> BigDecimal(4)
            else -> error(char)
        }
    }

    private val errorScore = mutableMapOf(
        ')' to 3,
        ']' to 57,
        '}' to 1197,
        '>' to 25137,
    )
}