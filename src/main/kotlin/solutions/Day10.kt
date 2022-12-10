package solutions

import java.io.BufferedReader
import kotlin.math.absoluteValue

class Day10(
    inputReader: BufferedReader,
) : Day<Int, Int>() {

    private sealed interface Instr {
        object Noop : Instr
        data class Addx(val value: Int) : Instr
    }

    private val input: List<Instr> = inputReader.transformLines { line ->
        when {
            line == "noop" -> Instr.Noop
            line.startsWith("addx") -> Instr.Addx(line.drop(5).toInt())
            else -> error("Invalid input")
        }
    }

    private val cycleToValue: Map<Int, Int> by lazy {
        var x = 1
        var cycle = 0
        val map = mutableMapOf<Int, Int>()

        fun cyclePP() {
            cycle++
            map[cycle] = x
        }

        input.forEach { instr ->
            when (instr) {
                Instr.Noop -> {
                    cyclePP()
                }

                is Instr.Addx -> {
                    cyclePP()
                    cyclePP()
                    x += instr.value
                }
            }
        }

        cyclePP()
        map
    }

    override fun solvePart1(): Int {
        return cycleToValue
            .filterKeys { (it - 20) % 40 == 0 }
            .map { (cycle, x) -> cycle * x }
            .sum()
    }

    override fun solvePart2(): Int {
        val width = 40
        val height = 6

        fun isPixelLit(pixel: Int): Boolean {
            val spritePos = cycleToValue[pixel + 1] ?: -3
            return (spritePos - (pixel % width)).absoluteValue <= 1
        }

        repeat(height) { y ->
            repeat(width) { x ->
                val c = if (isPixelLit(y * width + x)) '$' else ' '
                print(c)
            }
            println()
        }

        return 0
    }
}
