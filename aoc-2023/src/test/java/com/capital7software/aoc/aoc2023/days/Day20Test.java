package com.capital7software.aoc.aoc2023.days;

import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day20Test extends AdventOfCodeTestBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day20Test.class);

    @BeforeEach
    void setUp() {
        var instance = new Day20();
        setupFromFile(instance.getDefaultInputFilename());
    }

    @Test
    void getPulseProductSimple() {
        var instance = new Day20();
        var expected = 32_000_000;
        var actual = instance.getPulseProduct(lines, 1_000);
        assertEquals(expected, actual, "The pulse product after 1,000 button presses " +
                "is not what was expected: " + expected);
    }

    @Test
    void getPulseProductComplex() {
        var instance = new Day20();
        setupFromFile("inputs/input_day_20-02.txt");
        var expected = 11_687_500;
        var actual = instance.getPulseProduct(lines, 1_000);
        assertEquals(expected, actual, "The pulse product after 1,000 button presses " +
                "is not what was expected: " + expected);
    }

    @Test
    void getButtonPressesNeededToSendLowPulseToModuleSimple() {
        var instance = new Day20();
        var expected = -1;
        var actual = instance.getButtonPressesNeededToSendLowPulseToModule(
                lines,
                "c",
                1,
                10_000
        );
        assertEquals(expected, actual, "The minimum number of button presses " +
                "is not what was expected: " + expected);
    }

    @Test
    void getButtonPressesNeededToSendLowPulseToModuleComplex() {
        var instance = new Day20();
        setupFromFile("inputs/input_day_20-02.txt");
        var expected = 1;
        var actual = instance.getButtonPressesNeededToSendLowPulseToModule(
                lines,
                "output",
                1,
                10_000
        );
        assertEquals(expected, actual, "The minimum number of button presses " +
                "is not what was expected: " + expected);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}