package co.danhill.aoc.util

import java.io.File

fun readFile(filename: String): File {
    val foo = ClassLoader.getSystemResource(filename)
    return File(foo.file)
}
