package solutions

import utils.Move
import utils.Point2D
import java.io.BufferedReader
import kotlin.math.absoluteValue
import kotlin.math.sign

class Day09(
    inputReader: BufferedReader,
) : Day<Int, Int>() {

    private data class Motion(
        val move: Move,
        val steps: Int,
    )

    private val input: List<Motion> = inputReader.transformLines {
        val (d, s) = it.split(" ")
        val direction = when (d) {
            "D" -> Move.down
            "U" -> Move.up
            "L" -> Move.left
            "R" -> Move.right
            else -> error("Invalid input")
        }
        val steps = s.toIntOrNull() ?: error("Invalid input")
        Motion(direction, steps)
    }

    override fun solvePart1(): Int {
        return simulateRope(2, input)
    }

    override fun solvePart2(): Int {
        return simulateRope(10, input)
    }

    private fun simulateRope(numKnots: Int, movement: List<Motion>): Int {
        require(numKnots >= 2) { "At least 2 knots needed" }
        val knots = Array(numKnots) { Point2D(0, 0) }
        val tailPositions = mutableSetOf(knots.last())

        movement.forEach { motion ->
            repeat(motion.steps) {
                knots[0] = knots[0].applyMove(motion.move)
                for (i in 1..knots.lastIndex) {
                    knots[i] = knots[i].moveTowards(knots[i-1])
                }
                tailPositions += knots.last()
            }
        }

        return tailPositions.size
    }

    private fun Point2D.moveTowards(target: Point2D): Point2D {
        val xDiff = target.x - x
        val yDiff = target.y - y

        if (xDiff.absoluteValue <= 1 && yDiff.absoluteValue <= 1) {
            return this
        }
        return Point2D(x + xDiff.sign, y + yDiff.sign)
    }
}
