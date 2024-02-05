package com.capital7software.aoc.aoc2016aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day07Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day07Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day07()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testIpsThatSupportTls() {
    val instance = Day07()
    val expected = 2L
    val actual = instance.ipsThatSupportTls(lines)

    assertEquals(expected, actual, "The IPs that support TLS "
        + "is not what was expected: $expected")
  }

  @Test
  fun testIpsThatSupportSsl() {
    setupFromFile("inputs/input_day_07-02.txt")
    val instance = Day07()
    val expected = 3L
    val actual = instance.ipsThatSupportSsl(lines)

    assertEquals(expected, actual, "The IPs that support SSL "
        + "is not what was expected: $expected")
  }
}
