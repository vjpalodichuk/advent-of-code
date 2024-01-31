package com.capital7software.aoc.lib.grid;

import com.capital7software.aoc.lib.util.Pair;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

/**
 * Because your neighbors keep defeating you in the holiday house decorating contest year after
 * year, you've decided to deploy one million lights in a 1000x1000 grid.
 *
 * <p><br>
 * Furthermore, because you've been especially nice this year, Santa has mailed you instructions
 * on how to display the ideal lighting configuration.
 *
 * <p><br>
 * Lights in your grid are numbered from 0 to 999 in each direction; the lights at each corner are
 * at 0,0, 0,999, 999,999, and 999,0. The instructions include whether to turn on, turn off, or
 * toggle various inclusive ranges given as coordinate pairs. Each coordinate pair represents
 * opposite corners of a rectangle, inclusive; a coordinate pair like 0,0 through 2,2 therefore
 * refers to 9 lights in a 3x3 square. The lights all start turned off.
 *
 * <p><br>
 * To defeat your neighbors this year, all you have to do is set up your lights by doing the
 * instructions Santa sent you in order.
 *
 * <p><br>
 * For example:
 * <ul>
 *     <li>
 *         turn on 0,0 through 999,999 would turn on (or leave on) every light.
 *     </li>
 *     <li>
 *         toggle 0,0 through 999,0 would toggle the first line of 1000 lights, turning off the
 *         ones that were on, and turning on the ones that were off.
 *     </li>
 *     <li>
 *         turn off 499,499 through 500,500 would turn off (or leave off) the middle four lights.
 *     </li>
 * </ul>
 * After following the instructions, how many lights are lit?
 *
 * <p><br>
 * You just finish implementing your winning light pattern when you realize you mistranslated
 * Santa's message from Ancient Nordic Elvish.
 *
 * <p><br>
 * The light grid you bought actually has individual brightness controls; each light can have a
 * brightness of zero or more. The lights all start at zero.
 *
 * <p><br>
 * The phrase turn on actually means that you should increase the brightness of those lights by 1.
 *
 * <p><br>
 * The phrase turn off actually means that you should decrease the brightness of those lights by 1,
 * to a minimum of zero.
 *
 * <p><br>
 * The phrase toggle actually means that you should increase the brightness of those lights by 2.
 *
 * <p><br>
 * What is the total brightness of all lights combined after following Santa's instructions?
 *
 * <p><br>
 * For example:
 * <ul>
 *     <li>
 *         turn on 0,0 through 0,0 would increase the total brightness by 1.
 *     </li>
 *     <li>
 *         toggle 0,0 through 999,999 would increase the total brightness by 2000000.
 *     </li>
 * </ul>
 *
 * @param grid         The grid that holds all the lights.
 * @param instructions The list of instructions to apply to the light grid.
 */
public record ChristmasLights(@NotNull Grid2D<Integer> grid,
                              @NotNull List<LightInstruction> instructions) {
  /**
   * Instantiates a new ChristmasLights that owns the Grid2D and List of LightInstructions.
   *
   * @param grid         The grid that holds all the lights.
   * @param instructions The list of instructions to apply to the light grid.
   */
  public ChristmasLights(
      @NotNull Grid2D<Integer> grid,
      @NotNull List<LightInstruction> instructions
  ) {
    this.grid = grid.copy();
    this.instructions = new ArrayList<>(instructions);
  }

  /**
   * Builds the ChristmasLights in a grid of the specified size and parses and loads the
   * specified raw lighting instructions.<br>
   *
   * <p><br>
   * Format of the instructions are:<br>
   * <ul>
   *     <li>
   *         turn on 887,9 through 959,629
   *     </li>
   *     <li>
   *         turn on 454,398 through 844,448
   *     </li>
   *     <li>
   *         turn off 539,243 through 559,965
   *     </li>
   *     <li>
   *         turn off 370,819 through 676,868
   *     </li>
   * </ul>
   *
   * @param columns         The number of columns in the grid.
   * @param rows            The number of rows in the grid.
   * @param rawInstructions The raw lighting instructions to parse and load.
   * @return A new ChristmasLights instance in the specified size and loaded with the parsed
   *     instructions.
   */
  public static ChristmasLights buildFromLightingInstructions(
      int columns,
      int rows,
      @NotNull List<String> rawInstructions
  ) {
    var items = new Integer[columns * rows];

    var grid = new Grid2D<>(columns, rows, items, 0);
    var lights = new ChristmasLights(grid, LightInstruction.parse(rawInstructions));
    lights.reset();

    return lights;
  }

  /**
   * Builds the ChristmasLights in a grid based on the specified layout.<br>
   *
   * <p><br>
   * Format of the is:<br>
   * <ul>
   *     <li>
   *         All rows must be of the same length.
   *     </li>
   *     <li>
   *         A . means to turn the light off.
   *     </li>
   *     <li>
   *         A # means to turn the light on.
   *     </li>
   * </ul>
   * The grid size is determined by the number of rows and columns in the layout file.<br>
   * The ChristmasLights returned by this instance have no Instructions as they are meant to
   * be animated using the new animate method.
   *
   * @param layout    The layout file to parse.
   * @param cornersOn If set to true, the four corner lights are turned on even if the layout
   *                  doesn't specify them to be.
   * @return A new ChristmasLights instance in the specified size and loaded with the parsed
   *     instructions.
   */
  public static ChristmasLights buildFromLightingLayout(
      @NotNull List<String> layout,
      boolean cornersOn
  ) {
    var columns = new AtomicInteger(0);
    var rows = new AtomicInteger(0);
    var first = new AtomicBoolean(true);
    List<Integer> lights = new ArrayList<>();

    for (var line : layout) {
      if (line == null || line.isBlank()) {
        continue;
      }

      if (first.get()) {
        columns.set(line.length());
        first.set(false);
      }

      parseLayoutLine(line, lights);
      rows.getAndIncrement();
    }
    var items = new Integer[columns.get() * rows.get()];

    assert items.length == lights.size();

    for (int i = 0; i < lights.size(); i++) {
      items[i] = lights.get(i);
    }

    if (cornersOn) {
      items[0] = 1;
      items[items.length - 1] = 1;
      items[columns.get() - 1] = 1;
      items[columns.get() * rows.get() - columns.get()] = 1;
    }

    var grid = new Grid2D<>(columns.get(), rows.get(), items);

    return new ChristmasLights(grid, new ArrayList<>());
  }

  private static void parseLayoutLine(String line, @NotNull List<Integer> lights) {
    for (var c : line.toCharArray()) {
      if (c == '.') {
        lights.add(0);
      } else {
        lights.add(1);
      }
    }
  }

  /**
   * Returns a copy of the Grid2D used by this instance.
   *
   * @return A copy of the Grid2D used by this instance.
   */
  @Override
  public @NotNull Grid2D<Integer> grid() {
    return grid.copy();
  }

  /**
   * Returns an unmodifiable List of LightInstructions loaded into this instance.
   *
   * @return An unmodifiable List of LightInstructions loaded into this instance.
   */
  @Override
  public @NotNull List<LightInstruction> instructions() {
    return Collections.unmodifiableList(instructions);
  }

  /**
   * Turns all the lights in the grid off.
   */
  public void reset() {
    grid.fill(0);
  }

  /**
   * Applies all the loaded LightInstruction instructions in this instance.
   *
   * <p><br>
   * For example:
   *
   * <p><br>
   * turn on 0,0 through 999,999 would turn on (or leave on) every light.
   * toggle 0,0 through 999,0 would toggle the first line of 1000 lights, turning off the ones
   * that were on, and turning on the ones that were off.
   * turn off 499,499 through 500,500 would turn off (or leave off) the middle four lights.
   * After following the instructions, how many lights are lit?
   */
  public void applyInstructions() {
    for (var instruction : instructions) {
      if (instruction.type() == LightInstructionType.TURN_ON) {
        grid.set(instruction.point1(), instruction.point2(), 1);
      } else if (instruction.type() == LightInstructionType.TURN_OFF) {
        grid.set(instruction.point1(), instruction.point2(), 0);
      } else {
        grid.toggle(instruction.point1(), instruction.point2(), 1, 0);
      }
    }
  }

  /**
   * Applies all the loaded LightInstruction instructions in this instance using the new rules.
   *
   * <p><br>
   * The light grid you bought actually has individual brightness controls; each light can have a
   * brightness of zero or more. The lights all start at zero.
   *
   * <p><br>
   * The phrase turn on actually means that you should increase the brightness of those
   * lights by 1.
   *
   * <p><br>
   * The phrase turn off actually means that you should decrease the brightness of those
   * lights by 1, to a minimum of zero.
   *
   * <p><br>
   * The phrase toggle actually means that you should increase the brightness of those lights by 2.
   *
   * <p><br>
   * For example:
   *
   * <p><br>
   * turn on 0,0 through 0,0 would increase the total brightness by 1.
   * toggle 0,0 through 999,999 would increase the total brightness by 2000000.
   */
  public void applyNewInterpretationOfInstructions() {
    for (var instruction : instructions) {
      if (instruction.type() == LightInstructionType.TURN_ON) {
        grid.adjustBy(instruction.point1(), instruction.point2(), current -> current + 1);
      } else if (instruction.type() == LightInstructionType.TURN_OFF) {
        grid.adjustBy(
            instruction.point1(), instruction.point2(), current -> Math.max(0, current - 1)
        );
      } else {
        grid.adjustBy(
            instruction.point1(), instruction.point2(), current -> current + 2
        );
      }
    }
  }

  /**
   * Returns the count of lights that are currently on.
   *
   * @return The count of lights that are currently on.
   */
  public long getOnLightCount() {
    return Stream.of(grid().items()).filter(it -> it != 0).count();
  }

  /**
   * Returns the total brightness of all the lights that are on.
   *
   * @return The total brightness of all the lights that are on.
   */
  public long getTotalBrightness() {
    return Stream.of(grid().items()).mapToLong(it -> (long) it).sum();
  }

  /**
   * Animates the lights using the following rules.<br>
   * <ul>
   *     <li>
   *         A light which is on stays on when 2 or 3 neighbors are on, and turns off otherwise.
   *     </li>
   *     <li>
   *         A light which is off turns on if exactly 3 neighbors are on, and stays off otherwise.
   *     </li>
   * </ul>
   *
   * <p><br>
   * If cornersOn is set to true then the four corner lights will never turn off!
   *
   * @param steps     The number of animation steps to perform.
   * @param cornersOn If true, the four corner lights will never turn off!
   */
  public void animateLights(int steps, boolean cornersOn) {
    if (steps <= 0) {
      return;
    }

    for (int k = 0; k < steps; k++) {
      var currentState = grid.copy();

      for (int i = 0; i < grid.columns(); i++) {
        for (int j = 0; j < grid.rows(); j++) {
          if (cornersOn && ((i == 0 && j == 0) || (i == 0 && j == grid().rows() - 1)
              || (i == grid().columns() - 1 && j == 0)
              || (i == grid.columns() - 1 && j == grid().rows() - 1))) {
            continue;
          }

          var neighbors = currentState.getAllNeighbors(i, j);
          var onCount = neighbors.stream().map(Pair::second).filter(it -> it > 0).count();

          var currentValue = currentState.get(i, j);

          if (currentValue > 0) {
            if (onCount < 2 || onCount > 3) {
              grid.set(i, j, 0);
            }
          } else {
            if (onCount == 3) {
              grid.set(i, j, 1);
            }
          }
        }
      }
    }
  }
}
