package com.capital7software.aoc.aoc2017aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day12Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day12Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day12()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testProgramsInGroup() {
    val instance = Day12()
    val expected = 6

    val actual = instance.programsInGroup(lines, 0)

    Assertions.assertEquals(
        expected, actual, "The fewest steps " +
        "is not what was expected: $expected"
    )
  }

  @Test
  fun testAllGroups() {
    val instance = Day12()
    val expected = 2

    val actual = instance.allGroups(lines)

    Assertions.assertEquals(
        expected, actual, "The number of groups " +
        "is not what was expected: $expected"
    )
  }
}
