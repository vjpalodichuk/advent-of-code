package com.capital7software.aoc2015.lib.circuit.board;

import com.capital7software.aoc2015.lib.circuit.gate.Gate;
import com.capital7software.aoc2015.lib.circuit.gate.IdentityGate;
import com.capital7software.aoc2015.lib.circuit.signal.Signal;
import com.capital7software.aoc2015.lib.circuit.wire.Wire;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface CircuitBoard<T extends Comparable<T>> {
    String id();

    Map<String, Wire<T>> wires();

    Gate<T> add(@NotNull Gate<T> gate);

    Wire<T> add(@NotNull Wire<T> wire);

    default void resetWires() {
        wires().values().forEach(it -> it.consume(null));
    }

    default T override(String wireAId, String wireBId) {
        var wires = wires();
        Wire<T> wireA = wires.get(wireAId);
        Wire<T> wireB = wires.get(wireBId);

        if (wireA != null && wireB != null) {
            Signal<T> signalA = wireA.supply();
            Signal<T> signalB = wireB.supply();

            T oldBVal = signalB != null ? signalB.signal() : null;
            IdentityGate<T> newSupplier = new IdentityGate<>(wireAId + wireBId + wireAId + wireBId, () -> signalA);
            wireB.source(newSupplier);
            add(newSupplier);

            return oldBVal;
        }

        return null;
    }
}
