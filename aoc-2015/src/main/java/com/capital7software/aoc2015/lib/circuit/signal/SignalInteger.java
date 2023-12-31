package com.capital7software.aoc2015.lib.circuit.signal;

public record SignalInteger(int value) implements Signal<Integer> {
    @Override
    public Integer signal() {
        return value;
    }
}
