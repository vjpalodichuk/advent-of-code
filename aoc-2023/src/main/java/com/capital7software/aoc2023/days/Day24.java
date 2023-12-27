package com.capital7software.aoc2023.days;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Day24 {
    public record Plane(Day22.Point3DDouble point, double dotProduct) {
        public double sumAtTime0() {
            return (point.x() + point().y() + point.z()) / dotProduct;
        }
    }

    public record HailStone(Day22.Point3DDouble position, Day22.Point3DDouble velocity, double slope, double intercept) {
        public HailStone(double x, double y, double z, double vx, double vy, double vz) {
            this(new Day22.Point3DDouble(x, y, z), new Day22.Point3DDouble(vx, vy, vz), vy / vx, y - x * (vy / vx));
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

    public record HailStorm(List<HailStone> hailStones) {
        public static HailStorm build(Stream<String> stream) {
            return new HailStorm(stream.map(HailStone::parse).toList());
        }

        public List<HailStoneTestResult> testHailStones(long lowerBound, long upperBound) {
            var results = new ArrayList<HailStoneTestResult>();

            for (int i = 0; i < hailStones().size() - 1; i++) {
                for (int j = i + 1; j < hailStones.size(); j++) {
                    var test = new HailStoneTest(hailStones.get(i), hailStones.get(j), lowerBound, upperBound);
                    results.add(test.runTest());
                }
            }

            return results;
        }

        public Plane testFindRock() {
            // Grab a starting hailstone
            var stone1 = hailStones.get(0);
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
                    if (stone1.velocity().isLinearIndependent(hailStones.get(i).velocity()) &&
                            stone2.velocity().isLinearIndependent(hailStones.get(i).velocity())) {
                        stone3 = hailStones.get(i);
                        break;
                    }
                }
            }

            return findRock(stone1, stone2, stone3);
        }

        public Plane findRock(HailStone first, HailStone second, HailStone third) {
            var plane1 = first.calculatePlane(second);
            var plane2 = first.calculatePlane(third);
            var plane3 = second.calculatePlane(third);
            var a1 = plane2.point().cross(plane3.point());
            var b1 = plane3.point().cross(plane1.point());
            var c1 = plane1.point().cross(plane2.point());

            var w = Day22.Point3DDouble.linearize(
                    plane1.dotProduct(),
                    a1,
                    plane2.dotProduct(),
                    b1,
                    plane3.dotProduct(),
                    c1
            );

            var t = plane1.point().dot(plane2.point().cross(plane3.point()));

            w = new Day22.Point3DDouble(
                    Math.round(w.x() / t),
                    Math.round(w.y() / t),
                    Math.round(w.z() / t)
            );

            var w1 = first.velocity.subtract(w);
            var w2 = second.velocity.subtract(w);
            var ww = w1.cross(w2);

            var e = ww.dot(second.position.cross(w2));
            var f = ww.dot(first.position.cross(w1));
            var g = first.position.dot(ww);
            var s = ww.dot(ww);

            var rock = Day22.Point3DDouble.linearize(e, w1, -f, w2, g, ww);

            return new Plane(rock, s);
        }
    }

    public enum HailStoneTestResultType {
        WILL_CROSS_INSIDE_TEST_AREA,
        WILL_CROSS_OUTSIDE_TEST_AREA,
        CROSSED_IN_THE_PAST_A,
        CROSSED_IN_THE_PAST_B,
        CROSSED_IN_THE_PAST_BOTH,
        NEVER_INTERSECT,

    }

    public record HailStoneTestResult(
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

    public record HailStoneTest(
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
                } else if (timeA >=0 && timeB < 0) {
                    resultType = HailStoneTestResultType.CROSSED_IN_THE_PAST_B;
                } else if (timeA >= 0 && timeB >= 0) {
                    if (lowerBound <= newX && newX <= upperBound && lowerBound <= newY && newY <= upperBound) {
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

    private static final String inputFilename = "inputs/input_day_24-01.txt";

    public static void main(String[] args) throws URISyntaxException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        var url = classloader.getResource(inputFilename);
        assert url != null;
        var path = Paths.get(url.toURI());

        part1(path);
        part2(path);
    }

    private static void part1(Path path) {
        try (var stream = Files.lines(path)) {
            // Part 1
            System.out.println("Part 1: Start!");
            var start = Instant.now();
            var hailStorm = HailStorm.build(stream);
            var count = hailStorm.testHailStones(200_000_000_000_000L, 400_000_000_000_000L)
                    .stream()
                    .filter(HailStoneTestResult::passes)
                    .count();
            var end = Instant.now();
            System.out.println("There are : " + count + " hailstones in this storm that pass the test in " +
                    Duration.between(start, end).toNanos() + " ns");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void part2(Path path) {
        try (var stream = Files.lines(path)) {
            // Part 2
            System.out.println("Part 2: Start!");
            var start = Instant.now();
            var hailStorm = HailStorm.build(stream);
            var rock = hailStorm.testFindRock();
            var end = Instant.now();
            System.out.println("The Rock: " + rock );
            System.out.println(" was found and the sum of its parts is: " +
                    BigDecimal.valueOf(rock.sumAtTime0()).toPlainString() + " in "
                    + Duration.between(start, end).toNanos() + " ns");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
