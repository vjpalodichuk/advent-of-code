package com.capital7software.aoc2015.lib.circuit.signal;

/**
 * Capable of receiving / consuming a Signal of the specified type.
 *
 * @param <T> The type of the value that is carried by a Signal.
 */
public interface SignalConsumer<T extends Comparable<T>> {
    /**
     * This method is called to send the specified signal to this SignalConsumer.
     *
     * @param signal The Signal to send.
     */
    void consume(Signal<T> signal);
}
