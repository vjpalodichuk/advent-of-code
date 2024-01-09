package com.capital7software.aoc2015.days;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day20Test {
    List<String> lines;
    Path path;

    @BeforeEach
    void setUp() {
        var instance = new Day20();
        setupFromFile(instance.getDefaultInputFilename());
    }

    void setupFromFile(String filename) {
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            var url = classloader.getResource(filename);
            assert url != null;
            path = Paths.get(url.toURI());
            lines = Files.readAllLines(path);
        } catch (URISyntaxException | IOException e) {
            System.out.printf("Unable to load input data from: %s%n", path);
            throw new RuntimeException(e);
        }
    }

    @Test
    void lowestHouseNumber() {
        var instance = new Day20();
        int expected = 776160;
        var presents = Integer.parseInt(lines.getFirst());
        var actual = instance.lowestHouseNumber(presents);
        assertEquals(expected, actual, "The number for the house " +
                "is not what was expected: " + expected);
    }

    @Test
    void lowestHouseNumberNewRules() {
        var instance = new Day20();
        int expected = 786240;
        var presents = Integer.parseInt(lines.getFirst());
        var actual = instance.lowestHouseNumberNewRules(presents);
        assertEquals(expected, actual, "The number for the house " +
                "is not what was expected: " + expected);
    }
}