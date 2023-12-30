package com.capital7software.aoc2015.days;

import com.capital7software.aoc2015.lib.AdventOfCodeSolution;
import com.capital7software.aoc2015.lib.Direction;
import com.capital7software.aoc2015.lib.InfiniteGrid;
import com.capital7software.aoc2015.lib.Point2D;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * --- Day 3: Perfectly Spherical Houses in a Vacuum ---
 * Santa is delivering presents to an infinite two-dimensional grid of houses.
 * <p>
 * He begins by delivering a present to the house at his starting location, and then an elf at the North Pole
 * calls him via radio and tells him where to move next. Moves are always exactly one house to the north (^),
 * south (v), east (>), or west (<). After each move, he delivers another present to the house at his new location.
 * <p>
 * However, the elf back at the North Pole has had a little too much eggnog, and so his directions are a little
 * off, and Santa ends up visiting some houses more than once. How many houses receive at least one present?
 * <p>
 * For example:
 * <p>
 * > delivers presents to 2 houses: one at the starting location, and one to the east.
 * ^>v< delivers presents to 4 houses in a square, including twice to the house at his starting/ending location.
 * ^v^v^v^v^v delivers a bunch of presents to some very lucky children at only 2 houses.
 * <p>
 *     Your puzzle answer was 2081.
 * <p>
 * --- Part Two ---
 * The next year, to speed up the process, Santa creates a robot version of himself, Robo-Santa,
 * to deliver presents with him.
 * <p>
 * Santa and Robo-Santa start at the same location (delivering two presents to the same starting house),
 * then take turns moving based on instructions from the elf, who is eggnoggedly reading from the same
 * script as the previous year.
 * <p>
 * This year, how many houses receive at least one present?
 * <p>
 * For example:
 * <p>
 * ^v delivers presents to 3 houses, because Santa goes north, and then Robo-Santa goes south.
 * ^>v< now delivers presents to 3 houses, and Santa and Robo-Santa end up back where they started.
 * ^v^v^v^v^v now delivers presents to 11 houses, with Santa going one direction and Robo-Santa going the other.
 * <p>
 *     Your puzzle answer was 2341
 */
public class Day03 implements AdventOfCodeSolution {
    public record Sleigh(Map<Integer, Map<Point2D<Long>, Long>> deliveredGifts, InfiniteGrid grid) {
        public Sleigh() {
            this(new HashMap<>(), new InfiniteGrid());
        }

        public int deliverGifts(String route) {
            var start = new Point2D<>(0L, 0L);
            var routeId = deliveredGifts().size();
            var routeMap = deliveredGifts.computeIfAbsent(routeId, it -> new HashMap<>());

            routeMap.put(start, 1L);

            var lastHouse = start;

            for (char c : route.toCharArray()) {
                lastHouse = deliverGift(lastHouse, c, routeMap);
            }

            return routeId;
        }

        public int deliverGiftsWithRoboSanta(String route) {
            var start = new Point2D<>(0L, 0L);
            var routeId = deliveredGifts().size();
            var routeMap = deliveredGifts.computeIfAbsent(routeId, it -> new HashMap<>());

            routeMap.put(start, 2L);

            var santaLastHouse = start;
            var roboLastHouse = start;

            int i = 0;
            for (char c : route.toCharArray()) {
                if (i % 2 == 0) {
                    santaLastHouse = deliverGift(santaLastHouse, c, routeMap);
                } else {
                    roboLastHouse = deliverGift(roboLastHouse, c, routeMap);
                }
                i++;
            }

            return routeId;
        }

        private Point2D<Long> deliverGift(Point2D<Long> lastHouse, char direction, Map<Point2D<Long>, Long> routeMap) {
            Point2D<Long> newHouse;
            switch (direction) {
                case '^' -> newHouse = grid.pointInDirection(lastHouse, Direction.NORTH);
                case 'v' -> newHouse = grid.pointInDirection(lastHouse, Direction.SOUTH);
                case '>' -> newHouse = grid.pointInDirection(lastHouse, Direction.EAST);
                case '<' -> newHouse = grid.pointInDirection(lastHouse, Direction.WEST);
                default -> newHouse = lastHouse;
            }

            routeMap.put(newHouse, routeMap.getOrDefault(newHouse, 0L) + 1);
            return newHouse;
        }

        public long getUniqueHouseCount(int routeId) {
            return deliveredGifts.computeIfAbsent(routeId, it -> new HashMap<>()).size();
        }
    }
    private static final String defaultFilename = "inputs/input_day_03-01.txt";

    private final Sleigh sleigh = new Sleigh();

    @Override
    public String getDefaultInputFilename() {
        return defaultFilename;
    }

    @Override
    public void runPart1(List<String> input) {

        for (var route : input) {
            var start = Instant.now();
            int routeId = deliverGifts(route);
            var total = getUniqueHouseCount(routeId);
            var end = Instant.now();

            System.out.printf("Santa delivered at least one gift to %d houses!%n", total);
            printTiming(start, end);
        }
    }

    @Override
    public void runPart2(List<String> input) {

        for (var route : input) {
            var start = Instant.now();
            int routeId = deliverGiftsWithRoboSanta(route);
            var total = getUniqueHouseCount(routeId);
            var end = Instant.now();

            System.out.printf("Santa and Robo Santa delivered at least one gift to %d houses!%n", total);
            printTiming(start, end);
        }
    }

    public int deliverGifts(String route) {
        return sleigh.deliverGifts(route);
    }

    public int deliverGiftsWithRoboSanta(String route) {
        return sleigh.deliverGiftsWithRoboSanta(route);
    }

    public long getUniqueHouseCount(int routeId) {
        return sleigh.getUniqueHouseCount(routeId);
    }
}
