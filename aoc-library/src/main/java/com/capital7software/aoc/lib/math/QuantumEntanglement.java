package com.capital7software.aoc.lib.math;

import com.capital7software.aoc.lib.util.Pair;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * It's Christmas Eve, and Santa is loading up the sleigh for this year's deliveries. However,
 * there's one small problem: he can't get the sleigh to balance. If it isn't balanced,
 * he can't defy physics, and nobody gets presents this year.
 *
 * <p><br>
 * No pressure.
 *
 * <p><br>
 * Santa has provided you a list of the weights of every package he needs to fit on the sleigh.
 * The packages need to be split into three groups of exactly the same weight, and every package
 * has to fit. The first group goes in the passenger compartment of the sleigh, and the second and
 * third go in containers on either side. Only when all three groups weigh exactly the same amount
 * will the sleigh be able to fly. Defying physics has rules, you know!
 *
 * <p><br>
 * Of course, that's not the only problem. The first group - the one going in the
 * passenger compartment - needs as few packages as possible so that Santa has some legroom
 * left over. It doesn't matter how many packages are in either of the other two groups,
 * so long as all the groups weigh the same.
 *
 * <p><br>
 * Furthermore, Santa tells you, if there are multiple ways to arrange the packages such that
 * the fewest possible are in the first group, you need to choose the way where the first
 * group has the smallest quantum entanglement to reduce the chance of any "complications".
 * The quantum entanglement of a group of packages is the product of their weights, that is,
 * the value you get when you multiply their weights together. Only consider quantum entanglement
 * if the first group has the fewest possible number of packages in it and all groups weigh the
 * same amount.
 *
 * <p><br>
 * For example, suppose you have ten packages with weights 1 through 5 and 7 through 11.
 * For this situation, some of the unique first groups, their quantum entanglements,
 * and a way to divide the remaining packages are as follows:
 *
 * <p><br>
 * <code>
 * Group 1; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 * Group 2;&nbsp;&nbsp;Group 3<br>
 * 11 9 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; (QE= 99); &nbsp;10 8 2; &nbsp;&nbsp;7 5 4 3 1<br>
 * 10 9 1 &nbsp;&nbsp;&nbsp; (QE= 90); &nbsp;11 7 2; &nbsp;&nbsp;8 5 4 3<br>
 * 10 8 2 &nbsp;&nbsp;&nbsp; (QE=160); &nbsp;11 9; &nbsp;&nbsp;&nbsp; 7 5 4 3 1<br>
 * 10 7 3 &nbsp;&nbsp;&nbsp; (QE=210); &nbsp;11 9; &nbsp;&nbsp;&nbsp; 8 5 4 2 1<br>
 * 10 5 4 1 &nbsp; (QE=200);&nbsp; 11 9; &nbsp;&nbsp;&nbsp; 8 7 3 2<br>
 * 10 5 3 2 &nbsp; (QE=300);&nbsp; 11 9; &nbsp;&nbsp;&nbsp; 8 7 4 1<br>
 * 10 4 3 2 1 (QE=240);&nbsp; 11 9; &nbsp;&nbsp;&nbsp; 8 7 5<br>
 * 9 8 3 &nbsp;&nbsp;&nbsp;&nbsp; (QE=216);&nbsp; 11 7 2; &nbsp;&nbsp;10 5 4 1<br>
 * 9 7 4 &nbsp;&nbsp;&nbsp;&nbsp; (QE=252);&nbsp; 11 8 1; &nbsp;&nbsp;10 5 3 2<br>
 * 9 5 4 2 &nbsp;&nbsp;&nbsp;(QE=360);&nbsp; 11 8 1; &nbsp;&nbsp;10 7 3<br>
 * 8 7 5 &nbsp;&nbsp;&nbsp;&nbsp; (QE=280);&nbsp; 11 9; &nbsp;&nbsp;&nbsp; 10 4 3 2 1<br>
 * 8 5 4 3 &nbsp;&nbsp;&nbsp;(QE=480);&nbsp; 11 9; &nbsp;&nbsp;&nbsp; 10 7 2 1<br>
 * 7 5 4 3 1 &nbsp;(QE=420); &nbsp;11 9; &nbsp;&nbsp;&nbsp; 10 8 2<br>
 * </code>
 *
 * <p><br>
 * Of these, although 10 9 1 has the smallest quantum entanglement (90), the configuration
 * with only two packages, 11 9, in the passenger compartment gives Santa the most legroom
 * and wins. In this situation, the quantum entanglement for the ideal configuration is
 * therefore 99. Had there been two configurations with only two packages in the first group,
 * the one with the smaller quantum entanglement would be chosen.
 *
 * <p><br>
 * What is the quantum entanglement of the first group of packages in the ideal configuration?
 *
 * <p><br>
 * That's weird... the sleigh still isn't balancing.
 *
 * <p><br>
 * "Ho ho ho", Santa muses to himself. "I forgot the trunk".
 *
 * <p><br>
 * Balance the sleigh again, but this time, separate the packages into four groups instead
 * of three. The other constraints still apply.
 *
 * <p><br>
 * Given the example packages above, this would be some of the new unique first groups,
 * their quantum entanglements, and one way to divide the remaining packages:
 *
 * <p><br>
 * <code>
 * 11 4 &nbsp;&nbsp;&nbsp;&nbsp;(QE=44); 10 5; &nbsp;&nbsp; 9 3 2 1; &nbsp;8 7<br>
 * 10 5 &nbsp;&nbsp;&nbsp; (QE=50); 11 4; &nbsp;&nbsp; 9 3 2 1; &nbsp;8 7<br>
 * 9 5 1 &nbsp;&nbsp; (QE=45); 11 4; &nbsp;&nbsp;10 3 2; &nbsp;&nbsp; 8 7<br>
 * 9 4 2 &nbsp;&nbsp; (QE=72); 11 3 1; 10 5; &nbsp;&nbsp;&nbsp;&nbsp; 8 7<br>
 * 9 3 2 1 &nbsp;(QE=54); 11 4; &nbsp;&nbsp;10 5; &nbsp;&nbsp;&nbsp;&nbsp; 8 7<br>
 * 8 7 &nbsp;&nbsp;&nbsp;&nbsp; (QE=56); 11 4; &nbsp;&nbsp;10 5; &nbsp;&nbsp;&nbsp;&nbsp; 9 3 2 1
 * <br>
 * </code>
 *
 * <p><br>
 * Of these, there are three arrangements that put the minimum (two) number of packages
 * in the first group: 11 4, 10 5, and 8 7. Of these, 11 4 has the lowest quantum entanglement,
 * and so it is selected.
 *
 * <p><br>
 * Now, what is the quantum entanglement of the first group of packages in the ideal configuration?
 */
public class QuantumEntanglement {
  private final List<Integer> source;

  /**
   * Instantiates a new QuantumEntanglement instance with the specified list of packages.
   *
   * @param input The packages for the sleigh.
   */
  public QuantumEntanglement(List<String> input) {
    source = new ArrayList<>();

    source.addAll(parseInput(input));
  }

  private Collection<Integer> parseInput(List<String> input) {
    return input.stream().map(Integer::parseInt).toList();
  }

  /**
   * Returns the product of the weight of the packages in the smallest set that is capable of
   * balancing the sleigh into k number of equal weight sets! Please note that the returned value
   * is the lowest QE score among sets that contain the fewest number of elements. That does mean
   * it is possible for there to be a lower QE score but only in a subset with more elements.
   *
   * @param k The number of sets to balance the packages into.
   * @return the product of the weight of the packages in the smallest set that is capable of
   *     balancing the sleigh into k number of equal weight sets!
   */
  public long getLowestQeScore(int k) {
    var totalSum = source.stream().mapToInt(it -> it).sum();

    if (totalSum % k != 0) {
      return -1;
    }

    var targetSum = totalSum / k;

    var lowest = calculateLowestQeScore(
        source,
        targetSum,
        k,
        0,
        1,
        0,
        0,
        new Pair<>(-1L, 0)
    );
    return lowest.first();
  }

  private static Pair<Long, Integer> calculateLowestQeScore(
      List<Integer> items,
      int targetSum,
      int requiredPartitions,
      int currentIndex,
      long totalScore,
      int totalSum,
      int itemsUsed, // Our bitmask indicating which elements have been used!,
      Pair<Long, Integer> bestSoFar
  ) {
    // Bail early if we have already calculated a better result!
    int setSize = Integer.bitCount(itemsUsed);
    if (bestSoFar.first() != -1 && setSize >= bestSoFar.second()
        && totalScore >= bestSoFar.first()) {
      return bestSoFar;
    }

    // Have to validate that the remaining sets can be created based off of what we have
    // created so far!
    if (totalSum == targetSum) {
      if (canCreateRemainingEqualSets(
          items,
          targetSum,
          requiredPartitions - 1,
          0,
          0,
          itemsUsed
      )) {
        // We can split the remainder so return our result!
        return new Pair<>(totalScore, setSize);
      } else {
        return new Pair<>(-1L, setSize);
      }
    }

    // If we have gone through all the items or have exceeded the targetSum, bail!
    if (currentIndex >= items.size() || totalSum > targetSum) {
      return new Pair<>(-1L, setSize);
    }

    // We have to split the recursion in two where we either add to the totals or don't.
    // In either case we move to the next index.
    var added = calculateLowestQeScore(
        items,
        targetSum,
        requiredPartitions,
        currentIndex + 1,
        totalScore * items.get(currentIndex),
        totalSum + items.get(currentIndex),
        itemsUsed | (1 << currentIndex),
        bestSoFar
    );
    if (bestSoFar.first() == -1) {
      bestSoFar = added;
    } else if (added.first() != -1) {
      if (added.second() < bestSoFar.second()) {
        // Using fewer packages is better than more packages but a lower QE score.
        bestSoFar = added;
      } else if (added.second().equals(bestSoFar.second()) && added.first() < bestSoFar.first()) {
        // Used same number of packages, but we have a better score!
        bestSoFar = added;
      }
    }

    var notAdded = calculateLowestQeScore(
        items,
        targetSum,
        requiredPartitions,
        currentIndex + 1,
        totalScore,
        totalSum,
        itemsUsed,
        bestSoFar
    );
    if (added.first() == -1) {
      return notAdded;
    }
    if (notAdded.first() == -1) {
      return added;
    }
    if (added.second() < notAdded.second()) {
      // Using fewer packages is better than more packages but a lower QE score.
      return added;
    } else if (added.second().equals(notAdded.second()) && added.first() < notAdded.first()) {
      // Used same number of packages, but we have a better score!
      return added;
    }
    return notAdded;
  }

  private static boolean canCreateRemainingEqualSets(
      List<Integer> items,
      int targetSum,
      int requiredPartitions,
      int currentIndex,
      int totalSum,
      int itemsUsed // Our bitmask indicating which elements have been used!
  ) {
    if (totalSum > targetSum) {
      return false;
    }
    if (currentIndex >= items.size()) {
      return false;
    }
    if (requiredPartitions == 0) {
      // If we don't require anymore partitions then return true if we
      // have put every element into a set!
      return itemsUsed == ((1 << items.size()) - 1);
    }
    if (totalSum == targetSum) {
      // Recur requiring one less partition! Reset the index and total sum!
      return canCreateRemainingEqualSets(
          items,
          targetSum,
          requiredPartitions - 1,
          0,
          0,
          itemsUsed
      );
    }
    if ((itemsUsed & (1 << currentIndex)) != 0) {
      // Recur moving to the next index as we have already used this index!
      return canCreateRemainingEqualSets(
          items,
          targetSum,
          requiredPartitions,
          currentIndex + 1,
          totalSum,
          itemsUsed
      );
    }
    // We have to check both conditions of adding the currentIndex value and not adding its value
    // to the total sum. For the one that we add its element to the sum, we also add the index to
    // the used indices. In both cases, we move to the next index.
    return canCreateRemainingEqualSets(
        items,
        targetSum,
        requiredPartitions,
        currentIndex + 1,
        totalSum + items.get(currentIndex),
        itemsUsed | (1 << currentIndex)
    ) || canCreateRemainingEqualSets(
        items,
        targetSum,
        requiredPartitions,
        currentIndex + 1,
        totalSum,
        itemsUsed
    );
  }
}
