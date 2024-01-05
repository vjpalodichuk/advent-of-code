package com.capital7software.aoc2015.lib.math;

import com.capital7software.aoc2015.lib.graph.Graph;
import com.capital7software.aoc2015.lib.graph.Vertex;
import com.capital7software.aoc2015.lib.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A Reindeer Racing Simulator!
 * <p>
 * Easily run races and calculate how many points each reindeer is awarded at any point in time during the race.
 */
public class ReindeerOlympics {
    Map<String, Pair<DistanceOverTimeWithRest, Double>> points;

    /**
     * Instantiates a new instance using the reindeer from the specified Graph.
     * The Graph needs to have DistanceOverTimeWithRest as the type for the Vertex value and
     * Integer as the type for the Edge weight. The Graph also needs to be fully populated!
     *
     * @param graph The populated Graph to get the reindeer from.
     */
    public ReindeerOlympics(Graph<DistanceOverTimeWithRest, Integer> graph) {
        points = new HashMap<>(graph.size());
        points.putAll(
                graph.getVertices()
                        .stream()
                        .filter(it -> it.getValue().isPresent())
                        .collect(Collectors.toMap(Vertex::getId, it -> new Pair<>(it.getValue().get(), 0.0)))
        );

    }

    /**
     * Runs this race for the specified amount of time. After every second, point totals are calculated and updated.
     * After each second the reindeer that are currently in the lead each receive one point.
     * After the time has elapsed a map of each reindeer and their total points is returned.
     *
     * @param duration The amount of time to run the race for.
     * @return A map of each reindeer and their total points.
     */
    public @NotNull Map<String, Double> runRace(long duration) {
        for (int i = 1; i <= duration; i++) {
            int finalI = i;
            // Calculate who is in the lead.
            var current = points.entrySet()
                    .stream()
                    .map(it -> new Pair<>(it.getKey(), it.getValue().first().distance(finalI)))
                    .sorted(Comparator.comparingDouble(Pair::second))
                    .toList();
            // Get the max distance
            var currentMax = current.get(current.size() - 1).second();
            // Update the score for anyone with that distance.
            current
                    .stream()
                    .filter(it -> it.second().equals(currentMax))
                    .map(Pair::first)
                    .forEach(it -> points.get(it).setSecond(points.get(it).second() + 1.0));
        }

        return points.entrySet()
                .stream()
                .map(entry -> new Pair<>(entry.getKey(), entry.getValue().second()))
                .collect(Collectors.toMap(Pair::first, Pair::second));
    }
}
