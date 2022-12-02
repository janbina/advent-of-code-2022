package solutions

import java.io.BufferedReader

class Day02(
    inputReader: BufferedReader,
): Day<Int, Int>() {

    enum class Shape(val score: Int) {
        Rock(1),
        Paper(2),
        Scissors(3);

        val losesTo: Shape
            get() = when (this) {
                Rock -> Paper
                Paper -> Scissors
                Scissors -> Rock
            }

        val winsAgainst: Shape
            get() = this.losesTo.losesTo

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
        Win(6);

        companion object {
            fun fromEncrypted(c: Char): Outcome {
                return when (c) {
                    'X' -> Loss
                    'Y' -> Draw
                    'Z' -> Win
                    else -> error("Invalid argument")
                }
            }
        }
    }

    private val input = inputReader.transformLines { it }

    private val inputP1: List<Pair<Shape, Shape>> by lazy {
        input.map { line ->
            line.split(" ").let { splits ->
                Shape.fromEncrypted(splits[0].first()) to Shape.fromEncrypted(splits[1].first())
            }
        }
    }

    private val inputP2: List<Pair<Shape, Outcome>> by lazy {
        input.map { line ->
            line.split(" ").let { splits ->
                Shape.fromEncrypted(splits[0].first()) to Outcome.fromEncrypted(splits[1].first())
            }
        }
    }

    override fun solvePart1(): Int {
        return inputP1.sumOf {
            it.second.score + playRound(it.first, it.second).score
        }
    }

    override fun solvePart2(): Int {
        return inputP2.sumOf {
            it.second.score + playRound(it.first, it.second).score
        }
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

    private fun playRound(opponent: Shape, desiredOutcome: Outcome): Shape {
        return when (desiredOutcome) {
            Outcome.Draw -> opponent
            Outcome.Loss -> opponent.winsAgainst
            Outcome.Win -> opponent.losesTo
        }
    }
}
