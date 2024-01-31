package com.capital7software.aoc.aoc2023.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Day05Test extends AdventOfCodeTestBase {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day05Test.class);

  @BeforeEach
  void setUp() {
    var instance = new Day05();
    setupFromFile(instance.getDefaultInputFilename());
  }

  @Test
  void getLowestLocationNumber() {
    var instance = new Day05();
    int expected = 35;
    var actual = instance.getLowestLocationNumber(lines);
    assertEquals(expected, actual, "The lowest location number "
        + "is not what was expected: " + expected);
  }

  @Test
  void getTotalNumberOfCards() {
    var instance = new Day05();
    int expected = 46;
    var actual = instance.getLowestLocationNumberForAnyInitialSeed(lines);
    assertEquals(expected, actual, "The lowest location number for any initial seed "
        + "is not what was expected: " + expected);
  }

  @Override
  protected Logger getLogger() {
    return LOGGER;
  }
}