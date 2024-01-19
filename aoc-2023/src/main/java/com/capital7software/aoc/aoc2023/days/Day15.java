package com.capital7software.aoc.aoc2023.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.analysis.LensLibrary;
import com.capital7software.aoc.lib.string.HashString;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.List;
import java.util.logging.Logger;

/**
 * --- Day 15: Lens Library ---<br><br>
 * The newly-focused parabolic reflector dish is sending all the collected light to a point on the
 * side of yet another mountain - the largest mountain on Lava Island. As you approach the mountain,
 * you find that the light is being collected by the wall of a large facility embedded in the mountainside.
 * <p><br>
 * You find a door under a large sign that says "Lava Production Facility" and next to a smaller sign
 * that says "Danger - Personal Protective Equipment required beyond this point".
 * <p><br>
 * As you step inside, you are immediately greeted by a somewhat panicked reindeer wearing goggles and
 * a loose-fitting hard hat. The reindeer leads you to a shelf of goggles and hard hats (you quickly
 * find some that fit) and then further into the facility. At one point, you pass a button with a faint
 * snout mark and the label "PUSH FOR HELP". No wonder you were loaded into that trebuchet so quickly!
 * <p><br>
 * You pass through a final set of doors surrounded with even more warning signs and into what must be
 * the room that collects all the light from outside. As you admire the large assortment of lenses
 * available to further focus the light, the reindeer brings you a book titled "Initialization Manual".
 * <p><br>
 * "Hello!", the book cheerfully begins, apparently unaware of the concerned reindeer reading over your
 * shoulder. "This procedure will let you bring the Lava Production Facility online - all without
 * burning or melting anything unintended!"
 * <p><br>
 * "Before you begin, please be prepared to use the Holiday ASCII String Helper algorithm (appendix 1A)."
 * You turn to appendix 1A. The reindeer leans closer with interest.
 * <p><br>
 * The HASH algorithm is a way to turn any string of characters into a single number in the range 0 to 255.
 * To run the HASH algorithm on a string, start with a current value of 0. Then, for each character in
 * the string starting from the beginning:
 * <ol>
 *     <li>
 *         Determine the ASCII code for the current character of the string.
 *     </li>
 *     <li>
 *         Increase the current value by the ASCII code you just determined.
 *     </li>
 *     <li>
 *         Set the current value to itself multiplied by 17.
 *     </li>
 *     <li>
 *         Set the current value to the remainder of dividing itself by 256.
 *     </li>
 * </ol>
 * After following these steps for each character in the string in order, the current value is the
 * output of the HASH algorithm.
 * <p><br>
 * So, to find the result of running the HASH algorithm on the string HASH:
 * <p><br>
 * The current value starts at 0.<br>
 * The first character is H; its ASCII code is 72.<br>
 * The current value increases to 72.<br>
 * The current value is multiplied by 17 to become 1224.<br>
 * The current value becomes 200 (the remainder of 1224 divided by 256).<br>
 * The next character is A; its ASCII code is 65.<br>
 * The current value increases to 265.<br>
 * The current value is multiplied by 17 to become 4505.<br>
 * The current value becomes 153 (the remainder of 4505 divided by 256).<br>
 * The next character is S; its ASCII code is 83.<br>
 * The current value increases to 236.<br>
 * The current value is multiplied by 17 to become 4012.<br>
 * The current value becomes 172 (the remainder of 4012 divided by 256).<br>
 * The next character is H; its ASCII code is 72.<br>
 * The current value increases to 244.<br>
 * The current value is multiplied by 17 to become 4148.<br>
 * The current value becomes 52 (the remainder of 4148 divided by 256).<br>
 * <p><br>
 * So, the result of running the HASH algorithm on the string HASH is 52.
 * <p><br>
 * The initialization sequence (your puzzle input) is a comma-separated list of steps to start the Lava
 * Production Facility. Ignore newline characters when parsing the initialization sequence.
 * To verify that your HASH algorithm is working, the book offers the sum of the result of running
 * the HASH algorithm on each step in the initialization sequence.
 * <p><br>
 * For example:
 * <p><br>
 * rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7
 * <p><br>
 * This initialization sequence specifies 11 individual steps; the result of running the HASH
 * algorithm on each of the steps is as follows:
 * <p><br>
 * <code>
 * rn=1 becomes 30.<br>
 * cm- becomes 253.<br>
 * qp=3 becomes 97.<br>
 * cm=2 becomes 47.<br>
 * qp- becomes 14.<br>
 * pc=4 becomes 180.<br>
 * ot=9 becomes 9.<br>
 * ab=5 becomes 197.<br>
 * pc- becomes 48.<br>
 * pc=6 becomes 214.<br>
 * ot=7 becomes 231.<br>
 * </code>
 * <p><br>
 * In this example, the sum of these results is 1320. Unfortunately, the reindeer has stolen the page
 * containing the expected verification number and is currently running around the facility with it excitedly.
 * <p><br>
 * Run the HASH algorithm on each step in the initialization sequence. What is the sum of the results?
 * (The initialization sequence is one long line; be careful when copy-pasting it.)
 * <p><br>
 * Your puzzle answer was 521434.
 * <p><br>
 * --- Part Two ---<br><br>
 * You convince the reindeer to bring you the page; the page confirms that your HASH algorithm is working.
 * <p><br>
 * The book goes on to describe a series of 256 boxes numbered 0 through 255. The boxes are arranged
 * in a line starting from the point where light enters the facility. The boxes have holes that allow
 * light to pass from one box to the next all the way down the line.
 * <p><br>
 * <code>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+-----+  +-----+  &nbsp;&nbsp;&nbsp;&nbsp;+-----+<br>
 * Light | Box |  | Box |   ...   | Box |<br>
 * -----------------------------------------><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;0&nbsp;&nbsp;|&nbsp;|&nbsp;&nbsp;1&nbsp;&nbsp;|&nbsp;...&nbsp;|&nbsp;255&nbsp;|<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+-----+  +-----+  &nbsp;&nbsp;&nbsp;&nbsp;+-----+<br>
 * </code>
 * <p><br>
 * Inside each box, there are several lens slots that will keep a lens correctly positioned to focus
 * light passing through the box. The side of each box has a panel that opens to allow you to insert
 * or remove lenses as necessary.
 * <p><br>
 * Along the wall running parallel to the boxes is a large library containing lenses organized by
 * focal length ranging from 1 through 9. The reindeer also brings you a small handheld label printer.
 * <p><br>
 * The book goes on to explain how to perform each step in the initialization sequence, a process
 * it calls the Holiday ASCII String Helper Manual Arrangement Procedure, or HASHMAP for short.
 * <p><br>
 * Each step begins with a sequence of letters that indicate the label of the lens on which the
 * step operates. The result of running the HASH algorithm on the label indicates the correct box for that step.
 * <p><br>
 * The label will be immediately followed by a character that indicates the operation to perform:
 * either an equals sign (=) or a dash (-).
 * <p><br>
 * If the operation character is a dash (-), go to the relevant box and remove the lens with the
 * given label if it is present in the box. Then, move any remaining lenses as far forward in the
 * box as they can go without changing their order, filling any space made by removing the indicated lens.
 * (If no lens in that box has the given label, nothing happens.)
 * <p><br>
 * If the operation character is an equals sign (=), it will be followed by a number indicating
 * the focal length of the lens that needs to go into the relevant box; be sure to use the label
 * maker to mark the lens with the label given in the beginning of the step so you can find it later.
 * There are two possible situations:
 * <p><br>
 * If there is already a lens in the box with the same label, replace the old lens with the new
 * lens: remove the old lens and put the new lens in its place, not moving any other lenses in the box.
 * If there is not already a lens in the box with the same label, add the lens to the box immediately
 * behind any lenses already in the box. Don't move any of the other lenses when you do this.
 * If there aren't any lenses in the box, the new lens goes all the way to the front of the box.
 * Here is the contents of every box after each step in the example initialization sequence above:
 * <p><br>
 * After "rn=1":<br>
 * Box 0: [rn 1]<br>
 * <p><br>
 * After "cm-":<br>
 * Box 0: [rn 1]<br>
 * <p><br>
 * After "qp=3":<br>
 * Box 0: [rn 1]<br>
 * Box 1: [qp 3]<br>
 * <p><br>
 * After "cm=2":<br>
 * Box 0: [rn 1] [cm 2]<br>
 * Box 1: [qp 3]<br>
 * <p><br>
 * After "qp-":<br>
 * Box 0: [rn 1] [cm 2]<br>
 * <p><br>
 * After "pc=4":<br>
 * Box 0: [rn 1] [cm 2]<br>
 * Box 3: [pc 4]<br>
 * <p><br>
 * After "ot=9":<br>
 * Box 0: [rn 1] [cm 2]<br>
 * Box 3: [pc 4] [ot 9]<br>
 * <p><br>
 * After "ab=5":<br>
 * Box 0: [rn 1] [cm 2]<br>
 * Box 3: [pc 4] [ot 9] [ab 5]<br>
 * <p><br>
 * After "pc-":<br>
 * Box 0: [rn 1] [cm 2]<br>
 * Box 3: [ot 9] [ab 5]<br>
 * <p><br>
 * After "pc=6":<br>
 * Box 0: [rn 1] [cm 2]<br>
 * Box 3: [ot 9] [ab 5] [pc 6]<br>
 * <p><br>
 * After "ot=7":<br>
 * Box 0: [rn 1] [cm 2]<br>
 * Box 3: [ot 7] [ab 5] [pc 6]<br>
 * <p><br>
 * All 256 boxes are always present; only the boxes that contain any lenses are shown here.
 * Within each box, lenses are listed from front to back; each lens is shown as its label and
 * focal length in square brackets.
 * <p><br>
 * To confirm that all the lenses are installed correctly, add up the focusing power of all the lenses.
 * The focusing power of a single lens is the result of multiplying together:
 * <ol>
 *     <li>
 *         One plus the box number of the lens in question.
 *     </li>
 *     <li>
 *         The slot number of the lens within the box: 1 for the first lens, 2 for the second lens, and so on.
 *     </li>
 *     <li>
 *         The focal length of the lens.
 *     </li>
 * </ol>
 * At the end of the above example, the focusing power of each lens is as follows:
 * <p><br>
 * <code>
 * rn: 1 (box 0) * 1 (first slot) * 1 (focal length) = 1<br>
 * cm: 1 (box 0) * 2 (second slot) * 2 (focal length) = 4<br>
 * ot: 4 (box 3) * 1 (first slot) * 7 (focal length) = 28<br>
 * ab: 4 (box 3) * 2 (second slot) * 5 (focal length) = 40<br>
 * pc: 4 (box 3) * 3 (third slot) * 6 (focal length) = 72<br>
 * </code>
 * <p><br>
 * So, the above example ends up with a total focusing power of 145.
 * <p><br>
 * With the help of an over-enthusiastic reindeer in a hard hat, follow the initialization sequence.
 * What is the focusing power of the resulting lens configuration?
 * <p><br>
 * Your puzzle answer was 248279.
 */
public class Day15 implements AdventOfCodeSolution {
    private static final Logger LOGGER = Logger.getLogger(Day15.class.getName());

    /**
     * Instantiates this Solution instance.
     */
    public Day15() {

    }

    @Override
    public String getDefaultInputFilename() {
        return "inputs/input_day_15-01.txt";
    }

    @Override
    public void runPart1(List<String> input) {
        var start = Instant.now();
        var answer = sumOfLensHashes(input);
        var end = Instant.now();

        LOGGER.info(String.format("The total sum of the lens hashes is: %d",
                answer));
        logTimings(LOGGER, start, end);
    }

    @Override
    public void runPart2(List<String> input) {
        var start = Instant.now();
        var answer = focusingPowerOfLensConfiguration(input);
        var end = Instant.now();

        LOGGER.info(String.format("The total focusing power of the lens configuration is: %d",
                answer));
        logTimings(LOGGER, start, end);
    }

    /**
     * Calculates and returns the total sum of all the Strings in the initialization sequence.
     *
     * @param input The lens configuration to parse.
     * @return The total sum of all the Strings in the initialization sequence.
     */
    public long sumOfLensHashes(@NotNull List<String> input) {
        var strings = HashString.parse(input);
        return strings.stream().mapToInt(HashString::hashAscii).sum();
    }

    /**
     * Calculates and returns the total focusin power of the lens configuration.
     *
     * @param input The lens configuration to parse.
     * @return The total focusin power of the lens configuration.
     */
    public long focusingPowerOfLensConfiguration(@NotNull List<String> input) {
        var library = LensLibrary.buildLensLibrary(input);
        return library.focusingPower();
    }
}
