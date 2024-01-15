package com.capital7software.aoc2015.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.grid.ChristmasLights;
import com.capital7software.aoc.lib.util.Pair;

import java.time.Instant;
import java.util.List;

/**
 * --- Day 18: Like a GIF For Your Yard ---<br>
 * After the million lights incident, the fire code has gotten stricter: now, at most ten
 * thousand lights are allowed. You arrange them in a 100x100 grid.
 * <p>
 * Never one to let you down, Santa again mails you instructions on the ideal lighting configuration.
 * With so few lights, he says, you'll have to resort to animation.
 * <p>
 * Start by setting your lights to the included initial configuration (your puzzle input).
 * A # means "on", and a . means "off".
 * <p>
 * Then, animate your grid in steps, where each step decides the next configuration based on the current one.
 * Each light's next state (either on or off) depends on its current state and the current states of
 * the eight lights adjacent to it (including diagonals). Lights on the edge of the grid might have
 * fewer than eight neighbors; the missing ones always count as "off".
 * <p>
 * For capital7software, in a simplified 6x6 grid, the light marked A has the neighbors numbered 1 through 8,
 * and the light marked B, which is on an edge, only has the neighbors marked 1 through 5:
 * <p>
 * 1B5...<br>
 * 234...<br>
 * ......<br>
 * ..123.<br>
 * ..8A4.<br>
 * ..765.<br>
 * The state a light should have next is based on its current state (on or off) plus the
 * number of neighbors that are on:
 * <p>
 * A light which is on stays on when 2 or 3 neighbors are on, and turns off otherwise.
 * A light which is off turns on if exactly 3 neighbors are on, and stays off otherwise.
 * All of the lights update simultaneously; they all consider the same current state before moving to the next.
 * <p>
 * Here's a few steps from an capital7software configuration of another 6x6 grid:
 * <p>
 * Initial state:<br>
 * .#.#.#<br>
 * ...##.<br>
 * #....#<br>
 * ..#...<br>
 * #.#..#<br>
 * ####..<br>
 * <p>
 * After 1 step:<br>
 * ..##..<br>
 * ..##.#<br>
 * ...##.<br>
 * ......<br>
 * #.....<br>
 * #.##..<br>
 * <p>
 * After 2 steps:<br>
 * ..###.<br>
 * ......<br>
 * ..###.<br>
 * ......<br>
 * .#....<br>
 * .#....<br>
 * <p>
 * After 3 steps:<br>
 * ...#..<br>
 * ......<br>
 * ...#..<br>
 * ..##..<br>
 * ......<br>
 * ......<br>
 * <p>
 * After 4 steps:<br>
 * ......<br>
 * ......<br>
 * ..##..<br>
 * ..##..<br>
 * ......<br>
 * ......<br>
 * After 4 steps, this capital7software has four lights on.
 * <p>
 * In your grid of 100x100 lights, given your initial configuration, how many lights are on after 100 steps?
 * <p>
 * Your puzzle answer was 821.
 * <p><br>
 * --- Part Two ---<br>
 * You flip the instructions over; Santa goes on to point out that this is all just an implementation
 * of Conway's Game of Life. At least, it was, until you notice that something's wrong with the grid
 * of lights you bought: four lights, one in each corner, are stuck on and can't be turned off.
 * <br>The capital7software above will actually run like this:<br>
 * <p>
 * Initial state:<br>
 * ##.#.#<br>
 * ...##.<br>
 * #....#<br>
 * ..#...<br>
 * #.#..#<br>
 * ####.#<br>
 * <p>
 * After 1 step:<br>
 * #.##.#<br>
 * ####.#<br>
 * ...##.<br>
 * ......<br>
 * #...#.<br>
 * #.####<br>
 * <p>
 * After 2 steps:<br>
 * #..#.#<br>
 * #....#<br>
 * .#.##.<br>
 * ...##.<br>
 * .#..##<br>
 * ##.###<br>
 * <p>
 * After 3 steps:<br>
 * #...##<br>
 * ####.#<br>
 * ..##.#<br>
 * ......<br>
 * ##....<br>
 * ####.#<br>
 * <p>
 * After 4 steps:<br>
 * #.####<br>
 * #....#<br>
 * ...#..<br>
 * .##...<br>
 * #.....<br>
 * #.#..#<br>
 * <p>
 * After 5 steps:<br>
 * ##.###<br>
 * .##..#<br>
 * .##...<br>
 * .##...<br>
 * #.#...<br>
 * ##...#<br>
 * <p>
 * After 5 steps, this capital7software now has 17 lights on.
 * <p>
 * In your grid of 100x100 lights, given your initial configuration,
 * but with the four corners always in the on state, how many lights are on after 100 steps?
 * <p>
 * Your puzzle answer was 886.
 *
 */
public class Day18 implements AdventOfCodeSolution {
    /**
     * Instantiates the solution instance.
     */
    public Day18() {

    }

    @Override
    public String getDefaultInputFilename() {
        return "inputs/input_day_18-01.txt";
    }

    @Override
    public void runPart1(List<String> input) {
        var steps = 100;
        var start = Instant.now();
        var lights = animateLights(steps, input, false);
        var end = Instant.now();
        System.out.printf("The number of lights on after %d steps is: %d%n", steps, lights.first());
        printTiming(start, end);
    }

    @Override
    public void runPart2(List<String> input) {
        var steps = 100;
        var start = Instant.now();
        var lights = animateLights(steps, input, true);
        var end = Instant.now();
        System.out.printf("The number of lights on (corners always on) after %d steps is: %d%n", steps, lights.first());
        printTiming(start, end);
    }

    /**
     * Returns the number of lights still on after animating them for the specified number of steps.
     *
     * @param steps The number os steps to animate the lights for.
     * @param input The initial lighting layout.
     * @param cornersOn If true, the corner lights are always on.
     * @return The number of lights still on after animating them for the specified number of steps.
     */
    public Pair<Long, ChristmasLights> animateLights(int steps, List<String> input, boolean cornersOn) {
        var lights = ChristmasLights.buildFromLightingLayout(input, cornersOn);

        lights.animateLights(steps, cornersOn);
        var onCount = lights.getOnLightCount();

        return new Pair<>(onCount, lights);
    }
}
