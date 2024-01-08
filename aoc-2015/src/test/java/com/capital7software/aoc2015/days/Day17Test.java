package com.capital7software.aoc2015.days;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day17Test {
    List<String> lines;
    Path path;

    @BeforeEach
    void setUp() {
        var instance = new Day17();
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
    void combinationsOfContainersToHoldEggNog() {
        var instance = new Day17();
        int expected = 4;

        var actual = instance.combinationsOfContainersToHoldEggNog(25, lines);
        assertEquals(expected, actual.first(), "The number of Egg Nog container combinations " +
                "is not what was expected: " + expected);
    }

    @Test
    void minimumNumberOfContainersToHoldEggNog() {
        var instance = new Day17();
        int expected = 3;
        int minExpected = 2;

        var actual = instance.combinationsOfContainersToHoldEggNog(25, lines);

        var min = actual.second().stream().map(List::size).min(Comparator.naturalOrder()).orElse(-1);
        assertEquals(minExpected, min, "The minimum number of containers needed did not meet expectations: " + minExpected);
        var minCount = actual.second().stream().filter(it -> it.size() == min).count();
        assertEquals(expected, minCount, "The number of Egg Nog container combinations " +
                "is not what was expected: " + expected);
    }
}