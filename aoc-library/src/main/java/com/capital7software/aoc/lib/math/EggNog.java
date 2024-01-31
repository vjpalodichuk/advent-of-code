package com.capital7software.aoc.lib.math;

import com.capital7software.aoc.lib.util.Pair;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.jetbrains.annotations.NotNull;

/**
 * The elves bought too much eggnog again - 150 liters this time. To fit it all into your
 * refrigerator, you'll need to move it into smaller containers. You take an inventory of
 * the capacities of the available containers.
 *
 * <p><br>
 * For example, suppose you have containers of size 20, 15, 10, 5, and 5 liters. If you need
 * to store 25 liters, there are four ways to do it:
 *
 * <p><br>
 * 15 and 10<br>
 * 20 and 5 (the first 5)<br>
 * 20 and 5 (the second 5)<br>
 * 15, 5, and 5<br>
 *
 * <p><br>
 * Filling all containers entirely, how many different combinations of containers can exactly fit
 * all 150 liters of eggnog?
 *
 * <p><br>
 * While playing with all the containers in the kitchen, another load of eggnog arrives!
 * The shipping and receiving department is requesting as many containers as you can spare.
 *
 * <p><br>
 * Find the minimum number of containers that can exactly fit all 150 liters of eggnog.
 * How many different ways can you fill that number of containers and still hold exactly 150 litres?
 *
 * <p><br>
 * In the example above, the minimum number of containers was two.
 * There were three ways to use that many containers, and so the answer there would be 3.
 *
 * @param liters     The total amount of Eggnog that needs to be stored.
 * @param containers The list of container sizes that are available to store the Eggnog.
 */
public record EggNog(long liters, @NotNull List<Long> containers) {
  /**
   * Instantiates a new EggNog instance that owns the list of containers.
   *
   * @param liters     The amount of eggnog to store.
   * @param containers The sizes of the available containers to store the eggnog.
   */
  public EggNog(long liters, @NotNull List<Long> containers) {
    this.liters = liters;
    this.containers = new ArrayList<>(containers);
  }

  /**
   * Returns an unmodifiable List of the containers held by this instance.
   *
   * @return An unmodifiable List of the containers held by this instance.
   */
  @Override
  public List<Long> containers() {
    return Collections.unmodifiableList(containers);
  }

  /**
   * Parses the list of containers and returns a new instance of EggNog with the specified
   * number of liters and a list of container sizes from the input list of strings.
   *
   * @param liters The total amount of Eggnog that needs to be stored.
   * @param input  The list of strings where each string is a non-zero integer indicating
   *               its capacity.
   * @return A new instance of EggNog with the specified liters and list of available
   *     container capacities.
   */
  public static @NotNull EggNog parse(long liters, @NotNull List<String> input) {
    return new EggNog(liters, input.stream().map(Long::parseLong).toList());
  }

  /**
   * Counts and returns a Pair of the number of different combinations that can be formed from
   * the list of container capacities for this EggNog instance to exactly contain liters amount
   * of EggNog.
   *
   * @return The first element of the pair is the number of combinations and the second
   *     element is a List of the actual combinations.
   */
  public @NotNull Pair<Integer, List<List<Long>>> combinations() {
    var count = new AtomicInteger(0);
    List<List<Long>> usedList = new ArrayList<>();
    List<Long> currentUsed = new ArrayList<>();

    countCombinations(0, 0, count, currentUsed, usedList);
    return new Pair<>(count.get(), usedList);
  }

  private void countCombinations(
      long currentLength,
      int index,
      AtomicInteger count,
      List<Long> currentUsed,
      List<List<Long>> usedList
  ) {
    for (int i = index; i < containers.size(); i++) {
      currentUsed.add(containers.get(i));
      if (currentLength + containers.get(i) == liters) {
        count.incrementAndGet();
        usedList.add(new ArrayList<>(currentUsed));
      } else if (currentLength + containers.get(i) < liters) {
        countCombinations(
            currentLength + containers.get(i),
            i + 1,
            count,
            currentUsed,
            usedList
        );
      }
      if (!currentUsed.isEmpty()) {
        currentUsed.removeLast();
      }
    }
  }
}
