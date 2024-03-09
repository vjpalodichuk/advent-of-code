package com.capital7software.aoc.aoc2017aoc.days

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
  fun testRecoverFrequency() {
    val instance = Day18()
    val expected = 4L
    val actual = instance.recoverFrequency(lines)

    Assertions.assertEquals(
        expected, actual, "The number of the recovered frequency " +
        "is not what was expected: $expected"
    )
  }

  @Test
  fun testGetMessageSentCount() {
    setupFromFile("inputs/input_day_18-02.txt")
    val instance = Day18()
    val expected = 3
    val actual = instance.getMessageSentCount(lines)

    Assertions.assertEquals(
        expected, actual, "The number of messages sent " +
        "is not what was expected: $expected"
    )
  }

}
