package com.capital7software.aoc.aoc2017aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day03Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day03Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day03()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testGetNumberOfStepsBackToStart() {
    val instance = Day03()
    val expected = listOf(0, 3, 2, 31)

    lines.withIndex().forEach { (index, line) ->
      val actual = instance.getNumberOfStepsBackToStart(line.toInt())

      Assertions.assertEquals(
          expected[index], actual, "The steps required to carry the data back to " +
          "square 1 is not what was expected: $expected"
      )
    }
  }

  @Test
  fun testGetFirstNumberWrittenLargerThanData() {
    val instance = Day03()
    val expected = listOf(2, 23, 25, 1968)

    lines.withIndex().forEach { (index, line) ->
      val actual = instance.getFirstNumberWrittenLargerThanData(line.toInt())

      Assertions.assertEquals(
          expected[index], actual, "The first value written that is larger than " +
          "the input data is not what was expected: $expected"
      )
    }
  }
}
