package com.capital7software.aoc2015.lib.graph.parser;

import com.capital7software.aoc2015.lib.graph.Graph;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * A simple parser capable of loading a Graph specified in the following format:<br>
 * <code>Name1 would gain 00 happiness units by sitting next to Name2</code><br>
 * <code>Name2 would lose 00 happiness units by sitting next to Name1</code><br>
 * <p><br>
 * For example, suppose you have only four attendees planned, and you calculate their potential happiness as follows:
 * <ul>
 *     <li>
 *         Alice would gain 54 happiness units by sitting next to Bob.
 *     </li>
 *     <li>
 *         Alice would lose 79 happiness units by sitting next to Carol.
 *     </li>
 *     <li>
 *         Alice would lose 2 happiness units by sitting next to David.
 *     </li>
 *     <li>
 *         Bob would gain 83 happiness units by sitting next to Alice.
 *     </li>
 *     <li>
 *         Bob would lose 7 happiness units by sitting next to Carol.
 *     </li>
 *     <li>
 *         Bob would lose 63 happiness units by sitting next to David.
 *     </li>
 *     <li>
 *         Carol would lose 62 happiness units by sitting next to Alice.
 *     </li>
 *     <li>
 *         Carol would gain 60 happiness units by sitting next to Bob.
 *     </li>
 *     <li>
 *         Carol would gain 55 happiness units by sitting next to David.
 *     </li>
 *     <li>
 *         David would gain 46 happiness units by sitting next to Alice.
 *     </li>
 *     <li>
 *         David would lose 7 happiness units by sitting next to Bob.
 *     </li>
 *     <li>
 *         David would gain 41 happiness units by sitting next to Carol.
 *     </li>
 * </ul>
 * Then, if you seat Alice next to David, Alice would lose 2 happiness units
 * (because David talks so much), but David would gain 46 happiness units
 * (because Alice is such a good listener), for a total change of 44.
 * <p>
 * The value of the weight of the edge in both directions is the sum of the two edges.
 * Meaning the weight of the Edge Alice-Bob and Bob-Alice would be 137 (54 + 83).
 * <p>
 * Like with any Graph, the graph can be further manipulated after it is loaded.
 *
 */
public class Day13Parser implements GraphParser<String, Integer> {
    @Override
    public @NotNull Optional<Graph<String, Integer>> parse(@NotNull List<String> input, @NotNull String name) {
        var graph = new Graph<String, Integer>(Objects.requireNonNull(name));
        var map = new HashMap<String, Map<String, Long>>();
        var finalMap = new HashMap<String, Map<String, Long>>();
        Objects.requireNonNull(input).forEach(line -> parseLine(line, map));

        map.forEach((key, value) -> value.keySet().forEach(dest -> {
            var otherAmount = value.get(dest);
            var amount = map.get(dest).get(key);
            var total = amount + otherAmount;
            finalMap.computeIfAbsent(key, it -> new HashMap<>()).put(dest, total);
        }));

        finalMap.forEach((key, value) -> value.forEach((key2, value2) -> {
            graph.add(key);
            graph.add(key2);
            graph.add(key, key2, key + "-" + key2, value2.intValue());

        }));

        return Optional.of(graph);
    }

    private void parseLine(String line, HashMap<String, Map<String, Long>> map) {
        var split1 = line.split(" would ");
        var pMap = map.computeIfAbsent(split1[0].trim(), it -> new HashMap<>());
        var split2 = split1[1].split(" happiness units by sitting next to ");
        var other = split2[1].replace(".", "").trim();
        var split3 = split2[0].split(" ");
        long amount = Long.parseLong(split3[1].trim());

        if (split3[0].trim().equals("lose")) {
            amount = -amount;
        }
        pMap.put(other, amount);
    }
}
