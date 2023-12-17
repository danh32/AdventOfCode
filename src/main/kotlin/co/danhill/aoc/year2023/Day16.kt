package co.danhill.aoc.year2023

import co.danhill.aoc.util.*

fun main() = Day16.run("2023/16.txt")

object Day16 : Day {
    override fun part1(input: Input): Any {
        return input.lines.toCharGrid().energy()
    }

    override fun part2(input: Input): Any {
        val grid = input.lines.toCharGrid()
        return listOf(
            (grid.minX..grid.maxX).map { x -> Beam(x to 0, Direction.DOWN) },
            (grid.minY..grid.maxY).map { y -> Beam(0 to y, Direction.RIGHT) },
            (grid.minY..grid.maxY).map { y -> Beam(grid.maxX to y, Direction.LEFT) },
            (grid.minX..grid.maxX).map { x -> Beam(x to grid.maxY, Direction.UP) },
        )
            .flatten()
            .maxOf { grid.energy(it.point, it.direction) }
    }

    data class Beam(
        val point: Point,
        val direction: Direction
    )

    private fun Grid<Char>.energy(
        startPoint: Point = 0 to 0,
        direction: Direction = Direction.RIGHT,
    ): Int {
        val visited = mutableSetOf<Beam>()
        val energy = gridOf { '.' }
        val beamEdges = mutableListOf(Beam(startPoint, direction))
        while (beamEdges.isNotEmpty()) {
            val beam = beamEdges.removeFirst()
            visited += beam
            if (this.contains(beam.point)) {
                energy[beam.point] = '#'
                val nextPoints = this.getNextBeam(beam)
//                println("${beam.point}, ${beam.direction} -> ${nextPoints.map { it.point }}")
                beamEdges += nextPoints.filter { !visited.contains(it) }
            }
//            for (y in grid.minY..grid.maxY) {
//                for (x in grid.minX..grid.maxX) {
//                    print(energy[x to y] ?: grid[x to y])
//                }
//                println()
//            }
        }

        return energy.size
    }

    private fun Grid<Char>.getNextBeam(beam: Beam): List<Beam> {
        val nextBeams = mutableListOf<Beam>()
        val nextPoint = beam.point.step(beam.direction)
        when (val nextTile = get(nextPoint)) {
            '.' -> nextBeams += Beam(nextPoint, beam.direction)
            '|' -> when (beam.direction) {
                Direction.UP,
                Direction.DOWN -> nextBeams += Beam(nextPoint, beam.direction)
                Direction.LEFT,
                Direction.RIGHT -> {
                    nextBeams += Beam(nextPoint, Direction.UP)
                    nextBeams += Beam(nextPoint, Direction.DOWN)
                }
            }
            '-' -> when (beam.direction) {
                Direction.UP,
                Direction.DOWN -> {
                    nextBeams += Beam(nextPoint, Direction.LEFT)
                    nextBeams += Beam(nextPoint, Direction.RIGHT)
                }
                Direction.LEFT,
                Direction.RIGHT -> nextBeams += Beam(nextPoint, beam.direction)
            }
            '\\' -> nextBeams += when (beam.direction) {
                Direction.LEFT -> Beam(nextPoint, Direction.UP)
                Direction.UP -> Beam(nextPoint, Direction.LEFT)
                Direction.RIGHT -> Beam(nextPoint, Direction.DOWN)
                Direction.DOWN -> Beam(nextPoint, Direction.RIGHT)
            }
            '/' -> nextBeams += when (beam.direction) {
                Direction.LEFT -> Beam(nextPoint, Direction.DOWN)
                Direction.UP -> Beam(nextPoint, Direction.RIGHT)
                Direction.RIGHT -> Beam(nextPoint, Direction.UP)
                Direction.DOWN -> Beam(nextPoint, Direction.LEFT)
            }
            null -> {} // ran offscreen, no new beams
            else -> error("Unknown tile $nextTile at ${beam.point}")
        }
        return nextBeams
    }
}
