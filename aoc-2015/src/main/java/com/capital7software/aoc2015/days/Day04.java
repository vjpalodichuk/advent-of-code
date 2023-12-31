package com.capital7software.aoc2015.days;

import com.capital7software.aoc2015.lib.AdventOfCodeSolution;
import com.capital7software.aoc2015.lib.crypt.MD5Fun;

import java.time.Instant;
import java.util.List;

/**
 * --- Day 4: The Ideal Stocking Stuffer ---
 * Santa needs help mining some AdventCoins (very similar to bitcoins) to use as gifts for all the economically
 * forward-thinking little girls and boys.
 * <p>
 * To do this, he needs to find MD5 hashes which, in hexadecimal, start with at least five zeroes.
 * The input to the MD5 hash is some secret key (your puzzle input, given below) followed by a number in decimal.
 * To mine AdventCoins, you must find Santa the lowest positive number (no leading zeroes: 1, 2, 3, ...)
 * that produces such a hash.
 * <p>
 * For example:
 * <p>
 * If your secret key is abcdef, the answer is 609043, because the MD5 hash of abcdef609043 starts with five
 * zeroes (000001dbbfa...), and it is the lowest such number to do so.
 * If your secret key is pqrstuv, the lowest number it combines with to make an MD5 hash starting with five
 * zeroes is 1048970; that is, the MD5 hash of pqrstuv1048970 looks like 000006136ef....
 * Your puzzle input is yzbqklnj.
 * <p>
 * Your puzzle answer was 282749.
 * --- Part Two ---
 * Now find one that starts with six zeroes.
 * <p>
 * Your puzzle answer was 9962624.
 */
public class Day04 implements AdventOfCodeSolution {
    private static final String defaultFilename = "inputs/input_day_04-01.txt";

    @Override
    public String getDefaultInputFilename() {
        return defaultFilename;
    }

    @Override
    public void runPart1(List<String> input) {

        for (var secret : input) {
            var start = Instant.now();
            var total = lowestPositiveNumber(secret, 5);
            var end = Instant.now();

            System.out.printf(
                    "Lowest positive number to concatenate with %s to produce a MD5 hash with 5 leading zeros is: %d%n",
                    secret, total);
            printTiming(start, end);
        }
    }

    @Override
    public void runPart2(List<String> input) {

        for (var secret : input) {
            var start = Instant.now();
            var total = lowestPositiveNumber(secret, 6);
            var end = Instant.now();

            System.out.printf(
                    "Lowest positive number to concatenate with %s to produce a MD5 hash with 6 leading zeros is: %d%n",
                    secret, total);
            printTiming(start, end);
        }
    }

    public long lowestPositiveNumber(String secret, int leadingZeros) {
        return MD5Fun.lowestPositiveNumberWithLeadingZeros(secret, leadingZeros);
    }
}
