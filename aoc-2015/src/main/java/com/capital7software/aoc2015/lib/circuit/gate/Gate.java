package com.capital7software.aoc2015.lib.circuit.gate;

import com.capital7software.aoc2015.lib.circuit.signal.SignalSupplier;

public interface Gate<T extends Comparable<T>> extends SignalSupplier<T> {
    String id();
}
