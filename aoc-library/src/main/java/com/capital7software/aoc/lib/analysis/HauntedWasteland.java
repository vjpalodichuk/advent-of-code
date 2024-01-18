package com.capital7software.aoc.lib.analysis;

import com.capital7software.aoc.lib.collection.BinaryTreeNode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.logging.Logger;

/**
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
 *
 * @param steps The List of the Left and Right steps to make.
 * @param nodeMap The Map of nodes in this Haunted Wasteland.
 */
public record HauntedWasteland(
        @NotNull List<Character> steps,
        @NotNull Map<String, BinaryTreeNode<String>> nodeMap
) {
    private static final Logger LOGGER = Logger.getLogger(HauntedWasteland.class.getName());
    private static final String NODE_NAME_SPLIT = " = ";
    private static final String NODE_CHILDREN_SPLIT = ", ";
    private static final char LEFT = 'L';
    private static final char RIGHT = 'R';

    /**
     * Instantiates a new HauntedWasteland with the specified list of steps and nodes.
     *
     * @param steps The List of the Left and Right steps to make.
     * @param nodeMap The Map of nodes in this Haunted Wasteland.
     */
    public HauntedWasteland(
            @NotNull List<Character> steps,
            @NotNull Map<String, BinaryTreeNode<String>> nodeMap
    ) {
        this.steps = new ArrayList<>(steps);
        this.nodeMap = new HashMap<>(nodeMap);
    }

    /**
     * Returns an unmodifiable List of the steps to navigate this HauntedWasteland.
     *
     * @return An unmodifiable List of the steps to navigate this HauntedWasteland.
     */
    public @NotNull List<Character> steps() {
        return Collections.unmodifiableList(steps);
    }

    /**
     * Returns an unmodifiable Map of the nodes in this HauntedWasteland.
     *
     * @return An unmodifiable Map of the nodes in this HauntedWasteland.
     */
    public @NotNull Map<String, BinaryTreeNode<String>> nodeMap() {
        return Collections.unmodifiableMap(nodeMap);
    }

    /**
     * Builds and returns a new HauntedWasteland from the specified input.
     *
     * @param input The List of Strings that define the steps and nodes.
     * @return A new HauntedWasteland from the specified input.
     */
    public static @NotNull HauntedWasteland buildHauntedWasteland(@NotNull List<String> input) {
        var steps = new ArrayList<Character>();
        var nodeMap = new HashMap<String, BinaryTreeNode<String>>();
        var firstLine = new AtomicBoolean(true);

        input
                .forEach(line -> {
                    if (line != null && !line.isBlank()) {
                        if (firstLine.get()) {
                            steps.addAll(loadSteps(line));
                            firstLine.set(false);
                        } else {
                            loadNode(line, nodeMap);
                        }
                    }
                });

        return new HauntedWasteland(steps, nodeMap);
    }

    private static @NotNull List<Character> loadSteps(@NotNull String allSteps) {
        return allSteps.chars()
                .mapToObj(it -> (char) it)
                .toList();
    }

    private static void loadNode(@NotNull String stringNode, @NotNull Map<String, BinaryTreeNode<String>> nodeMap) {
        // parse the line
        var split = stringNode.split(NODE_NAME_SPLIT);

        var nodeName = split[0].trim();
        var childSplits = split[1]
                .replace("(", "")
                .replace(")", "")
                .split(NODE_CHILDREN_SPLIT);

        var leftChildName = childSplits[0].trim();
        var rightChildName = childSplits[1].trim();


        var leftNode = nodeMap.computeIfAbsent(leftChildName, BinaryTreeNode::new);
        var rightNode = nodeMap.computeIfAbsent(rightChildName, BinaryTreeNode::new);
        var currentNode = nodeMap.computeIfAbsent(nodeName, BinaryTreeNode::new);

        currentNode.setLeft(leftNode);
        currentNode.setRight(rightNode);
    }

    /**
     * Traverses the network of nodes and counts the number of steps to get to the end.
     *
     * @param startNode The ID of the starting node.
     * @param atEndNode The Predicate that returns true when at the end node.
     * @return The number of steps to get to the end.
     */
    public long traverse(@NotNull String startNode, @NotNull Predicate<String> atEndNode) {
        long stepCount = 0;

        LOGGER.fine("Traversing the network of Nodes starting at " + startNode +
                " Node");

        var currentNode = nodeMap.get(startNode);

        do {
            for (var nextStep : steps) {
                if (currentNode == null || atEndNode.test(currentNode.getValue())) {
                    break;
                }
                currentNode = takeStep(nextStep, currentNode);
                stepCount++;
            }
        } while (currentNode != null && !atEndNode.test(currentNode.getValue()));

        if (currentNode != null) {
            LOGGER.fine("Traversed the network of nodes starting at " +
                    startNode + " and ended at " + currentNode.getValue() + " in " + stepCount + " steps!");
        } else {
            LOGGER.warning("Something went wrong after taking " + stepCount +
                    " steps when starting at " + startNode + " as we didn't end at the correct node!");
        }
        return stepCount;
    }

    private @NotNull BinaryTreeNode<String> takeStep(char nextStep, @NotNull BinaryTreeNode<String> currentNode) {
        if (nextStep == LEFT) {
            return currentNode.getLeft();
        } else if (nextStep == RIGHT) {
            return currentNode.getRight();
        } else {
            throw new RuntimeException("Unable to take the next step due to an unknown Direction: " + nextStep);
        }
    }

}
