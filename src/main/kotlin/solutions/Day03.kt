package solutions

import java.io.BufferedReader

class Day03(
    inputReader: BufferedReader,
) : Day<Int, Int>() {

    private val input = inputReader.transformLines { line ->
        val compartmentSize = line.length / 2
        Rucksack(
            all = line.toSet(),
            compartment1 = line.take(compartmentSize).toSet(),
            compartment2 = line.drop(compartmentSize).toSet(),
        )
    }

    override fun solvePart1(): Int {
        return input.map {
            it.compartment1.intersect(it.compartment2).firstOrNull()
        }.sumOf { it?.priority ?: 0 }
    }

    override fun solvePart2(): Int {
        return input.chunked(3).map { group ->
            group.map { it.all }
                .reduce { acc, rucksack -> acc.intersect(rucksack) }
                .firstOrNull()
        }.sumOf { it?.priority ?: 0 }
    }

    private val Char.priority: Int
        get() = if (this.isLowerCase()) {
            this.code - 'a'.code + 1
        } else {
            this.code - 'A'.code + 27
        }

    private data class Rucksack(
        val all: Set<Char>,
        val compartment1: Set<Char>,
        val compartment2: Set<Char>,
    )
}
