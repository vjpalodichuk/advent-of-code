package com.capital7software.aoc2015.days;

import com.capital7software.aoc.aoc2015.days.Day21;
import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day21Test extends AdventOfCodeTestBase {
    private static final Logger LOGGER = Logger.getLogger(Day21Test.class.getName());

    @BeforeEach
    void setUp() {
        var instance = new Day21();
        setupFromFile(instance.getDefaultInputFilename());
    }

    @Test
    void leastAmountOfGoldAndStillWin() {
        var instance = new Day21();
        int expected = 65;
        var actual = instance.leastAmountOfGoldAndStillWin(lines);
        assertEquals(expected, actual, "The least amount of gold " +
                "is not what was expected: " + expected);
    }

    @Test
    void mostAmountOfGoldAndStillLose() {
        var instance = new Day21();
        int expected = 188;
        var actual = instance.mostAmountOfGoldAndStillLose(lines);
        assertEquals(expected, actual, "The most amount of gold " +
                "is not what was expected: " + expected);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}