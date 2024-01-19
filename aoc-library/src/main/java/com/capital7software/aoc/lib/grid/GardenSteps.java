package com.capital7software.aoc.lib.grid;

import com.capital7software.aoc.lib.geometry.Direction;
import com.capital7software.aoc.lib.geometry.Point2D;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * While you wait, one of the Elves that works with the gardener heard how good you are at solving
 * problems and would like your help. He needs to get his steps in for the day, and so he'd like to
 * know which garden plots he can reach with exactly his remaining 64 steps.
 * <p><br>
 * He gives you an up-to-date map (your puzzle input) of his starting position (S), garden plots (.),
 * and rocks (#). For example:
 * <p><br>
 * <code>
 * ...........<br>
 * .....###.#.<br>
 * .###.##..#.<br>
 * ..#.#...#..<br>
 * ....#.#....<br>
 * .##..S####.<br>
 * .##..#...#.<br>
 * .......##..<br>
 * .##.#.####.<br>
 * .##..##.##.<br>
 * ...........<br>
 * </code>
 * <p><br>
 * The Elf starts at the starting position (S) which also counts as a garden plot. Then, he can
 * take one step north, south, east, or west, but only onto tiles that are garden plots. This
 * would allow him to reach any of the tiles marked O:
 * <p><br>
 * <code>
 * ...........<br>
 * .....###.#.<br>
 * .###.##..#.<br>
 * ..#.#...#..<br>
 * ....#O#....<br>
 * .##.OS####.<br>
 * .##..#...#.<br>
 * .......##..<br>
 * .##.#.####.<br>
 * .##..##.##.<br>
 * ...........<br>
 * </code>
 * <p><br>
 * Then, he takes a second step. Since at this point he could be at either tile marked O, his
 * second step would allow him to reach any garden plot that is one step north, south, east,
 * or west of any tile that he could have reached after the first step:
 * <p><br>
 * <code>
 * ...........<br>
 * .....###.#.<br>
 * .###.##..#.<br>
 * ..#.#O..#..<br>
 * ....#.#....<br>
 * .##O.O####.<br>
 * .##.O#...#.<br>
 * .......##..<br>
 * .##.#.####.<br>
 * .##..##.##.<br>
 * ...........<br>
 * </code>
 * <p><br>
 * After two steps, he could be at any of the tiles marked O above, including the starting
 * position (either by going north-then-south or by going west-then-east).
 * <p><br>
 * A single third step leads to even more possibilities:
 * <p><br>
 * <code>
 * ...........<br>
 * .....###.#.<br>
 * .###.##..#.<br>
 * ..#.#.O.#..<br>
 * ...O#O#....<br>
 * .##.OS####.<br>
 * .##O.#...#.<br>
 * ....O..##..<br>
 * .##.#.####.<br>
 * .##..##.##.<br>
 * ...........<br>
 * </code>
 * <p><br>
 * He will continue like this until his steps for the day have been exhausted. After a total
 * of 6 steps, he could reach any of the garden plots marked O:
 * <p><br>
 * <code>
 * ...........<br>
 * .....###.#.<br>
 * .###.##.O#.<br>
 * .O#O#O.O#..<br>
 * O.O.#.#.O..<br>
 * .##O.O####.<br>
 * .##.O#O..#.<br>
 * .O.O.O.##..<br>
 * .##.#.####.<br>
 * .##O.##.##.<br>
 * ...........<br>
 * </code>
 * <p><br>
 * In this example, if the Elf's goal was to get exactly 6 more steps today, he could use them
 * to reach any of 16 garden plots.
 * <p><br>
 * However, the Elf actually needs to get 64 steps today, and the map he's handed you is much
 * larger than the example map.
 * <p><br>
 * Starting from the garden plot marked S on your map, how many garden plots could the Elf
 * reach in exactly 64 steps?
 * <p><br>
 * The Elf seems confused by your answer until he realizes his mistake: he was reading from
 * a list of his favorite numbers that are both perfect squares and perfect cubes, not his step counter.
 * <p><br>
 * The actual number of steps he needs to get today is exactly 26501365.
 * <p><br>
 * He also points out that the garden plots and rocks are set up so that the map repeats
 * infinitely in every direction.
 * <p><br>
 * So, if you were to look one additional map-width or map-height out from the edge of the
 * example map above, you would find that it keeps repeating:
 * <p><br>
 * <code>
 * .................................<br>
 * .....###.#......###.#......###.#.<br>
 * .###.##..#..###.##..#..###.##..#.<br>
 * ..#.#...#....#.#...#....#.#...#..<br>
 * ....#.#........#.#........#.#....<br>
 * .##...####..##...####..##...####.<br>
 * .##..#...#..##..#...#..##..#...#.<br>
 * .......##.........##.........##..<br>
 * .##.#.####..##.#.####..##.#.####.<br>
 * .##..##.##..##..##.##..##..##.##.<br>
 * .................................<br>
 * .................................<br>
 * .....###.#......###.#......###.#.<br>
 * .###.##..#..###.##..#..###.##..#.<br>
 * ..#.#...#....#.#...#....#.#...#..<br>
 * ....#.#........#.#........#.#....<br>
 * .##...####..##..S####..##...####.<br>
 * .##..#...#..##..#...#..##..#...#.<br>
 * .......##.........##.........##..<br>
 * .##.#.####..##.#.####..##.#.####.<br>
 * .##..##.##..##..##.##..##..##.##.<br>
 * .................................<br>
 * .................................<br>
 * .....###.#......###.#......###.#.<br>
 * .###.##..#..###.##..#..###.##..#.<br>
 * ..#.#...#....#.#...#....#.#...#..<br>
 * ....#.#........#.#........#.#....<br>
 * .##...####..##...####..##...####.<br>
 * .##..#...#..##..#...#..##..#...#.<br>
 * .......##.........##.........##..<br>
 * .##.#.####..##.#.####..##.#.####.<br>
 * .##..##.##..##..##.##..##..##.##.<br>
 * .................................<br>
 * </code>
 * <p><br>
 * This is just a tiny three-map-by-three-map slice of the inexplicably-infinite farm layout;
 * garden plots and rocks repeat as far as you can see. The Elf still starts on the one middle
 * tile marked S, though - every other repeated S is replaced with a normal garden plot (.).
 * <p><br>
 * Here are the number of reachable garden plots in this new infinite version of the example
 * map for different numbers of steps:
 * <p><br>
 * <code>
 * In exactly 6 steps, he can still reach 16 garden plots.<br>
 * In exactly 10 steps, he can reach any of 50 garden plots.<br>
 * In exactly 50 steps, he can reach 1594 garden plots.<br>
 * In exactly 100 steps, he can reach 6536 garden plots.<br>
 * In exactly 500 steps, he can reach 167004 garden plots.<br>
 * In exactly 1000 steps, he can reach 668697 garden plots.<br>
 * In exactly 5000 steps, he can reach 16733044 garden plots.<br>
 * </code>
 * <p><br>
 * However, the step count the Elf needs is much larger! Starting from the garden plot marked
 * S on your infinite map, how many garden plots could the Elf reach in exactly 26501365 steps?
 */
public class GardenSteps {
    private enum GardenSpace {
        PLOT('.', true),
        ROCK('#', false);

        private final char label;
        private final boolean walkable;

        GardenSpace(char label, boolean walkable) {
            this.label = label;
            this.walkable = walkable;
        }

        public boolean isWalkable() {
            return walkable;
        }

        public static GardenSpace fromLabel(char label) {
            for (var value : values()) {
                if (value.label == label) {
                    return value;
                }
            }
            return null;
        }
    }

    private record GardenTile(GardenSpace entity, Point2D<Integer> point) {
        public boolean isWalkable() {
            return entity.isWalkable();
        }
    }

    private record RowResults(List<GardenTile> tiles, Point2D<Integer> initialPosition) {
    }

    private record Garden(Grid2D<GardenTile> grid, Point2D<Integer> initialPosition) {
        public static final Long LONG_WALK_THRESHOLD = 100L;

        public static Garden buildGarden(List<String> input) {
            var columns = new AtomicInteger(0);
            var rows = new AtomicInteger(0);
            var first = new AtomicBoolean(true);
            var startPoint = new AtomicReference<Point2D<Integer>>();
            var rowResults = new LinkedList<RowResults>();

            input.forEach(line -> {
                if (first.get()) {
                    columns.set(line.length());
                    first.set(false);
                }
                var results = parseLine(line, rows.getAndIncrement());
                if (startPoint.get() == null && results.initialPosition() != null) {
                    startPoint.set(results.initialPosition());
                }

                rowResults.add(results);
            });

            var tiles = new ArrayList<GardenTile>(rows.get() * columns.get() + columns.get());
            rowResults.stream().map(RowResults::tiles).forEach(tiles::addAll);

            return new Garden(
                    new Grid2D<>(
                            rows.get(),
                            columns.get(),
                            tiles.toArray(new GardenTile[rows.get() * columns.get()])
                    ),
                    startPoint.get()
            );
        }

        private static RowResults parseLine(String line, int row) {
            if (line == null || line.isBlank()) {
                return null;
            }

            var tiles = new LinkedList<GardenTile>();
            var columns = line.length();
            Point2D<Integer> startPoint = null;

            for (int i = 0; i < columns; i++) {
                char element = line.charAt(i);
                GardenTile tile;
                var point = new Point2D<>(i, row);
                if (element == 'S') {
                    startPoint = point;
                    tile = new GardenTile(GardenSpace.PLOT, point);
                } else {
                    tile = new GardenTile(GardenSpace.fromLabel(element), point);
                }
                tiles.add(tile);
            }

            return new RowResults(tiles, startPoint);
        }

        private long walk(long steps, boolean virtual) {
            return walk(initialPosition, steps, virtual);
        }

        /**
         * Take a walk through this Garden starting at the specified position and taking the specified number of
         * steps from that position. <p>If the specified position is not in this Garden a RuntimeException is thrown.
         * <p>If the specified position is not a walkable tile then a RuntimeException is thrown. <p>If the specified
         * number of steps is less than 0 then a RuntimeException is thrown.
         *
         * @param startingPosition The position to start from. Must be a walkable plot in this Garden!
         * @param steps            The number of steps that must be taken!
         * @return The total number of plots in this garden that can be reached from the specified starting position
         * and walking the specified number of steps.
         */
        private long walk(Point2D<Integer> startingPosition, long steps, boolean virtual) {
            if (!grid.isOnGrid(startingPosition)) {
                throw new RuntimeException("The startingPosition is not in this Garden! " + startingPosition);
            }
            if (steps < 0) {
                throw new RuntimeException("The number of steps is less than 0! " + steps);
            }

            var startTile = grid.get(startingPosition);

            if (!startTile.isWalkable()) {
                throw new RuntimeException("Cannot walk from the starting position! " + startTile);
            }

            if (steps == 0) {
                return 0; // Why even bother trying to take a walk!
            }

            if (steps > LONG_WALK_THRESHOLD) {
                if (!virtual) {
                    throw new RuntimeException("Requested a long walk in a non-virtual Garden!!");
                }
                return takeLongWalk(startingPosition, steps);
            }

            var reachedPlots = new HashSet<GardenTile>();

            walkAndTrackPlots(startTile, steps, reachedPlots, virtual);

            return reachedPlots.size();
        }

        private long takeLongWalk(Point2D<Integer> startingPosition, long steps) {
            var newStart = grid.virtualToReal(startingPosition);

            var startTile = grid.get(newStart);

            if (!startTile.isWalkable()) {
                throw new RuntimeException("Cannot walk from the starting position! " + startTile);
            }

            long cycles = steps / grid.columns();
            long remainder = steps % grid.columns();

            var previousStepsNeighbors = new HashSet<Point2D<Integer>>();
            previousStepsNeighbors.add(startTile.point());
            var pointsToConsider = new LinkedList<Point2D<Integer>>();
            var alreadySeen = new HashSet<Point2D<Integer>>();
            // Just find three points to consider!
            int stepsTaken = 0;
            for (int i = 0; i < 3; i++) {
                // Simply take all steps!
                while (stepsTaken < (long) i * grid.columns() + remainder) {
                    stepsTaken++;
                    // Add any new neighbors to this new queue
                    var newQueue = new HashSet<Point2D<Integer>>();
                    alreadySeen.clear();
                    for (var point : previousStepsNeighbors) {
                        for (var neighbor : getWalkableNeighbors(point)) {
                            if (alreadySeen.contains(neighbor)) {
                                continue;
                            }
                            newQueue.add(neighbor);
                            alreadySeen.add(neighbor);
                        }
                    }

                    previousStepsNeighbors = newQueue;
                }
                pointsToConsider.add(new Point2D<>(i, previousStepsNeighbors.size()));
            }

            return calculateQuadraticCurve(cycles, pointsToConsider);
        }

        private long calculateQuadraticCurve(long x, LinkedList<Point2D<Integer>> pointsToConsider) {
            double x1 = pointsToConsider.get(0).x();
            double y1 = pointsToConsider.get(0).y();
            double x2 = pointsToConsider.get(1).x();
            double y2 = pointsToConsider.get(1).y();
            double x3 = pointsToConsider.get(2).x();
            double y3 = pointsToConsider.get(2).y();

            return (long) (((x - x2) * (x - x3)) / ((x1 - x2) * (x1 - x3)) * y1 +
                    ((x - x1) * (x - x3)) / ((x2 - x1) * (x2 - x3)) * y2 +
                    ((x - x1) * (x - x2)) / ((x3 - x1) * (x3 - x2)) * y3);
        }

        private void walkAndTrackPlots(
                GardenTile startTile,
                long stepsToTake,
                HashSet<GardenTile> reachedPlots,
                boolean virtual
        ) {
            if (!startTile.isWalkable()) {
                return;
            }
            if (stepsToTake == 0) {
                reachedPlots.add(startTile);
                return;
            }

            var previousStepsNeighbors = new LinkedList<GardenTile>();
            previousStepsNeighbors.offer(startTile);
            // Use a little more memory to track if we already visited a neighbor to the queue
            var alreadySeen = new HashSet<GardenTile>();

            // Simply take all steps!
            for (int stepsTaken = 1; stepsTaken <= stepsToTake; stepsTaken++) {

                // Add any new neighbors to this new queue
                var newQueue = new LinkedList<GardenTile>();
                alreadySeen.clear();

                for (var tile : previousStepsNeighbors) {
                    for (var neighbor : getWalkableNeighbors(tile, virtual)) {
                        if (alreadySeen.contains(neighbor)) {
                            continue;
                        }
                        if (stepsTaken % 2 == 0 || stepsTaken == stepsToTake && !reachedPlots.contains(neighbor)) {
                            reachedPlots.add(neighbor); // Can go back and forth so just add it as a destination!
                        }
                        newQueue.offer(neighbor);
                        alreadySeen.add(neighbor);
                    }
                }

                previousStepsNeighbors = newQueue;
            }
        }

        private List<GardenTile> getWalkableNeighbors(GardenTile tile, boolean virtual) {
            var neighbors = new LinkedList<GardenTile>();

            for (var direction : Direction.CARDINAL_DIRECTIONS) {
                var newPoint = Grid2D.pointInDirection(tile.point(), direction);
                var realPoint = virtual ? grid.virtualToReal(newPoint) : newPoint;
                if (grid.isOnGrid(realPoint)) {
                    var neighbor = grid.get(realPoint);
                    if (neighbor.isWalkable()) {
                        neighbors.add(new GardenTile(neighbor.entity(), newPoint));
                    }
                }
            }

            return neighbors;
        }

        private List<Point2D<Integer>> getWalkableNeighbors(Point2D<Integer> point) {
            var neighbors = new LinkedList<Point2D<Integer>>();

            for (var direction : Direction.CARDINAL_DIRECTIONS) {
                var newPoint = Grid2D.pointInDirection(point, direction);
                var realPoint = grid.virtualToReal(newPoint);
                if (grid.isOnGrid(realPoint)) {
                    var neighbor = grid.get(realPoint);
                    if (neighbor.isWalkable()) {
                        neighbors.add(newPoint);
                    }
                }
            }

            return neighbors;
        }
    }

    private final Garden garden;

    private GardenSteps(@NotNull Garden garden) {
        this.garden = garden;
    }

    /**
     * Builds and returns a new GardenSteps instance loaded with the specified Garden.
     *
     * @param input The List of Strings to parse into a Garden.
     *
     * @return A new GardenSteps instance loaded with the specified Garden.
     */
    public static GardenSteps buildGardenSteps(List<String> input) {
        return new GardenSteps(Garden.buildGarden(input));
    }

    /**
     * Take a walk through the garden. Walks the specified number of steps. If virtual is true, the garden is
     * expanded to be an infinite garden. Returns the number of garden plots that can be reached by walking
     * the specified number of steps.
     *
     * @param steps The number of steps to take.
     * @param virtual If true, the garden is expanded to be an infinite garden.
     * @return The number of garden plots that can be reached by walking the specified number of steps.
     */
    public long walk(long steps, boolean virtual) {
        return garden.walk(steps, virtual);
    }
}
