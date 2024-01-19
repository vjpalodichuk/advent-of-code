package com.capital7software.aoc.lib.math;

import org.jetbrains.annotations.NotNull;

/**
 * A simple record that can calculate distance traveled at time t given rate r the duration d that can be
 * traveled at a rate of r and the amount of rest u that is needed before traveling at rate r again.
 *
 * @param rate The rate at which the object travels.
 * @param duration The amount of time at which the object travels at the specified rate.
 * @param rest After traveling at the specified rate for the specified duration, how much
 *             time needs to elapse before being able to travel at that rate again.
 */
public record DistanceOverTimeWithRest(double rate, long duration, long rest) implements Comparable<DistanceOverTimeWithRest> {
    /**
     * Calculates the distance traveled at the specified time.
     * <p>
     * Basic formula is: r * dt = distance
     * <p>
     * Where r is the rate of this object and dt is the amount of time spent traveling for this object.
     * <p>
     * dt = duration * (time / (duration + rest)) + min(duration, time % (duration + rest))
     *
     * @param time The time to calculate the distance traveled for.
     * @return The distance traveled by this object at the specified time.
     */
    public double distance(long time) {
        var cycle = duration + rest;

        if (cycle == 0) {
            return 0;
        }

        var secondsTraveling = (duration * (time / cycle)) + Math.min(duration, time % cycle);

        return secondsTraveling * rate;
    }

    @Override
    public int compareTo(@NotNull DistanceOverTimeWithRest o) {
        return Double.compare(rate, o.rate);
    }
}
