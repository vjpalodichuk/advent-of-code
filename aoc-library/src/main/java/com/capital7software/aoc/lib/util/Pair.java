package com.capital7software.aoc.lib.util;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;

/**
 * A simple class to hold two elements in a type safe manner.
 *
 * @param <T> The type of the first element
 * @param <U> The type of the second element
 */
public final class Pair<T, U> {
  private T first;
  private U second;

  /**
   * Instantiates a new and empty Pair.
   */
  public Pair() {
    first = null;
    second = null;
  }

  /**
   * Instantiates a new Pair with the specified values.
   *
   * @param first  The first element
   * @param second The second element
   */
  public Pair(T first, U second) {
    this.first = first;
    this.second = second;
  }

  /**
   * Returns the first value of this Triple.
   *
   * @return The first value of this Triple.
   */
  public T first() {
    return first;
  }

  /**
   * Sets the first value to the specified value.
   *
   * @param first The value to use.
   */
  public void first(T first) {
    this.first = first;
  }

  /**
   * Returns the second value of this Triple.
   *
   * @return The second value of this Triple.
   */
  public U second() {
    return second;
  }

  /**
   * Sets the second value to the specified value.
   *
   * @param second The value to use.
   */
  public void second(U second) {
    this.second = second;
  }

  /**
   * Returns a copy of this Pair.
   *
   * @return A copy of this Pair.
   */
  public @NotNull Pair<T, U> copy() {
    return new Pair<>(first, second);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Pair<?, ?> pair)) {
      return false;
    }
    return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
  }

  @Override
  public int hashCode() {
    return Objects.hash(first, second);
  }

  @Override
  public String toString() {
    return "Pair["
        + "first=" + first + ", "
        + "second=" + second + ']';
  }

  /**
   * Returns the first element. Used by Kotlin to support decomposing assignments.
   *
   * @return The first element.
   */
  public T component1() {
    return first;
  }

  /**
   * Returns the second element. Used by Kotlin to support decomposing assignments.
   *
   * @return The second element.
   */
  public U component2() {
    return second;
  }
}
