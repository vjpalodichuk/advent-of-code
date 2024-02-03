package com.capital7software.aoc.aoc2016aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day03Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day03Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day03()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testCalculateValidTriangles() {
    val instance = Day03()
    val expected = 7
    val actual = instance.calculateValidTriangles(lines)

    assertEquals(expected, actual, "The number of valid triangles "
        + "is not what was expected: $expected")
  }

  @Test
  fun testCalculateValidVerticalTriangles() {
    val instance = Day03()
    val expected = 12
    val actual = instance.calculateValidTrianglesVertically(lines)

    assertEquals(expected, actual, "The number of valid triangles "
        + "is not what was expected: $expected")
  }
}
