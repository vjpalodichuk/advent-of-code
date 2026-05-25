package com.capital7software.aoc.aoc2024aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import com.capital7software.aoc.lib.grid.RaceCondition
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day20Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day20Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day20()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testCheatSavingsDistributionDefault() {
    val duration = 2
    val expected = mapOf(
        2 to 14L,
        4 to 14L,
        6 to 2L,
        8 to 4L,
        10 to 2L,
        12 to 3L,
        20 to 1L,
        36 to 1L,
        38 to 1L,
        40 to 1L,
        64 to 1L
    )

    val actual = RaceCondition.parse(lines).cheatSavingsDistribution(duration)

    Assertions.assertEquals(expected, actual)
  }

  @Test
  fun testCountCheatsThatSaveAtLeastDefault() {
    val duration = 2

    listOf(
        2 to 44L,
        4 to 30L,
        6 to 16L,
        8 to 14L,
        10 to 10L,
        12 to 8L,
        20 to 5L,
        36 to 4L,
        38 to 3L,
        40 to 2L,
        64 to 1L
    ).forEach { (savings, cheats) ->
      val actual = Day20().countCheatsThatSaveAtLeast(
          lines,
          savings,
          duration
      )

      Assertions.assertEquals(
          cheats,
          actual,
          "The number of cheats that save at least $savings is not the expected value $cheats."
      )
    }
  }

  @Test
  fun testCheatSavingsDistributionLonger() {
    val duration = 20
    val expected = mapOf(
        2 to 138L,
        4 to 329L,
        6 to 122L,
        8 to 224L,
        10 to 109L,
        12 to 252L,
        14 to 101L,
        16 to 263L,
        18 to 94L,
        20 to 217L,
        22 to 76L,
        24 to 129L,
        26 to 66L,
        28 to 80L,
        30 to 61L,
        32 to 61L,
        34 to 58L,
        36 to 57L,
        38 to 51L,
        40 to 93L,
        42 to 41L,
        44 to 99L,
        46 to 38L,
        48 to 37L,
        50 to 32L,
        52 to 31L,
        54 to 29L,
        56 to 39L,
        58 to 25L,
        60 to 23L,
        62 to 20L,
        64 to 19L,
        66 to 12L,
        68 to 14L,
        70 to 12L,
        72 to 22L,
        74 to 4L,
        76 to 3L
    )

    val actual = RaceCondition.parse(lines).cheatSavingsDistribution(duration)

    Assertions.assertEquals(expected, actual)
  }

  @Test
  fun testCountCheatsThatSaveAtLeastLonger() {
    val duration = 20

    listOf(
        50 to 285L,
        52 to 253L,
        54 to 222L,
        56 to 193L,
        58 to 154L,
        60 to 129L,
        62 to 106L,
        64 to 86L,
        66 to 67L,
        68 to 55L,
        70 to 41L,
        72 to 29L,
        74 to 7L,
        76 to 3L
    ).forEach { (savings, cheats) ->
      val actual = Day20().countCheatsThatSaveAtLeast(
          lines,
          savings,
          duration
      )

      Assertions.assertEquals(
          cheats,
          actual,
          "The number of cheats that save at least $savings is not the expected value $cheats."
      )
    }
  }
}
