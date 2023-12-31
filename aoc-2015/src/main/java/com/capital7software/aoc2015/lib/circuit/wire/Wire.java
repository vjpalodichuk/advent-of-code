package com.capital7software.aoc2015.lib.circuit.wire;

import com.capital7software.aoc2015.lib.circuit.signal.Signal;
import com.capital7software.aoc2015.lib.circuit.signal.SignalConsumer;
import com.capital7software.aoc2015.lib.circuit.signal.SignalSupplier;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface Wire<T extends Comparable<T>> extends SignalSupplier<T>, SignalConsumer<T> {
    String id();
    void source(@NotNull SignalSupplier<T> source);
    SignalSupplier<T> source();
    boolean add(@NotNull SignalConsumer<T> destination);
    boolean remove(@NotNull SignalConsumer<T> destination);
    Collection<SignalConsumer<T>> destinations();
    Signal<T> updateFromSource();

    /**
     * Updates the signal on this wire from its source and then passes it to this wire's destinations!
     */
    default void carry() {

        var signal = updateFromSource(); // Update the signal on this wire
        var destinations = destinations();

        if (destinations != null) {
            destinations.forEach(it -> it.consume(signal)); // Pass the signal down the line
        }
    }
}
