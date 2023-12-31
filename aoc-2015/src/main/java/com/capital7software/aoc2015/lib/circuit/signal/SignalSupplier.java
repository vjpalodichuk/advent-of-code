package com.capital7software.aoc2015.lib.circuit.signal;

public interface SignalSupplier<T extends Comparable<T>> {
    Signal<T> supply();
}
