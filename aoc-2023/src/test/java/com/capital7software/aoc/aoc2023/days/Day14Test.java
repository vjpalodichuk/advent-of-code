package com.capital7software.aoc.aoc2023.days;

import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day14Test extends AdventOfCodeTestBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day14Test.class);

    @BeforeEach
    void setUp() {
        var instance = new Day14();
        setupFromFile(instance.getDefaultInputFilename());
    }

    @Test
    void tiltAndCalculateLoadOnNorthSupports() {
        var instance = new Day14();
        int expected = 136;
        var actual = instance.tiltAndCalculateLoadOnNorthSupports(lines);
        assertEquals(expected, actual, "The load on the North supports " +
                "is not what was expected: " + expected);
    }

    @Test
    void spinAndCalculateLoadOnNorthSupports() {
        var instance = new Day14();
        var cycleCount = 1_000_000_000;
        int expected = 64;
        var actual = instance.spinAndCalculateLoadOnNorthSupports(lines, cycleCount);
        assertEquals(expected, actual, "The load on the North supports " +
                "is not what was expected: " + expected);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}