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

    public Pair() {
        first = null;
        second = null;
    }

    /**
     * @param first  The first element
     * @param second The second element
     */
    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    public T first() {
        return first;
    }

    public U second() {
        return second;
    }

    public void first(T first) {
        this.first = first;
    }

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
