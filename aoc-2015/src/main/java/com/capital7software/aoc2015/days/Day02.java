package com.capital7software.aoc2015.days;

import com.capital7software.aoc2015.lib.AdventOfCodeSolution;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;

/**
 * --- Day 2: I Was Told There Would Be No Math ---
 * The elves are running low on wrapping paper, and so they need to submit an order for more. They have a list of
 * the dimensions (length l, width w, and height h) of each present, and only want to order exactly as much as
 * they need.
 * <p>
 * Fortunately, every present is a box (a perfect right rectangular prism), which makes calculating the required
 * wrapping paper for each gift a little easier: find the surface area of the box, which is 2*l*w + 2*w*h + 2*h*l.
 * The elves also need a little extra paper for each present: the area of the smallest side.
 * <p>
 * For example:
 * <p>
 * A present with dimensions 2x3x4 requires 2*6 + 2*12 + 2*8 = 52 square feet of wrapping paper plus 6 square
 * feet of slack, for a total of 58 square feet.
 * A present with dimensions 1x1x10 requires 2*1 + 2*10 + 2*10 = 42 square feet of wrapping paper plus 1
 * square foot of slack, for a total of 43 square feet.
 * <p>
 * Part1: All numbers in the elves' list are in feet. How many total square feet of wrapping paper should they order?
 * <p>
 *     Your puzzle answer was 1606483.
 * --- Part Two ---
 * The elves are also running low on ribbon. Ribbon is all the same width, so they only have to worry about
 * the length they need to order, which they would again like to be exact.
 * <p>
 * The ribbon required to wrap a present is the shortest distance around its sides, or the smallest perimeter of
 * any one face. Each present also requires a bow made out of ribbon as well; the feet of ribbon required for the
 * perfect bow is equal to the cubic feet of volume of the present. Don't ask how they tie the bow, though;
 * they'll never tell.
 * <p>
 * For example:
 * <p>
 * A present with dimensions 2x3x4 requires 2+2+3+3 = 10 feet of ribbon to wrap the present plus 2*3*4 = 24 feet
 * of ribbon for the bow, for a total of 34 feet.
 * A present with dimensions 1x1x10 requires 1+1+1+1 = 4 feet of ribbon to wrap the present plus 1*1*10 = 10 feet
 * of ribbon for the bow, for a total of 14 feet.
 * How many total feet of ribbon should they order?
 * <p>
 *     Your puzzle answer was 3842356.
 */
public class Day02 implements AdventOfCodeSolution {
    public record Present(long length, long width, long height) {
        public long calculatePaper() {
            var side1 = length * width;
            var side2 = width * height;
            var side3 = height * length;

            var minSide = Math.min(side1, Math.min(side2, side3));

            return 2 * side1 + 2 * side2 + 2 * side3 + minSide;
        }

        public long calculateRibbon() {
            var queue = new PriorityQueue<Long>();
            queue.offer(length);
            queue.offer(width);
            queue.offer(height);
            var area = length * width * height;
            var min1 = queue.poll();
            var min2 = queue.poll();

            assert min1 != null;
            assert min2 != null;

            return 2 * min1 + 2 * min2 + area;
        }

        public static List<Present> parse(List<String> lines) {
            return lines
                    .stream()
                    .map(Present::parse)
                    .filter(Objects::nonNull)
                    .toList();
        }

        public static Present parse(String line) {
            if (line == null || line.isBlank()) {
                return null;
            }

            var split = line.split("x");
            var length = Long.parseLong(split[0].trim());
            var width = Long.parseLong(split[1].trim());
            var height = Long.parseLong(split[2].trim());

            return new Present(length, width, height);
        }
    }

    private static final String defaultFilename = "inputs/input_day_02-01.txt";

    @Override
    public String getDefaultInputFilename() {
        return defaultFilename;
    }

    @Override
    public void runPart1(List<String> input) {
        var start = Instant.now();
        var presents = loadPresents(input);
        var total = howMuchTotalWrappingPaper(presents);
        var end = Instant.now();

        System.out.printf("The elves' need to order %d square feet of wrapping paper!%n", total);
        printTiming(start, end);
    }

    @Override
    public void runPart2(List<String> input) {
        var start = Instant.now();
        var presents = loadPresents(input);
        var total = howMuchTotalRibbon(presents);
        var end = Instant.now();

        System.out.printf("The elves' need to order %d square feet of ribbon!%n", total);
        printTiming(start, end);
    }

    public List<Present> loadPresents(List<String> input) {
        return Present.parse(input);
    }

    public long howMuchTotalWrappingPaper(List<Present> presents) {
        return presents
                .stream()
                .mapToLong(Present::calculatePaper)
                .sum();
    }

    public long howMuchTotalRibbon(List<Present> presents) {
        return presents
                .stream()
                .mapToLong(Present::calculateRibbon)
                .sum();
    }
}
