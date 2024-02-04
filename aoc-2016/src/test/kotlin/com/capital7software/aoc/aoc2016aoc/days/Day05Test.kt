package com.capital7software.aoc.aoc2016aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day05Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day05Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day05()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testSumOfSectorIdsOfRealRooms() {
    val instance = Day05()
    val expected = "18f47a30"
    val actual = instance.generatePassword(lines.first())

    assertEquals(expected, actual, "The generated password "
        + "is not what was expected: $expected")
  }

  @Test
  fun testSectorIdOfRoomWhereObjectsAreStored() {
    val instance = Day05()
    val expected = "05ace8e3"
    val actual = instance.generateAdvancedPassword(lines.first())

    assertEquals(expected, actual, "The generated advanced password "
        + "is not what was expected: $expected")
  }
}
