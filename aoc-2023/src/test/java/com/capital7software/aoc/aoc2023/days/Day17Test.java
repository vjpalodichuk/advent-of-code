package com.capital7software.aoc.aoc2023.days;

import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day17Test extends AdventOfCodeTestBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day17Test.class);

    @BeforeEach
    void setUp() {
        var instance = new Day17();
        setupFromFile(instance.getDefaultInputFilename());
    }

    @Test
    void calculateMinimumHeatLossWith1MinStepAnd3MaxStepsNormal() {
        var instance = new Day17();
        var expected = 102;
        var actual = instance.calculateMinimumHeatLoss(lines, 1, 3);
        assertEquals(expected, actual, "The minimum heat loss " +
                "is not what was expected: " + expected);
    }

    @Test
    void calculateMinimumHeatLossWith4MinStepAnd10MaxStepsNormal() {
        var instance = new Day17();
        var expected = 94;
        var actual = instance.calculateMinimumHeatLoss(lines, 4, 10);
        assertEquals(expected, actual, "The minimum heat loss " +
                "is not what was expected: " + expected);
    }

    @Test
    void calculateMinimumHeatLossWith4MinStepAnd10MaxStepsSmall() {
        var instance = new Day17();
        setupFromFile("inputs/input_day_17-02.txt");
        var expected = 71;
        var actual = instance.calculateMinimumHeatLoss(lines, 4, 10);
        assertEquals(expected, actual, "The minimum heat loss " +
                "is not what was expected: " + expected);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}