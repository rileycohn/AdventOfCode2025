package Day3

import java.io.File
import java.util.PriorityQueue

private val DAY = "3"

fun main() {
    testExamples()
    val input = File("/Users/cohriley/Documents/AoC/AdventOfCode2025/src/Day$DAY/input.txt").readLines()
    println("==============================================================================")
    dayXPart1(input)
    dayXPart2(input)
}

fun dayXPart1(input: List<String>): Long {
    val result = doWork(input)
    println("Day $DAY Part 1: $result")
    return result
}

fun dayXPart2(input: List<String>): Long {
    val result = part2(input)
    println("Day $DAY Part 2: $result")
    return result
}

private fun doWork(input: List<String>): Long {
    var total = 0L
    for (line in input) {
        // We need to track the two largest numbers per line
        var max = 0
        var maxIndex = 0
        var secondMax = 0
        for ((index, char) in line.withIndex()) {
            val digitInt = char.digitToInt()
            if (digitInt > max && index < line.length - 1) {
                max = digitInt
                maxIndex = index

                // If we change the max, we have to reset second max
                secondMax = 0
            } else if (digitInt > secondMax && index > maxIndex) {
                secondMax = digitInt
            }
        }

        // Concat the numbers
        total += (max.toString() + secondMax.toString()).toLong()
    }

    return total
}

private fun part2(input: List<String>): Long {
    var total = 0L
    for (line in input) {
        // For each line, we need to find the max number with index < length - 12, then the subsequent 11 highest numbers, in order
        var result = ""
        var remaining = 12

        for ((index, char) in line.withIndex()) {
            val availableDigits = line.length - index
            if (availableDigits == remaining) {
                // We have to take the rest
                result += line.substring(index)
                break
            } else if (remaining > 0) {
                // Find the largest digit that still enabled us to find the remaining amount
                val maxDigitLeft = line.substring(index, index + availableDigits - remaining + 1).max()
                if (line[index] == maxDigitLeft) {
                    result += line[index]
                    remaining--
                }
            }
        }

        total += result.toLong()
    }

    return total
}

private fun testExamples() {
    val testInput =
        """
        987654321111111
        811111111111119
        234234234234278
        818181911112111
        """.trimIndent().split("\n")

    // Part 1 test
    val expected1 = 357L
    val result1 = dayXPart1(testInput)
    println("Day $DAY Part 1 Example. Expected $expected1. Result: $result1")
    assert(expected1 == result1)

    // Part 2 test
    val expected2 = 3121910778619L
    val result2 = dayXPart2(testInput)
    println("Day $DAY Part 2 Example. Expected $expected2. Result: $result2")
    assert(expected2 == result2)
}
