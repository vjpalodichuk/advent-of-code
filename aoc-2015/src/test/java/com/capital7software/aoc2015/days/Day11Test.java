package com.capital7software.aoc2015.days;

import com.capital7software.aoc.aoc2015.days.Day11;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day11Test {
    List<String> lines;
    Path path;

    @BeforeEach
    void setUp() {
        var instance = new Day11();
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
    void isValidPassword() {
        var instance = new Day11();
        boolean[] expected = {false, false, false, false, true, false, true};
        boolean[] actual = new boolean[expected.length];

        for (int i = 0; i < expected.length; i++) {
            actual[i] = instance.isValidPassword(lines.get(i));
            assertEquals(expected[i], actual[i], "Password validation is not what was expected: "
                    + expected[i] + " actual: " + actual[i] + " for word: " + lines.get(i));
        }
    }

    @Test
    void suggestNextPassword() {
        var instance = new Day11();
        String[] expected = {"hjaaabcc", "abbcefgg", "abbcffgh", "abcdffaa", "abcdffbb", "ghjaabcc", "ghjbbcdd"};
        String[] actual = new String[expected.length];

        for (int i = 0; i < expected.length; i++) {
            actual[i] = instance.suggestNextPassword(lines.get(i));
            assertEquals(expected[i], actual[i], "Suggest Password is not what was expected: "
                    + expected[i] + " actual: " + actual[i] + " for word: " + lines.get(i));
        }
    }
}