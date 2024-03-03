package com.capital7software.aoc.aoc2016aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day23Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day23Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day23()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testGetValueInRegister() {
    val register = "a"
    val eggs = 7
    val instance = Day23()
    val expected = 3
    val actual = instance.getValueInRegister(lines, register, register, eggs)

    assertEquals(expected, actual, "The value in register a "
        + "is not what was expected: $expected")
  }

  @Test
  fun testGetValueInRegisterOptimized() {
    setupFromFile("inputs/input_day_23-02.txt")
    val register = "a"
    val eggs = 12
    val instance = Day23()
    val expected = 166
    val actual = instance.getValueInRegister(lines, register, register, eggs)

    assertEquals(expected, actual, "The value in register a "
        + "is not what was expected: $expected")
  }
}
