package com.capital7software.aoc.lib.string;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Santa is trying to deliver presents in a large apartment building, but he can't find the right
 * floor - the directions he got are a little confusing. He starts on the ground floor (floor 0)
 * and then follows the instructions one character at a time.
 *
 * <p><br>
 * An opening parenthesis, (, means he should go up one floor, and a closing parenthesis, ),
 * means he should go down one floor.
 *
 * <p><br>
 * The apartment building is very tall, and the basement is very deep; he will never find the
 * top or bottom floors.
 *
 * <p><br>
 * For example:
 *
 * <p><br>
 * <code>
 * (()) and ()() both result in floor 0.<br>
 * ((( and (()(()( both result in floor 3.<br>
 * ))((((( also results in floor 3.<br>
 * ()) and ))( both result in floor -1 (the first basement level).<br>
 * ))) and )())()) both result in floor -3.<br>
 * </code>
 *
 * <p><br>
 * Part 1: To what floor do the instructions take Santa?
 *
 * <p><br>
 * Now, given the same instructions, find the position of the first character that causes
 * him to enter the basement (floor -1). The first character in the instructions has position 1,
 * the second character has position 2, and so on.
 *
 * <p><br>
 * For example:
 *
 * <p><br>
 * <code>
 * ) causes him to enter the basement at character position 1.<br>
 * ()()) causes him to enter the basement at character position 5.<br>
 * </code>
 *
 * <p><br>
 * What is the position of the character that causes Santa to first enter the basement?
 *
 * @param instructions The list of instructions to follow.
 */
public record ApartmentBuilding(@NotNull List<String> instructions) {
  /**
   * Helper constructor to ensure that the specified instructions are copied to this instance.
   *
   * @param instructions The instructions to follow.
   */
  public ApartmentBuilding(@NotNull List<String> instructions) {
    this.instructions = new ArrayList<>(instructions);
  }

  /**
   * Returns an unmodifiable List of the instructions for this instance.
   *
   * @return An unmodifiable List of the instructions for this instance.
   */
  @Override
  public @NotNull List<String> instructions() {
    return Collections.unmodifiableList(instructions);
  }

  /**
   * The final floor Santa is on after following the instructions.
   *
   * @return The final floor Santa is on.
   */
  public long finalFloor() {
    var openCount = 0L;
    var closedCount = 0L;

    for (var line : instructions) {
      if (line != null && !line.isBlank()) {
        for (var c : line.toCharArray()) {
          if (c == '(') {
            openCount++;
          } else if (c == ')') {
            closedCount++;
          }
        }
      }
    }

    return openCount - closedCount;
  }

  /**
   * Finds the position of the first character that causes Santa to enter
   * the basement (floor -1). The first character in the instructions has position 1,
   * the second character has position 2, and so on.
   *
   * @return the position of the first character that causes Santa to enter the basement
   *     or -1 if he never enters the basement.
   */
  public long firstBasementFloorPosition() {
    var openCount = 0L;
    var closedCount = 0L;

    for (var line : instructions) {
      if (line != null && !line.isBlank()) {
        var chars = line.toCharArray();
        for (int i = 0; i < chars.length; i++) {
          char c = chars[i];

          if (c == '(') {
            openCount++;
          } else if (c == ')') {
            closedCount++;
          }
          if (closedCount > openCount) {
            return i + 1;
          }
        }
      }
    }

    return -1L;
  }
}
