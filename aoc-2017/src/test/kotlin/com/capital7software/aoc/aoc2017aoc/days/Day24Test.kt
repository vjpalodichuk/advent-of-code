package com.capital7software.aoc.aoc2017aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day24Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day24Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day24()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testGetStrongestBridge() {
    val instance = Day24()
    val expected = 31
    val actual = instance.getStrongestBridge(lines).sumOf { it.strength }

    Assertions.assertEquals(
        expected, actual, "$actual for the strongest bridge strength " +
        "is not what was expected: $expected"
    )
  }

  @Test
  fun testGetLongestThenStrongestBridge() {
    val instance = Day24()
    val expected = 19
    val actual = instance.getLongestThenStrongestBridge(lines).sumOf { it.strength }

    Assertions.assertEquals(
        expected, actual, "$actual for the longest then strongest bridge strength " +
        "is not what was expected: $expected"
    )
  }
}
