package Day8

import java.io.File
import kotlin.math.pow
import kotlin.math.sqrt

private val DAY = "8"

fun main() {
    testExamples()
    val input = File("/Users/cohriley/Documents/AoC/AdventOfCode2025/src/Day$DAY/input.txt").readLines()
//    dayXPart1(input, 1000)
    dayXPart2(input)
}

fun dayXPart1(
    input: List<String>,
    pairsToConnect: Int,
): Long {
    val result = part1(input, pairsToConnect)
    println("Day $DAY Part 1: $result")
    return result
}

fun dayXPart2(input: List<String>): Long {
    val result = part2(input)
    println("Day $DAY Part 2: $result")
    return result
}

private fun part1(
    input: List<String>,
    pairsToConnect: Int,
): Long = doWork(input, pairsToConnect)

private fun part2(input: List<String>): Long = doWork(input, Int.MAX_VALUE)

private fun doWork(
    input: List<String>,
    pairsToConnect: Int,
): Long {
    val boxes = parseToBoxes(input)
    val boxToCircuitMap = mutableMapOf<Box, Int>()
    val circuitToBoxesMap = mutableMapOf<Int, MutableList<Box>>()
    val boxToBoxesMap = mutableMapOf<Box, MutableList<Box>>()

    // Add the first box to a circuit
    boxToCircuitMap[boxes.first()] = 1
    // Add all boxes to a circuit
    for ((index, box) in boxes.withIndex()) {
        boxToCircuitMap[box] = index
        circuitToBoxesMap[index] = mutableListOf(box)
    }

    var pairsConnected = 0

    while (pairsConnected < pairsToConnect) {
        var minDistance = Int.MAX_VALUE
        var closestBoxes: Pair<Box, Box>? = null
        // Let's find the two closest boxes
        for (i in boxes.indices) {
            val box1 = boxes[i]

            for (j in boxes.indices) {
                if (i == j) continue
                val box2 = boxes[j]
                // If these boxes are already linked, don't do it again
                if (boxToBoxesMap[box1]?.contains(box2) == true) continue
                val distance = euclideanDistance(box1, box2)
                if (distance < minDistance) {
                    minDistance = distance
                    closestBoxes = Pair(box1, box2)
                }
            }
        }

        if (closestBoxes == null) {
            println("No closest boxes found")
            break
        }

        // Link the two boxes
        boxToBoxesMap.getOrPut(closestBoxes.first) { mutableListOf() }.add(closestBoxes.second)
        boxToBoxesMap.getOrPut(closestBoxes.second) { mutableListOf() }.add(closestBoxes.first)
        pairsConnected++

        // If these boxes are already in the same circuit, keep moving.
        if (boxToCircuitMap[closestBoxes.first] == boxToCircuitMap[closestBoxes.second]) continue

        // If either box is already in a circuit, join them
        if (boxToCircuitMap.containsKey(closestBoxes.first)) {
            // Get all the boxes in box2's circuit and map them to box 1's
            val box1Circuit = boxToCircuitMap[closestBoxes.first]!!
            val circuitToReplace = boxToCircuitMap[closestBoxes.second]!!
            val boxesToMove = circuitToBoxesMap[circuitToReplace]!!
            boxesToMove.forEach { box ->
                boxToCircuitMap[box] = box1Circuit
                circuitToBoxesMap[box1Circuit]?.add(box)
            }

            // Need to clear out the circuit that was replaced
            circuitToBoxesMap.remove(circuitToReplace)
        } else if (boxToCircuitMap.containsKey(closestBoxes.second)) {
            // Get all the boxes in box1's circuit and map them to box 2's
            val box2Circuit = boxToCircuitMap[closestBoxes.second]!!
            val circuitToReplace = boxToCircuitMap[closestBoxes.first]!!
            val boxesToMove = circuitToBoxesMap[circuitToReplace]!!
            boxesToMove.forEach { box ->
                boxToCircuitMap[box] = box2Circuit
                circuitToBoxesMap[box2Circuit]?.add(box)
            }
            // Need to clear out the circuit that was replaced
            circuitToBoxesMap.remove(circuitToReplace)
        }

        if (pairsToConnect == Int.MAX_VALUE && circuitToBoxesMap.size == 1) {
            // If there's only one circuit, we're done
            return closestBoxes.first.x.toLong() * closestBoxes.second.x.toLong()
        }
    }

    // Get the 3 largest circuits
    val biggestCircuits = circuitToBoxesMap.values.sortedByDescending { it.size }.take(3)

    return biggestCircuits.map { it.size }.reduce { acc, i -> acc * i }.toLong()
}

data class Box(
    val x: Int,
    val y: Int,
    val z: Int,
)

private fun parseToBoxes(input: List<String>): List<Box> =
    input.map { line ->
        val (x, y, z) = line.split(",").map { it.toInt() }
        Box(x, y, z)
    }

private fun euclideanDistance(
    a: Box,
    b: Box,
): Int =
    sqrt(
        (a.x - b.x).toDouble().pow(2.0) +
            (a.y - b.y).toDouble().pow(2.0) +
            (a.z - b.z).toDouble().pow(2.0),
    ).toInt()

private fun testExamples() {
    val testInput =
        (
            "162,817,812\n" +
                "57,618,57\n" +
                "906,360,560\n" +
                "592,479,940\n" +
                "352,342,300\n" +
                "466,668,158\n" +
                "542,29,236\n" +
                "431,825,988\n" +
                "739,650,466\n" +
                "52,470,668\n" +
                "216,146,977\n" +
                "819,987,18\n" +
                "117,168,530\n" +
                "805,96,715\n" +
                "346,949,466\n" +
                "970,615,88\n" +
                "941,993,340\n" +
                "862,61,35\n" +
                "984,92,344\n" +
                "425,690,689"
        ).split("\n")

    // Part 1 test
    val expected1 = 40L
    val result1 = dayXPart1(testInput, 10)
    println("Day $DAY Part 1 Example. Expected $expected1. Result: $result1")
    assert(expected1 == result1)

    // Part 2 test
    val expected2 = 25272L
    val result2 = dayXPart2(testInput)
    println("Day $DAY Part 2 Example. Expected $expected2. Result: $result2")
    assert(expected2 == result2)
}
