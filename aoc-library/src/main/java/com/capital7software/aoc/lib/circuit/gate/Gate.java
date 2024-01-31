package com.capital7software.aoc.lib.circuit.gate;

import com.capital7software.aoc.lib.circuit.signal.SignalSupplier;

/**
 * A Gate is capable of both consuming a Signal from its inputs and supplying its Signal to one or
 * more consumers provided it receives a valid Signal from all its inputs.
 *
 * @param <T> The type of the value that is carried by a Signal.
 */
public interface Gate<T extends Comparable<T>> extends SignalSupplier<T> {
  /**
   * Returns the ID of the Gate.
   *
   * @return The ID of the Gate.
   */
  String id();
}
