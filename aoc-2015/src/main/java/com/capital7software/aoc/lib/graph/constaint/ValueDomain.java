package com.capital7software.aoc.lib.graph.constaint;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Used by Solvers to get values to assign to the Unknowns.
 *
 * @param <T> The type of the values within the domain.
 */
public interface ValueDomain <T extends Number & Comparable<T>> {
    /**
     * Returns a single value from the domain chosen at random.
     *
     * @return A random value from the domain.
     */
    @NotNull
    T getRandomValue();

    /**
     * Returns a list with the specified number of values from the domain chosen randomly.
     *
     * @param count The number of random values to return.
     * @return A list with the specified number of values from the domain chosen randomly.
     */
    @NotNull
    List<T> getRandomValues(int count);
}
