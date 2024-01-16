package com.capital7software.aoc.aoc2023.days;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class Day13 {

    private enum Ground {
        ASH('.'),
        ROCK('#');

        private final char label;

        Ground(char label) {
            this.label = label;
        }

        public static Ground from(char label) {
            for (var value : values()) {
                if (value.label == label) {
                    return value;
                }
            }

            return null;
        }

        @Override
        public String toString() {
            return String.valueOf(label);
        }
    }

    private enum Axis {
        HORIZONTAL,
        VERTICAL
    }

    private record Reflection(Axis axis, long low, long high) {
    }

    private record Pattern(List<Ground> grounds, int columns, int rows) {

        public static List<Pattern> parse(Stream<String> stream) {
            var result = new ArrayList<Pattern>();
            AtomicReference<ArrayList<Ground>> grounds = new AtomicReference<>(new ArrayList<>());
            var rows = new AtomicInteger(0);
            var columns = new AtomicInteger(0);

            stream.forEach(line -> {
                if (grounds.get().isEmpty()) {
                    columns.set(line.length());
                }

                if (parseLine(line, grounds.get())) {
                    rows.incrementAndGet();
                } else {
                    result.add(new Pattern(grounds.get(), columns.get(), rows.get()));
                    grounds.set(new ArrayList<>());
                    columns.set(0);
                    rows.set(0);
                }
            });

            if (!grounds.get().isEmpty()) {
                result.add(new Pattern(grounds.get(), columns.get(), rows.get()));
            }

            return result;
        }

        private static boolean parseLine(String line, List<Ground> grounds) {
            if (line == null || line.isBlank()) {
                return false;
            }

            return grounds.addAll(line.chars()
                    .mapToObj(it -> (char) it)
                    .map(Ground::from)
                    .toList());
        }

        @Override
        public String toString() {
            var builder = new StringBuilder();
            builder.append("Pattern{");
            builder.append("\n\tColumns: ").append(columns);
            builder.append("\n\tRows: ").append(rows);
            builder.append("\n\tGrounds: ");
            for (int i = 0; i < rows; i++) {
                builder.append("\n\t\t").append(grounds.subList(i * columns, i * columns + columns));
            }
            builder.append("\n}");
            return builder.toString();
        }

        /**
         * Finds the perfect reflection within the pattern. The reflection can either be along the vertical or
         * horizontal axis and will contain the low and high points where the pattern reflects across.
         * If no reflection is found, then no reflection is returned.
         *
         * @param smudges The number of differences between two rows / columns in order to consider them a match.
         * @return An Optional Reflection of this Pattern.
         */
        public Optional<Reflection> findReflection(int smudges) {
            // We check each axis independently
            // We check the current and next column / row for a reflection, if one is found,
            // the low / high points are marked, and then we check above / left of the low point and below / right of
            // the high point. A valid reflection is found if we reach the beginning / end of the pattern while
            // validating a reflection.

            var reflection = getReflection(Axis.HORIZONTAL, smudges);

            if (reflection.isEmpty()) {
                reflection = getReflection(Axis.VERTICAL, smudges);
            }

            return reflection;
        }

        private Optional<Reflection> getReflection(Axis axis, int smudges) {
            int length = getRowOrColumnLength(axis);
            for (int i = 0; i < length - 1; i++) {
                int smudgeCount = 0;
                for (int j = 0; j < length; j++) {
                    if ((i - j) >= 0 && i + 1 + (i - j) < length) {
                        var first = getRowOrColumn(axis, j);
                        var second = getRowOrColumn(axis, i + 1 + (i - j));

                        if (!first.equals(second)) {
                            for (int k = 0; k < first.size(); k++) {
                                if (first.get(k) != second.get(k)) {
                                    smudgeCount += 1;
                                }
                            }
                        }
                    }
                }
                if (smudgeCount == smudges) {
                    return Optional.of(new Reflection(axis, i, i + 1));
                }
            }

            return Optional.empty();
        }

        /**
         * Gets a row or column as a List that then can be easily compared
         *
         * @param axis The x or y axis
         * @param index The axis index
         * @return A list of Ground tiles that can be easily looped over.
         */
        private List<Ground> getRowOrColumn(Axis axis, int index) {
            if (axis == Axis.VERTICAL) {
                if (index < 0 || index >= columns) {
                    throw new IndexOutOfBoundsException("Column index out of range: " + index);
                }

                var list = new ArrayList<Ground>(rows);
                for (int i = 0; i < rows; i++) {
                    list.add(grounds.get( index + (i * columns)));
                }
                return list;
            } else if (axis == Axis.HORIZONTAL) {
                if (index < 0 || index >= rows) {
                    throw new IndexOutOfBoundsException("Row index out of range: " + index);
                }

                var list = new ArrayList<Ground>(columns);
                for (int i = 0; i < columns; i++) {
                    list.add(grounds.get( i + (index * columns)));
                }
                return list;
            } else {
                throw new RuntimeException("Unknown axis specified: " + axis);
            }
        }

        private int getRowOrColumnLength(Axis axis) {
            switch (axis) {
                case VERTICAL -> {
                    return columns;
                }
                case HORIZONTAL -> {
                    return rows;
                }
                default -> {
                    return 0;
                }
            }
        }
    }

    private static final String inputFilename = "inputs/input_day_13-01.txt";

    public static void main(String[] args) throws URISyntaxException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        var url = classloader.getResource(inputFilename);
        assert url != null;
        var path = Paths.get(url.toURI());
        part1(path);
        part2(path);
    }

    private static void part1(Path path) {
        try (var stream = Files.lines(path)) {
            // Part 1
            System.out.println("Part 1: Start!");
            var patterns = loadPatterns(stream, false);
            System.out.println("Summarizing patterns...");
            var start = Instant.now();
            var sum = summarizeNotes(patterns, 0);
            var end = Instant.now();
            System.out.println("Sum of all patterns is: " + sum + " in " + Duration.between(start, end).toNanos() + " ns");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void part2(Path path) {
        try (var stream = Files.lines(path)) {
            // Part 2
            System.out.println("Part 2: Start!");
            var patterns = loadPatterns(stream, false);
            System.out.println("Summarizing patterns...");
            var start = Instant.now();
            var sum = summarizeNotes(patterns, 1);
            var end = Instant.now();
            System.out.println("Sum of all patterns is: " + sum + " in " + Duration.between(start, end).toNanos() + " ns");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Pattern> loadPatterns(Stream<String> stream, boolean print) {
        List<Pattern> patterns = Pattern.parse(stream);
        System.out.println("Loaded " + patterns.size() + " patterns.");
        if (print) {
            System.out.println("Patterns:");
            patterns.forEach(System.out::println);
        }

        return patterns;
    }

    public static long summarizeNotes(List<Pattern> patterns, int smudges) {
        return patterns.stream().
                mapToLong(pattern -> {
                    var optional = pattern.findReflection(smudges);

                    if (optional.isPresent()) {
                        Reflection reflection = optional.get();
                        if (reflection.axis() == Axis.VERTICAL) {
                            return reflection.low() + 1;
                        } else {
                            return 100 * (reflection.low() + 1);
                        }
                    } else {
                        return 0;
                    }
                })
                .sum();
    }

}
