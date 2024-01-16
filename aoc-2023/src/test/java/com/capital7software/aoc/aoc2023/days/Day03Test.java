package com.capital7software.aoc.aoc2023.days;

import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day03Test extends AdventOfCodeTestBase {
    @BeforeEach
    void setUp() {
        var instance = new Day03();
        setupFromFile(instance.getDefaultInputFilename());
    }

    @Test
    void getSumOfAllPartNumbersInSchematic() {
        var instance = new Day03();
        int expected = 4361;
        var actual = instance.getSumOfAllPartNumbersInSchematic(lines);
        assertEquals(expected, actual, "The sum of all PartNumbers " +
                "is not what was expected: " + expected);
    }

    @Test
    void getSumOfAllGearRatiosInSchematic() {
        var instance = new Day03();
        int expected = 467835;
        var actual = instance.getSumOfAllGearRatiosInSchematic(lines);
        assertEquals(expected, actual, "The sum of all gear ratios " +
                "is not what was expected: " + expected);
    }
}