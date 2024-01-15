package co.danhill.aoc.year2022

import co.danhill.aoc.util.*

fun main() = Day22.run()

object Day22 : Day(2022, 22) {

    private fun Input.parse(): Pair<Grid<Char>, List<Instruction>> {
        val (mapLines, directionText) = groupedText

        val instructions = mutableListOf<Instruction>()
        var numberSoFar = 0
        fun storeNumberIfNeeded() {
            if (numberSoFar != 0) {
                instructions += Instruction.Step(numberSoFar)
                numberSoFar = 0
            }
        }

        for (c in directionText) {
            when (c) {
                'R' -> {
                    storeNumberIfNeeded()
                    instructions += Instruction.Turn(true)
                }
                'L' -> {
                    storeNumberIfNeeded()
                    instructions += Instruction.Turn(false)
                }
                else -> {
                    numberSoFar *= 10
                    numberSoFar += c.digitToInt()
                }
            }
        }
        storeNumberIfNeeded()

        return mapLines.split('\n').toCharGrid() to instructions
    }

    override fun part1(input: Input): Any {
        val (map, instructions) = input.parse()
        var player = map.startingPoint
        var direction = Direction.RIGHT

        for (instruction in instructions) {
            when (instruction) {
                is Instruction.Step -> {
                    for (i in 0 until instruction.steps) {
                        val nextPosition = map.step(player, direction)
                        if (nextPosition == player) {
                            break
                        }
                        map[player] = direction.displayChar
                        player = nextPosition
                    }
                }
                is Instruction.Turn -> direction = direction.turn(instruction.clockwise)
            }
        }
        map[player] = direction.displayChar
        map.print { it?.toString() ?: " " }

        return player.rowValue + player.columnValue + direction.facingValue
    }

    override fun part2(input: Input): Any {
        TODO()
    }

    private val Grid<Char>.startingPoint
        get() = (0..maxX).first { x -> this[x to 0] == '.' } to 0

    private val walkableChars = setOf('.', '<', '^', '>', 'v')

    private fun Grid<Char>.step(player: Point, direction: Direction): Point {
        var candidate = player.step(direction)
        while (this[candidate] !in walkableChars) {
            when (val c = this[candidate]) {
                in walkableChars -> error("Should be caught by outer while clause")
                // hit a wall, abort early
                '#' -> return player

                null, ' ' -> {
                    val (x, y) = candidate.step(direction)
                    candidate = (((x % maxX) + maxX) % maxX) to (((y % maxY) + maxY) % maxY)
                }
                else -> error("Unknown grid value: $c")
            }
        }
        return candidate
    }

    private val Point.columnValue: Int
        get() = (x + 1) * 4

    private val Point.rowValue: Int
        get() = (y + 1) * 1000

    private val Direction.facingValue: Int
        get() = when (this) {
            Direction.LEFT -> 2
            Direction.UP -> 3
            Direction.RIGHT -> 0
            Direction.DOWN -> 1
        }

    private sealed class Instruction {
        data class Step(val steps: Int) : Instruction()
        data class Turn(val clockwise: Boolean) : Instruction()
    }
}
