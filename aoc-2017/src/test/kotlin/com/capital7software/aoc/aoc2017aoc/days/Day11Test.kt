package com.capital7software.aoc.aoc2017aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day11Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day11Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day11()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testFewestStepsToReachChild() {
    val instance = Day11()
    val expected = listOf(3, 0, 2, 3)

    lines.withIndex().forEach { (index, line) ->
      val actual = instance.fewestStepsToReachChild(line)

      Assertions.assertEquals(
          expected[index], actual, "The fewest steps " +
          "is not what was expected: $expected"
      )
    }
  }

  @Test
  fun testFurthestDistanceReached() {
    val instance = Day11()
    val expected = listOf(3, 2, 2, 3)

    lines.withIndex().forEach { (index, line) ->
      val actual = instance.furthestDistanceReached(line)

      Assertions.assertEquals(
          expected[index], actual, "The fewest steps " +
          "is not what was expected: $expected"
      )
    }
  }
}
