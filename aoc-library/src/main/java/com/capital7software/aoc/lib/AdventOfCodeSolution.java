package com.capital7software.aoc.lib;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

/**
 * Interface that the AdventOfCodeRunner expects the Day classes
 * to implement.
 */
public interface AdventOfCodeSolution {
    /**
     * The default input filename for the Day.
     *
     * @return The input filename for the Day.
     */
    String getDefaultInputFilename();

    /**
     * Runs the solution for Part 1 of the problem.
     *
     * @param input The contents of the input file.
     */
    void runPart1(List<String> input);

    /**
     * Runs the solution for Part 2 of the problem.
     *
     * @param input The contents of the input file.
     */
    default void runPart2(List<String> input) {
    }

    /**
     * A helper method for printing out the timing of the solutions.
     *
     * @param start The Instant the solution was started.
     * @param end The Instant the solution finished.
     */
    default void printTiming(Instant start, Instant end) {
        System.out.printf("Calculated in %d ns%n", Duration.between(start, end).toNanos());
    }
}
