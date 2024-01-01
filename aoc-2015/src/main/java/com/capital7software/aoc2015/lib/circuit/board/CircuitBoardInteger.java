package com.capital7software.aoc2015.lib.circuit.board;

import com.capital7software.aoc2015.lib.circuit.gate.*;
import com.capital7software.aoc2015.lib.circuit.signal.SignalInteger;
import com.capital7software.aoc2015.lib.circuit.signal.SignalSupplier;
import com.capital7software.aoc2015.lib.circuit.wire.Wire;
import com.capital7software.aoc2015.lib.circuit.wire.WireInteger;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/**
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
 * to emulate the circuit instead, almost all programming languages (for example, C, JavaScript, or Python)
 * provide operators for these gates.
 * <p>
 * For example, here is a simple circuit:
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
 * @param id The ID of this CircuitBoardInteger
 * @param gateMap The map that is used to store the Gates where the key is the Gate ID.
 * @param wireMap The map that is used to store the Wires where the key is the Wire ID.
 */
public record CircuitBoardInteger(
        @NotNull String id,
        @NotNull Map<String, Gate<Integer>> gateMap,
        @NotNull Map<String, Wire<Integer>> wireMap

) implements CircuitBoard<Integer> {
    /**
     * Instantiates a new CircuitBoardInteger with the specified ID and no Wires and no Gates.
     * @param id The ID of this CircuitBoardInteger
     */
    public CircuitBoardInteger(@NotNull String id) {
        this(id, new HashMap<>(), new HashMap<>());
    }

    /**
     * Builds a new CircuitBoardInteger with the specified ID and from the schematic.
     * <p>
     * For example, here is a simple circuit:
     * <p>
     * 123 -> x
     * 456 -> y
     * x AND y -> d
     * x OR y -> e
     * x LSHIFT 2 -> f
     * y RSHIFT 2 -> g
     * NOT x -> h
     * NOT y -> i
     * <p>
     * See the Gate specified documentation for further information on their behavior.
     *
     * @param id The ID of the new CircuitBoardInteger.
     * @param schematic The schematic to build the board from.
     * @return A new CircuitBoardInteger with the specified ID and configured by the specified schematic.
     */
    public static CircuitBoardInteger parse(
            @NotNull String id,
            @NotNull Collection<String> schematic
    ) {
        var board = new CircuitBoardInteger(id);
        var gateId = new AtomicInteger(0);

        schematic.forEach(line -> parseLine(line, board, gateId, patterns(), splits()));

        return board;
    }

    /**
     * Parses a single specification for configuring a source for a Wire.
     * See parse for details on the format of a specification.
     *
     * @param specification The instructions for configuring the source for the Wire.
     * @param board The CircuitBoardInteger that will receive the results of the parsing.
     * @param gateId The provider of the ID for any new gates
     * @param patterns The Regular Expression pattern instances to parse the specification.
     * @param splits The delimiters to split the specification up based on what it is for.
     */
    private static void parseLine(
            String specification,
            CircuitBoardInteger board,
            AtomicInteger gateId,
            Map<String, Pattern> patterns,
            Map<String, String> splits
    ) {
        if (specification == null || specification.isBlank()) {
            return;
        }

        var split = specification.split("->");
        var wireId = split[1].trim();

        // Will have its source updated with whatever supplier is in split[0]
        var wire = board.wireMap.computeIfAbsent(wireId, WireInteger::new);

        var supplier = parseSupplier(split[0].trim(), board, gateId, patterns, splits);

        if (supplier == null) {
            throw new RuntimeException("Unable to parse supplier information: " + split[0]);
        }

        wire.source(supplier);

        board.add(supplier);
    }

    /**
     * A Factory method that parses a single specification for configuring the source / supplier portion of
     * the specification. Returns an instance of the specific Gate that is specified as the source.
     *
     * @param specification The instructions for configuring the source for the Wire.
     * @param board The CircuitBoardInteger that will receive the results of the parsing.
     * @param gateId The provider of the ID for any new gates
     * @param patterns The Regular Expression pattern instances to parse the specification.
     * @param splits The delimiters to split the specification up based on what it is for.
     * @return A new Gate or null if the supplier cannot be successfully parsed.
     */
    private static Gate<Integer> parseSupplier(
            String specification,
            @NotNull CircuitBoardInteger board,
            @NotNull AtomicInteger gateId,
            @NotNull Map<String, Pattern> patterns,
            @NotNull Map<String, String> splits
    ) {
        if (specification == null || specification.isBlank()) {
            return null;
        }

        var nextId = "" + gateId.getAndIncrement();

        if (patterns.get("integers").matcher(specification).matches()) {
            var signal = new SignalInteger(Integer.parseInt(specification));
            return new IdentityGate<>(nextId, () -> signal);
        } else if (patterns.get("letters").matcher(specification).matches()) {
            var suppliers = wireOrSupplier(new String[]{specification}, 1, patterns.get("integers"), board.wireMap);
            return new IdentityGate<>(nextId, suppliers.get(0));
        } else if (patterns.get("and").matcher(specification).matches()) {
            var split = specification.split(splits.get("and"));
            var suppliers = wireOrSupplier(split, 2, patterns.get("integers"), board.wireMap);
            return new AndGate16Bit(nextId, suppliers.get(0), suppliers.get(1));
        } else if (patterns.get("or").matcher(specification).matches()) {
            var split = specification.split(splits.get("or"));
            var suppliers = wireOrSupplier(split, 2, patterns.get("integers"), board.wireMap);
            return new OrGate16Bit(nextId, suppliers.get(0), suppliers.get(1));
        } else if (patterns.get("not").matcher(specification).matches()) {
            var split = specification.split(splits.get("not"));
            var suppliers = wireOrSupplier(split, 1, patterns.get("integers"), board.wireMap);
            return new NotGate16Bit(nextId, suppliers.get(0));
        } else if (patterns.get("lshift").matcher(specification).matches()) {
            var split = specification.split(splits.get("lshift"));
            var suppliers = wireOrSupplier(split, 2, patterns.get("integers"), board.wireMap);
            return new LeftShift16Bit(nextId, suppliers.get(0), suppliers.get(1).supply().signal());
        } else if (patterns.get("rshift").matcher(specification).matches()) {
            var split = specification.split(splits.get("rshift"));
            var suppliers = wireOrSupplier(split, 2, patterns.get("integers"), board.wireMap);
            return new RightShift16Bit(nextId, suppliers.get(0), suppliers.get(1).supply().signal());
        }

        return null;
    }

    /**
     * Returns A list that will contain the expectedCount number of suppliers that were configured. An assertion
     * is performed to ensure that the expected number of suppliers have been configured.
     *
     * @param specification The instructions for configuring the source for the Wire.
     * @param expectedCount The number of inputs expected by the supplier in the specification.
     * @param numberOnlyPattern The pattern to test for a static number as a Supplier.
     * @param wireMap The map of existing Wires.
     * @return A list that will contain the expectedCount number of suppliers that were configured.
     */
    private static List<SignalSupplier<Integer>> wireOrSupplier(
            String[] specification,
            int expectedCount,
            @NotNull Pattern numberOnlyPattern,
            @NotNull Map<String, Wire<Integer>> wireMap) {
        var suppliers = new ArrayList<SignalSupplier<Integer>>(4);

        if (specification == null || specification.length == 0) {
            throw new RuntimeException("Input cannot be null or empty!");
        }

        for (var item : specification) {
            var trimmed = item.trim();

            if (trimmed.isBlank()) {
                continue;
            }

            if (numberOnlyPattern.matcher(trimmed).matches()) {
                var signal = new SignalInteger(Integer.parseInt(trimmed));
                suppliers.add(new IdentityGate<>(trimmed, () -> signal));
            } else {
                suppliers.add(wireMap.computeIfAbsent(trimmed, WireInteger::new));
            }
        }

        assert suppliers.size() == expectedCount;

        return suppliers;
    }

    private static Map<String, Pattern> patterns() {
        var patternMap = new HashMap<String, Pattern>();

        patternMap.put("integers", Pattern.compile("^-?\\d+$"));
        patternMap.put("letters", Pattern.compile("^[a-zA-Z]+$"));
        patternMap.put("and", Pattern.compile("^\\w+\\sAND\\s\\w+$"));
        patternMap.put("or", Pattern.compile("^\\w+\\sOR\\s\\w+$"));
        patternMap.put("not", Pattern.compile("^NOT\\s\\w+$"));
        patternMap.put("lshift", Pattern.compile("^\\w+\\sLSHIFT\\s\\d+$"));
        patternMap.put("rshift", Pattern.compile("^\\w+\\sRSHIFT\\s\\d+$"));

        return patternMap;
    }

    private static Map<String, String> splits() {
        var splitMap = new HashMap<String, String>();

        splitMap.put("and", "AND");
        splitMap.put("or", "OR");
        splitMap.put("not", "NOT");
        splitMap.put("lshift", "LSHIFT");
        splitMap.put("rshift", "RSHIFT");

        return splitMap;
    }

    @Override
    public Map<String, Wire<Integer>> wires() {
        return Collections.unmodifiableMap(wireMap);
    }

    @Override
    public Gate<Integer> add(@NotNull Gate<Integer> gate) {
        return gateMap.put(gate.id(), gate);
    }

    @Override
    public Wire<Integer> add(@NotNull Wire<Integer> wire) {
        return wireMap.put(wire.id(), wire);
    }
}
