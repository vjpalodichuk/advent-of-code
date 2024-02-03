package com.capital7software.aoc.aoc2015aoc.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.math.MoleculeFabrication;
import com.capital7software.aoc.lib.util.Pair;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * --- Day 19: Medicine for Rudolph ---<br><br>
 * Rudolph the Red-Nosed Reindeer is sick! His nose isn't shining very brightly, and he needs
 * medicine.
 *
 * <p><br>
 * Red-Nosed Reindeer biology isn't similar to regular reindeer biology; Rudolph is going to need
 * custom-made medicine. Unfortunately, Red-Nosed Reindeer chemistry isn't similar to regular
 * reindeer chemistry, either.
 *
 * <p><br>
 * The North Pole is equipped with a Red-Nosed Reindeer nuclear fusion/fission plant, capable of
 * constructing any Red-Nosed Reindeer molecule you need. It works by starting with some input
 * molecule and then doing a series of replacements, one per step, until it has the right molecule.
 *
 * <p><br>
 * However, the machine has to be calibrated before it can be used. Calibration involves
 * determining the number of molecules that can be generated in one step from a given
 * starting point.
 *
 * <p><br>
 * For example, imagine a simpler machine that supports only the following replacements:
 *
 * <p><br>
 * <code>
 * H =&#62; HO<br>
 * H =&#62; OH<br>
 * O =&#62; HH<br>
 * </code>
 *
 * <p><br>
 * Given the replacements above and starting with HOH, the following molecules could be generated:
 *
 * <p><br>
 * <code>
 * HOOH (via H =&#62; HO on the first H).<br>
 * HOHO (via H =&#62; HO on the second H).<br>
 * OHOH (via H =&#62; OH on the first H).<br>
 * HOOH (via H =&#62; OH on the second H).<br>
 * HHHH (via O =&#62; HH).<br>
 * </code>
 *
 * <p><br>
 * So, in the example above, there are 4 distinct molecules (not five, because HOOH appears twice)
 * after one replacement from HOH. Santa's favorite molecule, HOHOHO, can become 7 distinct
 * molecules (over nine replacements: six from H, and three from O).
 *
 * <p><br>
 * The machine replaces without regard for the surrounding characters. For example, given the
 * string H2O, the transition H =&#62; OO would result in OO2O.
 *
 * <p><br>
 * Your puzzle input describes all the possible replacements and, at the bottom, the medicine
 * molecule for which you need to calibrate the machine. How many distinct molecules can be
 * created after all the different ways you can do one replacement on the medicine molecule?
 *
 * <p><br>
 * Your puzzle answer was 535.
 *
 * <p><br>
 * --- Part Two ---<br><br>
 * Now that the machine is calibrated, you're ready to begin molecule fabrication.
 *
 * <p><br>
 * Molecule fabrication always begins with just a single electron, e, and applying
 * replacements one at a time, just like the ones during calibration.
 *
 * <p><br>
 * For example, suppose you have the following replacements:
 *
 * <p><br>
 * <code>
 * e =&#62; H<br>
 * e =&#62; O<br>
 * H =&#62; HO<br>
 * H =&#62; OH<br>
 * O =&#62; HH<br>
 * </code>
 *
 * <p><br>
 * If you'd like to make HOH, you start with e, and then make the following replacements:
 *
 * <p><br>
 * <code>
 * e =&#62; O to get O<br>
 * O =&#62; HH to get HH<br>
 * H =&#62; OH (on the second H) to get HOH<br>
 * </code>
 *
 * <p><br>
 * So, you could make HOH after 3 steps. Santa's favorite molecule, HOHOHO, can be made in 6 steps.
 *
 * <p><br>
 * How long will it take to make the medicine? Given the available replacements and the medicine
 * molecule in your puzzle input, what is the fewest number of steps to go from e to the
 * medicine molecule?
 *
 * <p><br>
 * Your puzzle answer was 212.
 */
public class Day19 implements AdventOfCodeSolution {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day19.class);

  /**
   * Instantiates the solution instance.
   */
  public Day19() {

  }

  @Override
  public String getDefaultInputFilename() {
    return "inputs/input_day_19-01.txt";
  }

  @Override
  public void runPart1(List<String> input) {
    var start = Instant.now();
    var distinct = calibrate(input).first();
    var end = Instant.now();
    LOGGER.info("The number of distinct molecules is: {}", distinct);
    logTimings(LOGGER, start, end);
  }

  @Override
  public void runPart2(List<String> input) {
    var start = Instant.now();
    var distinct = fabricate(input).first();
    var end = Instant.now();
    LOGGER.info("The fewest number of steps to go from e to the medicine molecule is: {}",
                distinct);
    logTimings(LOGGER, start, end);
  }

  /**
   * Returns the number of distinct molecules from the calibration process.
   *
   * @param input The replacement String and the molecule to fabricate.
   * @return The number of distinct molecules from the calibration process.
   */
  public Pair<Integer, Set<String>> calibrate(List<String> input) {
    var machine = MoleculeFabrication.buildMachine(input);
    return machine.calibrate();
  }

  /**
   * Returns the fewest number of steps to fabricate the desired molecule
   * from the input Strings.
   *
   * @param input The replacement String and the molecule to fabricate.
   * @return The fewest number of steps to fabricate the desired molecule.
   */
  public Pair<Integer, Set<String>> fabricate(List<String> input) {
    var machine = MoleculeFabrication.buildMachine(input);
    return machine.fabricate();
  }
}
