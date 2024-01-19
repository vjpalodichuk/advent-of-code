package com.capital7software.aoc.aoc2023.days;

import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day13Test extends AdventOfCodeTestBase {
    private static final Logger LOGGER = Logger.getLogger(Day13Test.class.getName());

    @BeforeEach
    void setUp() {
        var instance = new Day13();
        setupFromFile(instance.getDefaultInputFilename());
    }

    @Test
    void summarizePatternReflectionNotes() {
        var instance = new Day13();
        int expected = 405;
        var actual = instance.summarizePatternReflectionNotes(lines, 0);
        assertEquals(expected, actual, "The pattern notes summary " +
                "is not what was expected: " + expected);
    }

    @Test
    void summarizePatternReflectionNotesWithSmudge() {
        var instance = new Day13();
        int expected = 400;
        var actual = instance.summarizePatternReflectionNotes(lines, 1);
        assertEquals(expected, actual, "The pattern notes counting smudges summary " +
                "is not what was expected: " + expected);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}