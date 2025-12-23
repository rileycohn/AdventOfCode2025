package Day11

import java.io.File

private val DAY = "11"

fun main() {
    testExamples()
    val input = File("/Users/cohriley/Documents/AoC/AdventOfCode2025/src/Day$DAY/input.txt").readLines()
    dayXPart1(input)
    dayXPart2(input)
}

fun dayXPart1(input: List<String>): Long {
    val result = part1(input)
    println("Day $DAY Part 1: $result")
    return result
}

fun dayXPart2(input: List<String>): Long {
    val result = part2(input)
    println("Day $DAY Part 2: $result")
    return result
}

private fun part1(input: List<String>): Long {
    val graph = parseToMap(input)
    // Solve with BFS
    val start = "you"
    val end = "out"
    val queue = ArrayDeque<String>()
    queue.add(start)
    var count = 0L
    while (queue.isNotEmpty()) {
        val current = queue.removeFirst()
        if (current == end) {
            count++
        }
        graph[current]?.forEach { neighbor ->
            queue.add(neighbor)
        }
    }

    return count
}

private fun parseToMap(input: List<String>): Map<String, Set<String>> {
    val map = mutableMapOf<String, MutableSet<String>>()
    input.forEach { line ->
        val (key, values) = line.split(": ")
        val valueList = values.split(" ")
        map[key] = valueList.toMutableSet()
    }
    return map
}

private fun part2(input: List<String>): Long {
    val graph = parseToMap(input)
    // Track the nodeName, hasDac, and hasFft values for memoization to reduce computations later on
    val memo = mutableMapOf<Triple<String, Boolean, Boolean>, Long>()

    fun dfs(
        node: String,
        hasDac: Boolean,
        hasFft: Boolean,
    ): Long {
        // Base condition - we found the end, record if we have seen dac and fft
        if (node == "out") return if (hasDac && hasFft) 1L else 0L

        val key = Triple(node, hasDac, hasFft)
        // Reuse past decisions
        memo[key]?.let { return it }

        val newDac = hasDac || node == "dac"
        val newFft = hasFft || node == "fft"

        // Keep going down and sum up all the paths we get to out while seeing the required nodes
        val result = graph[node]?.sumOf { dfs(it, newDac, newFft) } ?: 0L

        // Store the results for later
        memo[key] = result
        return result
    }

    return dfs("svr", hasDac = false, hasFft = false)
}

private fun testExamples() {
    val testInput =
        (
            "aaa: you hhh\n" +
                "you: bbb ccc\n" +
                "bbb: ddd eee\n" +
                "ccc: ddd eee fff\n" +
                "ddd: ggg\n" +
                "eee: out\n" +
                "fff: out\n" +
                "ggg: out\n" +
                "hhh: ccc fff iii\n" +
                "iii: out"
        ).split("\n")

    // Part 1 test
    val expected1 = 5L
    val result1 = dayXPart1(testInput)
    println("Day $DAY Part 1 Example. Expected $expected1. Result: $result1")
    assert(expected1 == result1)

    val testInput2 =
        (
            "svr: aaa bbb\n" +
                "aaa: fft\n" +
                "fft: ccc\n" +
                "bbb: tty\n" +
                "tty: ccc\n" +
                "ccc: ddd eee\n" +
                "ddd: hub\n" +
                "hub: fff\n" +
                "eee: dac\n" +
                "dac: fff\n" +
                "fff: ggg hhh\n" +
                "ggg: out\n" +
                "hhh: out"
        ).split("\n")

    // Part 2 test
    val expected2 = 2L
    val result2 = dayXPart2(testInput2)
    println("Day $DAY Part 2 Example. Expected $expected2. Result: $result2")
    assert(expected2 == result2)
}
