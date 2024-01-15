package co.danhill.aoc.year2022

import co.danhill.aoc.util.Day
import co.danhill.aoc.util.Input

fun main() = Day21.run()

object Day21 : Day(2022, 21) {

    private fun Input.parse() = lines.map { line ->
        val name = line.substring(0 until 4)
        val operationString = line.substring(6 until line.length)
        val scalar = operationString.toLongOrNull()
        if (scalar != null) {
            Monkey.Resolved(name, scalar)
        } else {
            val left = operationString.substring(0 until 4)
            val operandSymbol = operationString[5]
            val right = operationString.substring(7 until operationString.length)
            Monkey.Unresolved(
                name,
                left,
                right,
                Operand.from(operandSymbol),
            )
        }
    }

    override fun part1(input: Input): Any {
        val result = input.parse()
            .associateByTo(mutableMapOf(), Monkey::name)
            .resolve("root")
        return (result as Result.Number).number
    }

    override fun part2(input: Input): Any {
        val monkeys = input.parse().associateByTo(mutableMapOf(), Monkey::name)
        monkeys.remove("humn")
        val root = monkeys.getValue("root")
        if (root !is Monkey.Unresolved) error("Impossible inputs - root monkey already resolved")
        val left = monkeys.resolve(root.leftMonkeyName).evaluate()
        val right = monkeys.resolve(root.rightMonkeyName).evaluate()

        var (targetNumber, result) = when (left) {
            Result.Human -> error("Human too high up.")
            is Result.Number -> left.number to right
            is Result.Operation -> when (right) {
                Result.Human -> error("Human too high up.")
                is Result.Number -> right.number to left
                is Result.Operation -> error("Two is too many humans.")
            }
        }

        while (result is Result.Operation) {
            val pair = result.reduce(targetNumber)
            targetNumber = pair.first
            result = pair.second
        }

        return targetNumber
    }

    private fun MutableMap<String, Monkey>.resolve(name: String): Result {
        return when (val toResolve = this[name]) {
            null -> if (name == "humn") Result.Human else error("Couldn't find monkey $name")
            is Monkey.Resolved -> Result.Number(toResolve.number)
            is Monkey.Unresolved -> Result.Operation(
                toResolve.operand,
                resolve(toResolve.leftMonkeyName).evaluate(),
                resolve(toResolve.rightMonkeyName).evaluate(),
            ).evaluate()
        }
    }

    private enum class Operand(val symbol: Char) {
        ADD('+'),
        SUBTRACT('-'),
        MULTIPLY('*'),
        DIVIDE('/'),
        ;

        fun evaluate(left: Long, right: Long) = when (this) {
            ADD -> left + right
            SUBTRACT -> left - right
            MULTIPLY -> left * right
            DIVIDE -> left / right
        }

        companion object {
            private val values = values()
            fun from(symbol: Char): Operand = values.find { it.symbol == symbol } ?: error("Unknown symbol $symbol")
        }
    }

    private sealed class Monkey(val name: String) {
        class Resolved(name: String, val number: Long) : Monkey(name)

        class Unresolved(
            name: String,
            val leftMonkeyName: String,
            val rightMonkeyName: String,
            val operand: Operand,
        ) : Monkey(name)
    }

    private sealed class Result {
        class Operation(val operand: Operand, val left: Result, val right: Result) : Result() {
            override fun evaluate(): Result = when (left) {
                Human -> when (right) {
                    Human -> this
                    is Number -> this
                    is Operation -> Operation(operand, left, right.evaluate())
                }

                is Number -> when (right) {
                    Human -> this
                    is Number -> Number(operand.evaluate(left.number, right.number))
                    is Operation -> Operation(operand, left, right.evaluate())
                }

                is Operation -> when (right) {
                    Human -> Operation(operand, left.evaluate(), right)
                    is Number -> Operation(operand, left.evaluate(), right)
                    is Operation -> Operation(operand, left.evaluate(), right.evaluate())
                }
            }

            fun reduce(target: Long): Pair<Long, Result> {
                return when (left) {
                    Human -> when (right) {
                        Human -> error("Two is too many humans.")
                        is Operation -> error("The right operation should be simplified already.")
                        is Number -> when (operand) {
                            Operand.ADD -> (target - right.number) to left
                            Operand.SUBTRACT -> (target + right.number) to left
                            Operand.MULTIPLY -> (target / right.number) to left
                            Operand.DIVIDE -> (target * right.number) to left
                        }
                    }
                    is Number -> when (right) {
                        is Number -> error("This operation should be simplified already.")
                        Human,
                        is Operation -> when (operand) {
                            Operand.ADD -> (target - left.number) to right
                            Operand.SUBTRACT -> (left.number - target) to right
                            Operand.MULTIPLY -> (target / left.number) to right
                            Operand.DIVIDE -> (left.number / target) to right
                        }
                    }
                    is Operation -> when (right) {
                        Human -> error("The left operation should be simplified already.")
                        is Operation -> error("One of these two operations should be simplified already.")
                        is Number -> when (operand) {
                            Operand.ADD -> (target - right.number) to left
                            Operand.SUBTRACT -> (target + right.number) to left
                            Operand.MULTIPLY -> (target / right.number) to left
                            Operand.DIVIDE -> (target * right.number) to left
                        }
                    }
                }
            }
        }

        class Number(val number: Long) : Result()
        object Human : Result()

        open fun evaluate(): Result = this

        override fun toString(): String = when (this) {
            Human -> "humn"
            is Number -> number.toString()
            is Operation -> "($left ${operand.symbol} $right)"
        }
    }
}
