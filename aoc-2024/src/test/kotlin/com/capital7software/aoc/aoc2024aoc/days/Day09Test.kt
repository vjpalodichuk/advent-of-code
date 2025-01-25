package com.capital7software.aoc.aoc2024aoc.days

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
  fun testFilesystemDefragmentation() {
    val instance = Day09()
    val expected = 1928L

    val actual = instance.filesystemDefragmentation(lines)
    Assertions.assertEquals(
        expected,
        actual,
        "The actual checksum of $actual is not the expected checksum of $expected."
    )
  }

  @Test
  fun testFastFilesystemDefragmentation() {
    val instance = Day09()
    val expected = 2858L

    val actual = instance.fastFilesystemDefragmentation(lines)
    Assertions.assertEquals(
        expected,
        actual,
        "The actual checksum of $actual is not the expected checksum of $expected."
    )
  }
}
