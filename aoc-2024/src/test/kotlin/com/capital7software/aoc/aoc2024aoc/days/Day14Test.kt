package com.capital7software.aoc.aoc2024aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day14Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day14Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day14()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testSafetyFactorAfterTime() {
    val instance = Day14()
    val width = 11
    val height = 7
    val seconds = 100
    val expected = 12L

    val actual = instance.safetyFactorAfterTime(lines, width, height, seconds)
    Assertions.assertEquals(
        expected,
        actual,
        "The Security Factor of $actual is not the expected Security Factor of $expected."
    )
  }
}
