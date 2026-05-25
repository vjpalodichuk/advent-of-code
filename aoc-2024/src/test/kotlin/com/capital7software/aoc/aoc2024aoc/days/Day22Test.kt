package com.capital7software.aoc.aoc2024aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import com.capital7software.aoc.lib.math.MonkeyMarket
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
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

  @SuppressFBWarnings
  @Test
  fun testNextSecretSequence() {
    val expected = listOf(
        15887950L,
        16495136L,
        527345L,
        704524L,
        1553684L,
        12683156L,
        11100544L,
        12249484L,
        7753432L,
        5908254L
    )

    var secret = 123L
    val actual = expected.indices.map {
      secret = MonkeyMarket.nextSecret(secret)
      secret
    }

    Assertions.assertEquals(
        expected,
        actual,
        "The generated secret sequence does not match the expected sequence."
    )
  }

  @Test
  fun testGenerate2000thSecret() {
    val market = MonkeyMarket.parse(lines)

    val expected = 37327623L
    val actual = market.sumOfNthSecret(2000)

    Assertions.assertEquals(
        expected,
        actual,
        "The sum of the 2000th generated secrets is not the expected value."
    )
  }

  @Test
  fun testPartOneExample() {
    val actual = Day22().sumOfNthSecret(lines)

    Assertions.assertEquals(37327623L, actual)
  }

  @Test
  fun testPartOneConvenienceMethodExample() {
    val actual = MonkeyMarket.part1(lines)

    Assertions.assertEquals(37327623L, actual)
  }


  @Test
  fun testMostBananasExample() {
    setupFromFile("inputs/input_day_22-02.txt")

    val actual = Day22().mostBananas(lines)

    Assertions.assertEquals(23L, actual)
  }

  @Test
  fun testPartTwoConvenienceMethodExample() {
    setupFromFile("inputs/input_day_22-02.txt")

    val actual = MonkeyMarket.part2(lines)

    Assertions.assertEquals(23L, actual)
  }
}
