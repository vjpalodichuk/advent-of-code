package com.capital7software.aoc.lib.graph.network;

import com.capital7software.aoc.lib.graph.Edge;
import com.capital7software.aoc.lib.graph.Graph;
import com.capital7software.aoc.lib.util.Pair;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

/**
 * "Error 2023, you say? Why, that's a power overload error, of course! It means you have
 * too many components plugged in. Try unplugging some components and--" You explain that
 * there are hundreds of components here, and you're in a bit of a hurry.
 *
 * <p><br>
 * "Well, let's see how bad it is; do you see a big red reset button somewhere? It should
 * be on its own module. If you push it, it probably won't fix anything, but it'll report
 * how overloaded things are." After a minute or two, you find the reset button; it's so
 * big that it takes two hands just to get enough leverage to push it. Its screen then displays:
 *
 * <p><br>
 * <code>
 * <b>SYSTEM OVERLOAD!</b>
 * <br><br>
 * Connected components would require<br>
 * power equal to at least <b>100 stars!</b><br>
 * </code>
 *
 * <p><br>
 * "Wait, how many components did you say are plugged in? With that much equipment, you
 * could produce snow for an entire--" You disconnect the call.
 *
 * <p><br>
 * You have nowhere near that many stars - you need to find a way to disconnect at least
 * half of the equipment here, but it's already Christmas! You only have time to disconnect
 * three wires.
 *
 * <p><br>
 * Fortunately, someone left a wiring diagram (your puzzle input) that shows how the
 * components are connected. For example:
 *
 * <p><br>
 * <code>
 * jqt: rhn xhk nvd<br>
 * rsh: frs pzl lsr<br>
 * xhk: hfx<br>
 * cmg: qnr nvd lhk bvb<br>
 * rhn: xhk bvb hfx<br>
 * bvb: xhk hfx<br>
 * pzl: lsr hfx nvd<br>
 * qnr: nvd<br>
 * ntq: jqt hfx bvb xhk<br>
 * nvd: lhk<br>
 * lsr: lhk<br>
 * rzs: qnr cmg lsr rsh<br>
 * frs: qnr lhk lsr<br>
 * </code>
 *
 * <p><br>
 * Each line shows the name of a component, a colon, and then a list of other components
 * to which that component is connected. Connections aren't directional; abc: xyz and xyz:
 * abc both represent the same configuration. Each connection between two components is
 * represented only once, so some components might only ever appear on the left or right
 * side of a colon.
 *
 * <p><br>
 * In this example, if you disconnect the wire between hfx/pzl, the wire between bvb/cmg,
 * and the wire between nvd/jqt, you will divide the components into two separate,
 * disconnected groups:
 *
 * <p><br>
 * <ul>
 *     <li>
 *         9 components: cmg, frs, lhk, lsr, nvd, pzl, qnr, rsh, and rzs.
 *     </li>
 *     <li>
 *         6 components: bvb, hfx, jqt, ntq, rhn, and xhk.
 *     </li>
 * </ul>
 * Multiplying the sizes of these groups together produces 54.<br>
 *
 * <p><br>
 * Find the three wires you need to disconnect in order to divide the components into two
 * separate groups. What do you get if you multiply the sizes of these two groups together?
 *
 * <p><br>
 * You climb over weather machines, under giant springs, and narrowly avoid a pile of
 * pipes as you find and disconnect the three wires.
 *
 * <p><br>
 * A moment after you disconnect the last wire, the big red reset button module makes a
 * small ding noise:
 *
 * <p><br>
 * <code>
 * System overload resolved!<br>
 * Power required is now <b>50 stars.</b><br>
 * </code>
 *
 * <p><br>
 * Out of the corner of your eye, you notice goggles and a loose-fitting hard hat peeking
 * at you from behind an ultra crucible. You think you see a faint glow, but before you
 * can investigate, you hear another small ding:
 *
 * <p><br>
 * <code>
 * Power required is now <b>49 stars.</b>
 * <br><br>
 * Please supply the necessary stars and<br>
 * push the button to restart the system.<br>
 * </code>
 */
public class WeatherStation {
  private final Graph<Integer, Integer> graph;
  private final Random random;

  private WeatherStation(@NotNull Map<String, @NotNull Set<String>> edges) {
    graph = new Graph<>("weather-station");

    edges.forEach((key, value) -> {
      graph.add(key);
      value.forEach(it -> {
        graph.add(it);
        graph.add(key, it);
        graph.add(it, key);
      });
    });
    this.random = new Random(Instant.now().toEpochMilli());
  }

  private WeatherStation(@NotNull Graph<Integer, Integer> graph) {
    this.graph = graph.copy();
    this.random = new Random(Instant.now().toEpochMilli());
  }

  private void removeEdge(String nodeA, String nodeB) {
    graph.remove(nodeA, nodeB);
    graph.remove(nodeB, nodeA);
  }

  private @NotNull WeatherStation copy() {
    return new WeatherStation(graph);
  }

  private @NotNull Set<String> get(@NotNull String node) {
    graph.add(node);
    return graph.getEdges(node).stream().map(Edge::getTarget).collect(Collectors.toSet());
  }

  /**
   * Make sure to copy the weather station prior to calling this method as it will remove
   * the edges from the station.
   *
   * @param station The WeatherStation to make the directed edges from
   * @return A list of edges represented as a pair of nodes.
   */
  private static @NotNull List<Pair<String, String>> makeDirected(
      @NotNull WeatherStation station
  ) {
    var edges = new ArrayList<Pair<String, String>>(station.graph.size() * 3);

    for (var node : station.graph.getVertices()) {
      var neighbors = new HashSet<>(station.get(node.getId()));
      for (var neighbor : neighbors) {
        edges.add(new Pair<>(node.getId(), neighbor));
        station.removeEdge(node.getId(), neighbor);
      }
    }

    return edges;
  }

  /**
   * Calculates and returns the product of the two sets that produce the maximum product.
   *
   * @param station The WeatherStation to operate on.
   * @param cuts    The number of cuts to make.
   * @return The product of the two sets that produce the maximum product.
   */
  public static long findMinimumCut(@NotNull WeatherStation station, long cuts) {
    var graph = station.copy();

    var edges = makeDirected(graph);

    var savedEdges = new ArrayList<>(edges.stream().map(Pair::copy).toList());

    List<String> keys;
    Map<String, Long> nodeSizes;

    // Eventually this will find a solution and break out of the loop.
    while (true) {
      // Use Lists instead of sets here since we rename the edges
      // And that can lead to duplicates and Java's sets go wonky if that happens!
      edges = new ArrayList<>(savedEdges.stream().map(Pair::copy).toList());
      var nodes = new HashMap<String, List<Pair<String, String>>>();
      nodeSizes = new HashMap<>();

      for (var edge : edges) {
        var nodeA = edge.first();
        var nodeB = edge.second();
        // Use Lists instead of sets here since we rename the edges
        // And that can lead to duplicates and Java's sets go wonky if that happens!
        nodes.computeIfAbsent(nodeA, it -> new LinkedList<>()).add(edge);
        nodes.computeIfAbsent(nodeB, it -> new LinkedList<>()).add(
            new Pair<>(edge.second(), edge.first())
        );
      }

      for (var nodeEntry : nodes.entrySet()) {
        nodeSizes.put(nodeEntry.getKey(), 1L);
      }

      while (nodes.size() > 2) {
        // We select a random edge to ensure that we don't cut the graph in
        // a lopsided manner!!!
        var index = station.random.nextInt(edges.size());

        var edgeT = edges.remove(index);
        var nodeA = edgeT.first();
        var nodeB = edgeT.second();

        if (nodeA.equals(nodeB)) {
          continue; // self edge
        }

        for (var edgeB : nodes.get(nodeB)) {
          // rename b's edges to a's!
          if (edgeB.first().equals(nodeB)) {
            edgeB.first(nodeA);
            nodes.get(edgeB.second())
                .stream()
                .filter(it -> it.second().equals(nodeB))
                .forEach(it -> it.second(nodeA));
          }
          if (edgeB.second().equals(nodeB)) {
            edgeB.second(nodeA);
            nodes.get(edgeB.first())
                .stream()
                .filter(it -> it.first().equals(nodeB))
                .forEach(it -> it.first(nodeA));
          }
          nodes.get(nodeA).add(edgeB);
        }
        nodes.remove(nodeB).clear();
        nodeSizes.put(nodeA, nodeSizes.get(nodeA) + nodeSizes.get(nodeB));
        nodeSizes.remove(nodeB);

        var edgesA = new LinkedList<Pair<String, String>>();

        for (var edgeA : nodes.get(nodeA)) {
          if (edgeA.first().equals(nodeB)) {
            edgeA.first(nodeA);
          }
          if (edgeA.second().equals(nodeB)) {
            edgeA.second(nodeA);
          }
          var a = edgeA.first();
          var b = edgeA.second();
          if (a.equals(b)) {
            continue; // self edge
          }
          edgesA.add(edgeA);
        }
        nodes.put(nodeA, edgesA);
      }

      // Should only have two nodes left
      keys = nodes.keySet().stream().toList();

      if (nodes.get(keys.getFirst()).size() <= cuts) {
        break;
      }
    }
    return nodeSizes.get(keys.getFirst()) * nodeSizes.get(keys.get(1));
  }

  /**
   * Builds and returns a new WeatherStation populated with the components specified in the
   * List of String.
   *
   * @param stream The List of Strings to parse the components for the WeatherStation from.
   * @return A new WeatherStation populated with the components specified in the List of String.
   */
  public static @NotNull WeatherStation buildWeatherStation(@NotNull List<String> stream) {
    var components = new HashMap<String, Set<String>>();

    stream.forEach(line -> parseLine(line, components));

    return new WeatherStation(components);
  }

  private static void parseLine(
      String line,
      HashMap<String, Set<String>> components
  ) {
    if (line == null || line.isBlank()) {
      return;
    }

    var split = line.split(": ");
    var id = split[0].trim();
    var connections = split[1].split(" ");

    var component = components.computeIfAbsent(id, it -> new HashSet<>());

    for (var connection : connections) {
      var connectionComponent = components
          .computeIfAbsent(connection.trim(), it -> new HashSet<>());
      component.add(connection);
      connectionComponent.add(id);
    }
  }
}
