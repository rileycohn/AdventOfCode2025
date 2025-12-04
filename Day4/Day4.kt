package Day4

import java.io.File

private val DAY = "4"

fun main() {
    testExamples()
    val input = File("/Users/cohriley/Documents/AoC/AdventOfCode2025/src/Day$DAY/input.txt").readLines().toMutableList()
    dayXPart1(input)
    dayXPart2(input)
}

fun dayXPart1(input: MutableList<String>): Int {
    val result = doWork(input)
    println("Day $DAY Part 1: $result")
    return result
}

fun dayXPart2(input: MutableList<String>): Int {
    val result = part2(input)
    println("Day $DAY Part 2: $result")
    return result
}

/**
 * 8 directions around a point
 */
private val DIRECTIONS: List<Pair<Int, Int>> =
    listOf(
        // Column x Row
        Pair(0, -1), // Top
        Pair(1, -1), // Top right
        Pair(1, 0), // Right
        Pair(1, 1), // Bottom right
        Pair(0, 1), // Bottom
        Pair(-1, 1), // Bottom left
        Pair(-1, 0), // Left
        Pair(-1, -1), // Top Left
    )

private fun doWork(input: List<String>): Int {
    // 2D array to traverse
    val rows = input.size
    val columns = input[0].length

    var accessibleRolls = 0

    for (col in 0..<columns) {
        for (row in 0..<rows) {
            // For each spot

            // Check there is a roll here
            if (input[row][col] == '@') {
                // Now check how many rolls are in adjacent spots
                var adjacentRolls = 0

                for (direction in DIRECTIONS) {
                    // Out of bounds? Who cares
                    try {
                        if (input[row + direction.second][col + direction.first] == '@') {
                            adjacentRolls++
                        }
                    } catch (e: Exception) {
                        // Ignore
                    }
                }

                if (adjacentRolls < 4) {
                    accessibleRolls++
                }
            }
        }
    }

    return accessibleRolls
}

private fun part2(input: MutableList<String>): Int {
    // 2D array to traverse
    val rows = input.size
    val columns = input[0].length

    var totalRollsRemoved = 0
    var numPasses = 0

    while (true) {
        numPasses++
        var rollsRemovedInPass = 0

        for (col in 0..<columns) {
            for (row in 0..<rows) {
                // For each spot

                // Check there is a roll here
                if (input[row][col] == '@') {
                    // Now check how many rolls are in adjacent spots
                    var adjacentRolls = 0

                    for (direction in DIRECTIONS) {
                        // Out of bounds? Who cares
                        try {
                            if (input[row + direction.second][col + direction.first] == '@') {
                                adjacentRolls++
                            }
                        } catch (e: Exception) {
                            // Ignore
                        }
                    }

                    if (adjacentRolls < 4) {
                        rollsRemovedInPass++
                        input[row] = input[row].replaceRange(col, col + 1, "x")
                    }
                }
            }
        }

        totalRollsRemoved += rollsRemovedInPass

        if (rollsRemovedInPass == 0) break
    }

    return totalRollsRemoved
}

private fun testExamples() {
    val testInput =
        "..@@.@@@@.\n" +
            "@@@.@.@.@@\n" +
            "@@@@@.@.@@\n" +
            "@.@@@@..@.\n" +
            "@@.@@@@.@@\n" +
            ".@@@@@@@.@\n" +
            ".@.@.@.@@@\n" +
            "@.@@@.@@@@\n" +
            ".@@@@@@@@.\n" +
            "@.@.@@@.@."

    val input = testInput.split("\n").toMutableList()

    // Part 1 test
    val expected1 = 13
    val result1 = dayXPart1(input)
    println("Day $DAY Part 1 Example. Expected $expected1. Result: $result1")
    assert(expected1 == result1)

    // Part 2 test
    val expected2 = 43
    val result2 = dayXPart2(input)
    println("Day $DAY Part 2 Example. Expected $expected2. Result: $result2")
    assert(expected2 == result2)
}
