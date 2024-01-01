package com.capital7software.aoc2015.lib.circuit.signal;

/**
 * Capable of supplying a Signal to one or more consumers of Signals.
 *
 * @param <T> The type of the value that is carried by a Signal.
 */
public interface SignalSupplier<T extends Comparable<T>> {
    /**
     * This method is called to return the Signal that is supplied by this SignalSupplier.
     *
     * @return The Signal that is supplied by this SignalSupplier.
     */
    Signal<T> supply();
}
