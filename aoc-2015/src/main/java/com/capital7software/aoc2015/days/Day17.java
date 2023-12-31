package com.capital7software.aoc2015.days;

import com.capital7software.aoc2015.lib.AdventOfCodeSolution;
import com.capital7software.aoc2015.lib.math.EggNog;
import com.capital7software.aoc2015.lib.util.Pair;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

/**
 * --- Day 17: No Such Thing as Too Much ---
 * The elves bought too much eggnog again - 150 liters this time. To fit it all into your refrigerator,
 * you'll need to move it into smaller containers. You take an inventory of the capacities of the available containers.
 * <p>
 * For example, suppose you have containers of size 20, 15, 10, 5, and 5 liters. If you need to store 25 liters,
 * there are four ways to do it:
 * <p>
 * 15 and 10
 * 20 and 5 (the first 5)
 * 20 and 5 (the second 5)
 * 15, 5, and 5
 * Filling all containers entirely, how many different combinations of containers can exactly fit
 * all 150 liters of eggnog?
 * <p>
 * Your puzzle answer was 1304.
 * <p>
 * --- Part Two ---
 * While playing with all the containers in the kitchen, another load of eggnog arrives!
 * The shipping and receiving department is requesting as many containers as you can spare.
 * <p>
 * Find the minimum number of containers that can exactly fit all 150 liters of eggnog.
 * How many different ways can you fill that number of containers and still hold exactly 150 litres?
 * <p>
 * In the example above, the minimum number of containers was two.
 * There were three ways to use that many containers, and so the answer there would be 3.
 * <p>
 * Your puzzle answer was 18.
 * <p>
 */
public class Day17 implements AdventOfCodeSolution {
    @Override
    public String getDefaultInputFilename() {
        return "inputs/input_day_17-01.txt";
    }

    @Override
    public void runPart1(List<String> input) {
        var start = Instant.now();
        var combinations = combinationsOfContainersToHoldEggNog(150, input);
        var end = Instant.now();
        System.out.printf("The number of the Sue that got me the gift is: %d%n", combinations.first());
        printTiming(start, end);
    }

    @Override
    public void runPart2(List<String> input) {
        var start = Instant.now();
        var combinations = combinationsOfContainersToHoldEggNog(150, input);
        var min = combinations.second().stream().map(List::size).min(Comparator.naturalOrder()).orElse(-1);
        var minCount = combinations.second().stream().filter(it -> it.size() == min).count();
        var end = Instant.now();
        System.out.printf("The minimum number of containers needed is %d and there are %d of those containers on hand%n", min, minCount);
        printTiming(start, end);
    }

    public Pair<Integer, List<List<Long>>> combinationsOfContainersToHoldEggNog(int liters, List<String> input) {
        var nog = EggNog.parse(liters, input);

        return nog.combinations();
    }
}
