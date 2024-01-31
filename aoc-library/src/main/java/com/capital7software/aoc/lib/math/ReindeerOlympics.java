package com.capital7software.aoc.lib.math;

import com.capital7software.aoc.lib.graph.Graph;
import com.capital7software.aoc.lib.graph.Vertex;
import com.capital7software.aoc.lib.util.Pair;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

/**
 * A Reindeer Racing Simulator!
 *
 * <p><br>
 * Easily run races and calculate how many points each reindeer is awarded at any point in
 * time during the race.
 *
 * <p><br>
 * Reindeer can only either be flying (always at their top speed) or resting (not moving at all),
 * and always spend whole seconds in either state.
 *
 * <p><br>
 * For example, suppose you have the following Reindeer:
 * <ul>
 *     <li>
 *         Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.
 *     </li>
 *     <li>
 *         Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds.
 *     </li>
 *     <li>
 *         After one second, Comet has gone 14 km, while Dancer has gone 16 km.
 *     </li>
 *     <li>
 *         After ten seconds, Comet has gone 140 km, while Dancer has gone 160 km.
 *     </li>
 *     <li>
 *         On the eleventh second, Comet begins resting (staying at 140 km), and Dancer
 *         continues on for a total distance of 176 km.
 *     </li>
 *     <li>
 *         On the 12th second, both reindeer are resting.
 *     </li>
 *     <li>
 *         They continue to rest until the 138th second, when Comet flies for another ten seconds.
 *     </li>
 *     <li>
 *         On the 174th second, Dancer flies for another 11 seconds.
 *     </li>
 * </ul>
 * In this example, after the 1000th second, both reindeer are resting, and Comet is in the
 * lead at 1120 km (poor Dancer has only gotten 1056 km by that point). So, in this situation,
 * Comet would win (if the race ended at 1000 seconds).
 *
 * <p><br>
 * Given the descriptions of each reindeer (in your puzzle input), after exactly 2503 seconds,
 * what distance has the winning reindeer traveled?
 *
 * <p><br>
 * Seeing how reindeer move in bursts, Santa decides he's not pleased with the old scoring system.
 *
 * <p><br>
 * Instead, at the end of each second, he awards one point to the reindeer currently in the lead.
 * (If there are multiple reindeer tied for the lead, they each get one point.)
 * He keeps the traditional 2,503-second time limit, of course, as doing otherwise would be
 * entirely ridiculous.
 *
 * <p><br>
 * Given the example reindeer from above, after the first second, Dancer is in the lead and gets
 * one point. He stays in the lead until several seconds into Comet's second burst: after the
 * 140th second, Comet pulls into the lead and gets his first point. Of course, since Dancer had
 * been in the lead for the 139 seconds before that, he has accumulated 139 points by the
 * 140th second.
 *
 * <p><br>
 * After the 1000th second, Dancer has accumulated 689 points, while poor Comet, our old champion,
 * only has 312. So, with the new scoring system, Dancer would win (if the race ended at
 * 1000 seconds).
 *
 * <p><br>
 * Again given the descriptions of each reindeer (in your puzzle input), after exactly 2503
 * seconds, how many points does the winning reindeer have?
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
   * Runs this race for the specified amount of time. After every second, point totals are
   * calculated and updated. After each second the reindeer that are currently in the lead each
   * receive one point. After the time has elapsed a map of each reindeer and their total points
   * is returned.
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
      var currentMax = current.getLast().second();
      // Update the score for anyone with that distance.
      current
          .stream()
          .filter(it -> it.second().equals(currentMax))
          .map(Pair::first)
          .forEach(it -> points.get(it).second(points.get(it).second() + 1.0));
    }

    return points.entrySet()
        .stream()
        .map(entry -> new Pair<>(entry.getKey(), entry.getValue().second()))
        .collect(Collectors.toMap(Pair::first, Pair::second));
  }
}
