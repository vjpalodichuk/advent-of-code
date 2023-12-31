package com.capital7software.aoc2015.lib.circuit.gate;

import com.capital7software.aoc2015.lib.circuit.signal.Signal;
import com.capital7software.aoc2015.lib.circuit.signal.SignalSupplier;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record IdentityGate<T extends Comparable<T>>(
        @NotNull String id,
        @NotNull SignalSupplier<T> supplier
) implements Gate<T> {
    @Override
    public Signal<T> supply() {
        return supplier.supply();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Gate<?> that)) return false;
        return id.equals(that.id());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
