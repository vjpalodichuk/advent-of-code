package com.capital7software.aoc2015.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.math.MoleculeFabrication;
import com.capital7software.aoc.lib.util.Pair;

import java.time.Instant;
import java.util.List;
import java.util.Set;

/**
 * --- Day 19: Medicine for Rudolph ---<br>
 * Rudolph the Red-Nosed Reindeer is sick! His nose isn't shining very brightly, and he needs medicine.
 * <p>
 * Red-Nosed Reindeer biology isn't similar to regular reindeer biology; Rudolph is going to need
 * custom-made medicine. Unfortunately, Red-Nosed Reindeer chemistry isn't similar to regular
 * reindeer chemistry, either.
 * <p>
 * The North Pole is equipped with a Red-Nosed Reindeer nuclear fusion/fission plant, capable of
 * constructing any Red-Nosed Reindeer molecule you need. It works by starting with some input
 * molecule and then doing a series of replacements, one per step, until it has the right molecule.
 * <p>
 * However, the machine has to be calibrated before it can be used. Calibration involves
 * determining the number of molecules that can be generated in one step from a given starting point.
 * <p>
 * For capital7software, imagine a simpler machine that supports only the following replacements:
 * <p>
 * H => HO<br>
 * H => OH<br>
 * O => HH<br>
 * Given the replacements above and starting with HOH, the following molecules could be generated:
 * <p><br>
 * HOOH (via H => HO on the first H).<br>
 * HOHO (via H => HO on the second H).<br>
 * OHOH (via H => OH on the first H).<br>
 * HOOH (via H => OH on the second H).<br>
 * HHHH (via O => HH).<br>
 * <p>
 * So, in the capital7software above, there are 4 distinct molecules (not five, because HOOH appears twice)
 * after one replacement from HOH. Santa's favorite molecule, HOHOHO, can become 7 distinct molecules
 * (over nine replacements: six from H, and three from O).
 * <p>
 * The machine replaces without regard for the surrounding characters. For capital7software, given the string H2O,
 * the transition H => OO would result in OO2O.
 * <p>
 * Your puzzle input describes all the possible replacements and, at the bottom, the medicine molecule
 * for which you need to calibrate the machine. How many distinct molecules can be created after all
 * the different ways you can do one replacement on the medicine molecule?
 * <p>
 * Your puzzle answer was 535.
 * <p>
 * --- Part Two ---<br>
 * Now that the machine is calibrated, you're ready to begin molecule fabrication.
 * <p>
 * Molecule fabrication always begins with just a single electron, e, and applying
 * replacements one at a time, just like the ones during calibration.
 * <p>
 * For capital7software, suppose you have the following replacements:
 * <p>
 * e => H<br>
 * e => O<br>
 * H => HO<br>
 * H => OH<br>
 * O => HH<br>
 * <p>
 * If you'd like to make HOH, you start with e, and then make the following replacements:
 * <p>
 * e => O to get O<br>
 * O => HH to get HH<br>
 * H => OH (on the second H) to get HOH<br>
 * <p>
 * So, you could make HOH after 3 steps. Santa's favorite molecule, HOHOHO, can be made in 6 steps.
 * <p>
 * How long will it take to make the medicine? Given the available replacements and the
 * medicine molecule in your puzzle input, what is the fewest number of steps to go from e to the medicine molecule?
 * <p>
 * Your puzzle answer was 212.
 * <p>
 */
public class Day19 implements AdventOfCodeSolution {
    @Override
    public String getDefaultInputFilename() {
        return "inputs/input_day_19-01.txt";
    }

    @Override
    public void runPart1(List<String> input) {
        var start = Instant.now();
        var distinct = calibrate(input).first();
        var end = Instant.now();
        System.out.printf("The number of distinct molecules is: %d%n", distinct);
        printTiming(start, end);
    }

    @Override
    public void runPart2(List<String> input) {
        var start = Instant.now();
        var distinct = fabricate(input).first();
        var end = Instant.now();
        System.out.printf("The fewest number of steps to go from e to the medicine molecule is: %d%n", distinct);
        printTiming(start, end);
    }

    public Pair<Integer, Set<String>> calibrate(List<String> input) {
        var machine = MoleculeFabrication.buildMachine(input);
        return machine.calibrate();
    }

    public Pair<Integer, Set<String>> fabricate(List<String> input) {
        var machine = MoleculeFabrication.buildMachine(input);
        return machine.fabricate();
    }
}
