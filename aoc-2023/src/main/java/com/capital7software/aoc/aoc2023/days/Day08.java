package com.capital7software.aoc.aoc2023.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.analysis.HauntedWasteland;
import com.capital7software.aoc.lib.math.MathOperations;

import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

/**
 * --- Day 8: Haunted Wasteland ---<br><br>
 * You're still riding a camel across Desert Island when you spot a sandstorm quickly approaching.
 * When you turn to warn the Elf, she disappears before your eyes! To be fair, she had just finished
 * warning you about ghosts a few minutes ago.
 * <p><br>
 * One of the camel's pouches is labeled "maps" - sure enough, it's full of documents
 * (your puzzle input) about how to navigate the desert. At least, you're pretty sure that's
 * what they are; one of the documents contains a list of left/right instructions, and the rest
 * of the documents seem to describe some kind of network of labeled nodes.
 * <p><br>
 * It seems like you're meant to use the left/right instructions to navigate the network.
 * Perhaps if you have the camel follow the same instructions, you can escape the haunted wasteland!
 * <p><br>
 * After examining the maps for a bit, two nodes stick out: AAA and ZZZ. You feel like AAA
 * is where you are now, and you have to follow the left/right instructions until you reach ZZZ.
 * <p><br>
 * This format defines each node of the network individually. <br><br>For example:<br><br>
 * <code>
 * RL<br>
 * <br>
 * AAA = (BBB, CCC)<br>
 * BBB = (DDD, EEE)<br>
 * CCC = (ZZZ, GGG)<br>
 * DDD = (DDD, DDD)<br>
 * EEE = (EEE, EEE)<br>
 * GGG = (GGG, GGG)<br>
 * ZZZ = (ZZZ, ZZZ)<br>
 * </code>
 * <p><br>
 * Starting with AAA, you need to look up the next element based on the next left/right
 * instruction in your input. In this example, start with AAA and go right (R) by choosing
 * the right element of AAA, CCC. Then, L means to choose the left element of CCC, ZZZ.
 * By following the left/right instructions, you reach ZZZ in 2 steps.
 * <p><br>
 * Of course, you might not find ZZZ right away. If you run out of left/right instructions,
 * repeat the whole sequence of instructions as necessary: RL really means RLRLRLRLRLRLRLRL...
 * and so on. <br><br>For example, here is a situation that takes 6 steps to reach ZZZ:<br><br>
 * <code>
 * LLR<br>
 * <br>
 * AAA = (BBB, BBB)<br>
 * BBB = (AAA, ZZZ)<br>
 * ZZZ = (ZZZ, ZZZ)<br>
 * </code>
 * <p><br>
 * Starting at AAA, follow the left/right instructions. How many steps are required to reach ZZZ?
 * <p><br>
 * Your puzzle answer was 13939.
 * <p><br>
 * --- Part Two ---<br><br>
 * The sandstorm is upon you, and you aren't any closer to escaping the wasteland.
 * You had the camel follow the instructions, but you've barely left your starting position.
 * It's going to take significantly more steps to escape!
 * <p><br>
 * What if the map isn't for people - what if the map is for ghosts? Are ghosts even bound
 * by the laws of spacetime? Only one way to find out.
 * <p><br>
 * After examining the maps a bit longer, your attention is drawn to a curious fact:
 * the number of nodes with names ending in A is equal to the number ending in Z!
 * If you were a ghost, you'd probably just start at every node that ends with A and
 * follow all the paths at the same time until they all simultaneously end up at nodes that end with Z.
 * <p><br>
 * For example:<br><br>
 * <code>
 * LR<br>
 * <br>
 * 11A = (11B, XXX)<br>
 * 11B = (XXX, 11Z)<br>
 * 11Z = (11B, XXX)<br>
 * 22A = (22B, XXX)<br>
 * 22B = (22C, 22C)<br>
 * 22C = (22Z, 22Z)<br>
 * 22Z = (22B, 22B)<br>
 * XXX = (XXX, XXX)<br>
 * </code>
 * <p><br>
 * Here, there are two starting nodes, 11A and 22A (because they both end with A).
 * As you follow each left/right instruction, use that instruction to simultaneously
 * navigate away from both nodes you're currently on. Repeat this process until all
 * the nodes you're currently on end with Z. (If only some of the nodes you're on end
 * with Z, they act like any other node and you continue as normal.) In this example,
 * you would proceed as follows:
 * <p><br>
 * Step 0: You are at 11A and 22A.<br>
 * Step 1: You choose all the left paths, leading you to 11B and 22B.<br>
 * Step 2: You choose all the right paths, leading you to 11Z and 22C.<br>
 * Step 3: You choose all the left paths, leading you to 11B and 22Z.<br>
 * Step 4: You choose all the right paths, leading you to 11Z and 22B.<br>
 * Step 5: You choose all the left paths, leading you to 11B and 22C.<br>
 * Step 6: You choose all the right paths, leading you to 11Z and 22Z.<br>
 * <p><br>
 * So, in this example, you end up entirely on nodes that end in Z after 6 steps.
 * <p><br>
 * Simultaneously start on every node that ends with A. How many steps does it take
 * before you're only on nodes that end with Z?
 * <p><br>
 * Your puzzle answer was 8906539031197.
 */
public class Day08 implements AdventOfCodeSolution {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day08.class);

    /**
     * Instantiates this Solution instance.
     */
    public Day08() {

    }

    @Override
    public String getDefaultInputFilename() {
        return "inputs/input_day_08-01.txt";
    }

    @Override
    public void runPart1(List<String> input) {
        var start = Instant.now();
        var answer = calculateStepsToTraverseWastelandSingleNode(input);
        var end = Instant.now();

        LOGGER.info("The total number of steps taken to traverse the Wasteland is: {}", answer);
        logTimings(LOGGER, start, end);
    }

    @Override
    public void runPart2(List<String> input) {
        var start = Instant.now();
        var answer = calculateStepsToTraverseWastelandMultipleNodes(input);
        var end = Instant.now();

        LOGGER.info("The total number of steps to traverse the Wasteland is: {}", answer);
        logTimings(LOGGER, start, end);
    }

    /**
     * Calculates and returns the number of steps to traverse the network from a single starting node to
     * a single destination node.
     *
     * @param input The List of Strings that contain the nodes to parse.
     * @return The number of steps to traverse the network from a single starting node to
     * a single destination node.
     */
    public long calculateStepsToTraverseWastelandSingleNode(List<String> input) {
        var network = HauntedWasteland.buildHauntedWasteland(input);
        return network.traverse("AAA", "ZZZ"::equals);
    }

    /**
     * Calculates and returns the number of steps to traverse the network from multiple starting nodes to
     * multiple destination nodes.
     *
     * @param input The List of Strings that contain the nodes to parse.
     * @return The number of steps to traverse the network from multiple starting nodes to
     * multiple destination nodes.
     */
    public long calculateStepsToTraverseWastelandMultipleNodes(List<String> input) {
        var network = HauntedWasteland.buildHauntedWasteland(input);
        var startingNodes = network
                .nodeMap()
                .keySet()
                .stream()
                .filter(it -> it.endsWith("A"))
                .collect(Collectors.toSet());
        var endingNodes = network
                .nodeMap()
                .keySet()
                .stream()
                .filter(it -> it.endsWith("Z"))
                .collect(Collectors.toSet());
        var stepsToOneEnd = startingNodes.stream()
                .map(startNode -> network.traverse(startNode, endingNodes::contains))
                .collect(Collectors.toSet());

        return stepsToOneEnd.stream().reduce(MathOperations::lcm).orElse(0L);
    }
}
