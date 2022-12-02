package solutions

import java.io.BufferedReader

class Day02(
    inputReader: BufferedReader,
): Day<Int, Int>() {

    private val inputLines = inputReader.transformLines { it }

    private val inputP1: List<InputP1> by lazy {
        inputLines.map { line ->
            line.split(" ").let { splits ->
                InputP1(
                    opponentsShape = Shape.fromEncrypted(splits[0].first()),
                    myShape = Shape.fromEncrypted(splits[1].first()),
                )
            }
        }
    }

    private val inputP2: List<InputP2> by lazy {
        inputLines.map { line ->
            line.split(" ").let { splits ->
                InputP2(
                    opponentsShape = Shape.fromEncrypted(splits[0].first()),
                    desiredOutcome = Outcome.fromEncrypted(splits[1].first()),
                )
            }
        }
    }

    override fun solvePart1(): Int {
        return inputP1.sumOf { input ->
            playRound(
                opponent = input.opponentsShape,
                me = input.myShape,
            ).score + input.myShape.score
        }
    }

    override fun solvePart2(): Int {
        return inputP2.sumOf { input ->
            playRound(
                opponent = input.opponentsShape,
                desiredOutcome = input.desiredOutcome,
            ).score + input.desiredOutcome.score
        }
    }

    private fun playRound(opponent: Shape, me: Shape): Outcome {
        return when (opponent) {
            me -> Outcome.Draw
            me.winsAgainst -> Outcome.Win
            else -> Outcome.Loss
        }
    }

    private fun playRound(opponent: Shape, desiredOutcome: Outcome): Shape {
        return when (desiredOutcome) {
            Outcome.Draw -> opponent
            Outcome.Loss -> opponent.winsAgainst
            Outcome.Win -> opponent.losesTo
        }
    }

    private data class InputP1(
        val opponentsShape: Shape,
        val myShape: Shape,
    )

    private data class InputP2(
        val opponentsShape: Shape,
        val desiredOutcome: Outcome,
    )

    private enum class Shape(val score: Int) {
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

    private enum class Outcome(val score: Int) {
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
}
