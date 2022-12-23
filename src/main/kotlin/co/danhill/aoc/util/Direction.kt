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

    companion object {
        val values = values()
    }
}