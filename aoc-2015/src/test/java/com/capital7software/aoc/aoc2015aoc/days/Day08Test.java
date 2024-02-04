package com.capital7software.aoc.aoc2015aoc.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Day08Test extends AdventOfCodeTestBase {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day08Test.class);

  @BeforeEach
  void setUp() {
    var instance = new Day08();
    setupFromFile(instance.getDefaultInputFilename());
  }

  @Test
  void codeCountOfStrings() {
    var instance = new Day08();
    var expected = new int[]{2, 5, 10, 6, 43};

    for (int i = 0; i < lines.size(); i++) {
      var actual = instance.codeCount(lines.get(i));
      assertEquals(expected[i], actual, "Code count didn't match for: " + lines.get(i));
    }
  }

  @Test
  void codeMemoryOfStrings() {
    var instance = new Day08();
    var expected = new int[]{0, 3, 7, 1, 29};

    for (int i = 0; i < lines.size(); i++) {
      var actual = instance.memoryCount(lines.get(i));
      assertEquals(expected[i], actual, "Memory count didn't match for: " + lines.get(i));
    }
  }

  @Test
  void codeCountOfNewStrings() {
    var instance = new Day08();
    var expected = new int[]{6, 9, 16, 11, 56};

    for (int i = 0; i < lines.size(); i++) {
      var actual = instance.codeCount(instance.encodeString(lines.get(i)));
      assertEquals(expected[i], actual, "Code count didn't match for: " + lines.get(i));
    }
  }

  @Override
  protected Logger getLogger() {
    return LOGGER;
  }
}