package com.capital7software.aoc.aoc2016aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day10Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day10Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day10()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testBotIdResponsibleForComparing() {
    val instance = Day10()
    val compareValues = setOf(5, 2)
    val expected = "2"
    val actual = instance.botIdResponsibleForComparing(lines, compareValues)

    assertEquals(expected, actual, "The ID of the Bot that handles values 2 and 5 "
        + "is not what was expected: $expected")
  }

  @Test
  fun testProductOfOutputBins() {
    val instance = Day10()
    val compareValues = setOf("0", "1", "2")
    val expected = 30L
    val actual = instance.productOfOutputBins(lines, compareValues)

    assertEquals(expected, actual, "The product of the sum of Bins 0, 1, and 2 "
        + "is not what was expected: $expected")
  }
}
