import solutions.*
import java.io.BufferedReader
import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    measureTimeMillis {
        runDay(3)
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
    Day02(getDayInputFile(3)!!.bufferedReader()).run {
        require(solvePart1() == 8153)
        require(solvePart2() == 2342)
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
        else -> error("Day $day not yet implemented")
    }
}
