package com.capital7software.aoc.aoc2015aoc.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import java.util.List;
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
  void getUniqueHousesVisited() {
    var instance = new Day03();
    var expected = List.of(2L, 4L, 2L);
    for (int i = 0; i < lines.size(); i++) {
      var routeId = instance.deliverGifts(lines.get(i));
      var actual = instance.getUniqueHouseCount(routeId);

      assertEquals(expected.get(i), actual);
    }
  }

  @Test
  void getUniqueHousesVisitedWithRoboSanta() {
    var instance = new Day03();
    var expected = List.of(3L, 3L, 11L);
    for (int i = 0; i < lines.size(); i++) {
      var routeId = instance.deliverGiftsWithRoboSanta(lines.get(i));
      var actual = instance.getUniqueHouseCount(routeId);

      assertEquals(expected.get(i), actual);
    }
  }

  @Override
  protected Logger getLogger() {
    return LOGGER;
  }
}