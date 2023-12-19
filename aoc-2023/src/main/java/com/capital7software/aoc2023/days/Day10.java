package com.capital7software.aoc2023.days;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public class Day10 {
    public record Range(int low, int high) {

        public int difference() {
            return high - low;
        }

        public boolean contains(int number) {
            return (number >= low && number < high);
        }

        public int count() {
            return difference() + 1;
        }

        public static Range of(int min, int high) {
            return new Range(min, high);
        }

        /**
         * Splits this range in two distinct ranges based on the splitPoint.
         * May produce an empty range if splitPoint is outside of this range.
         *
         * @param splitPoint The number to split the range on.
         * @return A list with two new ranges.
         */
        public List<Range> split(int splitPoint) {
            var result = new LinkedList<Range>();

            result.add(Range.of(low, Math.min(splitPoint - 1, high)));
            result.add(Range.of(Math.max(low, splitPoint), high));
            return result;
        }

        public boolean isEmpty() {
            return low > high;
        }
    }

    private enum Direction {
        NORTH(),
        EAST(),
        SOUTH(),
        WEST()
    }

    private enum Tile {
        VERTICAL('|', new Direction[]{Direction.NORTH, Direction.SOUTH}) {
            @Override
            public boolean connectsNorth() {
                return true;
            }

            @Override
            public boolean connectsSouth() {
                return true;
            }
        },
        HORIZONTAL('-', new Direction[]{Direction.EAST, Direction.WEST}) {
            @Override
            public boolean connectsEast() {
                return true;
            }

            @Override
            public boolean connectsWest() {
                return true;
            }
        },
        NORTH_EAST('L', new Direction[]{Direction.NORTH, Direction.EAST}) {
            @Override
            public boolean connectsNorth() {
                return true;
            }

            @Override
            public boolean connectsEast() {
                return true;
            }
        },
        NORTH_WEST('J', new Direction[]{Direction.NORTH, Direction.WEST}) {
            @Override
            public boolean connectsNorth() {
                return true;
            }

            @Override
            public boolean connectsWest() {
                return true;
            }
        },
        SOUTH_WEST('7', new Direction[]{Direction.SOUTH, Direction.WEST}) {
            @Override
            public boolean connectsSouth() {
                return true;
            }

            @Override
            public boolean connectsWest() {
                return true;
            }
        },
        SOUTH_EAST('F', new Direction[]{Direction.SOUTH, Direction.EAST}) {
            @Override
            public boolean connectsEast() {
                return true;
            }

            @Override
            public boolean connectsSouth() {
                return true;
            }
        },
        GROUND('.', null),
        START('S', null);

        private final char label;
        private final Set<Direction> directions;

        Tile(char label, Direction[] directions) {
            this.label = label;
            if (directions != null) {
                this.directions = new HashSet<>(directions.length);
                this.directions.addAll(Arrays.asList(directions));
            } else {
                this.directions = new HashSet<>();
            }
        }

        public char getLabel() {
            return label;
        }

        public Set<Direction> getDirections() {
            return directions;
        }

        boolean isStart() {
            return this == Tile.START;
        }

        @Override
        public String toString() {
            return String.valueOf(label);
        }

        public static Tile fromLabel(char label) {
            for (var value : values()) {
                if (label == value.getLabel()) {
                    return value;
                }
            }

            return null;
        }

        public static Tile fromDirections(HashSet<Direction> directions) {
            for (var value : values()) {
                if (directions.containsAll(value.getDirections())) {
                    return value;
                }
            }

            return null;
        }

        public boolean connectsNorth() {
            return false;
        }

        public boolean connectsEast() {
            return false;
        }

        public boolean connectsSouth() {
            return false;
        }

        public boolean connectsWest() {
            return false;
        }
    }

    private static class MazeTile implements Comparable<MazeTile> {
        private final Tile tile;
        private final int x;
        private final int y;

        public MazeTile(Tile tile, int x, int y) {
            this.tile = tile;
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public boolean isStartTile() {
            return tile.isStart();
        }

        public boolean connectsNorth() {
            return tile.connectsNorth();
        }

        public boolean connectsEast() {
            return tile.connectsEast();
        }

        public boolean connectsSouth() {
            return tile.connectsSouth();
        }

        public boolean connectsWest() {
            return tile.connectsWest();
        }

        public boolean connectsNorthTo(MazeTile other) {
            return connectsNorth() && other.connectsSouth() && y == other.y + 1 && x == other.x;
        }

        public boolean connectsEastTo(MazeTile other) {
            return connectsEast() && other.connectsWest() && y == other.y && x == other.x - 1;
        }

        public boolean connectsSouthTo(MazeTile other) {
            return connectsSouth() && other.connectsNorth() && y == other.y - 1 && x == other.x;
        }

        public boolean connectsWestTo(MazeTile other) {
            return connectsWest() && other.connectsEast() && y == other.y && x == other.x + 1;
        }

        public List<MazeTile> getNeighbors(List<List<MazeTile>> maze) {
            var neighbors = new ArrayList<MazeTile>();

            if (y > 0 && connectsNorthTo(maze.get(y - 1).get(x))) {
                neighbors.add(maze.get(y - 1).get(x));
            }
            if (y < maze.size() - 1 && connectsSouthTo(maze.get(y + 1).get(x))) {
                neighbors.add(maze.get(y + 1).get(x));
            }
            if (x > 0 && connectsWestTo(maze.get(y).get(x - 1))) {
                neighbors.add(maze.get(y).get(x - 1));
            }
            if (x < maze.get(y).size() - 1 && connectsEastTo(maze.get(y).get(x + 1))) {
                neighbors.add(maze.get(y).get(x + 1));
            }

            return neighbors;
        }

        @Override
        public String toString() {
            return "MazeTile{" +
                    "tile=" + tile +
                    ", x=" + x +
                    ", y=" + y +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MazeTile mazeTile)) return false;
            return getX() == mazeTile.getX() && getY() == mazeTile.getY();
        }

        @Override
        public int hashCode() {
            return Objects.hash(getX(), getY());
        }

        @Override
        public int compareTo(MazeTile o) {
            if (y != o.y) {
                return Integer.compare(y, o.y);
            } else {
                return Integer.compare(x, o.x);
            }
        }

        public Tile getTile() {
            return tile;
        }
    }

    private static class StartMazeTile extends MazeTile {
        public StartMazeTile(Tile tile, int x, int y) {
            super(tile, x, y);
        }

        @Override
        public boolean isStartTile() {
            return true;
        }

        public static StartMazeTile fromDirections(HashSet<Direction> directions, int x, int y) {
            return new StartMazeTile(Tile.fromDirections(directions), x, y);
        }

    }

    private static final String inputFilename = "inputs/input_day_10-01.txt";

    public static void main(String[] args) throws URISyntaxException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        var url = classloader.getResource(inputFilename);
        assert url != null;
        var path = Paths.get(url.toURI());
        var maze = new ArrayList<List<MazeTile>>();
        try (var stream = Files.lines(path)) {
            System.out.println("Loading pipe maze...");
            loadMaze(stream, maze);
            System.out.println("Done loading pipe maze!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Part 1
        System.out.println("Part 1: Start!");
        System.out.println("Finding the starting tile");
        final var startTile = findStartingTile(maze);
        System.out.println("Starting tile found at: " + startTile);
        System.out.println("Determining starting tile's pipe...");
        final var startPipeTile = determineStartingTileType(startTile, maze);
        System.out.println("Is starting tile: " + startPipeTile.isStartTile());
        System.out.println("Starting tile is: " + startPipeTile);
        System.out.println("Updating maze with updated starting tile...");
        maze.get(startPipeTile.getY()).set(startPipeTile.getX(), startPipeTile);
        System.out.println("Maze has been updated.");
        System.out.println(("Calculating distances using BFS..."));
        final var bfsStart = Instant.now();
        final var distances = calculateDistancesBFS(startPipeTile, maze);
        final var bfsStop = Instant.now();
        System.out.println("Maximum distance: " +
                distances.values().stream().mapToInt(it -> it).max().orElse(0) + " in " +
                Duration.between(bfsStart, bfsStop).toMillis() + " ms");

//        System.out.println(("Calculating distances using DFS..."));
//        final var dfsDistances = new HashMap<MazeTile, Integer>();
//        final var dfsStart = Instant.now();
//        calculateDistancesDFS(startPipeTile, 0, dfsDistances, maze);
//        final var dfsStop = Instant.now();
//        System.out.println("Maximum distance: " +
//                (distances.values().stream().mapToInt(it -> it).max().orElse(0) / 2) + " in " +
//                Duration.between(bfsStart, bfsStop).toMillis() + " ms");

        // Part 2
        System.out.println("Part 2: Start!");
        System.out.println(("Calculating the number of tiles inside the polygon formed by the loop..."));
        final var polyStart = Instant.now();
        final var tilesInLoop = calculateTilesInLoop(distances, maze);
        final var polyStop = Instant.now();
        System.out.println("Tiles enclosed in the loop: " + tilesInLoop + " in " +
                Duration.between(polyStart, polyStop).toMillis() + " ms");
    }

    private static long calculateTilesInLoop(Map<MazeTile, Integer> mainLoop, ArrayList<List<MazeTile>> maze) {
        final var directions = Set.of(Tile.VERTICAL, Tile.NORTH_EAST, Tile.NORTH_WEST);
        final var mainCount = new AtomicLong(0);

        maze.forEach(row -> {
            long count = row.stream()
                    .filter(it -> !mainLoop.containsKey(it)) // Only need to check what isn't in the loop!.
                    .filter(tile -> {
                        // We use ray casting to determine if a point is inside or outside the polygon formed by
                        // the main loop. We start at the top and cast a ray in the WEST direction and then count
                        // how many points on the polygon it hits.
                        // Since we are casting from the top we only have to check VERTICAL, NORTH_EAST, and NORTH_WEST
                        // directions.
                        // Using the Jordan Curve Theorem, an even number of crossings means the point is outside of
                        // the polygon!
                        var xRange = Range.of(0, tile.getX());
                        return (mainLoop.keySet()
                                .stream()
                                .filter(it -> xRange.contains(it.getX()) && tile.getY() == it.getY())
                                .filter(it -> directions.contains(maze.get(it.getY()).get(it.getX()).getTile()))
                                .count() % 2) == 1;
                    })
                    .count();
            mainCount.addAndGet(count);
        });

        return mainCount.get();
    }

    private static MazeTile determineStartingTileType(MazeTile startTile, List<List<MazeTile>> maze) {
        var directions = new HashSet<Direction>();
        var startX = startTile.getX();
        var startY = startTile.getY();

        // Check north, west, east, and south of the start tile to find two tiles that connect to the starting tile
        if (startY > 0 && maze.get(startY - 1).get(startX).connectsSouth()) {
            directions.add(Direction.NORTH);
        }
        if (startX > 0 && maze.get(startY).get(startX - 1).connectsEast()) {
            directions.add(Direction.WEST);
        }
        if (startY < maze.size() - 1 && maze.get(startY + 1).get(startX).connectsNorth()) {
            directions.add(Direction.SOUTH);
        }
        if (startX < maze.get(startY).size() - 1 && maze.get(startY).get(startX + 1).connectsWest()) {
            directions.add(Direction.EAST);
        }

        return StartMazeTile.fromDirections(directions, startX, startY);
    }

    private static MazeTile findStartingTile(List<List<MazeTile>> maze) {
        for (var row : maze) {
            for (var tile : row) {
                if (tile.isStartTile()) {
                    return tile;
                }
            }
        }

        throw new RuntimeException("Unable to find the starting tile!");
    }

    private static void loadMaze(Stream<String> stream, List<List<MazeTile>> maze) {
        var y = new AtomicInteger(0);
        stream.forEach(line -> maze.add(loadMazeRow(line, y.getAndIncrement())));
    }

    private static List<MazeTile> loadMazeRow(String line, int y) {
        var x = new AtomicInteger(0);
        var list = new ArrayList<MazeTile>();
        line.chars()
                .mapToObj(it -> (char) it)
                .map(Tile::fromLabel)
                .forEach(it -> list.add(new MazeTile(it, x.getAndIncrement(), y)));
        return list;
    }

    /**
     * We will use Dijkstra's shortest path algorithm to calculate the distances from the specified starting tile.
     *
     * @param start The tile to start from
     * @param maze  The maze of tiles
     * @return A map of the distances for the tiles in the path from the starting tile.
     */
    private static Map<MazeTile, Integer> calculateDistancesBFS(final MazeTile start, final List<List<MazeTile>> maze) {
        final var queue = new PriorityQueue<MazeTile>();
        final var distances = new HashMap<MazeTile, Integer>();

        // Initialize our queue and distances
        queue.offer(start);
        distances.put(start, 0);

        while (!queue.isEmpty()) {
            final var current = queue.poll();
            final var distance = distances.get(current);
            final var neighbors = current.getNeighbors(maze);

            for (final var neighbor : neighbors) {
                var distanceToNeighbor = distance;
                distanceToNeighbor++;
                if (distanceToNeighbor < distances.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    distances.put(neighbor, distanceToNeighbor);
                    queue.add(neighbor);
                }
            }
        }

        return distances;
    }

    private static void calculateDistancesDFS(
            final MazeTile tile,
            final int level,
            final Map<MazeTile, Integer> distances,
            final List<List<MazeTile>> maze
    ) {
        distances.put(tile, level);

        final var neighbors = tile.getNeighbors(maze);

        for (var neighbor : neighbors) {
            if (!distances.containsKey(neighbor)) {
                calculateDistancesDFS(neighbor, level + 1, distances, maze);
            }
        }
    }
}
