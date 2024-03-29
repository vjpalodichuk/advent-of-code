package com.capital7software.aoc.lib.graph.constraint;

import com.capital7software.aoc.lib.util.Pair;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;

/**
 * The SimpleSolver is a simple and straightforward implementation of the Solver interface.<br>
 * This Solver uses Random values generated from the ValueDomain and with each iteration a
 * new set of Random values is assigned. A more complex implementation would try to improve the
 * score of the current assignment prior to getting all new Random values. That is left as an
 * exercise for later!
 * <br><br>
 * <b>Because this Solver always gets a new set of Random values for the Unknowns a high number of
 * iterations should be specified to ensure that the actual minimum or maximum value is found.</b>
 * <br>
 *
 * @param <T> The type of the value used and produced by this Solver instance.
 */
public class SimpleSolver<T extends Number & Comparable<T>> implements Solver<T> {
  private final Map<String, T> unknowns;
  private ValueDomain<T> valueDomain;
  private final Map<String, Function<Map<String, T>, T>> variables;
  private final Map<String, BiPredicate<Map<String, T>, Map<String, T>>> constraints;
  private BiFunction<Map<String, T>, Map<String, T>, T> score;

  /**
   * Instantiates a new SimpleSolver.
   */
  public SimpleSolver() {
    this.unknowns = new HashMap<>();
    this.valueDomain = null;
    this.variables = new HashMap<>();
    this.constraints = new HashMap<>();
    this.score = null;
  }

  @Override
  public void addUnknown(@NotNull String id) {
    unknowns.put(id, null);
  }

  @Override
  public void setValueDomain(@NotNull ValueDomain<T> valueDomain) {
    this.valueDomain = valueDomain;
  }

  @Override
  public void addVariable(@NotNull String id, @NotNull Function<Map<String, T>, T> variable) {
    variables.putIfAbsent(id, variable);
  }

  @Override
  public void addConstraint(
      @NotNull String id,
      @NotNull BiPredicate<Map<String, T>, Map<String, T>> constraint
  ) {
    constraints.putIfAbsent(id, constraint);
  }

  @Override
  public void setScoreFunction(@NotNull BiFunction<Map<String, T>, Map<String, T>, T> score) {
    this.score = score;
  }

  @Override
  public @NotNull Pair<T, Map<String, T>> max(long maxIterations) throws IllegalStateException {
    return calculate(maxIterations, true);
  }

  @Override
  public @NotNull Pair<T, Map<String, T>> min(long maxIterations) throws IllegalStateException {
    return calculate(maxIterations, false);
  }

  private void ensureValid(long maxIterations) {
    if (score == null) {
      throw new IllegalStateException("The score function must be set before calling!");
    }
    if (valueDomain == null) {
      throw new IllegalStateException("The valueDomain must be set before calling!");
    }
    if (unknowns.isEmpty()) {
      throw new IllegalStateException("At least one unknown must be added before calling!");
    }
    if (maxIterations <= 0) {
      throw new IllegalArgumentException("maxIterations must be a positive number");
    }
  }

  private @NotNull Pair<T, Map<String, T>> calculate(long maxIterations, boolean maxCalc) {
    ensureValid(maxIterations);

    var iterationsLeft = maxIterations;

    T validScore = null;
    final Map<String, T> variableValues = new HashMap<>(variables.size());
    final Map<String, Boolean> constraintValues = new HashMap<>();
    final Map<String, T> validUnknowns = new HashMap<>();

    while (iterationsLeft > 0) {
      // Assign values to the unknowns!
      assignUnknownValues();
      // Update the variables with the new unknown values!
      variables.forEach((key, func) -> variableValues.put(key, func.apply(unknowns)));
      // Get the constraint results!
      constraints.forEach((key, pred) ->
                              constraintValues.put(key, pred.test(unknowns, variableValues)));

      if (constraintValues.containsValue(Boolean.FALSE)) {
        // If any constraints failed start again.
        iterationsLeft--;
        continue;
      }

      var temp = score.apply(unknowns, variableValues);

      if (validScore == null) {
        validScore = temp;
        validUnknowns.putAll(unknowns);
      } else if ((!maxCalc && temp.compareTo(validScore) < 0)
          || (maxCalc && temp.compareTo(validScore) > 0)) {
        validScore = temp;
        validUnknowns.putAll(unknowns);
        iterationsLeft++; // As long as we keep improving we keep iterating.
      }

      iterationsLeft--;
    }

    if (validScore == null) {
      throw new IllegalStateException("Unable to calculate a valid score!");
    }

    return new Pair<>(validScore, validUnknowns);
  }

  private void assignUnknownValues() {
    final List<T> values = valueDomain.getRandomValues(unknowns.size());

    var count = new AtomicInteger(0);

    unknowns.keySet().forEach(key -> unknowns.put(key, values.get(count.getAndIncrement())));
  }
}
