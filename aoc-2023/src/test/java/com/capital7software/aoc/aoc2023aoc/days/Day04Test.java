package com.capital7software.aoc.aoc2023aoc.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Day04Test extends AdventOfCodeTestBase {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day04Test.class);

  @BeforeEach
  void setUp() {
    var instance = new Day04();
    setupFromFile(instance.getDefaultInputFilename());
  }

  @Test
  void getSumOfAllCardPoints() {
    var instance = new Day04();
    int expected = 13;
    var actual = instance.getSumOfAllCardPoints(lines);
    assertEquals(expected, actual, "The sum of all ScratchCard points "
        + "is not what was expected: " + expected);
  }

  @Test
  void getTotalNumberOfCards() {
    var instance = new Day04();
    int expected = 30;
    var actual = instance.getTotalNumberOfCards(lines);
    assertEquals(expected, actual, "The total number of ScratchCards "
        + "is not what was expected: " + expected);
  }

  @Override
  protected Logger getLogger() {
    return LOGGER;
  }
}