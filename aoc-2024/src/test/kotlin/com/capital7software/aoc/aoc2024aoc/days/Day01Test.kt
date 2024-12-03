package com.capital7software.aoc.aoc2024aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day01Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day01Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day01()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testCalculateTotalDistance() {
    val instance = Day01()
    val expected = 11L

    val actual = instance.calculateTotalDistance(lines)
    Assertions.assertEquals(
        expected,
        actual,
        "The total distance of $actual is not the expected distance of $expected."
    )
  }

  @Test
  fun testCalculateSimilarityScore() {
    val instance = Day01()
    val expected = 31L

    val actual = instance.calculateSimilarityScore(lines)
    Assertions.assertEquals(
        expected,
        actual,
        "The similarity score of $actual is not the expected similarity score of $expected."
    )
  }
}
