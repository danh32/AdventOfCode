package co.danhill.aoc.year2022

import co.danhill.aoc.util.Day
import co.danhill.aoc.util.Input
import co.danhill.aoc.util.groupedText

fun main() = Day05.run("2022/05.txt")

private typealias Stack = MutableList<Char>
private typealias Stacks = List<Stack>
private typealias Move = Triple<Int, Int, Int>
private typealias Foo = Pair<Stacks, List<Move>>

object Day05 : Day {

    private fun Input.parse(): Foo {
        val (stacksText, movesText) = groupedText
        val stacks = mutableListOf<Stack>()
        val stacksLines = stacksText.split('\n')
        stacksLines.last().forEachIndexed { index, c ->
            if (c.isDigit()) {
                val stack = mutableListOf<Char>()
                for (i in stacksLines.size - 2 downTo 0) {
                    stacksLines[i].getOrNull(index)?.let { crate ->
                        if (crate.isLetter()) stack.add(crate)
                    }
                }
                stacks.add(stack)
            }
        }

        val moves = movesText.split('\n').map { line ->
            val (count, from, to) = line.split(' ').mapNotNull { it.toIntOrNull() }
            Triple(count, from - 1, to - 1)
        }

        return stacks to moves
    }

    override fun part1(input: Input): Any {
        val (stacks, moves) = input.parse()
        moves.forEach { move ->
            stacks.execute(move)
        }
        return stacks.topCrateString()
    }

    override fun part2(input: Input): Any {
        val (stacks, moves) = input.parse()
        moves.forEach { move ->
            stacks.execute(move) { it }
        }
        return stacks.topCrateString()
    }

    private fun List<MutableList<Char>>.execute(move: Move, transform: (MutableList<Char>) -> List<Char> = { it.reversed() }) {
        val (count, from, to) = move
        val fromStack = get(from)
        val toStack = get(to)
        val moving = fromStack.subList(fromStack.size - count, fromStack.size)
        toStack.addAll(transform(moving))
        moving.clear()
    }

    private fun Stacks.topCrateString(): String {
        return joinToString("") { it.last().toString() }
    }
}
