package co.danhill.aoc.year2022

import co.danhill.aoc.util.*

fun main() = Day17.run()

object Day17 : Day(2022, 17) {

    override fun part1(input: Input): Any {
        val grid = gridOf<Char>().also { it.addFloor() }
        val jetPatterns = input.text
        var currentRock: Rock? = null
        var rocksAdded = 0
        for (i in 0..Int.MAX_VALUE) {
            if (currentRock == null) {
                currentRock = grid.spawnNextRock()
            }

            println("Rocks added $rocksAdded, i = $i")
//            grid.printEmBois(currentRock)
//            println()

            if (i % 2 == 0) {
                // jetstream turn
                when (jetPatterns.getNextJetPattern()) {
                    '<' -> {
                        val nextPosition = currentRock.position.left
                        if (nextPosition.x > 0) {
                            currentRock.position = nextPosition
                        }
                    }

                    '>' -> {
                        //
                        val nextPosition = currentRock.position.right
                        if (nextPosition.x + currentRock.shape.width < 8) {
                            currentRock.position = nextPosition
                        }
                    }

                    else -> error("Unknown jet pattern")
                }
            } else {
                // fall down turn
                val nextPosition = currentRock.position.down
                val rowToConsider = currentRock.position.y + currentRock.shape.height
                val collides = (0..currentRock.shape.width).any { grid[it to nextPosition.y + currentRock!!.shape.height] == '#' }

                if (collides) {
                    // collision! add rock at current position
                    grid.addRock(currentRock)
                    rocksAdded++
                    if (rocksAdded > 2021) return grid.maxY
                    currentRock = null
                } else {
                    currentRock.position = nextPosition
                }
            }
        }

        println("Finished")
        grid.printEmBois(currentRock ?: grid.spawnNextRock())
        return grid.maxY
    }

    override fun part2(input: Input): Any {
        TODO()
    }

    private fun Grid<Char>.addFloor() {
        for (x in 0..7) {
            this[x to 0] = '#'
        }
    }

    private fun Grid<Char>.addRock(rock: Rock) {
        val (topLeftX, topLeftY) = rock.position
        for (y in 0..rock.shape.height) {
            for (x in 0..rock.shape.width) {
                if (rock.shape[x to y] == '#') {
                    this[topLeftX + x to topLeftY + y] = '#'
                }
            }
        }
    }

    private fun Grid<Char>.printEmBois(currentRock: Rock) {
        val minX = 0
        val minY = listOf(
            this.minY,
            currentRock.position.y - currentRock.shape.height
        ).min()
        val maxX = 8
        val maxY = 0

        for (y in minY..maxY) {
            for (x in minX..maxX) {
                val gridValue = this[x to y]
                val c = when {
                    x == 0 && y == 0 -> '+'
                    x == 8 && y == 0 -> '+'
                    x == 0 -> '|'
                    x == 8 -> '|'
                    y == 0 -> '-'
                    else -> {
//                        val translatedX = x - currentRock.position.x
//                        val translatedY = y - currentRock.position.y
                        val rockValue = currentRock.get(x to y).let { // [translatedX to translatedY].let {
                            if (it == '#') '@' else it
                        }
                        gridValue ?: rockValue ?: '.'
                    }
                }
                print(c)
            }
            println()
        }
    }

    private var _rockIndex = 0
    private fun Grid<Char>.spawnNextRock(): Rock {
        val shape = Shape.values[_rockIndex]
        _rockIndex++
        _rockIndex %= Shape.values.size
        return Rock(
            shape = shape,
            position = 3 to this.minY - 4 - shape.height,
        )
    }

    private var _jetPatternIndex = 0
    private fun String.getNextJetPattern(): Char {
        val char = this[_jetPatternIndex]
        _jetPatternIndex++
        _jetPatternIndex %= length
        return char
    }

    private class Rock(
        val shape: Shape,
        var position: Point,
    ) {
        fun get(point: Point): Char? {
            val translatedX = point.x - position.x
            val translatedY = point.y - position.y
            return shape.display[translatedX to translatedY]
        }
    }

    private enum class Shape(val display: Grid<Char>) : Grid<Char> by display {
        DASH(
            listOf(
                "####"
            ).toCharGrid()
        ),
        PLUS(
            listOf(
                ".#.",
                "###",
                ".#.",
            ).toCharGrid()
        ),
        L_BACKWARDS(
            listOf(
                "..#",
                "..#",
                "###",
            ).toCharGrid()
        ),
        I(
            listOf(
                "#",
                "#",
                "#",
                "#",
            ).toCharGrid(),
        ),
        SQUARE(
            listOf(
                "##",
                "##",
            ).toCharGrid()
        ),
        ;

        val width: Int
            get() = display.maxX

        val height: Int
            get() = display.maxY

        companion object {
            val values = values()
        }
    }
}
