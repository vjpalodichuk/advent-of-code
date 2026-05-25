package com.capital7software.aoc.aoc2024aoc.days

import com.capital7software.aoc.lib.AdventOfCodeTestBase
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
  fun part1SampleShouldCountHistorianCandidateTriples() {
    val instance = Day23()
    val expected = 7
    val actual = instance.countHistorianCandidateTriples(lines)

    Assertions.assertEquals(
        expected,
        actual,
        "The number of historian candidate triples does not match the expected number."
    )
  }

  @Test
  fun part2SampleShouldFindLanPartyPassword() {
    val instance = Day23()
    val expected = "co,de,ka,ta"
    val actual = instance.lanPartyPassword(lines)

    Assertions.assertEquals(
        expected,
        actual,
        "The generated lan password does not match the expected password."
    )
  }
}
