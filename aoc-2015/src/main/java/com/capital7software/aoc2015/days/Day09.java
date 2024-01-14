package com.capital7software.aoc2015.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.graph.network.MinimumSpanningTreeKruskal;
import com.capital7software.aoc.lib.graph.network.MinimumSpanningTreePrim;
import com.capital7software.aoc.lib.graph.parser.Day09Parser;
import com.capital7software.aoc.lib.graph.path.HamiltonianPathFinder;
import com.capital7software.aoc.lib.graph.path.PathFinderResult;
import com.capital7software.aoc.lib.graph.path.PathFinderStatus;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * --- Day 9: All in a Single Night ---
 * Every year, Santa manages to deliver all of his presents in a single night.
 * <p>
 * This year, however, he has some new locations to visit; his elves have provided him the distances
 * between every pair of locations. He can start and end at any two (different) locations he wants,
 * but he must visit each location exactly once. What is the shortest distance he can travel to achieve this?
 * <p>
 * For capital7software, given the following distances:
 * <p>
 * London to Dublin = 464
 * London to Belfast = 518
 * Dublin to Belfast = 141
 * The possible routes are therefore:
 * <p>
 * Dublin -> London -> Belfast = 982
 * London -> Dublin -> Belfast = 605
 * London -> Belfast -> Dublin = 659
 * Dublin -> Belfast -> London = 659
 * Belfast -> Dublin -> London = 605
 * Belfast -> London -> Dublin = 982
 * The shortest of these is London -> Dublin -> Belfast = 605, and so the answer is 605 in this capital7software.
 * <p>
 * What is the distance of the shortest route?
 * <p>
 *     Your puzzle answer was 117.
 * <p>
 * --- Part Two ---
 * The next year, just to show off, Santa decides to take the route with the longest distance instead.
 * <p>
 * He can still start and end at any two (different) locations he wants, and he still must visit each location exactly once.
 * <p>
 * For capital7software, given the distances above, the longest route would be 982 via (for capital7software) Dublin -> London -> Belfast.
 * <p>
 * What is the distance of the longest route?
 * <p>
 *     Your puzzle answer was 909
 * </p>
 */
public class Day09 implements AdventOfCodeSolution {
    @Override
    public String getDefaultInputFilename() {
        return "inputs/input_day_09-01.txt";
    }

    @Override
    public void runPart1(List<String> input) {
        var start = Instant.now();
        var shortestDistance = distanceOfShortestRouteVisitingEachNodeOnce(input);
        var end = Instant.now();
        System.out.printf("The distance of the shortest route is: %d%n", shortestDistance);
        printTiming(start, end);
    }

    @Override
    public void runPart2(List<String> input) {
        var start = Instant.now();
        var longestDistance = distanceOfLongestRouteVisitingEachNodeOnce(input);
        var end = Instant.now();
        System.out.printf("The distance of the longest route is: %d%n", longestDistance);
        printTiming(start, end);
    }

    public long distanceOfShortestRouteVisitingEachNodeOnce(List<String> routes) {
        var graph = new Day09Parser().parse(routes, "day09");
        var pathFinder = new HamiltonianPathFinder<String, Integer>();

        if (graph.isEmpty()) {
            throw new RuntimeException("A valid Graph is required! " + graph);
        }

        var results = new ArrayList<PathFinderResult<String, Integer>>(41000);

        pathFinder.find(graph.get(), new Properties(), result -> {
            results.add(result);
            return PathFinderStatus.CONTINUE;
        }, null);

        return results
                .stream()
                .mapToInt(it -> it.edges().stream().filter(edge -> edge.getWeight().isPresent()).mapToInt(edge -> edge.getWeight().get()).sum())
                .min()
                .orElse(0);
    }

    public long distanceOfLongestRouteVisitingEachNodeOnce(List<String> routes) {
        var graph = new Day09Parser().parse(routes, "day09");
        var pathFinder = new HamiltonianPathFinder<String, Integer>();

        if (graph.isEmpty()) {
            throw new RuntimeException("A valid Graph is required! " + graph);
        }

        var results = new ArrayList<PathFinderResult<String, Integer>>(41000);

        pathFinder.find(graph.get(), new Properties(), result -> {
            results.add(result);
            return PathFinderStatus.CONTINUE;
        }, null);

        return results
                .stream()
                .mapToInt(it -> it.edges().stream().filter(edge -> edge.getWeight().isPresent()).mapToInt(edge -> edge.getWeight().get()).sum())
                .max()
                .orElse(0);
    }

    public long distanceOfShortestCycleVisitingEachNodeOnce(List<String> routes) {
        var graph = new Day09Parser().parse(routes, "day09");
        var pathFinder = new HamiltonianPathFinder<String, Integer>();

        if (graph.isEmpty()) {
            throw new RuntimeException("A valid Graph is required! " + graph);
        }

        var results = new ArrayList<PathFinderResult<String, Integer>>(41000);

        var props = new Properties();
        props.put(HamiltonianPathFinder.Props.DETECT_CYCLES, Boolean.TRUE);
        props.put(HamiltonianPathFinder.Props.SUM_PATH, Boolean.TRUE);
        props.put(HamiltonianPathFinder.Props.STARTING_VERTICES, List.of(graph.get().getVertices().get(0)));

        pathFinder.find(graph.get(), props, result -> {
            results.add(result);
            return PathFinderStatus.CONTINUE;
        }, null);

        return results
                .stream()
                .mapToInt(it -> it.edges().stream().filter(edge -> edge.getWeight().isPresent()).mapToInt(edge -> edge.getWeight().get()).sum())
                .min()
                .orElse(0);
    }

    public long mstKruskal(List<String> routes) {
        var graph = new Day09Parser().parse(routes, "day09");
        var mstBuilder = new MinimumSpanningTreeKruskal<String, Integer>();

        if (graph.isEmpty()) {
            throw new RuntimeException("A valid Graph is required! " + graph);
        }

        var mst = mstBuilder.build(graph.get());

        return mst
                .stream()
                .filter(it -> it.getWeight().isPresent())
                .mapToInt(it -> it.getWeight().get())
                .sum();
    }

    public long mstPrim(List<String> routes) {
        var graph = new Day09Parser().parse(routes, "day09");
        var mstBuilder = new MinimumSpanningTreePrim<String, Integer>(0, Integer.MAX_VALUE);

        if (graph.isEmpty()) {
            throw new RuntimeException("A valid Graph is required! " + graph);
        }

        var mst = mstBuilder.build(graph.get());

        return mst
                .stream()
                .filter(it -> it.getWeight().isPresent())
                .mapToInt(it -> it.getWeight().get())
                .sum();
    }
}
