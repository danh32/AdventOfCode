package co.danhill.aoc.year2023

import co.danhill.aoc.util.Day
import co.danhill.aoc.util.Input

fun main() = Day04.run()

object Day04 : Day(2023, 4) {
    override fun part1(input: Input): Any {
        return input.toCards().sumOf { it.points }
    }

    override fun part2(input: Input): Any {
        val originalCards = input.toCards().associateBy { it.id }
        val workingCardIdsToCount = originalCards.mapValues { 1 }.toMutableMap()
        var processedCardsCount = 0
        while (workingCardIdsToCount.isNotEmpty()) {
            val cardId = workingCardIdsToCount.keys.min()
            val cardCount = workingCardIdsToCount.remove(cardId)!!
            val card = originalCards[cardId]!!
            println("Card $cardId x $cardCount wins ${card.awardedIds}")
            for (wonCardId in card.awardedIds) {
                workingCardIdsToCount.compute(wonCardId) { _, current -> (current ?: 0) + cardCount }
            }
            processedCardsCount += cardCount
        }
        return processedCardsCount
    }

    private fun Input.toCards(): List<Card> {
        return lines.map { line ->
            val (cardId, numbers) = line.split(": ")
            val id = cardId.removePrefix("Card ").trim().toInt()
            val (winningNumbers, ownedNumbers) = numbers.split(" | ")
            val winning = winningNumbers.split(" ")
                .mapNotNull { it.trim().toIntOrNull() }
            val owned = ownedNumbers.split(" ")
                .mapNotNull { it.trim().toIntOrNull() }
            Card(
                id = id,
                winningNumbers = winning,
                revealedNumbers = owned,
            )
        }
    }

    private data class Card(
        val id: Int,
        val winningNumbers: List<Int>,
        val revealedNumbers: List<Int>,
    ) {
        val matches = revealedNumbers
            .filter { winningNumbers.contains(it) }

        val points: Int = matches.fold(0) { acc, _ ->
            if (acc == 0) 1 else acc * 2
        }

        val awardedIds = (id + 1)..(id + matches.size)
    }
}
