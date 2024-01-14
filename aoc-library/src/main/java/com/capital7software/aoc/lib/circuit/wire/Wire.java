package com.capital7software.aoc.lib.circuit.wire;

import com.capital7software.aoc.lib.circuit.signal.SignalConsumer;
import com.capital7software.aoc.lib.circuit.signal.SignalSupplier;
import org.jetbrains.annotations.NotNull;

/**
 * A Wire is capable of both consuming a Signal from a single source and supplying its Signal to one or
 * more consumers.
 * <p>
 * A Wire's signal may be overridden by directly calling consume() on it and passing in the desired Signal.
 * <p>
 * A Signal is retrieved from its source automatically if supply() is called and the Signal currently produced by
 * this Wire is currently invalid.
 * <p>
 * @param <T> The type of the value that is carried by a Signal.
 */
public interface Wire<T extends Comparable<T>> extends SignalSupplier<T>, SignalConsumer<T> {
    /**
     *
     * @return The ID of this Wire
     */
    String id();

    /**
     *
     * @param source The source of this Signal. There can only be one source at any given time.
     */
    void source(@NotNull SignalSupplier<T> source);

    /**
     * Updates the Signal carried by this Wire from its source.
     */
    void updateFromSource();
}
