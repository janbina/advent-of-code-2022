package solutions

import utils.product
import java.io.BufferedReader

class Day11(
    inputReader: BufferedReader,
) : Day<Long, Long>() {

    private val input = inputReader
        .transformLines { it }
        .filter { it.isNotBlank() }
        .chunked(6, ::createMonkey)

    private data class Monkey(
        val id: Int,
        val items: List<Long>,
        val operation: (Long) -> Long,
        val testDivider: Long,
        val trueMonkeyId: Int,
        val falseMonkeyId: Int,
    )

    private fun createMonkey(lines: List<String>): Monkey {
        val id = "\\d+".toRegex().find(lines[0])?.value?.toIntOrNull() ?: error("Invalid input")
        val items = "\\d+".toRegex().findAll(lines[1]).map { it.value.toLong() }.toList()
        val operation = lines[2].trim().let {
            require(it.startsWith("Operation: new = old "))
            val operator = it.removePrefix("Operation: new = old ").take(1)
            val arg = it.removePrefix("Operation: new = old ").drop(2).toLongOrNull()
            val func: (Long) -> Long = { old ->
                val num = arg ?: old
                if (operator == "+") old + num else old * num
            }
            func
        }
        val testDivider = lines[3].trim().removePrefix("Test: divisible by ").toLong()
        val trueId = "\\d+".toRegex().find(lines[4])?.value?.toIntOrNull() ?: error("Invalid input")
        val falseId =
            "\\d+".toRegex().find(lines[5])?.value?.toIntOrNull() ?: error("Invalid input")

        return Monkey(
            id = id,
            items = items,
            operation = operation,
            testDivider = testDivider,
            trueMonkeyId = trueId,
            falseMonkeyId = falseId,
        )
    }

    override fun solvePart1(): Long {
        return runMonkeyBusiness(
            monkeys = input,
            rounds = 20,
            worryLevelReducer = { it / 3 },
        )
    }

    override fun solvePart2(): Long {
        return runMonkeyBusiness(
            monkeys = input,
            rounds = 10_000,
            worryLevelReducer = { level -> level % input.map { it.testDivider }.product() },
        )
    }

    private fun runMonkeyBusiness(
        monkeys: List<Monkey>,
        rounds: Int,
        worryLevelReducer: (Long) -> Long,
    ): Long {
        val monkeyMap = monkeys.associateBy { it.id }.toMutableMap()
        val monkeyOrder = monkeys.map { it.id }.sorted()
        val monkeyInspections = mutableMapOf<Int, Long>()

        repeat(rounds) {
            monkeyOrder.forEach { monkeyId ->
                val monkey = monkeyMap[monkeyId]!!
                monkeyInspections.merge(monkeyId, monkey.items.size.toLong(), Long::plus)
                monkey.items.forEach { item ->
                    val worryLevel = worryLevelReducer(monkey.operation(item))
                    val targetId = if (worryLevel % monkey.testDivider == 0L) {
                        monkey.trueMonkeyId
                    } else {
                        monkey.falseMonkeyId
                    }
                    monkeyMap[targetId] = monkeyMap[targetId]!!.run {
                        copy(items = items + worryLevel)
                    }
                }
                monkeyMap[monkeyId] = monkey.copy(items = emptyList())
            }
        }

        return monkeyInspections.values.sortedDescending().take(2).product()
    }
}
