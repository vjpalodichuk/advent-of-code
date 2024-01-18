package com.capital7software.aoc.lib.math;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
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
 *
 * @param sequenceMap The Map to use with this HistorySequence.
 */
public record HistorySequence(@NotNull Map<Long, Deque<Long>> sequenceMap) {
    private static final Logger LOGGER = Logger.getLogger(HistorySequence.class.getName());
    private static final String VALUE_SPLIT = " ";
    private static final Long HISTORY_KEY = 0L;

    /**
     * Instantiates a new HistorySequence using the specified Map.
     *
     * @param sequenceMap The Map to use with the new HistorySequence.
     */
    public HistorySequence(@NotNull Map<Long, Deque<Long>> sequenceMap) {
        this.sequenceMap = new HashMap<>(sequenceMap);
    }

    /**
     * Instantiates a new HistorySequence from the sequence defined in the specified String.
     *
     * @param history The String to create a HistorySequence from.
     */
    public HistorySequence(@NotNull String history) {
        this(new HashMap<>());
        var parsed = parseHistory(history);
        var historyQueue = new ArrayDeque<Long>(parsed.size() + 4);
        historyQueue.addAll(parsed);
        sequenceMap.put(HISTORY_KEY, historyQueue);
    }

    /**
     * Returns an unmodifiable copy of the sequence map.
     *
     * @return An unmodifiable copy of the sequence map.
     */
    public Map<Long, Deque<Long>> sequenceMap() {
        return Collections.unmodifiableMap(sequenceMap);
    }

    private @NotNull List<Long> parseHistory(@NotNull String history) {
        if (history.isBlank()) {
            throw new RuntimeException("The specified history is null or blank: " + history);
        }

        return Arrays.stream(history.split(VALUE_SPLIT))
                .map(Long::parseLong)
                .toList();
    }

    /**
     * Predicts and returns the next number in this HistorySequence.
     *
     * @return The next number in this HistorySequence.
     */
    public @NotNull Long predictNextValue() {
        var size = sequenceMap.size();

        if (size == 1) {
            LOGGER.finest("Building sequence map...");
            buildSequenceMap();
            size = sequenceMap.size();
        }
        LOGGER.finest("Calculating the next value in the history sequence...");
        Long nextValue = calculateNextValue((long) (size - 1));
        LOGGER.finest(String.format("Next predicted value is: " + nextValue));
        return nextValue;
    }

    /**
     * Predicts and returns the previous number in this HistorySequence.
     *
     * @return The previous number in this HistorySequence.
     */
    public @NotNull Long predictPreviousValue() {
        var size = sequenceMap.size();

        if (size == 1) {
            LOGGER.finest("Building previous sequence map...");
            buildSequenceMap();
            size = sequenceMap.size();
        }
        LOGGER.finest("Calculating the previous value in the history sequence...");
        Long previousValue = calculatePreviousValue((long) (size - 1));
        LOGGER.finest(String.format("Previous predicted value is: %d", previousValue));
        return previousValue;
    }

    private void buildSequenceMap() {
        // The initial key is the history sequence
        // we keep adding sequences until the sequence is all zeroes
        // Each successive sequence will have one fewer element than the
        // preceding sequence as we take the difference between two adjacent elements and add that to
        // the next sequence

        // Start by adding
        var currentKey = 1L;
        List<Long> previousSequence;
        var currentSequence = new ArrayList<>(sequenceMap.get(HISTORY_KEY));

        do {
            previousSequence = currentSequence;
            currentSequence = new ArrayList<>();

            for (int i = 0, j = 1; j < previousSequence.size(); i++, j++) {
                currentSequence.add(previousSequence.get(j) - previousSequence.get(i));
            }
            var deque = new ArrayDeque<Long>(currentSequence.size() + 4);
            deque.addAll(currentSequence);
            sequenceMap.put(currentKey, deque);
            currentKey++;

        } while (!currentSequence.stream().allMatch(it -> it == 0L));
    }

    private @NotNull Long calculateNextValue(@NotNull Long currentKey) {
        int size = sequenceMap.size();
        // stopping condition
        if (currentKey < 0 || size == 1) {
            // We are done, so simply return the last value from the HISTORY_KEY
            LOGGER.finest("Next value has been calculated!");
            return sequenceMap.get(HISTORY_KEY).getLast();
        }
        // size - 1 is the key in the map for the zeros sequence
        // We start by adding another zero to that sequence and move to the next key
        if (currentKey + 1 == size) {
            sequenceMap.get(currentKey).add(0L);
            currentKey--;
        }

        var currentSequence = sequenceMap.get(currentKey);
        var nextSequence = sequenceMap.get(currentKey + 1);

        currentSequence.add(currentSequence.getLast() + nextSequence.getLast());

        return calculateNextValue(currentKey - 1);
    }

    private @NotNull Long calculatePreviousValue(@NotNull Long currentKey) {
        int size = sequenceMap.size();
        // stopping condition
        if (currentKey < 0 || size == 1) {
            // We are done, so simply return the first value from the HISTORY_KEY
            LOGGER.finest("Previous value has been calculated!");
            return sequenceMap.get(HISTORY_KEY).getFirst();
        }
        // size - 1 is the key in the map for the zeros sequence
        // We start by adding another zero to that sequence and move to the next key
        if (currentKey + 1 == size) {
            sequenceMap.get(currentKey).addFirst(0L);
            currentKey--;
        }

        var currentSequence = sequenceMap.get(currentKey);
        var nextSequence = sequenceMap.get(currentKey + 1);

        currentSequence.addFirst(currentSequence.getFirst() - nextSequence.getFirst());

        return calculatePreviousValue(currentKey - 1);
    }
}
