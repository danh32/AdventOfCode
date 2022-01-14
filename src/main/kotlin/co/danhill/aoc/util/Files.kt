package co.danhill.aoc.util

import java.io.File

fun readFile(filename: String): File {
    val url = Day::class.java.classLoader.getResource(filename)
    return File(url.file)
}
