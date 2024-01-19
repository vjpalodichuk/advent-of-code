package com.capital7software.aoc.aoc2023.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.analysis.MirrorMirror;

import java.time.Instant;
import java.util.List;
import java.util.logging.Logger;

/**
 * --- Day 13: Point of Incidence ---<br><br>
 * With your help, the hot springs team locates an appropriate spring which launches you neatly and precisely
 * up to the edge of Lava Island.
 * <p><br>
 * There's just one problem: you don't see any lava.
 * <p><br>
 * You do see a lot of ash and igneous rock; there are even what look like gray mountains scattered around.
 * After a while, you make your way to a nearby cluster of mountains only to discover that the valley between
 * them is completely full of large mirrors. Most of the mirrors seem to be aligned in a consistent way;
 * perhaps you should head in that direction?
 * <p><br>
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
 * <p><br>
 * Your puzzle answer was 31947.
 */
public class Day13 implements AdventOfCodeSolution {
    private static final Logger LOGGER = Logger.getLogger(Day13.class.getName());

    /**
     * Instantiates this Solution instance.
     */
    public Day13() {

    }

    @Override
    public String getDefaultInputFilename() {
        return "inputs/input_day_13-01.txt";
    }

    @Override
    public void runPart1(List<String> input) {
        var start = Instant.now();
        var answer = summarizePatternReflectionNotes(input, 0);
        var end = Instant.now();

        LOGGER.info(String.format("Summary of pattern notes is: %d",
                answer));
        logTimings(LOGGER, start, end);
    }

    @Override
    public void runPart2(List<String> input) {
        var start = Instant.now();
        var answer = summarizePatternReflectionNotes(input, 1);
        var end = Instant.now();

        LOGGER.info(String.format("Summary of pattern notes allowing for a single smudge is: %d",
                answer));
        logTimings(LOGGER, start, end);
    }

    /**
     * Returns the summarized pattern reflection notes. If allowedSmudges is positive, then the specified number of
     * differences will be used in determining a match.
     *
     * @param input The List of Strings that contain the patterns.
     * @param allowedSmudges If positive, then that many differences in the patterns will still result in a match.
     * @return The summarized pattern reflections notes.
     */
    public long summarizePatternReflectionNotes(List<String> input, int allowedSmudges) {
        var mirrorMirror = MirrorMirror.buildMirrorMirror(input);

        return mirrorMirror.summarizeNotes(allowedSmudges);
    }
}
