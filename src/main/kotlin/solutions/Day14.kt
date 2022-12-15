package solutions

import utils.Point2D
import java.io.BufferedReader

class Day14(
    inputReader: BufferedReader,
) : Day<Int, Int>() {

    private val input: List<List<Point2D>> = inputReader.transformLines(::parseLine)

    private fun parseLine(line: String): List<Point2D> {
        return line.split(" -> ").map {
            val (x, y) = it.split(',')
            Point2D(x.toInt(), y.toInt())
        }
    }

    private val sandSource = Point2D(500, 0)

    private val rock: Set<Point2D> by lazy {
        mutableSetOf<Point2D>().also { set ->
            input.forEach { path ->
                path.windowed(2) { (a, b) ->
                    for (x in minOf(a.x, b.x)..maxOf(a.x, b.x)) {
                        for (y in minOf(a.y, b.y)..maxOf(a.y, b.y)) {
                            set += Point2D(x, y)
                        }
                    }
                }
            }
        }
    }

    override fun solvePart1(): Int {
        val sand = mutableSetOf<Point2D>()

        while (true) {
            val idlePos = traceSand(rock + sand, sandSource) ?: break
            sand += idlePos
        }

        return sand.size
    }

    override fun solvePart2(): Int {
        val maxY = rock.maxOf { it.y } + 1
        val sand = mutableSetOf<Point2D>()

        while (true) {
            val idlePos = traceSand(rock + sand, sandSource, maxY)!!
            sand += idlePos
            if (idlePos == sandSource) break
        }

        return sand.size
    }

    private tailrec fun traceSand(map: Set<Point2D>, start: Point2D, maxY: Int? = null): Point2D? {
        return when {
            maxY != null && start.y == maxY -> start
            maxY == null && start.y > map.maxOf { it.y } -> null
            start.down() !in map -> traceSand(map, start.down(), maxY)
            start.downLeft() !in map -> traceSand(map, start.downLeft(), maxY)
            start.downRight() !in map -> traceSand(map, start.downRight(), maxY)
            else -> start
        }
    }

    private fun print(rock: Set<Point2D>, sand: Set<Point2D>) {
        for (y in 0..(rock.maxOf { it.y } + 20)) {
            for (x in (rock.minOf { it.x } - 20)..(rock.maxOf { it.x } + 20)) {
                val c = when (Point2D(x, y)) {
                    in rock -> '#'
                    in sand -> 'o'
                    else -> '.'
                }
                print(c)
            }
            println()
        }
    }
}
