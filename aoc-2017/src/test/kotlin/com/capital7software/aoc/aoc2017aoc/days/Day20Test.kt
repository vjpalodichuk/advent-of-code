package com.capital7software.aoc.aoc2017aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Day20Test : AdventOfCodeTestBase() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(Day20Test::class.java)
  }

  override fun getLogger(): Logger {
    return log
  }

  @BeforeEach
  fun setup() {
    val instance = Day20()
    setupFromFile(instance.defaultInputFilename)
  }

  @Test
  fun testGetParticleClosestToTarget() {
    val instance = Day20()
    val expected = 0
    val actual = instance.getParticleClosestToTarget(lines)

    Assertions.assertEquals(
        expected, actual, "The closest particle " +
        "is not what was expected: $expected"
    )
  }

  @Test
  fun testGetParticleClosestToTargetFast() {
    val instance = Day20()
    val expected = 0
    val actual = instance.getParticleClosestToTargetFast(lines)

    Assertions.assertEquals(
        expected, actual, "The closest particle " +
        "is not what was expected: $expected"
    )
  }

  @Test
  fun testGetSurvivingParticles() {
    setupFromFile("inputs/input_day_20-02.txt")
    val instance = Day20()
    val expected = 1
    val actual = instance.getSurvivingParticles(lines).size

    Assertions.assertEquals(
        expected, actual, "The surviving particles count " +
        "is not what was expected: $expected"
    )
  }
}
