package com.capital7software.aoc.aoc2024aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions
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
  fun testTotalPriceByPerimeterOfAllFencing() {
    val instance = Day12()
    var expected = 140L

    var actual = instance.totalPriceByPerimeterOfAllFencing(lines)
    Assertions.assertEquals(
        expected,
        actual,
        "The total price of $actual is not the expected total price of $expected."
    )

    setupFromFile("inputs/input_day_12-02.txt")
    expected = 772L
    actual = instance.totalPriceByPerimeterOfAllFencing(lines)
    Assertions.assertEquals(
        expected,
        actual,
        "The total price of $actual is not the expected total price of $expected."
    )

    setupFromFile("inputs/input_day_12-03.txt")
    expected = 1930L
    actual = instance.totalPriceByPerimeterOfAllFencing(lines)
    Assertions.assertEquals(
        expected,
        actual,
        "The total price of $actual is not the expected total price of $expected."
    )
  }

  @Test
  fun testTotalPriceBySidesOfAllFencing() {
    val instance = Day12()
    var expected = 80L

    var actual = instance.totalPriceBySidesOfAllFencing(lines)
    Assertions.assertEquals(
        expected,
        actual,
        "The total price of $actual is not the expected total price of $expected."
    )

    setupFromFile("inputs/input_day_12-02.txt")
    expected = 436L
    actual = instance.totalPriceBySidesOfAllFencing(lines)
    Assertions.assertEquals(
        expected,
        actual,
        "The total price of $actual is not the expected total price of $expected."
    )

    setupFromFile("inputs/input_day_12-03.txt")
    expected = 1206L
    actual = instance.totalPriceBySidesOfAllFencing(lines)
    Assertions.assertEquals(
        expected,
        actual,
        "The total price of $actual is not the expected total price of $expected."
    )

    setupFromFile("inputs/input_day_12-04.txt")
    expected = 236L
    actual = instance.totalPriceBySidesOfAllFencing(lines)
    Assertions.assertEquals(
        expected,
        actual,
        "The total price of $actual is not the expected total price of $expected."
    )

    setupFromFile("inputs/input_day_12-05.txt")
    expected = 368L
    actual = instance.totalPriceBySidesOfAllFencing(lines)
    Assertions.assertEquals(
        expected,
        actual,
        "The total price of $actual is not the expected total price of $expected."
    )
  }
}
