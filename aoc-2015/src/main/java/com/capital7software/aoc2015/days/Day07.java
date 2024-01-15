package com.capital7software.aoc2015.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.circuit.board.CircuitBoardInteger;
import com.capital7software.aoc.lib.circuit.signal.Signal;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * --- Day 7: Some Assembly Required ---
 * This year, Santa brought little Bobby Tables a set of wires and bitwise logic gates! Unfortunately,
 * little Bobby is a little under the recommended age range, and he needs help assembling the circuit.
 * <p>
 * Each wire has an identifier (some lowercase letters) and can carry a 16-bit signal (a number from 0 to 65535).
 * A signal is provided to each wire by a gate, another wire, or some specific value. Each wire can only get
 * a signal from one source, but can provide its signal to multiple destinations. A gate provides no signal
 * until all of its inputs have a signal.
 * <p>
 * The included instructions booklet describes how to connect the parts together: x AND y -> z means to connect
 * wires x and y to an AND gate, and then connect its output to wire z.
 * <p>
 * For example:
 * <p>
 * 123 -> x means that the signal 123 is provided to wire x.
 * x AND y -> z means that the bitwise AND of wire x and wire y is provided to wire z.
 * p LSHIFT 2 -> q means that the value from wire p is left-shifted by 2 and then provided to wire q.
 * NOT e -> f means that the bitwise complement of the value from wire e is provided to wire f.
 * Other possible gates include OR (bitwise OR) and RSHIFT (right-shift). If, for some reason, you'd like
 * to emulate the circuit instead, almost all programming languages (for capital7software, C, JavaScript, or Python)
 * provide operators for these gates.
 * <p>
 * For capital7software, here is a simple circuit:
 * <p>
 * 123 -> x
 * 456 -> y
 * x AND y -> d
 * x OR y -> e
 * x LSHIFT 2 -> f
 * y RSHIFT 2 -> g
 * NOT x -> h
 * NOT y -> i
 * After it is run, these are the signals on the wires:
 * <p>
 * d: 72
 * e: 507
 * f: 492
 * g: 114
 * h: 65412
 * i: 65079
 * x: 123
 * y: 456
 * In little Bobby's kit's instructions booklet (provided as your puzzle input),
 * what signal is ultimately provided to wire a?
 * <p>
 *     Your puzzle answer was 3176.
 * <p>
 * --- Part Two ---
 * Now, take the signal you got on wire a, override wire b to that signal, and reset the other wires
 * (including wire a). What new signal is ultimately provided to wire a?
 * <p>
 *     Your puzzle answer was 14710.
 */
public class Day07 implements AdventOfCodeSolution {
    /**
     * Instantiates the solution instance.
     */
    public Day07() {

    }

    @Override
    public String getDefaultInputFilename() {
        return "inputs/input_day_07-01.txt";
    }

    @Override
    public void runPart1(List<String> input) {
        var start = Instant.now();
        var board = loadCircuitBoard("0", input);
        var total = getWireValues(board).get("a");
        var end = Instant.now();
        System.out.printf(
                "%d is provided to wire a!%n", total);
        printTiming(start, end);
    }

    @Override
    public void runPart2(List<String> input) {
        var start = Instant.now();
        var board = loadCircuitBoard("0", input);
        var wires = getWireValues(board);
        var oldB = override(board, "a", "b");
        resetWires(board);
        wires = getWireValues(board);
        var total = wires.get("a");
        var end = Instant.now();
        System.out.printf(
                "%d is wire b's old value, %d is provided to wire a!%n", oldB.orElse(null), total);
        printTiming(start, end);
    }

    /**
     * Returns a CircuitBoardInteger with the specified ID and loaded with the
     * specified setup.
     *
     * @param id The ID of the board to create.
     * @param input The layout of the gates and wires.
     * @return A CircuitBoardInteger with the specified ID and loaded with the
     * specified setup.
     */
    public CircuitBoardInteger loadCircuitBoard(String id, List<String> input) {
        return CircuitBoardInteger.parse(id, input);
    }

    /**
     * Returns the values of the wires in the specified board.
     *
     * @param board The board to retrieve the wires from.
     * @return The values of the wires in the specified board.
     */
    public Map<String, Integer> getWireValues(CircuitBoardInteger board) {
        var results = new HashMap<String, Integer>();
        board.wires().values().forEach(wire -> wire.supply().flatMap(Signal::signal).ifPresent(signal -> results.put(wire.id(), signal)));

        return results;
    }

    /**
     * Overrides the signal from wireA to wireB on the specified board.
     *
     * @param board The board that contains the wires.
     * @param wireA The source wire
     * @param wireB The destination wire to override the signal of.
     * @return The value of the Signal that was stored in wireB.
     */
    public Optional<Integer> override(CircuitBoardInteger board, String wireA, String wireB) {
        return board.override(wireA, wireB);
    }

    /**
     * Clears the Signals from all the wires of the specified board.
     *
     * @param board The board to reset.
     */
    public void resetWires(CircuitBoardInteger board) {
        board.resetWires();
    }
}
