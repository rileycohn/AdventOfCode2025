package DayX

import java.io.File

fun main() {
    testExamples()
    val input = File("/Users/cohriley/Documents/AoC/AdventOfCode2025/src/DayX/input.txt").toString()
    dayXPart1(input)
    dayXPart2(input)
}

fun dayXPart1(input: String): Int {
    val result = doWork(input)
    println("Day X Part 1: $result")
    return result
}

fun dayXPart2(input: String): Int {
    val result = doWork(input)
    println("Day X Part 2: $result")
    return result
}

private fun doWork(input: String): Int = 0

private fun testExamples() {
    val testInput = ""

    // Part 1 test
    val expected1 = 0
    val result1 = dayXPart1(testInput)
    println("Day X Part 1 Example. Expected $expected1. Result: $result1")
    assert(expected1 == result1)

    // Part 2 test
    val expected2 = 0
    val result2 = dayXPart2(testInput)
    println("Day X Part 2 Example. Expected $expected2. Result: $result2")
    assert(expected2 == result2)
}
