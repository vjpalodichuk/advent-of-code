package com.capital7software.aoc.aoc2017aoc.days

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
  fun testGetLettersOnPath() {
    val instance = Day19()
    val expected = "ABCDEF"
    val actual = instance.getLettersOnPath(lines)

    Assertions.assertEquals(
        expected, actual, "The letters encountered " +
        "is not what was expected: $expected"
    )
  }

  @Test
  fun testGetStepCountOfPath() {
    val instance = Day19()
    val expected = 38
    val actual = instance.getStepCountOfPath(lines)

    Assertions.assertEquals(
        expected, actual, "The number of steps " +
        "is not what was expected: $expected"
    )
  }
}
