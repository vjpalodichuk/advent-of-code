package com.capital7software.aoc.aoc2023.days;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day09 {

    private static class History {
        public static final Long HISTORY_KEY = 0L;
        private final Map<Long, Deque<Long>> sequenceMap;

        public History(String history) {
            this.sequenceMap = new HashMap<>();
            var parsed = parseHistory(history);
            var historyQueue = new ArrayDeque<Long>(parsed.size() + 4);
            historyQueue.addAll(parsed);
            this.sequenceMap.put(HISTORY_KEY, historyQueue);
        }

        private List<Long> parseHistory(String history) {
            if (history == null || history.isBlank()) {
                throw new RuntimeException("The specified history is null or blank: " + history);
            }

            return Arrays.stream(history.split(VALUE_SPLIT))
                    .map(Long::parseLong)
                    .toList();
        }

        public Long predictNextValue() {
            var size = sequenceMap.size();

            if (size == 1) {
                System.out.println("Building sequence map...");
                buildSequenceMap();
                size = sequenceMap.size();
            }
            System.out.println("Calculating the next value in the history sequence...");
            Long nextValue = calculateNextValue((long) (size - 1));
            System.out.println("Next predicted value is: " + nextValue);
            return nextValue;
        }

        public Long predictPreviousValue() {
            var size = sequenceMap.size();

            if (size == 1) {
                System.out.println("Building previous sequence map...");
                buildSequenceMap();
                size = sequenceMap.size();
            }
            System.out.println("Calculating the previous value in the history sequence...");
            Long previousValue = calculatePreviousValue((long) (size - 1));
            System.out.println("Previous predicted value is: " + previousValue);
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

        private Long calculateNextValue(Long currentKey) {
            int size = sequenceMap.size();
            // stopping condition
            if (currentKey < 0 || size == 1) {
                // We are done, so simply return the last value from the HISTORY_KEY
                System.out.println("Next value has been calculated!");
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

        private Long calculatePreviousValue(Long currentKey) {
            int size = sequenceMap.size();
            // stopping condition
            if (currentKey < 0 || size == 1) {
                // We are done, so simply return the first value from the HISTORY_KEY
                System.out.println("Previous value has been calculated!");
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
    private static final String inputFilename = "inputs/input_day_09-01.txt";
    private static final String VALUE_SPLIT = " ";

    public static void main(String[] args) throws URISyntaxException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        var url = classloader.getResource(inputFilename);
        assert url != null;
        var path = Paths.get(url.toURI());
        List<History> histories = new ArrayList<>();
        try (var stream = Files.lines(path)) {
            System.out.println("Loading histories...");
            stream.forEach(line -> histories.add(new History(line)));
            System.out.println("Done loading histories!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Part 1
        System.out.println("Part 1: Start!");
        System.out.println("Predicting the next values for " + histories.size() + " histories...");
        var sumOfNextPredictions = histories.stream().mapToLong(History::predictNextValue).sum();
        System.out.println("The sum of the predicted values is: " + sumOfNextPredictions);

        // Part 2
        System.out.println("Part 2: Start!");
        System.out.println("Predicting the previous values for " + histories.size() + " histories...");
        var sumOfPreviousPredictions = histories.stream().mapToLong(History::predictPreviousValue).sum();
        System.out.println("The sum of the predicted values is: " + sumOfPreviousPredictions);
    }
}
