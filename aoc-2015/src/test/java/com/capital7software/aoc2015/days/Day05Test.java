package com.capital7software.aoc2015.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.capital7software.aoc.aoc2015.days.Day05;
import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import java.util.List;
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
  void getNaughtyOrNice() {
    var instance = new Day05();
    var expected = List.of(true, true, false, false, false);
    var expectedCount = 2;
    var actualCount = 0;
    for (int i = 0; i < lines.size(); i++) {
      var actual = instance.isNice(lines.get(i));

      if (actual) {
        actualCount++;
      }
      assertEquals(expected.get(i), actual, "Test input: " + lines.get(i));
    }
    assertEquals(expectedCount, actualCount);
  }

  @Test
  void getNewNaughtyOrNice() {
    setupFromFile("inputs/input_day_05-02.txt");
    var instance = new Day05();
    var expected = List.of(true, true, false, false);
    var expectedCount = 2;
    var actualCount = 0;
    for (int i = 0; i < lines.size(); i++) {
      var actual = instance.isNewNice(lines.get(i));

      if (actual) {
        actualCount++;
      }
      assertEquals(expected.get(i), actual, "Test input: " + lines.get(i));
    }
    assertEquals(expectedCount, actualCount);
  }

  @Override
  protected Logger getLogger() {
    return LOGGER;
  }
}