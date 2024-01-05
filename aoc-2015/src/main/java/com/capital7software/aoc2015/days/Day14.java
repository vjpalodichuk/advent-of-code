package com.capital7software.aoc2015.days;

import com.capital7software.aoc2015.lib.AdventOfCodeSolution;
import com.capital7software.aoc2015.lib.math.ReindeerOlympics;
import com.capital7software.aoc2015.lib.graph.parser.Day14Parser;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

/**
 * --- Day 14: Reindeer Olympics ---
 * This year is the Reindeer Olympics! Reindeer can fly at high speeds, but must rest occasionally
 * to recover their energy. Santa would like to know which of his reindeer is fastest, and so he has them race.
 * <p>
 * Reindeer can only either be flying (always at their top speed) or resting (not moving at all),
 * and always spend whole seconds in either state.
 * <p>
 * For example, suppose you have the following Reindeer:
 * <p>
 * Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.
 * Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds.
 * After one second, Comet has gone 14 km, while Dancer has gone 16 km. After ten seconds, Comet has
 * gone 140 km, while Dancer has gone 160 km. On the eleventh second, Comet begins resting (staying at 140 km),
 * and Dancer continues on for a total distance of 176 km. On the 12th second, both reindeer are resting.
 * They continue to rest until the 138th second, when Comet flies for another ten seconds. On the 174th second,
 * Dancer flies for another 11 seconds.
 * <p>
 * In this example, after the 1000th second, both reindeer are resting, and Comet is in the lead at 1120
 * km (poor Dancer has only gotten 1056 km by that point). So, in this situation, Comet would win
 * (if the race ended at 1000 seconds).
 * <p>
 * Given the descriptions of each reindeer (in your puzzle input), after exactly 2503 seconds,
 * what distance has the winning reindeer traveled?
 * <p>
 * Your puzzle answer was 2660.
 * <p>
 * --- Part Two ---
 * Seeing how reindeer move in bursts, Santa decides he's not pleased with the old scoring system.
 * <p>
 * Instead, at the end of each second, he awards one point to the reindeer currently in the lead.
 * (If there are multiple reindeer tied for the lead, they each get one point.)
 * He keeps the traditional 2503 second time limit, of course, as doing otherwise would be entirely ridiculous.
 * <p>
 * Given the example reindeer from above, after the first second, Dancer is in the lead and gets one point.
 * He stays in the lead until several seconds into Comet's second burst: after the 140th second,
 * Comet pulls into the lead and gets his first point. Of course, since Dancer had been in the
 * lead for the 139 seconds before that, he has accumulated 139 points by the 140th second.
 * <p>
 * After the 1000th second, Dancer has accumulated 689 points, while poor Comet, our old champion,
 * only has 312. So, with the new scoring system, Dancer would win (if the race ended at 1000 seconds).
 * <p>
 * Again given the descriptions of each reindeer (in your puzzle input), after exactly 2503 seconds,
 * how many points does the winning reindeer have?
 * <p>
 * Your puzzle answer was 1256.
 * <p>
 */
public class Day14 implements AdventOfCodeSolution {
    @Override
    public String getDefaultInputFilename() {
        return "inputs/input_day_14-01.txt";
    }

    @Override
    public void runPart1(List<String> input) {
        var start = Instant.now();
        var max = whatDistanceHasTheWinnerTraveled(input, 2_503);
        var end = Instant.now();
        System.out.printf("The distance traveled by the winning reindeer at 2,503 seconds is: %s%n", max);
        printTiming(start, end);
    }

    @Override
    public void runPart2(List<String> input) {
        var start = Instant.now();
        var max = howManyPointsDoesTheWinnerHave(input, 2_503);
        var end = Instant.now();
        System.out.printf("The total points by the winning reindeer after 2,503 seconds is: %s%n", max);
        printTiming(start, end);
    }

    public double howManyPointsDoesTheWinnerHave(List<String> input, long time) {
        var optional = new Day14Parser().parse(input, "day14");

        if (optional.isEmpty()) {
            throw new RuntimeException("A valid Graph is required! " + optional);
        }

        var graph = optional.get();

        var race = new ReindeerOlympics(graph);
        var results = race.runRace(time);

        return results.values().stream().max(Comparator.naturalOrder()).orElse(0.0);
    }

    public double whatDistanceHasTheWinnerTraveled(List<String> input, long time) {
        var optional = new Day14Parser().parse(input, "day14");

        if (optional.isEmpty()) {
            throw new RuntimeException("A valid Graph is required! " + optional);
        }

        var graph = optional.get();

        return graph
                .getVertices()
                .stream()
                .filter(it -> it.getValue().isPresent())
                .mapToDouble(it -> it.getValue().get().distance(time))
                .max()
                .orElse(0);
    }
}
