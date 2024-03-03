package com.capital7software.aoc.aoc2017aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions
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
  fun testGetChecksum() {
    val instance = Day02()
    val expected = 18

    val actual = instance.getChecksum(lines)

    Assertions.assertEquals(expected, actual, "The checksum "
        + "is not what was expected: $expected")
  }

  @Test
  fun testGetChecksumEvenlyDivisible() {
    setupFromFile("inputs/input_day_02-02.txt")

    val instance = Day02()
    val expected = 9

    val actual = instance.getChecksum(lines, true)

    Assertions.assertEquals(expected, actual, "The checksum "
        + "is not what was expected: $expected")
  }
}
