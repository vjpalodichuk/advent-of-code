package com.capital7software.aoc.aoc2015aoc.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import java.util.List;
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
  void getLowestPositiveNumberWithFiveLeadingZerosMd5Hash() {
    var instance = new Day04();
    var expected = List.of(609043L, 1048970L);
    for (int i = 0; i < lines.size(); i++) {
      var actual = instance.lowestPositiveNumber(lines.get(i), 5);

      assertEquals(expected.get(i), actual);
    }
  }

  @Test
  void getLowestPositiveNumberWithSixLeadingZerosMd5Hash() {
    var instance = new Day04();
    var expected = List.of(6742839L, 5714438L);
    for (int i = 0; i < lines.size(); i++) {
      var actual = instance.lowestPositiveNumber(lines.get(i), 6);

      assertEquals(expected.get(i), actual);
    }
  }

  @Override
  protected Logger getLogger() {
    return LOGGER;
  }
}