package Day1

import java.io.File

fun main() {
    testExamples()
    val input = File("/Users/cohriley/Documents/AoC/AdventOfCode2025/src/Day1/input.txt").readLines()
    day1Part1(input)
    day1Part2(input)
}

fun day1Part1(input: List<String>): Int {
    val result = simulateDial(input, false)
    println("Day 1 Part 1: $result")
    return result
}

fun day1Part2(input: List<String>): Int {
    val result = simulateDial(input, true)
    println("Day 1 Part 2: $result")
    return result
}

/**
 * Simulates dial rotations and counts the number of times it points to 0
 */
private fun simulateDial(
    rotationCommands: List<String>,
    countEveryZero: Boolean,
): Int {
    // Starting position is 50
    var position = 50
    var zeroCounter = 0

    for (command in rotationCommands) {
        val direction = command[0]
        val distance = command.substring(1).toInt()

        if (countEveryZero) {
            (0..<distance).forEach { turn ->
                position =
                    if (direction == 'L') {
                        (position - 1) % 100
                    } else {
                        (position + 1) % 100
                    }

                if (position == 0) {
                    zeroCounter++
                }
            }
        } else {
            position =
                if (direction == 'L') {
                    (position - distance) % 100
                } else {
                    (position + distance) % 100
                }

            if (position == 0) {
                zeroCounter++
            }
        }
    }

    return zeroCounter
}

private fun testExamples() {
    val testInput =
        listOf(
            "L68",
            "L30",
            "R48",
            "L5",
            "R60",
            "L55",
            "L1",
            "L99",
            "R14",
            "L82",
        )

    // Part 1 test
    val expected1 = 3
    val result1 = day1Part1(testInput)
    println("Day 1 Part 1 Example. Expected $expected1. Result: $result1")
    assert(expected1 == result1)

    // Part 2 test
    val expected2 = 6
    val result2 = day1Part2(testInput)
    println("Day 1 Part 2 Example. Expected $expected2. Result: $result2")
    assert(expected2 == result2)
}
