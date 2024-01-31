package com.capital7software.aoc.lib.math;

import com.capital7software.aoc.lib.util.Pair;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.jetbrains.annotations.NotNull;

/**
 * Determines the number of molecules that can be generated in one step from a given starting point.
 *
 * <p><br>
 * For example, imagine a machine that supports only the following replacements:
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
 * @param molecule     The molecule to fabricate.
 * @param replacements The grammar rules for building the molecule.
 */
public record MoleculeFabrication(@NotNull String molecule,
                                  @NotNull List<Pair<String, String>> replacements) {
  /**
   * Instantiates a new MoleculeFabrication instance with the specified molecule and List of
   * replacements to make that this instance owns.
   *
   * @param molecule     The molecule to fabricate.
   * @param replacements The list of replacements to make.
   */
  public MoleculeFabrication(
      @NotNull String molecule,
      @NotNull List<Pair<String, String>> replacements
  ) {
    this.molecule = molecule;
    this.replacements = new ArrayList<>(
        replacements.stream().map(it -> new Pair<>(it.first(), it.second())).toList()
    );
  }

  /**
   * Returns an unmodifiable List of Pairs that specify the replacements to make.
   *
   * @return An unmodifiable List of Pairs that specify the replacements to make.
   */
  @Override
  public List<Pair<String, String>> replacements() {
    return replacements.stream().map(it -> new Pair<>(it.first(), it.second())).toList();
  }

  /**
   * Builds a new MoleculeFabrication instance from the specified input.<br>
   * This method supports the following format:<br>
   * <ul>
   *     <li>
   *         Al => ThF
   *     </li>
   *     <li>
   *         Al => ThRnFAr
   *     </li>
   *     <li>
   *         H => NRnFYFAr
   *     </li>
   *     <li>
   *         BLANK LINE
   *     </li>
   *     <li>
   *         CRnCaCaCaSiRnBPTiMgArSiRnSiRnMgArSiRnCaFArTiTiBSiThFYCaFArCaCaSiThCaPBSiThSiThC
   *         aCaPTiRnP
   *     </li>
   * </ul>
   * The first lines define the replacements. After all the replacements are defined there is a
   * blank line and then the molecule string.
   *
   * @param input The list of input strings to parse.
   * @return A new MoleculeFabrication instance from the specified input.
   */
  public static @NotNull MoleculeFabrication buildMachine(@NotNull List<String> input) {
    List<Pair<String, String>> replacements = new ArrayList<>(input.size());
    var molecule = new AtomicReference<>("");
    var last = new AtomicBoolean(false);

    input.forEach(line -> {
      if (last.get()) {
        molecule.set(line);
        last.set(false);
      } else if (line.isBlank()) {
        last.set(true);
      } else {
        var split = line.split(" => ");
        replacements.add(new Pair<>(split[0].trim(), split[1].trim()));
      }
    });

    return new MoleculeFabrication(molecule.get(), replacements);
  }

  /**
   * Calibrates the machine by doing single step replacements on the molecule string with the
   * loaded replacements.
   *
   * @return A Pair where the first property is the number of unique replacements and the second
   *     is the set of unique replacements.
   */
  public @NotNull Pair<Integer, Set<String>> calibrate() {
    Set<String> molecules = new HashSet<>();

    for (var replacement : replacements) {
      var index = molecule.indexOf(replacement.first());
      while (index != -1) {
        var temp = replace(molecule, replacement.first(), replacement.second(), index);

        molecules.add(temp);

        if (index + 1 < molecule.length()) {
          index = molecule.indexOf(replacement.first(), index + 1);
        } else {
          index = -1;
        }
      }
    }

    return new Pair<>(molecules.size(), molecules);
  }

  /**
   * Fabricates the medicine molecule starting with the e replacement. Returns a Pair where
   * the first property is the fewest number of steps to fabricate the molecule and the second
   * property contains the molecule from each step.
   *
   * @return A Pair where the first property is the fewest number of steps to fabricate
   *     the molecule and the second property contains the molecule from each step.
   */
  public @NotNull Pair<Integer, Set<String>> fabricate() {
    var temp = molecule;
    var steps = 0;
    Set<String> stepsSet = new HashSet<>();

    while (!temp.equals("e")) {
      if (temp.matches("^e+$")) {
        break; // entire string has been replaced with e!
      }

      for (var replacement : replacements) {
        if (temp.contains(replacement.second())) {
          temp = replace(
              temp,
              replacement.second(),
              replacement.first(),
              temp.lastIndexOf(replacement.second())
          );
          steps++;
          stepsSet.add(temp);
        }
      }
    }

    return new Pair<>(steps, stepsSet);
  }

  /**
   * Performs a replacement in String t by replacing the characters within t at the specified index
   * and length i with String o. Returns a new String with the replacement substring.
   *
   * @param t     The string that contains the substring to replace.
   * @param i     The substring to replace.
   * @param o     The replacement substring to add.
   * @param index The index of the start of the replacement in String t.
   * @return A new String with the replacement substring.
   */
  public static @NotNull String replace(String t, String i, String o, int index) {
    return t.substring(0, index) + o + t.substring(index + i.length());
  }
}
