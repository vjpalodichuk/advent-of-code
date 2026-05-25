package com.capital7software.aoc.aoc2024aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day24Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day24Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day24()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testCrossedWiresOutputSmall() {
    val expected = 4L
    val actual = Day24().crossedWiresOutput(lines)

    Assertions.assertEquals(
        expected,
        actual,
        "The crossed wires output $actual is not the expected value $expected."
    )
  }

  @Test
  fun testCrossedWiresOutputLarger() {
    val instance = Day24()
    setupFromFile("inputs/input_day_24-02.txt")

    val expected = 2024L
    val actual = instance.crossedWiresOutput(lines)

    Assertions.assertEquals(
        expected,
        actual,
        "The crossed wires output $actual is not the expected value $expected."
    )
  }

  @Test
  fun testSwappedOutputWiresSmall() {
    val expected = "z00,z01"
    val actual = Day24().swappedOutputWires(lines)

    Assertions.assertEquals(
        expected,
        actual,
        "The swapped output wires $actual are not the expected value $expected."
    )
  }

  @Test
  fun testSwappedOutputWiresLarge() {
    val instance = Day24()
    setupFromFile("inputs/input_day_24-03.txt")

    val expected = "z00,z01,z02,z05"
    val actual = instance.swappedOutputWires(lines)

    Assertions.assertEquals(
        expected,
        actual,
        "The swapped output wires $actual are not the expected value $expected."
    )
  }
}
