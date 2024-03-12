package com.capital7software.aoc.aoc2018aoc.days

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
  fun testGetFrequency() {
    val instance = Day01()
    val inputs = listOf(
        listOf("+1", "-2", "+3", "+1"),
        listOf("+1", "+1", "+1"),
        listOf("+1", "+1", "-2"),
        listOf("-1", "-2", "-3")
    )
    val expected = listOf(3, 3, 0, -6)

    for (i in inputs.indices) {
      val actual = instance.getFrequency(inputs[i])
      Assertions.assertEquals(expected[i], actual, "The frequency "
          + "is not what was expected: $expected")
    }
  }

  @Test
  fun testGetFirstRepeatedFrequency() {
    val instance = Day01()
    val inputs = listOf(
        listOf("+1", "-1"),
        listOf("+3", "+3", "+4", "-2", "-4"),
        listOf("-6", "+3", "+8", "+5", "-6"),
        listOf("+7", "+7", "-2", "-7", "-4")
    )
    val expected = listOf(0, 10, 5, 14)

    for (i in inputs.indices) {
      val actual = instance.getFirstRepeatedFrequency(inputs[i])
      Assertions.assertEquals(expected[i], actual, "The first repeated frequency "
          + "is not what was expected: $expected")
    }
  }
}
