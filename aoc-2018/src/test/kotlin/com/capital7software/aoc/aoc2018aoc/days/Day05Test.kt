package com.capital7software.aoc.aoc2018aoc.days

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
  fun testGetRemainingCount() {
    val instance = Day05()
    val expected = 10
    val actual = instance.getRemainingCount(lines.first())
    Assertions.assertEquals(expected, actual, "The length of the remaining units  "
        + "is not what was expected: $expected")
  }

  @Test
  fun testGetScrubRemainingCount() {
    val instance = Day05()
    val expected = 4
    val actual = instance.getRemainingCount(lines.first(), true)
    Assertions.assertEquals(expected, actual, "The length of the remaining units  "
        + "is not what was expected: $expected")
  }

}
