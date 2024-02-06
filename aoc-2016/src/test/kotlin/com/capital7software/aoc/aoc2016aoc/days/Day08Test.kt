package com.capital7software.aoc.aoc2016aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day08Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day08Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day08()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testNumberOfPixelsThatShouldBeLit() {
    val instance = Day08()
    val expected = 6L
    val actual = instance.numberOfPixelsThatShouldBeLit(lines)

    assertEquals(expected, actual, "The number of pixels that should be lit "
        + "is not what was expected: $expected")
  }

  @Test
  fun testGetDisplayMessage() {
    val instance = Day08()
    val expected = """LightGrid - Columns: 50, Rows: 6, Instructions: 4
    1 1                                           
1 1                                               
 1                                                
 1                                                
                                                  
                                                  
"""
    val actual = instance.getDisplayMessage(lines)

    assertEquals(expected, actual, "The display message "
        + "is not what was expected: $expected")
  }
}
