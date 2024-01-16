package com.capital7software.aoc2015.days;

import com.capital7software.aoc.aoc2015.days.Day05;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day05Test {
    List<String> lines;
    Path path;

    @BeforeEach
    void setUp() {
        var instance = new Day05();
        setupByFile(instance.getDefaultInputFilename());
    }

    void setupByFile(String filename) {
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
    void getNaughtyOrNice() {
        var instance = new Day05();
        var expected = List.of(true, true, false, false, false);
        var expectedCount = 2;
        var actualCount = 0;
        for (int i = 0; i < lines.size(); i++) {
            var actual = instance.isNice(lines.get(i));

            if (actual) {
                actualCount++;
            }
            assertEquals(expected.get(i), actual, "Test input: " + lines.get(i));
        }
        assertEquals(expectedCount, actualCount);
    }

    @Test
    void getNewNaughtyOrNice() {
        setupByFile("inputs/input_day_05-02.txt");
        var instance = new Day05();
        var expected = List.of(true, true, false, false);
        var expectedCount = 2;
        var actualCount = 0;
        for (int i = 0; i < lines.size(); i++) {
            var actual = instance.isNewNice(lines.get(i));

            if (actual) {
                actualCount++;
            }
            assertEquals(expected.get(i), actual, "Test input: " + lines.get(i));
        }
        assertEquals(expectedCount, actualCount);
    }
}