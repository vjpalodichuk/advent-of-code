package com.capital7software.aoc2015.days;

import com.capital7software.aoc2015.lib.AdventOfCodeSolution;

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
 * and you have to be accurate so you can reproduce your results in the future.
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
 * Your puzzle answer was .
 * <p>
 * <p>
 * Your puzzle answer was .
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
        var max = whatIsTheTotalScoreOfTheHighestScoringCookie(input);
        var end = Instant.now();
        System.out.printf("Total score of the highest scoring cookie is: %d%n", max);
        printTiming(start, end);
    }

    public long whatIsTheTotalScoreOfTheHighestScoringCookie(List<String> input) {
        return 0;
    }
}
