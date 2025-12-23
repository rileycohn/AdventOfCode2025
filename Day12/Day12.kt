package Day12

import java.io.File

private val DAY = "12"

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
    val requirements = parseInput(input)

    var theoreticalMatches = 0L

    // Let's do a bare minimum check if there's even enough empty space to accommodate the boxes by area
    for (requirement in requirements.requirements) {
        var totalAreaAvailable = requirement.width * requirement.height
        // How many '#' are in each present
        for ((index, present) in requirement.presentsNeeded.withIndex()) {
            totalAreaAvailable -= present * getXInPresent(requirements.boxShapes[index].shape, '#')
        }

        if (totalAreaAvailable > 0) {
            theoreticalMatches++
            println("Theoretical match: $requirement. Free space $totalAreaAvailable")
        }
    }

    return theoreticalMatches
}

private fun getXInPresent(
    present: List<List<Char>>,
    x: Char,
): Int {
    var count = 0
    for (row in present) {
        for (char in row) {
            if (char == x) {
                count++
            }
        }
    }

    return count
}

private fun parseInput(input: List<String>): Input {
    val boxShapes = mutableListOf<Box>()
    val requirements = mutableListOf<Requirements>()

    var i = 0
    while (i < input.size) {
        val line = input[i]
        if (line.contains(":") && !line.contains("x")) {
            // Box shape definition
            i++
            val shape = mutableListOf<List<Char>>()
            while (i < input.size && input[i].isNotEmpty() && !input[i].contains(":")) {
                shape.add(input[i].toList())
                i++
            }
            boxShapes.add(Box(shape))
        } else if (line.contains("x")) {
            // Requirement line
            val parts = line.split(": ")
            val dimensions = parts[0].split("x")
            val width = dimensions[0].toInt()
            val height = dimensions[1].toInt()
            val presents = parts[1].split(" ").map { it.toInt() }
            requirements.add(Requirements(width, height, presents))
            i++
        } else {
            i++
        }
    }

    return Input(boxShapes, requirements)
}

data class Box(
    val shape: List<List<Char>>,
)

data class Input(
    val boxShapes: List<Box>,
    val requirements: List<Requirements>,
)

data class Requirements(
    val width: Int,
    val height: Int,
    // Using list index for box shape index
    val presentsNeeded: List<Int>,
)

private fun part2(input: List<String>): Long = 0L

private fun testExamples() {
    val testInput =
        (
            "0:\n" +
                "###\n" +
                "##.\n" +
                "##.\n" +
                "\n" +
                "1:\n" +
                "###\n" +
                "##.\n" +
                ".##\n" +
                "\n" +
                "2:\n" +
                ".##\n" +
                "###\n" +
                "##.\n" +
                "\n" +
                "3:\n" +
                "##.\n" +
                "###\n" +
                "##.\n" +
                "\n" +
                "4:\n" +
                "###\n" +
                "#..\n" +
                "###\n" +
                "\n" +
                "5:\n" +
                "###\n" +
                ".#.\n" +
                "###\n" +
                "\n" +
                "4x4: 0 0 0 0 2 0\n" +
                "12x5: 1 0 1 0 2 2\n" +
                "12x5: 1 0 1 0 3 2"
        ).split("\n")

    // Part 1 test
    val expected1 = 2L
    val result1 = dayXPart1(testInput)
    println("Day $DAY Part 1 Example. Expected $expected1. Result: $result1")
    assert(expected1 == result1)

    // Part 2 test
    val expected2 = 0L
    val result2 = dayXPart2(testInput)
    println("Day $DAY Part 2 Example. Expected $expected2. Result: $result2")
    assert(expected2 == result2)
}
