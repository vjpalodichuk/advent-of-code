package com.capital7software.aoc.aoc2015aoc.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Day20Test extends AdventOfCodeTestBase {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day20Test.class);

  @BeforeEach
  void setUp() {
    var instance = new Day20();
    setupFromFile(instance.getDefaultInputFilename());
  }

  @Test
  void lowestHouseNumber() {
    var instance = new Day20();
    int expected = 776160;
    var presents = Integer.parseInt(lines.getFirst());
    var actual = instance.lowestHouseNumber(presents);
    assertEquals(expected, actual, "The number for the house "
        + "is not what was expected: " + expected);
  }

  @Test
  void lowestHouseNumberNewRules() {
    var instance = new Day20();
    int expected = 786240;
    var presents = Integer.parseInt(lines.getFirst());
    var actual = instance.lowestHouseNumberNewRules(presents);
    assertEquals(expected, actual, "The number for the house "
        + "is not what was expected: " + expected);
  }

  @Override
  protected Logger getLogger() {
    return LOGGER;
  }
}