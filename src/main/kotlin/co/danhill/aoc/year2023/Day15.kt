package co.danhill.aoc.year2023

import co.danhill.aoc.util.*

fun main() = Day15.run("2023/15.txt")

object Day15 : Day {
    override fun part1(input: Input): Any {
        return input.lines.single()
            .split(',')
            .sumOf { step -> step.hash() }
    }

    override fun part2(input: Input): Any {
        val boxes = MutableList<MutableList<Pair<String, Int>>>(size = 256) {
            mutableListOf()
        }
        input.lines.single()
            .split(',')
            .forEach { step ->
                val isDash = step.last() == '-'
                val indexOfCommand = if (isDash) step.length - 1 else step.indexOf('=')
                val label = step.subSequence(0, indexOfCommand)
                val box = label.hash()
                if (isDash) {
                    boxes[box].removeIf { (lbl, _) -> lbl == label }
                } else {
                    val focalLength = step.subSequence(indexOfCommand + 1, step.length).toString().toInt()
                    val lenses = boxes[box]
                    val match = lenses.indexOfFirst { (lbl, _) -> lbl == label }
                    val newLens = label.toString() to focalLength
                    if (match >=0) lenses[match] = newLens
                    else lenses += newLens
                }
            }
        return boxes.withIndex().sumOf { (boxIndex, box) ->
            box.withIndex().sumOf { (lensIndex, lens) ->
                (boxIndex + 1) * (lensIndex + 1) * lens.second
            }
        }
    }

    private fun CharSequence.hash(): Int {
        var currentValue = 0
        for (char in this) {
            currentValue += char.code
            currentValue *= 17
            currentValue %= 256
        }
        return currentValue
    }
}
