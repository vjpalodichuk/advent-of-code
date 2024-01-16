package com.capital7software.aoc.aoc2023.days;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Stream;

public class Day11 {
    private record Galaxy(int id, int x, int y) {

        @Override
        public String toString() {
            return "Galaxy{" +
                    "id=" + id +
                    ", x=" + x +
                    ", y=" + y +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Galaxy galaxy)) return false;
            return id() == galaxy.id();
        }

        @Override
        public int hashCode() {
            return Objects.hash(id());
        }
    }

    private static class Universe {
        private static final char GALAXY_MARKER = '#';
        private static final Map<Integer, Integer> EMPTY_MAP = new HashMap<>();
        private final Map<Integer, Integer> columnGalaxyCountMap = new HashMap<>();
        private final Map<Integer, Map<Integer, Integer>> columnGapCountMap = new HashMap<>();
        private final Map<Integer, Integer> rowGalaxyCountMap = new HashMap<>();
        private final Map<Integer, Map<Integer, Integer>> rowGapCountMap = new HashMap<>();

        private final Map<Galaxy, Map<Galaxy, Long>> galaxyDistances = new HashMap<>();

        public static Universe loadUniverse(Stream<String> file) {
            var universe = new Universe();
            var rowCount = new AtomicInteger(0);
            var columnSize = new AtomicInteger(0);
            file.forEach(it -> {
                if (columnSize.get() == 0) {
                    columnSize.set(it.length());
                }

                processRow(it, rowCount.getAndIncrement(), universe);
            });

            universe.expandUniverse(rowCount.get(), columnSize.get());

            return universe;
        }

        private void expandUniverse(int rowCount, int columnCount) {
            System.out.println("Expanding the Universe...");

            var startTime = Instant.now();

            // Everything is ordered so that the first map index is always less than the second map index.
            populateGapCountMap(rowCount, rowGalaxyCountMap, rowGapCountMap);
            populateGapCountMap(columnCount, columnGalaxyCountMap, columnGapCountMap);

            var endTime = Instant.now();

            System.out.println("Expanded the universe in " + Duration.between(startTime, endTime).toNanos() + " ns");
        }

        private void populateGapCountMap(int count, Map<Integer, Integer> galaxyCountMap, Map<Integer, Map<Integer, Integer>> gapCountMap) {
            for (int i = 0; i < count; i++) {
                var blanks = 0;
                for (int j = i; j < count; j++) {
                    if (galaxyCountMap.computeIfAbsent(j, it -> 0) == 0) {
                        blanks++;
                    }
                    if (blanks > 0) {
                        gapCountMap.computeIfAbsent(i, it -> new HashMap<>()).putIfAbsent(j, blanks);
                    }
                }
            }
        }

        private static void processRow(String line, int row, Universe universe) {
            var columnCount = new AtomicInteger(0);

            line
                    .chars()
                    .mapToObj(it -> (char) it)
                    .forEach(it -> processCharacter(it, row, columnCount.getAndIncrement(), universe));
        }

        private static void processCharacter(char item, int row, int column, Universe universe) {
            if (item == GALAXY_MARKER) {
                universe.addGalaxy(row, column);
            }
        }

        private void addGalaxy(int row, int column) {
            var galaxy = new Galaxy(galaxyDistances.size(), column, row);
            galaxyDistances.computeIfAbsent(galaxy, it -> new HashMap<>());
            columnGalaxyCountMap.put(column, columnGalaxyCountMap.computeIfAbsent(column, it -> 0) + 1);
            rowGalaxyCountMap.put(row, rowGalaxyCountMap.computeIfAbsent(row, it -> 0) + 1);
        }

        public int getGalaxyCount() {
            return galaxyDistances.size();
        }

        public long calculateDistances() {
            return calculateDistances(1);
        }

        public long calculateDistances(long gapFactor) {
            System.out.println("Calculating distances for all of the galaxy pairs...");

            var startTime = Instant.now();
            // We calculate the distances for all pairs a, b where a != b && a.id < b.id
            // Start with a sorted list of all galaxies
            var galaxies = galaxyDistances
                    .keySet()
                    .stream()
                    .sorted(Comparator.comparing(Galaxy::id)).toList();

            // start with the first galaxy encountered
            var currentGalaxy = galaxies.get(0);

            do {
                for (int i = currentGalaxy.id() + 1; i < galaxies.size(); i++) {
                    calculateAndStoreDistance(currentGalaxy, galaxies.get(i), gapFactor);
                }
                currentGalaxy = galaxies.get(currentGalaxy.id + 1);
            } while (currentGalaxy.id() != galaxies.size() - 1); // Skip the last galaxy!

            var endTime = Instant.now();

            System.out.println("Calculated the distances where each gap counts as " + (gapFactor + 1) + " in "
                    + Duration.between(startTime, endTime).toNanos() + " ns");

            return galaxyDistances
                    .values()
                    .stream()
                    .mapToLong(it -> it.values().stream().mapToLong(distance -> distance).sum())
                    .sum();
        }

        /**
         * The distance between two galaxies is the sum of the differences between their x and y coordinates plus any
         * expanded columns and rows that lie between them. The ID of the first galaxy must be less than the second
         * galaxy!
         *
         * @param first  The first galaxy to compare
         * @param second The second galaxy to compare
         */
        private void calculateAndStoreDistance(Galaxy first, Galaxy second, long gapFactor) {
            long columnDistance = 0;
            if (first.x() != second.x()) {
                var min = Math.min(first.x(), second.x());
                var max = Math.max(first.x(), second.x());
                final var gap = columnGapCountMap.getOrDefault(min, EMPTY_MAP).getOrDefault(max, 0);
                columnDistance = (max - min) + (gap * gapFactor);
            }
            long rowDistance = 0;
            if (first.y() != second.y()) {
                var min = Math.min(first.y(), second.y());
                var max = Math.max(first.y(), second.y());
                final var gap = rowGapCountMap.getOrDefault(min, EMPTY_MAP).getOrDefault(max, 0);
                rowDistance = (max - min) + (gap * gapFactor);
            }
            galaxyDistances.computeIfAbsent(first, it -> new HashMap<>()).put(second, columnDistance + rowDistance);
        }

        public long calculateOptimizedDistances() {
            return calculateOptimizedDistances(1);
        }

        public long calculateOptimizedDistances(long gapFactor) {
            System.out.println("Greedy calculation of distances for all galaxy pairs...");

            var startTime = Instant.now();

            // start with the first galaxy encountered
            var galaxies = galaxyDistances.keySet();

            long totalSum = greedySum(galaxies, Galaxy::x, gapFactor);
            totalSum += greedySum(galaxies, Galaxy::y, gapFactor);

            var endTime = Instant.now();

            System.out.println("Calculated the distances where each gap counts as " + (gapFactor + 1) + " in "
                    + Duration.between(startTime, endTime).toNanos() + " ns");

            return totalSum;
        }

        private long greedySum(Set<Galaxy> galaxies, Function<Galaxy, Integer> keyExtractor, long gapFactor) {
            // Start with a sorted list of all galaxies
            List<Galaxy> sorted = galaxies.stream().sorted(Comparator.comparing(keyExtractor)).toList();
            long total = 0, sum = 0, gap = 0;
            // Used for looking up gaps
            int prevCoord = keyExtractor.apply(sorted.get(0));

            for (int i = 0; i < galaxies.size(); i++) {
                var currentCoord = keyExtractor.apply(sorted.get(i));
                // Add any new gaps to our calculations!
                if (currentCoord - prevCoord > 1) {
                    gap += ((currentCoord - prevCoord) - 1) * gapFactor;
                }

                total += (((currentCoord + gap) * i) - sum);
                sum += (currentCoord + gap);

                prevCoord = currentCoord;
            }

            return total;
        }
    }

    private static final String inputFilename = "inputs/input_day_11-01.txt";

    public static void main(String[] args) throws URISyntaxException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        var url = classloader.getResource(inputFilename);
        assert url != null;
        var path = Paths.get(url.toURI());
        Universe universe;

        try (var stream = Files.lines(path)) {
            System.out.println("Loading the Universe...");
            universe = Universe.loadUniverse(stream);
            System.out.println("Loaded " + universe.getGalaxyCount() + " galaxies in the universe!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Part 1
        System.out.println("Part 1: Start!");
        long sum = universe.calculateDistances();
        System.out.println("The sum of all distances is: " + sum);
        sum = universe.calculateDistances();
        System.out.println("The sum of all distances is: " + sum);
        sum = universe.calculateOptimizedDistances();
        System.out.println("The sum of all distances is: " + sum);

        // Part 2
        System.out.println("Part 2: Start!");
        sum = universe.calculateDistances(999_999);
        System.out.println("The sum of all distances is: " + sum);
        sum = universe.calculateOptimizedDistances(999_999);
        System.out.println("The sum of all distances is: " + sum);
    }
}
