package com.capital7software.aoc.aoc2017aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day17Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day17Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day17()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testNumberAfterLastWritten() {
    val instance = Day17()
    val expected = 638
    val actual = instance.numberAfterLastWritten(lines.first().toInt())

    Assertions.assertEquals(
        expected, actual, "The number after the last one written " +
        "is not what was expected: $expected"
    )
  }

  @Test
  fun testNumberAfterZeroWritten() {
    val instance = Day17()
    val expected = 1222153
    val actual = instance.numberAfterZeroWritten(lines.first().toInt())

    Assertions.assertEquals(
        expected, actual, "The number after zero " +
        "is not what was expected: $expected"
    )
  }
}
