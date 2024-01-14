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
     * @param first  The first element.
     * @param second The second element.
     * @param third  The third element.
     */
    public Triple(T first, U second, V third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public T first() {
        return first;
    }

    public U second() {
        return second;
    }

    public V third() {
        return third;
    }

    public void first(T first) {
        this.first = first;
    }

    public void second(U second) {
        this.second = second;
    }

    public void third(V thid) {
        this.third = thid;
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
