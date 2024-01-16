package com.capital7software.aoc.lib.util;

import java.util.Objects;

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
     *
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
     * Returns the second value of this Triple.
     *
     * @return The second value of this Triple.
     */
    public U second() {
        return second;
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
     * Sets the second value to the specified value.
     *
     * @param second The value to use.
     */
    public void second(U second) {
        this.second = second;
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
        return "Pair[" +
                "first=" + first + ", " +
                "second=" + second + ']';
    }

}