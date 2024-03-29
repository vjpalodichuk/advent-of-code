package com.capital7software.aoc.lib.circuit.board;

import com.capital7software.aoc.lib.circuit.gate.Gate;
import com.capital7software.aoc.lib.circuit.gate.IdentityGate;
import com.capital7software.aoc.lib.circuit.wire.Wire;
import java.util.Map;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

/**
 * CircuitBoards contain Wires and Gates to carry Signals of the specified type. Configuration
 * is board specific so please refer the documentation for the type of board you wish to work with.
 *
 * @param <T> The type of the value of the Signal that is carried by the components of this board.
 */
public interface CircuitBoard<T extends Comparable<T>> {
  /**
   * Returns the id of this circuit board.
   *
   * @return The id of this circuit board.
   */
  String id();

  /**
   * An unmodifiable map copy of the wires in this board. The keys are the wire ids and the values
   * are the Wires.
   *
   * @return An unmodifiable map copy of the wires in this board.
   */
  Map<String, Wire<T>> wires();

  /**
   * Adds a new Gate to this circuit board.
   *
   * @param gate The gate to add.
   * @return The gate that was added if the add was successful.
   */
  Gate<T> add(@NotNull Gate<T> gate);

  /**
   * Adds a new Wire to this circuit board.
   *
   * @param wire The wire to add.
   * @return The wire that was added if the add was successful.
   */
  Wire<T> add(@NotNull Wire<T> wire);

  /**
   * Clears any existing signal from all the wires of this board.
   */
  default void resetWires() {
    wires().values().forEach(it -> it.consume(null));
  }

  /**
   * Overrides the signal of wire b with the current signal of wire a.
   *
   * @param firstWireId The wire that contains the signal to copy to wire b.
   * @param secondWireId The wire that will receive the signal currently in wire a.
   * @return The value of the signal that was stored in wire b.
   */
  default Optional<T> override(String firstWireId, String secondWireId) {
    var wires = wires();
    Wire<T> wireA = wires.get(firstWireId);
    Wire<T> wireB = wires.get(secondWireId);

    if (wireA != null && wireB != null) {
      var signalA = wireA.supply();
      var signalB = wireB.supply();
      Optional<T> oldSignalB = signalB.isPresent() ? signalB.get().signal() : Optional.empty();
      IdentityGate<T> newSupplier = new IdentityGate<>(
          firstWireId + secondWireId + firstWireId + secondWireId,
          () -> signalA
      );
      wireB.source(newSupplier);
      add(newSupplier);

      return oldSignalB;
    }

    return Optional.empty();
  }
}
