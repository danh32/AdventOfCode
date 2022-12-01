package co.danhill.aoc.util

import java.io.File

fun readFile(filename: String): File {
    return File(ClassLoader.getSystemResource(filename).file)
}
