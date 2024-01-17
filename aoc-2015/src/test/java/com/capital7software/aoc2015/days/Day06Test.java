package com.capital7software.aoc2015.days;

import com.capital7software.aoc.aoc2015.days.Day06;
import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day06Test extends AdventOfCodeTestBase {
    private static final Logger LOGGER = Logger.getLogger(Day06Test.class.getName());

    @BeforeEach
    void setUp() {
        var instance = new Day06();
        setupFromFile(instance.getDefaultInputFilename());
    }

    @Test
    void getLitLights() {
        var instance = new Day06();
        var expectedCount = 998_996;
        var lights = instance.loadLights(lines, 1_000, 1_000);
        lights.applyInstructions();
        var actualCount = instance.getOnLightCount(lights);

        assertEquals(expectedCount, actualCount);
    }

    @Test
    void getTotalBrightnessOfLights() {
        var instance = new Day06();
        var expectedCount = 1_001_996;
        var lights = instance.loadLights(lines, 1_000, 1_000);
        lights.applyNewInterpretationOfInstructions();
        var actualCount = instance.getTotalBrightness(lights);

        assertEquals(expectedCount, actualCount);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}