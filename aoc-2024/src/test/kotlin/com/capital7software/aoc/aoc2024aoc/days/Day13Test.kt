package com.capital7software.aoc.aoc2024aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day13Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day13Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day13()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testFewestTokensToWinAllPrizes() {
    val instance = Day13()
    val expected = 480L

    val actual = instance.fewestTokensToWinAllPrizes(lines)
    Assertions.assertEquals(
        expected,
        actual,
        "The fewest tokens of $actual is not the expected fewest tokens of $expected."
    )
  }

  @Test
  fun testFewestTokensToWinAllPrizesWithOffset() {
    val instance = Day13()
    val expected = 875_318_608_908

    val actual = instance.fewestTokensToWinAllPrizesWithOffset(lines)
    Assertions.assertEquals(
        expected,
        actual,
        "The fewest tokens of $actual is not the expected fewest tokens of $expected."
    )
  }
}
