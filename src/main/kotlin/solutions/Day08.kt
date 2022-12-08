package solutions

import utils.Point2D
import utils.getOrNull
import utils.max
import java.io.BufferedReader

class Day08(
    inputReader: BufferedReader,
) : Day<Int, Int>() {

    private val input = inputReader.transformLines { line ->
        line.map { it.digitToInt() }.toTypedArray()
    }.toTypedArray().also {
        val rowSize = it.first().size
        it.forEach { row -> require(row.size == rowSize) }
    }

    override fun solvePart1(): Int {
        return input.mapIndexed { y, ints ->
            ints.mapIndexed { x, _ ->
                if (isVisible(input, Point2D(x, y))) 1 else 0
            }.sum()
        }.sum()
    }

    override fun solvePart2(): Int {
        return input.flatMapIndexed { y, ints ->
            ints.mapIndexed { x, _ ->
                scenicScore(input, Point2D(x, y))
            }
        }.max()
    }

    private fun isVisible(map: Array<Array<Int>>, position: Point2D): Boolean {
        val height = map.getOrNull(position) ?: error("Illegal argument")
        var flag = false
        for (i in 0 until position.x) {
            if (map[position.y][i] >= height) { flag = true; break }
        }
        if (!flag) return true

        flag = false
        for (i in position.x + 1 until map[0].size) {
            if (map[position.y][i] >= height) { flag = true; break }
        }
        if (!flag) return true

        flag = false
        for (i in 0 until position.y) {
            if (map[i][position.x] >= height) { flag = true; break }
        }
        if (!flag) return true

        flag = false
        for (i in position.y + 1 until map.size) {
            if (map[i][position.x] >= height) { flag = true; break }
        }
        if (!flag) return true

        return false
    }

    private fun scenicScore(map: Array<Array<Int>>, position: Point2D): Int {
        val height = map.getOrNull(position) ?: error("Illegal argument")
        var (a,b,c,d) = listOf(0,0,0,0)

        for (i in position.x - 1 downTo 0) {
            a++
            if (map[position.y][i] >= height) break
        }
        for (i in position.x + 1 until map[0].size) {
            b++
            if (map[position.y][i] >= height) break
        }
        for (i in position.y - 1 downTo 0) {
            c++
            if (map[i][position.x] >= height) break
        }
        for (i in position.y +1 until map.size) {
            d++
            if (map[i][position.x] >= height) break
        }

        return a*b*c*d
    }
}
