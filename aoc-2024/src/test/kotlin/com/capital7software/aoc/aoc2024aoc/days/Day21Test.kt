package com.capital7software.aoc.aoc2024aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import com.capital7software.aoc.lib.grid.KeypadConundrum
import org.junit.jupiter.api.Assertions
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
  fun testShortestHumanInputLength() {
    val conundrum = KeypadConundrum.parse(lines)

    val expected = mapOf(
        "029A" to 68L,
        "980A" to 60L,
        "179A" to 68L,
        "456A" to 64L,
        "379A" to 64L
    )

    expected.forEach { (code, length) ->
      Assertions.assertEquals(
          length,
          conundrum.shortestHumanInputLength(code, directionalRobots = 2),
          "The shortest human input length for $code is not the expected value $length."
      )
    }
  }

  @Test
  fun testPartOneExample() {
    val actual = Day21().sumOfComplexities(lines, directionalRobots = 2)

    Assertions.assertEquals(126384L, actual)
  }

  @Test
  fun testPartOneConvenienceMethodExample() {
    val actual = KeypadConundrum.part1(lines)

    Assertions.assertEquals(126384L, actual)
  }

  @Test
  fun testPartTwoExample() {
    val actual = Day21().sumOfComplexities(lines, directionalRobots = 25)

    Assertions.assertEquals(154115708116294L, actual)
  }

  @Test
  fun testPartTwoConvenienceMethodExample() {
    val actual = KeypadConundrum.part2(lines)

    Assertions.assertEquals(154115708116294L, actual)
  }
}
