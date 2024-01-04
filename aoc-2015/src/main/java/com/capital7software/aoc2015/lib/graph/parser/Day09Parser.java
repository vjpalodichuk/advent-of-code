package com.capital7software.aoc2015.lib.graph.parser;

import com.capital7software.aoc2015.lib.graph.Graph;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A simple parser capable of loading a Graph specified in the following format:
 * <code>source to target = weight_of_edge</code>
 * <p>
 * For example, given the following distances:
 * <p>
 * <code>
 * London to Dublin = 464
 * London to Belfast = 518
 * Dublin to Belfast = 141
 * </code>
 * <p>
 * Each edge is undirected and so London to Dublin implies Dublin to London with
 * the same weight for the Edge.
 *
 */
public class Day09Parser implements GraphParser<String, Integer> {
    @NotNull
    @Override
    public Optional<Graph<String, Integer>> parse(@NotNull List<String> input, @NotNull String name) {
        var graph = new Graph<String, Integer>(Objects.requireNonNull(name));

        Objects.requireNonNull(input).forEach(line -> parseLine(line, graph));

        return Optional.of(graph);
    }

    private void parseLine(String line, Graph<String, Integer> graph) {
        if (line == null || line.isBlank()) {
            return;
        }

        var split = line.split("=");

        assert split.length == 2;

        var rawEdge = split[0].trim().split(" to ");
        var weight = Integer.parseInt(split[1].trim());

        assert rawEdge.length == 2;

        // Create / add the Nodes to the Graph.
        String source = rawEdge[0].trim();
        String target = rawEdge[1].trim();
        String label = String.valueOf(weight);

        // Ensure the nodes are added to the Graph before trying to add an Edge.
        graph.add(source);
        graph.add(target);

        // Add the new edge with the specified weight.
        graph.add(source, target, label, weight);
        // Add it in the other direction as well.
        graph.add(target, source, label, weight);
    }
}
