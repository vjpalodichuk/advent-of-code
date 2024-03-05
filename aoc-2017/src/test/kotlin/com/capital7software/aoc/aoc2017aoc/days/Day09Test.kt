package com.capital7software.aoc.aoc2017aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day09Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day09Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day09()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testScoreOfStream() {
    val instance = Day09()
    val expected = listOf(1, 6, 5, 16, 1, 9, 9, 3)

    lines.withIndex().forEach { (index, line) ->
      val actual = instance.scoreOfStream(line)

      Assertions.assertEquals(
          expected[index], actual, "The largest value " +
          "is not what was expected: $expected"
      )
    }
  }

  @Test
  fun testGarbageRemoved() {
    setupFromFile("inputs/input_day_09-02.txt")
    val instance = Day09()
    val expected = listOf(0, 4, 3, 2, 0, 0, 10)

    lines.withIndex().forEach { (index, line) ->
      val actual = instance.garbageRemoved(line)

      Assertions.assertEquals(
          expected[index], actual, "The largest value " +
          "is not what was expected: $expected"
      )
    }
  }
}
