package solutions

import utils.forEachIndexed
import java.io.BufferedReader

private typealias Rock = Array<Array<Char>>

class Day17(
    inputReader: BufferedReader,
) : Day<Int, Long>() {

    private val jetPattern = inputReader.transformLines { it }.first().toList()

    private fun String.toRock(): Rock {
        return split('|').map { it.toCharArray().toTypedArray() }.toTypedArray()
    }

    private val rocks = listOf(
        "####".toRock(),
        " # |###| # ".toRock(),
        "  #|  #|###".toRock(),
        "#|#|#|#".toRock(),
        "##|##".toRock(),
    )

    private data class Repetition(
        val rockCount: Long,
        val towerHeight: Long,
    )

    private class Chamber(
        private val jetPattern: List<Char>,
    ) {
        // we dont need cyclick array in the end, but dont want to change it back
        // plus its nice that 100 rows is enough, otherwise we would need ~5000
        private val arr = CyclicArray(7, 100).also {
            it.clearRow(0, '#')
        }

        private var jetIndex = 0

        private fun getJetDirection(): Char {
            return jetPattern[jetIndex].also {
                jetIndex = (jetIndex + 1) % jetPattern.size
            }
        }

        private data class CacheKey(
            @Suppress("ArrayInDataClass") // rock arrays are immutable
            val rock: Rock,
            val jetIndex: Int,
            val frontLine: List<Int>,
        )

        // y coord of highest rock in the chamber, starting with floor
        private var height: Long = 0

        fun getHeight(): Long {
            return -height
        }

        private val seen = mutableMapOf<CacheKey, Pair<Long, Long>>()

        private var rockIndex = 0L

        fun placeRock(rock: Rock): Repetition? {
            rockIndex++
            val key = CacheKey(rock, jetIndex, arr.getFrontLine(height))
            if (seen.contains(key)) {
                val (ri, hi) = seen[key]!!
                seen.clear()
                return Repetition(rockIndex - ri, -(height - hi)).also {
                    println("Cycle found at height ${-height} $it")
                }
            }
            seen[key] = rockIndex to height
            var x = 2 // rock starts 2 units from left side
            var y = height - 4 // bottom edge 3 units above
            for (py in height - 1 downTo height - 10 - rock.size) {
                arr.clearRow(py)
            }

            fun collides(): Boolean {
                if (x < 0) return true
                if (x + rock[0].size > 7) return true
                var flag = false
                rock.forEachIndexed { px, py, c ->
                    if (
                        c == '#' &&
                        arr.get(y - rock.size + 1 + py, x + px) == '#'
                    ) {
                        flag = true
                        return@forEachIndexed
                    }
                }
                return flag
            }

            fun place() {
                height = minOf(height, y - rock.size + 1)
                rock.forEachIndexed { px, py, c ->
                    if (c == '#') {
                        arr.set(y - rock.size + 1 + py, x + px, '#')
                    }
                }
            }

            fun maybeMoveX() {
                val movement = getJetDirection()
                if (movement == '<') {
                    x--
                    if (collides()) x++
                }
                if (movement == '>') {
                    x++
                    if (collides()) x--
                }
            }

            fun maybeMoveY(): Boolean {
                y++
                return if (collides()) {
                    y--
                    false
                } else {
                    true
                }
            }

            do {
                maybeMoveX()
            } while (maybeMoveY())
            place()
            return null
        }
    }

    override fun solvePart1(): Int {
        return run(2022).toInt()
    }

    override fun solvePart2(): Long {
        return run(1000000000000)
    }

    private fun run(stones: Long): Long {
        val chamber = Chamber(jetPattern)
        var additionalHeight = 0L

        // simulate the rockfall until some repetition occurs,
        // skip the simulation of repeating parts and simulate the rest
        var i = 0L
        while (i < stones) {
            val rock = rocks[(i % rocks.size).toInt()]
            val repetition = chamber.placeRock(rock)
            if (repetition != null) {
                val remaining = stones - i
                val repeats = remaining / repetition.rockCount
                additionalHeight += repeats * repetition.towerHeight
                i += repeats * repetition.rockCount
            } else {
                i++
            }
        }

        return chamber.getHeight() + additionalHeight
    }
}

private class CyclicArray(
    width: Int,
    private val height: Int,
) {
    private val arr = Array(height) { Array(width) { ' ' } }

    private fun Long.toIndex(): Int {
        return (this % arr.size).let {
            if (it < 0) it + height else it
        }.toInt()
    }

    fun get(y: Long, x: Int): Char {
        return arr[y.toIndex()][x]
    }

    fun getFrontLine(y: Long): List<Int> {
        return (0 until arr[0].size).map { x ->
            var h = 0L
            while (true) {
                if (arr[(y+h).toIndex()][x] == '#') break
                h++
            }
            h.toInt()
        }
    }

    fun set(y: Long, x: Int, c: Char) {
        arr[y.toIndex()][x] = c
    }

    fun clearRow(y: Long, c: Char = ' ') {
        for (x in arr.first().indices) {
            set(y, x, c)
        }
    }
}
