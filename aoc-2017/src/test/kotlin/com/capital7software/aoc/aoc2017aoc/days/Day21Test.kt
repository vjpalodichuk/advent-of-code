package com.capital7software.aoc.aoc2017aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day21Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day21Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day21()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testGetFractalCounts() {
    val instance = Day21()
    val iterations = 2
    val expected = Pair(12, 24)
    val actual = instance.getFractalCounts(lines, iterations)

    Assertions.assertEquals(
        expected, actual, "The number of pixels in the on and off states " +
        "is not what was expected: $expected"
    )
  }

  @Test
  fun testGetFractalCountsThrows() {
    val instance = Day21()
    val iterations = 3
    val exception = assertThrows(
        IllegalStateException::class.java
    ) { instance.getFractalCounts(lines, iterations) }

    val expected = "Unable to find a rule match for ##/#."
    val actual = exception.message

    Assertions.assertEquals(
        expected, actual, "The exception message " +
        "is not what was expected: $expected"
    )
  }

  @Test
  fun testLogFractalAndCounts() {
    val instance = Day21()
    val iterations = 2
    val expected = Pair(12, 24)
    val actual = instance.logFractalAndCounts(lines, iterations)

    Assertions.assertEquals(
        expected, actual, "The number of pixels in the on and off states " +
        "is not what was expected: $expected"
    )
  }
}
