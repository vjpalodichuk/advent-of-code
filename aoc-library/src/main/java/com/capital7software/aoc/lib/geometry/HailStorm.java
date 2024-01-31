package com.capital7software.aoc.lib.geometry;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
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
 */
public class HailStorm {

  private record Plane(Point3D<Double> point, double dotProduct) {
    public double sumAtTime0() {
      return (point.x() + point().y() + point.z()) / dotProduct;
    }
  }

  private record HailStone(Point3D<Double> position, Point3D<Double> velocity, double slope,
                           double intercept) {
    public HailStone(double x, double y, double z, double vx, double vy, double vz) {
      this(new Point3D<>(x, y, z), new Point3D<>(vx, vy, vz), vy / vx, y - x * (vy / vx));
    }

    public static HailStone parse(String line) {
      if (line == null || line.isBlank()) {
        return null;
      }

      var split = line.split(" @ ");
      var positions = split[0].trim().split(",");
      var velocities = split[1].trim().split(",");
      var x = Long.parseLong(positions[0].trim());
      var y = Long.parseLong(positions[1].trim());
      var z = Long.parseLong(positions[2].trim());
      var vx = Long.parseLong(velocities[0].trim());
      var vy = Long.parseLong(velocities[1].trim());
      var vz = Long.parseLong(velocities[2].trim());

      return new HailStone(x, y, z, vx, vy, vz);
    }

    public Plane calculatePlane(HailStone other) {
      var pp = position.subtract(other.position);
      var pv = velocity.subtract(other.velocity);
      var velocityVector = velocity.cross(other.velocity);
      return new Plane(pp.cross(pv), pp.dot(velocityVector));
    }
  }

  private enum HailStoneTestResultType {
    WILL_CROSS_INSIDE_TEST_AREA,
    WILL_CROSS_OUTSIDE_TEST_AREA,
    CROSSED_IN_THE_PAST_A,
    CROSSED_IN_THE_PAST_B,
    CROSSED_IN_THE_PAST_BOTH,
    NEVER_INTERSECT,

  }

  private record HailStoneTestResult(
      boolean passes,
      HailStoneTestResultType type,
      double intersectX,
      double intersectY,
      long lowerBound,
      long upperBound,
      HailStone stoneA,
      HailStone stoneB
  ) {

  }

  private record HailStoneTest(
      HailStone stoneA, // The first stone to test
      HailStone stoneB, // The second stone to test
      long lowerBound, // The minimum number of nanos in the test window
      long upperBound // The maximum number of nanos in the test window
  ) {
    public HailStoneTestResult runTest() {
      HailStoneTestResultType resultType = HailStoneTestResultType.NEVER_INTERSECT;
      HailStoneTestResult result;
      boolean passes = false;

      if (stoneA.slope() == stoneB.slope()) {
        // They will never intersect as they are parallel!
        result = new HailStoneTestResult(
            false,
            resultType,
            Double.NEGATIVE_INFINITY,
            Double.NEGATIVE_INFINITY,
            lowerBound,
            upperBound,
            stoneA,
            stoneB
        );
      } else {
        var newX = (stoneB.intercept() - stoneA.intercept()) / (stoneA.slope() - stoneB.slope());
        var newY = stoneA.slope() * newX + stoneA.intercept();
        var timeA = (newX - stoneA.position().x()) / stoneA.velocity.x();
        var timeB = (newX - stoneB.position().x()) / stoneB.velocity.x();

        if (timeA < 0 && timeB < 0) {
          resultType = HailStoneTestResultType.CROSSED_IN_THE_PAST_BOTH;
        } else if (timeA < 0 && timeB >= 0) {
          resultType = HailStoneTestResultType.CROSSED_IN_THE_PAST_A;
        } else if (timeA >= 0 && timeB < 0) {
          resultType = HailStoneTestResultType.CROSSED_IN_THE_PAST_B;
        } else if (timeA >= 0 && timeB >= 0) {
          if (lowerBound <= newX
              && newX <= upperBound
              && lowerBound <= newY
              && newY <= upperBound
          ) {
            resultType = HailStoneTestResultType.WILL_CROSS_INSIDE_TEST_AREA;
            passes = true;
          } else {
            resultType = HailStoneTestResultType.WILL_CROSS_OUTSIDE_TEST_AREA;
          }
        }
        result = new HailStoneTestResult(
            passes,
            resultType,
            newX,
            newY,
            lowerBound,
            upperBound,
            stoneA,
            stoneB
        );
      }
      return result;
    }
  }

  private final List<HailStone> hailStones;

  private HailStorm(@NotNull List<HailStone> hailStones) {
    this.hailStones = hailStones;
  }

  /**
   * Builds and returns a new HailStorm instance that is populated with the specified
   * hail stone positions and velocities.
   *
   * @param input The List of Strings to parse into hail stones and their velocities.
   * @return A new HailStorm instance that is populated with the specified
   *     hail stone positions and velocities.
   */
  public static @NotNull HailStorm buildHailStorm(@NotNull List<String> input) {
    return new HailStorm(input.stream().map(HailStone::parse).toList());
  }

  /**
   * Returns the total number of intersections that occur within the specified test area.
   *
   * @param lowerBound The lower bound of the test area.
   * @param upperBound The upper bound of the test area.
   * @return The total number of intersections that occur within the specified test area.
   */
  public long testHailStones(long lowerBound, long upperBound) {
    var results = new ArrayList<HailStoneTestResult>();

    for (int i = 0; i < hailStones.size() - 1; i++) {
      for (int j = i + 1; j < hailStones.size(); j++) {
        var test = new HailStoneTest(hailStones.get(i), hailStones.get(j), lowerBound, upperBound);
        results.add(test.runTest());
      }
    }

    return results
        .stream()
        .filter(HailStoneTestResult::passes)
        .count();
  }

  /**
   * Returns the sum of the coordinates of the rock at its initial position that results
   * in the rock destroying every single hail stone.
   *
   * @return The sum of the coordinates of the rock at its initial position.
   */
  public double testFindRock() {
    // Grab a starting hailstone
    var stone1 = hailStones.getFirst();
    HailStone stone2 = null;
    HailStone stone3 = null;
    int i = 0;

    while (i < hailStones.size()) {
      i++;
      if (stone2 == null) {
        if (stone1.velocity().isLinearIndependent(hailStones.get(i).velocity())) {
          stone2 = hailStones.get(i);
        }
      } else {
        if (stone1.velocity().isLinearIndependent(hailStones.get(i).velocity())
            && stone2.velocity().isLinearIndependent(hailStones.get(i).velocity())) {
          stone3 = hailStones.get(i);
          break;
        }
      }
    }

    if (stone1 == null || stone2 == null || stone3 == null) {
      return -1;
    }

    var rock = findRock(stone1, stone2, stone3);
    return rock.sumAtTime0();
  }

  private Plane findRock(
      @NotNull HailStone first,
      @NotNull HailStone second,
      @NotNull HailStone third
  ) {
    var plane1 = first.calculatePlane(second);
    var plane2 = first.calculatePlane(third);
    var plane3 = second.calculatePlane(third);
    var a1 = plane2.point().cross(plane3.point());
    var b1 = plane3.point().cross(plane1.point());
    var c1 = plane1.point().cross(plane2.point());

    var w = Point3D.linearize(
        plane1.dotProduct(),
        a1,
        plane2.dotProduct(),
        b1,
        plane3.dotProduct(),
        c1
    );

    var t = plane1.point().dot(plane2.point().cross(plane3.point()));

    w = new Point3D<>(
        (double) Math.round(w.x() / t),
        (double) Math.round(w.y() / t),
        (double) Math.round(w.z() / t)
    );

    var w1 = first.velocity.subtract(w);
    var w2 = second.velocity.subtract(w);
    var ww = w1.cross(w2);

    var e = ww.dot(second.position.cross(w2));
    var f = ww.dot(first.position.cross(w1));
    var g = first.position.dot(ww);
    var s = ww.dot(ww);

    var rock = Point3D.linearize(e, w1, -f, w2, g, ww);

    return new Plane(rock, s);
  }
}
