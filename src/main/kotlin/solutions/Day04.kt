package solutions

import java.io.BufferedReader

class Day04(
    inputReader: BufferedReader,
) : Day<Int, Int>() {

    private val input: List<Pair<IntRange, IntRange>> = inputReader.transformLines { line ->
        line.split(",").map {
            val (a, b) = it.split("-")
            IntRange(a.toInt(), b.toInt())
        }
    }.map { it[0] to it[1] }

    override fun solvePart1(): Int {
        return input.count {
            it.first.contains(it.second) || it.second.contains(it.first)
        }
    }

    override fun solvePart2(): Int {
        return input.count {
            it.first.overlaps(it.second)
        }
    }

    private fun IntRange.contains(other: IntRange): Boolean {
        return other.first >= this.first && other.last <= this.last
    }

    private fun IntRange.overlaps(other: IntRange): Boolean {
        return this.contains(other.first) ||
                this.contains(other.last) ||
                other.contains(this.first) ||
                other.contains(this.last)
    }
}
