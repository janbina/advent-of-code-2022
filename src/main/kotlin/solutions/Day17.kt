package solutions

import utils.forEachIndexed
import java.io.BufferedReader

private typealias Rock = Array<Array<Char>>

private val Rock.width: Int get() = first().size
private val Rock.height: Int get() = size

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

    private fun rockSequence(): Sequence<Rock> =
        generateSequence(0) { it + 1 }.map { rocks[it % rocks.size] }

    private class Chamber(
        private val jetPattern: List<Char>,
    ) {
        private val arr = CyclicArray(7, 1000).also {
            it.clearRow(0, '#')
        }

        private var jetIndex = 0

        private fun getJetDirection(): Char {
            return jetPattern[jetIndex].also {
                jetIndex = (jetIndex + 1) % jetPattern.size
            }
        }

        // y coord of highest rock in the chamber, starting with floor
        private var height: Long = 0

        fun getHeight(): Long {
            return -height
        }

        fun placeRock(rock: Rock) {
            var x = 2 // rock starts 2 units from left side
            var y = height - 4 // bottom edge 3 units above
            for (py in height - 1 downTo height - 10 - rock.size) {
                arr.clearRow(py)
            }

            fun collides(): Boolean {
                if (x < 0) return true
                if (x + rock.width > 7) return true
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
        }
    }

    override fun solvePart1(): Int {
        val chamber = Chamber(jetPattern)

        rockSequence().take(2022).forEach(chamber::placeRock)

        return chamber.getHeight().toInt()
    }

    override fun solvePart2(): Long {
        return 0L
//        val chamber = Chamber(
//            width = 7,
//            jetStream = jetSequence(),
//        )
//
//        val rocks = rockSequence().iterator()
//        for (i in 0 until 1000000000000) {
//            if (i % 100000 == 0L) {
//                println(i)
//            }
//            chamber.placeRock(rocks.next())
//        }
//
//        return chamber.getHeight()
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

    fun set(y: Long, x: Int, c: Char) {
        arr[y.toIndex()][x] = c
    }

    fun clearRow(y: Long, c: Char = ' ') {
        for (x in arr.first().indices) {
            set(y, x, c)
        }
    }
}
