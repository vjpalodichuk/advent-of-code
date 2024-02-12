package com.capital7software.aoc.aoc2016aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day11Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day11Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day11()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testMinimumStepsToSolve() {
    val instance = Day11()
    val expected = 9
    val actual = instance.minimumStepsToSolve(lines)

    assertEquals(expected, actual, "The minimum number of steps to solve "
        + "is not what was expected: $expected")
  }

  @Test
  fun testMinimumStepsToSolveWithAddedPieces() {
    val instance = Day11()
    val expected = 33
    val actual = instance.minimumStepsToSolveWithAddedPieces(lines)

    assertEquals(expected, actual, "The minimum number of steps to solve "
        + "is not what was expected: $expected")
  }
}
