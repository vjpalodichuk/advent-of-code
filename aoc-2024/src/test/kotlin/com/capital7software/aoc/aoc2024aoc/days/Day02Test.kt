package com.capital7software.aoc.aoc2024aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day02Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day02Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day02()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testCalculateNumberOfSafeReports() {
    val instance = Day02()
    val expected = 2L
    val useDampener = false

    val actual = instance.calculateNumberOfSafeReports(lines, useDampener)
    Assertions.assertEquals(
        expected,
        actual,
        "The actual number of safe reports of $actual is not the expected " +
            "number of safe reports of $expected."
    )
  }

  @Test
  fun testCalculateNumberOfSafeReportsWithOneErrorLevel() {
    val instance = Day02()
    val expected = 4L
    val useDampener = true

    val actual = instance.calculateNumberOfSafeReports(lines, useDampener)
    Assertions.assertEquals(
        expected,
        actual,
        "The actual number of safe reports of $actual is not the expected " +
            "number of safe reports of $expected."
    )
  }
}
