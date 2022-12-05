package solutions

import java.io.BufferedReader

class Day06(
    inputReader: BufferedReader,
) : Day<Int, Int>() {

    private val input = inputReader.transformLines { it }.first()

    override fun solvePart1(): Int {
        val index = input.indexOfFirstMarker(4) ?: error("No marker")
        return index + 4
    }

    override fun solvePart2(): Int {
        val index = input.indexOfFirstMarker(14) ?: error("No marker")
        return index + 14
    }

    private fun String.indexOfFirstMarker(markerLength: Int): Int? {
        for (i in 0..this.length - markerLength) {
            if (substring(i, i + markerLength).toSet().size == markerLength) {
                return i
            }
        }
        return null
    }
}
