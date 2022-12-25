package co.danhill.aoc.year2022

import co.danhill.aoc.util.Day
import co.danhill.aoc.util.Input
import co.danhill.aoc.util.lines
import java.math.BigInteger

fun main() = Day25.run("2022/25.txt")

object Day25 : Day {

    private val snafuToDecimalMap = mapOf(
        '=' to (-2).toBigInteger(),
        '-' to (-1).toBigInteger(),
        '0' to BigInteger.ZERO,
        '1' to BigInteger.ONE,
        '2' to 2.toBigInteger(),
    )

    // base 5 digit (adjusted) to char and whether to carry-over
    private val base5ToSnafuMap = mapOf(
        0 to ('0' to false),
        1 to ('1' to false),
        2 to ('2' to false),
        3 to ('=' to true),
        4 to ('-' to true),
        5 to ('0' to true),
    )

    override fun part1(input: Input): Any {
        return input.lines
            .sumOf { it.snafuToDecimal() }
            .toSnafu()
    }

    override fun part2(input: Input): Any {
        TODO("Not yet implemented")
    }

    private fun String.snafuToDecimal(): BigInteger {
        return reversed()
            .mapIndexed { index, c ->
                snafuToDecimalMap[c]!! * 5.toBigInteger().pow(index)
            }
            .sumOf { it }
    }

    private fun BigInteger.toSnafu(): String {
        val (sb, carryover) = toString(5).foldRight(StringBuilder(20) to false) { c, (sb, carryover) ->
            val adjusted = c.digitToInt() + if (carryover) 1 else 0
            val (toAppend, carry) = base5ToSnafuMap[adjusted]!!
            sb.append(toAppend)
            sb to carry
        }
        if (carryover) sb.append('1')
        return sb.reversed().toString()
    }
}
