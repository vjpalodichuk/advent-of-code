package com.capital7software.aoc2015.days;

import com.capital7software.aoc.aoc2015.days.Day10;
import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day10Test extends AdventOfCodeTestBase {
    private static final Logger LOGGER = Logger.getLogger(Day10Test.class.getName());

    @BeforeEach
    void setUp() {
        var instance = new Day10();
        setupFromFile(instance.getDefaultInputFilename());
    }

    @Test
    void getRunLengthEncodedStringString() {
        var instance = new Day10();
        String[] expected = {"11", "21", "1211", "111221", "312211"};
        String[] actual = new String[expected.length];
        for (int i = 0; i < expected.length; i++) {
            actual[i] = instance.getRunLengthEncodedStringString(lines.get(i));
            assertEquals(expected[i], actual[i], "Encoded string not what was expected: "
                    + expected[i] + " actual: " + actual[i]);
        }
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}