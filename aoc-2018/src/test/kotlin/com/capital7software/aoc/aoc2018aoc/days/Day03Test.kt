package com.capital7software.aoc.aoc2018aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day03Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day03Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day03()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testGetAreaOfOverlap() {
    val instance = Day03()
    val expected = 4
    val actual = instance.getAreaOfOverlap(lines)
    Assertions.assertEquals(expected, actual, "The area of overlap "
        + "is not what was expected: $expected")
  }

  @Test
  fun testGetNoOverlapClaim() {
    val instance = Day03()
    val expected = "3"
    val actual = instance.getNoOverlapClaim(lines).id
    Assertions.assertEquals(expected, actual, "The ID of the claim with no overlap "
        + "is not what was expected: $expected")
  }
}
