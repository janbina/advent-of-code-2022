package solutions

import java.io.BufferedReader

class Day13(
    inputReader: BufferedReader,
) : Day<Int, Int>() {

    private val input: List<Element> = inputReader
        .transformLines { it }
        .filter(String::isNotBlank)
        .map(::parseString)

    override fun solvePart1(): Int {
        return input
            .chunked(2)
            .withIndex()
            .sumOf { (index, packets) ->
                if (packets[0] < packets[1]) {
                    index + 1
                } else 0
            }
    }

    override fun solvePart2(): Int {
        val p1 = parseString("[[2]]")
        val p2 = parseString("[[6]]")

        val list = input.toMutableList().apply {
            addAll(listOf(p1, p2))
            sort()
        }

        return (list.indexOf(p1) + 1) * (list.indexOf(p2) + 1)
    }

    private sealed interface Element : Comparable<Element> {
        data class L(val list: List<Element>) : Element {
            override fun toString(): String {
                return list.joinToString(separator = ",", prefix = "[", postfix = "]")
            }

            override fun compareTo(other: Element): Int {
                return when (other) {
                    is I -> this.compareTo(L(listOf(other)))
                    is L -> this.list.compareTo(other.list)
                }
            }

            private fun <T : Comparable<T>> List<T>.compareTo(other: List<T>): Int {
                return this.zip(other)
                    .map { it.first.compareTo(it.second) }
                    .firstOrNull { it != 0 }
                    ?: (this.size - other.size)
            }
        }

        data class I(val int: Int) : Element {
            override fun toString(): String {
                return "$int"
            }

            override fun compareTo(other: Element): Int {
                return when (other) {
                    is I -> this.int - other.int
                    is L -> L(listOf(this)).compareTo(other)
                }
            }
        }
    }

    private fun parseString(input: String): Element {
        val result = mutableListOf<Element>()
        var nesting = 0
        var listStartIndex = -1

        fun String.lastInt(): Int? {
            return takeLastWhile { it.isDigit() }.toIntOrNull()
        }

        input.forEachIndexed { index, c ->
            if (c == '[') {
                if (nesting == 0) {
                    listStartIndex = index
                }
                nesting++
            }
            if (c == ']') {
                nesting--
                if (nesting == 0) {
                    result.add(parseString(input.substring(listStartIndex + 1, index)))
                }
            }
            if (c == ',') {
                if (nesting == 0) {
                    input.take(index).lastInt()?.let {
                        result.add(Element.I(it))
                    }
                }
            }
        }

        if (nesting == 0) {
            input.lastInt()?.let {
                result.add(Element.I(it))
            }
        }

        return Element.L(result)
    }
}
