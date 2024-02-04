package com.capital7software.aoc.aoc2016aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day04Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day04Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day04()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testSumOfSectorIdsOfRealRooms() {
    val instance = Day04()
    val expected = 1838L
    val actual = instance.calculateSumOfRealSectorIds(lines)

    assertEquals(expected, actual, "The sum of sector IDs "
        + "is not what was expected: $expected")
  }

  @Test
  fun testSectorIdOfRoomWhereObjectsAreStored() {
    val instance = Day04()
    val expected = 324L
    val actual = instance.calculateSectorIdOfRoomWhereObjectsAreStored(lines)

    assertEquals(expected, actual, "The sector ID "
        + "is not what was expected: $expected")
  }
}
