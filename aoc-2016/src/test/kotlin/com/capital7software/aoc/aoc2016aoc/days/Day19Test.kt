package com.capital7software.aoc.aoc2016aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day19Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day19Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day19()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testElfWithAllPresentsLeftExchange() {
    val instance = Day19()
    val expectedId = 3
    val expectedCount = 5
    val actual = instance.elfWithAllPresentsLeftExchange(lines.first().toInt())

    assertEquals(expectedId, actual.id, "The ID of the Elf "
        + "is not what was expected: $expectedId")
    assertEquals(expectedCount, actual.presents, "The number of presents "
        + "is not what was expected: $expectedId")
  }

  @Test
  fun testElfWithAllPresentsLeftExchangeFast() {
    val instance = Day19()
    val expectedId = 3
    val expectedCount = 5
    val actual = instance.elfWithAllPresentsLeftExchangeFast(lines.first().toInt())

    assertEquals(expectedId, actual.id, "The ID of the Elf "
        + "is not what was expected: $expectedId")
    assertEquals(expectedCount, actual.presents, "The number of presents "
        + "is not what was expected: $expectedId")
  }

  @Test
  fun testElfWithAllPresentsAcrossExchange() {
    val instance = Day19()
    val expectedId = 2
    val expectedCount = 5
    val actual = instance.elfWithAllPresentsAcrossExchange(lines.first().toInt())

    assertEquals(expectedId, actual.id, "The ID of the Elf "
        + "is not what was expected: $expectedId")
    assertEquals(expectedCount, actual.presents, "The number of presents "
        + "is not what was expected: $expectedId")
  }

  @Test
  fun testElfWithAllPresentsAcrossExchangeFast() {
    val instance = Day19()
    val expectedId = 2
    val expectedCount = 5
    val actual = instance.elfWithAllPresentsAcrossExchangeFast(lines.first().toInt())

    assertEquals(expectedId, actual.id, "The ID of the Elf "
        + "is not what was expected: $expectedId")
    assertEquals(expectedCount, actual.presents, "The number of presents "
        + "is not what was expected: $expectedId")
  }
}
