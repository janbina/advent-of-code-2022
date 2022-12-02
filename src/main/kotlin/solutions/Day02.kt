package solutions

import java.io.BufferedReader

class Day02(
    inputReader: BufferedReader,
): Day<Int, Int>() {

    enum class Shape(val score: Int) {
        Rock(1),
        Paper(2),
        Scissors(3);

        companion object {
            fun fromEncrypted(c: Char): Shape {
                return when (c) {
                    'A', 'X' -> Rock
                    'B', 'Y' -> Paper
                    'C', 'Z' -> Scissors
                    else -> error("Invalid argument")
                }
            }
        }
    }

    enum class Outcome(val score: Int) {
        Loss(0),
        Draw(3),
        Win(6),
    }

    private val input: List<Pair<Shape, Shape>> by lazy {
        inputReader.transformLines { line ->
            line.split(" ").let { splits ->
                Shape.fromEncrypted(splits[0].first()) to Shape.fromEncrypted(splits[1].first())
            }
        }
    }

    override fun solvePart1(): Int {
        return input.sumOf {
            it.second.score + playRound(it.first, it.second).score
        }
    }

    override fun solvePart2(): Int {
        return 0
    }

    private fun playRound(opponent: Shape, me: Shape): Outcome {
        return when {
            opponent == me -> Outcome.Draw
            (opponent == Shape.Rock && me == Shape.Scissors) ||
                    (opponent == Shape.Paper && me == Shape.Rock) ||
                    (opponent == Shape.Scissors && me == Shape.Paper) -> Outcome.Loss
            else -> Outcome.Win
        }
    }
}
