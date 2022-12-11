package co.danhill.aoc.year2022

import co.danhill.aoc.util.Day
import co.danhill.aoc.util.Input
import co.danhill.aoc.util.groupedText

fun main() = Day11.run("2022/11.txt")

object Day11 : Day {

    private fun Input.toMonkeys() = this.groupedText
        .map { group ->
            val lines = group.split('\n')
            val id = lines[0].split(' ').last().dropLast(1).toInt()
            val items = lines[1].removePrefix("  Starting items: ").split(", ").map { it.toLong() }
            val operationText = lines[2].removePrefix("  Operation: new = old ").split(' ')
            val operation = when {
                operationText[0] == "+" ->
                    if (operationText[1] == "old") Operation.AddSelf
                    else Operation.Add(operationText[1].toInt())
                operationText[0] == "*" ->
                    if (operationText[1] == "old") Operation.MultiplyBySelf
                    else Operation.Multiply(operationText[1].toInt())
                else -> error("Unknown operation $operationText")
            }
            val testDivisor = lines[3].split(' ').last().toInt()
            val trueMonkeyId = lines[4].split(' ').last().toInt()
            val falseMonkeyId = lines[5].split(' ').last().toInt()
            val test = Test(testDivisor, trueMonkeyId, falseMonkeyId)
            Monkey(id, items.toMutableList(), operation, test)
        }

    override fun part1(input: Input): Any {
        return simulate(input.toMonkeys(), 20) { it / 3 }
    }

    override fun part2(input: Input): Any {
        val monkeys = input.toMonkeys()
        val magicFuckery = monkeys.fold(1L) { acc, monkey -> acc * monkey.test.divisor }
        return simulate(monkeys, 10_000) { it % magicFuckery }
    }

    private fun simulate(monkeys: List<Monkey>, rounds: Int, reducer: (Long) -> Long): Long {
        val inspectionCounts = mutableMapOf<Int, Long>().withDefault { 0 }
        for (round in 0 until rounds) {
            monkeys.forEach { monkey ->
                while (monkey.items.isNotEmpty()) {
                    inspectionCounts[monkey.id] = inspectionCounts.getValue(monkey.id) + 1
                    val item = monkey.items.removeFirst()
                    val modified = reducer(monkey.operation.run(item))
                    val targetMonkey = monkeys[monkey.test.targetId(modified)]
                    targetMonkey.items += modified
                }
            }
        }

        return inspectionCounts.values.sortedDescending().take(2).reduce { a, b -> a * b }
    }

    private class Monkey(
        val id: Int,
        val items: MutableList<Long>,
        val operation: Operation,
        val test: Test,
    ) {
        override fun toString() = "Monkey $id: ${items.joinToString(", ")}"
    }

    private sealed class Operation {

        abstract fun run(input: Long): Long

        class Add(val value: Int) : Operation() {
            override fun run(input: Long): Long = input + value
        }
        object AddSelf : Operation() {
            override fun run(input: Long): Long = input + input
        }
        class Multiply(val value: Int) : Operation() {
            override fun run(input: Long): Long = input * value
        }
        object MultiplyBySelf : Operation() {
            override fun run(input: Long): Long = input * input
        }
    }

    private class Test(
        val divisor: Int,
        val trueMonkeyId: Int,
        val falseMonkeyId: Int,
    ) {
        fun targetId(input: Long): Int = if (passes(input)) trueMonkeyId else falseMonkeyId
        fun passes(input: Long): Boolean = input % divisor == 0L
    }
}
