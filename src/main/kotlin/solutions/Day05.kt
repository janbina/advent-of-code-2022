package solutions

import java.io.BufferedReader

class Day05(
    inputReader: BufferedReader,
) : Day<String, String>() {

    private val input by lazy {
        val lines = inputReader.transformLines { it }
        val stackLines = lines.takeWhile { it.isNotBlank() }
        val stacks = mutableMapOf<Int, String>()

        stackLines.dropLast(1).forEach { line ->
            for (i in 0..Int.MAX_VALUE) {
                val char = line.getOrNull(i * 4 + 1) ?: break
                if (char.isWhitespace()) continue
                stacks.merge(i, char.toString(), String::plus)
            }
        }

        val instructions = lines.filter { it.startsWith("move") }.map { line ->
            val splits = line.split(" ")
            Instruction(
                amount = splits[1].toInt(),
                from = splits[3].toInt() - 1,
                to = splits[5].toInt() - 1,
            )
        }

        Input(stacks, instructions)
    }

    override fun solvePart1(): String {
        return rearrange(input, canMoveMultiple = false)
    }

    override fun solvePart2(): String {
        return rearrange(input, canMoveMultiple = true)
    }

    private fun rearrange(input: Input, canMoveMultiple: Boolean): String {
        val stacks = input.stacks.toMutableMap()

        input.instructions.forEach { instruction ->
            val from = stacks[instruction.from] ?: ""
            val to = stacks[instruction.to] ?: ""
            stacks[instruction.from] = from.drop(instruction.amount)
            stacks[instruction.to] = from.take(instruction.amount).run {
                if (!canMoveMultiple) reversed() else this
            } + to
        }

        return stacks.toSortedMap().values
            .joinToString(separator = "") { it.firstOrNull()?.toString() ?: "" }
    }

    private data class Input(
        val stacks: Map<Int, String>,
        val instructions: List<Instruction>,
    )

    private data class Instruction(
        val amount: Int,
        val from: Int,
        val to: Int,
    )
}
