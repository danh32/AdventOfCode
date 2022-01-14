package co.danhill.aoc.util

typealias Point = Pair<Int, Int>

val Point.x: Int
    get() = first

val Point.y: Int
    get() = second

val Point.upLeft: Point
    get() = x - 1 to y - 1

val Point.up: Point
    get() = x to y - 1

val Point.upRight: Point
    get() = x + 1 to y - 1

val Point.left: Point
    get() = x - 1 to y

val Point.right: Point
    get() = x + 1 to y

val Point.downLeft: Point
    get() = x - 1 to y + 1

val Point.down: Point
    get() = x to y + 1

val Point.downRight: Point
    get() = x + 1 to y + 1

val Point.cardinalNeighbors: List<Point>
    get() = listOf(left, up, right, down)

val Point.ordinalNeighbors: List<Point>
    get() = listOf(upLeft, upRight, downLeft, downRight)

val Point.neighbors: List<Point>
    get() = listOf(
        upLeft,
        up,
        upRight,
        left,
        right,
        downLeft,
        down,
        downRight
    )
