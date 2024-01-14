package com.capital7software.aoc.lib.circuit.gate;

import com.capital7software.aoc.lib.circuit.signal.Signal;
import com.capital7software.aoc.lib.circuit.signal.SignalSupplier;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

/**
 * A special Gate that directly passes along the Signal without performing any operations on it.
 *
 * @param id The ID of this Gate.
 * @param supplier The supplier of the Signal that will pass through this Gate.
 */
public record IdentityGate<T extends Comparable<T>>(
        @NotNull String id,
        @NotNull SignalSupplier<T> supplier
) implements Gate<T> {
    @Override
    public Optional<Signal<T>> supply() {
        return supplier.supply();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Gate<?> that)) {
            return false;
        }
        return id.equals(that.id());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
