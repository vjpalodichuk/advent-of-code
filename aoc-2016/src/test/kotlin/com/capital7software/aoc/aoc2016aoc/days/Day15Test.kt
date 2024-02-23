package com.capital7software.aoc.aoc2016aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import com.capital7software.aoc.lib.math.DiscSculpture
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day15Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day15Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day15()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testTimeToPushTheButton() {
    val instance = Day15()
    val expected = 5
    val actual = instance.timeToPushTheButton(lines)

    assertEquals(expected, actual, "The time to push the button "
        + "is not what was expected: $expected")
  }

  @Test
  fun testTimeToPushTheButtonWithAddedDisc() {
    val instance = Day15()
    val expected = 5
    val actual = instance.timeToPushTheButton(lines)

    assertEquals(expected, actual, "The time to push the button "
        + "is not what was expected: $expected")
  }

  @Test
  fun testNoSolutionToTimeToPushTheButton() {
    val instance = DiscSculpture(lines)
    instance.add(DiscSculpture.Disc(instance.size + 1, 2, 0))
    instance.add(DiscSculpture.Disc(instance.size + 1, 2, 0))
    val expected = -1
    val actual = instance.solve()

    assertEquals(expected, actual, "The time to push the button "
        + "is not what was expected: $expected")
  }
}
