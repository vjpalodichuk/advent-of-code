package com.capital7software.aoc.aoc2024aoc.days

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
  fun testCalculateSumOfProducts() {
    val instance = Day03()
    val expected = 161L

    val actual = instance.calculateSumOfProducts(lines)
    Assertions.assertEquals(
        expected,
        actual,
        "The actual sum of products of $actual is not the expected " +
            "sum of products of $expected."
    )
  }

  @Test
  fun testCalculateRealSumOfProducts() {
    setupFromFile("inputs/input_day_03-02.txt")
    val instance = Day03()
    val expected = 48L

    val actual = instance.calculateRealSumOfProducts(lines)
    Assertions.assertEquals(
        expected,
        actual,
        "The actual sum of products of $actual is not the expected " +
            "sum of products of $expected."
    )
  }
}
