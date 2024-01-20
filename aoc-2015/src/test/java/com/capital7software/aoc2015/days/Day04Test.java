package com.capital7software.aoc2015.days;

import com.capital7software.aoc.aoc2015.days.Day04;
import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day04Test extends AdventOfCodeTestBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day04Test.class);

    @BeforeEach
    void setUp() {
        var instance = new Day04();
        setupFromFile(instance.getDefaultInputFilename());
    }

    @Test
    void getLowestPositiveNumberWithFiveLeadingZerosMD5Hash() {
        var instance = new Day04();
        var expected = List.of(609043L, 1048970L);
        for (int i = 0; i < lines.size(); i++) {
            var actual = instance.lowestPositiveNumber(lines.get(i), 5);

            assertEquals(expected.get(i), actual);
        }
    }

    @Test
    void getLowestPositiveNumberWithSixLeadingZerosMD5Hash() {
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