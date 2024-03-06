package com.capital7software.aoc.aoc2017aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day16Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day16Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day16()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testJustDanceOnce() {
    val dancing = 5
    val instance = Day16()
    val expected = "baedc"
    val actual = instance.justDance(lines.first(), dancing)

    Assertions.assertEquals(
        expected, actual, "The order of programs " +
        "is not what was expected: $expected"
    )
  }

  @Test
  fun testJustDanceMany() {
    val dancing = 5
    val times = 1_000_000_000
    val instance = Day16()
    val expected = "abcde"
    val actual = instance.justDance(lines.first(), dancing, times)

    Assertions.assertEquals(
        expected, actual, "The order of programs after $times dances " +
        "is not what was expected: $expected"
    )
  }
}
