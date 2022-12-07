package solutions

import utils.min
import java.io.BufferedReader

class Day07(
    inputReader: BufferedReader,
) : Day<Int, Int>() {

    private val input = inputReader.transformLines {
        if (it.startsWith("$ cd")) {
            Command.Cd(it.drop(5))
        } else if (it.startsWith("$ ls")) {
            Command.Ls
        } else if (it.startsWith("dir")) {
            Output.Dir(it.drop(4))
        } else {
            val s = it.split(" ")
            Output.File(s[0].toInt())
        }
    }

    private sealed interface Line

    private sealed interface Command : Line {
        class Cd(val path: String) : Command
        object Ls : Command
    }

    private sealed interface Output : Line {
        class File(
            val size: Int,
        ) : Output

        class Dir(
            val name: String,
        ) : Output
    }

    private class Node(
        var size: Int,
        val children: MutableList<Node>,
    )

    // file tree with computed sizes
    private val tree: Node by lazy {
        val map = mutableMapOf<String, Node>()
        val currentPath = mutableListOf<String>()

        fun pathStr(path: List<String>): String {
            return path.joinToString(separator = "/", prefix = "/")
        }

        val root = Node(size = 0, children = mutableListOf())
        map[pathStr(currentPath)] = root

        input.forEach { line ->
            when (line) {
                is Command.Cd -> {
                    when (line.path) {
                        "/" -> currentPath.clear()
                        ".." -> currentPath.removeLast()
                        else -> currentPath.addAll(line.path.split("/"))
                    }
                }

                Command.Ls -> Unit

                is Output.Dir -> {
                    val parentPath = pathStr(currentPath)
                    val parentNode = map[parentPath] ?: error("parent does not exist")
                    val dirNode = Node(
                        size = 0,
                        children = mutableListOf(),
                    )
                    map[pathStr(currentPath + line.name)] = dirNode
                    parentNode.children += dirNode
                }

                is Output.File -> {
                    val parentPath = pathStr(currentPath)
                    val parentNode = map[parentPath] ?: error("parent does not exist")
                    val fileNode = Node(
                        size = line.size,
                        children = mutableListOf(),
                    )
                    parentNode.children += fileNode
                }
            }
        }

        fun calcSize(root: Node): Int {
            return if (root.children.isNotEmpty()) {
                root.children.sumOf { calcSize(it) }.also {
                    root.size = it
                }
            } else root.size
        }
        calcSize(root)

        root
    }

    // list with all dirSizes
    private val dirSizes: List<Int> by lazy {
        mutableListOf<Int>().apply {
            fun process(node: Node) {
                node.children.forEach(::process)
                if (node.children.isNotEmpty()) {
                    add(node.size)
                }
            }
            process(tree)
        }
    }

    override fun solvePart1(): Int {
        return dirSizes.filter { it <= 100_000 }.sum()
    }

    override fun solvePart2(): Int {
        val totalSpace = 70_000_000
        val needed = 30_000_000

        val freeSpace = totalSpace - tree.size
        val needToDelete = needed - freeSpace

        return dirSizes.filter { it >= needToDelete }.min()
    }
}
