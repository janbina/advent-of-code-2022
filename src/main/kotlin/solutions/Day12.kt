package solutions

import utils.Point2D
import utils.aStarSearch
import utils.getOrNull
import utils.positionOfFirst
import java.io.BufferedReader

class Day12(
    inputReader: BufferedReader,
) : Day<Int, Int>() {

    private val input = inputReader
        .transformLines { it.toCharArray().toTypedArray() }
        .toTypedArray()

    private val start: Point2D = input.positionOfFirst { it == 'S' } ?: error("Invalid input")

    private val end: Point2D = input.positionOfFirst { it == 'E' } ?: error("Invalid input")

    private val Char.height: Int
        get() = when (this) {
            'S' -> 'a'.height
            'E' -> 'z'.height
            else -> this.code - 'a'.code
        }

    private fun getHeight(p: Point2D): Int? {
        return input.getOrNull(p)?.height
    }

    override fun solvePart1(): Int {
        return aStarSearch(
            start = start,
            isEnd = { it == end },
            next = { node ->
                val height = getHeight(node)!!
                node.neighbors()
                    .filter {
                        val h = getHeight(it)
                        h != null && (h <= height + 1)
                    }
                    .map { it to 1 }
            },
            heuristicCostToEnd = { node ->
                getHeight(end)!! - getHeight(node)!!
            },
        )?.cost ?: error("Cannot find path")
    }

    override fun solvePart2(): Int {
        return aStarSearch(
            start = end,
            isEnd = { getHeight(it) == 0 },
            next = { node ->
                val height = getHeight(node)!!
                node.neighbors()
                    .filter {
                        val h = getHeight(it)
                        h != null && (h + 1 >= height)
                    }
                    .map { it to 1 }
            },
            heuristicCostToEnd = { node ->
                getHeight(node)!!
            },
        )?.cost ?: error("Cannot find path")
    }
}
