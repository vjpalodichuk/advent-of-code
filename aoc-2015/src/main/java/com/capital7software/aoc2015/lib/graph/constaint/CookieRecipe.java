package com.capital7software.aoc2015.lib.graph.constaint;

import com.capital7software.aoc2015.lib.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * You set out on the task of perfecting your milk-dunking cookie recipe.
 * All you have to do is find the right balance of ingredients.
 * <p>
 * Your recipe leaves room for exactly 100 teaspoons of ingredients.
 * You make a list of the remaining ingredients you could use to finish the recipe
 * and their properties per teaspoon:<br><br>
 * <p>
 * capacity (how well it helps the cookie absorb milk)<br>
 * durability (how well it keeps the cookie intact when full of milk)<br>
 * flavor (how tasty it makes the cookie)<br>
 * texture (how it improves the feel of the cookie)<br>
 * calories (how many calories it adds to the cookie)<br><br>
 * You can only measure ingredients in whole-teaspoon amounts accurately,
 * and you have to be accurate, so you can reproduce your results in the future.<br> <br>
 * The total score of a cookie can be found by adding up each of the properties
 * (negative totals become 0) and then multiplying together everything except calories.
 * <p><br>
 * For instance, suppose you have these two ingredients:
 * <p>
 * Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8<br>
 * Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3<br><br>
 * Then, choosing to use 44 teaspoons of butterscotch and 56 teaspoons of cinnamon
 * (because the amounts of each ingredient must add up to 100) would result in a cookie with the following properties:
 * <p>
 * A capacity of 44*-1 + 56*2 = 68<br>
 * A durability of 44*-2 + 56*3 = 80<br>
 * A flavor of 44*6 + 56*-2 = 152<br>
 * A texture of 44*3 + 56*-1 = 76<br><br>
 * Multiplying these together (68 * 80 * 152 * 76, ignoring calories for now)<br>
 * results in a total score of 62842880, which happens to be the best score possible given these ingredients.<br><br>
 * If any properties had produced a negative total,
 * it would have instead become zero, causing the whole score to multiply to zero.
 *
 * @param ingredients The list of available Ingredients.
 */
public record CookieRecipe(List<Ingredient> ingredients) {
    public static final long MAX_ITERATIONS = 10_000_000L;
    public static final long CALORIES_TARGET = 500L;

    /**
     * A simple data class for holding the properties of an Ingredient.
     *
     * @param name       The name of the Ingredient.
     * @param capacity   How well it helps the cookie absorb milk.
     * @param durability How well it keeps the cookie intact when full of milk.
     * @param flavor     How tasty it makes the cookie.
     * @param texture    How it improves the feel of the cookie.
     * @param calories   How many calories it adds to the cookie.
     */
    public record Ingredient(
            String name,
            long capacity,
            long durability,
            long flavor,
            long texture,
            long calories
    ) {
    }

    /**
     * Parses a list of Ingredients and returns a new CookieRecipe instance with the list of parsed Ingredients.
     * <br><br>
     * Parses Ingredients in the following format:<br>
     * <ul>
     *     <li>
     *         Sugar: capacity 3, durability 0, flavor 0, texture -3, calories 2
     *     </li>
     *     <li>
     *         Sprinkles: capacity -3, durability 3, flavor 0, texture 0, calories 9
     *     </li>
     * </ul>
     *
     * @param input The list of Ingredients to parse.
     * @return A new CookieRecipe instance with the list of parsed Ingredients.
     */
    public static CookieRecipe parse(List<String> input) {
        return new CookieRecipe(input.stream().map(CookieRecipe::parseLine).filter(Objects::nonNull).toList());
    }

    private static Ingredient parseLine(String line) {
        if (line == null || line.isBlank()) {
            return null;
        }

        var split = line.split(": ");
        var name = split[0].trim();

        var properties = split[1].split(", ");
        var capacity = 0L;
        var durability = 0L;
        var flavor = 0L;
        var texture = 0L;
        var calories = 0L;

        for (var property : properties) {
            var split2 = property.split(" ");

            var value = Long.parseLong(split2[1].trim());

            switch (split2[0].trim()) {
                case "capacity" -> capacity = value;
                case "durability" -> durability = value;
                case "flavor" -> flavor = value;
                case "texture" -> texture = value;
                case "calories" -> calories = value;
                default -> throw new IllegalArgumentException("Unknown ingredient property:" + split2[0].trim());
            }
        }

        return new Ingredient(name, capacity, durability, flavor, texture, calories);
    }

    /**
     * Returns the best scoring recipe as a Pair where the first property is the score and the second is a
     * Map of the ingredients used the number of teaspoons used of the ingredients.
     *
     * @param caloriesRestricted Set to true if the best cookie has to contain exactly 500 calories.
     * @param maxIterations The maximum number of iterations to perform.
     * @return The best scoring recipe as a Pair where the first property is the score and the second is a
     * Map of the ingredients used the number of teaspoons used of the ingredients.
     */
    public Pair<Long, Map<Ingredient, Long>> getBestRecipe(boolean caloriesRestricted, long maxIterations) {
        var solver = setupSolver(caloriesRestricted);

        var max = solver.max(maxIterations);

        return new Pair<>(
                max.first(),
                ingredients.stream()
                        .collect(Collectors.toMap(Function.identity(), it -> max.second().get(it.name()))));
    }

    private Solver<Long> setupSolver(boolean includeCalories) {
        var solver = new SimpleSolver<Long>();
        var domain = new LongRangedValueDomain(0, 100);

        // Set the domain provider
        solver.setValueDomain(domain);
        // Add the ingredients
        ingredients.forEach(ingredient -> solver.addUnknown(ingredient.name()));
        // Add our variables
        solver.addVariable("capacity", (unknowns) -> Math.max(0, ingredients.stream()
                .mapToLong(ingredient -> unknowns.get(ingredient.name()) * ingredient.capacity())
                .sum()));

        solver.addVariable("durability", (unknowns) -> Math.max(0, ingredients.stream()
                .mapToLong(ingredient -> unknowns.get(ingredient.name()) * ingredient.durability())
                .sum()));

        solver.addVariable("flavor", (unknowns) -> Math.max(0, ingredients.stream()
                .mapToLong(ingredient -> unknowns.get(ingredient.name()) * ingredient.flavor())
                .sum()));

        solver.addVariable("texture", (unknowns) -> Math.max(0, ingredients.stream()
                .mapToLong(ingredient -> unknowns.get(ingredient.name()) * ingredient.texture())
                .sum()));

        if (includeCalories) {
            solver.addVariable("calories", (unknowns) -> Math.max(0, ingredients.stream()
                    .mapToLong(ingredient -> unknowns.get(ingredient.name()) * ingredient.calories())
                    .sum()));
        }
        // Add some constraints
        solver.addConstraint("withinRange", (unknowns, variables) ->
                unknowns.values().stream().mapToLong(it -> it).sum() == domain.getMaximum());
        solver.addConstraint("aboveZero", (unknowns, variables) ->
                variables.values().stream().allMatch(it -> it > 0));

        if (includeCalories) {
            solver.addConstraint("calories", (unknowns, variables) -> variables.get("calories") == CALORIES_TARGET);
        }

        solver.setScoreFunction((unknowns, variables) ->
                variables.get("capacity") *
                        variables.get("durability") *
                        variables.get("flavor") *
                        variables.get("texture")
        );

        return solver;
    }
}
