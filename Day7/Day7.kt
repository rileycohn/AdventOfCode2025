package Day7

import java.io.File

private val DAY = "7"

fun main() {
    testExamples()
    val input = File("/Users/cohriley/Documents/AoC/AdventOfCode2025/src/Day$DAY/input.txt").readLines()
    dayXPart1(input)
    dayXPart2(input)
}

fun dayXPart1(input: List<String>): Int {
    val result = part1(input)
    println("Day $DAY Part 1: $result")
    return result
}

fun dayXPart2(input: List<String>): Long {
    val result = part2(input)
    println("Day $DAY Part 2: $result")
    return result
}

private fun part1(input: List<String>): Int {
    // First, find the index of the S
    val sIndex = input[0].indexOf('S')
    val beamIndexes = mutableSetOf<Int>()
    beamIndexes.add(sIndex)
    var totalSplits = 0
    // Now go row by row and count the splits
    for (row in 1..<input.size) {
        // For each char, if it's a splitter ('^'), split the beam
        for ((index, char) in input[row].toCharArray().withIndex()) {
            if (char == '^' && beamIndexes.contains(index)) {
                // Split the beam
                beamIndexes.add(index - 1)
                beamIndexes.add(index + 1)
                beamIndexes.remove(index)
                totalSplits++
            }
        }
    }

    return totalSplits
}

private fun part2(input: List<String>): Long {
    val sIndex = input[0].indexOf('S')
    var positions = mutableMapOf<Int, Long>()
    positions[sIndex] = 1L

    for (row in 1..<input.size) {
        // Keep track of how many paths lead to this position (index)
        val newPositions = mutableMapOf<Int, Long>()
        for ((pos, count) in positions) {
            // Found splitter
            if (input[row][pos] == '^') {
                // Add count (total paths to this point so far) to the tracker for the left and right indices
                // This will help us know how many possible ways we can get to this specific point (num paths)
                newPositions[pos - 1] = (newPositions[pos - 1] ?: 0L) + count
                newPositions[pos + 1] = (newPositions[pos + 1] ?: 0L) + count
            } else {
                newPositions[pos] = (newPositions[pos] ?: 0L) + count
            }
        }

        // Keep this going, replace old positions with new
        positions = newPositions
    }

    // Now, we have the count of ways to get to each point in the positions map, so sum them up
    return positions.values.sum()
}

private fun testExamples() {
    val testInput =
        (
            ".......S.......\n" +
                "...............\n" +
                ".......^.......\n" +
                "...............\n" +
                "......^.^......\n" +
                "...............\n" +
                ".....^.^.^.....\n" +
                "...............\n" +
                "....^.^...^....\n" +
                "...............\n" +
                "...^.^...^.^...\n" +
                "...............\n" +
                "..^...^.....^..\n" +
                "...............\n" +
                ".^.^.^.^.^...^.\n" +
                "..............."
        ).split("\n").toList()

    // Part 1 test
    val expected1 = 21
    val result1 = dayXPart1(testInput)
    println("Day $DAY Part 1 Example. Expected $expected1. Result: $result1")
    assert(expected1 == result1)

    // Part 2 test
    val expected2 = 40L
    val result2 = dayXPart2(testInput)
    println("Day $DAY Part 2 Example. Expected $expected2. Result: $result2")
    assert(expected2 == result2)
}
