package co.danhill.aoc.year2023

import co.danhill.aoc.util.Day
import co.danhill.aoc.util.Input

fun main() = Day07.run()

object Day07 : Day(2023, 7) {
    // 249638405
    override fun part1(input: Input): Any {
        val handsToBids = input.lines.map { line ->
            val (handString, bidString) = line.split(' ')
            val hand = Hand.fromString(handString)
            val bid = bidString.toInt()
            hand to bid
        }

        return handsToBids
            .sortedBy { it.first }
            .withIndex()
            .sumOf { (i, pair) ->
                val (_, bid) = pair
                bid * (i + 1)
            }
    }

    override fun part2(input: Input): Any {
        val handsToBids = input.lines.map { line ->
            val (handString, bidString) = line.split(' ')
            val hand = Hand.fromString(handString, jokerRules = true)
            val bid = bidString.toInt()
            hand to bid
        }

        return handsToBids
            .sortedBy { it.first }
            .withIndex()
            .sumOf { (i, pair) ->
                val (_, bid) = pair
                bid * (i + 1)
            }
    }

    enum class Card(val char: Char) {
        JOKER('*'),

        TWO('2'),
        THREE('3'),
        FOUR('4'),
        FIVE('5'),
        SIX('6'),
        SEVEN('7'),
        EIGHT('8'),
        NINE('9'),
        TEN('T'),
        JACK('J'),
        QUEEN('Q'),
        KING('K'),
        ACE('A'),
        ;

        override fun toString(): String {
            return char.toString()
        }

        companion object {
            fun fromChar(char: Char, jokerRules: Boolean = false): Card {
                return if (jokerRules && char == 'J') JOKER
                else Card.values().first { it.char == char }
            }
        }
    }

    data class Hand(
        val cards: List<Card>,
        val jokerRules: Boolean = false
    ) : Comparable<Hand> {

        val cardsForComparison: List<Card> = run {
            if (jokerRules && cards.contains(Card.JOKER)) {
                val noJokers = cards.filter { it != Card.JOKER }
                val groups = noJokers.groupBy { it }
                if (groups.isEmpty()) {
                    cards.map { Card.ACE }
                } else {
                    val biggestGroupSize = groups.maxOf { it.value.size }
                    val bestCard = groups.filter { it.value.size == biggestGroupSize }
                        .map { it.key }
                        .sorted()
                        .first()

                    cards.map { if (it == Card.JOKER) bestCard else it }
                }
            } else {
                cards
            }
        }
        enum class Type {
            HIGH_CARD,
            ONE_PAIR,
            TWO_PAIR,
            THREE_OF_A_KIND,
            FULL_HOUSE,
            FOUR_OF_A_KIND,
            FIVE_OF_A_KIND,
            ;
        }

        val type: Type = run {
                val groups = cardsForComparison.groupBy { it }
                when (groups.size) {
                    5 -> Type.HIGH_CARD
                    4 -> Type.ONE_PAIR
                    3 -> {
                        when (groups.maxOf { it.value.size }) {
                            2 -> Type.TWO_PAIR
                            3 -> Type.THREE_OF_A_KIND
                            else -> error("Unknown type: $cards")
                        }
                    }

                    2 -> {
                        when (groups.maxOf { it.value.size }) {
                            3 -> Type.FULL_HOUSE
                            4 -> Type.FOUR_OF_A_KIND
                            else -> error("Unknown type: $cards")
                        }
                    }

                    1 -> Type.FIVE_OF_A_KIND
                    else -> error("idk, man")
                }
            }

        override fun toString(): String {
            return buildString {
                cards.forEach { c ->
                    append(c.char)
                }
            }
        }

        override fun compareTo(other: Hand): Int {
            val typeComparison = type.compareTo(other.type)
            return if (typeComparison == 0) {
                (cards.indices)
                    .map { cardIndex ->
                        cards[cardIndex].compareTo(other.cards[cardIndex])
                    }
                    .firstOrNull { it != 0 } ?: 0
            } else {
                typeComparison
            }
        }

        companion object {
            fun fromString(string: String, jokerRules: Boolean = false): Hand {
                return Hand(string.map { Card.fromChar(it, jokerRules) }, jokerRules)
            }
        }
    }
}
