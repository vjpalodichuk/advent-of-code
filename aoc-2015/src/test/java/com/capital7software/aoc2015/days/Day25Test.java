package com.capital7software.aoc2015.days;

import com.capital7software.aoc.aoc2015.days.Day25;
import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import com.capital7software.aoc.lib.grid.CodeGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day25Test extends AdventOfCodeTestBase {
    private static final Logger LOGGER = Logger.getLogger(Day25Test.class.getName());

    @BeforeEach
    void setUp() {
        var instance = new Day25();
        setupFromFile(instance.getDefaultInputFilename());
    }

    @Test
    void getNextCode() {
        var instance = new Day25();
        var generator = CodeGenerator.buildGenerator(lines.getFirst());
        int expected = 9132360;
        var actual = instance.getNextCode(generator);
        assertEquals(expected, actual, "The next code " +
                "is not what was expected: " + expected);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}