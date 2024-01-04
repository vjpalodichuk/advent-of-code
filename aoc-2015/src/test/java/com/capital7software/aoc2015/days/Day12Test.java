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

class Day12Test {
    List<String> lines;
    Path path;

    @BeforeEach
    void setUp() {
        var instance = new Day12();
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
    void sumNumbersInString() {
        var instance = new Day12();
        long[] expected = {6, 6, 3, 3, 0, 0, 0, 0};
        long[] actual = new long[expected.length];

        for (int i = 0; i < expected.length; i++) {
            actual[i] = instance.sumNumbersInString(lines.get(i));
            assertEquals(expected[i], actual[i], "Sum of numbers in JSON string is not what was expected: "
                    + expected[i] + " actual: " + actual[i] + " for JSON: " + lines.get(i));
        }
    }
}