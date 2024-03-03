package com.capital7software.aoc.aoc2016aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day25Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day25Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day25()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testGetFewestNumberOfSteps() {
    val instance = Day25()
    val expected = 192
    val actual = instance.getLowestPositiveInteger(lines)

    assertEquals(expected, actual, "The fewest number of steps "
        + "is not what was expected: $expected")
  }
}
