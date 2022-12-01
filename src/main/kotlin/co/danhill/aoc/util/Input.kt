package co.danhill.aoc.util

import java.io.File

typealias Input = File

/**
 * The input parsed as one long string.
 */
val Input.text: String
    get() = readText()

/**
 * The input parsed as lines - one string for each newline.
 */
val Input.lines: List<String>
    get() = readLines()

/**
 * The input split into groups, where each group is delimited by a double newline. Each string may contain multiple
 * lines of the original file.
 */
val Input.groupedText: List<String>
    get() = text.split("\n\n")
