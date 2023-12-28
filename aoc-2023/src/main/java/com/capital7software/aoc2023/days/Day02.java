package com.capital7software.aoc2023.days;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day02 {
    private static class GameResult {
        private record Reveal(int id, int red, int green, int blue) {
        }

        private final int id;
        private final List<Reveal> reveals;

        public GameResult(int id) {
            this.id = id;
            this.reveals = new ArrayList<>();
        }

        public int getId() {
            return id;
        }

        public List<Reveal> getReveals() {
            return reveals;
        }

        public void addReveal(Reveal reveal) {
            reveals.add(reveal);
        }

        public void addReveal(int id, int red, int green, int blue) {
            addReveal(new Reveal(id, red, green, blue));
        }

        /**
         * @return The product of the minimum number of cubes required to play this game.
         */
        public int power() {
            if (reveals.isEmpty()) {
                throw new RuntimeException("At least one reveal is required to calculate the power of this game");
            }

            var minimum = findMinimumCubes();

            return minimum.red() * minimum.green() * minimum.blue();
        }

        /**
         * @return The minimum number of each cube color required to play this game.
         */
        public Reveal findMinimumCubes() {
            if (reveals.isEmpty()) {
                throw new RuntimeException("At least one reveal is required to find the minimum cubes of this game");
            }

            int red = 0, green = 0, blue = 0;

            for (Reveal reveal : reveals) {
                red = Integer.max(red, reveal.red());
                green = Integer.max(green, reveal.green());
                blue = Integer.max(blue, reveal.blue());
            }
            return new Reveal(0, red, green, blue);
        }

        /**
         * @return True if the set of cubes contain only 12 red cubes, 13 green cubes, and 14 blue cubes
         */
        public boolean isPossible() {
            if (reveals.isEmpty()) {
                throw new RuntimeException("At least one reveal is required to find the minimum cubes of this game");
            }

            return reveals.stream().allMatch(it -> it.red() <= 12 && it.green() <= 13 && it.blue() <= 14);
        }

        @Override
        public String toString() {
            return "GameResult{" +
                    "id=" + id +
                    ", reveals=" + reveals +
                    '}';
        }
    }

    private static final String inputFilename = "inputs/input_day_02-01.txt";
    private static final String FIRST_SPLIT = ": ";
    private static final String GAME_SPLIT = "; ";
    private static final String RESULT_SPLIT = ", ";
    private static final String VALUE_SPLIT = " ";

    public static void main(String[] args) throws URISyntaxException {
        // Open the input file and get a handle to the input stream
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        var url = classloader.getResource(inputFilename);
        assert url != null;
        var path = Paths.get(url.toURI());

        try (var stream = Files.lines(path)) {
            var results = new ArrayList<GameResult>();

            stream.forEach(it -> {
                // Process the line of input
                var result = processGameInput(it);
                if (result != null) {
                    results.add(result);
                } else {
                    System.out.println("Error processing game input: " + it);
                }
            });

            System.out.println("Processed " + results.size() + " games");

            results.forEach(System.out::println);

            var sum = results
                    .stream()
                    .filter(GameResult::isPossible)
                    .mapToInt(GameResult::getId)
                    .sum();

            System.out.println("Summation of all possible game ids is: " + sum);

            var powerSum = results
                    .stream()
                    .mapToInt(GameResult::power)
                    .sum();

            System.out.println("Summation of the power of all games is: " + powerSum);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static GameResult processGameInput(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }

        String[] initialSplit = input.split(FIRST_SPLIT);

        if (initialSplit.length != 2) {
            throw new RuntimeException("Got more than two results from the initial split!");
        }

        int gameId = Integer.parseInt(initialSplit[0].split(VALUE_SPLIT)[1]);

        var reveals = initialSplit[1].split(GAME_SPLIT);

        var result = new GameResult(gameId);

        for (int i = 0; i < reveals.length; i++) {
            // Split each reveal into its individual results
            var resultSplit = reveals[i].split(RESULT_SPLIT);
            int red = 0, green = 0, blue = 0;

            for (String s : resultSplit) {
                var valueSplit = s.split(VALUE_SPLIT);

                if (valueSplit[1].equalsIgnoreCase("red")) {
                    red = Integer.parseInt(valueSplit[0]);
                } else if (valueSplit[1].equalsIgnoreCase("green")) {
                    green = Integer.parseInt(valueSplit[0]);
                } else if (valueSplit[1].equalsIgnoreCase("blue")) {
                    blue = Integer.parseInt(valueSplit[0]);
                } else {
                    throw new RuntimeException("Unrecognized value! " + valueSplit[1]);
                }
            }
            result.addReveal(i + 1, red, green, blue);
        }

        return result;
    }
}
