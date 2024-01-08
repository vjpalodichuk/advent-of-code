package com.capital7software.aoc2015.lib.graph.constaint;

import com.capital7software.aoc2015.lib.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public record CookieRecipe(List<Ingredient> ingredients) {
    public static final long MAX_ITERATIONS = 10_000_000L;
    public static final long CALORIES_TARGET = 500L;

    public record Ingredient(
            String name,
            long capacity,
            long durability,
            long flavor,
            long texture,
            long calories
    ) {}

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

    public Pair<Long, Map<Ingredient, Long>> getBestRecipe(boolean caloriesRestricted) {
        var solver = setupSolver(caloriesRestricted);

        var max = solver.max(MAX_ITERATIONS);

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
