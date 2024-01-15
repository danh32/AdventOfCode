package co.danhill.aoc.util

import java.io.File

fun readFile(filename: String): File {
    val url = ClassLoader.getSystemResource(filename)
    return File(url.file)
}

fun readCacheFile(filename: String): File {
    return File(filename)
}
