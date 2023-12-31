package com.capital7software.aoc2015.lib.circuit.signal;

public interface SignalConsumer<T extends Comparable<T>> {
    void consume(Signal<T> signal);
}
