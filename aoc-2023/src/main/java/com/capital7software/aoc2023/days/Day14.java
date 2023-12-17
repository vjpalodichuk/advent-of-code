package com.capital7software.aoc2023.days;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Day14 {

    private enum Rock {
        ROUNDED_ROCK('O'),
        CUBE_ROCK('#'),
        EMPTY('.');

        private final char label;

        Rock(char label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return String.valueOf(label);
        }

        public char getLabel() {
            return label;
        }

        public static Rock from(char label) {
            for (var value : values()) {
                if (value.getLabel() == label) {
                    return value;
                }
            }

            return null;
        }
    }

    private static class Tile {
        private final Rock rock;
        private int column;
        private int row;

        public Tile(Rock rock, int column, int row) {
            this.rock = rock;
            this.column = column;
            this.row = row;
        }

        public Rock getRock() {
            return rock;
        }

        public int getColumn() {
            return column;
        }

        public void setColumn(int column) {
            this.column = column;
        }

        public int getRow() {
            return row;
        }

        public void setRow(int row) {
            this.row = row;
        }

        @Override
        public String toString() {
            return rock.toString();
        }

        public static Tile from(char label) {
            return from(label, -1, -1);
        }

        public static Tile from(char label, int column, int row) {
            return new Tile(Rock.from(label), column, row);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Tile tile)) return false;
            return getColumn() == tile.getColumn() && getRow() == tile.getRow() && getRock() == tile.getRock();
        }

        @Override
        public int hashCode() {
            return Objects.hash(getRock(), getColumn(), getRow());
        }
    }

    private enum Direction {
        NORTH(-1),
        EAST(1),
        SOUTH(1),
        WEST(-1);

        private final int offset;

        Direction(int offset) {
            this.offset = offset;
        }

        public int getOffset() {
            return offset;
        }
    }

    private record Platform(List<Tile> tiles, int columns, int rows) {

        public static Platform parse(Stream<String> stream) {
            var tiles = new ArrayList<Tile>();

            var columns = new AtomicInteger(0);
            var rows = new AtomicInteger(0);

            var first = new AtomicBoolean(true);

            stream.forEach(line -> {
                if (first.get()) {
                    columns.set(line.length());
                    first.set(false);
                }

                if (parseLine(line, tiles, rows.get())) {
                    rows.incrementAndGet();
                }
            });

            return new Platform(tiles, columns.get(), rows.get());
        }

        private static boolean parseLine(String line, ArrayList<Tile> tiles, int row) {
            if (line == null || line.isBlank()) {
                return false;
            }

            var chars = line.toCharArray();
            for (int column = 0; column < chars.length; column++) {
                tiles.add(Tile.from(chars[column], column, row));
            }
            return true;
        }

        @Override
        public String toString() {
            var builder = new StringBuilder();
            builder.append("Platform {");
            builder.append("\n\tColumns: ");
            builder.append(columns());
            builder.append("\n\tRows: ");
            builder.append(rows());
            builder.append("\n\tCurrent tile positions:");

            for (int i = 0; i < rows(); i++) {
                builder.append("\n\t\t");
                for (int j = 0; j < columns(); j++) {
                    builder.append(tiles.get(j + (i * columns())));
                }
                if (i + 1 < rows()) {
                    builder.append("\n");
                }
            }
            builder.append("\n}");

            return builder.toString();
        }

        public void tilt(Direction direction, boolean print) {
            var offset = direction.getOffset();
            var loopOffset = getLoopOffset(direction);
            var extractor = getExtractor(direction);
            var setter = getSetter(direction);
            var predicate = getLoopPredicate(direction);
            var loopStart = getLoopStart(direction);
            if (print) {
                System.out.println("Tilting platform " + direction + "...");
            }

            // Go through each row / column depending on the direction
            for (var i = 0; i < getRowOrColumnLength(direction); i++) {
                var items = getRowOrColumn(direction, i);
                // Go through each item in the row / column and attempt to move them!
                moveRockTiles(loopStart, predicate, offset, loopOffset, extractor, setter, items);
            }

            if (print) {
                System.out.println("New layout");
                System.out.println(this);
            }
        }

        private void moveRockTiles(
                Integer loopStart,
                Predicate<Integer> predicate,
                int offset,
                int loopOffset,
                Function<Tile, Integer> extractor,
                BiConsumer<Tile, Integer> setter,
                List<Tile> items
        ) {
            // To do this efficiently in a single pass per row / column, we have to keep
            // track of the previous space count.
            // For example, if the row looked like this:
            // . . O . . . O O . O . . . . O . #
            // The first rounded rock would have a value of 2, the next would have a value of 5, then 5, then 6,
            // then 10; the last rock is a Cube and cannot be moved.
            // This allows us to make a single pass through the list and update our grid accordingly!
            var currentSpaceCount = 0;
            var previousSpaceCount = 0;

            // If the currentRock is a Space just increment space count and move to the next rock!
            // If it is a rounded rock, we will add the currentSpaceCount to the previousSpaceCount
            // and then add the rounded rock to the map and reset the current space count.
            // If it is a cube rock, we simply reset the space counts
            for (var i = loopStart; predicate.test(i); i += loopOffset) {
                var tile = items.get(i);
                if (tile.getRock() == Rock.EMPTY) {
                    currentSpaceCount++;
                } else if (tile.getRock() == Rock.ROUNDED_ROCK) {
                    previousSpaceCount += currentSpaceCount;
                    if (previousSpaceCount > 0) {
                        // Only move it if it actually moved!!
                        moveRockTile(tile, extractor, setter, offset, previousSpaceCount);
                    }
                    currentSpaceCount = 0;
                } else {
                    currentSpaceCount = 0;
                    previousSpaceCount = 0;
                }
            }

        }

        private void moveRockTile(
                Tile tile,
                Function<Tile, Integer> extractor,
                BiConsumer<Tile, Integer> setter,
                Integer offset,
                Integer movedBy
        ) {
            // We first move the rock to its new spot then we put an empty rock in its old spot.
            int oldColumn = tile.getColumn();
            int oldRow = tile.getRow();
            int newPos = extractor.apply(tile) + (offset * movedBy);
            // Update it's values
            setter.accept(tile, newPos);

            tiles.set(tile.getColumn() + (tile.getRow() * rows), tile);
            tiles.set(oldColumn + (oldRow * rows), new Tile(Rock.EMPTY, oldColumn, oldRow));
        }

        public BiConsumer<Tile, Integer> getSetter(Direction direction) {
            return switch (direction) {
                case NORTH, SOUTH -> Tile::setRow;
                case EAST, WEST -> Tile::setColumn;
            };
        }

        public Function<Tile, Integer> getExtractor(Direction direction) {
            return switch (direction) {
                case NORTH, SOUTH -> Tile::getRow;
                case EAST, WEST -> Tile::getColumn;
            };
        }

        public Predicate<Integer> getLoopPredicate(Direction direction) {
            return switch (direction) {
                case NORTH, WEST -> current -> current < getRowOrColumnLength(direction);
                case EAST, SOUTH -> current -> current >= 0;
            };
        }

        public Integer getLoopStart(Direction direction) {
            return switch (direction) {
                case NORTH, WEST -> 0;
                case EAST, SOUTH -> getRowOrColumnLength(direction) - 1;
            };
        }

        public Integer getLoopOffset(Direction direction) {
            return switch (direction) {
                case NORTH, WEST -> 1;
                case EAST, SOUTH -> -1;
            };
        }

        public Function<Tile, Integer> getLoadCalculator(Direction direction) {
            switch (direction) {
                case NORTH -> {
                    return (Tile tile) -> getRowOrColumnLength(direction) - tile.getRow();
                }
                case SOUTH -> {
                    return (Tile tile) -> 1 + tile.getRow();
                }
                case EAST -> {
                    return (Tile tile) -> 1 + tile.getColumn();
                }
                case WEST -> {
                    return (Tile tile) -> getRowOrColumnLength(direction) - tile.getColumn();
                }
                default -> throw new RuntimeException("Unknown direction: " + direction);
            }
        }

        public long calculateLoad(Direction direction, boolean print) {
            long supportLoad;
            var loadCalculator = getLoadCalculator(direction);

            if (print) {
                System.out.println("Calculating the load on the " + direction + " support beams...");
            }

            supportLoad = tiles.stream()
                    .filter(it -> it.getRock() == Rock.ROUNDED_ROCK)
                    .mapToLong(it -> (long)loadCalculator.apply(it))
                    .sum();

            if (print) {
                System.out.println("Calculated load: ");
                System.out.println(supportLoad);
            }

            return supportLoad;
        }

        /**
         * Gets a row or column as a List that then can be easily compared
         */
        private List<Tile> getRowOrColumn(Direction direction, int index) {
            if (direction == Direction.NORTH || direction == Direction.SOUTH) {
                if (index < 0 || index >= columns) {
                    throw new IndexOutOfBoundsException("Column index out of range: " + index);
                }

                var list = new ArrayList<Tile>(rows);
                for (int i = 0; i < rows; i++) {
                    list.add(tiles.get(index + (i * columns)));
                }
                return list;
            } else if (direction == Direction.EAST || direction == Direction.WEST) {
                if (index < 0 || index >= rows) {
                    throw new IndexOutOfBoundsException("Row index out of range: " + index);
                }

                var list = new ArrayList<Tile>(columns);
                for (int i = 0; i < columns; i++) {
                    list.add(tiles.get(i + (index * columns)));
                }
                return list;
            } else {
                throw new RuntimeException("Unknown direction specified: " + direction);
            }
        }

        private int getRowOrColumnLength(Direction direction) {
            return switch (direction) {
                case NORTH, SOUTH -> columns;
                case EAST, WEST -> rows;
            };
        }

        /**
         * Spins the platform by tilting it NORTH, WEST, SOUTH, EAST up to the requested number of times.
         * After this method returns, the platform will be in a state as if it was spun the requested
         * number of times.
         *
         * @param cyclesToPerform The requested number of times to cycle the platform.
         * @param print If true, prints some output to the console.
         */
        public void spinCycle(long cyclesToPerform, boolean print) {
            if (print) {
                System.out.println("Performing " + cyclesToPerform + " spin cycle(s)...");
            }
            var gridCache = new ArrayList<List<Tile>>();
            var detectedCycleIndex = 0L;
            var currentTiles = copyTiles();
            // Add the current state of the platform
            gridCache.add(currentTiles);

            var cycleDetected = false;
            var cycleCount = 0;
            while (!cycleDetected && cycleCount < cyclesToPerform) {
                currentTiles = cycle();
                cycleCount++;
                if (print) {
                    System.out.println("After " + cycleCount + " spin cyclesToPerform the layout is:");
                    System.out.println(this);
                }
                var index = gridCache.indexOf(currentTiles);

                if (index != -1) {
                    detectedCycleIndex = index;
                    cycleDetected = true;
                } else {
                    gridCache.add(currentTiles);
                }
            }
            long calculatedIndex = cyclesToPerform;

            if (cycleDetected && calculatedIndex >= detectedCycleIndex) {
                // The end state has already been cached so calculate the index to it.
                // The index can be found using this formula:
                // index = indexOfFirstInstanceOfDuplicate +
                // ((cyclesToPerform - indexOfFirstInstanceOfDuplicate) %
                // (actualCyclesPerformed - indexOfFirstInstanceOfDuplicate))
                calculatedIndex = detectedCycleIndex +
                        ((cyclesToPerform - detectedCycleIndex) % (cycleCount - detectedCycleIndex));
                if (print) {
                    System.out.println("Duplicate Cycle detected after " + cycleCount + " cycle(s) and the spin cycle has been stopped!");
                    System.out.println("The duplicate Cycle was found at iteration " + detectedCycleIndex + "!");
                    System.out.println("The platform is in the state after " + calculatedIndex + " cycle(s) have been performed!");
                    System.out.println("This is a state equal if all " + cyclesToPerform + " cycle(s) had been performed!");
                }
                tiles.clear();
                tiles.addAll(gridCache.get((int) calculatedIndex));
//                gridCache.forEach(List::clear);
//                gridCache.clear();
            }

            if (print) {
                System.out.println("Performed " + cycleCount + " spin cycle(s)");
            }
        }

        private List<Tile> cycle() {
            var print = false;
            tilt(Direction.NORTH, print);
            tilt(Direction.WEST, print);
            tilt(Direction.SOUTH, print);
            tilt(Direction.EAST, print);

            return copyTiles();
        }

        private List<Tile> copyTiles() {
            return tiles.stream().map(it -> new Tile(it.getRock(), it.getColumn(), it.getRow())).toList();
        }
    }

    private static final String inputFilename = "inputs/input_day_14-01.txt";

    public static void main(String[] args) throws URISyntaxException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        var url = classloader.getResource(inputFilename);
        assert url != null;
        var path = Paths.get(url.toURI());
        var print = false;
        var loopCount = 5;
        for (int i = 0; i < loopCount; i++) {
            part1(path, print);
            part2(path, print);
        }
    }

    private static void part1(Path path, boolean print) {
        try (var stream = Files.lines(path)) {
            // Part 1
            System.out.println("Part 1: Start!");
            var platform = loadPlatform(stream, print);
            var start = Instant.now();
//            System.out.println("Tilting platform North...");
            platform.tilt(Direction.NORTH, print);
//            System.out.println("Calculating load on the North support beams...");
            var totalLoad = platform.calculateLoad(Direction.NORTH, print);
            var end = Instant.now();
            System.out.println("Total load on the North support beams is: " + totalLoad + " in " +
                    Duration.between(start, end).toNanos() + " ns");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void part2(Path path, boolean print) {
        try (var stream = Files.lines(path)) {
            // Part 2
            System.out.println("Part 2: Start!");
            var platform = loadPlatform(stream, print);
            var spinCount = 1_000_000_000;
            var start = Instant.now();
//            System.out.println("Performing " + spinCount + " spin cycle(s)...");
            platform.spinCycle(spinCount, print);
//            System.out.println("Calculating load on the North support beams...");
            var totalLoad = platform.calculateLoad(Direction.NORTH, print);
            var end = Instant.now();
            System.out.println("Total load on the North support beams is: " + totalLoad + " in " +
                    Duration.between(start, end).toNanos() + " ns");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Platform loadPlatform(Stream<String> stream, boolean print) {
        var platform = Platform.parse(stream);

        if (print) {
            System.out.println("Platform contains " + platform.tiles().size() + " tiles!");
            System.out.println(platform);
        }
        return platform;
    }
}
