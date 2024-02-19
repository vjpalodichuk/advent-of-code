package com.capital7software.aoc.aoc2016aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day13Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day13Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day13()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testFewestNumberOfSteps() {
    val favorite = lines.first().toLong()
    val instance = Day13()
    val expected = 11L
    val actual = instance.fewestNumberOfSteps(1, 1, 7, 4, favorite)

    assertEquals(expected, actual, "The number of steps to solve the Maze "
        + "is not what was expected: $expected")
  }

  @Test
  fun testDistinctPointsReachedWithinLimit() {
    val favorite = lines.first().toLong()
    val limit = 50.0
    val instance = Day13()
    val expected = 151L
    val actual = instance.distinctPointsReachedWithinLimit(1, 1, favorite, limit)

    assertEquals(expected, actual, "The distinct number of tiles "
        + "is not what was expected: $expected")
  }
}
