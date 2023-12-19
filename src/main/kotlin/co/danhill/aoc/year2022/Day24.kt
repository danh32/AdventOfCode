package co.danhill.aoc.year2022

import co.danhill.aoc.util.*

fun main() = Day24.run("2022/24.txt")

object Day24 : Day {
    private fun Input.parse() = lines.toCharGrid()

    override fun part1(input: Input): Any {
        val initialGrid = input.parse()
        val blizzards = initialGrid.toBlizzards()

        val (pathSize, _) = betterGetPath(
            initialGrid = initialGrid,
            initialState = State(1 to 0, blizzards),
            target = initialGrid.maxX - 1 to initialGrid.maxY,
        )

        return pathSize
    }

    override fun part2(input: Input): Any {
        val initialGrid = input.parse()
        val blizzards = initialGrid.toBlizzards()
        val start = 1 to 0
        val end = initialGrid.maxX - 1 to initialGrid.maxY

        var sizeSoFar = 0;
        var lastState = State(start, blizzards)
        val cycles = 3
        for (i in 0 until cycles) {
            val target = if (i % 2 == 0) end else start
            val (pathSize, finalState) = betterGetPath(
                initialGrid = initialGrid,
                initialState = lastState,
                target = target
            )
            sizeSoFar += pathSize
            lastState = finalState
        }

        return sizeSoFar
    }

    private fun betterGetPath(
        initialGrid: Grid<Char>,
        initialState: State,
        target: Point,
    ): Pair<Int, State> {
        val generateNextStates: (State, Sequence<State>) -> List<State> = { state, _ ->
            val (position, blizzards) = state
            val nextBlizzards = blizzards.map { it.step(initialGrid) }
            state.blizzards = emptyList() // OOM avoidance!
            (position.cardinalNeighbors + position).mapNotNull { candidate ->
                when {
                    initialGrid[candidate] != '.' -> null
                    nextBlizzards.find { it.point == candidate } != null -> null
                    else -> State(candidate, nextBlizzards)
                }
            }
        }
        val movementCost: (State, State) -> Int = { _, _ -> 1 }
        val heuristicCostToEndState: (State) -> Int = { from -> from.position.manhattanDistanceTo(target) }

        val path = Search.aStar(
            start = initialState,
            isEnd = { it.position == target },
            generateNextStates = generateNextStates,
            movementCost = movementCost,
            heuristicCostToEndState = heuristicCostToEndState,
        )
        return path.size - 1 to path.last()
    }

    private fun Grid<Char>.toBlizzards() = mapNotNull { (p, c) ->
        when (c) {
            '.', '#' -> null
            '<' -> Direction.LEFT
            '^' -> Direction.UP
            '>' -> Direction.RIGHT
            'v' -> Direction.DOWN
            else -> error("Unknown char $c")
        }?.let { direction ->
            this[p] = '.'
            Blizzard(p, direction)
        }
    }

    private data class Blizzard(
        val point: Point,
        val direction: Direction,
    ) {
        fun step(grid: Grid<Char>): Blizzard {
            val candidate = point.step(direction)
            val next = when (val c = grid[candidate]) {
                '.' -> candidate
                '#' -> when (direction) {
                    Direction.LEFT -> (grid.maxX - 1) to candidate.y
                    Direction.UP -> candidate.x to (grid.maxY - 1)
                    Direction.RIGHT -> 1 to candidate.y
                    Direction.DOWN -> candidate.x to 1
                }

                else -> error("Unknown char $c")
            }
            return Blizzard(next, direction)
        }
    }

    private data class State(val position: Point, var blizzards: List<Blizzard>)
}
