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

class Day10Test {
    List<String> lines;
    Path path;

    @BeforeEach
    void setUp() {
        var instance = new Day10();
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
    void getRunLengthEncodedStringString() {
        var instance = new Day10();
        String[] expected = {"11", "21", "1211", "111221", "312211"};
        String[] actual = new String[expected.length];
        for (int i = 0; i < expected.length; i++) {
            actual[i] = instance.getRunLengthEncodedStringString(lines.get(i));
            assertEquals(expected[i], actual[i], "Encoded string not what was expected: "
                    + expected[i] + " actual: " + actual[i]);
        }
    }
}