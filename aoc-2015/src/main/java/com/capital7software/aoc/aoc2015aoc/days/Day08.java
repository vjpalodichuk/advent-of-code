package com.capital7software.aoc.aoc2015aoc.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.string.EncodingFun;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * --- Day 8: Matchsticks ---<br><br>
 * Space on the sleigh is limited this year, and so Santa will be bringing his list as a digital
 * copy. He needs to know how much space it will take up when stored.
 *
 * <p><br>
 * It is common in many programming languages to provide a way to escape special characters in
 * strings. For example, C, JavaScript, Perl, Python, and even PHP handle special characters
 * in very similar ways.
 *
 * <p><br>
 * However, it is important to realize the difference between the number of characters in the code
 * representation of the string literal and the number of characters in the in-memory string itself.
 *
 * <p><br>
 * For example:
 * <ul>
 *     <li>
 *         "" is 2 characters of code (the two double quotes), but the string contains zero
 *         characters.
 *     </li>
 *     <li>
 *         "abc" is 5 characters of code, but 3 characters in the string data.
 *     </li>
 *     <li>
 *         "aaa\"aaa" is 10 characters of code, but the string itself contains six "a" characters
 *         and a single, escaped quote character, for a total of 7 characters in the string data.
 *     </li>
 *     <li>
 *         "\x27" is 6 characters of code, but the string itself contains just one -
 *         an apostrophe ('), escaped using hexadecimal notation.
 *     </li>
 * </ul>
 * Santa's list is a file that contains many double-quoted string literals, one on each line.
 * The only escape sequences used are \\ (which represents a single backslash),
 * \" (which represents a lone double-quote character), and \x plus two hexadecimal characters
 * (which represents a single character with that ASCII code).
 *
 * <p><br>
 * Disregarding the whitespace in the file, what is the number of characters of code for string
 * literals minus the number of characters in memory for the values of the strings in total for
 * the entire file?
 *
 * <p><br>
 * For example, given the four strings above, the total number of characters of string
 * code (2 + 5 + 10 + 6 = 23) minus the total number of characters in memory for string
 * values (0 + 3 + 7 + 1 = 11) is 23 - 11 = 12.
 *
 * <p><br>
 * Your puzzle answer was 1350.
 *
 * <p><br>
 * --- Part Two ---<br><br>
 * Now, let's go the other way. In addition to finding the number of characters of code, you
 * should now encode each code representation as a new string and find the number of characters
 * of the new encoded representation, including the surrounding double quotes.
 *
 * <p><br>
 * For example:
 * <ul>
 *     <li>
 *         "" encodes to "\"\"", an increase from 2 characters to 6.
 *     </li>
 *     <li>
 *         "abc" encodes to "\"abc\"", an increase from 5 characters to 9.
 *     </li>
 *     <li>
 *         "aaa\"aaa" encodes to "\"aaa\\\"aaa\"", an increase from 10 characters to 16.
 *     </li>
 *     <li>
 *         "\x27" encodes to "\"\\x27\"", an increase from 6 characters to 11.
 *     </li>
 * </ul>
 * Your task is to find the total number of characters to represent the newly encoded strings
 * minus the number of characters of code in each original string literal. For example, for the
 * strings above, the total encoded length (6 + 9 + 16 + 11 = 42) minus the characters in the
 * original code representation (23, just like in the first part of this puzzle) is 42 - 23 = 19.
 *
 * <p><br>
 * Your puzzle answer was 1350.
 */
public class Day08 implements AdventOfCodeSolution {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day02.class);

  /**
   * Instantiates the solution instance.
   */
  public Day08() {

  }

  @Override
  public String getDefaultInputFilename() {
    return "inputs/input_day_08-01.txt";
  }

  @Override
  public void runPart1(List<String> input) {
    var memory = new AtomicInteger(0);
    var code = new AtomicInteger(0);

    var start = Instant.now();

    input.forEach(s -> {
      memory.set(memory.get() + memoryCount(s));
      code.set(code.get() + codeCount(s));
    });

    var total = code.get() - memory.get();
    var end = Instant.now();
    final var message = "The number of characters of code: {} minus the number of "
        + "characters in memory {} in the whole file is: {}";
    LOGGER.info(message, code.get(), memory.get(), total);
    logTimings(LOGGER, start, end);
  }

  @Override
  public void runPart2(List<String> input) {
    var newCode = new AtomicInteger(0);
    var code = new AtomicInteger(0);

    var start = Instant.now();

    input.forEach(s -> {
      newCode.set(newCode.get() + codeCount(encodeString(s)));
      code.set(code.get() + codeCount(s));
    });

    var total = newCode.get() - code.get();
    var end = Instant.now();
    final var message = "The number of characters in new string: {} minus the number of "
        + "characters of code in old string {} in the whole file is: {}";
    LOGGER.info(message, newCode.get(), code.get(), total);
    logTimings(LOGGER, start, end);
  }

  /**
   * Returns the count of Code characters in the specified String.
   *
   * @param string The String to count the Code characters in.
   * @return The count of Code characters in the specified String.
   */
  public int codeCount(String string) {
    return EncodingFun.countCode(string);
  }

  /**
   * Returns the count of Memory characters in the specified String.
   *
   * @param string The String to count the Code characters in.
   * @return The count of Memory characters in the specified String.
   */
  public int memoryCount(String string) {
    return EncodingFun.countMemory(string);
  }

  /**
   * Encodes the specified String as a new String with Code characters.
   * Returns the new encoded String.
   *
   * @param string The String to encode.
   * @return The new encoded String.
   */
  public String encodeString(String string) {
    return EncodingFun.encodeString(string);
  }
}
