package com.capital7software.aoc2023.days;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;

public class Day23 {
    private static final String inputFilename = "inputs/input_day_23-02.txt";

    public static void main(String[] args) throws URISyntaxException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        var url = classloader.getResource(inputFilename);
        assert url != null;
        var path = Paths.get(url.toURI());

        part1(path);
        part2(path);
    }

    private static void part1(Path path) {
        try (var stream = Files.lines(path)) {
            // Part 1
            System.out.println("Part 1: Start!");
            var start = Instant.now();
            var disintegratedCount = 0;
            var end = Instant.now();
            System.out.println("Total number of bricks that can be safely disintegrated: "
                    + disintegratedCount + " in " + Duration.between(start, end).toNanos() + " ns");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void part2(Path path) {
        try (var stream = Files.lines(path)) {
            // Part 2
            System.out.println("Part 2: Start!");
            var start = Instant.now();
            var chainReactionCount = 0;
            var end = Instant.now();
            System.out.println("Total number of bricks that will fall in a chain reaction: "
                    + chainReactionCount + " in " + Duration.between(start, end).toNanos() + " ns");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
