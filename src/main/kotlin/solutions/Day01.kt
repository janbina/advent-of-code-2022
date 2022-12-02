package solutions

import java.io.BufferedReader

class Day01(
    inputReader: BufferedReader,
): Day<Int, Int>() {

    private val input: List<List<Int>> by lazy {
        val result = mutableListOf<List<Int>>()
        val current = mutableListOf<Int>()
        inputReader.lines().forEach { line ->
            if (line.isBlank()) {
                result.add(current.toList())
                current.clear()
            } else {
                current.add(line.toInt())
            }
        }
        result
    }

    override fun solvePart1(): Int {
        return input.maxOf { it.sum() }
    }

    override fun solvePart2(): Int {
        return input.map { it.sum() }.sortedDescending().take(3).sum()
    }
}
