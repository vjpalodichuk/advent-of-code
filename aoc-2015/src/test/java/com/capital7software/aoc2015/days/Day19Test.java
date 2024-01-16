package com.capital7software.aoc2015.days;

import com.capital7software.aoc.aoc2015.days.Day19;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day19Test {
    List<String> lines;
    Path path;

    @BeforeEach
    void setUp() {
        var instance = new Day19();
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
    void calibrate() {
        var instance = new Day19();
        int expected = 7;

        var actual = instance.calibrate(lines);
        assertEquals(expected, actual.first(), "The number of distinct molecules " +
                "is not what was expected: " + expected);
    }

    @Test
    void fabricate() {
        var instance = new Day19();
        int expected = 6;

        var actual = instance.fabricate(lines);
        assertEquals(expected, actual.first(), "The fewest number of steps to build the medicine " +
                "is not what was expected: " + expected);
    }
}