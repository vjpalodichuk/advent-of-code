package com.capital7software.aoc.aoc2024aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day10Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day10Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day10()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testSumAllTrailheadScores() {
    val instance = Day10()
    val expected = 36L

    val actual = instance.sumAllTrailheadScores(lines)
    Assertions.assertEquals(
        expected,
        actual,
        "The sum of $actual is not the expected sum of $expected."
    )
  }

  @Test
  fun testSumAllTrailheadRatings() {
    val instance = Day10()
    val expected = 81L

    val actual = instance.sumAllTrailheadRatings(lines)
    Assertions.assertEquals(
        expected,
        actual,
        "The actual sum of $actual is not the expected sum of $expected."
    )
  }
}
