package com.capital7software.aoc.aoc2015aoc.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.math.QuantumEntanglement;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * --- Day 24: It Hangs in the Balance ---<br><br>
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
 * Your puzzle answer was 10723906903.
 *
 * <p><br>
 * --- Part Two ---<br><br>
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
 *
 * <p><br>
 * Your puzzle answer was 74850409.
 */
public class Day24 implements AdventOfCodeSolution {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day24.class);

  /**
   * Instantiates the solution instance.
   */
  public Day24() {

  }

  @Override
  public String getDefaultInputFilename() {
    return "inputs/input_day_24-01.txt";
  }

  @Override
  public void runPart1(List<String> input) {
    var start = Instant.now();
    var lowest = getLowestQeScore(input, 3);
    var end = Instant.now();
    LOGGER.info("The lowest QE Score with 3 partitions is: {}", lowest);
    logTimings(LOGGER, start, end);
  }

  @Override
  public void runPart2(List<String> input) {
    var start = Instant.now();
    var lowest = getLowestQeScore(input, 4);
    var end = Instant.now();
    LOGGER.info("The lowest QE Score with 4 partitions is: {}", lowest);
    logTimings(LOGGER, start, end);
  }

  /**
   * Returns the lowest Quantum Entanglement Score for the specified number of
   * gifts and the number of equal partitions to break them up into.
   *
   * @param input      The gifts to put in to equal weight sets.
   * @param partitions The number of sets to put the gifts in.
   * @return the lowest Quantum Entanglement Score.
   */
  public long getLowestQeScore(List<String> input, int partitions) {
    var sleigh = new QuantumEntanglement(input);
    return sleigh.getLowestQeScore(partitions);
  }
}
