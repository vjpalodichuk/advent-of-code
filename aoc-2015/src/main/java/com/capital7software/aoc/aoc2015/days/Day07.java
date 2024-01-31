package com.capital7software.aoc.aoc2015.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.circuit.board.CircuitBoardInteger;
import com.capital7software.aoc.lib.circuit.signal.Signal;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * --- Day 7: Some Assembly Required ---<br><br>
 * This year, Santa brought little Bobby Tables a set of wires and bitwise logic gates!
 * Unfortunately, little Bobby is a little under the recommended age range, and he needs
 * help assembling the circuit.
 *
 * <p><br>
 * Each wire has an identifier (some lowercase letters) and can carry a 16-bit signal
 * (a number from 0 to 65535). A signal is provided to each wire by a gate, another wire, or
 * some specific value. Each wire can only get a signal from one source, but can provide its
 * signal to multiple destinations. A gate provides no signal until all of its inputs have a signal.
 *
 * <p><br>
 * The included instructions booklet describes how to connect the parts together: x AND y -> z
 * means to connect wires x and y to an AND gate, and then connect its output to wire z.
 *
 * <p><br>
 * For example:
 *
 * <p><br>
 * <code>
 * 123 -&#62; x means that the signal 123 is provided to wire x.<br>
 * x AND y -&#62; z means that the bitwise AND of wire x and wire y is provided to wire z.<br>
 * p LSHIFT 2 -&#62; q means that the value from wire p is left-shifted by 2 and then provided
 * to wire q.<br> NOT e -&#62; f means that the bitwise complement of the value from wire e is
 * provided to wire f.<br>
 * </code>
 *
 * <p><br>
 * Other possible gates include OR (bitwise OR) and RSHIFT (right-shift). If, for some reason,
 * you'd like to emulate the circuit instead, almost all programming languages (for example, C,
 * JavaScript, or Python) provide operators for these gates.
 *
 * <p><br>
 * For example, here is a simple circuit:
 *
 * <p><br>
 * <code>
 * 123 -&#62; x<br>
 * 456 -&#62; y<br>
 * x AND y -&#62; d<br>
 * x OR y -&#62; e<br>
 * x LSHIFT 2 -&#62; f<br>
 * y RSHIFT 2 -&#62; g<br>
 * NOT x -&#62; h<br>
 * NOT y -&#62; i<br>
 * </code>
 *
 * <p><br>
 * After it is run, these are the signals on the wires:
 *
 * <p><br>
 * <code>
 * d: 72<br>
 * e: 507<br>
 * f: 492<br>
 * g: 114<br>
 * h: 65412<br>
 * i: 65079<br>
 * x: 123<br>
 * y: 456<br>
 * </code>
 *
 * <p><br>
 * In little Bobby's kit's instructions booklet (provided as your puzzle input),
 * what signal is ultimately provided to wire a?
 *
 * <p><br>
 * Your puzzle answer was 3176.
 *
 * <p><br>
 * --- Part Two ---<br><br>
 * Now, take the signal you got on wire a, override wire b to that signal, and reset the other
 * wires (including wire a). What new signal is ultimately provided to wire a?
 *
 * <p><br>
 * Your puzzle answer was 14710.
 */
public class Day07 implements AdventOfCodeSolution {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day07.class);

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
    final var start = Instant.now();
    final var board = loadCircuitBoard("0", input);
    final var total = getWireValues(board).get("a");
    final var end = Instant.now();
    LOGGER.info("{} is provided to wire a!", total);
    logTimings(LOGGER, start, end);
  }

  @Override
  public void runPart2(List<String> input) {
    final var start = Instant.now();
    final var board = loadCircuitBoard("0", input);
    var wires = getWireValues(board);
    final var oldB = override(board, "a", "b");
    resetWires(board);
    wires = getWireValues(board);
    final var total = wires.get("a");
    final var end = Instant.now();
    LOGGER.info("{} is wire b's old value, {} is provided to wire a!",
                oldB.orElse(null), total);
    logTimings(LOGGER, start, end);
  }

  /**
   * Returns a CircuitBoardInteger with the specified ID and loaded with the
   * specified setup.
   *
   * @param id    The ID of the board to create.
   * @param input The layout of the gates and wires.
   * @return A CircuitBoardInteger with the specified ID and loaded with the
   *     specified setup.
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
    board.wires()
        .values()
        .forEach(
            wire -> wire.supply()
                .flatMap(Signal::signal)
                .ifPresent(signal -> results.put(wire.id(), signal))
        );

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
