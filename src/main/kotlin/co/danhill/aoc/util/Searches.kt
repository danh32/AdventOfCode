package co.danhill.aoc.util

import java.util.PriorityQueue

object Search {
    fun <T> aStar(
        start: T,
        isEnd: (T) -> Boolean,
        generateNextStates: (T) -> List<T>,
        movementCost: (from: T, to: T) -> Int,
        heuristicCostToEndState: (from: T) -> Int,
    ): List<T> {
        val endNode = findEnd(start, isEnd, generateNextStates, movementCost, heuristicCostToEndState)
        val reversePath = mutableListOf<T>()
        var current: Node<T>? = endNode
        while (current != null) {
            reversePath.add(current.data)
            current = current.parent
        }
        return reversePath.reversed()
    }

    private fun <T> findEnd(
        start: T,
        isEnd: (T) -> Boolean,
        generateNextStates: (T) -> List<T>,
        movementCost: (from: T, to: T) -> Int,
        heuristicCostToEndState: (from: T) -> Int,
    ): Node<T>? {
        val open = PriorityQueue<Node<T>>(250)
        open += Node(
            data = start,
            g = 0,
            h = heuristicCostToEndState(start),
        )
        val closed = mutableMapOf<T, Int>()
        while (open.isNotEmpty()) {
            val bestCandidate = open.remove()
            if (isEnd(bestCandidate.data)) {
                return bestCandidate
            }

            generateNextStates(bestCandidate.data)
                // don't step backwards
                .filter { it != bestCandidate.parent?.data }
                .mapNotNull { nextCandidate ->
                    val cost = movementCost(bestCandidate.data, nextCandidate)
                    if (cost == Int.MAX_VALUE) null
                    else Node(
                        data = nextCandidate,
                        g = bestCandidate.g + cost,
                        h = heuristicCostToEndState(nextCandidate),
                    ).apply {
                        parent = bestCandidate
                    }
                }
                .forEach { candidate ->
                    // remove from consideration any states identical to this one, but more costly
                    open.removeIf { it.data == candidate.data && it.f > candidate.f }
                    // reopen the state if we've found a cheaper way to get there
                    closed[candidate.data]?.let { previouslyClosedF ->
                        if (candidate.f < previouslyClosedF) {
                            closed.remove(candidate.data)
                        }
                    }
                    // open to consideration if not already open and not already closed
                    if (!open.contains(candidate) && !closed.contains(candidate.data)) {
                        open.add(candidate)
                    }
                }

            // finally, close the point
            closed.addBest(bestCandidate)
        }
        // no path found
        return null
    }
}

private data class Node<T>(
    val data: T,
    val g: Int = 0,
    val h: Int = 0,
) : Comparable<Node<T>> {
    var parent: Node<T>? = null

    val f: Int
        get() = g + h

    override fun compareTo(other: Node<T>): Int {
        return f - other.f
    }
}

private fun <T> MutableMap<T, Int>.addBest(node: Node<T>) {
    val currentF = get(node.data) ?: Int.MAX_VALUE
    if (node.f < currentF) {
        set(node.data, node.f)
    }
}

sealed class Heuristic(
    val distance: (Point, Point) -> Int,
) {

    object ManhattanDistance : Heuristic(
        distance = { start: Point, end: Point ->
            start.manhattanDistanceTo(end)
        }
    )
}
