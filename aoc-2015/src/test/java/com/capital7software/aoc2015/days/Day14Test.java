package com.capital7software.aoc2015.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.capital7software.aoc.aoc2015.days.Day14;
import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Day14Test extends AdventOfCodeTestBase {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day14Test.class);

  @BeforeEach
  void setUp() {
    var instance = new Day14();
    setupFromFile(instance.getDefaultInputFilename());
  }

  @Test
  void whatDistanceHasTheWinnerTraveled() {
    var instance = new Day14();
    double[] expected = {16.0, 160.0, 176.0, 1120.0};

    var actual = instance.whatDistanceHasTheWinnerTraveled(lines, 1);
    assertEquals(expected[0], actual, "The distance traveled of the winning reindeer is "
        + "not what was expected: " + expected[0]);

    actual = instance.whatDistanceHasTheWinnerTraveled(lines, 10);
    assertEquals(expected[1], actual, "The distance traveled of the winning reindeer is "
        + "not what was expected: " + expected[0]);

    actual = instance.whatDistanceHasTheWinnerTraveled(lines, 11);
    assertEquals(expected[2], actual, "The distance traveled of the winning reindeer is "
        + "not what was expected: " + expected[0]);

    actual = instance.whatDistanceHasTheWinnerTraveled(lines, 1000);
    assertEquals(expected[3], actual, "The distance traveled of the winning reindeer is "
        + "not what was expected: " + expected[0]);
  }

  @Test
  void howManyPointsDoesTheWinnerHave() {
    var instance = new Day14();
    double[] expected = {1.0, 10.0, 11.0, 689.0};

    var actual = instance.howManyPointsDoesTheWinnerHave(lines, 1);
    assertEquals(expected[0], actual, "The distance traveled of the winning reindeer is "
        + "not what was expected: " + expected[0]);

    actual = instance.howManyPointsDoesTheWinnerHave(lines, 10);
    assertEquals(expected[1], actual, "The distance traveled of the winning reindeer is "
        + "not what was expected: " + expected[0]);

    actual = instance.howManyPointsDoesTheWinnerHave(lines, 11);
    assertEquals(expected[2], actual, "The distance traveled of the winning reindeer is "
        + "not what was expected: " + expected[0]);

    actual = instance.howManyPointsDoesTheWinnerHave(lines, 1000);
    assertEquals(expected[3], actual, "The distance traveled of the winning reindeer is "
        + "not what was expected: " + expected[0]);
  }

  @Override
  protected Logger getLogger() {
    return LOGGER;
  }
}