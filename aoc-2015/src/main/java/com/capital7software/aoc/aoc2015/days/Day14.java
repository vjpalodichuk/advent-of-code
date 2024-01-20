package com.capital7software.aoc.aoc2015.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.graph.parser.Day14Parser;
import com.capital7software.aoc.lib.math.ReindeerOlympics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

/**
 * --- Day 14: Reindeer Olympics ---<br><br>
 * This year is the Reindeer Olympics! Reindeer can fly at high speeds, but must rest occasionally
 * to recover their energy. Santa would like to know which of his reindeer is fastest, and so he has them race.
 * <p><br>
 * Reindeer can only either be flying (always at their top speed) or resting (not moving at all),
 * and always spend whole seconds in either state.
 * <p><br>
 * For example, suppose you have the following Reindeer:
 * <ul>
 *     <li>
 *         Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.
 *     </li>
 *     <li>
 *         Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds.
 *     </li>
 *     <li>
 *         After one second, Comet has gone 14 km, while Dancer has gone 16 km.
 *     </li>
 *     <li>
 *         After ten seconds, Comet has gone 140 km, while Dancer has gone 160 km.
 *     </li>
 *     <li>
 *         On the eleventh second, Comet begins resting (staying at 140 km), and Dancer
 *         continues on for a total distance of 176 km.
 *     </li>
 *     <li>
 *         On the 12th second, both reindeer are resting.
 *     </li>
 *     <li>
 *         They continue to rest until the 138th second, when Comet flies for another ten seconds.
 *     </li>
 *     <li>
 *         On the 174th second, Dancer flies for another 11 seconds.
 *     </li>
 * </ul>
 * In this example, after the 1000th second, both reindeer are resting, and Comet is in the lead at 1120
 * km (poor Dancer has only gotten 1056 km by that point). So, in this situation, Comet would win
 * (if the race ended at 1000 seconds).
 * <p><br>
 * Given the descriptions of each reindeer (in your puzzle input), after exactly 2503 seconds,
 * what distance has the winning reindeer traveled?
 * <p><br>
 * Your puzzle answer was 2660.
 * <p><br>
 * --- Part Two ---<br><br>
 * Seeing how reindeer move in bursts, Santa decides he's not pleased with the old scoring system.
 * <p><br>
 * Instead, at the end of each second, he awards one point to the reindeer currently in the lead.
 * (If there are multiple reindeer tied for the lead, they each get one point.)
 * He keeps the traditional 2,503-second time limit, of course, as doing otherwise would be entirely ridiculous.
 * <p><br>
 * Given the example reindeer from above, after the first second, Dancer is in the lead and gets one point.
 * He stays in the lead until several seconds into Comet's second burst: after the 140th second,
 * Comet pulls into the lead and gets his first point. Of course, since Dancer had been in the
 * lead for the 139 seconds before that, he has accumulated 139 points by the 140th second.
 * <p><br>
 * After the 1000th second, Dancer has accumulated 689 points, while poor Comet, our old champion,
 * only has 312. So, with the new scoring system, Dancer would win (if the race ended at 1000 seconds).
 * <p><br>
 * Again given the descriptions of each reindeer (in your puzzle input), after exactly 2503 seconds,
 * how many points does the winning reindeer have?
 * <p><br>
 * Your puzzle answer was 1256.
 */
public class Day14 implements AdventOfCodeSolution {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day14.class);

    /**
     * Instantiates the solution instance.
     */
    public Day14() {

    }

    @Override
    public String getDefaultInputFilename() {
        return "inputs/input_day_14-01.txt";
    }

    @Override
    public void runPart1(List<String> input) {
        var start = Instant.now();
        var max = whatDistanceHasTheWinnerTraveled(input, 2_503);
        var end = Instant.now();
        LOGGER.info("The distance traveled by the winning reindeer at 2,503 seconds is: {}", max);
        logTimings(LOGGER, start, end);
    }

    @Override
    public void runPart2(List<String> input) {
        var start = Instant.now();
        var max = howManyPointsDoesTheWinnerHave(input, 2_503);
        var end = Instant.now();
        LOGGER.info("The total points by the winning reindeer after 2,503 seconds is: {}", max);
        logTimings(LOGGER, start, end);
    }

    /**
     * Uses a Graph and the ReindeerOlympics to calculate how many points the
     * winning reindeer has after the specified time has elapsed in the race.
     *
     * @param input The horses and their stats, which is the Graph input.
     * @param time  The amount of time to run the race for.
     * @return The number of points the winning reindeer has after the specified time.
     */
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

    /**
     * Uses a Graph and the ReindeerOlympics to calculate how far the
     * winning reindeer traveled has after the specified time has elapsed in the race.
     *
     * @param input The horses and their stats, which is the Graph input.
     * @param time  The amount of time to run the race for.
     * @return The distance the winning reindeer has traveled after the specified time.
     */
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
