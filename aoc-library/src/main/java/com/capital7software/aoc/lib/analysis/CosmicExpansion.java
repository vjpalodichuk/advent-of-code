package com.capital7software.aoc.lib.analysis;

import com.capital7software.aoc.lib.geometry.Point2D;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;

/**
 * The researcher has collected a bunch of data and compiled the data into a single giant image
 * (your puzzle input). The image includes empty space (.) and galaxies (#). For example:
 *
 * <p><br>
 * <code>
 * ...#......<br>
 * .......#..<br>
 * #.........<br>
 * ..........<br>
 * ......#...<br>
 * .#........<br>
 * .........#<br>
 * ..........<br>
 * .......#..<br>
 * #...#.....<br>
 * </code>
 *
 * <p><br>
 * The researcher is trying to figure out the sum of the lengths of the shortest
 * path between every pair of galaxies. However, there's a catch: the universe
 * expanded in the time it took the light from those galaxies to reach the observatory.
 *
 * <p><br>
 * Due to something involving gravitational effects, only some space expands.
 * In fact, the result is that any rows or columns that contain no galaxies should all
 * actually be twice as big.
 *
 * <p><br>
 * In the above example, three columns and two rows contain no galaxies:
 *
 * <p><br>
 * <code>
 * v  v  v<br>
 * ...#......<br>
 * .......#..<br>
 * #.........<br>
 * &#62;..........&#60;<br>
 * ......#...<br>
 * .#........<br>
 * .........#<br>
 * &#62;..........&#60;<br>
 * .......#..<br>
 * #...#.....<br>
 * ^  ^  ^<br>
 * </code>
 *
 * <p><br>
 * These rows and columns need to be twice as big; the result of cosmic expansion therefore
 * looks like this:
 *
 * <p><br>
 * <code>
 * ....#........<br>
 * .........#...<br>
 * #............<br>
 * .............<br>
 * .............<br>
 * ........#....<br>
 * .#...........<br>
 * ............#<br>
 * .............<br>
 * .............<br>
 * .........#...<br>
 * #....#.......<br>
 * </code>
 *
 * <p><br>
 * Equipped with this expanded universe, the shortest path between every pair of galaxies
 * can be found. It can help to assign every galaxy a unique number:
 *
 * <p><br>
 * <code>
 * ....1........<br>
 * .........2...<br>
 * 3............<br>
 * .............<br>
 * .............<br>
 * ........4....<br>
 * .5...........<br>
 * ............6<br>
 * .............<br>
 * .............<br>
 * .........7...<br>
 * 8....9.......<br>
 * </code>
 *
 * <p><br>
 * In these 9 galaxies, there are 36 pairs. Only count each pair once; order within the pair
 * doesn't matter. For each pair, find any shortest path between the two galaxies using only
 * steps that move up, down, left, or right exactly one . or # at a time. (The shortest path
 * between two galaxies is allowed to pass through another galaxy.)
 *
 * <p><br>
 * For example, here is one of the shortest paths between galaxies 5 and 9:
 *
 * <p><br>
 * <code>
 * ....1........<br>
 * .........2...<br>
 * 3............<br>
 * .............<br>
 * .............<br>
 * ........4....<br>
 * .5...........<br>
 * .##.........6<br>
 * ..##.........<br>
 * ...##........<br>
 * ....##...7...<br>
 * 8....9.......<br>
 * </code>
 *
 * <p><br>
 * This path has length 9 because it takes a minimum of nine steps to get from galaxy 5 to galaxy
 * 9 (the eight locations marked # plus the step onto galaxy 9 itself). Here are some other
 * examples of shortest path lengths:
 *
 * <p><br>
 * <code>
 * Between galaxy 1 and galaxy 7: 15<br>
 * Between galaxy 3 and galaxy 6: 17<br>
 * Between galaxy 8 and galaxy 9: 5<br>
 * </code>
 *
 * <p><br>
 * In this example, after expanding the universe, the sum of the shortest path between all
 * 36 pairs of galaxies is 374.
 *
 * <p><br>
 * Expand the universe, then find the length of the shortest path between every pair of galaxies.
 * What is the sum of these lengths?
 *
 * <p><br>
 * The galaxies are much older (and thus much farther apart) than the researcher initially
 * estimated.
 *
 * <p><br>
 * Now, instead of the expansion you did before, make each empty row or column one million
 * times larger. That is, each empty row should be replaced with 1000000 empty rows, and each
 * empty column should be replaced with 1000000 empty columns.
 *
 * <p><br>
 * (In the example above, if each empty row or column were merely 10 times larger, the sum
 * of the shortest paths between every pair of galaxies would be 1030. If each empty row or
 * column were merely 100 times larger, the sum of the shortest paths between every pair of
 * galaxies would be 8410. However, your universe will need to expand far beyond these values.)
 *
 * <p><br>
 * Starting with the same initial image, expand the universe according to these new rules,
 * then find the length of the shortest path between every pair of galaxies. What is the sum
 * of these lengths?
 */
public class CosmicExpansion {
  private record Galaxy(int id, @NotNull Point2D<Integer> point) {
    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof Galaxy galaxy)) {
        return false;
      }
      return id() == galaxy.id();
    }

    @Override
    public int hashCode() {
      return Objects.hash(id());
    }
  }

  private static class Universe {
    private static final char GALAXY_MARKER = '#';
    private static final Map<Integer, Integer> EMPTY_MAP = new HashMap<>();
    private final Map<Integer, Integer> columnGalaxyCountMap = new HashMap<>();
    private final Map<Integer, Map<Integer, Integer>> columnGapCountMap = new HashMap<>();
    private final Map<Integer, Integer> rowGalaxyCountMap = new HashMap<>();
    private final Map<Integer, Map<Integer, Integer>> rowGapCountMap = new HashMap<>();

    private final Map<Galaxy, Map<Galaxy, Long>> galaxyDistances = new HashMap<>();

    public static @NotNull Universe loadUniverse(@NotNull List<String> file) {
      var universe = new Universe();
      var rowCount = new AtomicInteger(0);
      var columnSize = new AtomicInteger(0);

      file.forEach(it -> {
        if (columnSize.get() == 0) {
          columnSize.set(it.length());
        }

        processRow(it, rowCount.getAndIncrement(), universe);
      });

      universe.expandUniverse(rowCount.get(), columnSize.get());

      return universe;
    }

    private void expandUniverse(int rowCount, int columnCount) {
      // Everything is ordered so that the first map index is always less than the second map index.
      populateGapCountMap(rowCount, rowGalaxyCountMap, rowGapCountMap);
      populateGapCountMap(columnCount, columnGalaxyCountMap, columnGapCountMap);
    }

    private void populateGapCountMap(
        int count,
        @NotNull Map<Integer, Integer> galaxyCountMap,
        @NotNull Map<Integer, Map<Integer, Integer>> gapCountMap
    ) {
      for (int i = 0; i < count; i++) {
        var blanks = 0;
        for (int j = i; j < count; j++) {
          if (galaxyCountMap.computeIfAbsent(j, it -> 0) == 0) {
            blanks++;
          }
          if (blanks > 0) {
            gapCountMap.computeIfAbsent(i, it -> new HashMap<>()).putIfAbsent(j, blanks);
          }
        }
      }
    }

    private static void processRow(String line, int row, @NotNull Universe universe) {
      var columnCount = new AtomicInteger(0);

      line
          .chars()
          .mapToObj(it -> (char) it)
          .forEach(it -> processCharacter(it, row, columnCount.getAndIncrement(), universe));
    }

    private static void processCharacter(
        char item,
        int row,
        int column,
        @NotNull Universe universe
    ) {
      if (item == GALAXY_MARKER) {
        universe.addGalaxy(new Point2D<>(column, row));
      }
    }

    private void addGalaxy(@NotNull Point2D<Integer> point) {
      var galaxy = new Galaxy(galaxyDistances.size(), point);
      galaxyDistances.computeIfAbsent(galaxy, it -> new HashMap<>());
      columnGalaxyCountMap.put(
          point.x(), columnGalaxyCountMap.computeIfAbsent(point.x(), it -> 0) + 1);
      rowGalaxyCountMap.put(point.y(), rowGalaxyCountMap.computeIfAbsent(point.y(), it -> 0) + 1);
    }

    public long sumOfAllPairsShortestPath(long gapFactor) {
      // We calculate the distances for all pairs a, b where a != b && a.id < b.id
      // Start with a sorted list of all galaxies
      var galaxies = galaxyDistances
          .keySet()
          .stream()
          .sorted(Comparator.comparing(Galaxy::id)).toList();

      // start with the first galaxy encountered
      var currentGalaxy = galaxies.getFirst();

      do {
        for (int i = currentGalaxy.id() + 1; i < galaxies.size(); i++) {
          calculateAndStoreDistance(currentGalaxy, galaxies.get(i), gapFactor);
        }
        currentGalaxy = galaxies.get(currentGalaxy.id + 1);
      } while (currentGalaxy.id() != galaxies.size() - 1); // Skip the last galaxy!

      return galaxyDistances
          .values()
          .stream()
          .mapToLong(it -> it.values().stream().mapToLong(distance -> distance).sum())
          .sum();
    }

    /**
     * The distance between two galaxies is the sum of the differences between their x
     * and y coordinates plus any expanded columns and rows that lie between them. The ID of
     * the first galaxy must be less than the second galaxy!
     *
     * @param first  The first galaxy to compare
     * @param second The second galaxy to compare
     */
    private void calculateAndStoreDistance(
        @NotNull Galaxy first,
        @NotNull Galaxy second,
        long gapFactor
    ) {
      long columnDistance = 0;
      if (!Objects.equals(first.point().x(), second.point().x())) {
        var min = Math.min(first.point().x(), second.point().x());
        var max = Math.max(first.point().x(), second.point().x());
        final var gap = columnGapCountMap
            .getOrDefault(min, EMPTY_MAP).getOrDefault(max, 0);
        columnDistance = (max - min) + (gap * gapFactor);
      }
      long rowDistance = 0;
      if (!Objects.equals(first.point().y(), second.point().y())) {
        var min = Math.min(first.point().y(), second.point().y());
        var max = Math.max(first.point().y(), second.point().y());
        final var gap = rowGapCountMap.getOrDefault(min, EMPTY_MAP).getOrDefault(max, 0);
        rowDistance = (max - min) + (gap * gapFactor);
      }
      galaxyDistances.computeIfAbsent(first, it ->
          new HashMap<>()).put(second, columnDistance + rowDistance);
    }

    public long sumOfOptimizedAllPairsShortestPath(long gapFactor) {
      // start with the first galaxy encountered
      var galaxies = galaxyDistances.keySet();

      long totalSum = greedySum(galaxies, it -> it.point().x(), gapFactor);
      totalSum += greedySum(galaxies, it -> it.point().y(), gapFactor);

      return totalSum;
    }

    private long greedySum(
        @NotNull Set<Galaxy> galaxies,
        @NotNull Function<Galaxy, Integer> keyExtractor,
        long gapFactor
    ) {
      // Start with a sorted list of all galaxies
      List<Galaxy> sorted = galaxies.stream().sorted(Comparator.comparing(keyExtractor)).toList();
      var total = 0L;
      var sum = 0L;
      var gap = 0L;
      // Used for looking up gaps
      var prevCoord = keyExtractor.apply(sorted.getFirst());

      for (int i = 0; i < galaxies.size(); i++) {
        var currentCoord = keyExtractor.apply(sorted.get(i));
        // Add any new gaps to our calculations!
        if (currentCoord - prevCoord > 1) {
          gap += ((currentCoord - prevCoord) - 1) * gapFactor;
        }

        total += (((currentCoord + gap) * i) - sum);
        sum += (currentCoord + gap);

        prevCoord = currentCoord;
      }

      return total;
    }
  }

  private final Universe universe;

  private CosmicExpansion(@NotNull Universe universe) {
    this.universe = universe;
  }

  /**
   * Builds a new CosmicExpansion by constructing a new Universe and populating it with
   * the Galaxies in the specified List of Strings.
   *
   * @param input The List of String to parse into Galaxies.
   * @return A new CosmicExpansion populated with the Galaxies from the List of input Strings.
   */
  public static @NotNull CosmicExpansion buildUniverse(List<String> input) {
    return new CosmicExpansion(Universe.loadUniverse(input));
  }

  /**
   * Calculates and returns the sum of the shortest path for all pairs of Galaxies.<br><br>
   * The GapFactor parameter is used to expand the space between galaxies. A gapFactor of zero (0)
   * would count any row or column between two Galaxies that is completely empty as a single space.
   * A gapFactor of 9 would count the same gap as 10 spaces.<br><br>
   * The Optimized parameter, if true, will use a time and space optimized algorithm for
   * calculating the sum of the shortest paths between all pairs.
   *
   * @param gapFactor The gap factor to use when calculating the gap space.
   * @param optimized If true, will use the time and space optimized algorithm for
   *                  calculating distances.
   * @return The sum of the shortest path for all pairs of Galaxies.
   */
  public long sumOfAllPairsShortestPath(int gapFactor, boolean optimized) {
    if (optimized) {
      return universe.sumOfOptimizedAllPairsShortestPath(gapFactor);
    } else {
      return universe.sumOfAllPairsShortestPath(gapFactor);
    }
  }
}
