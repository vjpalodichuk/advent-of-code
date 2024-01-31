package com.capital7software.aoc.aoc2015.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.graph.constraint.CookieRecipe;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * --- Day 15: Science for Hungry People ---<br><br>
 * Today, you set out on the task of perfecting your milk-dunking cookie recipe.
 * All you have to do is find the right balance of ingredients.
 *
 * <p><br>
 * Your recipe leaves room for exactly 100 teaspoons of ingredients. You make a
 * list of the remaining ingredients you could use to finish the
 * recipe (your puzzle input) and their properties per teaspoon:
 * <ul>
 *     <li>
 *         capacity (how well it helps the cookie absorb milk)
 *     </li>
 *     <li>
 *         durability (how well it keeps the cookie intact when full of milk)
 *     </li>
 *     <li>
 *         flavor (how tasty it makes the cookie)
 *     </li>
 *     <li>
 *         texture (how it improves the feel of the cookie)
 *     </li>
 *     <li>
 *         calories (how many calories it adds to the cookie)
 *     </li>
 *     <li>
 *         You can only measure ingredients in whole-teaspoon amounts accurately,
 *         and you have to be accurate, so you can reproduce your results in the future.
 *     </li>
 *     <li>
 *         The total score of a cookie can be found by adding up each of the properties
 *         (negative totals become 0) and then multiplying together everything except calories.
 *     </li>
 * </ul>
 * For instance, suppose you have these two ingredients:
 * <ul>
 *     <li>
 *         Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8
 *     </li>
 *     <li>
 *         Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3
 *     </li>
 * </ul>
 * Then, choosing to use 44 teaspoons of butterscotch and 56 teaspoons of
 * cinnamon (because the amounts of each ingredient must add up to 100) would
 * result in a cookie with the following properties:
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
 * Multiplying these together (68 * 80 * 152 * 76, ignoring calories for now)
 * results in a total score of 62842880, which happens to be the best score
 * possible given these ingredients. If any properties had produced a negative
 * total, it would have instead become zero, causing the whole score to multiply to zero.
 *
 * <p><br>
 * Given the ingredients in your kitchen and their properties, what is the total
 * score of the highest-scoring cookie you can make?
 *
 * <p><br>
 * Your puzzle answer was 222870.
 *
 * <p><br>
 * --- Part Two ---<br><br>
 * Your cookie recipe becomes wildly popular! Someone asks if you can make another recipe that
 * has exactly 500 calories per cookie (so they can use it as a meal replacement). Keep the rest
 * of your award-winning process the same (100 teaspoons, same ingredients, same scoring system).
 *
 * <p><br>
 * For example, given the ingredients above, if you had instead selected 40 teaspoons of
 * butterscotch and 60 teaspoons of cinnamon (which still adds to 100), the total calorie count
 * would be 40*8 + 60*3 = 500. The total score would go down, though: only 57600000, the best
 * you can do in such trying circumstances.
 *
 * <p><br>
 * Given the ingredients in your kitchen and their properties, what is the total score of the
 * highest-scoring cookie you can make with a calorie total of 500?
 *
 * <p><br>
 * Your puzzle answer was 117936.
 */
public class Day15 implements AdventOfCodeSolution {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day15.class);

  /**
   * Instantiates the solution instance.
   */
  public Day15() {

  }

  @Override
  public String getDefaultInputFilename() {
    return "inputs/input_day_15-01.txt";
  }

  @Override
  public void runPart1(List<String> input) {
    var start = Instant.now();
    var max = whatIsTheTotalScoreOfTheHighestScoringCookie(input, CookieRecipe.MAX_ITERATIONS);
    var end = Instant.now();
    LOGGER.info("Total score of the highest scoring cookie is: {}", max);
    logTimings(LOGGER, start, end);
  }

  @Override
  public void runPart2(List<String> input) {
    var start = Instant.now();
    var max = whatIsTheTotalScoreOfTheHighestScoringCaloriesRestrictedCookie(
        input,
        CookieRecipe.MAX_ITERATIONS
    );
    var end = Instant.now();
    LOGGER.info("Total score of the highest scoring calorie restricted cookie is: {}", max);
    logTimings(LOGGER, start, end);
  }

  /**
   * The CookieRecipe uses the Solver to determine the best mix of ingredients to get the
   * highest score. Returns the total score of the highest scoring cookie.
   *
   * @param input      The available ingredients for the recipe.
   * @param iterations How many iterations to let the Solver run to find the best mix.
   * @return The total score of the highest scoring cookie.
   */
  public long whatIsTheTotalScoreOfTheHighestScoringCookie(List<String> input, long iterations) {
    var cookieRecipe = CookieRecipe.parse(input);

    var max = cookieRecipe.getBestRecipe(false, iterations);

    return max.first();
  }

  /**
   * The CookieRecipe uses the Solver to determine the best mix of ingredients to get the
   * highest score for a 500 calorie cookie. Returns the total score of the highest scoring
   * 500 calorie cookie.
   *
   * @param input      The available ingredients for the recipe.
   * @param iterations How many iterations to let the Solver run to find the best mix.
   * @return The total score of the highest scoring 500 calorie cookie.
   */
  public long whatIsTheTotalScoreOfTheHighestScoringCaloriesRestrictedCookie(
      List<String> input,
      long iterations
  ) {
    var cookieRecipe = CookieRecipe.parse(input);

    var max = cookieRecipe.getBestRecipe(true, iterations);

    return max.first();
  }
}
