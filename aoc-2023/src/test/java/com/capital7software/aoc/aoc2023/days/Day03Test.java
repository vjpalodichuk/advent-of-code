package com.capital7software.aoc.aoc2023.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Day03Test extends AdventOfCodeTestBase {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day03Test.class);

  @BeforeEach
  void setUp() {
    var instance = new Day03();
    setupFromFile(instance.getDefaultInputFilename());
  }

  @Test
  void getSumOfAllPartNumbersInSchematic() {
    var instance = new Day03();
    int expected = 4361;
    var actual = instance.getSumOfAllPartNumbersInSchematic(lines);
    assertEquals(expected, actual, "The sum of all PartNumbers "
        + "is not what was expected: " + expected);
  }

  @Test
  void getSumOfAllGearRatiosInSchematic() {
    var instance = new Day03();
    int expected = 467835;
    var actual = instance.getSumOfAllGearRatiosInSchematic(lines);
    assertEquals(expected, actual, "The sum of all gear ratios "
        + "is not what was expected: " + expected);
  }

  @Override
  protected Logger getLogger() {
    return LOGGER;
  }
}