package com.capital7software.aoc.aoc2017aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day01Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day01Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day01()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testGetCaptchaSolution() {
    val instance = Day01()
    val expected = listOf(3, 4, 0, 9)

    lines.withIndex().forEach { (index, line) ->
      val actual = instance.getCaptchaSolution(line)

      Assertions.assertEquals(expected[index], actual, "The captcha solution "
          + "is not what was expected: $expected")
    }
  }

  @Test
  fun testGetCaptchaSolutionHalfway() {
    setupFromFile("inputs/input_day_01-02.txt")
    val instance = Day01()
    val expected = listOf(6, 0, 4, 12, 4)
    lines.withIndex().forEach { (index, line) ->
      val actual = instance.getCaptchaSolution(line, true)

      Assertions.assertEquals(expected[index], actual, "The captcha solution using halfway "
          + "is not what was expected: $expected")
    }
  }
}
