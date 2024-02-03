package com.capital7software.aoc.aoc2023aoc.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.geometry.HailStorm;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * --- Day 24: Never Tell Me The Odds ---<br><br>
 * It seems like something is going wrong with the snow-making process. Instead of forming snow,
 * the water that's been absorbed into the air seems to be forming hail!
 *
 * <p><br>
 * Maybe there's something you can do to break up the hailstones?
 *
 * <p><br>
 * Due to strong, probably-magical winds, the hailstones are all flying through the air in
 * perfectly linear trajectories. You make a note of each hailstone's position and velocity
 * (your puzzle input). For example:
 *
 * <p><br>
 * <code>
 * 19, 13, 30 @ -2,  1, -2<br>
 * 18, 19, 22 @ -1, -1, -2<br>
 * 20, 25, 34 @ -2, -2, -4<br>
 * 12, 31, 28 @ -1, -2, -1<br>
 * 20, 19, 15 @  1, -5, -3<br>
 * </code>
 *
 * <p><br>
 * Each line of text corresponds to the position and velocity of a single hailstone.
 * The positions indicate where the hailstones are right now (at time 0). The velocities
 * are constant and indicate exactly how far each hailstone will move in one nanosecond.
 *
 * <p><br>
 * Each line of text uses the format px py pz @ vx vy vz. For instance, the hailstone
 * specified by 20, 19, 15 @ 1, -5, -3 has initial X position 20, Y position 19, Z position 15,
 * X velocity 1, Y velocity -5, and Z velocity -3. After one nanosecond, the hailstone
 * would be at 21, 14, 12.
 *
 * <p><br>
 * Perhaps you won't have to do anything. How likely are the hailstones to collide with
 * each other and smash into tiny ice crystals?
 *
 * <p><br>
 * To estimate this, consider only the X and Y axes; ignore the Z axis. Looking forward
 * in time, how many of the hailstones' paths will intersect within a test area? (The
 * hailstones themselves don't have to collide, just test for intersections between the
 * paths they will trace.)
 *
 * <p><br>
 * In this example, look for intersections that happen with an X and Y position each at
 * least 7 and at most 27; in your actual data, you'll need to check a much larger test
 * area. Comparing all pairs of hailstones' future paths produces the following results:
 *
 * <p><br>
 * <code>
 * Hailstone A: 19, 13, 30 @ -2, 1, -2<br>
 * Hailstone B: 18, 19, 22 @ -1, -1, -2<br>
 * Hailstones' paths will cross inside the test area (at x=14.333, y=15.333).<br>
 * </code>
 *
 * <p><br>
 * <code>
 * Hailstone A: 19, 13, 30 @ -2, 1, -2<br>
 * Hailstone B: 20, 25, 34 @ -2, -2, -4<br>
 * Hailstones' paths will cross inside the test area (at x=11.667, y=16.667).<br>
 * </code>
 *
 * <p><br>
 * <code>
 * Hailstone A: 19, 13, 30 @ -2, 1, -2<br>
 * Hailstone B: 12, 31, 28 @ -1, -2, -1<br>
 * Hailstones' paths will cross outside the test area (at x=6.2, y=19.4).<br>
 * </code>
 *
 * <p><br>
 * <code>
 * Hailstone A: 19, 13, 30 @ -2, 1, -2<br>
 * Hailstone B: 20, 19, 15 @ 1, -5, -3<br>
 * Hailstones' paths crossed in the past for hailstone A.<br>
 * </code>
 *
 * <p><br>
 * <code>
 * Hailstone A: 18, 19, 22 @ -1, -1, -2<br>
 * Hailstone B: 20, 25, 34 @ -2, -2, -4<br>
 * Hailstones' paths are parallel; they never intersect.<br>
 * </code>
 *
 * <p><br>
 * <code>
 * Hailstone A: 18, 19, 22 @ -1, -1, -2<br>
 * Hailstone B: 12, 31, 28 @ -1, -2, -1<br>
 * Hailstones' paths will cross outside the test area (at x=-6, y=-5).<br>
 * </code>
 *
 * <p><br>
 * <code>
 * Hailstone A: 18, 19, 22 @ -1, -1, -2<br>
 * Hailstone B: 20, 19, 15 @ 1, -5, -3<br>
 * Hailstones' paths crossed in the past for both hailstones.<br>
 * </code>
 *
 * <p><br>
 * <code>
 * Hailstone A: 20, 25, 34 @ -2, -2, -4<br>
 * Hailstone B: 12, 31, 28 @ -1, -2, -1<br>
 * Hailstones' paths will cross outside the test area (at x=-2, y=3).<br>
 * </code>
 *
 * <p><br>
 * <code>
 * Hailstone A: 20, 25, 34 @ -2, -2, -4<br>
 * Hailstone B: 20, 19, 15 @ 1, -5, -3<br>
 * Hailstones' paths crossed in the past for hailstone B.<br>
 * </code>
 *
 * <p><br>
 * <code>
 * Hailstone A: 12, 31, 28 @ -1, -2, -1<br>
 * Hailstone B: 20, 19, 15 @ 1, -5, -3<br>
 * Hailstones' paths crossed in the past for both hailstones.<br>
 * </code>
 *
 * <p><br>
 * So, in this example, 2 hailstones' future paths cross inside the boundaries of
 * the test area.
 *
 * <p><br>
 * However, you'll need to search a much larger test area if you want to see if any
 * hailstones might collide. Look for intersections that happen with an X and Y position
 * each at least 200000000000000 and at most 400000000000000. Disregard the Z axis entirely.
 *
 * <p><br>
 * Considering only the X and Y axes, check all pairs of hailstones' future paths for
 * intersections. How many of these intersections occur within the test area?
 *
 * <p><br>
 * Your puzzle answer was 29142.
 *
 * <p><br>
 * --- Part Two ---<br><br>
 * Upon further analysis, it doesn't seem like any hailstones will naturally collide.
 * It's up to you to fix that!
 *
 * <p><br>
 * You find a rock on the ground nearby. While it seems extremely unlikely, if you throw
 * it just right, you should be able to hit every hailstone in a single throw!
 *
 * <p><br>
 * You can use the probably-magical winds to reach any integer position you like and to
 * propel the rock at any integer velocity. Now including the Z axis in your calculations,
 * if you throw the rock at time 0, where do you need to be so that the rock perfectly
 * collides with every hailstone? Due to probably-magical inertia, the rock won't slow down
 * or change direction when it collides with a hailstone.
 *
 * <p><br>
 * In the example above, you can achieve this by moving to position 24, 13, 10 and throwing
 * the rock at velocity -3, 1, 2. If you do this, you will hit every hailstone as follows:
 *
 * <p><br>
 * <code>
 * Hailstone: 19, 13, 30 @ -2, 1, -2<br>
 * Collision time: 5<br>
 * Collision position: 9, 18, 20<br>
 * </code>
 *
 * <p><br>
 * <code>
 * Hailstone: 18, 19, 22 @ -1, -1, -2<br>
 * Collision time: 3<br>
 * Collision position: 15, 16, 16<br>
 * </code>
 *
 * <p><br>
 * <code>
 * Hailstone: 20, 25, 34 @ -2, -2, -4<br>
 * Collision time: 4<br>
 * Collision position: 12, 17, 18<br>
 * </code>
 *
 * <p><br>
 * <code>
 * Hailstone: 12, 31, 28 @ -1, -2, -1<br>
 * Collision time: 6<br>
 * Collision position: 6, 19, 22<br>
 * </code>
 *
 * <p><br>
 * <code>
 * Hailstone: 20, 19, 15 @ 1, -5, -3<br>
 * Collision time: 1<br>
 * Collision position: 21, 14, 12<br>
 * </code>
 *
 * <p><br>
 * Above, each hailstone is identified by its initial position and its velocity. Then,
 * the time and position of that hailstone's collision with your rock are given.
 *
 * <p><br>
 * After 1 nanosecond, the rock has exactly the same position as one of the hailstones,
 * obliterating it into ice dust! Another hailstone is smashed to bits two nanoseconds
 * after that. After a total of 6 nanoseconds, all of the hailstones have been destroyed.
 *
 * <p><br>
 * So, at time 0, the rock needs to be at X position 24, Y position 13, and Z position 10.
 * Adding these three coordinates together produces 47. (Don't add any coordinates from
 * the rock's velocity.)
 *
 * <p><br>
 * Determine the exact position and velocity the rock needs to have at time 0 so that it
 * perfectly collides with every hailstone. What do you get if you add up the X, Y, and Z
 * coordinates of that initial position?
 *
 * <p><br>
 * Your puzzle answer was 848947587263033.
 */
public class Day24 implements AdventOfCodeSolution {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day24.class);

  /**
   * Instantiates this Solution instance.
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
    var answer = testHailStones(
        input, 200_000_000_000_000L, 400_000_000_000_000L
    );
    var end = Instant.now();

    LOGGER.info("Total number of hail stones that pass the test area is: {}", answer);
    logTimings(LOGGER, start, end);
  }

  @Override
  public void runPart2(List<String> input) {
    var start = Instant.now();
    var answer = testFindRock(input);
    var end = Instant.now();

    LOGGER.info("Total sum of the rock parts is: {}",
                BigDecimal.valueOf(answer).toPlainString());
    logTimings(LOGGER, start, end);
  }

  /**
   * Returns the count of hail stones that pass through the test area.
   *
   * @param input      The List of Strings to parse into hail stones and their velocities.
   * @param lowerBound The lower bound of the test area.
   * @param upperBound The upper bound of the test area.
   * @return The count of hail stones that pass through the test area.
   */
  public long testHailStones(@NotNull List<String> input, long lowerBound, long upperBound) {
    var storm = HailStorm.buildHailStorm(input);
    return storm.testHailStones(lowerBound, upperBound);
  }

  /**
   * Returns the sum of the parts that make up the rock that will destroy all hail stones.
   *
   * @param input The List of Strings to parse into hail stones and their velocities.
   * @return The sum of the parts that make up the rock that will destroy all hail stones.
   */
  public double testFindRock(@NotNull List<String> input) {
    var storm = HailStorm.buildHailStorm(input);
    return storm.testFindRock();
  }
}
