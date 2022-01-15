package co.danhill.aoc.util

import java.util.PriorityQueue
import kotlin.math.absoluteValue

fun <T> Grid<T>.findPath(
    start: Point,
    end: Point,
    movementCost: (Point) -> Int,
    heuristic: Heuristic = Heuristic.ManhattanDistance,
): List<Point> {
    val endNode = aStarSearch(start, end, movementCost, heuristic)
    val reversePath = mutableListOf<Point>()
    var current: Node? = endNode
    while (current != null) {
        reversePath.add(current.point)
        current = current.parent
    }
    return reversePath.reversed()
}

private fun <T> Grid<T>.aStarSearch(
    start: Point,
    end: Point,
    movementCost: (Point) -> Int,
    heuristic: Heuristic = Heuristic.ManhattanDistance,
): Node {
    val open = PriorityQueue<Node>()
    open += Node(start).apply {
        g = 0
        h = heuristic.distance(start, end)
    }
    val closed = mutableMapOf<Point, Node>()
    while (open.isNotEmpty()) {
        val bestCandidate = open.remove()
        if (bestCandidate.point == end) {
            return bestCandidate
        }

        bestCandidate.point
            .cardinalNeighbors
            .filter { neighbor ->
                // neighbor must be on the board, and not just a step backwards
                containsKey(neighbor) && neighbor != bestCandidate.parent?.point
            }
            .map { point ->
                Node(point).apply {
                    parent = bestCandidate
                    g = bestCandidate.g + movementCost(point)
                    h = heuristic.distance(point, end)
                }
            }
            .forEach { candidate ->
                open.removeIf { it.point == candidate.point && it.f > candidate.f }
                closed[candidate.point]?.let{ previouslyClosed ->
                    if (candidate.f < previouslyClosed.f) {
                        closed.remove(candidate.point)
                    }
                }
                if (!open.contains(candidate) && !closed.contains(candidate.point)) {
                    open.add(candidate)
                }
            }
        closed.addBest(bestCandidate)
    }
    error("End never found")
}

private data class Node(
    val point: Point,
) : Comparable<Node> {
    var parent: Node? = null
    var g: Int = 0
    var h: Int = 0

    val f: Int
        get() = g + h

    override fun compareTo(other: Node): Int {
        return f - other.f
    }
}

private fun MutableMap<Point, Node>.addBest(node: Node) {
    val currentF = get(node.point)?.f ?: Int.MAX_VALUE
    if (node.f < currentF) {
        set(node.point, node)
    }
}

sealed class Heuristic(
    val distance: (Point, Point) -> Int,
) {

    object ManhattanDistance : Heuristic(
        distance = { start: Point, end: Point ->
            (start.x - end.x).absoluteValue + (start.y - end.y).absoluteValue
        }
    )
}
