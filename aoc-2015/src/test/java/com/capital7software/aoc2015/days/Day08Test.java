package com.capital7software.aoc2015.days;

import com.capital7software.aoc.aoc2015.days.Day08;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day08Test {
    List<String> lines;
    Path path;

    @BeforeEach
    void setUp() {
        var instance = new Day08();
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
    void codeCountOfStrings() {
        var instance = new Day08();
        var expected = new int[] {2, 5, 10, 6, 43};

        for (int i = 0; i < lines.size(); i++) {
            var actual = instance.codeCount(lines.get(i));
            assertEquals(expected[i], actual, "Code count didn't match for: " + lines.get(i));
        }
    }

    @Test
    void codeMemoryOfStrings() {
        var instance = new Day08();
        var expected = new int[] {0, 3, 7, 1, 29};

        for (int i = 0; i < lines.size(); i++) {
            var actual = instance.memoryCount(lines.get(i));
            assertEquals(expected[i], actual, "Memory count didn't match for: " + lines.get(i));
        }
    }

    @Test
    void codeCountOfNewStrings() {
        var instance = new Day08();
        var expected = new int[] {6, 9, 16, 11, 56};

        for (int i = 0; i < lines.size(); i++) {
            var actual = instance.codeCount(instance.encodeString(lines.get(i)));
            assertEquals(expected[i], actual, "Code count didn't match for: " + lines.get(i));
        }
    }

}