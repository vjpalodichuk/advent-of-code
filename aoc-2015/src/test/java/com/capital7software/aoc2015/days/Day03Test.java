package com.capital7software.aoc2015.days;

import com.capital7software.aoc.aoc2015.days.Day03;
import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day03Test extends AdventOfCodeTestBase {
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
}