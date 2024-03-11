package com.capital7software.aoc.aoc2017aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day22Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day22Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day22()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testRunBurstsSmall() {
    val instance = Day22()
    val bursts = 70
    val expected = Pair(41, 29)
    val actual = instance.runBursts(lines, bursts)

    Assertions.assertEquals(
        expected, actual, "$actual results of $bursts bursts " +
        "is not what was expected: $expected"
    )
  }

  @Test
  fun testRunBursts() {
    val instance = Day22()
    val bursts = 10_000
    val expected = Pair(5_587, 4_413)
    val actual = instance.runBursts(lines, bursts)

    Assertions.assertEquals(
        expected, actual, "$actual results of $bursts bursts " +
        "is not what was expected: $expected"
    )
  }

  @Test
  fun testRunBurstsNewLogicSmall() {
    val instance = Day22()
    val bursts = 100
    val expected = Pair(26, 74)
    val actual = instance.runBursts(lines, bursts, true)

    Assertions.assertEquals(
        expected, actual, "$actual results of $bursts bursts " +
        "is not what was expected: $expected"
    )
  }

  @Test
  fun testRunBurstsNewLogic() {
    val instance = Day22()
    val bursts = 10_000_000
    val expected = Pair(2_511_944, 7_488_056)
    val actual = instance.runBursts(lines, bursts, true)

    Assertions.assertEquals(
        expected, actual, "$actual results of $bursts bursts " +
        "is not what was expected: $expected"
    )
  }
}
