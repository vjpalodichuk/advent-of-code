package com.capital7software.aoc.aoc2016aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day24Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day24Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day24()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testGetFewestNumberOfSteps() {
    val id = 0
    val instance = Day24()
    val expected = 14
    val actual = instance.getFewestNumberOfSteps(lines, id)

    assertEquals(expected, actual, "The fewest number of steps "
        + "is not what was expected: $expected")
  }

  @Test
  fun testGetFewestNumberOfStepsInCycle() {
    val id = 0
    val instance = Day24()
    val expected = 20
    val actual = instance.getFewestNumberOfStepsInCycle(lines, id)

    assertEquals(expected, actual, "The fewest number of steps in cycle "
        + "is not what was expected: $expected")
  }
}
