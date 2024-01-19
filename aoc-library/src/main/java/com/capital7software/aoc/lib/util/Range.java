package com.capital7software.aoc.lib.util;

import com.capital7software.aoc.lib.math.MathOperations;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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
     * The size of this Range is the end - start. This means that the value returned is the exclusive size.
     *
     * @return The exclusive size of this Range.
     */
    public long size() {
        return MathOperations.subtract(end, start).longValue();
    }

    /**
     * The inclusive size of this Range is the size + 1.
     *
     * @return The inclusive size of this Range.
     */
    public long sizeInclusive() {
        return size() + 1L;
    }

    /**
     * Creates a new Range with the inclusive start and the exclusive end is
     * the start plus the specified extent.
     *
     * @param start  The inclusive start of the new Range.
     * @param extent Added to the start value to get the exclusive end of the new Range.
     * @param <T>    The type of the value in the new Range.
     * @return A new Range with the inclusive start and the exclusive end is
     * the start plus the specified extent.
     */
    public static <T extends Number & Comparable<T>> @NotNull Range<T> from(@NotNull T start, @NotNull T extent) {
        return new Range<>(start, MathOperations.add(start, extent));
    }

    /**
     * Splits this range in two distinct ranges based on the splitPoint.
     * May produce an empty range if splitPoint is outside of this range.
     * If existing range is 1 - 4000 and splitPoint is 2000 then this method will
     * produce two new ranges: 1 - 1999, 2000 - 40000 if high is false. If high is true, then
     * the ranges would be 1 - 2000, 2001 - 4000
     *
     * @param range      The range to split
     * @param splitPoint The number to split the range on.
     * @param high       Pass true to split on the high end, else the low end.
     * @return A list with two new ranges.
     */
    public static List<Range<Long>> split(Range<Long> range, long splitPoint, boolean high) {
        var result = new ArrayList<Range<Long>>();
        long toSplit = high ? splitPoint + 1 : splitPoint;

        result.add(new Range<>(range.start(), Math.min(toSplit - 1, range.end())));
        result.add(new Range<>(Math.max(range.start(), toSplit), range.end()));
        return result;
    }

    /**
     * Splits this range in two distinct ranges based on the splitPoint.
     * May produce an empty range if splitPoint is outside of this range.
     * If existing range is 1 - 4000 and splitPoint is 2000 then this method will
     * produce two new ranges: 1 - 1999, 2000 - 40000 if high is false. If high is true, then
     * the ranges would be 1 - 2000, 2001 - 4000
     *
     * @param range      The range to split
     * @param splitPoint The number to split the range on.
     * @param high       Pass true to split on the high end, else the low end.
     * @return A list with two new ranges.
     */
    public static List<Range<Integer>> split(Range<Integer> range, int splitPoint, boolean high) {
        var result = new ArrayList<Range<Integer>>();
        int toSplit = high ? splitPoint + 1 : splitPoint;

        result.add(new Range<>(range.start(), Math.min(toSplit - 1, range.end())));
        result.add(new Range<>(Math.max(range.start(), toSplit), range.end()));
        return result;
    }
}
