package com.capital7software.aoc2015.lib.circuit.gate;

import com.capital7software.aoc2015.lib.circuit.signal.Signal;
import com.capital7software.aoc2015.lib.circuit.signal.SignalInteger;
import com.capital7software.aoc2015.lib.circuit.signal.SignalSupplier;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

/**
 * Performs a bitwise AND operation on the values of the Signals supplied by both of its inputs.
 * Only supports operating on 16bit Integer values.
 *
 * @param id The ID of this Gate.
 * @param first The first SignalSupplier.
 * @param second The second SignalSupplier.
 */
public record AndGate16Bit(
        @NotNull String id,
        @NotNull SignalSupplier<Integer> first,
        @NotNull SignalSupplier<Integer> second
) implements Gate<Integer> {
    @Override
    public Optional<Signal<Integer>> supply() {
        var val1 = first.supply();
        var val2 = second.supply();

        if (val1.isPresent() && val2.isPresent()) {
            Optional<Integer> signal1 = val1.get().signal();
            Optional<Integer> signal2 = val2.get().signal();
            if (signal1.isPresent() && signal2.isPresent()) {
                return Optional.of(
                        new SignalInteger(0x0000FFFF & (signal1.get() & (int) signal2.get()))
                );
            }
        }
        return Optional.empty();
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
