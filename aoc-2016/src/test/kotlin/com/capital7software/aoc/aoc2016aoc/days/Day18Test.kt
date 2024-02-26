package com.capital7software.aoc.aoc2016aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day18Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day18Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day18()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testFindNumberOfSafeTilesSmall() {
    val rows = 10
    val instance = Day18()
    val expected = 16
    val actual = instance.findNumberOfSafeTiles(lines.first(), rows)

    assertEquals(expected, actual, "The number of safe tiles "
        + "is not what was expected: $expected")
  }

  @Test
  fun testFindNumberOfSafeTilesMedium() {
    setupFromFile("inputs/input_day_18-02.txt")
    val rows = 10
    val instance = Day18()
    val expected = 38
    val actual = instance.findNumberOfSafeTiles(lines.first(), rows)

    assertEquals(expected, actual, "The number of safe tiles "
        + "is not what was expected: $expected")
  }
}
