package com.capital7software.aoc2023.days;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day16 {
    private enum Direction {
        NORTH(0, -1),
        SOUTH(0, 1),
        EAST(1, 0),
        WEST(-1, 0);

        private final int columnOffset;
        private final int rowOffset;

        Direction(int columnOffset, int rowOffset) {
            this.columnOffset = columnOffset;
            this.rowOffset = rowOffset;
        }
    }

    private enum Entity {
        EMPTY_SPACE('.') {
            public List<Direction> nextHeading(Direction heading) {
                switch (heading) {
                    case NORTH -> {
                        return NORTH;
                    }
                    case EAST -> {
                        return EAST;
                    }
                    case SOUTH -> {
                        return SOUTH;
                    }
                    case WEST -> {
                        return WEST;
                    }
                    default -> throw new RuntimeException("Unknown heading: " + heading);
                }
            }
        },
        UPWARD_MIRROR('/') {
            public List<Direction> nextHeading(Direction heading) {
                switch (heading) {
                    case NORTH -> {
                        return EAST;
                    }
                    case EAST -> {
                        return NORTH;
                    }
                    case SOUTH -> {
                        return WEST;
                    }
                    case WEST -> {
                        return SOUTH;
                    }
                    default -> throw new RuntimeException("Unknown heading: " + heading);
                }
            }
        },
        DOWNWARD_MIRROR('\\') {
            public List<Direction> nextHeading(Direction heading) {
                switch (heading) {
                    case NORTH -> {
                        return WEST;
                    }
                    case EAST -> {
                        return SOUTH;
                    }
                    case SOUTH -> {
                        return EAST;
                    }
                    case WEST -> {
                        return NORTH;
                    }
                    default -> throw new RuntimeException("Unknown heading: " + heading);
                }
            }
        },
        VERTICAL_SPLITTER('|') {
            public List<Direction> nextHeading(Direction heading) {
                switch (heading) {
                    case NORTH -> {
                        return NORTH;
                    }
                    case EAST, WEST -> {
                        return SPLIT_VERTICAL;
                    }
                    case SOUTH -> {
                        return SOUTH;
                    }
                    default -> throw new RuntimeException("Unknown heading: " + heading);
                }
            }
        },
        HORIZONTAL_SPLITTER('-') {
            public List<Direction> nextHeading(Direction heading) {
                switch (heading) {
                    case NORTH, SOUTH -> {
                        return SPLIT_HORIZONTAL;
                    }
                    case EAST -> {
                        return EAST;
                    }
                    case WEST -> {
                        return WEST;
                    }
                    default -> throw new RuntimeException("Unknown heading: " + heading);
                }
            }
        };

        private static final List<Direction> SPLIT_VERTICAL = List.of(Direction.NORTH, Direction.SOUTH);
        private static final List<Direction> SPLIT_HORIZONTAL = List.of(Direction.WEST, Direction.EAST);
        private static final List<Direction> NORTH = List.of(Direction.NORTH);
        private static final List<Direction> EAST = List.of(Direction.EAST);
        private static final List<Direction> SOUTH = List.of(Direction.SOUTH);
        private static final List<Direction> WEST = List.of(Direction.WEST);
        private static final List<Direction> EMPTY = new ArrayList<>();

        private final char label;

        Entity(char label) {
            this.label = label;
        }

        public static Entity from(char label) {
            for (var value : values()) {
                if (value.label == label) {
                    return value;
                }
            }

            throw new RuntimeException("Unknown label:" + label);
        }

        public List<Direction> nextHeading(Direction heading) {
            return EMPTY;
        }

        @Override
        public String toString() {
            return String.valueOf(label);
        }
    }

    private record Tile(Entity entity, int column, int row) {

        public static Tile from(char label, int column, int row) {
            return new Tile(Entity.from(label), column, row);
        }

        public List<Direction> nextHeading(Direction direction) {
            return entity.nextHeading(direction);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Tile tile)) return false;
            return column == tile.column && row == tile.row && entity == tile.entity;
        }

        @Override
        public int hashCode() {
            return Objects.hash(entity, column, row);
        }

        @Override
        public String toString() {
            return entity.toString();
        }
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

            for (var column = 0; column < chars.length; column++) {
                rowTiles.add(Tile.from(chars[column], column, row));
            }

            return rowTiles.size() > 0;
        }

        public Tile get(int column, int row) {
            return tiles.get(column + (row * columns));
        }

        public List<? extends Map.Entry<Tile, Direction>> getNext(Tile tile, Direction heading) {
            var directions = tile.nextHeading(heading);
            var result = new ArrayList<Map.Entry<Tile, Direction>>(directions.size());

            directions.forEach(direction -> {
                        var nextColumn = tile.column() + direction.columnOffset;
                        var nextRow = tile.row() + direction.rowOffset;
                        if (nextColumn >= 0 && nextColumn < columns() && nextRow >= 0 && nextRow < rows()) {
                            result.add(new AbstractMap.SimpleEntry<>(get(nextColumn, nextRow), direction));
                        }
                    });

            return result;
        }
    }

    private record Beam(Grid grid, int startColumn, int startRow, Direction startHeading) {

        public static Beam create(Grid grid) {
            return new Beam(grid, 0, 0, Direction.EAST);
        }

        public int maxEnergized() {
            final Set<Integer> max = ConcurrentHashMap.newKeySet();
            try  {
                ForkJoinPool pool = new ForkJoinPool(32);
                var tasks = new ArrayList<ForkJoinTask<Integer>>(4 * (grid().columns * grid().rows()));

                for (int i = 0; i < grid.columns(); i++) {
                    int finalI = i;
                    tasks.add(pool.submit(() -> {
                        max.add(energize(finalI, 0, Direction.SOUTH, new HashMap<>()));
                        max.add(energize(finalI, grid().rows() - 1, Direction.NORTH, new HashMap<>()));
                        return 0;
                    }));
                }

                for (int i = 0; i < grid.rows(); i++) {
                    int finalI = i;
                    tasks.add(pool.submit(() -> {
                        max.add(energize(0, finalI, Direction.EAST, new HashMap<>()));
                        max.add(energize(grid().columns() - 1, finalI, Direction.WEST, new HashMap<>()));
                        return 0;
                    }));
                }
                tasks.forEach(it -> {
                    try {
                        it.get();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                });
                pool.shutdown();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return max.stream().max(Integer::compareTo).orElse(0);
        }

        public int energize() {
            return energize(startColumn, startRow, startHeading, new HashMap<>());
        }

        public int energizeRecursive() {
            var tile = grid.get(startColumn, startRow);
            var visited = new HashMap<Tile, Set<Direction>>();
            traverseRecursive(tile, startHeading, visited);

            return visited.size();
        }

        public int energize(int column, int row, Direction heading, Map<Tile, Set<Direction>> visited) {
            // get the starting tile!
            var tile = grid.get(column, row);

            //            traverseRecursive(tile, startHeading);
            // We will perform the traversal iteratively since we run out of stack space performing it recursively
            var queue = new LinkedList<Map.Entry<Tile, Direction>>();
            queue.offer(new AbstractMap.SimpleEntry<>(tile, heading));

            while (queue.peek() != null) {
                var entry = queue.poll();
                var currentTile = entry.getKey();
                var currentHeading = entry.getValue();
                var visits = visited.computeIfAbsent(currentTile, it -> new HashSet<>());

                if (visits.contains(currentHeading)) {
                    continue; // We already visited this tile from this direction!
                }

                // We visit this tile and then get the next heading(s) and continue
                visits.add(currentHeading);

                grid.getNext(currentTile, currentHeading).forEach(queue::offer);
            }

            return visited.size();
        }

        private void traverseRecursive(Tile tile, Direction heading, Map<Tile, Set<Direction>> visited) {
            // Really cannot use on large grids unless the stack space is increased
            // Stopping conditions first
            var visits = visited.computeIfAbsent(tile, it -> new HashSet<>(Direction.values().length));

            if (visits.contains(heading)) {
                return; // We already visited this tile from this direction!
            }

            // We visit this tile and then get the next heading(s) and continue
            visits.add(heading);

            grid.getNext(tile, heading).forEach(it -> traverseRecursive(it.getKey(), it.getValue(), visited));
        }
    }

    private static final String inputFilename = "inputs/input_day_16-01.txt";

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
            var grid = Grid.parse(stream);
            var beam = Beam.create(grid);
//            System.out.println(grid);
            IntStream.range(0, 5).forEach(it -> {
                var start = Instant.now();
                var sum = beam.energize();
                var end = Instant.now();
                System.out.println("Total number of energized tiles: " + sum + " in " +
                        Duration.between(start, end).toNanos() + " ns");
            });

//            IntStream.range(0, 5).forEach(it -> {
//                var start = Instant.now();
//                var sum = beam.energizeRecursive();
//                var end = Instant.now();
//                System.out.println("Total number of recursive energized tiles: " + sum + " in " +
//                        Duration.between(start, end).toNanos() + " ns");
//            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void part2(Path path) {
        try (var stream = Files.lines(path)) {
            // Part 2
            System.out.println("Part 2: Start!");
            var grid = Grid.parse(stream);
            var beam = Beam.create(grid);
//            System.out.println(grid);
            IntStream.range(0, 5).forEach(it -> {
                var start = Instant.now();
                var sum = beam.maxEnergized();
                var end = Instant.now();
                System.out.println("Total number of energized tiles: " + sum + " in " +
                        Duration.between(start, end).toNanos() + " ns");
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
