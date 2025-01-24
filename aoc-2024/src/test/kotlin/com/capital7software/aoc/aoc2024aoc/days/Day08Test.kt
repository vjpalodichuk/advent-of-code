package com.capital7software.aoc.aoc2024aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day08Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day08Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day08()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testCountUniqueBoundedAntinodeLocations() {
    val instance = Day08()
    val expected = 14

    val actual = instance.countUniqueBoundedAntinodeLocations(lines)
    Assertions.assertEquals(
        expected,
        actual,
        "The actual number of $actual is not the expected number of $expected."
    )
  }

  @Test
  fun testCountNewModelBoundedAntinodeLocations() {
    val instance = Day08()
    val expected = 34

    val actual = instance.countNewModelBoundedAntinodeLocations(lines)
    Assertions.assertEquals(
        expected,
        actual,
        "The actual number of $actual is not the expected number of $expected."
    )
  }
}
