package solutions

import utils.forEachIndexed
import java.io.BufferedReader

private typealias Rock = Array<Array<Char>>
private val Rock.width: Int get() = first().size
private val Rock.height: Int get() = size

class Day17(
    inputReader: BufferedReader,
) : Day<Int, Int>() {

    private val jetPattern = inputReader.transformLines { it }.first().toList()

    private fun jetSequence(): Sequence<Char> =
        generateSequence(0) { it + 1 }.map { jetPattern[it % jetPattern.size] }

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
        height: Int,
        width: Int,
        jetStream: Sequence<Char>,
    ) {
        private val arr = Array(height) { Array(width) { '.' } }
        private val jetStream = jetStream.iterator()

        // y coord of highest rock in the chamber, starting with floor
        private var highestY = arr.size

        val height: Int get() = arr.size - highestY

        fun placeRock(rock: Rock) {
            var x = 2 // rock starts 2 units from left side
            var y = highestY - 4 // bottom edge 3 units above

            fun collides(): Boolean {
                if (x < 0) return true
                if (x + rock.width > 7) return true
                if (y >= arr.size) return true
                var flag = false
                rock.forEachIndexed { px, py, c ->
                    if (
                        c == '#' &&
                        arr[y - rock.size + 1 + py][x + px] == '#'
                    ) {
                        flag = true
                        return@forEachIndexed
                    }
                }
                return flag
            }

            fun place() {
                highestY = minOf(highestY, y - rock.size + 1)
                rock.forEachIndexed { px, py, c ->
                    if (c == '#') {
                        arr[y - rock.size + 1 + py][x + px] = '#'
                    }
                }
            }

            fun maybeMoveX() {
                val movement = jetStream.next()
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
        val chamber = Chamber(
            height = 2022 * 4 + 100,
            width = 7,
            jetStream = jetSequence(),
        )

        rockSequence().take(2022).forEach(chamber::placeRock)

        return chamber.height
    }

    override fun solvePart2(): Int {
        return 0
    }
}
