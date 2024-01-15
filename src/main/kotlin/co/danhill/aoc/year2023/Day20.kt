package co.danhill.aoc.year2023

import co.danhill.aoc.util.Day
import co.danhill.aoc.util.Input

fun main() = Day20.run()

object Day20 : Day(2023, 20) {
    override fun part1(input: Input): Any {
        val modules = input.getModules()

        var cycleLength = 0
        var lowCount = 0
        var highCount = 0
        do {
            val (lows, highs) = modules.pushTheButton()
            lowCount += lows
            highCount += highs
            cycleLength++
        } while (!modules.inStartingState() && cycleLength < 1000)

//        println("$cycleLength - $lowCount - $highCount")
        val multiplicand = 1000L / cycleLength
        return (multiplicand * lowCount) * (multiplicand * highCount)
    }

    private fun Map<String, Module>.pushTheButton(): Pair<Int, Int> {
        var lowCount = 0
        var highCount = 0
        val queue = mutableListOf(Triple("button", Pulse.LOW, "broadcaster"))
        while (queue.isNotEmpty()) {
            val (sender, pulse, receiver) = queue.removeFirst()
//            println("$sender -$pulse- $receiver")
            when (pulse) {
                Pulse.LOW -> lowCount++
                Pulse.HIGH -> highCount++
            }
            queue += dispatch(sender, pulse, receiver)
        }
        return lowCount to highCount
    }

    private fun Input.getModules(): Map<String, Module> {
        val firstPassModules = lines.map { line ->
            val (current, nexts) = line.split(" -> ")
            val name = current.substring(1)
            val nextModuleNames = nexts.split(", ")
            when (val prefix = current.first()) {
                'b' -> Module.Broadcaster(current, nextModuleNames)
                '%' -> Module.FlipFlop(name, nextModuleNames)
                '&' -> Module.Conjunction(name, nextModuleNames)
                else -> error("Unknown prefix $prefix")
            }
        }
        firstPassModules.forEach { module ->
            if (module is Module.Conjunction) {
                firstPassModules.filter { it.nextModuleNames.contains(module.name) }
                    .forEach { module.lastPulses[it.name] = Pulse.LOW  }
            }
        }
        return firstPassModules.associateBy { it.name }
    }

    private fun Map<String, Module>.dispatch(sender: String, pulse: Pulse, receiver: String): List<Triple<String, Pulse, String>> {
        val module = get(receiver) ?: return emptyList()
        val nextPulse = module.receive(sender, pulse)
        return if (nextPulse == null) emptyList()
        else module.nextModuleNames.map { Triple(module.name, nextPulse, it) }
    }

    private fun Map<String, Module>.inStartingState(): Boolean {
        return values.all { module ->
            when (module) {
                is Module.Broadcaster -> true
                is Module.Conjunction -> true //module.lastPulses.values.all { it == Pulse.LOW }
                is Module.Rx -> true
                is Module.FlipFlop -> !module.on
            }
        }
    }

    override fun part2(input: Input): Any {
        val modules = input.getModules()
        var buttonPushes = 0
        while (!Module.Rx.hasSeenALow) {
            buttonPushes++
            modules.pushTheButton()
        }
        return buttonPushes
    }

    private sealed class Module(
        open val name: String,
        open val nextModuleNames: List<String>,
    ) {

        abstract fun receive(from: String, pulse: Pulse): Pulse?

        data class Broadcaster(
            override val name: String,
            override val nextModuleNames: List<String>,
        ) : Module(name, nextModuleNames) {
            override fun receive(from: String, pulse: Pulse): Pulse? {
                return pulse
            }
        }

        data class FlipFlop(
            override val name: String,
            override val nextModuleNames: List<String>,
            var on: Boolean = false
        ) : Module(name, nextModuleNames) {

            override fun receive(from: String, pulse: Pulse): Pulse? {
                return when (pulse) {
                    Pulse.HIGH -> null
                    Pulse.LOW -> {
                        val currentOn = on
                        on = !on
                        if (currentOn) {
                            Pulse.LOW
                        } else {
                            Pulse.HIGH
                        }
                    }
                }
            }
        }

        class Conjunction(
            override val name: String,
            override val nextModuleNames: List<String>,
            val lastPulses: MutableMap<String, Pulse> = mutableMapOf(),
        ) : Module(name, nextModuleNames) {

            override fun receive(from: String, pulse: Pulse): Pulse? {
                lastPulses[from] = pulse
                val allHigh = lastPulses.all { (_, p) -> p == Pulse.HIGH }
                return if (allHigh) Pulse.LOW else Pulse.HIGH
            }
        }

        object Rx : Module("rx", emptyList()) {
            var hasSeenALow = false
            override fun receive(from: String, pulse: Pulse): Pulse? {
                if (pulse == Pulse.LOW) {
                    hasSeenALow = true
                }
                return null
            }

        }
    }
    private enum class Pulse { LOW, HIGH; }
}
