package com.capital7software.aoc2015.lib.circuit.wire;

import com.capital7software.aoc2015.lib.circuit.signal.SignalConsumer;
import com.capital7software.aoc2015.lib.circuit.signal.SignalSupplier;
import org.jetbrains.annotations.NotNull;

public interface Wire<T extends Comparable<T>> extends SignalSupplier<T>, SignalConsumer<T> {
    String id();

    void source(@NotNull SignalSupplier<T> source);

    void updateFromSource();
}
