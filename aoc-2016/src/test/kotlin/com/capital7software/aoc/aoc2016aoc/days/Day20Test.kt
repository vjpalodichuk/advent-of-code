package com.capital7software.aoc.aoc2016aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day20Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day20Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day20()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testFindLowestIpAvailable() {
    val instance = Day20()
    val expected = 3L
    val actual = instance.findLowestIpAvailable(lines)

    assertEquals(expected, actual, "The lowest available IP Address "
        + "is not what was expected: $expected")
  }

  @Test
  fun testElfWithAllPresentsLeftExchange() {
    val maxIp = 9L
    val instance = Day20()
    val expected = 2L
    val actual = instance.availableIps(lines, maxIp)

    assertEquals(expected, actual, "The number of available IP Addresses "
        + "is not what was expected: $expected")
  }
}
