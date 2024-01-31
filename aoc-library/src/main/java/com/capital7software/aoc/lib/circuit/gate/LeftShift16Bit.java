package com.capital7software.aoc.lib.circuit.gate;

import com.capital7software.aoc.lib.circuit.signal.Signal;
import com.capital7software.aoc.lib.circuit.signal.SignalInteger;
import com.capital7software.aoc.lib.circuit.signal.SignalSupplier;
import java.util.Objects;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

/**
 * Performs a bitwise LEFT SHIFT operation on the value of the Signal supplied by its input.
 * Only supports operating on 16bit Integer values.
 *
 * @param id       The ID of this Gate.
 * @param supplier The SignalSupplier for this Gate.
 * @param amount   The amount of the shift.
 */
public record LeftShift16Bit(
    @NotNull String id,
    @NotNull SignalSupplier<Integer> supplier,
    int amount
) implements Gate<Integer> {
  @Override
  public Optional<Signal<Integer>> supply() {
    var signal = supplier.supply();

    if (signal.isPresent()) {
      var actual = signal.get().signal();
      return actual.map(integer -> new SignalInteger(0x0000FFFF & (integer << amount)));
    }
    return Optional.empty();
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
