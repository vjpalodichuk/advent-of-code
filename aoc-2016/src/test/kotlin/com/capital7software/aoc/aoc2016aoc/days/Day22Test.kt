package com.capital7software.aoc.aoc2016aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day22Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day22Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day22()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testViablePairs() {
    val instance = Day22()
    val expected = 7
    val actual = instance.viablePairs(lines)

    assertEquals(expected, actual, "The number of viable node-pairs "
        + "is not what was expected: $expected")
  }

  @Test
  fun testFewestStepsToMoveGoalData() {
    val instance = Day22()
    val expected = 7
    val actual = instance.fewestStepsToMoveGoalData(lines)

    assertEquals(expected, actual, "The number of steps to move the goal data "
        + "is not what was expected: $expected")
  }
}
