package com.capital7software.aoc.aoc2015aoc.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Day15Test extends AdventOfCodeTestBase {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day15Test.class);

  @BeforeEach
  void setUp() {
    var instance = new Day15();
    setupFromFile(instance.getDefaultInputFilename());
  }

  @Test
  void whatIsTheTotalScoreOfTheHighestScoringCookie() {
    var instance = new Day15();
    long expected = 62_842_880;

    var actual = instance.whatIsTheTotalScoreOfTheHighestScoringCookie(lines, 100_000);
    assertEquals(expected, actual, "The total score of the highest-scoring "
        + "cookie is not what was expected: " + expected);
  }

  @Test
  void whatIsTheTotalScoreOfTheHighestScoringCalorieRestrictedCookie() {
    var instance = new Day15();
    long expected = 57_600_000;

    var actual = instance.whatIsTheTotalScoreOfTheHighestScoringCaloriesRestrictedCookie(
        lines,
        250_000
    );
    assertEquals(expected, actual, "The total score of the highest-scoring "
        + "cookie is not what was expected: " + expected);
  }

  @Override
  protected Logger getLogger() {
    return LOGGER;
  }
}