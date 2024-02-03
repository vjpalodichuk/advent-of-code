package com.capital7software.aoc.aoc2023aoc.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.grid.PipeMaze;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * --- Day 10: Pipe Maze ---<br><br>
 * You use the hang glider to ride the hot air from Desert Island all the way up to the floating
 * metal island. This island is surprisingly cold and there definitely aren't any thermals to
 * glide on, so you leave your hang glider behind.
 *
 * <p><br>
 * You wander around for a while, but you don't find any people or animals. However, you
 * do occasionally find signposts labeled "Hot Springs" pointing in a seemingly consistent
 * direction; maybe you can find someone at the hot springs and ask them where the desert-machine
 * parts are made.
 *
 * <p><br>
 * The landscape here is alien; even the flowers and trees are made of metal. As you stop to
 * admire some metal grass, you notice something metallic scurry away in your peripheral vision
 * and jump into a big pipe! It didn't look like any animal you've ever seen; if you want a
 * better look, you'll need to get ahead of it.
 *
 * <p><br>
 * Scanning the area, you discover that the entire field you're standing on is densely packed
 * with pipes; it was hard to tell at first because they're the same metallic silver color as
 * the "ground". You make a quick sketch of all the surface pipes you can see (your puzzle input).
 *
 * <p><br>
 * The pipes are arranged in a two-dimensional grid of tiles:
 *
 * <p><br>
 * <code>
 * | is a vertical pipe connecting north and south.<br>
 * - is a horizontal pipe connecting east and west.<br>
 * L is a 90-degree bend connecting north and east.<br>
 * J is a 90-degree bend connecting north and west.<br>
 * 7 is a 90-degree bend connecting south and west.<br>
 * F is a 90-degree bend connecting south and east.<br>
 * . is ground; there is no pipe in this tile.<br>
 * S is the starting position of the animal; there is a pipe on this tile, but your sketch doesn't
 * show what shape the pipe has.<br><br>
 * </code>
 * Based on the acoustics of the animal's scurrying, you're confident the pipe that contains
 * the animal is one large, continuous loop.
 *
 * <p><br>
 * For example, here is a square loop of pipe:
 *
 * <p><br>
 * <code>
 * .....<br>
 * .F-7.<br>
 * .|.|.<br>
 * .L-J.<br>
 * .....<br>
 * </code>
 *
 * <p><br>
 * If the animal had entered this loop in the northwest corner, the sketch would instead look
 * like this:
 *
 * <p><br>
 * <code>
 * .....<br>
 * .S-7.<br>
 * .|.|.<br>
 * .L-J.<br>
 * .....<br>
 * </code>
 *
 * <p><br>
 * In the above diagram, the S tile is still a 90-degree F bend: you can tell because of how
 * the adjacent pipes connect to it.
 *
 * <p><br>
 * Unfortunately, there are also many pipes that aren't connected to the loop! This sketch
 * shows the same loop as above:
 *
 * <p><br>
 * <code>
 * -L|F7<br>
 * 7S-7|<br>
 * L|7||<br>
 * -L-J|<br>
 * L|-JF<br>
 * </code>
 *
 * <p><br>
 * In the above diagram, you can still figure out which pipes form the main loop: they're the
 * ones connected to S, pipes those pipes connect to, pipes those pipes connect to, and so on.
 * Every pipe in the main loop connects to its two neighbors (including S, which will have
 * exactly two pipes connecting to it, and which is assumed to connect back to those two pipes).
 *
 * <p><br>
 * Here is a sketch that contains a slightly more complex main loop:
 *
 * <p><br>
 * <code>
 * ..F7.<br>
 * .FJ|.<br>
 * SJ.L7<br>
 * |F--J<br>
 * LJ...<br>
 * </code>
 *
 * <p><br>
 * Here's the same example sketch with the extra, non-main-loop pipe tiles also shown:
 *
 * <p><br>
 * <code>
 * 7-F7-<br>
 * .FJ|7<br>
 * SJLL7<br>
 * |F--J<br>
 * LJ.LJ<br>
 * </code>
 *
 * <p><br>
 * If you want to get out ahead of the animal, you should find the tile in the loop that is
 * farthest from the starting position. Because the animal is in the pipe, it doesn't make
 * sense to measure this by direct distance. Instead, you need to find the tile that would take
 * the longest number of steps along the loop to reach from the starting point - regardless of
 * which way around the loop the animal went.
 *
 * <p><br>
 * In the first example with the square loop:
 *
 * <p><br>
 * <code>
 * .....<br>
 * .S-7.<br>
 * .|.|.<br>
 * .L-J.<br>
 * .....<br>
 * </code>
 *
 * <p><br>
 * You can count the distance each tile in the loop is from the starting point like this:
 *
 * <p><br>
 * <code>
 * .....<br>
 * .012.<br>
 * .1.3.<br>
 * .234.<br>
 * .....<br>
 * </code>
 *
 * <p><br>
 * In this example, the farthest point from the start is 4 steps away.
 *
 * <p><br>
 * Here's the more complex loop again:
 *
 * <p><br>
 * <code>
 * ..F7.<br>
 * .FJ|.<br>
 * SJ.L7<br>
 * |F--J<br>
 * LJ...<br>
 * </code>
 *
 * <p><br>
 * Here are the distances for each tile on that loop:
 *
 * <p><br>
 * <code>
 * ..45.<br>
 * .236.<br>
 * 01.78<br>
 * 14567<br>
 * 23...<br>
 * </code>
 *
 * <p><br>
 * Find the single giant loop starting at S. How many steps along the loop does it take to get
 * from the starting position to the point farthest from the starting position?
 *
 * <p><br>
 * Your puzzle answer was 6842.
 *
 * <p><br>
 * --- Part Two ---<br><br>
 * You quickly reach the farthest point of the loop, but the animal never emerges. Maybe its
 * nest is within the area enclosed by the loop?
 *
 * <p><br>
 * To determine whether it's even worth taking the time to search for such a nest, you should
 * calculate how many tiles are contained within the loop. For example:
 *
 * <p><br>
 * <code>
 * ...........<br>
 * .S-------7.<br>
 * .|F-----7|.<br>
 * .||.....||.<br>
 * .||.....||.<br>
 * .|L-7.F-J|.<br>
 * .|..|.|..|.<br>
 * .L--J.L--J.<br>
 * ...........<br>
 * </code>
 *
 * <p><br>
 * The above loop encloses merely four tiles - the two pairs of . in the southwest and southeast
 * (marked I below). The middle . tiles (marked O below) are not in the loop. Here is the same loop
 * again with those regions marked:
 *
 * <p><br>
 * <code>
 * ...........<br>
 * .S-------7.<br>
 * .|F-----7|.<br>
 * .||OOOOO||.<br>
 * .||OOOOO||.<br>
 * .|L-7OF-J|.<br>
 * .|II|O|II|.<br>
 * .L--JOL--J.<br>
 * .....O.....<br>
 * </code>
 *
 * <p><br>
 * In fact, there doesn't even need to be a full tile path to the outside for tiles to count
 * as outside the loop - squeezing between pipes is also allowed! Here, I is still within the
 * loop and O is still outside the loop:
 *
 * <p><br>
 * <code>
 * ..........<br>
 * .S------7.<br>
 * .|F----7|.<br>
 * .||OOOO||.<br>
 * .||OOOO||.<br>
 * .|L-7F-J|.<br>
 * .|II||II|.<br>
 * .L--JL--J.<br>
 * ..........<br>
 * </code>
 *
 * <p><br>
 * In both of the above examples, 4 tiles are enclosed by the loop.
 *
 * <p><br>
 * Here's a larger example:
 *
 * <p><br>
 * <code>
 * .F----7F7F7F7F-7....<br>
 * .|F--7||||||||FJ....<br>
 * .||.FJ||||||||L7....<br>
 * FJL7L7LJLJ||LJ.L-7..<br>
 * L--J.L7...LJS7F-7L7.<br>
 * ....F-J..F7FJ|L7L7L7<br>
 * ....L7.F7||L7|.L7L7|<br>
 * .....|FJLJ|FJ|F7|.LJ<br>
 * ....FJL-7.||.||||...<br>
 * ....L---J.LJ.LJLJ...<br>
 * </code>
 *
 * <p><br>
 * The above sketch has many random bits of ground, some of which are in the loop (I) and some of
 * which are outside it (O):
 *
 * <p><br>
 * <code>
 * OF----7F7F7F7F-7OOOO<br>
 * O|F--7||||||||FJOOOO<br>
 * O||OFJ||||||||L7OOOO<br>
 * FJL7L7LJLJ||LJIL-7OO<br>
 * L--JOL7IIILJS7F-7L7O<br>
 * OOOOF-JIIF7FJ|L7L7L7<br>
 * OOOOL7IF7||L7|IL7L7|<br>
 * OOOOO|FJLJ|FJ|F7|OLJ<br>
 * OOOOFJL-7O||O||||OOO<br>
 * OOOOL---JOLJOLJLJOOO<br>
 * </code>
 *
 * <p><br>
 * In this larger example, 8 tiles are enclosed by the loop.
 *
 * <p><br>
 * Any tile that isn't part of the main loop can count as being enclosed by the loop.
 * Here's another example with many bits of junk pipe lying around that aren't connected to
 * the main loop at all:
 *
 * <p><br>
 * <code>
 * FF7FSF7F7F7F7F7F---7<br>
 * L|LJ||||||||||||F--J<br>
 * FL-7LJLJ||||||LJL-77<br>
 * F--JF--7||LJLJ7F7FJ-<br>
 * L---JF-JLJ.||-FJLJJ7<br>
 * |F|F-JF---7F7-L7L|7|<br>
 * |FFJF7L7F-JF7|JL---7<br>
 * 7-L-JL7||F7|L7F-7F7|<br>
 * L.L7LFJ|||||FJL7||LJ<br>
 * L7JLJL-JLJLJL--JLJ.L<br>
 * </code>
 *
 * <p><br>
 * Here are just the tiles that are enclosed by the loop marked with I:
 *
 * <p><br>
 * <code>
 * FF7FSF7F7F7F7F7F---7<br>
 * L|LJ||||||||||||F--J<br>
 * FL-7LJLJ||||||LJL-77<br>
 * F--JF--7||LJLJIF7FJ-<br>
 * L---JF-JLJIIIIFJLJJ7<br>
 * |F|F-JF---7IIIL7L|7|<br>
 * |FFJF7L7F-JF7IIL---7<br>
 * 7-L-JL7||F7|L7F-7F7|<br>
 * L.L7LFJ|||||FJL7||LJ<br>
 * L7JLJL-JLJLJL--JLJ.L<br>
 * </code>
 *
 * <p><br>
 * In this last example, 10 tiles are enclosed by the loop.
 *
 * <p><br>
 * Figure out whether you have time to search for the nest by calculating the area within the loop.
 * How many tiles are enclosed by the loop?
 *
 * <p><br>
 * Your puzzle answer was 393.
 */
public class Day10 implements AdventOfCodeSolution {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day10.class);

  /**
   * Instantiates this Solution instance.
   */
  public Day10() {

  }

  @Override
  public String getDefaultInputFilename() {
    return "inputs/input_day_10-01.txt";
  }

  @Override
  public void runPart1(List<String> input) {
    var start = Instant.now();
    var answer = calculateMaximumDistance(input, false);
    var end = Instant.now();

    LOGGER.info(
        "Maximum steps to the furthest point from the starting point using DFS: {}", answer)
    ;
    logTimings(LOGGER, start, end);

    start = Instant.now();
    answer = calculateMaximumDistance(input, true);
    end = Instant.now();

    LOGGER.info(
        "Maximum steps to the furthest point from the starting point using BFS: {}", answer
    );
    logTimings(LOGGER, start, end);
  }

  @Override
  public void runPart2(List<String> input) {
    var start = Instant.now();
    var answer = countTilesEnclosedInMainLoop(input, false);
    var end = Instant.now();

    LOGGER.info("Tiles enclosed in the loop: {} using DFS", answer);
    logTimings(LOGGER, start, end);

    start = Instant.now();
    answer = countTilesEnclosedInMainLoop(input, true);
    end = Instant.now();

    LOGGER.info("Tiles enclosed in the loop: {} using BFS", answer);
    logTimings(LOGGER, start, end);
  }

  /**
   * Calculates and returns the maximum steps to reach the further point from the starting tile.
   *
   * @param input The List of Strings to parse into a PipeMaze.
   * @param bfs   If true, then a Breadth First Search is performed. If false, a recursive Depth
   *              First Search is performed.
   * @return The maximum steps to reach the further point from the starting tile.
   */
  public long calculateMaximumDistance(List<String> input, boolean bfs) {
    var maze = PipeMaze.buildPipeMaze(input);
    return maze.calculateDistances(bfs).values().stream().mapToInt(it -> it).max().orElse(0);
  }

  /**
   * Calculates and returns the number of tiles that are enclosed by the main loop of the PipeMaze.
   * The main loop is the cycle that starts and stops at the starting tile.
   *
   * @param input The List of Strings to parse into a PipeMaze.
   * @param bfs   If true, then a Breadth First Search is performed. If false, a recursive Depth
   *              First Search is performed.
   * @return The number of tiles that are enclosed by the main loop of the PipeMaze.
   */
  public long countTilesEnclosedInMainLoop(List<String> input, boolean bfs) {
    var maze = PipeMaze.buildPipeMaze(input);
    var distances = maze.calculateDistances(bfs);
    return maze.calculateTilesEnclosedInLoop(distances);
  }
}
