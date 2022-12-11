package co.danhill.aoc.year2022

import co.danhill.aoc.util.Day
import co.danhill.aoc.util.Input
import co.danhill.aoc.util.lines

fun main() = Day10.run("2022/10.txt")

object Day10 : Day {
    private fun Input.toInstructions() = lines.map { line ->
        val splitLine = line.split(' ')
        when (splitLine.first()) {
            "addx" -> Instruction.AddX(splitLine.last().toLong())
            "noop" -> Instruction.Noop
            else -> error("Unknown command $line")
        }
    }

    override fun part1(input: Input): Any {
        val instructions = input.toInstructions()
        val interestingCycles = setOf(20, 60, 100, 140, 180, 220)
        return runCPU(instructions)
            .sumOf { (cycle, register) ->
                if (cycle in interestingCycles) cycle * register
                else 0
            }
    }

    override fun part2(input: Input): Any {
        val instructions = input.toInstructions()
        return runCPU(instructions)
            .joinToString("") { (cycle, spriteIndex) ->
                val crtPixelIndex = (cycle - 1) % 40
                if (crtPixelIndex - spriteIndex in -1..1) "#"
                else "."
            }
            .chunked(40)
            .take(6)
            .joinToString("\n", prefix = "\n")
    }

    private fun runCPU(instructions: List<Instruction>): Sequence<Pair<Int, Long>> {
        var cycle = 1
        var x: Long = 1
        val program = instructions.toMutableList()
        return sequence {
            yield(cycle to x)

            while (program.isNotEmpty()) {
                when (val instruction = program.removeFirst()) {
                    is Instruction.AddX -> {
                        cycle++
                        yield(cycle to x)
                        x += instruction.value
                        cycle++
                    }

                    Instruction.Noop -> cycle++
                }

                yield(cycle to x)
            }
        }
    }

    private sealed class Instruction {
        object Noop : Instruction()
        class AddX(val value: Long): Instruction()
    }
}
