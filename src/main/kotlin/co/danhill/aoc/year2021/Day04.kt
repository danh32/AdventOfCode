package co.danhill.aoc.year2021

import co.danhill.aoc.util.*
import java.util.UUID

fun main() {
    Day04.run("2021/04.txt")
}

object Day04 : Day<Pair<List<Int>, List<Day04.Board>>>() {

    override fun parseInput(input: Input): Pair<List<Int>, List<Board>> {
        val list = input.lines
        val drawnNumbers: List<Int> = list.first().split(',').map { it.toInt() }
        val boards = list.subList(2, list.size)
            .windowed(5, step = 6)
            .map { boardLines ->
                val board = Board()
                boardLines.forEachIndexed { index, line ->
                    val numbers = line.split(' ').mapNotNull { it.toIntOrNull() }
                    board.addLine(index, numbers)
                }
                board
            }
        return drawnNumbers to boards
    }

    override fun part1(input: Pair<List<Int>, List<Board>>): String {
        val (drawnNumbers, boards) = input
        for (number in drawnNumbers) {
            for (board in boards) {
                board.callNumber(number)
                if (board.isWinner()) {
                    return board.score(number).toString()
                }
            }
        }
        error("No winner")
    }

    override fun part2(input: Pair<List<Int>, List<Board>>): String {
        val (drawnNumbers, boards) = input
        val winningBoards = mutableMapOf<UUID, Pair<Board, Int>>()
        for (number in drawnNumbers) {
            for (board in boards) {
                board.callNumber(number)
                if (board.isWinner()) {
                    if (!winningBoards.contains(board.id)) {
                        winningBoards[board.id] = board to board.score(number)
                    }
                }
            }
        }
        val lowestBoard = winningBoards.values.last()
        return lowestBoard.second.toString()
    }

    class Cell(
        val value: Int,
        var highlighted: Boolean = false,
    )

    class Board(
        val id: UUID = UUID.randomUUID(),
        val grid: Grid<Cell> = gridOf()
    ) {

        fun isComplete(): Boolean {
            val point: Point = 4 to 4
            return grid[4 to 4] != null
        }

        fun addLine(index: Int, numbers: List<Int>) {
            check(index < 5) { "Index $index too high"}
            numbers.forEachIndexed { index2, number ->
                grid[index to index2] = Cell(number)
            }
        }

        fun callNumber(value: Int) {
            grid.values.find { it.value == value }?.highlighted = true
        }

        fun isWinner(): Boolean {
            return ROWS.any { row -> row.all { grid[it]!!.highlighted } } ||
                    COLUMNS.any { column -> column.all { grid[it]!!.highlighted } }
        }

        fun score(lastCalled: Int): Int {
            val sum = grid.values.sumOf { cell -> if (cell.highlighted) 0 else cell.value }
            println("$lastCalled * $sum = ${lastCalled * sum}")
            return lastCalled * sum
        }

        fun print() {
            (0..4).forEach { i ->
                (0..4).forEach { j ->
                    print(grid[i to j]!!.value)
                    print(' ')
                }
                println()
            }
        }

        companion object {
            val ROWS = listOf(
                listOf(
                    0 to 0,
                    0 to 1,
                    0 to 2,
                    0 to 3,
                    0 to 4,
                ),

                listOf(
                    1 to 0,
                    1 to 1,
                    1 to 2,
                    1 to 3,
                    1 to 4,
                ),

                listOf(
                    2 to 0,
                    2 to 1,
                    2 to 2,
                    2 to 3,
                    2 to 4,
                ),
                listOf(
                    3 to 0,
                    3 to 1,
                    3 to 2,
                    3 to 3,
                    3 to 4,
                ),

                listOf(
                    4 to 0,
                    4 to 1,
                    4 to 2,
                    4 to 3,
                    4 to 4,
                ),
            )

            val COLUMNS = listOf(
                listOf(
                    0 to 0,
                    1 to 0,
                    2 to 0,
                    3 to 0,
                    4 to 0,
                ),

                listOf(
                    0 to 1,
                    1 to 1,
                    2 to 1,
                    3 to 1,
                    4 to 1,
                ),

                listOf(
                    0 to 2,
                    1 to 2,
                    2 to 2,
                    3 to 2,
                    4 to 2,
                ),

                listOf(
                    0 to 3,
                    1 to 3,
                    2 to 3,
                    3 to 3,
                    4 to 3,
                ),

                listOf(
                    0 to 4,
                    1 to 4,
                    2 to 4,
                    3 to 4,
                    4 to 4,
                ),
            )
        }
    }
}