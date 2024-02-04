package com.capital7software.aoc.aoc2015aoc.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.crypt.Md5Fun;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * --- Day 4: The Ideal Stocking Stuffer ---<br><br>
 * Santa needs help mining some AdventCoins (very similar to bitcoins) to use as gifts for all
 * the economically forward-thinking little girls and boys.
 *
 * <p><br>
 * To do this, he needs to find MD5 hashes which, in hexadecimal, start with at least five zeroes.
 * The input to the MD5 hash is some secret key (your puzzle input, given below) followed by a
 * number in decimal. To mine AdventCoins, you must find Santa the lowest positive number
 * (no leading zeroes: 1, 2, 3, ...) that produces such a hash.
 *
 * <p><br>
 * For example:
 *
 * <p><br>
 * If your secret key is abcdef, the answer is 609043, because the MD5 hash of abcdef609043 starts
 * with five zeroes (000001dbbfa...), and it is the lowest such number to do so.
 *
 * <p><br>
 * If your secret key is pqrstuv, the lowest number it combines with to make an MD5 hash starting
 * with five zeroes is 1048970; that is, the MD5 hash of pqrstuv1048970 looks like 000006136ef....
 *
 * <p><br>
 * Your puzzle input is yzbqklnj.
 *
 * <p><br>
 * Your puzzle answer was 282749.
 *
 * <p><br>
 * --- Part Two ---<br><br>
 * Now find one that starts with six zeroes.
 *
 * <p><br>
 * Your puzzle answer was 9962624.
 */
public class Day04 implements AdventOfCodeSolution {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day04.class);

  /**
   * Instantiates the solution instance.
   */
  public Day04() {

  }

  @Override
  public String getDefaultInputFilename() {
    return "inputs/input_day_04-01.txt";
  }

  @Override
  public void runPart1(List<String> input) {
    var message = "Lowest positive number to concatenate with {} to produce a MD5 hash "
        + "with 5 leading zeros is: {}";

    for (var secret : input) {
      var start = Instant.now();
      var total = lowestPositiveNumber(secret, 5);
      var end = Instant.now();

      LOGGER.info(message, secret, total);
      logTimings(LOGGER, start, end);
    }
  }

  @Override
  public void runPart2(List<String> input) {
    var message = "Lowest positive number to concatenate with {} to produce a MD5 hash "
        + "with 6 leading zeros is: {}";

    for (var secret : input) {
      var start = Instant.now();
      var total = lowestPositiveNumber(secret, 6);
      var end = Instant.now();

      LOGGER.info(message, secret, total);
      logTimings(LOGGER, start, end);
    }
  }

  /**
   * Returns the lowest positive number to combine with the secret to generate a
   * MD5 Hash with the specified number of leading zeros.
   *
   * @param secret       The secret to combine with the number we are looking for.
   * @param leadingZeros How many leading zeros the hash must have.
   * @return The lowest positive number to combine with the secret to generate a
   *     MD5 Hash with the specified number of leading zeros.
   */
  public long lowestPositiveNumber(String secret, int leadingZeros) {
    return Md5Fun.lowestPositiveHashWithLeadingZeros(secret, leadingZeros, 0).second();
  }
}
