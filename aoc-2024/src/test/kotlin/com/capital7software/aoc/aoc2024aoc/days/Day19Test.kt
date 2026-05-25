package com.capital7software.aoc.aoc2024aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day19Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day19Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day19()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testPossibleDesignCount() {
    val expected = 6L

    val actual = Day19().possibleDesignCount(lines)

    Assertions.assertEquals(
        expected,
        actual,
        "The possible number of designs $actual is not the expected value $expected."
    )
  }

  @Test
  fun testTotalArrangementCount() {
    val expected = 16L

    val actual = Day19().totalArrangementCount(lines)

    Assertions.assertEquals(
        expected,
        actual,
        "The number of arrangements $actual is not the expected value $expected."
    )
  }
}
