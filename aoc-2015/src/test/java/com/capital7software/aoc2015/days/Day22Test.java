package com.capital7software.aoc2015.days;

import com.capital7software.aoc.aoc2015.days.Day22;
import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day22Test extends AdventOfCodeTestBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day22Test.class);

    @BeforeEach
    void setUp() {
        var instance = new Day22();
        setupFromFile(instance.getDefaultInputFilename());
    }

    @Test
    void leastAmountOfManaAndStillWin() {
        var instance = new Day22();
        int expected = 953;
        var actual = instance.leastAmountOfManaAndStillWin(lines, false);
        assertEquals(expected, actual, "The least amount of mana " +
                "is not what was expected: " + expected);
    }

    @Test
    void leastAmountOfManaOnHardAndStillWin() {
        var instance = new Day22();
        int expected = 1289;
        var actual = instance.leastAmountOfManaAndStillWin(lines, true);
        assertEquals(expected, actual, "The least amount of mana on hard " +
                "is not what was expected: " + expected);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}