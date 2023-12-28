package com.capital7software.aoc2023.days;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;

public class Day25 {
    public static class Pair<T> {
        private T first;
        private T second;

        public Pair(T first, T second) {
            this.first = first;
            this.second = second;
        }

        public T getFirst() {
            return first;
        }

        public void setFirst(T first) {
            this.first = first;
        }

        public T getSecond() {
            return second;
        }

        public void setSecond(T second) {
            this.second = second;
        }

        public Pair<T> copy() {
            return new Pair<>(first, second);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Pair<?> pair)) return false;
            return getFirst().equals(pair.getFirst()) && getSecond().equals(pair.getSecond());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getFirst(), getSecond());
        }

        @Override
        public String toString() {
            return "Pair{" +
                    "first=" + first +
                    ", second=" + second +
                    '}';
        }
    }

    public record WeatherStation(
            Map<String, Set<String>> edges,
            Set<String> nodes
    ) {
        public WeatherStation(Map<String, Set<String>> edges) {
            this(edges, new HashSet<>(edges.keySet()));
        }

        public void removeEdge(String nodeA, String nodeB) {
            edges.computeIfAbsent(nodeA, it -> new HashSet<>()).remove(nodeB);
            edges.computeIfAbsent(nodeB, it -> new HashSet<>()).remove(nodeA);
        }

        public WeatherStation copy() {
            var map = new HashMap<String, Set<String>>();

            edges.forEach((id, edgeSet) -> map.computeIfAbsent(id, it -> new HashSet<>(edgeSet)));

            return new WeatherStation(map);
        }

        public boolean contains(String node) {
            return nodes.contains(node);
        }

        public void add(String nodeA, String nodeB) {
            nodes.add(nodeA);
            nodes.add(nodeB);

            edges.computeIfAbsent(nodeA, it -> new HashSet<>()).add(nodeB);
            edges.computeIfAbsent(nodeB, it -> new HashSet<>()).add(nodeA);
        }

        public Set<String> get(String node) {
            nodes.add(node);
            return edges.computeIfAbsent(node, it -> new HashSet<>());
        }

        /**
         * Make sure to copy the weather station prior to calling this method as it will remove
         * the edges from the station!
         *
         * @param station The WeatherStation to make the directed edges from
         * @return A list of edges represented as a pair of nodes.
         */
        public static List<Pair<String>> makeDirected(WeatherStation station) {
            var edges = new ArrayList<Pair<String>>(station.nodes.size() * 3);

            for (var node : station.nodes) {
                var neighbors = new HashSet<>(station.get(node));
                for (var neighbor : neighbors) {
                    edges.add(new Pair<>(node, neighbor));
                    station.removeEdge(node, neighbor);
                }
            }

            return edges;
        }

        public static long findMinimumCut(WeatherStation station, long cuts) {
            var graph = station.copy();

            var edges = makeDirected(graph);

            var savedEdges = new ArrayList<>(edges.stream().map(Pair::copy).toList());

            List<String> keys;
            var nodeSizes = new HashMap<String, Long>();

            // Eventually this will find a solution and break out of the loop.
            while (true) {
                // Use Lists instead of sets here since we rename the edges
                // And that can lead to duplicates and Java's sets go wonky if that happens!
                edges = new ArrayList<>(savedEdges.stream().map(Pair::copy).toList());
                var nodes = new HashMap<String, List<Pair<String>>>();
                nodeSizes = new HashMap<>();

                for (var edge : edges) {
                    var nodeA = edge.getFirst();
                    var nodeB = edge.getSecond();
                    // Use Lists instead of sets here since we rename the edges
                    // And that can lead to duplicates and Java's sets go wonky if that happens!
                    nodes.computeIfAbsent(nodeA, it -> new LinkedList<>()).add(edge);
                    nodes.computeIfAbsent(nodeB, it -> new LinkedList<>()).add(edge);
                }

                for (var nodeEntry : nodes.entrySet()) {
                    nodeSizes.put(nodeEntry.getKey(), 1L);
                }

                while (nodes.size() > 2) {
                    // We select a random edge to ensure that we don't cut the graph in
                    // a lopsided manner!!!
                    var random = new Random(Instant.now().toEpochMilli());
                    var index = random.nextInt(edges.size());

                    var edgeT = edges.remove(index);
                    var nodeA = edgeT.getFirst();
                    var nodeB = edgeT.getSecond();

                    if (nodeA.equals(nodeB)) {
                        continue; // self edge
                    }

                    for (var bEdge : nodes.get(nodeB)) {
                        // rename b's edges to a's!
                        if (bEdge.getFirst().equals(nodeB)) {
                            bEdge.setFirst(nodeA);
                        }
                        if (bEdge.getSecond().equals(nodeB)) {
                            bEdge.setSecond(nodeA);
                        }
                        nodes.get(nodeA).add(bEdge);
                    }
                    nodes.remove(nodeB).clear();
                    nodeSizes.put(nodeA, nodeSizes.get(nodeA) + nodeSizes.get(nodeB));
                    nodeSizes.remove(nodeB);

                    var aEdges = new LinkedList<Pair<String>>();

                    for (var aEdge : nodes.get(nodeA)) {
                        var a = aEdge.getFirst();
                        var b = aEdge.getSecond();
                        if (a.equals(b)) {
                            continue; // self edge
                        }
                        aEdges.add(aEdge);
                    }
                    nodes.put(nodeA, aEdges);
                }

                // Should only have two nodes left
                keys = nodes.keySet().stream().toList();

                if (nodes.get(keys.get(0)).size() <= cuts) {
                    break;
                }
            }
            return nodeSizes.get(keys.get(0)) * nodeSizes.get(keys.get(1));
        }

        public static WeatherStation build(Stream<String> stream) {
            var components = new HashMap<String, Set<String>>();

            stream.forEach(line -> parseLine(line, components));

            return new WeatherStation(components);
        }

        private static void parseLine(
                String line,
                HashMap<String, Set<String>> components
        ) {
            if (line == null || line.isBlank()) {
                return;
            }

            var split = line.split(": ");
            var id = split[0].trim();
            var connections = split[1].split(" ");

            var component = components.computeIfAbsent(id, it -> new HashSet<>());

            for (var connection : connections) {
                var connectionComponent = components
                        .computeIfAbsent(connection.trim(), it -> new HashSet<>());
                component.add(connection);
                connectionComponent.add(id);
            }
        }
    }

    private static final String inputFilename = "inputs/input_day_25-01.txt";

    public static void main(String[] args) throws URISyntaxException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        var url = classloader.getResource(inputFilename);
        assert url != null;
        var path = Paths.get(url.toURI());

        part1(path);
        part2();
    }

    private static void part1(Path path) {
        try (var stream = Files.lines(path)) {
            // Part 1
            System.out.println("Part 1: Start!");
            var start = Instant.now();
            var station = WeatherStation.build(stream);
            var sum = WeatherStation.findMinimumCut(station, 3);
            var end = Instant.now();
            System.out.println("The product of the two groups of components is: "
                    + sum + " in " + Duration.between(start, end).toNanos() + " ns");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void part2() {
        // Part 2
        System.out.println("Part 2: Start!");
        System.out.println("Merry Christmas! Push the BIG RED button to continue!");
    }
}
