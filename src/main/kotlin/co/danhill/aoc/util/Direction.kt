package co.danhill.aoc.util

enum class Direction {
    LEFT,
    UP,
    RIGHT,
    DOWN,
    ;

    fun turn(clockwise: Boolean): Direction {
        val newOrdinal = this.ordinal + if (clockwise) 1 else -1
        return values[((newOrdinal % values.size) + values.size) % values.size]
    }

    val displayChar: Char
        get() = when (this) {
            LEFT -> '<'
            UP -> '^'
            RIGHT -> '>'
            DOWN -> 'v'
        }

    companion object {
        val values = values()

        fun fromChar(char: Char) = when (char) {
            'U' -> UP
            'D' -> DOWN
            'L' -> LEFT
            'R' -> RIGHT
            '<' -> LEFT
            '^' -> UP
            '>' -> RIGHT
            'v' -> DOWN
            else -> error("Unknown char $char")
        }
    }
}