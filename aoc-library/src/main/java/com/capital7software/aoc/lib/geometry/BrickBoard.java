package com.capital7software.aoc.lib.geometry;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * The stack is tall enough that you'll have to be careful about choosing which bricks to
 * disintegrate; if you disintegrate the wrong brick, large portions of the stack could
 * topple, which sounds pretty dangerous.
 * <p><br>
 * The Elves responsible for water filtering operations took a snapshot of the bricks while
 * they were still falling (your puzzle input) which should let you work out which bricks
 * are safe to disintegrate. For example:
 * <p><br>
 * <code>
 * 1,0,1~1,2,1<br>
 * 0,0,2~2,0,2<br>
 * 0,2,3~2,2,3<br>
 * 0,0,4~0,2,4<br>
 * 2,0,5~2,2,5<br>
 * 0,1,6~2,1,6<br>
 * 1,1,8~1,1,9<br>
 * </code>
 * <p><br>
 * Each line of text in the snapshot represents the position of a single brick at the time
 * the snapshot was taken. The position is given as two x,y,z coordinates - one for each
 * end of the brick - separated by a tilde (~). Each brick is made up of a single straight
 * line of cubes, and the Elves were even careful to choose a time for the snapshot that
 * had all the free-falling bricks at integer positions above the ground, so the whole
 * snapshot is aligned to a three-dimensional cube grid.
 * <p><br>
 * A line like 2,2,2~2,2,2 means that both ends of the brick are at the same coordinate -
 * in other words, that the brick is a single cube.
 * <p><br>
 * Lines like 0,0,10~1,0,10 or 0,0,10~0,1,10 both represent bricks that are two cubes in volume,
 * both oriented horizontally. The first brick extends in the x direction, while the second
 * brick extends in the y direction.
 * <p><br>
 * A line like 0,0,1~0,0,10 represents a ten-cube brick which is oriented vertically. One end of
 * the brick is the cube located at 0,0,1, while the other end of the brick is located directly
 * above it at 0,0,10.
 * <p><br>
 * The ground is at z=0 and is perfectly flat; the lowest z value a brick can have is therefore 1.
 * So, 5,5,1~5,6,1 and 0,2,1~0,2,5 are both resting on the ground, but 3,3,2~3,3,3 was above
 * the ground at the time of the snapshot.
 * <p><br>
 * Because the snapshot was taken while the bricks were still falling, some bricks will still
 * be in the air; you'll need to start by figuring out where they will end up. Bricks are
 * magically stabilized, so they never rotate, even in weird situations like where a long
 * horizontal brick is only supported on one end. Two bricks cannot occupy the same position,
 * so a falling brick will come to rest upon the first other brick it encounters.
 * <p><br>
 * Here is the same example again, this time with each brick given a letter, so it can be m
 * arked in diagrams:
 * <p><br>
 * <code>
 * 1,0,1~1,2,1   &#60;- A<br>
 * 0,0,2~2,0,2   &#60;- B<br>
 * 0,2,3~2,2,3   &#60;- C<br>
 * 0,0,4~0,2,4   &#60;- D<br>
 * 2,0,5~2,2,5   &#60;- E<br>
 * 0,1,6~2,1,6   &#60;- F<br>
 * 1,1,8~1,1,9   &#60;- G<br>
 * </code>
 * <p><br>
 * At the time of the snapshot, from the side so the x axis goes left to right, these bricks
 * are arranged like this:
 * <p><br>
 * <code>
 * &nbsp;x<br>
 * 012<br>
 * .G. 9<br>
 * .G. 8<br>
 * ... 7<br>
 * FFF 6<br>
 * ..E 5 z<br>
 * D.. 4<br>
 * CCC 3<br>
 * BBB 2<br>
 * .A. 1<br>
 * --- 0<br>
 * </code>
 * <p><br>
 * Rotating the perspective 90 degrees so the y axis now goes left to right, the same
 * bricks are arranged like this:
 * <p><br>
 * <code>
 * &nbsp;y<br>
 * 012<br>
 * .G. 9<br>
 * .G. 8<br>
 * ... 7<br>
 * .F. 6<br>
 * EEE 5 z<br>
 * DDD 4<br>
 * ..C 3<br>
 * B.. 2<br>
 * AAA 1<br>
 * --- 0<br>
 * </code>
 * <p><br>
 * Once all the bricks fall downward as far as they can go, the stack looks like this,
 * where ? means bricks are hidden behind other bricks at that location:
 * <p><br>
 * <code>
 * &nbsp;x<br>
 * 012<br>
 * .G. 6<br>
 * .G. 5<br>
 * FFF 4<br>
 * D.E 3 z<br>
 * ??? 2<br>
 * .A. 1<br>
 * --- 0<br>
 * </code>
 * <p><br>
 * Again from the side:
 * <p><br>
 * <code>
 * &nbsp;y<br>
 * 012<br>
 * .G. 6<br>
 * .G. 5<br>
 * .F. 4<br>
 * ??? 3 z<br>
 * B.C 2<br>
 * AAA 1<br>
 * --- 0<br>
 * </code>
 * <p><br>
 * Now that all the bricks have settled, it becomes easier to tell which bricks are supporting which other bricks:
 * <p><br>
 * <code>
 * Brick A is the only brick supporting bricks B and C.<br>
 * Brick B is one of two bricks supporting brick D and brick E.<br>
 * Brick C is the other brick supporting brick D and brick E.<br>
 * Brick D supports brick F.<br>
 * Brick E also supports brick F.<br>
 * Brick F supports brick G.<br>
 * Brick G isn't supporting any bricks.<br>
 * </code>
 * <p><br>
 * Your first task is to figure out which bricks are safe to disintegrate. A brick can be safely
 * disintegrated if, after removing it, no other bricks would fall further directly downward.
 * Don't actually disintegrate any bricks - just determine what would happen if, for each brick,
 * only that brick were disintegrated. Bricks can be disintegrated even if they're completely
 * surrounded by other bricks; you can squeeze between bricks if you need to.
 * <p><br>
 * In this example, the bricks can be disintegrated as follows:
 * <p><br>
 * <code>
 * Brick A cannot be disintegrated safely; if it were disintegrated, bricks B and C would both fall.<br>
 * Brick B can be disintegrated; the bricks above it (D and E) would still be supported by brick C.<br>
 * Brick C can be disintegrated; the bricks above it (D and E) would still be supported by brick B.<br>
 * Brick D can be disintegrated; the brick above it (F) would still be supported by brick E.<br>
 * Brick E can be disintegrated; the brick above it (F) would still be supported by brick D.<br>
 * Brick F cannot be disintegrated; the brick above it (G) would fall.<br>
 * Brick G can be disintegrated; it does not support any other bricks.<br>
 * </code>
 * <p><br>
 * So, in this example, 5 bricks can be safely disintegrated.
 * <p><br>
 * Figure how the blocks will settle based on the snapshot. Once they've settled, consider
 * disintegrating a single brick; how many bricks could be safely chosen as the one to get disintegrated?
 * <p><br>
 * Disintegrating bricks one at a time isn't going to be fast enough. While it might sound dangerous,
 * what you really need is a chain reaction.
 * <p><br>
 * You'll need to figure out the best brick to disintegrate. For each brick, determine how many other
 * bricks would fall if that brick were disintegrated.
 * <p><br>
 * Using the same example as above:
 * <p><br>
 * <code>
 * Disintegrating brick A would cause all 6 other bricks to fall.<br>
 * Disintegrating brick F would cause only 1 other brick, G, to fall.<br>
 * Disintegrating any other brick would cause no other bricks to fall.<br>
 * </code>
 * <p><br>
 * So, in this example, the sum of the number of other bricks that would fall as
 * a result of disintegrating each brick is 7.
 * <p><br>
 * For each brick, determine how many other bricks would fall if that brick were disintegrated.
 * What is the sum of the number of other bricks that would fall?
 */
public class BrickBoard {
    /**
     * Represents a three-dimensional Brick for the BrickBoard.
     */
    public static class Brick implements Comparable<Brick> {
        private final int id;
        private LineSegment3D<Double> lineSegment;

        /**
         * Instantiates a new Brick with the specified ID and LineSegment3D.
         *
         * @param id          The ID of the new Brick.
         * @param lineSegment The LineSegment3D of the new Brick.
         */
        public Brick(int id, LineSegment3D<Double> lineSegment) {
            this.id = id;
            this.lineSegment = lineSegment;
        }

        /**
         * Returns the number of cubes that make up this Brick.
         *
         * @return The number of cubes that make up this Brick.
         */
        public long cubeCount() {
            return lineSegment.length().longValue() + 1;
        }

        /**
         * Returns the Orientation3D of this Brick.
         * @return The Orientation3D of this Brick.
         */
        public Orientation3D orientation() {
            return lineSegment.orientation();
        }

        /**
         * Returns the Point3D of the start point of this Brick.
         *
         * @return The Point3D of the start point of this Brick.
         */
        public Point3D<Double> start() {
            return lineSegment.start();
        }

        /**
         * Returns the Point3D of the end point of this Brick.
         *
         * @return The Point3D of the end point of this Brick.
         */
        public Point3D<Double> end() {
            return lineSegment.end();
        }

        @Override
        public int compareTo(Brick o) {
            return Long.compare(
                    Math.min(lineSegment.start().z().longValue(), lineSegment.end().z().longValue()),
                    Math.min(o.lineSegment.start().z().longValue(), o.lineSegment.end().z().longValue()));
        }

        /**
         * Returns the ID of this Brick.
         * @return The ID of this Brick.
         */
        public int getId() {
            return id;
        }

        /**
         * Move this brick down the Z-Axis the specified amount.
         *
         * @param amount THe amount to move down the Z-Axis by.
         */
        public void moveDown(long amount) {
            var newStart = new Point3D<>(
                    lineSegment.start().x(),
                    lineSegment.start().y(),
                    lineSegment.start().z() - amount
            );
            var newEnd = new Point3D<>(
                    lineSegment.end().x(),
                    lineSegment.end().y(),
                    lineSegment.end().z() - amount
            );
            lineSegment = new LineSegment3D<>(newStart, newEnd);
        }

        /**
         * Returns true if this Brick is below the other Brick.
         *
         * @param other The Brick to test against.
         * @return True if this Brick is below the other Brick.
         */
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
            if (this == o) {
                return true;
            }
            if (!(o instanceof Brick brick)) {
                return false;
            }
            return getId() == brick.getId();
        }

        @Override
        public int hashCode() {
            return Objects.hash(getId());
        }
    }

    private final List<Brick> bricks;
    private final Map<Long, Set<Brick>> zBricks;
    private final Map<Brick, Set<Brick>> supports;
    private final Map<Brick, Set<Brick>> supportedBy;

    private BrickBoard(
            @NotNull List<Brick> bricks,
            @NotNull Map<Long, Set<Brick>> zBricks,
            @NotNull Map<Brick, Set<Brick>> supports,
            @NotNull Map<Brick, Set<Brick>> supportedBy
    ) {
        this.bricks = bricks;
        this.zBricks = zBricks;
        this.supports = supports;
        this.supportedBy = supportedBy;
    }

    /**
     * Builds and returns a new BrickBoard loaded with the specified Bricks.
     *
     * @param input The List of Strings to parse into Bricks.
     * @return A new BrickBoard loaded with the specified Bricks.
     */
    public static BrickBoard buildBrickBoard(List<String> input) {
        var count = new AtomicInteger(0);

        var bricks = input.stream().map(line -> parseBrick(line, count.getAndIncrement())).sorted().toList();

        return new BrickBoard(bricks, new LinkedHashMap<>(), new LinkedHashMap<>(), new LinkedHashMap<>());
    }

    private static Brick parseBrick(String line, int count) {
        var split = line.split("~");

        var start = split[0].trim().split(",");
        var end = split[1].trim().split(",");

        var startPoint = new Point3D<>(
                (double) Integer.parseInt(start[0]),
                (double) Integer.parseInt(start[1]),
                (double) Integer.parseInt(start[2])
        );
        var endPoint = new Point3D<>(
                (double) Integer.parseInt(end[0]),
                (double) Integer.parseInt(end[1]),
                (double) Integer.parseInt(end[2])
        );
        var segment = new LineSegment3D<>(startPoint, endPoint);

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
                        default -> {}
                    }

                    builder.append("\n");
                    builder.append("Brick ID: ");
                    builder.append(brick.getId());
                    builder.append(", Cubes: ");
                    builder.append(cubeCount);
                    builder.append(", Orientation3D: ");
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
                zBricks.computeIfAbsent(brick.lineSegment.end().z().longValue(), it -> new HashSet<>()).add(brick);
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
            zBricks.computeIfAbsent(brick.lineSegment.end().z().longValue(), it -> new HashSet<>()).add(brick);
        }
    }

    private long calculateDropDistance(Brick brick, Set<Brick> dependents) {
        var distanceToMove = brick.lineSegment.start().z().longValue() - 1;

        if (!dependents.isEmpty()) {
            distanceToMove -= dependents
                    .stream()
                    .max(Comparator.comparingLong(a -> a.lineSegment.end().z().longValue()))
                    .get()
                    .lineSegment
                    .end()
                    .z()
                    .longValue();
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

    /**
     * Returns a Set of Bricks that are safe to disintegrate.
     *
     * @return A Set of Bricks that are safe to disintegrate.
     */
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
