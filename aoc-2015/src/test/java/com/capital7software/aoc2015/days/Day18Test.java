package com.capital7software.aoc2015.days;

import com.capital7software.aoc.aoc2015.days.Day18;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day18Test {
    List<String> lines;
    Path path;

    @BeforeEach
    void setUp() {
        var instance = new Day18();
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
    void animateLights() {
        var instance = new Day18();
        int expected = 4;

        var actual = instance.animateLights(4, lines, false);
        assertEquals(expected, actual.first(), "The number of lights on " +
                "is not what was expected: " + expected);
    }

    @Test
    void animateLightsCornersAlwaysOn() {
        var instance = new Day18();
        int expected = 17;

        var actual = instance.animateLights(5, lines, true);
        assertEquals(expected, actual.first(), "The number of lights on " +
                "is not what was expected: " + expected);
    }

}