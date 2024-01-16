package com.capital7software.aoc2015.days;

import com.capital7software.aoc.aoc2015.days.Day24;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day24Test {
    List<String> lines;
    Path path;

    @BeforeEach
    void setUp() {
        var instance = new Day24();
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
    void getLowestQEScoreWith3Partitions() {
        var instance = new Day24();
        int expected = 99;
        var actual = instance.getLowestQEScore(lines, 3);
        assertEquals(expected, actual, "The lowest QE Score with 3 partitions " +
                "is not what was expected: " + expected);
    }

    @Test
    void getLowestQEScoreWith4Partitions() {
        var instance = new Day24();
        int expected = 44;
        var actual = instance.getLowestQEScore(lines, 4);
        assertEquals(expected, actual, "The lowest QE Score with 4 partitions " +
                "is not what was expected: " + expected);
    }
}