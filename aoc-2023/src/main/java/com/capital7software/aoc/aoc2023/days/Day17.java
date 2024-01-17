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
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day17 {
    private record Point(int column, int row) {
        public Point pointInDirection(Direction direction) {
            return new Point(column() + direction.columnOffset, row() + direction.rowOffset);
        }
    }

    private enum Direction {
        NORTH(0, -1) {
            public Direction getLeft() {
                return WEST;
            }

            public Direction getRight() {
                return EAST;
            }
        },
        SOUTH(0, 1) {
            public Direction getLeft() {
                return EAST;
            }

            public Direction getRight() {
                return WEST;
            }
        },
        EAST(1, 0) {
            public Direction getLeft() {
                return NORTH;
            }

            public Direction getRight() {
                return SOUTH;
            }
        },
        WEST(-1, 0) {
            public Direction getLeft() {
                return SOUTH;
            }

            public Direction getRight() {
                return NORTH;
            }
        };

        private final int columnOffset;
        private final int rowOffset;

        Direction(int columnOffset, int rowOffset) {
            this.columnOffset = columnOffset;
            this.rowOffset = rowOffset;
        }

        public Direction getLeft() {
            return null;
        }

        public Direction getRight() {
            return null;
        }
    }

    private record Tile(int heat, Point point) {
    }

    private record Grid(List<Tile> tiles, int columns, int rows) {

        public static Grid parse(Stream<String> stream) {
            var tiles = new ArrayList<Tile>();
            var columns = new AtomicInteger(0);
            var rows = new AtomicInteger(0);
            var first = new AtomicBoolean(true);

            stream.forEach(line -> {
                if (first.get()) {
                    columns.set(line.length());
                    first.set(false);
                }
                var rowTiles = new ArrayList<Tile>(line.length());

                if (parseRow(line, rowTiles, rows.get())) {
                    rows.incrementAndGet();
                    tiles.addAll(rowTiles);
                    rowTiles.clear();
                }
            });
            return new Grid(tiles, columns.get(), rows.get());
        }

        private static boolean parseRow(String line, ArrayList<Tile> rowTiles, int row) {
            if (line == null || line.isBlank()) {
                return false;
            }

            var chars = line.toCharArray();

            for (int column = 0; column < chars.length; column++) {
                rowTiles.add(new Tile(Integer.parseInt(String.valueOf(chars[column])), new Point(column, row)));
            }

            return rowTiles.size() > 0;
        }

        public Tile get(int column, int row) {
            return tiles.get(column + (row * columns));
        }

        public Tile get(Point point) { return get(point.column(), point.row());}

        public boolean isOnGrid(int column, int row) {
            return column >= 0 && column < columns() && row >= 0 && row < rows();
        }

        public boolean isOnGrid(Point point) { return isOnGrid(point.column(), point.row()); }
        public Tile getFirst() {
            return get(0, 0);
        }

        public Tile getLast() {
            return get(columns() - 1, rows() - 1);
        }
    }

    private static class Crucible {
        private record Path(Tile tile, Direction direction, int steps) {
            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof Path path)) return false;
                return steps == path.steps && tile.equals(path.tile) && direction == path.direction;
            }

            @Override
            public int hashCode() {
                return Objects.hash(tile, steps, direction);
            }
        }

        private record PathCost(Crucible.Path path, long cost, long distanceToGoal) implements Comparable<PathCost> {
            public long pathCost() { return cost; }

            public long stepsInDirection() { return path.steps(); }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof PathCost pathCost)) return false;
                return cost == pathCost.cost && distanceToGoal == pathCost.distanceToGoal && path.equals(pathCost.path);
            }

            @Override
            public int hashCode() {
                return Objects.hash(path, cost, distanceToGoal);
            }

            @Override
            public int compareTo(PathCost o) {
                return Long.compare(pathCost(), o.pathCost());
            }
        }

        private final Grid grid;
        private final Tile start;
        private final Tile end;

        private final int minSteps;
        private final int maxSteps;

        public Crucible(Grid grid, Tile start, Tile end, int minSteps, int maxSteps) {
            this.grid = grid;
            this.start = start;
            this.end = end;
            this.minSteps = minSteps;
            this.maxSteps = maxSteps;
        }

        public Crucible(Grid grid, int minSteps, int maxSteps) {
            this(grid, grid.getFirst(), grid.getLast(), minSteps, maxSteps);
        }

        public long calculateMinimumHeatLoss() {
            var visited = new HashSet<Path>();

            var costs = new HashMap<Path, Long>();

            var queue = new PriorityQueue<PathCost>();

            queue.offer(new PathCost(new Path(start, Direction.EAST, 0), 0, grid.rows() + grid.columns()));
            queue.offer(new PathCost(new Path(start, Direction.SOUTH, 0), 0, grid.rows() + grid.columns()));

            while (!queue.isEmpty()) {
                var pathCost = queue.poll();

                // Stopping conditions
                if (pathCost.path().tile().equals(end) && pathCost.stepsInDirection() >= minSteps) {
                    // We are at the end!
                    return pathCost.cost();
                }

                if (visited.contains(pathCost.path())) {
                    // We already have been here!
                    continue;
                }

                visited.add(pathCost.path());

                // Get our neighbors!
                for (var neighbor : getNeighbors(pathCost)) {
                    // Track how much of a heat loss the path will increase by
                    var newCost = pathCost.cost() + neighbor.tile().heat();

                    if (costs.computeIfAbsent(neighbor, it -> Long.MAX_VALUE) <= newCost) {
                        continue; // New heat is greater than the existing heat for this path!!
                    }
                    costs.put(neighbor, newCost);
                    var distanceToEnd = (end.point().column() - neighbor.tile().point().column()) +
                            (end.point().row() - neighbor.tile().point().row());
                    // Add this path back to the queue!
                    queue.offer(new PathCost(neighbor, newCost, distanceToEnd));
                }
            }

            return costs.keySet().stream().filter(it -> it.tile().equals(end))
                    .mapToLong(costs::get)
                    .min()
                    .orElse(Long.MAX_VALUE);
        }

        private List<Path> getNeighbors(PathCost pathCost) {
            var neighbors = new ArrayList<Path>(Direction.values().length);

            if (pathCost.stepsInDirection() < minSteps) {
                var neighbor = takeStepForward(pathCost.path());
                if (neighbor != null) {
                    neighbors.add(neighbor);
                }
            } else if (pathCost.stepsInDirection() < maxSteps) {
                var neighbor = takeStepForward(pathCost.path());
                if (neighbor != null) {
                    neighbors.add(neighbor);
                }
                neighbor = takeStepRight(pathCost.path());
                if (neighbor != null) {
                    neighbors.add(neighbor);
                }
                neighbor = takeStepLeft(pathCost.path());
                if (neighbor != null) {
                    neighbors.add(neighbor);
                }
            } else {
                var neighbor = takeStepRight(pathCost.path());
                if (neighbor != null) {
                    neighbors.add(neighbor);
                }
                neighbor = takeStepLeft(pathCost.path());
                if (neighbor != null) {
                    neighbors.add(neighbor);
                }
            }

            return neighbors;
        }

        private Path takeStepForward(Path path) {
            Point point = path.tile().point();
            Direction direction = path.direction();
            var newPoint = point.pointInDirection(direction);
            if (grid.isOnGrid(newPoint)) {
                var newTile = grid.get(newPoint);
                return new Path(newTile, direction, path.steps() + 1);
            }

            return null;
        }

        private Path takeStepLeft(Path path) {
            Point point = path.tile().point();
            Direction direction = path.direction().getLeft();
            var newPoint = point.pointInDirection(direction);
            if (grid.isOnGrid(newPoint)) {
                var newTile = grid.get(newPoint);
                return new Path(newTile, direction, 1);
            }

            return null;
        }

        private Path takeStepRight(Path path) {
            Point point = path.tile().point();
            Direction direction = path.direction().getRight();
            var newPoint = point.pointInDirection(direction);
            if (grid.isOnGrid(newPoint)) {
                var newTile = grid.get(newPoint);
                return new Path(newTile, direction, 1);
            }

            return null;
        }
    }

    private static final String inputFilename = "inputs/input_day_17-01.txt";

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
            LOGGER.info(String.format("Part 1: Start!");
            var grid = Grid.parse(stream);
            var crucible = new Crucible(grid, 1, 3);
            IntStream.range(0, 5).forEach(it -> {
                var start = Instant.now();
                var sum = crucible.calculateMinimumHeatLoss();
                var end = Instant.now();
                LOGGER.info(String.format("Minimum heat loss: " + sum + " in " +
                        Duration.between(start, end).toNanos() + " ns");
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void part2(Path path) {
        try (var stream = Files.lines(path)) {
            // Part 2
            LOGGER.info(String.format("Part 2: Start!");
            var grid = Grid.parse(stream);
            var crucible = new Crucible(grid, 4, 10);
            IntStream.range(0, 5).forEach(it -> {
                var start = Instant.now();
                var sum = crucible.calculateMinimumHeatLoss();
                var end = Instant.now();
                LOGGER.info(String.format("Minimum heat loss: " + sum + " in " +
                        Duration.between(start, end).toNanos() + " ns");
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
