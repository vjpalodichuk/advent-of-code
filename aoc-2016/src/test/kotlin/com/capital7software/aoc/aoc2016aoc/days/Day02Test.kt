package com.capital7software.aoc.aoc2016aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day02Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day02Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day02()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testCalculateSimpleBathroomCode() {
    val instance = Day02()
    val expected = "1985"
    val actual = instance.calculateSimpleBathroomCode(lines)

    assertEquals(expected, actual, "The room code is not what was expected: $expected")
  }

  @Test
  fun testCalculateComplexBathroomCode() {
    val instance = Day02()
    val expected = "5DB3"
    val actual = instance.calculateComplexBathroomCode(lines)

    assertEquals(expected, actual, "The room code is not what was expected: $expected")
  }
}
