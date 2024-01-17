package com.capital7software.aoc.aoc2023.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.math.BoatRaces;

import java.time.Instant;
import java.util.List;
import java.util.logging.Logger;

/**
 * --- Day 6: Wait For It ---<br>
 * The ferry quickly brings you across Island Island. After asking around, you discover that there is indeed
 * normally a large pile of sand somewhere near here, but you don't see anything besides lots of water and
 * the small island where the ferry has docked.
 * <p><br>
 * As you try to figure out what to do next, you notice a poster on a wall near the ferry dock. "Boat races!
 * Open to the public! Grand prize is an all-expenses-paid trip to Desert Island!" That must be where the
 * sand comes from! Best of all, the boat races are starting in just a few minutes.
 * <p><br>
 * You manage to sign up as a competitor in the boat races just in time. The organizer explains that it's
 * not really a traditional race - instead, you will get a fixed amount of time during which your boat has
 * to travel as far as it can, and you win if your boat goes the farthest.
 * <p><br>
 * As part of signing up, you get a sheet of paper (your puzzle input) that lists the time allowed for each
 * race and also the best distance ever recorded in that race. To guarantee you win the grand prize, you
 * need to make sure you go farther in each race than the current record holder.
 * <p><br>
 * The organizer brings you over to the area where the boat races are held. The boats are much smaller than
 * you expected - they're actually toy boats, each with a big button on top. Holding down the button charges
 * the boat, and releasing the button allows the boat to move. Boats move faster if their button was held
 * longer, but time spent holding the button counts against the total race time. You can only hold the button
 * at the start of the race, and boats don't move until the button is released.
 * <p><br>
 * For example:
 * <p><br>
 * <code>
 * Time:      7  15   30<br>
 * Distance:  9  40  200<br>
 * </code>
 * <p><br>
 * This document describes three races:
 * <p><br>
 * The first race lasts 7 milliseconds. The record distance in this race is 9 millimeters.<br>
 * The second race lasts 15 milliseconds. The record distance in this race is 40 millimeters.<br>
 * The third race lasts 30 milliseconds. The record distance in this race is 200 millimeters.<br>
 * <p><br>
 * Your toy boat has a starting speed of zero millimeters per millisecond. For each whole millisecond you
 * spend at the beginning of the race holding down the button, the boat's speed increases by one
 * millimeter per millisecond.
 * <p><br>
 * So, because the first race lasts 7 milliseconds, you only have a few options:
 * <p><br>
 * <ul>
 *     <li>
 *         Don't hold the button at all (that is, hold it for 0 milliseconds) at the start of the race.
 *         The boat won't move; it will have traveled 0 millimeters by the end of the race.
 *     </li>
 *     <li>
 *         Hold the button for 1 millisecond at the start of the race. Then, the boat will travel at a speed
 *         of 1 millimeter per millisecond for 6 milliseconds, reaching a total distance traveled of 6 millimeters.
 *     </li>
 *     <li>
 *         Hold the button for 2 milliseconds, giving the boat a speed of 2 millimeters per millisecond.
 *         It will then get 5 milliseconds to move, reaching a total distance of 10 millimeters.
 *     </li>
 *     <li>
 *         Hold the button for 3 milliseconds. After its remaining 4 milliseconds of travel time,
 *         the boat will have gone 12 millimeters.
 *     </li>
 *     <li>
 *         Hold the button for 4 milliseconds. After its remaining 3 milliseconds of travel time,
 *         the boat will have gone 12 millimeters.
 *     </li>
 *     <li>
 *         Hold the button for 5 milliseconds, causing the boat to travel a total of 10 millimeters.
 *     </li>
 *     <li>
 *         Hold the button for 6 milliseconds, causing the boat to travel a total of 6 millimeters.
 *     </li>
 *     <li>
 *         Hold the button for 7 milliseconds. That's the entire duration of the race. You never let go of the button.
 *         The boat can't move until you let go of the button. Please make sure you let go of the button
 *         so the boat gets to move. 0 millimeters.
 *     </li>
 * </ul>
 * Since the current record for this race is 9 millimeters, there are actually 4 different ways you could win:
 * you could hold the button for 2, 3, 4, or 5 milliseconds at the start of the race.
 * <p><br>
 * In the second race, you could hold the button for at least 4 milliseconds and at most 11 milliseconds
 * and beat the record, a total of 8 different ways to win.
 * <p><br>
 * In the third race, you could hold the button for at least 11 milliseconds and no more than 19 milliseconds
 * and still beat the record, a total of 9 ways you could win.
 * <p><br>
 * To see how much margin of error you have, determine the number of ways you can beat the record in each race;
 * in this example, if you multiply these values together, you get 288 (4 * 8 * 9).
 * <p><br>
 * Determine the number of ways you could beat the record in each race. What do you get if you multiply
 * these numbers together?
 * <p><br>
 * Your puzzle answer was 160816.
 * <p><br>
 * --- Part Two ---<br>
 * As the race is about to start, you realize the piece of paper with race times and record distances you
 * got earlier actually just has very bad kerning. There's really only one race - ignore the spaces
 * between the numbers on each line.
 * <p><br>
 * So, the example from before:
 * <p><br>
 * Time:      7  15   30
 * Distance:  9  40  200
 * ...now instead means this:
 * <p><br>
 * Time:      71530
 * Distance:  940200
 * Now, you have to figure out how many ways there are to win this single race. In this example, the
 * race lasts for 71530 milliseconds and the record distance you need to beat is 940200 millimeters.
 * You could hold the button anywhere from 14 to 71516 milliseconds and beat the record, a total of 71503 ways!
 * <p><br>
 * How many ways can you beat the record in this one much longer race?
 * <p><br>
 * Your puzzle answer was 46561107.
 */
public class Day06 implements AdventOfCodeSolution {
    private static final Logger LOGGER = Logger.getLogger(Day06.class.getName());

    /**
     * Instantiates the solution instance.
     */
    public Day06() {

    }

    @Override
    public String getDefaultInputFilename() {
        return "inputs/input_day_06-01.txt";
    }

    @Override
    public void runPart1(List<String> input) {
        var start = Instant.now();
        var answer = productOfTheNumberOfWinningHoldTimesPerRace(input);
        var end = Instant.now();

        LOGGER.info(String.format("The product of the number of winning hold times per race is: %d%n", answer));
        logTimings(LOGGER, start, end);
    }

    @Override
    public void runPart2(List<String> input) {
        var start = Instant.now();
        var answer = numberOfWinningHoldTimesIterative(input);
        var end = Instant.now();

        LOGGER.info(String.format("The number of winning hold times using iteration is: %d%n", answer));
        logTimings(LOGGER, start, end);

        start = Instant.now();
        answer = numberOfWinningHoldTimesQuadratic(input);
        end = Instant.now();

        LOGGER.info(String.format("The number of winning hold times using quadratic equation is: %d%n", answer));
        logTimings(LOGGER, start, end);
    }

    /**
     * For each millisecond the button is pressed, the boat will accelerate an additional
     * 1 millimeter per millisecond. Returns the hold times that result in a distance greater
     * than the current record.
     *
     * @param input The race record times and distances.
     * @return The hold times that result in a distance greater than the current record.
     */
    public long productOfTheNumberOfWinningHoldTimesPerRace(List<String> input) {
        var instance = BoatRaces.buildBoatRaces(input);
        return instance.productOfTheNumberOfWinningHoldTimesPerRace();
    }

    /**
     * For every millisecond the boat button is held, it will travel an additional 1 millimeter per millisecond.
     * The distance traveled can be expressed as:<br> distance = holdTime * (raceTime - holdTime)<br>
     * This can be rearranged as:<br> d = rh - h²; 0 = rh -h² - d; 0 = -h² + rh - d<br>
     * Now all we do is change the = to &#62; to switch this to a quadratic inequality:<br> -h² + rh - d &#62; 0<br>
     * And now we can use the quadratic formula to find our min and max times that satisfy the inequality
     * and then the difference between them plus 1 will give us the total number of winning hold times!
     * <p><br>
     * Remember that the quadratic formula is:<br> (-b ± √(b²-4ac))/(2a)<br>
     * In our case, a = -holdTime, b = raceTime, c = -distance<br>
     * <p>
     * So now we can use the quadratic equation to set up the inequality in terms of the holdTime as follows:<br>
     * (-b - √(b²-4c))/(2) &#62; a &#62; (-b + √(b²-4c))/(2)<br>
     * We can multiply everything by -1 and substitute in our variables to get the following:<br>
     * raceTime - √(raceTime² - (4 * distance))/(2) &#62; holdTime &#62; raceTime + √(raceTime² - (4 * distance))/(2)<br>
     * <p>
     * Since we will get Doubles as a result, we take the floor of the upper bound and the ceiling of the lower bound
     * and add one to the difference between the two to get our answer!!
     *
     * @param input The race record times and distances.
     * @return The number of different hold / charge times that result in a
     * distance greater than the specified distance.
     */
    public long numberOfWinningHoldTimesQuadratic(List<String> input) {
        var instance = BoatRaces.buildBoatRaces(input);
        return instance.calculateNumberOfWinningHoldTimesFormulaic();
    }

    /**
     * For every millisecond the boat button is held, it will travel an additional 1 millimeter per millisecond.
     * This is a brute force iterative implementation of the solution.
     *
     * @param input The race record times and distances.
     * @return The number of different hold / charge times that result in a
     * distance greater than the specified distance.
     */
    public long numberOfWinningHoldTimesIterative(List<String> input) {
        var instance = BoatRaces.buildBoatRaces(input);
        return instance.calculateNumberOfWinningHoldTimesIterative();
    }
}
