package com.capital7software.aoc.lib.util;

import com.capital7software.aoc.lib.math.MathOperations;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a range from start (inclusive) to end (exclusive).<br><br>
 * Supports: Integer, Long, Float, Double,
 * AtomicInteger, AtomicLong, BigDecimal, and BigInteger types.
 *
 * @param start The inclusive start of this Range.
 * @param end   The exclusive end of this Range.
 * @param <T>   The type of the value of this Range.
 */
public record Range<T extends Number & Comparable<T>>(@NotNull T start, @NotNull T end) {
    /**
     * Returns true if the specified value falls within this Range.
     *
     * @param value The value to test.
     * @return True if the specified value falls within this Range.
     */
    public boolean contains(@NotNull T value) {
        return start.compareTo(value) <= 0 && value.compareTo(end) < 0;
    }

    /**
     * Returns the distance from the start of this Range to the specified value. If
     * value comes before the start of this Range, the returned number will be negative.
     *
     * @param value The value to get the distance of.
     * @return The distance from the start of this Range to the specified value.
     */
    public @NotNull T distanceFromStart(@NotNull T value) {
        if (!contains(value)) {
            throw new RuntimeException("The specified value must be contained within this range!");
        }

        return MathOperations.subtract(value, start);
    }

    /**
     * Returns true if this Range contains at least one number.
     *
     * @return True if this Range contains at least one number.
     */
    public boolean isNotEmpty() {
        return end.compareTo(start) > 0;
    }

    /**
     * Returns true if the end of this range is equal to or less than the start.
     *
     * @return True if the end of this range is equal to or less than the start.
     */
    public boolean isEmpty() {
        return end.compareTo(start) <= 0;
    }

    /**
     * Creates a new Range with the inclusive start and the exclusive end is
     * the start plus the specified extent.
     *
     * @param start  The inclusive start of the new Range.
     * @param extent Added to the start value to get the exclusive end of the new Range.
     * @param <T> The type of the value in the new Range.
     * @return A new Range with the inclusive start and the exclusive end is
     * the start plus the specified extent.
     */
    public static <T extends Number & Comparable<T>> @NotNull Range<T> from(@NotNull T start, @NotNull T extent) {
        return new Range<>(start, MathOperations.add(start, extent));
    }
}
