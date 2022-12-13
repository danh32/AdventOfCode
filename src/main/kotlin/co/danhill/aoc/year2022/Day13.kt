package co.danhill.aoc.year2022

import co.danhill.aoc.util.Day
import co.danhill.aoc.util.Input
import co.danhill.aoc.util.groupedText
import co.danhill.aoc.util.lines

fun main() = Day13.run("2022/13.txt")

object Day13 : Day {

    override fun part1(input: Input): Any {
        return input.groupedText
            .map { group ->
                val (first, second) = group.split('\n')
                first.toPacketData() to second.toPacketData()
            }
            .foldRightIndexed(0) { index, pair, acc ->
                val (left, right) = pair
                acc + if (left.compareTo(right) < 1) index + 1 else 0
            }
    }

    override fun part2(input: Input): Any {
        val dividerPackets = listOf("[[2]]".toPacketData(), "[[6]]".toPacketData())
        return input.lines
            .filter { it.isNotEmpty() }
            .map { it.toPacketData() }
            .plus(dividerPackets)
            .sorted()
            .foldRightIndexed(1) { index, packetData, acc ->
                acc * if (packetData in dividerPackets) index + 1 else 1
            }
    }

    private sealed class PacketData : Comparable<PacketData> {
        class List(val list: kotlin.collections.List<PacketData>) : PacketData()
        class Integer(val value: Int) : PacketData() {
            fun toList(): PacketData.List = PacketData.List(listOf(this))
        }

        override fun compareTo(other: PacketData): Int = compare(this, other)
        override fun toString(): String = when (this) {
            is Integer -> value.toString()
            is List -> list.joinToString(",", "[", "]") { it.toString() }
        }
    }

    private fun compare(left: PacketData, right: PacketData): Int {
        return when (left) {
            is PacketData.Integer -> when (right) {
                is PacketData.Integer -> left.value.compareTo(right.value)
                is PacketData.List -> compare(left.toList(), right)
            }

            is PacketData.List -> when (right) {
                is PacketData.Integer -> compare(left, right.toList())
                is PacketData.List -> {
                    for (i in 0 until minOf(left.list.size, right.list.size)) {
                        when (val comparison = compare(left.list[i], right.list[i])) {
                            0 -> continue
                            else -> return comparison
                        }
                    }
                    left.list.size.compareTo(right.list.size)
                }
            }
        }
    }

    private fun String.toPacketData(): PacketData = toPacketDataInternal(0).first

    private fun String.toPacketDataInternal(startIndex: Int): Pair<PacketData, Int> {
        var currentList: MutableList<PacketData>? = null
        var i = startIndex
        while (i < length) {
            when (this[i]) {
                // start new list
                '[' -> {
                    if (currentList == null) {
                        currentList = mutableListOf()
                    } else {
                        // consume sub-packet and advance index to the end
                        val (subPacket, endIndex) = toPacketDataInternal(i)
                        currentList += subPacket
                        i = endIndex
                    }
                }
                // finish list
                ']' -> return PacketData.List(currentList!!) to i
                ',' -> {}
                // add Integer to list
                else -> {
                    var nextNonDigitIndex = i
                    while (this[nextNonDigitIndex].isDigit()) {
                        nextNonDigitIndex++
                    }
                    val integer = PacketData.Integer(this.substring(i until nextNonDigitIndex).toInt())
                    currentList!! += integer
                    i = nextNonDigitIndex - 1
                }
            }

            i++
        }

        return PacketData.List(currentList!!) to length
    }
}
