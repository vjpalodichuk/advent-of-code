package com.capital7software.aoc.aoc2017aoc.days

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
  fun testGetDiagnosticChecksum() {
    val instance = Day25()
    val expected = 3
    val actual = instance.getDiagnosticChecksum(lines)

    Assertions.assertEquals(
        expected, actual, "$actual for the diagnostic checksum " +
        "is not what was expected: $expected"
    )
  }
}
