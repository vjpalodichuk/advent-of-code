package com.capital7software.aoc2015.days;

import com.capital7software.aoc.aoc2015.days.Day04;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day04Test {
    List<String> lines;
    Path path;

    @BeforeEach
    void setUp() {
        var instance = new Day04();
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            var url = classloader.getResource(instance.getDefaultInputFilename());
            assert url != null;
            path = Paths.get(url.toURI());
            lines = Files.readAllLines(path);
        } catch (URISyntaxException | IOException e) {
            System.out.printf("Unable to load input data from: %s%n", path);
            throw new RuntimeException(e);
        }
    }

    @Test
    void getLowestPositiveNumberWithFiveLeadingZerosMD5Hash() {
        var instance = new Day04();
        var expected = List.of(609043L, 1048970L);
        for (int i = 0; i < lines.size(); i++) {
            var actual = instance.lowestPositiveNumber(lines.get(i), 5);

            assertEquals(expected.get(i), actual);
        }
    }

    @Test
    void getLowestPositiveNumberWithSixLeadingZerosMD5Hash() {
        var instance = new Day04();
        var expected = List.of(6742839L, 5714438L);
        for (int i = 0; i < lines.size(); i++) {
            var actual = instance.lowestPositiveNumber(lines.get(i), 6);

            assertEquals(expected.get(i), actual);
        }
    }
}