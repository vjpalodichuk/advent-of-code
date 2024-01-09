package com.capital7software.aoc2015.days;

import com.capital7software.aoc2015.lib.AdventOfCodeSolution;
import com.capital7software.aoc2015.lib.graph.constaint.CookieRecipe;

import java.time.Instant;
import java.util.List;

/**
 * --- Day 15: Science for Hungry People ---
 * Today, you set out on the task of perfecting your milk-dunking cookie recipe.
 * All you have to do is find the right balance of ingredients.
 * <p>
 * Your recipe leaves room for exactly 100 teaspoons of ingredients. You make a
 * list of the remaining ingredients you could use to finish the
 * recipe (your puzzle input) and their properties per teaspoon:
 * <p>
 * capacity (how well it helps the cookie absorb milk)
 * durability (how well it keeps the cookie intact when full of milk)
 * flavor (how tasty it makes the cookie)
 * texture (how it improves the feel of the cookie)
 * calories (how many calories it adds to the cookie)
 * You can only measure ingredients in whole-teaspoon amounts accurately,
 * and you have to be accurate, so you can reproduce your results in the future.
 * The total score of a cookie can be found by adding up each of the properties
 * (negative totals become 0) and then multiplying together everything except calories.
 * <p>
 * For instance, suppose you have these two ingredients:
 * <p>
 * <ul>
 *     <li>
 *         Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8
 *     </li>
 *     <li>
 *         Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3
 *     </li>
 * </ul>
 * <br>
 * Then, choosing to use 44 teaspoons of butterscotch and 56 teaspoons of
 * cinnamon (because the amounts of each ingredient must add up to 100) would
 * result in a cookie with the following properties:
 * <p>
 * <ul>
 *     <li>
 *         A capacity of 44*-1 + 56*2 = 68
 *     </li>
 *     <li>
 *         A durability of 44*-2 + 56*3 = 80
 *     </li>
 *     <li>
 *         A flavor of 44*6 + 56*-2 = 152
 *     </li>
 *     <li>
 *         A texture of 44*3 + 56*-1 = 76
 *     </li>
 * </ul>
 * <br>
 * Multiplying these together (68 * 80 * 152 * 76, ignoring calories for now)
 * results in a total score of 62842880, which happens to be the best score
 * possible given these ingredients. If any properties had produced a negative
 * total, it would have instead become zero, causing the whole score to multiply to zero.
 * <p>
 * Given the ingredients in your kitchen and their properties, what is the total
 * score of the highest-scoring cookie you can make?
 * <p>
 * Your puzzle answer was 222870.
 * <p>
 * --- Part Two ---
 * Your cookie recipe becomes wildly popular! Someone asks if you can make another recipe that has exactly
 * 500 calories per cookie (so they can use it as a meal replacement). Keep the rest of your award-winning
 * process the same (100 teaspoons, same ingredients, same scoring system).
 * <p>
 * For example, given the ingredients above, if you had instead selected 40 teaspoons of butterscotch and
 * 60 teaspoons of cinnamon (which still adds to 100), the total calorie count would be 40*8 + 60*3 = 500.
 * The total score would go down, though: only 57600000, the best you can do in such trying circumstances.
 * <p>
 * Given the ingredients in your kitchen and their properties, what is the total score of the highest-scoring
 * cookie you can make with a calorie total of 500?
 * <p>
 * Your puzzle answer was 117936.
 * <p>
 */
public class Day15 implements AdventOfCodeSolution {
    @Override
    public String getDefaultInputFilename() {
        return "inputs/input_day_15-01.txt";
    }

    @Override
    public void runPart1(List<String> input) {
        var start = Instant.now();
        var max = whatIsTheTotalScoreOfTheHighestScoringCookie(input, CookieRecipe.MAX_ITERATIONS);
        var end = Instant.now();
        System.out.printf("Total score of the highest scoring cookie is: %d%n", max);
        printTiming(start, end);
    }

    @Override
    public void runPart2(List<String> input) {
        var start = Instant.now();
        var max = whatIsTheTotalScoreOfTheHighestScoringCaloriesRestrictedCookie(input, CookieRecipe.MAX_ITERATIONS);
        var end = Instant.now();
        System.out.printf("Total score of the highest scoring calorie restricted cookie is: %d%n", max);
        printTiming(start, end);
    }

    public long whatIsTheTotalScoreOfTheHighestScoringCookie(List<String> input, long iterations) {
        var cookieRecipe = CookieRecipe.parse(input);

        var max = cookieRecipe.getBestRecipe(false, iterations);

        return max.first();
    }

    public long whatIsTheTotalScoreOfTheHighestScoringCaloriesRestrictedCookie(List<String> input, long iterations) {
        var cookieRecipe = CookieRecipe.parse(input);

        var max = cookieRecipe.getBestRecipe(true, iterations);

        return max.first();
    }
}
