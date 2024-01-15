package co.danhill.aoc.year2022

import co.danhill.aoc.util.Day
import co.danhill.aoc.util.Input
import kotlin.math.absoluteValue

fun main() = Day18.run()

private typealias Cube = Triple<Int, Int, Int>

object Day18 : Day(2022, 18) {
    private fun Input.parse() = lines.map { line ->
        val (x, y, z) = line.split(',').map { it.toInt() }
        Cube(x, y, z)
    }

    override fun part1(input: Input): Any {
        val cubes = input.parse()

        return cubes.sumOf { cube ->
            val adjacentCount = cubes.count { cube != it && cube.isAdjacent(it) }
            6 - adjacentCount
        }
    }

    override fun part2(input: Input): Any {
        val cubes = input.parse()

        val space = cubes.fold(mutableMapOf<Cube, Material>()) { acc, cube ->
            acc[cube] = Material.CUBE
            acc
        }

        println("Folded space")

        val (maxX, maxY, maxZ) = cubes.fold(Triple(0, 0, 0)) { (maxX, maxY, maxZ), (x, y, z) ->
            Triple(
                maxOf(maxX, x),
                maxOf(maxY, y),
                maxOf(maxZ, z),
            )
        }

        println("Folded maxes")

        fun Cube.isOutOfBounds(): Boolean = this.x > maxX || this.x < 0
                || this.y > maxY || this.y < 0
                || this.z > maxZ || this.z < 0

        val toConsider = mutableListOf(Cube(0, 0, 0))
        val visited = mutableSetOf<Cube>()
        while (toConsider.isNotEmpty()) {
            val current = toConsider.removeFirst()
            if (visited.contains(current)) continue
            visited += current
            println("Considering $current")
            space[current] = Material.WATER
            current.cardinalNeighbors.forEach { neighbor ->
                if (!neighbor.isOutOfBounds()) {
                    when (space[neighbor]) {
                        Material.CUBE,
                        Material.WATER -> {} // no-op
                        Material.AIR,
                        null -> {
                            toConsider += neighbor
                        }
                    }
                }
            }
        }

        println("Flood filled")

        val filledShape = cubes.toMutableList()
        for (z in 0..maxZ) {
            for (y in 0..maxY) {
                for (x in 0..maxX) {
                    val cube = Cube(x, y, z)
                    when (space[cube]) {
                        Material.CUBE,
                        Material.WATER -> {} // no-op
                        Material.AIR -> {
                            // we must be an airpocket, coerce to a cube instead
                            filledShape += cube
                        }
                        null -> {
                            println("Null material in $x, $y, $z")
                            // we must be an airpocket, coerce to a cube instead
                            filledShape += cube
                        }
                    }
                }
            }
        }

        println("Coerced shapes")

        return filledShape.sumOf { cube ->
            val adjacentCount = filledShape.count { cube != it && cube.isAdjacent(it) }
            6 - adjacentCount
        }
    }

    private val Cube.x: Int
        get() = this.first

    private val Cube.y: Int
        get() = this.second

    private val Cube.z: Int
        get() = this.third

    private val Cube.cardinalNeighbors: List<Cube>
        get() = listOf(
            Cube(x - 1, y, z),
            Cube(x + 1 , y, z),
            Cube(x, y - 1, z),
            Cube(x, y + 1, z),
            Cube(x, y, z - 1),
            Cube(x, y, z + 1),
        )

    private fun Cube.isAdjacent(other: Cube) = spaceManhattanDistanceTo(other) == 1

    private fun Cube.spaceManhattanDistanceTo(other: Cube): Int {
        return (x - other.x).absoluteValue + (y - other.y).absoluteValue + (z - other.z).absoluteValue
    }

    private enum class Material {
        CUBE,
        AIR,
        WATER,
    }

}
