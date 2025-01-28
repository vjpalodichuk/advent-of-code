package com.capital7software.aoc.aoc2024aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import com.capital7software.aoc.lib.analysis.PlutonianPebbles
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day11Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day11Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day11()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testNumberOfStonesAfterTwentyFiveBlinks() {
    val instance = Day11()
    val expected = 55312

    val actual = instance.numberOfStonesAfterTwentyFiveBlinks(lines.first())
    Assertions.assertEquals(
        expected,
        actual,
        "The number $actual is not the expected number of $expected."
    )
  }

  @Test
  fun testNumberOfStonesAfterSeventyFiveBlinks() {
    val instance = Day11()
    val expected = 65601038650482L

    val actual = instance.numberOfStonesAfterSeventyFiveBlinks(lines.first())
    Assertions.assertEquals(
        expected,
        actual,
        "The number $actual is not the expected number of $expected."
    )
  }

  @Test
  fun testWhyBlinksAreDifferent() {
    val instance = PlutonianPebbles(lines.first())

    IntRange(1, 25).forEach { iteration ->
      val slow = instance.blink(iteration)
      val fast = instance.fastBlink(iteration)

      val slowCount = slow.size.toLong()
      val fastCount = fast.values.sum()

      if (slowCount != fastCount) {
        val map = hashMapOf<PlutonianPebbles.Stone, Long>()

        slow.forEach { stone ->
          map[stone] = map.computeIfAbsent(stone) { 0L } + 1
        }

        val slowKeys = map.keys
        val fastKeys = fast.keys

        if (slowKeys != fastKeys) {
          val notInSlow = fastKeys - slowKeys
          val notInFast = slowKeys - fastKeys
          println("Not in slow $notInSlow")
          println("Not in fast $notInFast")
        }

        for (key in slowKeys) {
          val fastValue = fast[key]
          val slowValue = map[key]

          if (fastValue != slowValue) {
            println("We have a problem as $fastValue is not same as $slowValue")
          }
        }
      }
      Assertions.assertEquals(
          slowCount,
          fastCount,
          "The number $fastCount is not the expected number of $slowCount after iteration $iteration."
      )
    }
  }
}
