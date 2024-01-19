package com.capital7software.aoc2015.days;

import com.capital7software.aoc.aoc2015.days.Day24;
import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day24Test extends AdventOfCodeTestBase {
    private static final Logger LOGGER = Logger.getLogger(Day24Test.class.getName());

    @BeforeEach
    void setUp() {
        var instance = new Day24();
        setupFromFile(instance.getDefaultInputFilename());
    }

    @Test
    void getLowestQEScoreWith3Partitions() {
        var instance = new Day24();
        int expected = 99;
        var actual = instance.getLowestQEScore(lines, 3);
        assertEquals(expected, actual, "The lowest QE Score with 3 partitions " +
                "is not what was expected: " + expected);
    }

    @Test
    void getLowestQEScoreWith4Partitions() {
        var instance = new Day24();
        int expected = 44;
        var actual = instance.getLowestQEScore(lines, 4);
        assertEquals(expected, actual, "The lowest QE Score with 4 partitions " +
                "is not what was expected: " + expected);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}