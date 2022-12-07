package co.danhill.aoc.year2022

import co.danhill.aoc.util.Day
import co.danhill.aoc.util.Input
import co.danhill.aoc.util.lines

fun main() = Day07.run("2022/07.txt")

object Day07 : Day<Day07.FileType.Directory>() {
    override fun parseInput(input: Input): FileType.Directory {
        val root = FileType.Directory("/", mutableListOf())
        var currentDirectory = root

        input.lines
            .map { line ->
                val splitLine = line.split(' ')
                val (a, b) = splitLine
                when (a) {
                    "$" -> when (b) {
                        "cd" -> {
                            currentDirectory = when (val dirName = splitLine[2]) {
                                "/" -> root
                                ".." -> currentDirectory.parent
                                else -> currentDirectory.findDirectory(dirName)
                            }
                        }
                        "ls" -> {}
                        else -> error("Error parsing line $line")
                    }
                    "dir" -> {
                        val childDir = FileType.Directory(b, mutableListOf())
                        childDir.parent = currentDirectory
                        currentDirectory.children += childDir
                    }
                    else -> {
                        a.toLongOrNull()?.let { size ->
                            currentDirectory.children += FileType.File(b, size)
                        } ?: error("Error parsing line $line")
                    }
                }
            }

        return root
    }

    override fun part1(input: FileType.Directory): Any {
        return input.asSequence()
            .map { it.size() }
            .filter { it <= 100_000 }
            .sum()
    }

    override fun part2(input: FileType.Directory): Any {
        val needToFree = 30_000_000 - (70_000_000 - input.size())
        return input.asSequence()
            .map { it.size() }
            .filter { it >= needToFree }
            .min()
    }

    sealed class FileType(val name: String) {

        class Directory(name: String, val children: MutableList<FileType>): FileType(name) {
            lateinit var parent: Directory

            fun findDirectory(name: String) = children.first { it.name == name } as Directory

            override fun toString(): String = "$name (dir)"

            fun asSequence(): Sequence<Directory> {
                return sequence {
                    yield(this@Directory)
                    children.filterIsInstance<Directory>()
                        .forEach { child -> yieldAll(child.asSequence()) }
                }
            }
        }

        class File(name: String, val size: Long) : FileType(name) {
            override fun toString(): String = "$name (file, size=$size)"
        }

        fun size(): Long = when (this) {
                is File -> size
                is Directory -> children.sumOf { it.size() }
        }
    }
}
