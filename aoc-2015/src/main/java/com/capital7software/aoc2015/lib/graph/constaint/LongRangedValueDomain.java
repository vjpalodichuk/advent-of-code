package com.capital7software.aoc2015.lib.graph.constaint;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LongRangedValueDomain implements ValueDomain<Long>{
    private final Random random;
    private final Long minimum;
    private final Long maximum;

    public LongRangedValueDomain(long minimum, long maximum) {
        this(minimum, maximum, System.nanoTime());
    }

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

    public Long getMinimum() {
        return minimum;
    }

    public Long getMaximum() {
        return maximum;
    }
}
