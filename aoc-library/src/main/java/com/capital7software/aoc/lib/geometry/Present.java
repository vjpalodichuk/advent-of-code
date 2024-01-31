package com.capital7software.aoc.lib.geometry;

import com.capital7software.aoc.lib.collection.PriorityQueue;
import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

/**
 * Fortunately, every present is a box (a perfect right rectangular prism), which makes
 * calculating the required wrapping paper for each gift a little easier: find the surface area
 * of the box, which is 2*l*w + 2*w*h + 2*h*l. The elves also need a little extra paper for each
 * present: the area of the smallest side.
 *
 * <p><br>
 * For example:
 * <ul>
 *     <li>
 *         A present with dimensions 2x3x4 requires 2*6 + 2*12 + 2*8 = 52 square feet of wrapping
 *         paper plus 6 square feet of slack, for a total of 58 square feet.
 *     </li>
 *     <li>
 *         A present with dimensions 1x1x10 requires 2*1 + 2*10 + 2*10 = 42 square feet of
 *         wrapping paper plus 1 square foot of slack, for a total of 43 square feet.
 *     </li>
 * </ul>
 * Part 1: All numbers in the elves' list are in feet. How many total square feet of wrapping
 * paper should they order?
 *
 * <p><br>
 * The elves are also running low on ribbon. Ribbon is all the same width, so they only have to
 * worry about the length they need to order, which they would again like to be exact.
 *
 * <p><br>
 * The ribbon required to wrap a present is the shortest distance around its sides, or the
 * smallest perimeter of any one face. Each present also requires a bow made out of ribbon as well;
 * the feet of ribbon required for the perfect bow is equal to the cubic feet of volume of the
 * present. Don't ask how they tie the bow, though; they'll never tell.
 *
 * <p><br>
 * For example:
 * <ul>
 *     <li>
 *         A present with dimensions 2x3x4 requires 2+2+3+3 = 10 feet of ribbon to wrap the present
 *         plus 2*3*4 = 24 feet of ribbon for the bow, for a total of 34 feet.
 *     </li>
 *     <li>
 *         A present with dimensions 1x1x10 requires 1+1+1+1 = 4 feet of ribbon to wrap the present
 *         plus 1*1*10 = 10 feet of ribbon for the bow, for a total of 14 feet.
 *     </li>
 * </ul>
 * How many total feet of ribbon should they order?
 *
 * @param length The length of the box.
 * @param width  The width of the box.
 * @param height The height of the box.
 */
public record Present(long length, long width, long height) {
  /**
   * A present with dimensions 2x3x4 requires 2*6 + 2*12 + 2*8 = 52 square feet of wrapping
   * paper plus 6 square feet of slack, for a total of 58 square feet. A present with dimensions
   * 1x1x10 requires 2*1 + 2*10 + 2*10 = 42 square feet of wrapping paper plus 1 square foot
   * of slack, for a total of 43 square feet.
   *
   * @return The total amount of paper that is needed to wrap this present.
   */
  public long calculateNeededPaper() {
    var side1 = length * width;
    var side2 = width * height;
    var side3 = height * length;

    var minSide = Math.min(side1, Math.min(side2, side3));

    return 2 * side1 + 2 * side2 + 2 * side3 + minSide;
  }

  /**
   * A present with dimensions 2x3x4 requires 2+2+3+3 = 10 feet of ribbon to wrap the present
   * plus 2*3*4 = 24 feet of ribbon for the bow, for a total of 34 feet. A present with dimensions
   * 1x1x10 requires 1+1+1+1 = 4 feet of ribbon to wrap the present plus 1*1*10 = 10 feet of
   * ribbon for the bow, for a total of 14 feet.
   *
   * @return The total amount of ribbon needed to wrap this present and to make a bow for it.
   */
  public long calculateNeededRibbon() {
    var queue = new PriorityQueue<Long>();
    queue.offer(length);
    queue.offer(width);
    queue.offer(height);
    var area = length * width * height;
    var min1 = queue.poll();
    var min2 = queue.poll();

    assert min1 != null;
    assert min2 != null;

    return 2 * min1 + 2 * min2 + area;
  }

  /**
   * Parses a list of Strings and returns a list of Presents.
   *
   * @param presents A list of strings where each line is in LxWxH format.
   * @return A list of Presents parsed from the input strings.
   */
  @NotNull
  public static List<Present> parse(@NotNull List<String> presents) {
    return presents
        .stream()
        .map(Present::parse)
        .filter(Objects::nonNull)
        .toList();
  }


  /**
   * Parses a String and returns a Present.
   *
   * @param present The string to parse in LxWxH format.
   * @return A Present parsed from the input string.
   */
  public static Present parse(String present) {
    if (present == null || present.isBlank()) {
      return null;
    }

    var split = present.split("x");
    var length = Long.parseLong(split[0].trim());
    var width = Long.parseLong(split[1].trim());
    var height = Long.parseLong(split[2].trim());

    return new Present(length, width, height);
  }
}
