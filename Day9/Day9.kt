package Day9

import java.io.File
import kotlin.math.abs

private val DAY = "9"

fun main() {
    println("hi")
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

fun dayXPart2(input: List<String>): Long {
    val result = part2(input)
    println("Day $DAY Part 2: $result")
    return result
}

private fun part1(input: List<String>): Long {
    // Simply find the max area of all the points in the input
    val points = input.map { Point(it) }

    return getAllAreas(points).first().area
}

private fun getAllAreas(points: List<Point>): List<Area> {
    val areas = mutableListOf<Area>()
    for (i in 0..<points.size) {
        for (j in 1..<points.size) {
            areas.add(Area(points[i], points[j], findArea(points[i], points[j])))
        }
    }
    areas.sortByDescending { it.area }
    return areas
}

data class Area(
    val pointA: Point,
    val pointB: Point,
    val area: Long,
)

private fun findArea(
    pointA: Point,
    pointB: Point,
): Long = (abs(pointA.x - pointB.x) + 1) * (abs(pointA.y - pointB.y) + 1)

data class Point(
    val x: Long,
    val y: Long,
) {
    constructor(input: String) : this(input.split(",").first().toLong(), input.split(",").last().toLong())
}

private fun part2(input: List<String>): Long {
    val points = input.map { Point(it) }
    
    // Create green line segments between consecutive red points
    val greenSegments = mutableListOf<LineSegment>()
    for (i in points.indices) {
        val curr = points[i]
        val next = points[(i + 1) % points.size]
        greenSegments.add(LineSegment(curr, next))
    }
    
    // Find max area rectangle that only uses red/green tiles
    var maxArea = 0L
    for (i in points.indices) {
        for (j in i + 1 until points.size) {
            if (rectangleOverlapsOnlyRedGreen(points[i], points[j], points, greenSegments)) {
                maxArea = maxOf(maxArea, findArea(points[i], points[j]))
            }
        }
    }
    
    return maxArea
}

data class LineSegment(val start: Point, val end: Point) {
    fun containsPoint(point: Point): Boolean {
        // Check if point lies on this line segment
        if (start.x == end.x) {
            // Vertical line
            return point.x == start.x && point.y >= minOf(start.y, end.y) && point.y <= maxOf(start.y, end.y)
        } else {
            // Horizontal line
            return point.y == start.y && point.x >= minOf(start.x, end.x) && point.x <= maxOf(start.x, end.x)
        }
    }
}

private fun rectangleOverlapsOnlyRedGreen(p1: Point, p2: Point, redPoints: List<Point>, greenSegments: List<LineSegment>): Boolean {
    val minX = minOf(p1.x, p2.x)
    val maxX = maxOf(p1.x, p2.x)
    val minY = minOf(p1.y, p2.y)
    val maxY = maxOf(p1.y, p2.y)
    
    // Get all unique coordinates that could be in this rectangle
    val allX = redPoints.map { it.x }.distinct().filter { it in minX..maxX }
    val allY = redPoints.map { it.y }.distinct().filter { it in minY..maxY }
    
    // Check every coordinate in the rectangle
    for (y in allY) {
        for (x in allX) {
            val point = Point(x, y)
            val isRed = redPoints.contains(point)
            val isGreen = greenSegments.any { it.containsPoint(point) } || isInsidePolygon(redPoints, point)
            
            if (!isRed && !isGreen) {
                return false
            }
        }
    }
    return true
}

private fun isInsidePolygon(polygon: List<Point>, point: Point): Boolean {
    var inside = false
    var j = polygon.size - 1
    
    for (i in polygon.indices) {
        val pi = polygon[i]
        val pj = polygon[j]
        
        if (((pi.y > point.y) != (pj.y > point.y)) &&
            (point.x < (pj.x - pi.x) * (point.y - pi.y) / (pj.y - pi.y) + pi.x)) {
            inside = !inside
        }
        j = i
    }
    return inside
}

private fun fillInPoints(
    grid: MutableMap<Pair<Int, Int>, Char>,
    pointsToFill: MutableList<Point>,
    charToFillWith: Char,
) {
    pointsToFill.forEach { point ->
        grid[Pair(point.y.toInt(), point.x.toInt())] = charToFillWith
    }
}

private fun printGrid(grid: Map<Pair<Int, Int>, Char>) {
//    val maxX = grid.keys.maxOf { it.second }
//    val maxY = grid.keys.maxOf { it.first }
//
//    for (y in 0..maxY) {
//        for (x in 0..maxX) {
//            print(grid[Pair(y, x)] ?: '.')
//        }
//        println()
//    }
}

private fun testExamples() {
    val testInput =
        (
            "7,1\n" +
                "11,1\n" +
                "11,7\n" +
                "9,7\n" +
                "9,5\n" +
                "2,5\n" +
                "2,3\n" +
                "7,3"
        ).split("\n")

    // Part 1 test
    val expected1 = 50L
    val result1 = dayXPart1(testInput)
    println("Day $DAY Part 1 Example. Expected $expected1. Result: $result1")
    assert(expected1 == result1)

    // Part 2 test
    val expected2 = 24L
    val result2 = dayXPart2(testInput)
    println("Day $DAY Part 2 Example. Expected $expected2. Result: $result2")
    assert(expected2 == result2)
}
