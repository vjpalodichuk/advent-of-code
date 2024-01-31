package com.capital7software.aoc.lib.math;

/**
 * To keep the Elves busy, Santa has them deliver some presents by hand, door-to-door.<br>
 * He sends them down a street with infinite houses numbered sequentially: 1, 2, 3, 4, 5,
 * and so on.
 *
 * <p><br>
 * Each Elf is assigned a number, too, and delivers presents to houses based on that number:
 * <ul>
 *     <li>
 *         The first Elf (number 1) delivers presents to every house: 1, 2, 3, 4, 5, ....
 *     </li>
 *     <li>
 *         The second Elf (number 2) delivers presents to every second house: 2, 4, 6, 8, 10, ....
 *     </li>
 *     <li>
 *         Elf number 3 delivers presents to every third house: 3, 6, 9, 12, 15, ....
 *     </li>
 * </ul>
 * There are infinitely many Elves, numbered starting with 1.<br>
 * Each Elf delivers presents equal to ten times his or her number at each house.
 *
 * <p><br>
 * So, the first nine houses on the street end up like this:<br>
 *
 * <p><br>
 * <code>
 * House 1 got 10 presents.<br>
 * House 2 got 30 presents.<br>
 * House 3 got 40 presents.<br>
 * House 4 got 70 presents.<br>
 * House 5 got 60 presents.<br>
 * House 6 got 120 presents.<br>
 * House 7 got 80 presents.<br>
 * House 8 got 150 presents.<br>
 * House 9 got 130 presents.<br>
 * </code>
 * <ul>
 *     <li>
 *         The first house gets 10 presents: it is visited only by Elf 1, which delivers
 *         1 * 10 = 10 presents.
 *     </li>
 *     <li>
 *         The fourth house gets 70 presents, because it is visited by Elves 1, 2, and 4,
 *         for a total of 10 + 20 + 40 = 70 presents.
 *     </li>
 * </ul>
 * What is the lowest house number of the house to get at least as many presents as the
 * number in your puzzle input?
 *
 * <p><br>
 * The Elves decide they don't want to visit an infinite number of houses. Instead,
 * each Elf will stop after delivering presents to 50 houses. To make up for it,
 * they decide to deliver presents equal to eleven times their number at each house.
 *
 * <p><br>
 * With these changes, what is the new lowest house number of the house to get
 * at least as many presents as the number in your puzzle input?
 *
 * @param deliveredPresents The number of presents that have been delivered.
 */
public record InfiniteHouses(int deliveredPresents) {
  public static final int MAX_ITERATIONS = 1_000_000;
  public static final int STANDARD_MULTIPLIER = 10;
  public static final int NEW_MULTIPLIER = 11;
  public static final int MAX_HOUSE_STOPS = 50;

  /**
   * Returns the lowest house number based on the number of delivered presents where each
   * Elf delivers ten times their number of presents to each house, and they deliver to an
   * infinite number of houses.
   *
   * @return The lowest house number based on the number of delivered presents.
   */
  public int lowestHouseNumber() {
    int answer = 0;
    int[] houses = new int[MAX_ITERATIONS];

    // Populate the array
    for (int i = 1; i < MAX_ITERATIONS; i++) {
      for (int j = i; j < MAX_ITERATIONS; j += i) {
        houses[j] += i * STANDARD_MULTIPLIER;
      }
    }

    // Find the lowest house!
    for (int i = 0; i < MAX_ITERATIONS; i++) {
      if (houses[i] >= deliveredPresents) {
        answer = i;
        break;
      }
    }

    return answer;
  }

  /**
   * The Elves have revolted and will stop after delivering presents to 50 houses.
   * To make up for this change, they will deliver eleven times their number at each house instead
   * of the standard ten times.
   *
   * @return The lowest house number based on the number of delivered presents.
   */
  public int lowestHouseNumberNewRules() {
    int answer = 0;
    int[] houses = new int[MAX_ITERATIONS];

    // Populate the array
    for (int i = 1; i < MAX_ITERATIONS; i++) {
      for (int j = i; j <= i * MAX_HOUSE_STOPS && j < MAX_ITERATIONS; j += i) {
        houses[j] += i * NEW_MULTIPLIER;
      }
    }

    // Find the lowest house!
    for (int i = 0; i < MAX_ITERATIONS; i++) {
      if (houses[i] >= deliveredPresents) {
        answer = i;
        break;
      }
    }

    return answer;
  }
}
