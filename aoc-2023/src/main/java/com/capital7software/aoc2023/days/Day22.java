package com.capital7software.aoc2023.days;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day22 {
    public record Point3DInt(int x, int y, int z) implements Day21.Point {
        public Point3DInt minus(Point3DInt other) {
            int newX = Math.abs(x - other.x);
            int newY = Math.abs(y - other.y);
            int newZ = Math.abs(z - other.z);

            return new Point3DInt(newX, newY, newZ);
        }

        public Point3DInt plus(Point3DInt other) {
            return new Point3DInt(x + other.x, y + other.y, z + other.z);
        }
    }

    public enum Orientation3D {
        HORIZONTAL_X,
        HORIZONTAL_Y,
        VERTICAL_Z,
        UNKNOWN
    }

    public record LineSegment3D(Point3DInt start, Point3DInt end) {
        public int length() {
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

        public boolean isBelow(LineSegment3D other) {
            var otherX = new Day10.Range(other.start.x(), other.end.x() + 1);
            var otherY = new Day10.Range(other.start.y(), other.end.y() + 1);
            var xRange = new Day10.Range(start.x(), end.x() + 1);
            var yRange = new Day10.Range(start.y(), end.y() + 1);
            var xInOther = otherX.contains(start.x()) || otherX.contains(end.x());
            var yInOther = otherY.contains(start.y()) || otherY.contains(end.y());
            var otherInX = xRange.contains(other.start.x()) || xRange.contains(other.end.x());
            var otherInY = yRange.contains(other.start.y()) || yRange.contains(other.end.y());

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

        int cubeCount() {
            return lineSegment.length() + 1;
        }

        Orientation3D orientation() {
            return lineSegment.orientation();
        }

        Point3DInt start() {
            return lineSegment.start();
        }

        Point3DInt end() {
            return lineSegment.end();
        }

        @Override
        public int compareTo(Brick o) {
            return Integer.compare(
                    Math.min(lineSegment.start().z(), lineSegment.end().z()),
                    Math.min(o.lineSegment.start().z(), o.lineSegment.end().z()));
        }

        public int getId() {
            return id;
        }

        /**
         * Move this brick down the Z-Axis the specified amount.
         *
         * @param amount THe amount to move down the Z-Axis by.
         */
        public void moveDown(int amount) {
            var newStart = new Point3DInt(
                    lineSegment.start().x(),
                    lineSegment.start().y(),
                    lineSegment.start().z() - amount
            );
            var newEnd = new Point3DInt(
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

    private static class BrickBoard {
        private final List<Brick> bricks;
        private final HashMap<Integer, Set<Brick>> zBricks;
        private final HashMap<Brick, Set<Brick>> supports;
        private final HashMap<Brick, Set<Brick>> supportedBy;

        public BrickBoard(List<Brick> bricks, HashMap<Integer, Set<Brick>> zBricks, HashMap<Brick, Set<Brick>> supports, HashMap<Brick, Set<Brick>> supportedBy) {
            this.bricks = bricks;
            this.zBricks = zBricks;
            this.supports = supports;
            this.supportedBy = supportedBy;
        }

        public static BrickBoard load(Stream<String> stream) {
            var count = new AtomicInteger(0);

            var bricks = stream.map(line -> parseBrick(line, count.getAndIncrement())).toList();

            return new BrickBoard(bricks, new LinkedHashMap<>(), new LinkedHashMap<>(), new LinkedHashMap<>());
        }

        private static Brick parseBrick(String line, int count) {
            var split = line.split("~");

            var start = split[0].trim().split(",");
            var end = split[1].trim().split(",");

            var startPoint = new Point3DInt(
                    Integer.parseInt(start[0]),
                    Integer.parseInt(start[1]),
                    Integer.parseInt(start[2])
            );
            var endPoint = new Point3DInt(
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
            var longestX = new AtomicInteger(0);
            var longestY = new AtomicInteger(0);
            var longestZ = new AtomicInteger(0);
            var longestXId = new AtomicInteger(-1);
            var longestYId = new AtomicInteger(-1);
            var longestZId = new AtomicInteger(-1);
            var sorted = bricks.stream().sorted().toList();
            IntStream.range(0, bricks.size())
                    .forEach(id -> {
                        var brick = sorted.get(id);
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
                    zBricks.computeIfAbsent(brick.lineSegment.end().z(), it -> new HashSet<>()).add(brick);
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
                zBricks.computeIfAbsent(brick.lineSegment.end().z(), it -> new HashSet<>()).add(brick);
            }
        }

        private int calculateDropDistance(Brick brick, Set<Brick> dependents) {
            var distanceToMove = brick.lineSegment.start().z() - 1;

            if (!dependents.isEmpty()) {
                distanceToMove -= dependents
                        .stream()
                        .max(Comparator.comparingInt(a -> a.lineSegment.end().z()))
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
         * @param brick The brick we are trying to drop
         * @return A set of bricks that are directly below this brick.
         */
        private Set<Brick> updateDependentsAndMaps(Brick brick) {
            var dependents = new HashSet<Brick>();
            Integer maxKey = zBricks.keySet()
                    .stream()
                    .max(Comparator.comparing(Function.identity()))
                    .orElse(1);
            for (int i = maxKey; i > 0; i--) {
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
            supports.forEach((brick, supportSet) -> supportSet.forEach(it -> {
                if (safeToDisintegrate(supportSet)) {
                    safeToDelete.add(brick);
                }
            }));
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
         * being disintegrated. The block that is disintegrated is not included.
         * Performs a BFS search to calcuate the results
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
    }

    private static void part1(Path path) {
        try (var stream = Files.lines(path)) {
            // Part 1
            System.out.println("Part 1: Start!");
            var start = Instant.now();
            var board = BrickBoard.load(stream);
            board.fallDownward();
            var disintegratedCount = board.safeToDisintegrate().size();
            var end = Instant.now();
            System.out.println("Total number of bricks that can be safely disintegrated: "
                    + disintegratedCount + " in " + Duration.between(start, end).toNanos() + " ns");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void part2(Path path) {
        try (var stream = Files.lines(path)) {
            // Part 2
            System.out.println("Part 2: Start!");
            var start = Instant.now();
            var board = BrickBoard.load(stream);
            board.fallDownward();
            var chainReactionCount = board.chainReaction().values().stream().mapToLong(Set::size).sum();
            var end = Instant.now();
            System.out.println("Total number of bricks that will fall in a chain reaction: "
                    + chainReactionCount + " in " + Duration.between(start, end).toNanos() + " ns");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
