package com.capital7software.aoc.aoc2016aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day14Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day14Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day14()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testIndexOfNthKeyGenerated() {
    val salt = lines.first()
    val limit = 64
    val instance = Day14()
    val expected = 22_728
    val actual = instance.indexOfNthKeyGenerated(salt, limit)

    assertEquals(expected, actual, "The index of the $limit key "
        + "is not what was expected: $expected")
  }

  @Test
  fun testIndexOfNthKeyGeneratedStretched() {
    val salt = lines.first()
    val limit = 1
    val instance = Day14()
    val expected = 10
    val actual = instance.indexOfNthKeyGenerated(salt, limit, true)

    assertEquals(expected, actual, "The index of the $limit stretched key "
        + "is not what was expected: $expected")
  }
}
