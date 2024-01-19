package com.capital7software.aoc2015.days;

import com.capital7software.aoc.aoc2015.days.Day23;
import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day23Test extends AdventOfCodeTestBase {
    private static final Logger LOGGER = Logger.getLogger(Day23Test.class.getName());

    @BeforeEach
    void setUp() {
        var instance = new Day23();
        setupFromFile(instance.getDefaultInputFilename());
    }

    @Test
    void runProgramAndGetValueInRegister() {
        var instance = new Day23();
        int expected = 2;
        var actual = instance.runProgramAndGetValueInRegister(lines, "a");
        assertEquals(expected, actual, "The value in register a " +
                "is not what was expected: " + expected);
    }

    @Test
    void runProgramWithStartingRegisterAndValueAndGetValueInRegister() {
        var instance = new Day23();
        int expected = 7;
        var actual = instance.runProgramWithStartingRegisterAndValueAndGetValueInRegister(lines, "a", 1L, "a");
        assertEquals(expected, actual, "The value in register a " +
                "is not what was expected: " + expected);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}