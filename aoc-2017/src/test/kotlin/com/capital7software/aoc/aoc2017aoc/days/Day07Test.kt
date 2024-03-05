package com.capital7software.aoc.aoc2017aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day07Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day07Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day07()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testProgramAtBottomOfTower() {
    val instance = Day07()
    val expected = "tknk"
    val actual = instance.programAtBottomOfTower(lines)

    Assertions.assertEquals(
        expected, actual, "The program at the bottom of the tower " +
        "is not what was expected: $expected"
    )
  }

  @Test
  fun testUnbalancedProgramWeight() {
    val instance = Day07()
    val expected = 60
    val actual = instance.unbalancedProgramWeight(lines)

    Assertions.assertEquals(
        expected, actual, "The program at the bottom of the tower " +
        "is not what was expected: $expected"
    )
  }
}
