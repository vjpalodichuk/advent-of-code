package com.capital7software.aoc.lib.util;

import java.util.Objects;

/**
 * A simple class to hold three elements in a type-safe manner.
 *
 * @param <T> The type of the first element.
 * @param <U> The type of the second element.
 * @param <V> The type of the third element.
 */
public final class Triple<T, U, V> {
    private T first;
    private U second;
    private V third;

    /**
     * Creates a new Triple with the specified values.
     *
     * @param first  The first element.
     * @param second The second element.
     * @param third  The third element.
     */
    public Triple(T first, U second, V third) {
        this.first = first;
        this.second = second;
        this.third = third;
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
     * Returns the third value of this Triple.
     *
     * @return The third value of this Triple.
     */
    public V third() {
        return third;
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

    /**
     * Sets the third value to the specified value.
     *
     * @param third The value to use.
     */
    public void third(V third) {
        this.third = third;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Triple<?, ?, ?> triple)) {
            return false;
        }
        return Objects.equals(first, triple.first) &&
                Objects.equals(second, triple.second) &&
                Objects.equals(third, triple.third);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second, third);
    }

    @Override
    public String toString() {
        return "Triple{" +
                "first=" + first +
                ", second=" + second +
                ", third=" + third +
                '}';
    }
}
