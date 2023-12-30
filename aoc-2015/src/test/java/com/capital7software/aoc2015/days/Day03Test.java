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

class Day03Test {
    List<String> lines;
    Path path;

    @BeforeEach
    void setUp() {
        var instance = new Day03();
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
    void getUniqueHousesVisited() {
        var instance = new Day03();
        var expected = List.of(2L, 4L, 2L);
        for (int i = 0; i < lines.size(); i++) {
            var routeId = instance.deliverGifts(lines.get(i));
            var actual = instance.getUniqueHouseCount(routeId);

            assertEquals(expected.get(i), actual);
        }
    }

    @Test
    void getUniqueHousesVisitedWithRoboSanta() {
        var instance = new Day03();
        var expected = List.of(3L, 3L, 11L);
        for (int i = 0; i < lines.size(); i++) {
            var routeId = instance.deliverGiftsWithRoboSanta(lines.get(i));
            var actual = instance.getUniqueHouseCount(routeId);

            assertEquals(expected.get(i), actual);
        }
    }
}