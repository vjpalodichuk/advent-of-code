package com.capital7software.aoc.aoc2024aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day18Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day18Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day18()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testMinimumStepsToExit() {
    val expected = 22L

    val actual = Day18().minimumStepsToExit(
        input = lines,
        width = 7,
        height = 7,
        bytesToSimulate = 12
    )

    Assertions.assertEquals(
        expected,
        actual,
        "The minimum number of steps $actual is not the expected value $expected."
    )
  }

  @Test
  fun testFirstBlockingByte() {
    val expected = "6,1"

    val actual = Day18().firstBlockingByte(
        lines,
        width = 7,
        height = 7
    )

    Assertions.assertEquals(
        expected,
        actual,
        "The first blocking byte $actual is not the expected value $expected."
    )
  }
}
