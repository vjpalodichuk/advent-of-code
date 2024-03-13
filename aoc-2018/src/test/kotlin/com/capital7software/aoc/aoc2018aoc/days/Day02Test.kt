package com.capital7software.aoc.aoc2018aoc.days

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
    val expected = 12
    val actual = instance.getChecksum(lines)
    Assertions.assertEquals(expected, actual, "The checksum "
        + "is not what was expected: $expected")
  }

  @Test
  fun testGetPrototypeBoxIds() {
    setupFromFile("inputs/input_day_02-02.txt")
    val instance = Day02()
    val expected = Triple("fghij", "fguij", "fgij")
    val actual = instance.getPrototypeBoxIds(lines)
    Assertions.assertEquals(expected, actual, "The prototype boxes "
        + "is not what was expected: $expected")
  }
}
