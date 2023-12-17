package com.capital7software.aoc2023.days;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day08 {

    private static class Node {
        private final String name;

        private Node left;
        private Node right;

        public Node(String name, Node left, Node right) {
            this.name = name;
            this.left = left;
            this.right = right;
        }

        public Node(String name) {
            this(name, null, null);
        }

        public String getName() {
            return name;
        }

        public Node getLeft() {
            return left;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public Node getRight() {
            return right;
        }

        public void setRight(Node right) {
            this.right = right;
        }

        @Override
        public String toString() {
            String leftName = null;
            String rightName = null;

            if (left != null) {
                leftName = left.getName();
            }

            if (right != null) {
                rightName = right.getName();
            }

            return "Node{" +
                    "name='" + name + '\'' +
                    ", leftName=" + leftName +
                    ", rightName=" + rightName +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return getName().equals(node.getName());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getName());
        }
    }

    private enum Direction {
        LEFT('L'),
        RIGHT('R');

        private final char label;

        Direction(char label) {
            this.label = label;
        }

        public char getLabel() {
            return label;
        }

        public static Direction fromLabel(char label) {
            for (var value : values()) {
                if (value.getLabel() == label) {
                    return value;
                }
            }

            return null;
        }

        @Override
        public String toString() {
            return "Direction{" +
                    "name=" + name() +
                    ", label=" + label +
                    '}';
        }
    }

    private record Network(List<Direction> steps, Map<String, Node> nodeMap) {

        public static Network buildNetwork(Stream<String> stepsAndNodes) {
                var steps = new ArrayList<Direction>();
                var nodeMap = new HashMap<String, Node>();
                var firstLine = new AtomicBoolean(true);

                stepsAndNodes
                        .forEach(line -> {
                            if (firstLine.get()) {
                                steps.addAll(loadSteps(line));
                                firstLine.set(false);
                            } else {
                                if (line != null && !line.isBlank()) {
                                    loadNode(line, nodeMap);
                                }
                            }
                        });

                return new Network(steps, nodeMap);
            }

            public static List<Direction> loadSteps(String allSteps) {
                if (allSteps == null || allSteps.isBlank()) {
                    throw new RuntimeException("allSteps is either null or blank! A valid string with only Ls and/or Rs is required!!");
                }

                return allSteps.chars()
                        .mapToObj(it -> (char) it)
                        .map(Direction::fromLabel)
                        .toList();
            }

            public static void loadNode(String stringNode, Map<String, Node> nodeMap) {
                // parse the line
                var split = stringNode.split(NODE_NAME_SPLIT);

                var nodeName = split[0].trim();
                var childSplits = split[1].replace("(", "").replace(")", "").split(NODE_CHILDREN_SPLIT);

                var leftChildName = childSplits[0].trim();
                var rightChildName = childSplits[1].trim();


                var leftNode = nodeMap.computeIfAbsent(leftChildName, Node::new);
                var rightNode = nodeMap.computeIfAbsent(rightChildName, Node::new);
                var currentNode = nodeMap.computeIfAbsent(nodeName, Node::new);

                currentNode.setLeft(leftNode);
                currentNode.setRight(rightNode);
            }

            /**
             * Traverses the network of nodes and counts the number of steps to get to the end
             *
             * @return The number of steps to get to the end
             */
            public long traverse(Node startNode, Predicate<Node> atEndNode) {
                long stepCount = 0;

                System.out.println("Traversing the network of Nodes starting at " + startNode.getName() +
                        " Node");

                var currentNode = startNode;

                do {
                    for (var nextStep : steps) {
                        if (currentNode == null || atEndNode.test(currentNode)) {
                            break;
                        }
                        currentNode = takeStep(nextStep, currentNode);
                        stepCount++;
                    }
                } while (currentNode != null && !atEndNode.test(currentNode));

                if (currentNode != null) {
                    System.out.println("Traversed the network of nodes starting at " + startNode.getName() + " and ended at " + currentNode.getName() + " in " + stepCount + " steps!");
                } else {
                    System.out.println("Something went wrong after taking "  + stepCount + " steps when starting at " + startNode.getName() + " as we didn't end at the correct node!");
                }
                return stepCount;
            }

            public Node takeStep(Direction nextStep, Node currentNode) {
                if (nextStep == Direction.LEFT) {
                    return currentNode.getLeft();
                } else if (nextStep == Direction.RIGHT) {
                    return currentNode.getRight();
                } else {
                    throw new RuntimeException("Unable to take the next step due to an unknown Direction: " + nextStep);
                }
            }
        }

    private static final String inputFilename = "inputs/input_day_08-01.txt";
    private static final String NODE_NAME_SPLIT = " = ";
    private static final String NODE_CHILDREN_SPLIT = ", ";

    public static void main(String[] args) throws URISyntaxException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        var url = classloader.getResource(inputFilename);
        assert url != null;
        var path = Paths.get(url.toURI());
        Network network;

        try (var stream = Files.lines(path)) {
            network = Network.buildNetwork(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Part 1
        var endNode = network.nodeMap().get("ZZZ");
        System.out.println("Part 1: Start!");
        var stepsTaken = network.traverse(network.nodeMap.get("AAA"), it -> it == endNode);

        System.out.println("Part 1: Traversed the network of nodes in " + stepsTaken + " steps!");

        // Part 2
        System.out.println("Part 2: Start!");
        var startingNodes = network.nodeMap().values().stream().filter(it -> it.getName().endsWith("A")).collect(Collectors.toSet());
        var endingNodes = network.nodeMap().values().stream().filter(it -> it.getName().endsWith("Z")).collect(Collectors.toSet());
        var stepsToOneEnd = startingNodes.stream()
                .map(startNode -> network.traverse(startNode, endingNodes::contains))
                .collect(Collectors.toSet());

        stepsTaken = stepsToOneEnd.stream().reduce(Day08::leastCommonMultiple).orElse(0L);
        System.out.println("Part 2: Would need to take a total of " + stepsTaken +
                " steps in order to traverse the network from all of the starting nodes and end on a node that ends with the letter Z");

    }

    private static long leastCommonMultiple(long a, long b) {
        return (a * b) / greatestCommonDenominator(a, b);
    }

    private static long greatestCommonDenominator(long a, long b) {
        if (a == 0) {
            return b;
        }
        return greatestCommonDenominator(b % a, a);
    }
}
