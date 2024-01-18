package com.capital7software.aoc.aoc2023.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.math.HistorySequence;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.List;
import java.util.logging.Logger;

/**
 * --- Day 9: Mirage Maintenance ---<br>
 * You ride the camel through the sandstorm and stop where the ghost's maps told you to stop.
 * The sandstorm subsequently subsides, somehow seeing you standing at an oasis!
 * <p><br>
 * The camel goes to get some water, and you stretch your neck. As you look up, you discover
 * what must be yet another giant floating island, this one made of metal! That must be where
 * the parts to fix the sand machines come from.
 * <p><br>
 * There's even a hang glider partially buried in the sand here; once the sun rises and heats
 * up the sand, you might be able to use the glider and the hot air to get all the way up to the metal island!
 * <p><br>
 * While you wait for the sun to rise, you admire the oasis hidden here in the middle of
 * Desert Island. It must have a delicate ecosystem; you might as well take some ecological
 * readings while you wait. Maybe you can report any environmental instabilities you find to
 * someone so the oasis can be around for the next sandstorm-worn traveler.
 * <p><br>
 * You pull out your handy Oasis And Sand Instability Sensor and analyze your surroundings.
 * The OASIS produces a report of many values and how they are changing over time (your puzzle input).
 * Each line in the report contains the history of a single value. For example:
 * <p><br>
 * <code>
 * 0 3 6 9 12 15<br>
 * 1 3 6 10 15 21<br>
 * 10 13 16 21 30 45<br>
 * </code>
 * <p><br>
 * To best protect the oasis, your environmental report should include a prediction of the next value
 * in each history. To do this, start by making a new sequence from the difference at each step of
 * your history. If that sequence is not all zeroes, repeat this process, using the sequence you
 * just generated as the input sequence. Once all of the values in your latest sequence are zeroes,
 * you can extrapolate what the next value of the original history should be.
 * <p><br>
 * In the above dataset, the first history is 0 3 6 9 12 15. Because the values increase by 3 each step,
 * the first sequence of differences that you generate will be 3 3 3 3 3. Note that this sequence has
 * one fewer value than the input sequence because at each step it considers two numbers from the input.
 * Since these values aren't all zero, repeat the process: the values differ by 0 at each step,
 * so the next sequence is 0 0 0 0. This means you have enough information to extrapolate the history!
 * Visually, these sequences can be arranged like this:
 * <p><br>
 * <code>
 * 0   3   6   9  12  15<br>
 * 3   3   3   3   3<br>
 * 0   0   0   0<br>
 * </code>
 * <p><br>
 * To extrapolate, start by adding a new zero to the end of your list of zeroes; because the zeroes
 * represent differences between the two values above them, this also means there is now a placeholder
 * in every sequence above it:
 * <p><br>
 * <code>
 * 0   3   6   9  12  15   B<br>
 * 3   3   3   3   3   A<br>
 * 0   0   0   0   0<br>
 * </code>
 * <p><br>
 * You can then start filling in placeholders from the bottom up. A needs to be the result of
 * increasing 3 (the value to its left) by 0 (the value below it); this means A must be 3:
 * <p><br>
 * <code>
 * 0   3   6   9  12  15   B<br>
 * 3   3   3   3   3   3<br>
 * 0   0   0   0   0<br>
 * </code>
 * <p><br>
 * Finally, you can fill in B, which needs to be the result of increasing 15 (the value to its left)
 * by 3 (the value below it), or 18:
 * <p><br>
 * <code>
 * 0   3   6   9  12  15  18<br>
 * 3   3   3   3   3   3<br>
 * 0   0   0   0   0<br>
 * </code>
 * <p><br>
 * So, the next value of the first history is 18.
 * <p><br>
 * Finding all-zero differences for the second history requires an additional sequence:
 * <p><br>
 * <code>
 * 1   3   6  10  15  21<br>
 * 2   3   4   5   6<br>
 * 1   1   1   1<br>
 * 0   0   0<br>
 * </code>
 * <p><br>
 * Then, following the same process as before, work out the next value in each sequence from the bottom up:
 * <p><br>
 * <code>
 * 1   3   6  10  15  21  28<br>
 * 2   3   4   5   6   7<br>
 * 1   1   1   1   1<br>
 * 0   0   0   0<br>
 * </code>
 * <p><br>
 * So, the next value of the second history is 28.
 * <p><br>
 * The third history requires even more sequences, but its next value can be found the same way:
 * <p><br>
 * <code>
 * 10  13  16  21  30  45  68<br>
 * 3   3   5   9  15  23<br>
 * 0   2   4   6   8<br>
 * 2   2   2   2<br>
 * 0   0   0<br>
 * </code>
 * <p><br>
 * So, the next value of the third history is 68.
 * <p><br>
 * If you find the next value for each history in this example and add them together, you get 114.
 * <p><br>
 * Analyze your OASIS report and extrapolate the next value for each history. What is the sum of
 * these extrapolated values?
 * <p><br>
 * Your puzzle answer was 1901217887.
 * <p><br>
 * --- Part Two ---<br>
 * Of course, it would be nice to have even more history included in your report. Surely it's safe
 * to just extrapolate backwards as well, right?
 * <p><br>
 * For each history, repeat the process of finding differences until the sequence of differences
 * is entirely zero. Then, rather than adding a zero to the end and filling in the next values of
 * each previous sequence, you should instead add a zero to the beginning of your sequence of zeroes,
 * then fill in new first values for each previous sequence.
 * <p><br>
 * In particular, here is what the third example history looks like when extrapolating back in time:
 * <p><br>
 * <code>
 * 5  10  13  16  21  30  45<br>
 * 5   3   3   5   9  15<br>
 * -2   0   2   4   6<br>
 * 2   2   2   2<br>
 * 0   0   0<br>
 * </code>
 * <p><br>
 * Adding the new values on the left side of each sequence from bottom to top eventually reveals
 * the new left-most history value: 5.
 * <p><br>
 * Doing this for the remaining example data above results in previous values of -3 for the first
 * history and 0 for the second history. Adding all three new values together produces 2.
 * <p><br>
 * Analyze your OASIS report again, this time extrapolating the previous value for each history.
 * What is the sum of these extrapolated values?
 * <p><br>
 * Your puzzle answer was 905.
 */
public class Day09 implements AdventOfCodeSolution {
    private static final Logger LOGGER = Logger.getLogger(Day09.class.getName());

    /**
     * Instantiates this Solution instance.
     */
    public Day09() {

    }

    @Override
    public String getDefaultInputFilename() {
        return "inputs/input_day_09-01.txt";
    }

    @Override
    public void runPart1(List<String> input) {
        var start = Instant.now();
        var answer = sumOfNextPredictedHistories(input);
        var end = Instant.now();

        LOGGER.info(String.format("The sum of the next predicted values is: %d%n", answer));
        logTimings(LOGGER, start, end);
    }

    @Override
    public void runPart2(List<String> input) {
        var start = Instant.now();
        var answer = sumOfPreviousPredictedHistories(input);
        var end = Instant.now();

        LOGGER.info(String.format("The sum of the previous predicted values is: %d%n", answer));
        logTimings(LOGGER, start, end);
    }

    /**
     * Returns the sum of the next predicted value for all HistorySequences in the specified input.
     *
     * @param input The List of Strings of histories to parse and load.
     * @return The sum of the next predicted value for all parsed and loaded histories.
     */
    public long sumOfNextPredictedHistories(List<String> input) {
        var histories = loadHistories(input);
        return histories.stream().mapToLong(HistorySequence::predictNextValue).sum();
    }

    /**
     * Returns the sum of the previous predicted value for all HistorySequences in the specified input.
     *
     * @param input The List of Strings of histories to parse and load.
     * @return The sum of the previous predicted value for all parsed and loaded histories.
     */
    public long sumOfPreviousPredictedHistories(List<String> input) {
        var histories = loadHistories(input);
        return histories.stream().mapToLong(HistorySequence::predictPreviousValue).sum();
    }

    private @NotNull List<HistorySequence> loadHistories(List<String> input) {
        return input.stream().map(HistorySequence::new).toList();
    }
}
