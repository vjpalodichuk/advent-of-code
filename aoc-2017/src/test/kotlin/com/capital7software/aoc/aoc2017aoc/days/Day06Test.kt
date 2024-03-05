package com.capital7software.aoc.aoc2017aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions
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
  fun testCyclesRequiredToDetectLoop() {
    val instance = Day06()
    val expected = 5
    val actual = instance.cyclesRequiredToDetectLoop(lines.first())

    Assertions.assertEquals(
        expected, actual, "The number of cycles " +
        "is not what was expected: $expected"
    )
  }

  @Test
  fun testCyclesRequiredToDetectLoopLength() {
    val instance = Day06()
    val expected = 4
    val actual = instance.cyclesRequiredToDetectLoop(lines.first(), true)

    Assertions.assertEquals(
        expected, actual, "The length of the infinite loop cycle " +
        "is not what was expected: $expected"
    )
  }
}
