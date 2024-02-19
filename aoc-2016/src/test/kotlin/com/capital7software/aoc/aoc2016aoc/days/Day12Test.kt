package com.capital7software.aoc.aoc2016aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day12Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day12Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day12()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testGetValueInRegisterSmall() {
    val register = "a"
    val instance = Day12()
    val expected = 42L
    val actual = instance.getValueInRegister(lines, register)

    assertEquals(expected, actual, "The value in $register after running the program "
        + "is not what was expected: $expected")
  }

  @Test
  fun testGetValueInRegisterMedium() {
    setupFromFile("inputs/input_day_12-02.txt")
    val register = "b"
    val instance = Day12()
    val expected = 184L
    val actual = instance.getValueInRegister(lines, register)

    assertEquals(expected, actual, "The value in $register after running the program "
        + "is not what was expected: $expected")
  }

  @Test
  fun testSetAndGetValueInRegisterMedium() {
    setupFromFile("inputs/input_day_12-02.txt")
    val register = "b"
    val destRegister = "a"
    val destValue = 1L
    val instance = Day12()
    val expected = 231L
    val actual = instance.setAndGetValueInRegister(lines, register, destRegister, destValue)

    assertEquals(expected, actual, "The value in $register after running the program "
        + "is not what was expected: $expected")
  }
}
