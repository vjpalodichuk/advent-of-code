package com.capital7software.aoc.lib.analysis;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * As you walk, the Elf shows you a small bag and some cubes which are either red, green, or blue.
 * Each time you play this game, he will hide a secret number of cubes of each color in the bag,
 * and your goal is to figure out information about the number of cubes.
 * <p><br>
 * To get information, once a bag has been loaded with cubes, the Elf will reach into the bag,
 * grab a handful of random cubes, show them to you, and then put them back in the bag.
 * He'll do this a few times per game.
 * <p><br>
 * You play several games and record the information from each game (your puzzle input).
 * Each game is listed with its ID number (like the 11 in Game 11: ...) followed by a
 * semicolon-separated list of subsets of cubes that were revealed from the bag (like 3 red, 5 green, 4 blue).
 * <p><br>
 * For example, the record of a few games might look like this:
 * <ul>
 *     <li>
 *         Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
 *     </li>
 *     <li>
 *         Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
 *     </li>
 *     <li>
 *         Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
 *     </li>
 *     <li>
 *         Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
 *     </li>
 *     <li>
 *         Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
 *     </li>
 * </ul>
 * In game 1, three sets of cubes are revealed from the bag (and then put back again).
 * The first set is 3 blue cubes and 4 red cubes; the second set is 1 red cube, 2 green cubes,
 * and 6 blue cubes; the third set is only 2 green cubes.
 * <p><br>
 * The Elf would first like to know which games would have been possible if the bag contained
 * only 12 red cubes, 13 green cubes, and 14 blue cubes?
 * <p><br>
 * In the example above, games 1, 2, and 5 would have been possible if the bag had been
 * loaded with that configuration. However, game 3 would have been impossible because at
 * one point the Elf showed you 20 red cubes at once; similarly, game 4 would also have
 * been impossible because the Elf showed you 15 blue cubes at once. If you add up the IDs
 * of the games that would have been possible, you get 8.
 * <p><br>
 * Determine which games would have been possible if the bag had been loaded with only 12
 * red cubes, 13 green cubes, and 14 blue cubes. What is the sum of the IDs of those games?
 * <p><br>
 * Your puzzle answer was 2101.
 * <p><br>
 * --- Part Two ---<br>
 * The Elf says they've stopped producing snow because they aren't getting any water!
 * He isn't sure why the water stopped; however, he can show you how to get to the water
 * source to check it out for yourself. It's just up ahead!
 * <p><br>
 * As you continue your walk, the Elf poses a second question: in each game you played,
 * what is the fewest number of cubes of each color that could have been in the bag to make the game possible?
 * <p><br>
 * Again consider the example games from earlier:
 * <ul>
 *     <li>
 *         Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
 *     </li>
 *     <li>
 *         Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
 *     </li>
 *     <li>
 *         Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
 *     </li>
 *     <li>
 *         Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
 *     </li>
 *     <li>
 *         Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
 *     </li>
 * </ul>
 * <ul>
 *     <li>
 *         In game 1, the game could have been played with as few as 4 red, 2 green, and 6 blue cubes.
 *         If any color had even one fewer cube, the game would have been impossible.
 *     </li>
 *     <li>
 *         Game 2 could have been played with a minimum of 1 red, 3 green, and 4 blue cubes.
 *     </li>
 *     <li>
 *         Game 3 must have been played with at least 20 red, 13 green, and 6 blue cubes.
 *     </li>
 *     <li>
 *         Game 4 required at least 14 red, 3 green, and 15 blue cubes.
 *     </li>
 *     <li>
 *         Game 5 needed no fewer than 6 red, 3 green, and 2 blue cubes in the bag.
 *     </li>
 * </ul>
 * <b>The power of a set of cubes is equal to the numbers of red, green, and blue cubes multiplied together.</b>
 * The power of the minimum set of cubes in game 1 is 48. In games 2-5 it was 12, 1560, 630, and 36, respectively.
 * Adding up these five powers produces the sum 2286.
 * <p><br>
 * For each game, find the minimum set of cubes that must have been present. What is the sum of the power of these sets?
 */
public record CubeConundrum() {
    private static final String FIRST_SPLIT = ": ";
    private static final String GAME_SPLIT = "; ";
    private static final String RESULT_SPLIT = ", ";
    private static final String VALUE_SPLIT = " ";

    /**
     * A data class to hold the results of revealing the cubes.
     *
     * @param id The ID of the Reveal.
     * @param red How many Reds were revealed.
     * @param green How many Greens were revealed.
     * @param blue How many Blues were revealed.
     */
    public record Reveal(int id, int red, int green, int blue) {
    }

    /**
     * The result of playing a game of reveal the cubes with the elf.
     *
     * @param id The ID of this GameResult.
     * @param reveals The List of Reveals for this GameResult.
     */
    public record GameResult(int id, @NotNull List<Reveal> reveals) {

        /**
         * Instantiates a new GameResult with the specified ID and List of Reveals.
         *
         * @param id The ID of this GameResult.
         * @param reveals The List of Reveals for this GameResult.
         */
        public GameResult(int id, @NotNull List<Reveal> reveals) {
            this.id = id;
            this.reveals = new ArrayList<>(reveals);
        }

        /**
         * Instantiates a new GameResult with the specified ID and an empty List of Reveals.
         *
         * @param id The ID of this GameResult.
         */
        public GameResult(int id) {
            this(id, new ArrayList<>());
        }

        /**
         * Returns an unmodifiable copy of the Reveals of this GameResult.
         *
         * @return An unmodifiable copy of the Reveals of this GameResult.
         */
        public @NotNull List<Reveal> reveals() {
            return Collections.unmodifiableList(reveals);
        }

        /**
         * Returns the ID of this GameResult.
         *
         * @return The ID of this GameResult.
         */
        public int getId() {
            return id;
        }

        /**
         * Adds the specified Reveal to this GameResult.
         *
         * @param reveal The Reveal to add to this result.
         */
        public void addReveal(@NotNull Reveal reveal) {
            reveals.add(reveal);
        }

        /**
         * Add a new Reveal to this GameResult.
         *
         * @param id The ID of the Reveal.
         * @param red How many Reds were revealed.
         * @param green How many Greens were revealed.
         * @param blue How many Blues were revealed.
         */
        public void addReveal(int id, int red, int green, int blue) {
            addReveal(new Reveal(id, red, green, blue));
        }

        /**
         * Returns the product of the minimum number of cubes required to play this game.
         *
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
         * Returns the minimum number of each cube color required to play this game.
         *
         * @return The minimum number of each cube color required to play this game.
         */
        public @NotNull Reveal findMinimumCubes() {
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
         * Returns true if the set of cubes contain only 12 red cubes, 13 green cubes, and 14 blue cubes.
         *
         * @return True if the set of cubes contain only 12 red cubes, 13 green cubes, and 14 blue cubes.
         */
        public boolean isPossible() {
            if (reveals.isEmpty()) {
                throw new RuntimeException("At least one reveal is required to find the minimum cubes of this game");
            }

            return reveals.stream().allMatch(it -> it.red() <= 12 && it.green() <= 13 && it.blue() <= 14);
        }
    }

    /**
     * Parses the specified List of game inputs and returns a List of GameResult for the input.
     *
     * @param input The String version of the game input.
     * @return A List of GameResult for the input.
     */
    public @NotNull List<GameResult> getGameResults(@NotNull List<String> input) {
        return input.stream().map(this::processGameInput).filter(Objects::nonNull).toList();
    }

    private GameResult processGameInput(String input) {
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
