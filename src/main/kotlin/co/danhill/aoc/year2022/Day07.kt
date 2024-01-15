package co.danhill.aoc.year2022

import co.danhill.aoc.util.Day
import co.danhill.aoc.util.Input
import co.danhill.aoc.util.TreeNode

fun main() = Day07.run()

object Day07 : Day(2022, 7) {
    private fun Input.parse(): TreeNode<FileType> {
        val root = TreeNode<FileType>(FileType.Directory("/"))
        var currentDirectory = root

        lines.map { line ->
            val splitLine = line.split(' ')
            val (a, b) = splitLine
            when (a) {
                "$" -> when (b) {
                    "cd" -> {
                        currentDirectory = when (val dirName = splitLine[2]) {
                            "/" -> root
                            ".." -> currentDirectory.parent
                            else -> currentDirectory.children.first { it.data.name == dirName }
                        }
                    }

                    "ls" -> {}
                    else -> error("Error parsing line $line")
                }

                "dir" -> {
                    val childDir = TreeNode<FileType>(FileType.Directory(b))
                    childDir.parent = currentDirectory
                    currentDirectory.addChild(childDir)
                }

                else -> {
                    a.toLongOrNull()?.let { size ->
                        currentDirectory.addChild(TreeNode(FileType.File(b, size)))
                    } ?: error("Error parsing line $line")
                }
            }
        }

        return root
    }

    override fun part1(input: Input): Any {
        return input.parse()
            .asSequence()
            .filter { it.data is FileType.Directory }
            .map { it.size() }
            .filter { it <= 100_000 }
            .sum()
    }

    override fun part2(input: Input): Any {
        val parsed = input.parse()
        val needToFree = 30_000_000 - (70_000_000 - parsed.size())
        return parsed.asSequence()
            .filter { it.data is FileType.Directory }
            .map { it.size() }
            .filter { it >= needToFree }
            .min()
    }

    sealed class FileType(val name: String) {

        class Directory(name: String) : FileType(name) {
            override fun toString(): String = "$name (dir)"
        }

        class File(name: String, val size: Long) : FileType(name) {
            override fun toString(): String = "$name (file, size=$size)"
        }
    }

    private fun TreeNode<FileType>.size(): Long = when (data) {
        is FileType.File -> data.size
        is FileType.Directory -> children.sumOf { it.size() }
    }
}
