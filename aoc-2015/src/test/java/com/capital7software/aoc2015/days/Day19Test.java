package com.capital7software.aoc2015.days;

import com.capital7software.aoc.aoc2015.days.Day19;
import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day19Test extends AdventOfCodeTestBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day19Test.class);

    @BeforeEach
    void setUp() {
        var instance = new Day19();
        setupFromFile(instance.getDefaultInputFilename());
    }

    @Test
    void calibrate() {
        var instance = new Day19();
        int expected = 7;

        var actual = instance.calibrate(lines);
        assertEquals(expected, actual.first(), "The number of distinct molecules " +
                "is not what was expected: " + expected);
    }

    @Test
    void fabricate() {
        var instance = new Day19();
        int expected = 6;

        var actual = instance.fabricate(lines);
        assertEquals(expected, actual.first(), "The fewest number of steps to build the medicine " +
                "is not what was expected: " + expected);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}