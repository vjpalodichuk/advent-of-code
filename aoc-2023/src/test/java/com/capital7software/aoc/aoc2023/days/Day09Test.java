package com.capital7software.aoc.aoc2023.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Day09Test extends AdventOfCodeTestBase {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day09Test.class);

  @BeforeEach
  void setUp() {
    var instance = new Day09();
    setupFromFile(instance.getDefaultInputFilename());
  }

  @Test
  void sumOfNextPredictedHistories() {
    var instance = new Day09();
    int expected = 114;
    var actual = instance.sumOfNextPredictedHistories(lines);
    assertEquals(expected, actual, "The total of next predicted values "
        + "is not what was expected: " + expected);
  }

  @Test
  void sumOfPreviousPredictedHistories() {
    var instance = new Day09();
    int expected = 2;
    var actual = instance.sumOfPreviousPredictedHistories(lines);
    assertEquals(expected, actual, "The total of previous predicted values "
        + "is not what was expected: " + expected);
  }

  @Override
  protected Logger getLogger() {
    return LOGGER;
  }
}