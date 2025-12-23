package Day10

import java.io.File
import kotlin.math.pow

private val DAY = "10"

fun main() {
    testExamples()
    val input = File("/Users/cohriley/Documents/AoC/AdventOfCode2025/src/Day$DAY/input.txt").readLines()
//    dayXPart1(input)
    dayXPart2(input)
}

fun dayXPart1(input: List<String>): Long {
    val result = part1(input)
    println("Day $DAY Part 1: $result")
    return result
}

fun dayXPart2(input: List<String>): Int {
    val result = part2(input)
    println("Day $DAY Part 2: $result")
    return result
}

private fun part1(input: List<String>): Long {
    val machines = parseInput(input)
    var fewestPressesForAllMachines = 0
    for (machine in machines) {
        fewestPressesForAllMachines += findFewestPressesBruteForce(machine)
    }

    return fewestPressesForAllMachines.toLong()
}

private fun findFewestPressesBruteForce(machine: Machine): Int {
    var minPresses = Int.MAX_VALUE

    // Recursive method
    fun tryAllCombos(
        buttonIndex: Int,
        currentLights: MutableList<Boolean>,
        presses: Int,
    ) {
        // Base case
        if (buttonIndex == machine.buttonWiring.size) {
            if (currentLights == machine.expectedLights) {
                minPresses = minOf(minPresses, presses)
            }
            return
        }

        // Go to the next button without pressing this one
        tryAllCombos(buttonIndex + 1, currentLights, presses)

        // Press this button
        val newLights = currentLights.toMutableList()
        for (lightIdx in machine.buttonWiring[buttonIndex]) {
            newLights[lightIdx] = !newLights[lightIdx]
        }
        tryAllCombos(buttonIndex + 1, newLights, presses + 1)
    }

    tryAllCombos(0, MutableList(machine.currentLights.size) { false }, 0)

    return minPresses
}

fun solveJoltage(machines: List<Machine>): Int =
    machines.sumOf { machine ->
        val target = machine.joltageRequirements
        val buttons = machine.buttonWiring
        val m = buttons.size
        val n = target.size

        // Create augmented matrix [A|b]
        val matrix = Array(n) { DoubleArray(m + 1) }
        for (i in 0 until n) {
            for (j in 0 until m) {
                matrix[i][j] = if (i in buttons[j]) 1.0 else 0.0
            }
            matrix[i][m] = target[i].toDouble()
        }

        // Gaussian elimination to REF
        val pivotCols = mutableListOf<Int>()
        var row = 0
        for (col in 0 until m) {
            // Find pivot
            var pivotRow = -1
            for (r in row until n) {
                if (kotlin.math.abs(matrix[r][col]) > 1e-9) {
                    pivotRow = r
                    break
                }
            }

            if (pivotRow == -1) continue // Free variable

            // Swap rows
            if (pivotRow != row) {
                val temp = matrix[row]
                matrix[row] = matrix[pivotRow]
                matrix[pivotRow] = temp
            }

            pivotCols.add(col)

            // Eliminate
            for (r in row + 1 until n) {
                if (kotlin.math.abs(matrix[r][col]) > 1e-9) {
                    val factor = matrix[r][col] / matrix[row][col]
                    for (c in 0..m) {
                        matrix[r][c] -= factor * matrix[row][c]
                    }
                }
            }
            row++
        }

        // Find free variables
        val freeVars = (0 until m).filter { it !in pivotCols }

        var minSolution = Int.MAX_VALUE

        // Enumerate free variable values
        fun enumerate(
            freeIdx: Int,
            freeValues: IntArray,
        ) {
            if (freeIdx == freeVars.size) {
                // Solve for pivot variables
                val solution = IntArray(m)
                for (i in freeVars.indices) {
                    solution[freeVars[i]] = freeValues[i]
                }

                // Back substitution
                for (r in pivotCols.size - 1 downTo 0) {
                    val col = pivotCols[r]
                    var sum = matrix[r][m]
                    for (c in col + 1 until m) {
                        sum -= matrix[r][c] * solution[c]
                    }
                    val value = sum / matrix[r][col]
                    if (value >= 0 && kotlin.math.abs(value - kotlin.math.round(value)) < 1e-9) {
                        solution[col] = kotlin.math.round(value).toInt()
                    } else {
                        return // Invalid solution
                    }
                }

                minSolution = minOf(minSolution, solution.sum())
                return
            }

            val freeVar = freeVars[freeIdx]
            val maxVal = buttons[freeVar].minOfOrNull { target[it] } ?: 0

            for (value in 0..maxVal) {
                freeValues[freeIdx] = value
                enumerate(freeIdx + 1, freeValues)
            }
        }

        enumerate(0, IntArray(freeVars.size))
        minSolution
    }

private fun part2(input: List<String>): Int {
    val machines = parseInput(input)
    return solveJoltage(machines)
}

private fun parseInput(input: List<String>): List<Machine> = input.map { Machine.parse(it) }

data class Machine(
    val expectedLights: MutableList<Boolean>,
    val buttonWiring: List<List<Int>>,
    val joltageRequirements: List<Int>,
    val currentLights: MutableList<Boolean> = expectedLights.map { false }.toMutableList(),
) {
    companion object {
        fun parse(line: String): Machine {
            val lightDiagram = line.substringAfter('[').substringBefore(']')
            val lights = lightDiagram.map { it == '#' }.toMutableList()

            val buttonWiring = mutableListOf<List<Int>>()
            var remaining = line.substringAfter(']').substringBefore('{')
            while (remaining.contains('(')) {
                val start = remaining.indexOf('(')
                val end = remaining.indexOf(')', start)
                val content = remaining.substring(start + 1, end)
                buttonWiring.add(content.split(',').map { it.trim().toInt() })
                remaining = remaining.substring(end + 1)
            }

            val joltageContent = line.substringAfter('{').substringBefore('}')
            val joltageRequirements = joltageContent.split(',').map { it.trim().toInt() }

            return Machine(lights, buttonWiring, joltageRequirements)
        }
    }

    fun pressButton(buttonIndex: Int) {
        for (light in buttonWiring[buttonIndex]) {
            currentLights[light] = !currentLights[light]
        }
    }
}

private fun testExamples() {
    val testInput =
        (
            "[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}\n" +
                "[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}\n" +
                "[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}"
        ).split("\n")

    // Part 1 test
    val expected1 = 7L
    val result1 = dayXPart1(testInput)
    println("Day $DAY Part 1 Example. Expected $expected1. Result: $result1")
    assert(expected1 == result1)

    // Part 2 test
    val expected2 = 33
    val result2 = dayXPart2(testInput)
    println("Day $DAY Part 2 Example. Expected $expected2. Result: $result2")
    assert(expected2 == result2)
}
