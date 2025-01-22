package com.capital7software.aoc.aoc2024aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day05Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day05Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day05()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testCalculateOccurrencesOfXmas() {
    val instance = Day05()
    val expected = 143L

    val actual = instance.sumOfValidMiddlePages(lines)
    Assertions.assertEquals(
        expected,
        actual,
        "The actual sum of $actual is not the expected sum of $expected."
    )
  }

  @Test
  fun testSumOfInvalidMiddlePages() {
    val instance = Day05()
    val expected = 123L

    val actual = instance.sumOfInvalidMiddlePages(lines)
    Assertions.assertEquals(
        expected,
        actual,
        "The actual sum of $actual is not the expected sum of $expected."
    )
  }
}
