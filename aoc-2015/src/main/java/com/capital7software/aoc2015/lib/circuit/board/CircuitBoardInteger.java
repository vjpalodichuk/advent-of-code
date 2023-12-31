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

public record CircuitBoardInteger(
        @NotNull String id,
        @NotNull Map<String, Gate<Integer>> gateMap,
        @NotNull Map<String, Wire<Integer>> wireMap

) implements CircuitBoard<Integer> {
    public CircuitBoardInteger(@NotNull String id) {
        this(id, new HashMap<>(), new HashMap<>());
    }

    public static CircuitBoardInteger parse(
            @NotNull String id,
            @NotNull Collection<String> input
    ) {
        var board = new CircuitBoardInteger(id);
        var gateId = new AtomicInteger(0);

        input.forEach(line -> parseLine(line, board, gateId, patterns(), splits()));

        return board;
    }

    private static void parseLine(
            String line,
            CircuitBoardInteger board,
            AtomicInteger gateId,
            Map<String, Pattern> patterns,
            Map<String, String> splits
    ) {
        if (line == null || line.isBlank()) {
            return;
        }

        var split = line.split("->");
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

    private static Gate<Integer> parseSupplier(
            String input,
            @NotNull CircuitBoardInteger board,
            @NotNull AtomicInteger gateId,
            @NotNull Map<String, Pattern> patterns,
            @NotNull Map<String, String> splits
    ) {
        if (input == null || input.isBlank()) {
            return null;
        }

        var nextId = "" + gateId.getAndIncrement();

        if (patterns.get("integers").matcher(input).matches()) {
            var signal = new SignalInteger(Integer.parseInt(input));
            return new IdentityGate<>(nextId, () -> signal);
        } else if (patterns.get("letters").matcher(input).matches()) {
            var suppliers = wireOrSupplier(new String[]{input}, 1, patterns.get("integers"), board.wireMap);
            return new IdentityGate<>(nextId, suppliers.get(0));
        }
        else if (patterns.get("and").matcher(input).matches()) {
            var split = input.split(splits.get("and"));
            var suppliers = wireOrSupplier(split, 2, patterns.get("integers"), board.wireMap);
            return new AndGate16Bit(nextId, suppliers.get(0), suppliers.get(1));
        } else if (patterns.get("or").matcher(input).matches()) {
            var split = input.split(splits.get("or"));
            var suppliers = wireOrSupplier(split, 2, patterns.get("integers"), board.wireMap);
            return new OrGate16Bit(nextId, suppliers.get(0), suppliers.get(1));
        } else if (patterns.get("not").matcher(input).matches()) {
            var split = input.split(splits.get("not"));
            var suppliers = wireOrSupplier(split, 1, patterns.get("integers"), board.wireMap);
            return new NotGate16Bit(nextId, suppliers.get(0));
        } else if (patterns.get("lshift").matcher(input).matches()) {
            var split = input.split(splits.get("lshift"));
            var suppliers = wireOrSupplier(split, 2, patterns.get("integers"), board.wireMap);
            return new LeftShift16Bit(nextId, suppliers.get(0), suppliers.get(1).supply().signal());
        } else if (patterns.get("rshift").matcher(input).matches()) {
            var split = input.split(splits.get("rshift"));
            var suppliers = wireOrSupplier(split, 2, patterns.get("integers"), board.wireMap);
            return new RightShift16Bit(nextId, suppliers.get(0), suppliers.get(1).supply().signal());
        }

        return null;
    }

    private static List<SignalSupplier<Integer>> wireOrSupplier(
            String[] input,
            int expectedCount,
            @NotNull Pattern numberOnlyPattern,
            @NotNull Map<String, Wire<Integer>> wireMap) {
        var suppliers = new ArrayList<SignalSupplier<Integer>>(4);

        if (input == null || input.length == 0) {
            throw new RuntimeException("Input cannot be null or empty!");
        }

        for (var item : input) {
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
    public Collection<Gate<Integer>> gates() {
        return Collections.unmodifiableCollection(gateMap.values());
    }

    @Override
    public Collection<Wire<Integer>> wires() {
        return Collections.unmodifiableCollection(wireMap.values());
    }

    @Override
    public Gate<Integer> add(@NotNull Gate<Integer> gate) {
        return gateMap.put(gate.id(), gate);
    }

    @Override
    public Wire<Integer> add(@NotNull Wire<Integer> wire) {
        return wireMap.put(wire.id(), wire);
    }

    @Override
    public Gate<Integer> removeGate(@NotNull String gateId) {
        return gateMap.remove(gateId);
    }

    @Override
    public Wire<Integer> removeWire(@NotNull String wireId) {
        return wireMap.remove(wireId);
    }

    @Override
    public Gate<Integer> gate(@NotNull String gateId) {
        return gateMap.get(gateId);
    }

    @Override
    public Wire<Integer> wire(@NotNull String wireId) {
        return wireMap.get(wireId);
    }
}
