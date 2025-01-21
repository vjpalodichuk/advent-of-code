package com.capital7software.aoc.aoc2024aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day04Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day04Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day04()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testCalculateOccurrencesOfXmas() {
    val instance = Day04()
    val expected = 18

    val actual = instance.calculateOccurrencesOfXmas(lines)
    Assertions.assertEquals(
        expected,
        actual,
        "The actual occurrences of $actual is not the expected " +
            "number of occurrences of $expected."
    )
  }

  @Test
  fun testCalculateOccurrencesOfMas() {
    val instance = Day04()
    val expected = 9

    val actual = instance.calculateOccurrencesOfMas(lines)
    Assertions.assertEquals(
        expected,
        actual,
        "The actual occurrences of $actual is not the expected " +
            "number of occurrences of $expected."
    )
  }
}
