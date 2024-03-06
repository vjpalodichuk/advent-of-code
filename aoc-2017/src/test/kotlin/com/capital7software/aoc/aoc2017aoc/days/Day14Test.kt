package com.capital7software.aoc.aoc2017aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day14Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day14Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day14()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testDefragDisk() {
    val instance = Day14()
    val expected = Pair(8_108, 8_276)

    val actual = instance.defragmentation(lines.first())

    Assertions.assertEquals(
        expected, actual, "The results of defragmentation " +
        "is not what was expected: $expected"
    )
  }

  @Test
  fun testDiskRegions() {
    val instance = Day14()
    val expected = 1242

    val actual = instance.diskRegions(lines.first())

    Assertions.assertEquals(
        expected, actual, "The number of regions " +
        "is not what was expected: $expected"
    )
  }
}
