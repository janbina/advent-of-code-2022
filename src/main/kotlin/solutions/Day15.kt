package solutions

import utils.Point2D
import java.io.BufferedReader
import kotlin.math.abs

class Day15(
    inputReader: BufferedReader,
) : Day<Int, Long>() {

    private val regex = "-?\\d+".toRegex()

    private val input = inputReader.transformLines { line ->
        val i = regex.findAll(line).map { it.value.toInt() }.toList()
        val s = Point2D(i[0], i[1])
        val b = Point2D(i[2], i[3])
        SensorInfo(s, b, s.distanceTo(b))
    }

    private data class SensorInfo(
        val sensor: Point2D,
        val beacon: Point2D,
        val distance: Int,
    )

    override fun solvePart1(): Int {
        val targetY = 2000000
        var minX = Int.MAX_VALUE
        var maxX = Int.MIN_VALUE

        input.forEach {
            val yDist = abs(targetY - it.sensor.y)
            val xDist = it.distance - yDist
            if (xDist >= 0) {
                minX = minOf(minX, it.sensor.x - xDist)
                maxX = maxOf(maxX, it.sensor.x + xDist)
            }
        }

        return (minX..maxX)
            .count { !Point2D(it, targetY).canBeMissingBeacon() } -
                input.count { it.beacon.y == targetY }
    }

    override fun solvePart2(): Long {
        val beacon = input.asSequence().flatMap {
            it.sensor.neighborsInDistance(it.distance + 1)
        }.first { it.canBeMissingBeacon() }
        return beacon.x.toLong() * 4000000 + beacon.y
    }

    private fun Point2D.neighborsInDistance(distance: Int): Sequence<Point2D> = sequence {
        repeat(distance) { s ->
            yield(Point2D(x + s, y + (distance - s)))
            yield(Point2D(x + s, y - (distance - s)))
            yield(Point2D(x - s, y + (distance - s)))
            yield(Point2D(x - s, y - (distance - s)))
        }
    }.filter { it.x in 0..4000000 && it.y in 0..4000000 }

    private fun Point2D.canBeMissingBeacon(): Boolean {
        input.forEach {
            if (it.sensor.distanceTo(this) <= it.distance) {
                return false
            }
        }
        return true
    }
}
