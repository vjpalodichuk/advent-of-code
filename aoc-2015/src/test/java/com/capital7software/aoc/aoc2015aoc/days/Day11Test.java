package com.capital7software.aoc.aoc2015aoc.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Day11Test extends AdventOfCodeTestBase {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day11Test.class);

  @BeforeEach
  void setUp() {
    var instance = new Day11();
    setupFromFile(instance.getDefaultInputFilename());
  }

  @Test
  void isValidPassword() {
    var instance = new Day11();
    boolean[] expected = {false, false, false, false, true, false, true};
    boolean[] actual = new boolean[expected.length];

    for (int i = 0; i < expected.length; i++) {
      actual[i] = instance.isValidPassword(lines.get(i));
      assertEquals(expected[i], actual[i], "Password validation is not what was expected: "
          + expected[i] + " actual: " + actual[i] + " for word: " + lines.get(i));
    }
  }

  @Test
  void suggestNextPassword() {
    var instance = new Day11();
    String[] expected = {"hjaaabcc", "abbcefgg", "abbcffgh", "abcdffaa", "abcdffbb", "ghjaabcc",
        "ghjbbcdd"};
    String[] actual = new String[expected.length];

    for (int i = 0; i < expected.length; i++) {
      actual[i] = instance.suggestNextPassword(lines.get(i));
      assertEquals(expected[i], actual[i], "Suggest Password is not what was expected: "
          + expected[i] + " actual: " + actual[i] + " for word: " + lines.get(i));
    }
  }

  @Override
  protected Logger getLogger() {
    return LOGGER;
  }
}