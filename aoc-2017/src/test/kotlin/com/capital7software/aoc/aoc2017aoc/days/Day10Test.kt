package com.capital7software.aoc.aoc2017aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day10Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day10Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day10()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testGetCheckValue() {
    val instance = Day10()
    val expected = 12

    val actual = instance.getCheckValue(lines.first(), 5)

    Assertions.assertEquals(
        expected, actual, "The check value " +
        "is not what was expected: $expected"
    )
  }

  @Test
  fun testGetKnotHash() {
    setupFromFile("inputs/input_day_10-02.txt")
    val instance = Day10()
    val expected = listOf(
        "a2582a3a0e66e6e86e3812dcb672a272",
        "33efeb34ea91902bb2f59c9920caa6cd",
        "3efbe78a8d82f29979031a4aa0b16a9d",
        "63960835bcdc130f0b66d7ff4f6a5a8e"
    )

    lines.withIndex().forEach { (index, line) ->
      val actual = instance.getKnotHash(line)

      Assertions.assertEquals(
          expected[index], actual, "The check value " +
          "is not what was expected: $expected"
      )
    }
  }
}
