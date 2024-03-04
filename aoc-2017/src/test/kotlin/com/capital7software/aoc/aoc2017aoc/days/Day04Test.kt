package com.capital7software.aoc.aoc2017aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day04Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day04Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day04()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testValidPassphraseCount() {
    val instance = Day04()
    val expected = 2
    val actual = instance.validPassphraseCount(lines)

    Assertions.assertEquals(
        expected, actual, "The number of valid phrases " +
        "is not what was expected: $expected"
    )
  }

  @Test
  fun testValidNewRulesPassphraseCount() {
    setupFromFile("inputs/input_day_04-02.txt")
    val instance = Day04()
    val expected = 3
    val actual = instance.validNewRulesPassphraseCount(lines)

    Assertions.assertEquals(
        expected, actual, "The number of valid phrases " +
        "is not what was expected: $expected"
    )
  }
}
