package com.capital7software.aoc.aoc2017aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day13Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day13Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day13()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testTakeTrip() {
    val instance = Day13()
    val expected = Pair(0, 24)

    val actual = instance.takeTrip(lines)

    Assertions.assertEquals(
        expected, actual, "The severity " +
        "is not what was expected: $expected"
    )
  }

  @Test
  fun testTakeTripNoCatch() {
    val instance = Day13()
    val expected = Pair(10, 0)

    val actual = instance.takeTrip(lines, true)

    Assertions.assertEquals(
        expected, actual, "The severity " +
        "is not what was expected: $expected"
    )
  }
}
