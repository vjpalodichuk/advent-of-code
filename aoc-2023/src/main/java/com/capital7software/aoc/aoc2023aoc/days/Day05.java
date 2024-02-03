package com.capital7software.aoc.aoc2023aoc.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.analysis.SeedMapping;
import java.time.Instant;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * --- Day 5: If You Give A Seed A Fertilizer ---<br><br>
 * You take the boat and find the gardener right where you were told he would be: managing a giant
 * "garden" that looks more to you like a farm.
 *
 * <p><br>
 * "A water source? Island Island is the water source!" You point out that Snow Island isn't
 * receiving any water.
 *
 * <p><br>
 * "Oh, we had to stop the water because we ran out of sand to filter it with! Can't make
 * snow with dirty water. Don't worry, I'm sure we'll get more sand soon; we only turned
 * off the water a few days... weeks... oh no." His face sinks into a look of horrified realization.
 *
 * <p><br>
 * "I've been so busy making sure everyone here has food that I completely forgot to check
 * why we stopped getting more sand! There's a ferry leaving soon that is headed over in that
 * direction - it's much faster than your boat. Could you please go check it out?"
 *
 * <p><br>
 * You barely have time to agree to this request when he brings up another. "While you wait
 * for the ferry, maybe you can help us with our food production problem. The latest
 * Island Island Almanac just arrived, and we're having trouble making sense of it."
 *
 * <p><br>
 * The almanac (your puzzle input) lists all the seeds that need to be planted. It
 * also lists what type of soil to use with each kind of seed, what type of fertilizer
 * to use with each kind of soil, what type of water to use with each kind of fertilizer,
 * and so on. Every type of seed, soil, fertilizer and so on is identified with a number,
 * but numbers are reused by each category - that is, soil 123 and fertilizer 123 aren't
 * necessarily related to each other.
 *
 * <p><br>
 * For example:
 *
 * <p><br>
 * <code>
 * seeds: 79 14 55 13<br>
 * </code>
 *
 * <p><br>
 * <code>
 * seed-to-soil map:<br>
 * 50 98 2<br>
 * 52 50 48<br>
 * </code>
 *
 * <p><br>
 * <code>
 * soil-to-fertilizer map:<br>
 * 0 15 37<br>
 * 37 52 2<br>
 * 39 0 15<br>
 * </code>
 *
 * <p><br>
 * <code>
 * fertilizer-to-water map:<br>
 * 49 53 8<br>
 * 0 11 42<br>
 * 42 0 7<br>
 * 57 7 4<br>
 * </code>
 *
 * <p><br>
 * <code>
 * water-to-light map:<br>
 * 88 18 7<br>
 * 18 25 70<br>
 * </code>
 *
 * <p><br>
 * <code>
 * light-to-temperature map:<br>
 * 45 77 23<br>
 * 81 45 19<br>
 * 68 64 13<br>
 * </code>
 *
 * <p><br>
 * <code>
 * temperature-to-humidity map:<br>
 * 0 69 1<br>
 * 1 0 69<br>
 * </code>
 *
 * <p><br>
 * <code>
 * humidity-to-location map:<br>
 * 60 56 37<br>
 * 56 93 4<br>
 * </code>
 *
 * <p><br>
 * The almanac starts by listing which seeds need to be planted:<br>
 * seeds 79, 14, 55, and 13.
 *
 * <p><br>
 * The rest of the almanac contains a list of maps which describe how to convert numbers from a
 * source category into numbers in a destination category. That is, the section that starts with
 * seed-to-soil map: describes how to convert a seed number (the source) to a soil number
 * (the destination). This lets the gardener and his team know which soil to use with which seeds,
 * which water to use with which fertilizer, and so on.
 *
 * <p><br>
 * Rather than list every source number and its corresponding destination number one by one, the
 * maps describe entire ranges of numbers that can be converted. Each line within a map contains
 * three numbers: the destination range start, the source range start, and the range length.
 *
 * <p><br>
 * Consider again the example seed-to-soil map:
 *
 * <p><br>
 * <code>
 * 50 98 2<br>
 * 52 50 48<br>
 * </code>
 *
 * <p><br>
 * The first line has a destination range start of 50, a source range start of 98, and a range
 * length of 2. This line means that the source range starts at 98 and contains two values:
 * 98 and 99. The destination range is the same length, but it starts at 50, so its two values
 * are 50 and 51. With this information, you know that seed number 98 corresponds to soil number
 * 50 and that seed number 99 corresponds to soil number 51.
 *
 * <p><br>
 * The second line means that the source range starts at 50 and contains 48 values:
 * 50, 51, ..., 96, 97. This corresponds to a destination range starting at 52 and
 * also containing 48 values: 52, 53, ..., 98, 99. So, seed number 53 corresponds to soil number 55.
 *
 * <p><br>
 * Any source numbers that aren't mapped correspond to the same destination number.
 * So, seed number 10 corresponds to soil number 10.
 *
 * <p><br>
 * So, the entire list of seed numbers and their corresponding soil numbers looks like this:
 *
 * <p><br>
 * <code>
 * seed  soil<br>
 * 0     0<br>
 * 1     1<br>
 * ...   ...<br>
 * 48    48<br>
 * 49    49<br>
 * 50    52<br>
 * 51    53<br>
 * ...   ...<br>
 * 96    98<br>
 * 97    99<br>
 * 98    50<br>
 * 99    51<br>
 * </code>
 *
 * <p><br>
 * With this map, you can look up the soil number required for each initial seed number:
 *
 * <p><br>
 * Seed number 79 corresponds to soil number 81.<br>
 * Seed number 14 corresponds to soil number 14.<br>
 * Seed number 55 corresponds to soil number 57.<br>
 * Seed number 13 corresponds to soil number 13.<br>
 *
 * <p><br>
 * The gardener and his team want to get started as soon as possible, so they'd like to know
 * the closest location that needs a seed. Using these maps, find the lowest location number
 * that corresponds to any of the initial seeds. To do this, you'll need to convert each seed
 * number through other categories until you can find its corresponding location number.
 * In this example, the corresponding types are:
 *
 * <p><br>
 * Seed 79, soil 81, fertilizer 81, water 81, light 74, temperature 78, humidity 78, location 82.
 * <br>
 * Seed 14, soil 14, fertilizer 53, water 49, light 42, temperature 42, humidity 43, location 43.
 * <br>
 * Seed 55, soil 57, fertilizer 57, water 53, light 46, temperature 82, humidity 82, location 86.
 * <br>
 * Seed 13, soil 13, fertilizer 52, water 41, light 34, temperature 34, humidity 35, location 35.
 * <br>
 *
 * <p><br>
 * So, the lowest location number in this example is 35.
 *
 * <p><br>
 * What is the lowest location number that corresponds to any of the initial seed numbers?
 *
 * <p><br>
 * Your puzzle answer was 1181555926.
 *
 * <p><br>
 * --- Part Two ---<br><br>
 * Everyone will starve if you only plant such a small number of seeds. Re-reading the almanac,
 * it looks like the seeds: line actually describes ranges of seed numbers.
 *
 * <p><br>
 * The values on the initial seeds: line come in pairs. Within each pair, the first value
 * is the start of the range and the second value is the length of the range. So, in the first
 * line of the example above:
 *
 * <p><br>
 * seeds: 79 14 55 13<br>
 * This line describes two ranges of seed numbers to be planted in the garden. The first range
 * starts with seed number 79 and contains 14 values: 79, 80, ..., 91, 92. The second range
 * starts with seed number 55 and contains 13 values: 55, 56, ..., 66, 67.
 *
 * <p><br>
 * Now, rather than considering four seed numbers, you need to consider a total of 27 seed numbers.
 *
 * <p><br>
 * In the above example, the lowest location number can be obtained from seed number 82, which
 * corresponds to soil 84, fertilizer 84, water 84, light 77, temperature 45, humidity 46, and
 * location 46. So, the lowest location number is 46.
 *
 * <p><br>
 * Consider all the initial seed numbers listed in the ranges on the first line of the almanac.
 * What is the lowest location number that corresponds to any of the initial seed numbers?
 *
 * <p><br>
 * Your puzzle answer was 37806486.
 */
public class Day05 implements AdventOfCodeSolution {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day05.class);

  /**
   * Instantiates the solution instance.
   */
  public Day05() {

  }

  @Override
  public String getDefaultInputFilename() {
    return "inputs/input_day_05-01.txt";
  }

  @Override
  public void runPart1(List<String> input) {
    var start = Instant.now();
    var answer = getLowestLocationNumber(input);
    var end = Instant.now();

    LOGGER.info("The lowest location number is: {}", answer);
    logTimings(LOGGER, start, end);
  }

  @Override
  public void runPart2(List<String> input) {
    var start = Instant.now();
    var answer = getLowestLocationNumberForAnyInitialSeed(input);
    var end = Instant.now();

    LOGGER.info("The lowest location number using RangeMaps is: {}", answer);
    logTimings(LOGGER, start, end);
  }


  /**
   * Returns the location ID of the seed with the lowest location ID based on the seeds.
   *
   * @param input The seeds and mappings to parse.
   * @return The location ID of the seed with the lowest location ID based on the seeds.
   */
  public long getLowestLocationNumber(@NotNull List<String> input) {
    var instance = SeedMapping.buildSeedMapping(input);

    return instance.getSeeds()
        .stream()
        .mapToLong(SeedMapping.Seed::getLocationId)
        .min()
        .orElse(-1L);
  }

  /**
   * Returns the location ID of the seed with the lowest location ID based on ranges.
   *
   * @param input The seeds and mappings to parse.
   * @return The location ID of the seed with the lowest location ID based on ranges.
   */
  public long getLowestLocationNumberForAnyInitialSeed(@NotNull List<String> input) {
    var instance = SeedMapping.buildSeedMapping(input);
    var seed = instance.calculateMinimumSeedLocation();
    var answer = -1L;

    if (seed.isPresent()) {
      answer = seed.get().getLocationId();
    }

    return answer;
  }
}
