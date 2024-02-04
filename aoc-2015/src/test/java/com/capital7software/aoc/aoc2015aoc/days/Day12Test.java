package com.capital7software.aoc.aoc2015aoc.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Day12Test extends AdventOfCodeTestBase {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day12Test.class);

  @BeforeEach
  void setUp() {
    var instance = new Day12();
    setupFromFile(instance.getDefaultInputFilename());
  }

  @Test
  void sumNumbersInString() {
    var instance = new Day12();
    long[] expected = {6, 6, 3, 3, 0, 0, 0, 0, 6, 15, 6, 156366};
    long[] actual = new long[expected.length];

    for (int i = 0; i < expected.length; i++) {
      actual[i] = instance.sumNumbersInString(lines.get(i));
      assertEquals(
          expected[i],
          actual[i],
          "Sum of numbers in JSON string is not what was expected: "
          + expected[i] + " actual: " + actual[i] + " for JSON: " + lines.get(i)
      );
    }
  }

  @Test
  void sumNumbersInStringSkippingRedObjects() {
    var instance = new Day12();
    long[] expected = {6, 6, 3, 3, 0, 0, 0, 0, 4, 0, 6, 96852};
    long[] actual = new long[expected.length];

    for (int i = 0; i < expected.length; i++) {
      actual[i] = instance.sumNumbersInStringSkippingRedObjects(lines.get(i));
      assertEquals(
          expected[i],
          actual[i],
          "Sum of numbers in JSON string is not what was expected: "
          + expected[i] + " actual: " + actual[i] + " for JSON: " + lines.get(i)
      );
    }
  }

  @Override
  protected Logger getLogger() {
    return LOGGER;
  }
}