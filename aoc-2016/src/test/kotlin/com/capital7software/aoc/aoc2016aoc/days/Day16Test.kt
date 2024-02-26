package com.capital7software.aoc.aoc2016aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day16Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day16Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day16()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testGenerateChecksum() {
    val length = 20
    val instance = Day16()
    val expected = "01100"
    val actual = instance.generateChecksum(lines.first(), length)

    assertEquals(expected, actual, "The checksum "
        + "is not what was expected: $expected")
  }
}
