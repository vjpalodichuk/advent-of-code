package com.capital7software.aoc.aoc2024aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day07Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day07Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day07()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testSumOfPossiblyTrueTestValuesAddAndMultiplyOnly() {
    val instance = Day07()
    val expected = 3749L

    val actual = instance.sumOfPossiblyTrueTestValuesAddAndMultiplyOnly(lines)
    Assertions.assertEquals(
        expected,
        actual,
        "The actual sum of $actual is not the expected sum of $expected."
    )
  }

  @Test
  fun testNumberOfPossibleObstructionPositions() {
    val instance = Day07()
    val expected = 11387L

    val actual = instance.sumOfPossiblyTrueTestValuesAllOperators(lines)
    Assertions.assertEquals(
        expected,
        actual,
        "The actual sum of $actual is not the expected sum of $expected."
    )
  }
}
