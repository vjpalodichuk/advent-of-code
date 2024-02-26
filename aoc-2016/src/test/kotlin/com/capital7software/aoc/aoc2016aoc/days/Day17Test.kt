package com.capital7software.aoc.aoc2016aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day17Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day17Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day17()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testFindShortestPath1() {
    val instance = Day17()
    val expected = "DDRRRD"
    val actual = instance.findShortestPath(lines.first())

    assertEquals(expected, actual, "The path "
        + "is not what was expected: $expected")
  }

  @Test
  fun testFindShortestPath2() {
    val instance = Day17()
    val expected = "DDUDRLRRUDRD"
    val actual = instance.findShortestPath(lines[1])

    assertEquals(expected, actual, "The path "
        + "is not what was expected: $expected")
  }

  @Test
  fun testFindShortestPath3() {
    val instance = Day17()
    val expected = "DRURDRUDDLLDLUURRDULRLDUUDDDRR"
    val actual = instance.findShortestPath(lines[2])

    assertEquals(expected, actual, "The path "
        + "is not what was expected: $expected")
  }

  @Test
  fun testFindLongestPath1() {
    val instance = Day17()
    val expected = 370
    val actual = instance.findLongestPathLength(lines.first())

    assertEquals(expected, actual, "The length "
        + "is not what was expected: $expected")
  }

  @Test
  fun testFindLongestPath2() {
    val instance = Day17()
    val expected = 492
    val actual = instance.findLongestPathLength(lines[1])

    assertEquals(expected, actual, "The length "
        + "is not what was expected: $expected")
  }

  @Test
  fun testFindLongestPath3() {
    val instance = Day17()
    val expected = 830
    val actual = instance.findLongestPathLength(lines[2])

    assertEquals(expected, actual, "The length "
        + "is not what was expected: $expected")
  }
}
