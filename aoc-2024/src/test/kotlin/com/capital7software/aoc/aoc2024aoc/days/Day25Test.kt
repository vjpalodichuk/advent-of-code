package com.capital7software.aoc.aoc2024aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day25Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day25Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day25()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testFittingLockKeyPairCount() {
    val expected = 3L
    val actual = Day25().fittingLockKeyPairCount(lines)

    Assertions.assertEquals(
        expected,
        actual,
        "The fitting lock/key pair count $actual is not the expected value $expected."
    )
  }

  @Test
  fun testCompleteChronicle() {
    val expected = "Chronicle delivered"
    val actual = Day25().completeChronicle(lines)

    Assertions.assertEquals(
        expected,
        actual,
        "The chronicle completion message $actual is not the expected value $expected."
    )
  }
}
