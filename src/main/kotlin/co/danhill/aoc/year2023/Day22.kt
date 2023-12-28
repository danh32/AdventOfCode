package co.danhill.aoc.year2023

import co.danhill.aoc.util.*
import kotlin.math.max
import kotlin.math.min

fun main() = Day22.run("2023/22_test.txt")

private typealias Point3d = Triple<Int, Int, Int>

object Day22 : Day {
    override fun part1(input: Input): Any {
        val bricks = input.lines.mapIndexed { index, line ->
            val (start, end) = line.split('~')
            val (x1, y1, z1) = start.split(',').map { it.toInt() }
            val (x2, y2, z2) = end.split(',').map { it.toInt() }
            Brick(
                id = index,
                start = Point3d(x1, y1, z1),
                end = Point3d(x2, y2, z2),
            )
        }.sortedBy { it.minZ }

        bricks.map { brick ->
            println(brick.char)
            if (brick.minZ == 1) {
                brick.atRest = true
                brick
            } else {
                var movedBrick = brick
                while (!bricks.intersects(movedBrick) && movedBrick.minZ > 1) {
                    movedBrick = movedBrick.moveDown()
                }
                brick.atRest = true
                movedBrick.moveUp()
            }
        }
        return bricks.count {
            val canBeDeleted = bricks.canBeDeleted(it)
            println("Brick ${(it.id + 65).toChar()} can be deleted $canBeDeleted")
            canBeDeleted
        }
    }

    override fun part2(input: Input): Any {
        return ""
    }

    private fun List<Brick>.intersects(movedBrick: Brick): Boolean {
        return any { brick ->
            if (brick.id == movedBrick.id) false // can't intersect with ourselves
            else brick.intersects(movedBrick)
        }
    }

    private fun List<Brick>.canBeDeleted(brick: Brick): Boolean {
        val movedUpBrick = brick.moveUp()
        val supportedBricks = filter { it.intersects(movedUpBrick) }
        if (supportedBricks.isEmpty()){
            println("Brick ${brick.char} can be deleted since it's not supporting any beams")
            return true
        }

        println("Brick ${brick.char} is supporting ${supportedBricks.joinToString { it.char.toString() }}")

        return supportedBricks
            .map { it.moveDown() }
            .all { movedDownBrick ->
                val foo = this.any { it.intersects(movedDownBrick) && it.id != brick.id }
                if (foo) {
                    println("Dependent brick ${movedDownBrick.char} also supported by another")
                }
                foo
            }
    }

    private val Point3d.x
        get() = first
    private val Point3d.y
        get() = second
    private val Point3d.z
        get() = third

    private val Point3d.upZ: Point3d
        get() = Point3d(x, y, z + 1)

    private val Point3d.downZ: Point3d
        get() = Point3d(x, y, z - 1)

    private class Brick(
        val id: Int,
        val points: List<Point3d>
    ) {
        val minZ = points.minOf { (_, _, z) -> z }
        var atRest: Boolean = false

        constructor(
            id: Int,
            start: Point3d,
            end: Point3d,
        ) : this(
            id = id,
            points = when {
                start.x != end.x -> {
                    val xRange = min(start.x, end.x)..max(start.x, end.x)
                    xRange.map { x -> Point3d(x, start.y, start.z) }
                }

                start.y != end.y -> {
                    val yRange = min(start.y, end.y)..max(start.y, end.y)
                    yRange.map { y -> Point3d(start.x, y, start.z) }
                }

                start.z != end.z -> {
                    val zRange = min(start.z, end.z)..max(start.z, end.z)
                    zRange.map { z -> Point3d(start.x, start.y, z) }
                }

                else -> error("$start and $end do not align")
            }
        )

        val char: Char
            get() = (id + 65).toChar()

        fun intersects(other: Brick): Boolean {
            return points.any { p1 -> other.points.any { p2 -> p1 == p2 } }
        }

        fun moveDown(): Brick {
            return Brick(id = id, points = points.map { it.downZ })
        }

        fun moveUp(): Brick {
            return Brick(id = id, points = points.map { it.upZ })
        }
    }
}
