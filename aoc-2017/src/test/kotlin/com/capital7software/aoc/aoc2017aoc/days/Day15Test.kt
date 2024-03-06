package com.capital7software.aoc.aoc2017aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day15Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day15Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day15()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testGeneratorMatchCount() {
    val instance = Day15()
    val expected = 588
    val actual = instance.generatorMatchCount(lines)

    Assertions.assertEquals(
        expected, actual, "The match count " +
        "is not what was expected: $expected"
    )
  }

  @Test
  fun testGeneratorMatchCountNewLogic() {
    val instance = Day15()
    val expected = 309
    val actual = instance.generatorMatchCount(lines, true)

    Assertions.assertEquals(
        expected, actual, "The match count " +
        "is not what was expected: $expected"
    )
  }
}
