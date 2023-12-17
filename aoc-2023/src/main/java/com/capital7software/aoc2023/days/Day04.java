package com.capital7software.aoc2023.days;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class Day04 {
    private record ScratchCard(
            int id,
            Set<Integer> winningNumbers,
            Set<Integer> cardNumbers
    ) {
        public int calculatePoints() {
            var intersection = getWinners();

            var count = intersection.size();
            var points = 0;

            if (count > 0) {
                points = (int)Math.pow(2, count - 1);
            }

            return points;
        }

        public Set<Integer> getWinners() {
            var intersection = new HashSet<>(winningNumbers);
            intersection.retainAll(cardNumbers);
            return intersection;
        }

        @Override
        public String toString() {
            return "ScratchCard{" +
                    "id=" + id +
                    ", points=" + calculatePoints() +
                    ", winners=" + getWinners() +
                    ", winningNumbers=" + winningNumbers +
                    ", cardNumbers=" + cardNumbers +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ScratchCard that = (ScratchCard) o;
            return id == that.id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }

    private static final String inputFilename = "inputs/input_day_04-01.txt";

    private static final String FIRST_SPLIT = ": ";
    private static final String NUMBERS_SPLIT = " \\| ";
    private static final String VALUE_SPLIT = " ";

    public static void main(String[] args) throws URISyntaxException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        var url = classloader.getResource(inputFilename);
        assert url != null;
        var path = Paths.get(url.toURI());

        try (var stream = Files.lines(path)) {
            var results = new ArrayList<ScratchCard>();
            stream.forEach(it -> {
                // Process the line of input
                var result = convertToScratchCard(it);
                if (result != null) {
                    results.add(result);
                } else {
                    System.out.println("Error processing scratch card input: " + it);
                }
            });

            System.out.println("Processed " + results.size() + " scratch cards");

            results.forEach(System.out::println);

            var sum = results
                    .stream()
                    .mapToInt(ScratchCard::calculatePoints)
                    .sum();

            System.out.println("Summation of all card points is: " + sum);

            System.out.println("Calculating the total number of cards and generating copies...");
            var copies = new ArrayList<ScratchCard>();

            var start = Instant.now();
            results.forEach(it -> copies.addAll(processWinners(it, results)));
            var copyCount = copies.size();
            var end = Instant.now();

            System.out.println("Calculated " + copyCount + " 'Copies' via brute force in: " + Duration.between(start, end).toMillis() + " ms");
            System.out.println("Total number of cards (original + copies): " + (results.size() + copyCount));

            System.out.println("Calculating the total number of copies and their winners...");
            var copyCount2 = 0;
            var cardMap = new HashMap<Integer, Integer>();

            start = Instant.now();
            for (var it:results) {
                copyCount2 += getTotalCopyCount(it, results, cardMap);
            }
            end = Instant.now();
            System.out.println("Calculated " + copyCount2 + " 'Copies' via optimized method in: " + Duration.between(start, end).toMillis() + " ms");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<ScratchCard> processWinners(ScratchCard card, List<ScratchCard> results) {
        int size = card.getWinners().size();
        if (size == 0) {
            return new ArrayList<>();
        }

        var temp = new ArrayList<ScratchCard>();

        for (int i = card.id(); i < card.id() + size; i++) {
            temp.add(results.get(i));
            temp.addAll(processWinners(results.get(i), results));
        }

        return temp;
    }

    private static int getTotalCopyCount(ScratchCard card, List<ScratchCard> cards, Map<Integer, Integer> cardMap) {
        int size = cardMap.computeIfAbsent(card.id(), it -> card.getWinners().size());

        if (size == 0) {
            return 0;
        }

        var winCount = 0;

        for (int i = card.id(); i < card.id() + size; i++) {
            winCount++;
            winCount += getTotalCopyCount(cards.get(i), cards, cardMap);
        }

        return winCount;
    }

    private static ScratchCard convertToScratchCard(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }

        String[] initialSplit = input.split(FIRST_SPLIT);

        if (initialSplit.length != 2) {
            throw new RuntimeException("Got more than two results from the initial split!");
        }

        var cardId = getCardId(initialSplit[0]);

        var numberSplits = initialSplit[1].split(NUMBERS_SPLIT);

        var winningNumbers = getNumbers(numberSplits[0]);
        var cardNumbers = getNumbers(numberSplits[1]);

        return new ScratchCard(cardId, winningNumbers, cardNumbers);
    }

    private static Set<Integer> getNumbers(String input) {
        var splits = input.split(VALUE_SPLIT);

        return Arrays.stream(splits)
                .filter(it -> !it.trim().isEmpty())
                .map(Integer::parseInt)
                .collect(Collectors.toSet());
    }

    private static int getCardId(String input) {
        var splits = input.split(VALUE_SPLIT);

        var filtered = Arrays.stream(splits)
                .filter(it -> !it.trim().isEmpty())
                .toList();

        if (filtered.size() != 2) {
            throw new RuntimeException("Got more than two results from the card id split!");
        }

        return Integer.parseInt(filtered.get(1));
    }
}
