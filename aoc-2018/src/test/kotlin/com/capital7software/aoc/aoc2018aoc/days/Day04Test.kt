package com.capital7software.aoc.aoc2018aoc.days

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
  fun testGetGuardChecksum() {
    val instance = Day04()
    val expected = 240
    val actual = instance.getGuardChecksum(lines)
    Assertions.assertEquals(expected, actual, "The checksum  "
        + "is not what was expected: $expected")
  }

  @Test
  fun testGetGuardAlternateChecksum() {
    val instance = Day04()
    val expected = 4455
    val actual = instance.getGuardAlternateChecksum(lines)
    Assertions.assertEquals(expected, actual, "The checksum "
        + "is not what was expected: $expected")
  }
}
