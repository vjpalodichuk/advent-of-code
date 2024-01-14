package com.capital7software.aoc.lib;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public interface AdventOfCodeSolution {
    String getDefaultInputFilename();

    void runPart1(List<String> input);

    default void runPart2(List<String> input) {
    }

    default void printTiming(Instant start, Instant end) {
        System.out.printf("Calculated in %d ns%n", Duration.between(start, end).toNanos());
    }
}
