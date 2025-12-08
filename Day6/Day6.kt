package Day6

import java.io.File

private val DAY = "6"

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
    var grandTotal = 0L
    // We need each line in the input split by spaces
    for (problem in 0..<input[0].trim().split(Regex("\\s+")).size) {
        val numbers = mutableListOf<Long>()
        for (i in 0..input.size - 2) {
            // We will gather all the numbers in the first column
            numbers.add(
                input[i]
                    .trim()
                    .split(Regex("\\s+"))[problem]
                    .trim()
                    .toLong(),
            )
        }

        val operator = input[input.size - 1].trim().split(Regex("\\s+"))[problem]

        grandTotal += doMath(numbers, operator)
    }

    return grandTotal
}

private fun doMath(
    numbers: List<Long>,
    operator: String,
): Long {
    var total = numbers[0]
    for (number in 1..<numbers.size) {
        when (operator) {
            "*" -> total *= numbers[number]
            "+" -> total += numbers[number]
            else -> throw Exception("Unknown operator: $operator")
        }
    }
    return total
}

private fun part2(input: List<String>): Long {
    var grandTotal = 0L
    val maxNumsPerProblem = maxNumPerProblem(input)
    // We need each line in the input split by spaces
    val splitLines = mutableListOf<List<String>>()
    // Iterate each string in the input
    for (line in 0..<input.size - 1) {
        val curLine = mutableListOf<String>()
        // On the current string, do a substring for each problem
        // 0->3, " ", 4->6, " ", ...
        var currIndex = 0
        for (problem in 0..<maxNumsPerProblem.size) {
            curLine.add(input[line].substring(currIndex, currIndex + maxNumsPerProblem[problem]!!))
            currIndex += maxNumsPerProblem[problem]!! + 1

            if (currIndex > input[line].length - 1) {
                break
            }
        }

        splitLines.add(curLine)
    }

    // Now we have the numbers in each line, including spaces
    // Now, iterate backwards over each problem and character
    for (problem in maxNumsPerProblem.size - 1 downTo 0) {
        val operator = input[input.size - 1].trim().split(Regex("\\s+"))[problem]
        val numbers = mutableListOf<Long>()
        for (char in 0..<maxNumsPerProblem[problem]!!) {
            var columnNum = ""
            for (line in splitLines.indices) {
                // Get the string num for the current line
                // " 46" ?
                columnNum += splitLines[line][problem].reversed()[char]
            }

            // Now we have a column num
            numbers.add(columnNum.trim().toLong())
        }

        grandTotal += doMath(numbers, operator)
    }

    return grandTotal
}

private fun maxNumPerProblem(input: List<String>): Map<Int, Int> {
    val maxNums = mutableMapOf<Int, Int>()
    for (problem in 0..<input[0].trim().split(Regex("\\s+")).size) {
        for (i in 0..<input.size - 1) {
            // We will gather all the numbers in the first column
            val num =
                input[i]
                    .trim()
                    .split(Regex("\\s+"))[problem]
                    .trim()
                    .length

            if (num > (maxNums[problem] ?: 0)) maxNums[problem] = num
        }
    }

    return maxNums
}

private fun testExamples() {
    val testInput =
        "123 328  51 64 \n" +
            " 45 64  387 23 \n" +
            "  6 98  215 314\n" +
            "*   +   *   + "

    val input = testInput.split("\n").toList()

    // Part 1 test
    val expected1 = 4277556L
    val result1 = dayXPart1(input)
    println("Day $DAY Part 1 Example. Expected $expected1. Result: $result1")
    assert(expected1 == result1)

    // Part 2 test
    val expected2 = 3263827L
    val result2 = dayXPart2(input)
    println("Day $DAY Part 2 Example. Expected $expected2. Result: $result2")
    assert(expected2 == result2)
}
