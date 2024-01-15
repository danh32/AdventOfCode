package co.danhill.aoc.year2023

import co.danhill.aoc.util.*

fun main() = Day19.run()

typealias Part = MutableMap<Char, Int>

object Day19 : Day(2023, 19) {
    override fun part1(input: Input): Any {
        val (rulesGroup, partsGroup) = input.groupedText
        val workflows = rulesGroup.split('\n')
            .map { line ->
                val name = line.substringBefore('{')
                val stepsString = line.substringAfter('{').dropLast(1)
                val rules = stepsString.split(',')
                    .map { stepString ->
                        val isDynamic = stepString.contains(':')
                        if (isDynamic) {
                            val amount = stepString.substring(2)
                                .takeWhile { it.isDigit() }
                                .toInt()
                            val result = stepString.substringAfter(':')
                            Rule.Dynamic(
                                value = stepString.first(),
                                operator = stepString[1],
                                amount = amount,
                                result = result,
                            )
                        } else {
                            Rule.Static(stepString)
                        }
                    }
                Workflow(name, rules)
            }
        val parts = partsGroup.split('\n')
            .map { line ->
                line.substring(1, line.length - 1)
                    .split(',')
                    .fold(mutableMapOf<Char, Int>()) { part, str ->
//                        println(str)
                        part[str[0]] = str.substring(2, str.length).toInt()
                        part
                    }
            }
        val acceptedParts = parts.filter { part ->
//            println("Part x=${part['x']}")
            val accept = workflows.accept(part)
//            println(if (accept) "\tA" else "\tR")
            accept
        }
        return acceptedParts.sumOf { it['x']!! + it['m']!! + it['a']!! + it['s']!! }
    }

    override fun part2(input: Input): Any {
        return ""
    }

    private fun List<Workflow>.accept(part: Part): Boolean {
        var workflow: Workflow? = first { it.name == "in" }
        while (workflow != null) {
//            println("\t${workflow.name}")
            when (val result = workflow.apply(part)) {
                "R" -> return false
                "A" -> return true
                else -> {
                    workflow = first { it.name == result }
                }
            }
        }
        return true
    }

    data class Workflow(
        val name: String,
        val rules: List<Rule>
    ) {
        fun apply (part: Part): String {
            for (rule in rules) {
                val result = rule.apply(part)
//                println("\t\t$rule ---> $result")
                if (result != null) return result
            }
            return "A"
        }

        override fun toString(): String {
            return "$name{${rules.joinToString(",")}}"
        }
    }

    sealed class Rule {
        abstract fun apply(part: Part): String?

        data class Static(val result: String) : Rule() {
            override fun apply(part: Part): String = result
        }

        data class Dynamic(
            val value: Char,
            val operator: Char,
            val amount: Int,
            val result: String,
        ): Rule() {
            override fun apply(part: Part): String? {
                val partAmount = part.getValue(value)
                val matched = when (operator) {
                    '>' -> partAmount > amount
                    '<' -> partAmount < amount
                    else -> error("Unknown operator $operator")
                }
//                println("\t\t\t$partAmount ($value) $operator $amount : $result")
                return if (matched) {
                    result
                } else null
            }

            override fun toString(): String {
                return "$value$operator$amount:$result"
            }
        }
    }

}
