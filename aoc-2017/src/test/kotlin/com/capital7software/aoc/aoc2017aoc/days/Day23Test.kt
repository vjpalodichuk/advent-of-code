package com.capital7software.aoc.aoc2017aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import com.capital7software.aoc.lib.computer.Multiply
import org.junit.jupiter.api.Assertions
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
  fun testInstructionCount() {
    val instance = Day23()
    val kClass = Multiply::class
    val expected = 5929L
    val actual = instance.instructionCount(lines, kClass)

    Assertions.assertEquals(
        expected, actual, "$actual executions of $kClass " +
        "is not what was expected: $expected"
    )
  }

  @Test
  fun testRegisterValue() {
    val instance = Day23()
    val register = "h"
    val expected = 907L
    val actual = instance.registerValue(lines, register, 1)

    Assertions.assertEquals(
        expected, actual, "$actual in register $register " +
        "is not what was expected: $expected"
    )
  }
}
