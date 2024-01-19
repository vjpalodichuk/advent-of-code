package com.capital7software.aoc.lib.graph.parser;

import com.capital7software.aoc.lib.graph.Graph;
import com.capital7software.aoc.lib.graph.Vertex;
import com.capital7software.aoc.lib.math.DistanceOverTimeWithRest;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


/**
 * A simple parser for loading racing reindeer in to a Graph in the following format:<br>
 * <code>Name1 can fly 00 km/s for 0 seconds, but then must rest for 00 seconds.</code><br>
 * <code>Name2 can fly 00 km/s for 0 seconds, but then must rest for 00 seconds.</code><br>
 * <p><br>
 * For capital7software, suppose you have only two reindeer:
 * <ul>
 *     <li>
 *         Comet can fly 13 km/s for 7 seconds, but then must rest for 82 seconds.
 *     </li>
 *     <li>
 *         Dancer can fly 3 km/s for 16 seconds, but then must rest for 37 seconds.
 *     </li>
 * </ul>
 * <p>
 * Then the returned Graph will contain two vertices, one for each reindeer and the
 * value of each vertex will be a DistanceOverTimeWithRest instance that describes
 * the rate, duration, and rest needed by the reindeer.
 *
 */
public class Day14Parser implements GraphParser<DistanceOverTimeWithRest, Integer> {
    /**
     * Instantiates a new empty Day14Parser instance.
     */
    public Day14Parser() {

    }
    @Override
    public @NotNull Optional<Graph<DistanceOverTimeWithRest, Integer>> parse(
            @NotNull List<String> input,
            @NotNull String name
    ) {
        var graph = new Graph<DistanceOverTimeWithRest, Integer>(Objects.requireNonNull(name));
        Objects.requireNonNull(input).forEach(line -> parseLine(line, graph));
        return Optional.of(graph);
    }

    private void parseLine(String line, Graph<DistanceOverTimeWithRest, Integer> graph) {
        if (line == null || line.isBlank()) {
            return;
        }

        var split = line.split(" ");
        var name = split[0];
        var rate = Double.parseDouble(split[3].trim());
        var duration = Long.parseLong(split[6].trim());
        var rest = Long.parseLong(split[13].trim());
        var value = new DistanceOverTimeWithRest(rate, duration, rest);
        var vertex = new Vertex<DistanceOverTimeWithRest, Integer>(name, value);
        graph.add(vertex);
    }
}
