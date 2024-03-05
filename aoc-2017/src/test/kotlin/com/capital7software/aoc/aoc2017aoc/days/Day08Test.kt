package com.capital7software.aoc.aoc2017aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day08Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day08Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day08()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testLargestValueInAnyRegister() {
    val instance = Day08()
    val expected = 1
    val actual = instance.largestValueInAnyRegister(lines)

    Assertions.assertEquals(
        expected, actual, "The largest value " +
        "is not what was expected: $expected"
    )
  }

  @Test
  fun testLargestValueInAnyRegisterDuringExecution() {
    val instance = Day08()
    val expected = 10
    val actual = instance.largestValueInAnyRegisterDuringExecution(lines)

    Assertions.assertEquals(
        expected, actual, "The largest value " +
        "is not what was expected: $expected"
    )
  }
}
