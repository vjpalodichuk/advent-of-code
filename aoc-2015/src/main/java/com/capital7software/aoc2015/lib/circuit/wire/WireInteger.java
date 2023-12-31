package com.capital7software.aoc2015.lib.circuit.wire;

import com.capital7software.aoc2015.lib.circuit.signal.Signal;
import com.capital7software.aoc2015.lib.circuit.signal.SignalConsumer;
import com.capital7software.aoc2015.lib.circuit.signal.SignalSupplier;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class WireInteger implements Wire<Integer> {
    private final String id;
    private Signal<Integer> signal;
    private SignalSupplier<Integer> source;
    private final Set<SignalConsumer<Integer>> destinations;

    public WireInteger(
            @NotNull String id,
            @NotNull SignalSupplier<Integer> source,
            @NotNull Set<SignalConsumer<Integer>> destinations
    ) {
        this.id = id;
        this.source = source;
        this.signal = source.supply();
        this.destinations = new HashSet<>(destinations);
    }

    public WireInteger(@NotNull String id) {
        this(id, () -> null, new HashSet<>());
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public void consume(Signal<Integer> signal) {
        this.signal = signal;
    }

    @Override
    public Signal<Integer> supply() {
        if (signal == null) {
            updateFromSource(); // Attempt to get a non-null signal!
        }
        return signal;
    }

    @Override
    public void source(@NotNull SignalSupplier<Integer> source) {
        this.source = source;
    }

    @Override
    public SignalSupplier<Integer> source() {
        return source;
    }

    @Override
    public Signal<Integer> updateFromSource() {
        var updated = source.supply();
        consume(updated);
        return updated;
    }

    @Override
    public boolean add(@NotNull SignalConsumer<Integer> destination) {
        return destinations.add(destination);
    }

    @Override
    public boolean remove(@NotNull SignalConsumer<Integer> destination) {
        return destinations.remove(destination);
    }

    @Override
    public Collection<SignalConsumer<Integer>> destinations() {
        return Collections.unmodifiableCollection(destinations);
    }

    @Override
    public String toString() {
        return "Wire16Bit{" +
                "id='" + id + '\'' +
                ", signal=" + signal +
                ", source=" + source +
                ", destinations=" + destinations.size() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WireInteger wireInteger)) return false;
        return id.equals(wireInteger.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
