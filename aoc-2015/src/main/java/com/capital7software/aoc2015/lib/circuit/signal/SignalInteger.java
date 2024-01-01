package com.capital7software.aoc2015.lib.circuit.signal;

/**
 * A Signal that corries an Integer value.
 * @param value The Integer value that this Signal carries
 */
public record SignalInteger(int value) implements Signal<Integer> {
    @Override
    public Integer signal() {
        return value;
    }
}
