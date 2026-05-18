package com.capital7software.aoc.aoc2024aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day16Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day16Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day16()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testLowestScore() {
    val instance = Day16()
    val expected = 7036L

    val actual = instance.lowestScore(lines)
    Assertions.assertEquals(
        expected,
        actual,
        "The lowest score of $actual is not the expected lowest score of $expected."
    )
  }

  @Test
  fun testLowestScore2() {
    val instance = Day16()

    setupFromFile("inputs/input_day_16-02.txt")

    val expected = 11048L

    val actual = instance.lowestScore(lines)
    Assertions.assertEquals(
        expected,
        actual,
        "The lowest score of $actual is not the expected lowest score of $expected."
    )
  }

  @Test
  fun testBestPathTileCount() {
    val instance = Day16()
    val expected = 45L

    val actual = instance.bestPathTileCount(lines)
    Assertions.assertEquals(
        expected,
        actual,
        "The number of tiles of $actual is not the expected number of tiles of $expected."
    )
  }

  @Test
  fun testBestPathTileCount2() {
    val instance = Day16()

    setupFromFile("inputs/input_day_16-02.txt")

    val expected = 64L

    val actual = instance.bestPathTileCount(lines)
    Assertions.assertEquals(
        expected,
        actual,
        "The number of tiles of $actual is not the expected number of tiles of $expected."
    )
  }

}
