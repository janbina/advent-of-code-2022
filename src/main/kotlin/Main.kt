import solutions.*
import java.io.BufferedReader
import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    measureTimeMillis {
        runDay(12)
    }.also {
        println("Time taken: $it")
    }
    testAll()
}

private fun runDay(
    dayNumber: Int,
    input: BufferedReader? = null,
) {
    val inputReader = input
        ?: getDayInputFile(dayNumber)?.bufferedReader()
        ?: error("No input for day $dayNumber")
    val day = createDay(dayNumber, inputReader)
    println("Solving Day $dayNumber")
    println("\tPart 1 = ${day.solvePart1()}")
    println("\tPart 2 = ${day.solvePart2()}")
}

private fun testAll() {
    Day01(getDayInputFile(1)!!.bufferedReader()).run {
        require(solvePart1() == 70374)
        require(solvePart2() == 204610)
    }
    Day02(getDayInputFile(2)!!.bufferedReader()).run {
        require(solvePart1() == 11603)
        require(solvePart2() == 12725)
    }
    Day03(getDayInputFile(3)!!.bufferedReader()).run {
        require(solvePart1() == 8153)
        require(solvePart2() == 2342)
    }
    Day04(getDayInputFile(4)!!.bufferedReader()).run {
        require(solvePart1() == 441)
        require(solvePart2() == 861)
    }
    Day05(getDayInputFile(5)!!.bufferedReader()).run {
        require(solvePart1() == "ZBDRNPMVH")
        require(solvePart2() == "WDLPFNNNB")
    }
    Day06(getDayInputFile(6)!!.bufferedReader()).run {
        require(solvePart1() == 1623)
        require(solvePart2() == 3774)
    }
    Day07(getDayInputFile(7)!!.bufferedReader()).run {
        require(solvePart1() == 1390824)
        require(solvePart2() == 7490863)
    }
    Day08(getDayInputFile(8)!!.bufferedReader()).run {
        require(solvePart1() == 1688)
        require(solvePart2() == 410400)
    }
    Day09(getDayInputFile(9)!!.bufferedReader()).run {
        require(solvePart1() == 6181)
        require(solvePart2() == 2386)
    }
    Day10(getDayInputFile(10)!!.bufferedReader()).run {
        require(solvePart1() == 14920)
        require(solvePart2() == 0)
    }
    Day11(getDayInputFile(11)!!.bufferedReader()).run {
        require(solvePart1() == 50172L)
        require(solvePart2() == 11614682178)
    }
    Day12(getDayInputFile(12)!!.bufferedReader()).run {
        require(solvePart1() == 330)
        require(solvePart2() == 321)
    }
}

private fun getDayInputFile(day: Int): File? {
    val fileName = "day${day.toString().padStart(2, '0')}.txt"
    val fileUri = Day::class.java.classLoader.getResource(fileName)?.toURI() ?: return null
    return File(fileUri)
}

private fun createDay(day: Int, input: BufferedReader): Day<*, *> {
    return when (day) {
        1 -> Day01(input)
        2 -> Day02(input)
        3 -> Day03(input)
        4 -> Day04(input)
        5 -> Day05(input)
        6 -> Day06(input)
        7 -> Day07(input)
        8 -> Day08(input)
        9 -> Day09(input)
        10 -> Day10(input)
        11 -> Day11(input)
        12 -> Day12(input)
        else -> error("Day $day not yet implemented")
    }
}
