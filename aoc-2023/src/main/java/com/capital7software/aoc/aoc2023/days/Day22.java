package com.capital7software.aoc.aoc2023.days;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day22 {
    public record Point3DDouble(double x, double y, double z) implements Day21.Point {
        public Point3DDouble minus(Point3DDouble other) {
            double newX = Math.abs(x - other.x);
            double newY = Math.abs(y - other.y);
            double newZ = Math.abs(z - other.z);

            return new Point3DDouble(newX, newY, newZ);
        }

        public Point3DDouble plus(Point3DDouble other) {
            return new Point3DDouble(x + other.x, y + other.y, z + other.z);
        }

        /**
         * Subtracts the other point from this point and returns the result as a new 3D point.
         * THis method differs from minus as minus uses the absolute value of the difference
         * while this method does not.
         *
         * @param other The point to subtract from this point.
         * @return A new point
         */
        public Point3DDouble subtract(Point3DDouble other) {
            return new Point3DDouble(x - other.x, y - other.y, z - other.z);
        }

        /**
         * The cross-product between this point and the other point.
         *
         * @param other THe point to calculate the cross-product with.
         * @return A new point that is the result of the cross-product.
         */
        public Point3DDouble cross(Point3DDouble other) {
            return new Point3DDouble(y * other.z - z * other.y, z * other.x - x * other.z, x * other.y - y * other.x);
        }

        /**
         * Returns the dot-product between this point and the other point.
         * A long is returned to avoid any overflow.
         * @param other The point to calculate the dot-product with.
         * @return A long that represents the dot-product.
         */
        public double dot(Point3DDouble other) {
            return x * other.x + y * other.y + z * other.z;
        }

        public boolean isLinearIndependent(Point3DDouble other) {
            var point = cross(other);

            return point.x != 0 || point.y != 0 || point.z != 0;
        }

        public static Point3DDouble linearize(double va, Point3DDouble pa, double vb, Point3DDouble pb, double vc, Point3DDouble pc) {
            var x = va * pa.x + vb * pb.x + vc * pc.x;
            var y = va * pa.y + vb * pb.y + vc * pc.y;
            var z = va * pa.z + vb * pb.z + vc * pc.z;
            return new Point3DDouble(x, y, z);
        }
    }

    public enum Orientation3D {
        HORIZONTAL_X,
        HORIZONTAL_Y,
        VERTICAL_Z, // Used as the Height
        UNKNOWN // A single cube
    }

    public record LineSegment3D(Point3DDouble start, Point3DDouble end) {
        public double length() {
            var newPoint = start.minus(end);

            return newPoint.x() + newPoint.y() + newPoint.z();
        }

        public Orientation3D orientation() {
            var newPoint = start.minus(end);

            if (newPoint.x() != 0) {
                return Orientation3D.HORIZONTAL_X;
            } else if (newPoint.y() != 0) {
                return Orientation3D.HORIZONTAL_Y;
            } else if (newPoint.z() != 0) {
                return Orientation3D.VERTICAL_Z;
            } else {
                return Orientation3D.UNKNOWN;
            }
        }

        /**
         * Determines if this LineSegment is below the other LineSegment. This method does not consider the Z-axis when
         * making the determination. This is due to the fact that we are only concerned for collisions along the X-axis
         * and Y-axis.
         *
         * @param other The LineSegment to compare this segment with
         * @return If this LineSegment is below the other LineSegment, along the X-axis and Y-axis only, true is
         * returned; otherwise false is returned.
         */
        public boolean isBelow(LineSegment3D other) {
            var otherX = new Day10.Range((long) other.start.x(), (long)other.end.x() + 1);
            var otherY = new Day10.Range((long)other.start.y(), (long)other.end.y() + 1);
            var xRange = new Day10.Range((long)start.x(), (long)end.x() + 1);
            var yRange = new Day10.Range((long)start.y(), (long)end.y() + 1);
            var xInOther = otherX.contains((long)start.x()) || otherX.contains((long)end.x());
            var yInOther = otherY.contains((long)start.y()) || otherY.contains((long)end.y());
            var otherInX = xRange.contains((long)other.start.x()) || xRange.contains((long)other.end.x());
            var otherInY = yRange.contains((long)other.start.y()) || yRange.contains((long)other.end.y());

            return (xInOther || otherInX) && (yInOther || otherInY);
        }
    }

    private static class Brick implements Comparable<Brick> {
        private final int id;
        private LineSegment3D lineSegment;

        public Brick(int id, LineSegment3D lineSegment) {
            this.id = id;
            this.lineSegment = lineSegment;
        }

        long cubeCount() {
            return (long)lineSegment.length() + 1;
        }

        Orientation3D orientation() {
            return lineSegment.orientation();
        }

        Point3DDouble start() {
            return lineSegment.start();
        }

        Point3DDouble end() {
            return lineSegment.end();
        }

        @Override
        public int compareTo(Brick o) {
            return Long.compare(
                    Math.min((long)lineSegment.start().z(), (long)lineSegment.end().z()),
                    Math.min((long)o.lineSegment.start().z(), (long)o.lineSegment.end().z()));
        }

        public int getId() {
            return id;
        }

        /**
         * Move this brick down the Z-Axis the specified amount.
         *
         * @param amount THe amount to move down the Z-Axis by.
         */
        public void moveDown(long amount) {
            var newStart = new Point3DDouble(
                    lineSegment.start().x(),
                    lineSegment.start().y(),
                    lineSegment.start().z() - amount
            );
            var newEnd = new Point3DDouble(
                    lineSegment.end().x(),
                    lineSegment.end().y(),
                    lineSegment.end().z() - amount
            );
            lineSegment = new LineSegment3D(newStart, newEnd);
        }

        public boolean isBelow(Brick other) {
            return lineSegment.isBelow(other.lineSegment);
        }

        @Override
        public String toString() {
            return "Brick{" +
                    "id=" + id +
                    ", lineSegment=" + lineSegment +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Brick brick)) return false;
            return getId() == brick.getId();
        }

        @Override
        public int hashCode() {
            return Objects.hash(getId());
        }
    }

    private record BrickBoard(
            List<Brick> bricks,
            HashMap<Long, Set<Brick>> zBricks,
            HashMap<Brick, Set<Brick>> supports,
            HashMap<Brick, Set<Brick>> supportedBy
    ) {

        public static BrickBoard load(Stream<String> stream) {
            var count = new AtomicInteger(0);

            var bricks = stream.map(line -> parseBrick(line, count.getAndIncrement())).sorted().toList();

            return new BrickBoard(bricks, new LinkedHashMap<>(), new LinkedHashMap<>(), new LinkedHashMap<>());
        }

        private static Brick parseBrick(String line, int count) {
            var split = line.split("~");

            var start = split[0].trim().split(",");
            var end = split[1].trim().split(",");

            var startPoint = new Point3DDouble(
                    Integer.parseInt(start[0]),
                    Integer.parseInt(start[1]),
                    Integer.parseInt(start[2])
            );
            var endPoint = new Point3DDouble(
                    Integer.parseInt(end[0]),
                    Integer.parseInt(end[1]),
                    Integer.parseInt(end[2])
            );
            var segment = new LineSegment3D(startPoint, endPoint);

            return new Brick(count, segment);
        }

        @Override
        public String toString() {
            var builder = new StringBuilder();
            var longestX = new AtomicLong(0);
            var longestY = new AtomicLong(0);
            var longestZ = new AtomicLong(0);
            var longestXId = new AtomicLong(-1);
            var longestYId = new AtomicLong(-1);
            var longestZId = new AtomicLong(-1);
            IntStream.range(0, bricks.size())
                    .forEach(id -> {
                        var brick = bricks.get(id);
                        var cubeCount = brick.cubeCount();
                        var orientation = brick.orientation();

                        switch (orientation) {
                            case HORIZONTAL_X -> {
                                if (cubeCount > longestX.get()) {
                                    longestX.set(cubeCount);
                                    longestXId.set(brick.getId());
                                }

                            }
                            case HORIZONTAL_Y -> {
                                if (cubeCount > longestY.get()) {
                                    longestY.set(cubeCount);
                                    longestYId.set(brick.getId());
                                }

                            }
                            case VERTICAL_Z -> {
                                if (cubeCount > longestZ.get()) {
                                    longestZ.set(cubeCount);
                                    longestZId.set(brick.getId());
                                }

                            }
                        }

                        builder.append("\n");
                        builder.append("Brick ID: ");
                        builder.append(brick.getId());
                        builder.append(", Cubes: ");
                        builder.append(cubeCount);
                        builder.append(", Orientation: ");
                        builder.append(orientation);
                        builder.append(", Points: ");
                        builder.append(brick.start());
                        builder.append((" - "));
                        builder.append(brick.end());
                    });
            builder.append("\n");
            builder.append("Total number of Bricks: ");
            builder.append(bricks.size());
            builder.append(", Longest X ID: ");
            builder.append(longestXId.get());
            builder.append(" - Length: ");
            builder.append(longestX.get());
            builder.append(", Longest Y ID: ");
            builder.append(longestYId.get());
            builder.append(" - Length: ");
            builder.append(longestY.get());
            builder.append(", Longest Z ID: ");
            builder.append(longestZId.get());
            builder.append(" - Length: ");
            builder.append(longestZ.get());

            return builder.toString();
        }

        /**
         * Causes the bricks to fall to their lowest possible point. This movement is along the Z-Axis.
         * No two cubes of a brick can occupy the same space. Meaning that if a brick would intersect a cube of
         * another brick it will not be moved.
         * <p>
         * The ground is at z = 0 so no brick will be allowed to have a z less than 1 as that is considered to be
         * the lowest possible point.
         */
        public void fallDownward() {
            var sorted = bricks.stream().sorted().toList(); // Sorted by start Z ascending.

            for (var brick : sorted) {
                // Check if we are already at ground level!
                if (brick.lineSegment.start().z() == 1) {
                    zBricks.computeIfAbsent((long)brick.lineSegment.end().z(), it -> new HashSet<>()).add(brick);
                    continue;
                }
                // Okay, we are above ground level, so we have to check and see if we can drop this
                // brick and if we can, by how much?
                var dependents = updateDependentsAndMaps(brick);
                var distanceToMove = calculateDropDistance(brick, dependents);
                if (distanceToMove > 0) {
                    brick.moveDown(distanceToMove);
                }

                // Now add this to the Z-Brick list!
                zBricks.computeIfAbsent((long)brick.lineSegment.end().z(), it -> new HashSet<>()).add(brick);
            }
        }

        private long calculateDropDistance(Brick brick, Set<Brick> dependents) {
            var distanceToMove = (long)brick.lineSegment.start().z() - 1;

            if (!dependents.isEmpty()) {
                distanceToMove -= dependents
                        .stream()
                        .max(Comparator.comparingLong(a -> (long)a.lineSegment.end().z()))
                        .get()
                        .lineSegment
                        .end()
                        .z();
            }

            return distanceToMove;
        }

        /**
         * We do this by checking if we would intersect any bricks directly below this brick one level
         * at a time. If we find no intersections, then we can simply move all the way to the ground.
         * Otherwise, we can move down until we are on top of the intersecting brick(s).
         * We also add any intersecting bricks to our supportedBy map so that we can determine which
         * bricks can be disintegrated!
         *
         * @param brick The brick we are trying to drop
         * @return A set of bricks that are directly below this brick.
         */
        private Set<Brick> updateDependentsAndMaps(Brick brick) {
            var dependents = new HashSet<Brick>();
            Long maxKey = zBricks.keySet()
                    .stream()
                    .max(Comparator.comparing(Function.identity()))
                    .orElse(1L);
            for (long i = maxKey; i > 0; i--) {
                var bricksBelow = zBricks.computeIfAbsent(i, it -> new HashSet<>());

                for (var below : bricksBelow) {
                    if (below.isBelow(brick)) {
                        dependents.add(below);
                        supports.computeIfAbsent(below, it -> new HashSet<>()).add(brick);
                        supportedBy.computeIfAbsent(brick, it -> new HashSet<>()).add(below);
                    }
                }
                if (!dependents.isEmpty()) {
                    break; // If we found bricks below us then stop!
                }
            }
            return dependents;
        }

        public Set<Brick> safeToDisintegrate() {
            // Any brick that doesn't support any other brick is safe to delete
            var safeToDelete = new HashSet<>(bricks);
            safeToDelete.removeAll(supports.keySet());

            // Now add back any bricks that support other bricks but that are safe to delete!
            supports.forEach((brick, supportSet) -> {
                if (safeToDisintegrate(supportSet)) {
                    safeToDelete.add(brick);
                }
            });
            return safeToDelete;
        }

        private boolean safeToDisintegrate(Collection<Brick> supportedBricks) {
            var goodSupport = new HashSet<Brick>();

            for (var supportedBrick : supportedBricks) {
                if (supportedBy.get(supportedBrick).size() > 1) {
                    goodSupport.add(supportedBrick);
                }
            }

            return goodSupport.size() == supportedBricks.size();
        }

        /**
         * Simulates a chain reaction and determines exactly which blocks will fall due to a supporting brick
         * being disintegrated. The block that is disintegrated is the map key and not included in the set for that
         * key. Performs a BFS search to calculate the results.
         *
         * @return A map of sets of falling bricks where the key is the disintegrated brick and the set contains
         * the bricks that will fall due to that brick being destroyed.
         */
        public Map<Brick, Set<Brick>> chainReaction() {
            var chainReactionMap = new HashMap<Brick, Set<Brick>>(); // Unique bricks that will fall
            for (var destroyed : bricks) {
                var fallingBricks = new HashSet<Brick>();
                chainReactionMap.put(destroyed, fallingBricks);
                var potentiallyFalling = supports.get(destroyed);
                // This brick may not support any other bricks!!!
                if (potentiallyFalling == null || potentiallyFalling.isEmpty()) {
                    continue;
                }
                // queue up all the bricks that the destroyed brick supports
                var queue = new LinkedList<>(potentiallyFalling);
                // Keep track of the bricks that are falling due to this brick being destroyed
                fallingBricks.add(destroyed);
                while (queue.peek() != null) {
                    var brick = queue.poll();

                    // We select the bricks that are supported by the falling bricks
                    if (fallingBricks.containsAll(supportedBy.get(brick))) {
                        fallingBricks.add(brick);
                        potentiallyFalling = supports.get(brick);
                        // This brick may not support any other bricks!!!
                        if (potentiallyFalling != null && !potentiallyFalling.isEmpty()) {
                            queue.addAll(potentiallyFalling);
                        }
                    }
                }
                fallingBricks.remove(destroyed);
            }
            return chainReactionMap;
        }
    }

    private static final String inputFilename = "inputs/input_day_22-01.txt";

    public static void main(String[] args) throws URISyntaxException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        var url = classloader.getResource(inputFilename);
        assert url != null;
        var path = Paths.get(url.toURI());

        part1(path);
        part2(path);
        part3(path);
    }

    private static void part1(Path path) {
        try (var stream = Files.lines(path)) {
            // Part 1
            LOGGER.info(String.format("Part 1: Start!");
            var start = Instant.now();
            var board = BrickBoard.load(stream);
            board.fallDownward();
            var disintegratedCount = board.safeToDisintegrate().size();
            var end = Instant.now();
            LOGGER.info(String.format("Total number of bricks that can be safely disintegrated: "
                    + disintegratedCount + " in " + Duration.between(start, end).toNanos() + " ns");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void part2(Path path) {
        try (var stream = Files.lines(path)) {
            // Part 2
            LOGGER.info(String.format("Part 2: Start!");
            var start = Instant.now();
            var board = BrickBoard.load(stream);
            board.fallDownward();
            var chainReactionCount = board.chainReaction().values().stream().mapToLong(Set::size).sum();
            var end = Instant.now();
            LOGGER.info(String.format("Total number of bricks that will fall in a chain reaction: "
                    + chainReactionCount + " in " + Duration.between(start, end).toNanos() + " ns");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void part3(Path path) {
        try (var stream = Files.lines(path)) {
            // Part 2
            LOGGER.info(String.format("Part 3 (1 & 2): Start!");
            var start = Instant.now();
            var board = BrickBoard.load(stream);
            board.fallDownward();
            var disintegratedCount = board.safeToDisintegrate().size();
            var chain = board.chainReaction();
            var end = Instant.now();
            var idealBrickEntry = chain.entrySet()
                    .stream()
                    .max(Comparator.comparing(it -> it.getValue().size()))
                    .orElse(null);
            var idealBrick = idealBrickEntry != null ? idealBrickEntry.getKey() : null;
            var chainCount = idealBrickEntry != null ? idealBrickEntry.getValue().size() : 0;
            var chainReactionCount = chain.values().stream().mapToLong(Set::size).sum();
            LOGGER.info(String.format("Total number of bricks that can be safely disintegrated: " + disintegratedCount);
            LOGGER.info(String.format("Best brick to disintegrate to create a chain of " + chainCount + " falling bricks is: ");
            LOGGER.info(String.format(idealBrick);
            LOGGER.info(String.format("And total number of bricks that will fall in a chain reaction is: "
                    + chainReactionCount + " in " + Duration.between(start, end).toNanos() + " ns");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
