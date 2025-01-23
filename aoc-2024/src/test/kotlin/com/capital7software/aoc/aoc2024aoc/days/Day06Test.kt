package com.capital7software.aoc.aoc2024aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day06Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day06Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day06()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testDistinctNumberOfGuardPositions() {
    val instance = Day06()
    val expected = 41

    val actual = instance.distinctNumberOfGuardPositions(lines)
    Assertions.assertEquals(
        expected,
        actual,
        "The actual number of $actual is not the expected number of $expected."
    )
  }

  @Test
  fun testNumberOfPossibleObstructionPositions() {
    val instance = Day06()
    val expected = 6

    val actual = instance.numberOfPossibleObstructionPositions(lines)
    Assertions.assertEquals(
        expected,
        actual,
        "The actual number of $actual is not the expected number of $expected."
    )
  }
}
