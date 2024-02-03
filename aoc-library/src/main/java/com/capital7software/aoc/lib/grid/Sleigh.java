package com.capital7software.aoc.lib.grid;

import com.capital7software.aoc.lib.geometry.Direction;
import com.capital7software.aoc.lib.geometry.Point2D;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

/**
 * Santa is delivering presents to an infinite two-dimensional grid of houses.
 *
 * <p><br>
 * He begins by delivering a present to the house at his starting location, and then an elf
 * at the North Pole calls him via radio and tells him where to move next. Moves are always
 * exactly one house to the north (^), south (v), east (&#62;), or west (&#60;). After each
 * move, he delivers another present to the house at his new location.
 *
 * <p><br>
 * However, the elf back at the North Pole has had a little too much eggnog, and so his directions
 * are a little off, and Santa ends up visiting some houses more than once. How many houses receive
 * at least one present?
 *
 * <p><br>
 * For example:
 * <ul>
 *     <li>
 *         &#62; delivers presents to 2 houses: one at the starting location, and one to the east.
 *     </li>
 *     <li>
 *         ^&#62;v&#60; delivers presents to 4 houses in a square, including twice to the house
 *         at his starting/ending location.
 *     </li>
 *     <li>
 *         ^v^v^v^v^v delivers a bunch of presents to some very lucky children at only 2 houses.
 *     </li>
 * </ul>
 * This year, how many houses receive at least one present?
 *
 * <p><br>
 * The next year, to speed up the process, Santa creates a robot version of himself, Robo-Santa,
 * to deliver presents with him.
 *
 * <p><br>
 * Santa and Robo-Santa start at the same location (delivering two presents to the same starting
 * house), then take turns moving based on instructions from the elf, who is eggnoggedly reading
 * from the same script as the previous year.
 *
 * <p><br>
 * This year, how many houses receive at least one present?
 *
 * <p><br>
 * For example:
 * <ul>
 *     <li>
 *         ^v delivers presents to 3 houses, because Santa goes north, and then Robo-Santa goes
 *         south.
 *     </li>
 *     <li>
 *         ^&#62;v&#60; now delivers presents to 3 houses, and Santa and Robo-Santa end up back
 *         where they started.
 *     </li>
 *     <li>
 *         ^v^v^v^v^v now delivers presents to 11 houses, with Santa going one direction and
 *         Robo-Santa going the other.
 *     </li>
 * </ul>
 *
 * @param deliveredGifts The map to hold the delivered gifts per route where the first key is the
 *                       route and then each key is a house and each value is how many visits made
 *                       to that house.
 */
public record Sleigh(
    @NotNull Map<Integer, Map<Point2D<Long>, Long>> deliveredGifts
) {
  /**
   * Instantiates a new Sleigh with the specified Map of delivered gifts and the
   * InfiniteGrid to travel on.
   *
   * @param deliveredGifts The Map of delivered gifts.
   */
  public Sleigh(
      @NotNull Map<Integer, Map<Point2D<Long>, Long>> deliveredGifts
  ) {
    this.deliveredGifts = new HashMap<>(deliveredGifts);
  }

  /**
   * Convenience constructor.
   */
  public Sleigh() {
    this(new HashMap<>());
  }

  /**
   * For example:
   *
   * <p><br>
   * &#62; delivers presents to 2 houses: one at the starting location, and one to the east.
   * ^&#62;v&#60; delivers presents to 4 houses in a square, including twice to the house
   * at his starting/ending location. ^v^v^v^v^v delivers a bunch of presents to some very
   * lucky children at only 2 houses.
   *
   * @param route The route to take.
   * @return The ID of the route.
   */
  public int deliverGifts(@NotNull String route) {
    var start = new Point2D<>(0L, 0L);
    var routeId = deliveredGifts().size();
    var routeMap = deliveredGifts.computeIfAbsent(routeId, it -> new HashMap<>());

    routeMap.put(start, 1L);

    var lastHouse = start;

    for (char c : route.toCharArray()) {
      lastHouse = deliverGift(lastHouse, c, routeMap);
    }

    return routeId;
  }

  /**
   * For example:
   *
   * <p><br>
   * ^v delivers presents to 3 houses, because Santa goes north, and then Robo-Santa goes south.
   * ^&#62;v&#60; now delivers presents to 3 houses, and Santa and Robo-Santa end up back where
   * they started. ^v^v^v^v^v now delivers presents to 11 houses, with Santa going one direction
   * and Robo-Santa going the other.
   *
   * @param route The route to take.
   * @return The ID of the route.
   */
  public int deliverGiftsWithRoboSanta(@NotNull String route) {
    var start = new Point2D<>(0L, 0L);
    var routeId = deliveredGifts().size();
    var routeMap = deliveredGifts.computeIfAbsent(routeId, it -> new HashMap<>());

    routeMap.put(start, 2L);

    var santaLastHouse = start;
    var roboLastHouse = start;

    int i = 0;
    for (char c : route.toCharArray()) {
      if (i % 2 == 0) {
        santaLastHouse = deliverGift(santaLastHouse, c, routeMap);
      } else {
        roboLastHouse = deliverGift(roboLastHouse, c, routeMap);
      }
      i++;
    }

    return routeId;
  }

  /**
   * Delivers a gift to the next house, stores the result in the routeMap and returns the
   * Point2D of the house the gift was delivered to.
   *
   * @param lastHouse The last house a gift was delivered to.
   * @param direction The character representation of the direction (^,>,v,<) to the next house.
   * @param routeMap  The id of the route that is being delivered.
   * @return The Point2D of the house the gift was delivered to.
   */
  private @NotNull Point2D<Long> deliverGift(
      @NotNull Point2D<Long> lastHouse,
      char direction,
      @NotNull Map<Point2D<Long>, Long> routeMap
  ) {
    Point2D<Long> newHouse;
    switch (direction) {
      case '^' -> newHouse = InfiniteGrid.pointInDirection(lastHouse, Direction.NORTH);
      case 'v' -> newHouse = InfiniteGrid.pointInDirection(lastHouse, Direction.SOUTH);
      case '>' -> newHouse = InfiniteGrid.pointInDirection(lastHouse, Direction.EAST);
      case '<' -> newHouse = InfiniteGrid.pointInDirection(lastHouse, Direction.WEST);
      default -> newHouse = lastHouse;
    }

    routeMap.put(newHouse, routeMap.getOrDefault(newHouse, 0L) + 1);
    return newHouse;
  }

  /**
   * Returns the number of unique houses visited for the specified route.
   *
   * @param routeId The id of the route to retrieve.
   * @return The number of unique houses this Sleigh visited for the specified route.
   */
  public long getUniqueHouseCount(int routeId) {
    return deliveredGifts.computeIfAbsent(routeId, it -> new HashMap<>()).size();
  }

  /**
   * Returns an unmodifiable Map of the delivered gifts.
   *
   * @return An unmodifiable Map of the delivered gifts.
   */
  @Override
  public Map<Integer, Map<Point2D<Long>, Long>> deliveredGifts() {
    return Collections.unmodifiableMap(deliveredGifts);
  }
}
