package com.capital7software.aoc.aoc2016aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions.assertEquals
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
  fun testErrorCorrectSignal() {
    val instance = Day06()
    val expected = "easter"
    val actual = instance.errorCorrectSignal(lines)

    assertEquals(expected, actual, "The error corrected signal "
        + "is not what was expected: $expected")
  }

  @Test
  fun testLeastCommonErrorCorrectSignal() {
    val instance = Day06()
    val expected = "advent"
    val actual = instance.errorCorrectSignal(lines, true)

    assertEquals(expected, actual, "The error corrected signal "
        + "is not what was expected: $expected")
  }
}
