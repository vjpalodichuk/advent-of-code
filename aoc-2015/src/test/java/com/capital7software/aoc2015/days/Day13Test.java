package com.capital7software.aoc2015.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.capital7software.aoc.aoc2015.days.Day13;
import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Day13Test extends AdventOfCodeTestBase {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day13Test.class);

  @BeforeEach
  void setUp() {
    var instance = new Day13();
    setupFromFile(instance.getDefaultInputFilename());
  }

  @Test
  void greatestChangeInHappiness() {
    var instance = new Day13();
    var expected = 330;
    var actual = instance.greatestChangeInHappiness(lines);
    assertEquals(
        expected,
        actual,
        "Greatest change in happiness is not what was expected: " + expected
    );
  }

  @Test
  void greatestChangeInHappinessWithMeAtTheParty() {
    var instance = new Day13();
    var expected = 286;
    setupFromFile("inputs/input_day_13-02.txt");
    var actual = instance.greatestChangeInHappiness(lines);
    assertEquals(
        expected,
        actual,
        "Greatest change in happiness is not what was expected: " + expected
    );
  }

  @Override
  protected Logger getLogger() {
    return LOGGER;
  }
}