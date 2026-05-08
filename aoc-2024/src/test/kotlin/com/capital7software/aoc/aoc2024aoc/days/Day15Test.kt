package com.capital7software.aoc.aoc2024aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day15Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day15Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day15()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testGpsSum() {
    val instance = Day15()
    val expected = 2028L

    val actual = instance.gpsSum(lines)
    Assertions.assertEquals(
        expected,
        actual,
        "The GPS sum of $actual is not the expected GPS sum of $expected."
    )
  }

  @Test
  fun testGpsSum2() {
    val instance = Day15()

    setupFromFile("inputs/input_day_15-02.txt")

    val expected = 10092L

    val actual = instance.gpsSum(lines)
    Assertions.assertEquals(
        expected,
        actual,
        "The GPS sum of $actual is not the expected GPS sum of $expected."
    )
  }

  @Test
  fun testLargeGpsSum() {
    val instance = Day15()

    setupFromFile("inputs/input_day_15-03.txt")

    val expected = 618L

    val actual = instance.gpsSum(lines, true)
    Assertions.assertEquals(
        expected,
        actual,
        "The large box GPS sum of $actual is not the expected GPS sum of $expected."
    )
  }

  @Test
  fun testLargeGpsSum2() {
    val instance = Day15()

    setupFromFile("inputs/input_day_15-02.txt")

    val expected = 9021L

    val actual = instance.gpsSum(lines, true)
    Assertions.assertEquals(
        expected,
        actual,
        "The large box GPS sum of $actual is not the expected GPS sum of $expected."
    )
  }

}
