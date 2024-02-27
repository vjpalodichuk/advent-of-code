package com.capital7software.aoc.aoc2016aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day21Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day21Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day21()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testScramble() {
    val salt = "abcde"
    val instance = Day21()
    val expected = "decab"
    val actual = instance.scramble(salt, lines)

    assertEquals(expected, actual, "The scrambled password "
        + "is not what was expected: $expected")
  }
}
