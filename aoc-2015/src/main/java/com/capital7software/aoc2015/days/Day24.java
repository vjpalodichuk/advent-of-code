package com.capital7software.aoc2015.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.math.QuantumEntanglement;

import java.time.Instant;
import java.util.List;

/**
 * --- Day 24: It Hangs in the Balance ---<br>
 * It's Christmas Eve, and Santa is loading up the sleigh for this year's deliveries. However,
 * there's one small problem: he can't get the sleigh to balance. If it isn't balanced,
 * he can't defy physics, and nobody gets presents this year.
 * <p><br>
 * No pressure.
 * <p><br>
 * Santa has provided you a list of the weights of every package he needs to fit on the sleigh.
 * The packages need to be split into three groups of exactly the same weight, and every package has to fit.
 * The first group goes in the passenger compartment of the sleigh, and the second and
 * third go in containers on either side. Only when all three groups weigh exactly the same amount
 * will the sleigh be able to fly. Defying physics has rules, you know!
 * <p><br>
 * Of course, that's not the only problem. The first group - the one going in the
 * passenger compartment - needs as few packages as possible so that Santa has some legroom left over.
 * It doesn't matter how many packages are in either of the other two groups,
 * so long as all the groups weigh the same.
 * <p><br>
 * Furthermore, Santa tells you, if there are multiple ways to arrange the packages such that
 * the fewest possible are in the first group, you need to choose the way where the first
 * group has the smallest quantum entanglement to reduce the chance of any "complications".
 * The quantum entanglement of a group of packages is the product of their weights, that is,
 * the value you get when you multiply their weights together. Only consider quantum entanglement
 * if the first group has the fewest possible number of packages in it and all groups weigh the same amount.
 * <p><br>
 * For capital7software, suppose you have ten packages with weights 1 through 5 and 7 through 11.
 * For this situation, some of the unique first groups, their quantum entanglements,
 * and a way to divide the remaining packages are as follows:
 * <p><br>
 * Group 1;             Group 2; Group 3<br>
 * 11 9       (QE= 99); 10 8 2;  7 5 4 3 1<br>
 * 10 9 1     (QE= 90); 11 7 2;  8 5 4 3<br>
 * 10 8 2     (QE=160); 11 9;    7 5 4 3 1<br>
 * 10 7 3     (QE=210); 11 9;    8 5 4 2 1<br>
 * 10 5 4 1   (QE=200); 11 9;    8 7 3 2<br>
 * 10 5 3 2   (QE=300); 11 9;    8 7 4 1<br>
 * 10 4 3 2 1 (QE=240); 11 9;    8 7 5<br>
 * 9 8 3      (QE=216); 11 7 2;  10 5 4 1<br>
 * 9 7 4      (QE=252); 11 8 1;  10 5 3 2<br>
 * 9 5 4 2    (QE=360); 11 8 1;  10 7 3<br>
 * 8 7 5      (QE=280); 11 9;    10 4 3 2 1<br>
 * 8 5 4 3    (QE=480); 11 9;    10 7 2 1<br>
 * 7 5 4 3 1  (QE=420); 11 9;    10 8 2<br>
 * <p><br>
 * Of these, although 10 9 1 has the smallest quantum entanglement (90), the configuration with only
 * two packages, 11 9, in the passenger compartment gives Santa the most legroom and wins. In this situation,
 * the quantum entanglement for the ideal configuration is therefore 99. Had there been two configurations
 * with only two packages in the first group, the one with the smaller quantum entanglement would be chosen.
 * <p><br>
 * What is the quantum entanglement of the first group of packages in the ideal configuration?
 * <p>
 * Your puzzle answer was 10723906903.
 * <p>
 * --- Part Two ---<br>
 * That's weird... the sleigh still isn't balancing.
 * <p><br>
 * "Ho ho ho", Santa muses to himself. "I forgot the trunk".
 * <p><br>
 * Balance the sleigh again, but this time, separate the packages into four groups instead of three.
 * The other constraints still apply.
 * <p><br>
 * Given the capital7software packages above, this would be some of the new unique first groups,
 * their quantum entanglements, and one way to divide the remaining packages:
 * <p><br>
 * 11 4    (QE=44); 10 5;   9 3 2 1; 8 7<br>
 * 10 5    (QE=50); 11 4;   9 3 2 1; 8 7<br>
 * 9 5 1   (QE=45); 11 4;   10 3 2;  8 7<br>
 * 9 4 2   (QE=72); 11 3 1; 10 5;    8 7<br>
 * 9 3 2 1 (QE=54); 11 4;   10 5;    8 7<br>
 * 8 7     (QE=56); 11 4;   10 5;    9 3 2 1<br>
 * <p><br>
 * Of these, there are three arrangements that put the minimum (two) number of packages
 * in the first group: 11 4, 10 5, and 8 7. Of these, 11 4 has the lowest quantum entanglement, and so it is selected.
 * <p><br>
 * Now, what is the quantum entanglement of the first group of packages in the ideal configuration?
 * <p>
 * Your puzzle answer was 74850409.
 * <p>
 */
public class Day24 implements AdventOfCodeSolution {
    @Override
    public String getDefaultInputFilename() {
        return "inputs/input_day_24-01.txt";
    }

    @Override
    public void runPart1(List<String> input) {
        var start = Instant.now();
        var lowest = getLowestQEScore(input, 3);
        var end = Instant.now();
        System.out.printf("The lowest QE Score with 3 partitions is: %d%n", lowest);
        printTiming(start, end);
    }

    @Override
    public void runPart2(List<String> input) {
        var start = Instant.now();
        var lowest = getLowestQEScore(input, 4);
        var end = Instant.now();
        System.out.printf("The lowest QE Score with 4 partitions is: %d%n", lowest);
        printTiming(start, end);
    }

    public long getLowestQEScore(List<String> input, int partitions) {
        var sleigh = new QuantumEntanglement(input);
        return sleigh.getLowestQEScore(partitions);
    }
}
