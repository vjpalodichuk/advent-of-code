package com.capital7software.aoc.aoc2023.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.grid.ClumsyCrucible;

import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * --- Day 17: Clumsy Crucible ---<br><br>
 * The lava starts flowing rapidly once the Lava Production Facility is operational. As you leave, the
 * reindeer offers you a parachute, allowing you to quickly reach Gear Island.
 * <p><br>
 * As you descend, your bird's-eye view of Gear Island reveals why you had trouble finding anyone on
 * your way up: half of Gear Island is empty, but the half below you is a giant factory city!
 * <p><br>
 * You land near the gradually-filling pool of lava at the base of your new lavafall. Lavaducts will
 * eventually carry the lava throughout the city, but to make use of it immediately, Elves are loading
 * it into large crucibles on wheels.
 * <p><br>
 * The crucibles are top-heavy and pushed by hand. Unfortunately, the crucibles become very difficult
 * to steer at high speeds, and so it can be hard to go in a straight line for very long.
 * <p><br>
 * To get Desert Island the machine parts it needs as soon as possible, you'll need to find the best
 * way to get the crucible from the lava pool to the machine parts factory. To do this, you need to
 * minimize heat loss while choosing a route that doesn't require the crucible to go in a straight
 * line for too long.
 * <p><br>
 * Fortunately, the Elves here have a map (your puzzle input) that uses traffic patterns, ambient
 * temperature, and hundreds of other parameters to calculate exactly how much heat loss can be
 * expected for a crucible entering any particular city block.
 * <p><br>
 * For example:
 * <p><br>
 * <code>
 * 2413432311323<br>
 * 3215453535623<br>
 * 3255245654254<br>
 * 3446585845452<br>
 * 4546657867536<br>
 * 1438598798454<br>
 * 4457876987766<br>
 * 3637877979653<br>
 * 4654967986887<br>
 * 4564679986453<br>
 * 1224686865563<br>
 * 2546548887735<br>
 * 4322674655533<br>
 * </code>
 * <p><br>
 * Each city block is marked by a single digit that represents the amount of heat loss if the
 * crucible enters that block. The starting point, the lava pool, is the top-left city block;
 * the destination, the machine parts factory, is the bottom-right city block. (Because you already
 * start in the top-left block, you don't incur that block's heat loss unless you leave that block
 * and then return to it.)
 * <p><br>
 * Because it is difficult to keep the top-heavy crucible going in a straight line for very long,
 * it can move at most three blocks in a single direction before it must turn 90 degrees left or
 * right. The crucible also can't reverse direction; after entering each city block, it may only
 * turn left, continue straight, or turn right.
 * <p><br>
 * One way to minimize heat loss is this path:
 * <p><br>
 * <code>
 * 2&#62;&#62;34^&#62;&#62;&#62;1323<br>
 * 32v&#62;&#62;&#62;35v5623<br>
 * 32552456v&#62;&#62;54<br>
 * 3446585845v52<br>
 * 4546657867v&#62;6<br>
 * 14385987984v4<br>
 * 44578769877v6<br>
 * 36378779796v><br>
 * 465496798688v<br>
 * 456467998645v<br>
 * 12246868655&#60;v<br>
 * 25465488877v5<br>
 * 43226746555v&#62;<br>
 * </code>
 * <p><br>
 * This path never moves more than three consecutive blocks in the same direction and incurs a
 * heat loss of only 102.
 * <p><br>
 * Directing the crucible from the lava pool to the machine parts factory, but not moving more
 * than three consecutive blocks in the same direction, what is the least heat loss it can incur?
 * <p><br>
 * Your puzzle answer was 956.
 * <p><br>
 * --- Part Two ---<br><br>
 * The crucibles of lava simply aren't large enough to provide an adequate supply of lava to the
 * machine parts factory. Instead, the Elves are going to upgrade to ultra crucibles.
 * <p><br>
 * Ultra crucibles are even more difficult to steer than normal crucibles. Not only do they have
 * trouble going in a straight line, but they also have trouble turning!
 * <p><br>
 * Once an ultra crucible starts moving in a direction, it needs to move a minimum of four blocks
 * in that direction before it can turn (or even before it can stop at the end). However, it will
 * eventually start to get wobbly: an ultra crucible can move a maximum of ten consecutive blocks
 * without turning.
 * <p><br>
 * In the above example, an ultra crucible could follow this path to minimize heat loss:
 * <p><br>
 * <code>
 * 2&#62;&#62;&#62;&#62;&#62;&#62;&#62;&#62;1323<br>
 * 32154535v5623<br>
 * 32552456v4254<br>
 * 34465858v5452<br>
 * 45466578v&#62;&#62;&#62;&#62;<br>
 * 143859879845v<br>
 * 445787698776v<br>
 * 363787797965v<br>
 * 465496798688v<br>
 * 456467998645v<br>
 * 122468686556v<br>
 * 254654888773v<br>
 * 432267465553v<br>
 * </code>
 * <p><br>
 * In the above example, an ultra crucible would incur the minimum possible heat loss of 94.
 * <p><br>
 * Here's another example:
 * <p><br>
 * <code>
 * 111111111111<br>
 * 999999999991<br>
 * 999999999991<br>
 * 999999999991<br>
 * 999999999991<br>
 * </code>
 * <p><br>
 * Sadly, an ultra crucible would need to take an unfortunate path like this one:
 * <p><br>
 * <code>
 * 1&#62;&#62;&#62;&#62;&#62;&#62;&#62;1111<br>
 * 9999999v9991<br>
 * 9999999v9991<br>
 * 9999999v9991<br>
 * 9999999v&#62;&#62;&#62;&#62;<br>
 * </code>
 * <p><br>
 * This route causes the ultra crucible to incur the minimum possible heat loss of 71.
 * <p><br>
 * Directing the ultra crucible from the lava pool to the machine parts factory, what is the
 * least heat loss it can incur?
 * <p><br>
 * Your puzzle answer was 1106.
 */
public class Day17 implements AdventOfCodeSolution {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day17.class);

    /**
     * Instantiates this Solution instance.
     */
    public Day17() {

    }

    @Override
    public String getDefaultInputFilename() {
        return "inputs/input_day_17-01.txt";
    }

    @Override
    public void runPart1(List<String> input) {
        var start = Instant.now();
        var answer = calculateMinimumHeatLoss(input, 1, 3);
        var end = Instant.now();

        LOGGER.info("The minimum heat loss with minSteps 1 and maxSteps 2 is: {}", answer);
        logTimings(LOGGER, start, end);
    }

    @Override
    public void runPart2(List<String> input) {
        var start = Instant.now();
        var answer = calculateMinimumHeatLoss(input, 4, 10);
        var end = Instant.now();

        LOGGER.info("The minimum heat loss with minSteps 4 and maxSteps 10 is: {}", answer);
        logTimings(LOGGER, start, end);
    }

    /**
     * Calculates and returns the heat loss of the path with the minimum heat loss.
     * @param input The List of Strings to parse that contains the map of the grid.
     * @param minSteps The number of steps in the same direction the crucible must travel before it can turn.
     * @param maxSteps The number of steps in the same direction the crucible can travel before it must turn.
     * @return The heat loss of the path with the minimum heat loss.
     */
    public long calculateMinimumHeatLoss(List<String> input, int minSteps, int maxSteps) {
        var crucible = ClumsyCrucible.buildClumsyCrucible(input, minSteps, maxSteps);
        return crucible.calculateMinimumHeatLoss();
    }
}
