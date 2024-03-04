package com.capital7software.aoc.aoc2017aoc.days

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
  fun testStepsToExitLoop() {
    val instance = Day05()
    val expected = 5
    val actual = instance.stepsToExitLoop(lines)

    Assertions.assertEquals(
        expected, actual, "The number of steps to take " +
        "is not what was expected: $expected"
    )
  }

  @Test
  fun testStepsToExitLoopNewRules() {
    val instance = Day05()
    val expected = 10
    val actual = instance.stepsToExitLoop(lines, true)

    Assertions.assertEquals(
        expected, actual, "The number of steps to take " +
        "is not what was expected: $expected"
    )
  }
}
