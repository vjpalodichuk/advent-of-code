package com.capital7software.aoc2015.days;

import com.capital7software.aoc.aoc2015.days.Day01;
import com.capital7software.aoc.lib.AdventOfCodeTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day01Test extends AdventOfCodeTestBase {

    @BeforeEach
    void setUp() {
        var instance = new Day01();
        setupFromFile(instance.getDefaultInputFilename());
    }

    @Test
    void getFloor() {
        var instance = new Day01();
        var actual = instance.getFloor(lines);

        assertEquals(-3, actual);
    }

    @Test
    void getFirstBasementFloorPosition() {
        var instance = new Day01();
        var actual = instance.getFirstBasementFloorPosition(lines);

        assertEquals(1, actual);
    }
}