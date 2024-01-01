package com.capital7software.aoc2015.lib.circuit.signal;

import java.util.Optional;

/**
 * The Signal is carried by Wires and Gates and CircuitBoards put those two together to carry the Signal
 * across the board.
 * @param <T> The type of the value that this Signal carries.
 */
public interface Signal<T extends Comparable<T>> {
    /**
     * @return The typesafe value that is carried by this Signal.
     */
    Optional<T> signal();
}
