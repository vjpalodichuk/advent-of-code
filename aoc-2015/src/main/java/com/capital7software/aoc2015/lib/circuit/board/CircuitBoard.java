package com.capital7software.aoc2015.lib.circuit.board;

import com.capital7software.aoc2015.lib.circuit.gate.Gate;
import com.capital7software.aoc2015.lib.circuit.gate.IdentityGate;
import com.capital7software.aoc2015.lib.circuit.signal.Signal;
import com.capital7software.aoc2015.lib.circuit.wire.Wire;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface CircuitBoard<T extends Comparable<T>> {
    String id();
    Collection<Gate<T>> gates();
    Collection<Wire<T>> wires();
    Gate<T> add(@NotNull Gate<T> gate);
    Wire<T> add(@NotNull Wire<T> wire);
    Gate<T> removeGate(@NotNull String gateId);
    Wire<T> removeWire(@NotNull String wireId);
    Gate<T> gate(@NotNull String gateId);
    Wire<T> wire(@NotNull String wireId);

    default void updateSignals() {
        wires().forEach(Wire::updateFromSource);
    }

    default void resetWires() {
        wires().forEach(it -> it.consume(null));
    }

    default T override(String wireAId, String wireBId) {
        var wires = wires();
        Wire<T> wireA = wires.stream().filter(it -> it.id().equals(wireAId)).findFirst().orElse(null);
        Wire<T> wireB = wires.stream().filter(it -> it.id().equals(wireBId)).findFirst().orElse(null);

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
