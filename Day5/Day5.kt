package Day5

import java.io.File

private val DAY = "5"

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
    val inventory = getInventory(input)
    val ranges = getRanges(input)

    var freshInventory = 0L

    for (id in inventory) {
        for (range in ranges) {
            if (id in range.first..range.second) {
                freshInventory++
                // Only needs to be in one range
                break
            }
        }
    }
    return freshInventory
}

private fun part2(input: List<String>): Long {
    val ranges = getRanges(input)

    // Can we consolidate ranges?
    val sortedRanges = ranges.sortedBy { it.first }
    val consolidatedRanges = mutableListOf<Pair<Long, Long>>()
    for (range in sortedRanges) {
        // If this is the first range or if the current "max" is less than the next "min", we need a fresh range
        if (consolidatedRanges.isEmpty() || consolidatedRanges.last().second < range.first - 1) {
            consolidatedRanges.add(range)
            continue
        } else {
            // There's overlap here between the ranges
            // Remove the last range, so we can merge it with this new one
            val currMax = consolidatedRanges.removeLast()

            // Keep the original min, since we were in sorted order
            // The max needs to be the max of the original range and the new range.
            // This covers the case where the new range is a subset of the original range.
            consolidatedRanges.add(Pair(currMax.first, maxOf(range.second, currMax.second)))
        }
    }

    // Sum up all the ranges
    return consolidatedRanges.sumOf { it.second - it.first + 1 }
}

private fun getRanges(input: List<String>): List<Pair<Long, Long>> {
    val ranges = mutableListOf<Pair<Long, Long>>()
    for (line in input) {
        if (line.isEmpty()) break
        val (start, end) = line.split("-").map { it.toLong() }
        ranges.add(Pair(start, end))
    }

    return ranges
}

private fun getInventory(input: List<String>): List<Long> {
    val inventory = mutableListOf<Long>()
    var foundBlank = false
    for (line in input) {
        if (line.isEmpty()) {
            foundBlank = true
            continue
        } else if (foundBlank) {
            // Start tracking inventory
            inventory.add(line.toLong())
        }
    }

    return inventory
}

private fun testExamples() {
    val testInput =
        "3-5\n" +
            "10-14\n" +
            "16-20\n" +
            "12-18\n" +
            "\n" +
            "1\n" +
            "5\n" +
            "8\n" +
            "11\n" +
            "17\n" +
            "32"

    val input = testInput.split("\n").toMutableList()

    // Part 1 test
    val expected1 = 3L
    val result1 = dayXPart1(input)
    println("Day $DAY Part 1 Example. Expected $expected1. Result: $result1")
    assert(expected1 == result1)

    // Part 2 test
    val expected2 = 14L
    val result2 = dayXPart2(input)
    println("Day $DAY Part 2 Example. Expected $expected2. Result: $result2")
    assert(expected2 == result2)
}
