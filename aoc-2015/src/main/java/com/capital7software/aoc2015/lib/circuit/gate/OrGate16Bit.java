package com.capital7software.aoc2015.lib.circuit.gate;

import com.capital7software.aoc2015.lib.circuit.signal.Signal;
import com.capital7software.aoc2015.lib.circuit.signal.SignalInteger;
import com.capital7software.aoc2015.lib.circuit.signal.SignalSupplier;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record OrGate16Bit(
        @NotNull String id,
        @NotNull SignalSupplier<Integer> first,
        @NotNull SignalSupplier<Integer> second
) implements Gate<Integer> {
    @Override
    public Signal<Integer> supply() {
        var val1 = first.supply();
        var val2 = second.supply();

        if (val1 != null && val2 != null) {
            return new SignalInteger(0x0000FFFF & (val1.signal() | (int) val2.signal()));
        }
        return null;
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
