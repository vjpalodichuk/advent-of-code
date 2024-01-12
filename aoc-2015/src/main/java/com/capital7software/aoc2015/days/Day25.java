package com.capital7software.aoc2015.days;

import com.capital7software.aoc2015.lib.AdventOfCodeSolution;
import com.capital7software.aoc2015.lib.grid.CodeGenerator;

import java.time.Instant;
import java.util.List;

/**
 * --- Day 25: Let It Snow ---<br>
 * Merry Christmas! Santa is booting up his weather machine; looks like you might get a white Christmas after all.
 * <p><br>
 * The weather machine beeps! On the console of the machine is a copy protection message asking you
 * to enter a code from the instruction manual. Apparently, it refuses to run unless you give it that code.
 * No problem; you'll just look up the code in the--
 * <p><br>
 * "Ho ho ho", Santa ponders aloud. "I can't seem to find the manual."
 * <p><br>
 * You look up the support number for the manufacturer and give them a call. Good thing,
 * too - that 49th star wasn't going to earn itself.
 * <p><br>
 * "Oh, that machine is quite old!", they tell you. "That model went out of support six minutes ago,
 * and we just finished shredding all of the manuals. I bet we can find you the code generation algorithm, though."
 * <p><br>
 * After putting you on hold for twenty minutes (your call is very important to them,
 * it reminded you repeatedly), they finally find an engineer that remembers how the code system works.
 * <p><br>
 * The codes are printed on an infinite sheet of paper, starting in the top-left corner.
 * The codes are filled in by diagonals: starting with the first row with an empty first box,
 * the codes are filled in diagonally up and to the right. This process repeats until
 * the infinite paper is covered. So, the first few codes are filled in in this order:
 * <p><br>
 *    | 1   2   3   4   5   6  <br>
 * ---+---+---+---+---+---+---+
 *  1 |  1   3   6  10  15  21<br>
 *  2 |  2   5   9  14  20<br>
 *  3 |  4   8  13  19<br>
 *  4 |  7  12  18<br>
 *  5 | 11  17<br>
 *  6 | 16<br>
 * For example, the 12th code would be written to row 4, column 2; the 15th code would be written to row 1, column 5.
 * <p><br>
 * The voice on the other end of the phone continues with how the codes are actually generated.
 * The first code is 20151125. After that, each code is generated by taking the previous one,
 * multiplying it by 252533, and then keeping the remainder from dividing that value by 33554393.
 * <p><br>
 * So, to find the second code (which ends up in row 2, column 1), start with the previous value,
 * 20151125. Multiply it by 252533 to get 5088824049625. Then, divide that by 33554393,
 * which leaves a remainder of 31916031. That remainder is the second code.
 * <p><br>
 * "Oh!", says the voice. "It looks like we missed a scrap from one of the manuals.
 * Let me read it to you." You write down his numbers:
 * <p><br>
 *    |    1         2         3         4         5         6<br>
 * ---+---------+---------+---------+---------+---------+---------+<br>
 *  1 | 20151125  18749137  17289845  30943339  10071777  33511524<br>
 *  2 | 31916031  21629792  16929656   7726640  15514188   4041754<br>
 *  3 | 16080970   8057251   1601130   7981243  11661866  16474243<br>
 *  4 | 24592653  32451966  21345942   9380097  10600672  31527494<br>
 *  5 |    77061  17552253  28094349   6899651   9250759  31663883<br>
 *  6 | 33071741   6796745  25397450  24659492   1534922  27995004<br>
 *  <p><br>
 * "Now remember", the voice continues, "that's not even all the first few numbers;
 * for example, you're missing the one at 7,1 that would come before 6,2.
 * But, it should be enough to let your-- oh, it's time for lunch! Bye!" The call disconnects.
 * <p><br>
 * Santa looks nervous. Your puzzle input contains the message on the machine's console.
 * What code do you give the machine?
 * <p>
 * Your puzzle answer was 9132360.
 * <p>
 * --- Part Two ---<br>
 * The machine springs to life, then falls silent again. It beeps. "Insufficient fuel",
 * the console reads. "Fifty stars are required before proceeding. One star is available."
 * <p><br>
 * ..."one star is available"? You check the fuel tank; sure enough, a lone star sits at the bottom,
 * awaiting its friends. Looks like you need to provide 49 yourself.
 * <p><br>
 * You have enough stars to start the weather machine.
 * <p>
 * You fill the weather machine with fifty stars. It comes to life!
 * <p><br>
 * Snow begins to fall.
 * <p><br>
 * Congratulations! You've finished every puzzle in Advent of Code 2015!
 * I hope you had as much fun solving them as I had making them for you.
 * I'd love to hear about your adventure; you can get in touch with me via
 * contact info on my website or through Twitter or Mastodon.
 * <p>
 */
public class Day25 implements AdventOfCodeSolution {
    @Override
    public String getDefaultInputFilename() {
        return "inputs/input_day_25-01.txt";
    }

    @Override
    public void runPart1(List<String> input) {
        var start = Instant.now();
        var generator = CodeGenerator.buildGenerator(input.getFirst());
        var lowest = getNextCode(generator);
        var end = Instant.now();
        System.out.printf("The code found at row %d and column %d is: %d%n",
                generator.targetRow(), generator.targetColumn(), lowest);
        printTiming(start, end);
    }

    @Override
    public void runPart2(List<String> input) {
        var start = Instant.now();
        var end = Instant.now();
        System.out.printf("Happy Advent of Code 2015!!%n");
        printTiming(start, end);
    }

    public long getNextCode(CodeGenerator generator) {
        return generator.calculateCode();
    }
}
