package com.capital7software.aoc.lib.analysis;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


/**
 * As you move through the valley of mirrors, you find that several of them have fallen from the large metal
 * frames keeping them in place. The mirrors are extremely flat and shiny, and many of the fallen mirrors have
 * lodged into the ash at strange angles. Because the terrain is all one color, it's hard to tell where it's
 * safe to walk or where you're about to run into a mirror.
 * <p><br>
 * You note down the patterns of ash (.) and rocks (#) that you see as you walk (your puzzle input); perhaps
 * by carefully analyzing these patterns, you can figure out where the mirrors are!
 * <p><br>
 * For example:
 * <p><br>
 * <code>
 * #.##..##.<br>
 * ..#.##.#.<br>
 * ##......#<br>
 * ##......#<br>
 * ..#.##.#.<br>
 * ..##..##.<br>
 * #.#.##.#.<br>
 * </code>
 * <p><br>
 * <code>
 * #...##..#<br>
 * #....#..#<br>
 * ..##..###<br>
 * #####.##.<br>
 * #####.##.<br>
 * ..##..###<br>
 * #....#..#<br>
 * </code>
 * <p><br>
 * To find the reflection in each pattern, you need to find a perfect reflection across either a horizontal
 * line between two rows or across a vertical line between two columns.
 * <p><br>
 * In the first pattern, the reflection is across a vertical line between two columns; arrows on each of
 * the two columns point at the line between the columns:
 * <p><br>
 * <code>
 * 123456789<br>
 * &nbsp;&nbsp;&nbsp;    &#62;&#60;<br>
 * #.##..##.<br>
 * ..#.##.#.<br>
 * ##......#<br>
 * ##......#<br>
 * ..#.##.#.<br>
 * ..##..##.<br>
 * #.#.##.#.<br>
 * &nbsp;&nbsp;&nbsp;    &#62;&#60;<br>
 * 123456789<br>
 * </code>
 * <p><br>
 * In this pattern, the line of reflection is the vertical line between columns 5 and 6. Because the vertical
 * line is not perfectly in the middle of the pattern, part of the pattern (column 1) has nowhere to reflect
 * onto and can be ignored; every other column has a reflected column within the pattern and must match
 * exactly: column 2 matches column 9, column 3 matches 8, 4 matches 7, and 5 matches 6.
 * <p><br>
 * The second pattern reflects across a horizontal line instead:
 * <p><br>
 * <code>
 * 1 #...##..# 1<br>
 * 2 #....#..# 2<br>
 * 3 ..##..### 3<br>
 * 4v#####.##.v4<br>
 * 5^#####.##.^5<br>
 * 6 ..##..### 6<br>
 * 7 #....#..# 7<br>
 * </code>
 * <p><br>
 * This pattern reflects across the horizontal line between rows 4 and 5. Row 1 would reflect with a
 * hypothetical row 8, but since that's not in the pattern, row 1 doesn't need to match anything.
 * The remaining rows match: row 2 matches row 7, row 3 matches row 6, and row 4 matches row 5.
 * <p><br>
 * To summarize your pattern notes, add up the number of columns to the left of each vertical line of
 * reflection; to that, also add 100 multiplied by the number of rows above each horizontal line of
 * reflection. In the above example, the first pattern's vertical line has 5 columns to its left and
 * the second pattern's horizontal line has 4 rows above it, a total of 405.
 * <p><br>
 * Find the line of reflection in each of the patterns in your notes. What number do you get after
 * summarizing all of your notes?
 * <p><br>
 * Your puzzle answer was 27502.
 * <p><br>
 * --- Part Two ---<br><br>
 * You resume walking through the valley of mirrors and - SMACK! - run directly into one. Hopefully
 * nobody was watching, because that must have been pretty embarrassing.
 * <p><br>
 * Upon closer inspection, you discover that every mirror has exactly one smudge: exactly one .
 * or # should be the opposite type.
 * <p><br>
 * In each pattern, you'll need to locate and fix the smudge that causes a different reflection
 * line to be valid. (The old reflection line won't necessarily continue being valid after the smudge is fixed.)
 * <p><br>
 * Here's the above example again:
 * <p><br>
 * <code>
 * #.##..##.<br>
 * ..#.##.#.<br>
 * ##......#<br>
 * ##......#<br>
 * ..#.##.#.<br>
 * ..##..##.<br>
 * #.#.##.#.<br>
 * </code>
 * <p><br>
 * <code>
 * #...##..#<br>
 * #....#..#<br>
 * ..##..###<br>
 * #####.##.<br>
 * #####.##.<br>
 * ..##..###<br>
 * #....#..#<br>
 * </code>
 * <p><br>
 * The first pattern's smudge is in the top-left corner. If the top-left # were instead .,
 * it would have a different, horizontal line of reflection:
 * <p><br>
 * <code>
 * 1 ..##..##. 1<br>
 * 2 ..#.##.#. 2<br>
 * 3v##......#v3<br>
 * 4^##......#^4<br>
 * 5 ..#.##.#. 5<br>
 * 6 ..##..##. 6<br>
 * 7 #.#.##.#. 7<br>
 * </code>
 * <p><br>
 * With the smudge in the top-left corner repaired, a new horizontal line of reflection between
 * rows 3 and 4 now exists. Row 7 has no corresponding reflected row and can be ignored, but every
 * other row matches exactly: row 1 matches row 6, row 2 matches row 5, and row 3 matches row 4.
 * <p><br>
 * In the second pattern, the smudge can be fixed by changing the fifth symbol on row 2 from . to #:
 * <p><br>
 * <code>
 * 1v#...##..#v1<br>
 * 2^#...##..#^2<br>
 * 3 ..##..### 3<br>
 * 4 #####.##. 4<br>
 * 5 #####.##. 5<br>
 * 6 ..##..### 6<br>
 * 7 #....#..# 7<br>
 * </code>
 * <p><br>
 * Now, the pattern has a different horizontal line of reflection between rows 1 and 2.
 * <p><br>
 * Summarize your notes as before, but instead use the new different reflection lines. In this example,
 * the first pattern's new horizontal line has 3 rows above it and the second pattern's new horizontal
 * line has 1 row above it, summarizing to the value 400.
 * <p><br>
 * In each pattern, fix the smudge and find the different line of reflection. What number do you get
 * after summarizing the new reflection line in each pattern in your notes?
 */
public class MirrorMirror {
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

    private record Reflection(@NotNull Axis axis, long low, long high) {
    }

    private record Pattern(@NotNull List<Ground> grounds, int columns, int rows) {

        public static @NotNull List<Pattern> parse(@NotNull List<String> input) {
            var result = new ArrayList<Pattern>();
            AtomicReference<ArrayList<Ground>> grounds = new AtomicReference<>(new ArrayList<>());
            var rows = new AtomicInteger(0);
            var columns = new AtomicInteger(0);

            input.forEach(line -> {
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
         * @param axis  The x or y-axis
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
                    list.add(grounds.get(index + (i * columns)));
                }
                return list;
            } else if (axis == Axis.HORIZONTAL) {
                if (index < 0 || index >= rows) {
                    throw new IndexOutOfBoundsException("Row index out of range: " + index);
                }

                var list = new ArrayList<Ground>(columns);
                for (int i = 0; i < columns; i++) {
                    list.add(grounds.get(i + (index * columns)));
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

    private final List<Pattern> patterns;

    private MirrorMirror(List<Pattern> patterns) {
        this.patterns = new ArrayList<>(patterns);
    }

    /**
     * Builds and returns a new MirrorMirror loaded with the specified List of Strings as the patterns.
     *
     * @param input The List of Strings to parse for the patterns.
     * @return A new MirrorMirror loaded with the specified List of Strings as the patterns.
     */
    public static @NotNull MirrorMirror buildMirrorMirror(@NotNull List<String> input) {
        return new MirrorMirror(Pattern.parse(input));
    }

    /**
     * Returns the summarized pattern reflection notes. If allowedSmudges is positive, then the specified number of
     * differences will be used in determining a match.
     *
     * @param allowedSmudges If positive, then that many differences in the patterns will still result in a match.
     * @return The summarized pattern reflections notes.
     */
    public long summarizeNotes(int allowedSmudges) {
        return patterns.stream().
                mapToLong(pattern -> {
                    var optional = pattern.findReflection(allowedSmudges);

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
