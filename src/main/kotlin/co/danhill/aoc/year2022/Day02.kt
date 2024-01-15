package co.danhill.aoc.year2022

import co.danhill.aoc.util.Day
import co.danhill.aoc.util.Input

private typealias Round = Pair<Char, Char>

fun main() = Day02.run()

object Day02 : Day(2022, 2) {

    private fun Input.parse(): List<Round> {
        return lines.map { round ->
            val (opponent, me) = round.split(' ')
            opponent.single() to me.single()
        }
    }

    override fun part1(input: Input): Any {
        return input.parse().sumOf { it.second.toPlay().score(it.first.toPlay()) }
    }

    override fun part2(input: Input): Any {
        return input.parse().sumOf { round ->
            val result = round.second.toResult()
            val myPlay = result.getMyPlay(round.first.toPlay())
            result.score + myPlay.score
        }
    }

    enum class Result(val score: Int) {
        WIN(6),
        DRAW(3),
        LOSE(0),
        ;

        fun getMyPlay(opponentPlay: Play): Play = when (this) {
            WIN -> when (opponentPlay) {
                Play.ROCK -> Play.PAPER
                Play.PAPER -> Play.SCISSORS
                Play.SCISSORS -> Play.ROCK
            }

            DRAW -> opponentPlay
            LOSE -> when (opponentPlay) {
                Play.ROCK -> Play.SCISSORS
                Play.PAPER -> Play.ROCK
                Play.SCISSORS -> Play.PAPER
            }
        }
    }

    enum class Play(val score: Int) {
        ROCK(1),
        PAPER(2),
        SCISSORS(3),
        ;

        fun beats(other: Play): Boolean = when (this) {
            ROCK -> other == SCISSORS
            PAPER -> other == ROCK
            SCISSORS -> other == PAPER
        }

        fun result(opponentPlay: Play): Result = when {
            this.beats(opponentPlay) -> Result.WIN
            this == opponentPlay -> Result.DRAW
            else -> Result.LOSE
        }

        fun score(opponentPlay: Play): Int {
            return this.score + this.result(opponentPlay).score
        }
    }

    private fun Char.toPlay(): Play = when (this) {
        'A' -> Play.ROCK
        'B' -> Play.PAPER
        'C' -> Play.SCISSORS
        'X' -> Play.ROCK
        'Y' -> Play.PAPER
        'Z' -> Play.SCISSORS
        else -> error("Unknown play: $this")
    }

    private fun Char.toResult(): Result = when (this) {
        'X' -> Result.LOSE
        'Y' -> Result.DRAW
        'Z' -> Result.WIN
        else -> error("Unknown result: $this")
    }
}
