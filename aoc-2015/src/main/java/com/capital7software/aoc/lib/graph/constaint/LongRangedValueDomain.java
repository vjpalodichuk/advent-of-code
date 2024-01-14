package com.capital7software.aoc.lib.graph.constaint;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A ValueDomain whose values are limited to the inclusive range minimum - maximum. This means that a value
 * produced by this ValueDomain will fall within the specified minimum and maximum values.
 *
 */
public class LongRangedValueDomain implements ValueDomain<Long>{
    private final Random random;
    private final Long minimum;
    private final Long maximum;

    /**
     * Instantiates a new instance with the specified minimum and maximum values and seeds the random
     * number generator with the current number of nanoseconds that the JVM has been running for.
     *
     * @param minimum The inclusive minimum value.
     * @param maximum The inclusive maximum value.
     */
    public LongRangedValueDomain(long minimum, long maximum) {
        this(minimum, maximum, System.nanoTime());
    }

    /**
     * Instantiates a new instance with the specified minimum and maximum values and seeds the random
     * number generator with the specified seed. This is typically used for testing to ensure a repeatable list
     * of values is returned.
     *
     * @param minimum The inclusive minimum value.
     * @param maximum The inclusive maximum value.
     */
    public LongRangedValueDomain(long minimum, long maximum, long seed) {
        this.minimum = minimum;
        this.maximum = maximum;
        this.random = new Random(seed);
    }

    @Override
    public @NotNull Long getRandomValue() {
        return random.nextLong(minimum, maximum + 1);
    }

    @Override
    public @NotNull List<Long> getRandomValues(int count) {
        var answer = new ArrayList<Long>(count);
        long total = 0;
        for (int i = 0; i < count; i++) {
            var next = random.nextLong(minimum, maximum + 1 - total);
            total += next;
            answer.add(next);
        }

        return answer;
    }

    /**
     *
     * @return The inclusive minimum value produced by this domain.
     */
    public @NotNull Long getMinimum() {
        return minimum;
    }

    /**
     *
     * @return The inclusive maximum value produced by this domain.
     */
    public @NotNull Long getMaximum() {
        return maximum;
    }
}
