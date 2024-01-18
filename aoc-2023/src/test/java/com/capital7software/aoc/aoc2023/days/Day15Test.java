package com.capital7software.aoc.aoc2023.days;

import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day15Test extends AdventOfCodeTestBase {
    private static final Logger LOGGER = Logger.getLogger(Day15Test.class.getName());

    @BeforeEach
    void setUp() {
        var instance = new Day15();
        setupFromFile(instance.getDefaultInputFilename());
    }

    @Test
    void sumOfLensHashes() {
        var instance = new Day15();
        var expected = 1320;
        var actual = instance.sumOfLensHashes(lines);
        assertEquals(expected, actual, "The sum of all the lens hashes " +
                "is not what was expected: " + expected);
    }

    @Test
    void focusingPowerOfLensConfiguration() {
        var instance = new Day15();
        var expected = 145;
        var actual = instance.focusingPowerOfLensConfiguration(lines);
        assertEquals(expected, actual, "The focusing power of the lens configuration " +
                "is not what was expected: " + expected);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}