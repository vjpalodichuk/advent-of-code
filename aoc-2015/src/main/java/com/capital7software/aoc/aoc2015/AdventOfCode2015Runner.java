package com.capital7software.aoc.aoc2015;

import com.capital7software.aoc.lib.AdventOfCodeRunner;

/**
 * The Advent of Code 2015 Runner. Pass the name of the Solution class
 * to run.
 * <p><br>
 * For example:<br>
 * Day01 to run the Day01 solution.
 */
public class AdventOfCode2015Runner extends AdventOfCodeRunner {
    /**
     * Instantiates a new and empty instance.
     */
    public AdventOfCode2015Runner() {

    }

    /**
     * The main method called by the JVM. You must specify a AdventOfCodeSolution
     * derived class from the days package.
     *
     * @param args The command-line arguments. The first parameter must be a AdventOfCodeSolution class.
     */
    public static void main(String[] args) {
        var packageName = "com.capital7software.aoc.aoc2015.days.";

        runSolution(packageName, args);
    }
}
