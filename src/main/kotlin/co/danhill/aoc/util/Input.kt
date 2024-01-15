package co.danhill.aoc.util

import java.io.FileOutputStream
import java.net.URL
import java.nio.channels.Channels

interface Input {
    /**
     * The input parsed as one long string.
     */
    val text: String

    /**
     * The input parsed as lines - one string for each newline.
     */
    val lines: List<String>

    /**
     * The input split into groups, where each group is delimited by a double newline. Each string may contain multiple
     * lines of the original file.
     */
    val groupedText: List<String>
}

class FileInput internal constructor(filename: String): Input {

    constructor(
        year: Int,
        day: Int,
    ): this("$year/${day.toString().padStart(2, '0')}.txt")

    private val file = readFile(filename)

    override val text: String
        get() = file.readText()
    override val lines: List<String>
        get() = file.readLines()
    override val groupedText: List<String>
        get() = text.split("\n\n")
}

class RemoteInput private constructor(url: String): Input {

    constructor(
        year: Int,
        day: Int,
    ): this("https://adventofcode.com/$year/day/$day/input")

    private val fileInput: FileInput
    init {
        val file = readCacheFile(url)
        if (!file.exists()) {
            // TODO: Authentication
            URL(url).openStream().use { stream ->
                Channels.newChannel(stream).use { channel ->
                    FileOutputStream(file).use { output ->
                        output.channel.transferFrom(channel, 0, Long.MAX_VALUE)
                    }
                }
            }
        }
        fileInput = FileInput(file.name)
    }

    override val text: String
        get() = fileInput.text
    override val lines: List<String>
        get() = fileInput.lines
    override val groupedText: List<String>
        get() = fileInput.groupedText
}
