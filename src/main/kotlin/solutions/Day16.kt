package solutions

import utils.floydWarshall
import java.io.BufferedReader

class Day16(
    inputReader: BufferedReader,
) : Day<Int, Int>() {

    private val input = inputReader.transformLines(::parseValve)

    // map from valve id to valve representation
    // this create valve that has distances to all other valves with flowRate > 0
    // only keeps valves with flowRate > 0 and initial valve "AA", rest is useless
    // sorted by flowRate descending
    private val valves: Map<String, Valve> by lazy {
        val originalDistanceArray = input.map { source ->
            input.map { dest ->
                when {
                    source.id == dest.id -> 0
                    else -> source.distanceTo[dest.id] ?: 100000
                }
            }.toTypedArray()
        }.toTypedArray()

        val distanceArray = floydWarshall(originalDistanceArray)

        val withDistance = input.mapIndexed { sourceIndex, valve ->
            val dist = input.mapIndexedNotNull { destIndex, destValve ->
                if (sourceIndex != destIndex && destValve.flowRate > 0) {
                    destValve.id to distanceArray[sourceIndex][destIndex]
                } else null
            }.associate { it }
            valve.copy(distanceTo = dist)
        }

        withDistance
            .sortedByDescending { it.flowRate }
            .associateBy { it.id }
            .filter { it.value.flowRate > 0 || it.key == "AA" }
    }

    private fun parseValve(line: String): Valve {
        //Valve ZF has flow rate=7; tunnels lead to valves SC, BY, PM, LW, CJ
        val splits = line.split(" ")
        val id = splits[1]
        val flowRate = splits[4].drop(5).dropLast(1).toInt()
        val connectedTo = splits.drop(9).map { it.filter { it.isLetter() } }
        return Valve(id, flowRate, connectedTo.associateWith { 1 })
    }

    private data class Valve(
        val id: String,
        val flowRate: Int,
        val distanceTo: Map<String, Int>,
    )

    override fun solvePart1(): Int {
        fun solve(
            startValve: String,
            remainingTime: Int,
            closedValves: Set<String>,
            released: Int,
        ): Int {
            return closedValves.maxOfOrNull { target ->
                val tto = remainingTime - valves[startValve]!!.distanceTo[target]!! - 1
                if (tto > 0) {
                    solve(
                        startValve = target,
                        remainingTime = tto,
                        closedValves = closedValves - target,
                        released = released + valves[target]!!.flowRate * tto,
                    )
                } else released
            } ?: released
        }

        return solve("AA", 30, valves.keys - "AA", 0)
    }

    override fun solvePart2(): Int {
        fun solve(
            aS: String,
            aT: Int,
            bS: String,
            bT: Int,
            closed: Set<String>,
            released: Int,
        ): Int {
            return (closed.maxOfOrNull { target ->
                val tto = aT - valves[aS]!!.distanceTo[target]!! - 1
                val ttoe = bT - valves[bS]!!.distanceTo[target]!! - 1
                if (tto > 0 && tto > ttoe) {
                    solve(
                        aS = target,
                        aT = tto,
                        bS = bS,
                        bT = bT,
                        closed = closed - target,
                        released = released + valves[target]!!.flowRate * tto,
                    )
                } else if (ttoe > 0) {
                    solve(
                        aS = aS,
                        aT = aT,
                        bS = target,
                        bT = ttoe,
                        closed = closed - target,
                        released = released + valves[target]!!.flowRate * ttoe,
                    )
                } else released
            } ?: released)
        }

        return solve("AA", 26, "AA", 26, valves.keys - "AA", 0)
    }
}
