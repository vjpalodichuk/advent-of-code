package com.capital7software.aoc.aoc2023.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Day02Test extends AdventOfCodeTestBase {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day02Test.class);

  @BeforeEach
  void setUp() {
    var instance = new Day02();
    setupFromFile(instance.getDefaultInputFilename());
  }

  @Test
  void getSumOfGameResultIds() {
    var instance = new Day02();
    int expected = 8;
    var actual = instance.getSumOfGameResultIds(lines);
    assertEquals(expected, actual, "The sum of possible Game IDs "
        + "is not what was expected: " + expected);
  }

  @Test
  void getSumOfGameResultPower() {
    var instance = new Day02();
    int expected = 2286;
    var actual = instance.getSumOfGameResultPower(lines);
    assertEquals(expected, actual, "The sum of Game Powers "
        + "is not what was expected: " + expected);
  }

  @Override
  protected Logger getLogger() {
    return LOGGER;
  }
}