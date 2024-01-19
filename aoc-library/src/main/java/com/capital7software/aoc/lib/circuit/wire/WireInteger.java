package com.capital7software.aoc.lib.circuit.wire;

import com.capital7software.aoc.lib.circuit.signal.Signal;
import com.capital7software.aoc.lib.circuit.signal.SignalSupplier;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

/**
 * A Wire that carries a Signal that corries an Integer value.
 * <p>
 * Two wires with the same ID are considered to be equal.
 */
public class WireInteger implements Wire<Integer> {
    /**
     * The ID of this Wire
     */
    private final String id;
    /**
     * The current Signal on this Wire.
     */
    private Signal<Integer> signal;
    /**
     * THe source where this Wire receives its Signal.
     */
    private SignalSupplier<Integer> source;

    /**
     * Instantiates a new Wire with the specified ID and source. The Signal for the new Wire
     * will be retrieved from the specified source.
     *
     * @param id The ID of this new Wire.
     * @param source The source of this new Wire.
     */
    public WireInteger(
            @NotNull String id,
            @NotNull SignalSupplier<Integer> source
    ) {
        this.id = id;
        this.source = source;
        if (source.supply().isPresent()) {
            this.signal = source.supply().get();
        }
    }


    /**
     * Instantiates a new Wire with the specified ID and no source and no Signal.
     *
     * @param id The ID of this new Wire.
     */
    public WireInteger(@NotNull String id) {
        this(id, Optional::empty);
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
    public Optional<Signal<Integer>> supply() {
        if (signal == null) {
            updateFromSource(); // Attempt to get a non-null signal!
        }
        return Optional.ofNullable(signal);
    }

    @Override
    public void source(@NotNull SignalSupplier<Integer> source) {
        this.source = source;
    }

    @Override
    public void updateFromSource() {
        var updated = source.supply();
        updated.ifPresent(this::consume);
    }

    @Override
    public String toString() {
        return "Wire16Bit{" +
                "id='" + id + '\'' +
                ", signal=" + signal +
                ", source=" + source +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WireInteger wireInteger)) {
            return false;
        }
        return id.equals(wireInteger.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
