package com.capital7software.aoc.aoc2023.days;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class Day21 {

    public enum Direction {
        NORTH(0, -1) {
            @Override
            public Direction opposite() {
                return SOUTH;
            }

            @Override
            public Set<Direction> getPerpendicular() {
                return eastWest;
            }
        },
        SOUTH(0, 1) {
            @Override
            public Direction opposite() {
                return NORTH;
            }

            @Override
            public Set<Direction> getPerpendicular() {
                return eastWest;
            }
        },
        EAST(1, 0) {
            @Override
            public Direction opposite() {
                return WEST;
            }

            @Override
            public Set<Direction> getPerpendicular() {
                return northSouth;
            }
        },
        WEST(-1, 0) {
            @Override
            public Direction opposite() {
                return EAST;
            }

            @Override
            public Set<Direction> getPerpendicular() {
                return northSouth;
            }
        };

        private final int columnOffset;
        private final int rowOffset;

        private static final Set<Direction> northSouth = Set.of(NORTH, SOUTH);
        private static final Set<Direction> eastWest = Set.of(EAST, WEST);

        Direction(int columnOffset, int rowOffset) {
            this.columnOffset = columnOffset;
            this.rowOffset = rowOffset;
        }

        public int getColumnOffset() {
            return columnOffset;
        }

        public int getRowOffset() {
            return rowOffset;
        }

        public Direction opposite() { return null; }

        public Set<Direction> getPerpendicular() { return Collections.emptySet(); }
    }

    public interface Point {
    }

    public interface Tile {

    }

    public interface Grid<T extends Tile, P extends Point> {
        T get(P point);
    }

    public record Point2DInt(int x, int y) implements Point {
        public Point2DInt pointInDirection(Direction direction) {
            return new Point2DInt(x + direction.getColumnOffset(), y + direction.getRowOffset());
        }
    }

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

    private record GardenTile(GardenSpace entity, Point2DInt point) implements Tile {
        public boolean isWalkable() {
            return entity.isWalkable();
        }
    }

    public record GardenGrid<T extends Tile> (List<T> tiles, long rows,
                              long columns) implements Grid<T, Point2DInt> {
        @Override
        public T get(Point2DInt point) {
            return tiles.get((int) (point.x() + (columns * point.y())));
        }

        public boolean isOnGrid(Point2DInt point) {
            return point.x() >= 0 && point.x() < columns && point.y() >= 0 && point.y() < rows;
        }

        public Point2DInt virtualToReal(Point2DInt point) {
            long x = ((point.x() % columns) + columns) % columns;
            long y = ((point.y() % rows) + rows) % rows;

            return new Point2DInt((int) x, (int) y);
        }

    }

    public record RowResults<T extends Tile>(List<T> tiles, Point2DInt initialPosition) {
    }

    private record Garden(GardenGrid<GardenTile> grid, Point2DInt initialPosition) {
        public static final Long LONG_WALK_THRESHOLD = 100L;
        public static Garden build(Stream<String> stream) {
            var columns = new AtomicInteger(0);
            var rows = new AtomicInteger(0);
            var first = new AtomicBoolean(true);
            var startPoint = new AtomicReference<Point2DInt>();
            var rowResults = new LinkedList<RowResults<GardenTile>>();

            stream.forEach(line -> {
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

            return new Garden(new GardenGrid<>(tiles, rows.get(), columns.get()), startPoint.get());
        }

        private static RowResults<GardenTile> parseLine(String line, int row) {
            if (line == null || line.isBlank()) {
                return null;
            }

            var tiles = new LinkedList<GardenTile>();
            var columns = line.length();
            Point2DInt startPoint = null;

            for (int i = 0; i < columns; i++) {
                char element = line.charAt(i);
                GardenTile tile;
                var point = new Point2DInt(i, row);
                if (element == 'S') {
                    startPoint = point;
                    tile = new GardenTile(GardenSpace.PLOT, point);
                } else {
                    tile = new GardenTile(GardenSpace.fromLabel(element), point);
                }
                tiles.add(tile);
            }

            return new RowResults<>(tiles, startPoint);
        }

        long walk(long steps, boolean virtual) {
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
        long walk(Point2DInt startingPosition, long steps, boolean virtual) {
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

        private long takeLongWalk(Point2DInt startingPosition, long steps) {
            var newStart = grid.virtualToReal(startingPosition);

            var startTile = grid.get(newStart);

            if (!startTile.isWalkable()) {
                throw new RuntimeException("Cannot walk from the starting position! " + startTile);
            }

            long cycles = steps / grid.columns();
            long remainder = steps % grid.columns();

            var previousStepsNeighbors = new HashSet<Point2DInt>();
            previousStepsNeighbors.add(startTile.point());
            var pointsToConsider = new LinkedList<Point2DInt>();
            var alreadySeen = new HashSet<Point2DInt>();
            // Just find three points to consider!
            int stepsTaken = 0;
            for (int i = 0; i < 3; i++) {
                // Simply take all steps!
                while (stepsTaken < i * grid.columns() + remainder) {
                    stepsTaken++;
                    // Add any new neighbors to this new queue
                    var newQueue = new HashSet<Point2DInt>();
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
                pointsToConsider.add(new Point2DInt(i, previousStepsNeighbors.size()));
            }

            return calculateQuadraticCurve(cycles, pointsToConsider);
        }

        private long calculateQuadraticCurve(long x, LinkedList<Point2DInt> pointsToConsider) {
            double x1 = pointsToConsider.get(0).x();
            double y1 = pointsToConsider.get(0).y();
            double x2 = pointsToConsider.get(1).x();
            double y2 = pointsToConsider.get(1).y();
            double x3 = pointsToConsider.get(2).x();
            double y3 = pointsToConsider.get(2).y();

            return (long) (((x-x2) * (x-x3)) / ((x1-x2) * (x1-x3)) * y1 +
                    ((x-x1) * (x-x3)) / ((x2-x1) * (x2-x3)) * y2 +
                    ((x-x1) * (x-x2)) / ((x3-x1) * (x3-x2)) * y3);
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
            // Use a little more memory to track if we already a neighbor to the queue
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

        List<GardenTile> getWalkableNeighbors(GardenTile tile, boolean virtual) {
            var neighbors = new LinkedList<GardenTile>();

            for (var direction : Direction.values()) {
                var newPoint = tile.point().pointInDirection(direction);
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

        List<Point2DInt> getWalkableNeighbors(Point2DInt point) {
            var neighbors = new LinkedList<Point2DInt>();

            for (var direction : Direction.values()) {
                var newPoint = point.pointInDirection(direction);
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

    private static final String inputFilename = "inputs/input_day_21-01.txt";

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
            var garden = Garden.build(stream);
            var stepsToTake = 64;
            var start = Instant.now();
            var plotCount = garden.walk(stepsToTake, false);
            var end = Instant.now();
            System.out.println("Total number of plots the Elf can reach in " + stepsToTake + " steps: "
                    + plotCount + " in " + Duration.between(start, end).toNanos() + " ns");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void part2(Path path) {
        try (var stream = Files.lines(path)) {
            // Part 2
            System.out.println("Part 2: Start!");
            var garden = Garden.build(stream);
            var stepsToTake = 26501365;
            var start = Instant.now();
            var plotCount = garden.walk(stepsToTake, true);
            var end = Instant.now();
            System.out.println("Total number of plots the Elf can reach in " + stepsToTake + " steps: "
                    + plotCount + " in " + Duration.between(start, end).toNanos() + " ns");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
