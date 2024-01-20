package com.capital7software.aoc.lib.graph.constaint;

import com.capital7software.aoc.lib.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * The Solver is designed to easily solve complex constraint based linear programming problems.<br>
 * Add Unknown and set a ValueDomain that controls which values can be assigned to the Unknowns.<br>
 * Add Variables by defining functions that utilize the Unknowns to produce some kind of value.<br>
 * Add Constraints that validate the values assigned to the Unknowns and those produced by the Variables.<br>
 * Set a Score function that uses the Unknown values and / or Variables and produces a score.<br>
 * Finally, call the min or max methods with the specified maximum number of iterations to produce a result.<br>
 * <p>
 * Please note that the number of iterations required in order to produce the actual minimum or maximum is
 * implementation dependent.<br>
 * <p>
 * Please see the CookieRecipe for an capital7software on how to use the Solver interface.
 *
 * @param <T> The type of the value used and produced by the Solver, Unknowns, Variables, and Score.
 */
public interface Solver<T extends Number & Comparable<T>> {
    /**
     * Adds an Unknown with the specified ID. Typically, this will be the identifier for a value you are trying to
     * solve for.<br><br>For example with the CookieRecipe, the Unknown IDs are the names of the Ingredients.
     *
     * @param id The ID of the Unknown.
     */
    void addUnknown(@NotNull String id);

    /**
     * Sets the ValueDomain to be used with this Solver instance. The ValueDomain is called to provide
     * the values to get assigned to the Unknowns. Unbound ValueDomains should be avoided whenever possible.<br><br>
     * For example with the CookieRecipe, it uses a LongRangedValueDomain whose values are limited to the inclusive
     * range of 0 - 100.
     *
     * @param valueDomain The instance of the ValueDomain to use for this Solver instance.
     */
    void setValueDomain(@NotNull ValueDomain<T> valueDomain);

    /**
     * Adds a Variable with the specified ID. Typically, the ID will be the identifier for a variable that is used
     * in the Score function and the variable itself will be some kind of product or summation or both.<br><br>
     * For example with the CookieRecipe, the Variable IDs are names like totalCapacity and totalDurability and
     * they sum up the results of multiplying the capacity and durability of each Ingredient by the Unknown values
     * assigned to each of the Ingredients.
     *
     * @param id       The unique ID of the variable.
     * @param variable The Function that accepts a Map containing the Unknowns and their current values and produces
     *                 a new value that is stored by the specified ID for later use.
     */
    void addVariable(@NotNull String id, @NotNull Function<Map<String, T>, T> variable);

    /**
     * A constraint is simply a Predicate that returns false if the values assigned to the Unknowns or produced
     * by the Variables is unacceptable for use in calculating a valid Score. The ID of the constraint
     * typically describes what the constraint does.<br><br>For example with the CookieRecipe one constraint is
     * named aboveZero which ensures that all Variables have produced values that are greater than zero.
     * <br><br>The first parameter passed
     * to the Predicate is a map of the values currently assigned to the Unknowns and the second parameter is
     * a map of the values currently produced by the Variables.<br><br>
     * Any number of constraints can be added to the Solver instance. If any constraint returns false, then
     * how that is handled is implementation dependent.<br><br>
     * For example with the CookieRecipe, it uses the SimpleSolver which will abandon the current Unknowns and
     * request new values for all the Unknowns and then try again.
     *
     * @param id         THe unique ID of the constraint being added.
     * @param constraint The Predicate function that returns true if the constraint is satisfied and false if not.
     */
    void addConstraint(@NotNull String id, @NotNull BiPredicate<Map<String, T>, Map<String, T>> constraint);

    /**
     * The Score function is called only if all constraints have been satisfied (return true). The Score function
     * produces a value that is then used by min or max to determine the minimum or maximum score.<br><br>
     * For example with the CookieRecipe, the Score function is simply the product of all the Variables except
     * for the calories Variable.
     * <br><br>The first parameter passed to the Score function is a map of the values currently assigned to
     * the Unknowns and the second parameter is a map of the values currently produced by the Variables.<br><br>
     *
     * @param score The BiFunction that produces a score that the min and max methods use to either find the
     *              minimum or maximum score respectively.
     */
    void setScoreFunction(@NotNull BiFunction<Map<String, T>, Map<String, T>, T> score);

    /**
     * Finds the maximum Score iterating a maximum of maxIterations. The return value is a Pair where the first
     * property of the Pair is the maximum score and the second is a Map with the values of the Unknowns that were
     * used to produce the maximum score.<br><br>Please note that it is implementation dependent on whether
     * the returned maximum is guaranteed to be the actual maximum of the problem.
     *
     * @param maxIterations The maximum number of iterations to perform searching for the maximum value. Please note
     *                      that it is implementation dependent if the method will actually iterate upto maxIterations.
     * @return a Pair where the first property of the Pair is the maximum score and the second is a
     * Map with the values of the Unknowns that were used to produce the maximum score.
     * @throws IllegalStateException If unable to calculate a result.
     */
    @NotNull
    Pair<T, Map<String, T>> max(long maxIterations) throws IllegalStateException;

    /**
     * Finds the minimum Score iterating a maximum of maxIterations. The return value is a Pair where the first
     * property of the Pair is the minimum score and the second is a Map with the values of the Unknowns that were
     * used to produce the minimum score.<br><br>Please note that it is implementation dependent on whether
     * the returned minimum is guaranteed to be the actual minimum of the problem.
     *
     * @param maxIterations The maximum number of iterations to perform searching for the maximum value. Please note
     *                      that it is implementation dependent if the method will actually iterate upto maxIterations.
     * @return a Pair where the first property of the Pair is the minimum score and the second is a
     * Map with the values of the Unknowns that were used to produce the minimum score.
     * @throws IllegalStateException If unable to calculate a result.
     */
    @NotNull
    Pair<T, Map<String, T>> min(long maxIterations) throws IllegalStateException;
}
