package com.capital7software.aoc.aoc2015aoc.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.graph.network.MinimumSpanningTreeKruskal;
import com.capital7software.aoc.lib.graph.network.MinimumSpanningTreePrim;
import com.capital7software.aoc.lib.graph.parser.Day09Parser;
import com.capital7software.aoc.lib.graph.path.HamiltonianPathfinder;
import com.capital7software.aoc.lib.graph.path.PathfinderProperties;
import com.capital7software.aoc.lib.graph.path.PathfinderResult;
import com.capital7software.aoc.lib.graph.path.PathfinderStatus;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * --- Day 9: All in a Single Night ---<br><br>
 * Every year, Santa manages to deliver all of his presents in a single night.
 *
 * <p><br>
 * This year, however, he has some new locations to visit; his elves have provided him the
 * distances between every pair of locations. He can start and end at any two (different) locations
 * he wants, but he must visit each location exactly once. What is the shortest distance he can
 * travel to achieve this?
 *
 * <p><br>
 * For example, given the following distances:
 *
 * <p><br>
 * <code>
 * London to Dublin = 464<br>
 * London to Belfast = 518<br>
 * Dublin to Belfast = 141<br>
 * </code>
 *
 * <p><br>
 * The possible routes are therefore:
 *
 * <p><br>
 * <code>
 * Dublin -&#62; London -&#62; Belfast = 982<br>
 * London -&#62; Dublin -&#62; Belfast = 605<br>
 * London -&#62; Belfast -&#62; Dublin = 659<br>
 * Dublin -&#62; Belfast -&#62; London = 659<br>
 * Belfast -&#62; Dublin -&#62; London = 605<br>
 * Belfast -&#62; London -&#62; Dublin = 982<br>
 * </code>
 *
 * <p><br>
 * The shortest of these is London -&#62; Dublin -&#62; Belfast = 605, and so the answer is
 * 605 in this example.
 *
 * <p><br>
 * What is the distance of the shortest route?
 *
 * <p><br>
 * Your puzzle answer was 117.
 *
 * <p><br>
 * --- Part Two ---<br><br>
 * The next year, just to show off, Santa decides to take the route with the longest
 * distance instead.
 *
 * <p><br>
 * He can still start and end at any two (different) locations he wants, and he still must
 * visit each location exactly once.
 *
 * <p><br>
 * For example, given the distances above, the longest route would be 982 via
 * (for example) Dublin -&#62; London -&#62; Belfast.
 *
 * <p><br>
 * What is the distance of the longest route?
 *
 * <p><br>
 * Your puzzle answer was 909
 */
public class Day09 implements AdventOfCodeSolution {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day09.class);

  /**
   * Instantiates the solution instance.
   */
  public Day09() {

  }

  @Override
  public String getDefaultInputFilename() {
    return "inputs/input_day_09-01.txt";
  }

  @Override
  public void runPart1(List<String> input) {
    var start = Instant.now();
    var shortestDistance = distanceOfShortestRouteVisitingEachNodeOnce(input);
    var end = Instant.now();
    LOGGER.info("The distance of the shortest route is: {}", shortestDistance);
    logTimings(LOGGER, start, end);
  }

  @Override
  public void runPart2(List<String> input) {
    var start = Instant.now();
    var longestDistance = distanceOfLongestRouteVisitingEachNodeOnce(input);
    var end = Instant.now();
    LOGGER.info("The distance of the longest route is: {}", longestDistance);
    logTimings(LOGGER, start, end);
  }

  /**
   * Uses a Hamiltonian Pathfinder to determine the shortest route visiting
   * each node at least once along the available routes.
   *
   * @param routes All the available routes.
   * @return The distance of the shortest route that visits every node.
   */
  public long distanceOfShortestRouteVisitingEachNodeOnce(List<String> routes) {
    var graph = new Day09Parser().parse(routes, "day09");
    var pathFinder = new HamiltonianPathfinder<String, Integer>();

    if (graph.isEmpty()) {
      throw new RuntimeException("A valid Graph is required! " + graph);
    }

    var results = new ArrayList<PathfinderResult<String, Integer>>(41000);

    pathFinder.find(graph.get(), new Properties(), result -> {
      results.add(result);
      return PathfinderStatus.CONTINUE;
    }, null);

    return results
        .stream()
        .mapToInt(it -> it.edges()
            .stream()
            .filter(edge -> edge.getWeight().isPresent())
            .mapToInt(edge -> edge.getWeight().get())
            .sum()
        )
        .min()
        .orElse(0);
  }

  /**
   * Uses a Hamiltonian Pathfinder to determine the longest route visiting
   * each node at least once along the available routes.
   *
   * @param routes All the available routes.
   * @return The distance of the longest route that visits every node.
   */
  public long distanceOfLongestRouteVisitingEachNodeOnce(List<String> routes) {
    var graph = new Day09Parser().parse(routes, "day09");
    var pathFinder = new HamiltonianPathfinder<String, Integer>();

    if (graph.isEmpty()) {
      throw new RuntimeException("A valid Graph is required! " + graph);
    }

    var results = new ArrayList<PathfinderResult<String, Integer>>(41000);

    pathFinder.find(graph.get(), new Properties(), result -> {
      results.add(result);
      return PathfinderStatus.CONTINUE;
    }, null);

    return results
        .stream()
        .mapToInt(it -> it.edges()
            .stream()
            .filter(edge -> edge.getWeight().isPresent())
            .mapToInt(edge -> edge.getWeight().get())
            .sum()
        )
        .max()
        .orElse(0);
  }

  /**
   * Uses a Hamiltonian Pathfinder to determine the shortest route visiting
   * each node at least once along the available routes that completes a Cycle.
   *
   * @param routes All the available routes.
   * @return The distance of the shortest route that visits every node in a Cycle.
   */
  @SuppressFBWarnings
  public long distanceOfShortestCycleVisitingEachNodeOnce(List<String> routes) {
    var graph = new Day09Parser().parse(routes, "day09").orElse(null);

    if (graph == null) {
      throw new RuntimeException("A valid Graph is required! " + graph);
    }

    final var pathFinder = new HamiltonianPathfinder<String, Integer>();
    final var results = new ArrayList<PathfinderResult<String, Integer>>(41_000);

    var props = new Properties();
    props.put(PathfinderProperties.DETECT_CYCLES, Boolean.TRUE);
    props.put(PathfinderProperties.SUM_PATH, Boolean.TRUE);
    props.put(PathfinderProperties.STARTING_VERTICES,
              List.of(graph.getVertices().getFirst()));

    pathFinder.find(graph, props, result -> {
      results.add(result);
      return PathfinderStatus.CONTINUE;
    }, null);

    return results
        .stream()
        .mapToInt(
            it -> it.edges()
                .stream()
                .filter(edge -> edge.getWeight().isPresent())
                .mapToInt(edge -> edge.getWeight().get())
                .sum()
        )
        .min()
        .orElse(0);
  }

  /**
   * Builds the Minimum Spanning Tree of the available routes using Kruskal's
   * algorithm.
   *
   * @param routes The available routes.
   * @return The sum of the Edges in the MST.
   */
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

  /**
   * Builds the Minimum Spanning Tree of the available routes using Prim's
   * algorithm.
   *
   * @param routes The available routes.
   * @return The sum of the Edges in the MST.
   */
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
