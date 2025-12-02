package Day2

import java.io.File

fun main() {
    testExamples()
    val input = File("/Users/cohriley/Documents/AoC/AdventOfCode2025/src/Day2/input.txt").readText()
    day2Part1(input)
    day2Part2(input)
}

fun day2Part1(input: String): Long {
    val ranges = parseRange(input)
    val result = processIds(ranges, true)
    println("Day 2 Part 1: $result")
    return result
}

fun day2Part2(input: String): Long {
    val ranges = parseRange(input)
    val result = processIds(ranges, false)
    println("Day 2 Part 2: $result")
    return result
}

private fun processIds(
    input: List<Pair<Long, Long>>,
    matchHalves: Boolean,
): Long {
    val invalidIds = mutableListOf<Long>()
    input.forEach { pair ->
        for (i in pair.first..pair.second) {
            // Convert i to a string and split it in half and compare the halves
            val stringI = i.toString()

            if (matchHalves) {
                val left = stringI.take((stringI.length / 2))
                val right = stringI.substring((stringI.length / 2), stringI.length)

                if (left == right) {
                    println("Match! Left: $left and Right: $right")
                    invalidIds.add(i)
                }
            } else {
                // We need to find any repeating pattern

                // Start at 1, compare to all subsequent substrings of length j
                for (j in 1..<stringI.length) {
                    val pattern = stringI.take(j)
                    var allMatch = true
                    for (x in j until stringI.length step j) {
                        if (x + j <= stringI.length) {
                            val nextSeq = stringI.substring(x, x + j)
                            if (pattern != nextSeq) {
                                allMatch = false
                                break
                            }
                        } else {
                            allMatch = false
                            break
                        }
                    }

                    if (allMatch) {
                        println("Match! $stringI")
                        invalidIds.add(i)
                        // Don't need to check for other sequences
                        break
                    }
                }
            }
        }
    }

    return invalidIds.sum()
}

private fun testExamples() {
    val testInput =
        "11-22,95-115,998-1012,1188511880-1188511890,222220-222224," +
            "1698522-1698528,446443-446449,38593856-38593862,565653-565659," +
            "824824821-824824827,2121212118-2121212124"

    // Part 1 test
    val expected1 = 1227775554L
    val result1 = day2Part1(testInput)
    println("Day 2 Part 1 Example. Expected $expected1. Result: $result1")
    assert(expected1 == result1)

    // Part 2 test
    val expected2 = 4174379265L
    val result2 = day2Part2(testInput)
    println("Day 2 Part 2 Example. Expected $expected2. Result: $result2")
    assert(expected2 == result2)
}

private fun parseRange(input: String): List<Pair<Long, Long>> {
    val ranges = input.split(",").toList()
    val pairs = mutableListOf<Pair<Long, Long>>()
    ranges.forEach {
        val split = it.split("-")
        val min = split[0]
        val max = split[1]
        pairs.add(Pair(min.toLong(), max.toLong()))
    }

    return pairs
}
