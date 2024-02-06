package com.capital7software.aoc.aoc2016aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions.assertEquals
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
  fun testDecompressedLength() {
    val instance = Day09()
    val expected = 57L
    val actual = instance.decompressedLength(lines)

    assertEquals(expected, actual, "The decompressed length "
        + "is not what was expected: $expected")
  }

  @Test
  fun testDecompressedWithRepeatLength() {
    setupFromFile("inputs/input_day_09-02.txt")
    val instance = Day09()
    val expected = 242_394L
    val actual = instance.decompressedLength(lines, true)

    assertEquals(expected, actual, "The decompressed length "
        + "is not what was expected: $expected")
  }

  @Test
  fun testDecompressedLengthOnly() {
    val instance = Day09()
    val expected = 57L
    val actual = instance.decompressedLengthOnly(lines)

    assertEquals(expected, actual, "The decompressed length "
        + "is not what was expected: $expected")
  }

  @Test
  fun testDecompressedWithRepeatLengthOnly() {
    setupFromFile("inputs/input_day_09-02.txt")
    val instance = Day09()
    val expected = 242_394L
    val actual = instance.decompressedLengthOnly(lines, true)

    assertEquals(expected, actual, "The decompressed length "
        + "is not what was expected: $expected")
  }
}
