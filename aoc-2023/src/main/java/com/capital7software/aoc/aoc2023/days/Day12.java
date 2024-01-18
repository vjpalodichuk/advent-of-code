package com.capital7software.aoc.aoc2023.days;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class Day12 {
    private static final Logger LOGGER = Logger.getLogger(Day12.class.getName());

    /**
     * Instantiates this Solution instance.
     */
    public Day12() {

    }

    private enum Spring {
        Operational('.'),
        Damaged('#'),
        Unknown('?');

        private final char label;

        Spring(char label) {
            this.label = label;
        }

        public static Spring from(char label) {
            for (var value : values()) {
                if (value.label == label) {
                    return value;
                }
            }

            return null;
        }
    }

    public record SpringRecord(List<Spring> springs, List<Integer> damagedSequence) {
        private static final String INITIAL_SPLIT = " ";
        private static final String DAMAGED_SPLIT = ",";

        public static SpringRecord parse(String line) {
            return parse(line, false);
        }

        public static SpringRecord parse(String line, boolean unfold) {
            var split = line.split(INITIAL_SPLIT);

            var springs = new ArrayList<>(split[0].chars()
                    .mapToObj(it -> (char) it)
                    .map(Spring::from)
                    .toList());

            if (unfold) {
                for (int i = 0; i < 4; i++) {
                    springs.add(Spring.Unknown);
                    springs.addAll(split[0].chars()
                            .mapToObj(it -> (char) it)
                            .map(Spring::from)
                            .toList());
                }
            }

            var damagedSequence = new ArrayList<>(Arrays.stream(split[1].split(DAMAGED_SPLIT))
                    .map(Integer::parseInt)
                    .toList());

            if (unfold) {
                for (int i = 0; i < 4; i++) {
                    damagedSequence.addAll(Arrays.stream(split[1].split(DAMAGED_SPLIT))
                            .map(Integer::parseInt)
                            .toList());
                }
            }

            return new SpringRecord(springs, damagedSequence);
        }

        @Override
        public String toString() {
            return "SpringRecord{" +
                    "springs=" + springs +
                    ", damagedSequence=" + damagedSequence +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof SpringRecord that)) {
                return false;
            }
            return springs().equals(that.springs()) && damagedSequence().equals(that.damagedSequence());
        }

        @Override
        public int hashCode() {
            return Objects.hash(springs(), damagedSequence());
        }

        /**
         * @return Returns the total number of valid sequences that are possible that satisfy the damagedSequence of
         * this record. A valid sequence is one where the Unknown Springs are replaced with either Operational or
         * Damaged Springs and the damagedSequence is satisfied.
         */
        public long validSequences() {
            var cache = new HashMap<SpringRecord, Long>();
            //            LOGGER.info(String.format("Valid sequences for this SpringRecord: " + count);
            return validSequences(cache);
        }

        /**
         * This is a recursive method that uses a cache to avoid calculating the same solution.
         *
         * @param cache The cache that will be used
         * @return The total number of valid sequences
         */
        private long validSequences(HashMap<SpringRecord, Long> cache) {
            // Stopping conditions
            // If we already solved this sequence, return it.
            if (cache.containsKey(this)) {
                return cache.get(this);
            }

            // If there are no Springs left to process, then we check if we have any damaged sequences left as well.
            // If we don't have any damaged sequences left to check, then we return 1 as the sequence is valid,
            // else 0.
            if (springs.isEmpty()) {
                if (damagedSequence.isEmpty()) {
                    return 1;
                } else {
                    return 0;
                }
            }

            // If we have some Springs left, but we don't have any damaged sequences left to check, then if
            // the remaining springs are all operational the sequence is valid, and we return 1, else 0.
            if (damagedSequence.isEmpty()) {
                if (springs.contains(Spring.Damaged)) {
                    return 0;
                } else {
                    return 1;
                }
            }

            // Get the next spring and sequence
            var spring = springs.getFirst();
            var sequence = damagedSequence.getFirst();

            // If the next spring could be Operational we ignore it and continue to check the remainder
            // of the sequence
            var countOperational = 0L;

            if (spring != Spring.Damaged) {
                countOperational = new SpringRecord(springs.subList(1, springs.size()), damagedSequence)
                        .validSequences(cache);
            }

            // If the next spring could be Damaged we have to check that the conditions to ensure that the damaged
            // sequence is met. The conditions are:
            // - Must be at least as many Springs remaining as expected by the next damage sequence.
            // - The next damage sequence's count of Springs must all be Damaged or possibly Damaged.
            // - After the damage sequence, there are no Springs left to process, the sequence is ended by an
            //   Operational Spring or possibly Operational Spring.
            // If these conditions are met, we can move on to the next damage sequence and process the remaining
            // Springs.
            var countDamaged = 0L;
            // Assume current Spring is operational or that the current damage sequence is
            // greater than the remaining number of springs!
            if (spring != Spring.Operational && sequence <= springs.size() &&
                    springs.subList(0, sequence).stream().noneMatch(it -> it == Spring.Operational) &&
                    !(springs.size() != sequence && springs.get(sequence) == Spring.Damaged)) {
                countDamaged = new SpringRecord(
                        springs.subList(Math.min(sequence + 1, springs.size()), springs.size()),
                        damagedSequence.subList(1, damagedSequence.size())
                ).validSequences(cache);
            }

            var total = countOperational + countDamaged;

            // Store the total in the cache!!
            cache.put(this, total);

            return total;
        }
    }

    private static final String inputFilename = "inputs/input_day_12-01.txt";

    public static void main(String[] args) throws URISyntaxException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        var url = classloader.getResource(inputFilename);
        assert url != null;
        var path = Paths.get(url.toURI());
        part1(path);
        part2(path);
    }

    public static void part1(Path path) {
        try (var stream = Files.lines(path)) {
            List<SpringRecord> springRecords = stream.map(SpringRecord::parse).toList();

            LOGGER.info(String.format("Loaded %d spring records!", springRecords.size()));
//            springRecords.forEach(System.out::println);
            // Part 1
            LOGGER.info("Part 1: Start!");
            var start = Instant.now();
            var sumOfSequences = solve(springRecords);
            var end = Instant.now();
            LOGGER.info(String.format("Sum of valid sequences: %d in %d ns",
                    sumOfSequences, Duration.between(start, end).toNanos()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void part2(Path path) {
        try (var stream = Files.lines(path)) {
            List<SpringRecord> springRecords = stream.map(it -> SpringRecord.parse(it, true)).toList();

            LOGGER.info(String.format("Loaded %d spring records!", springRecords.size()));
//            springRecords.forEach(System.out::println);
            // Part 2
            LOGGER.info("Part 2: Start!");
            var start = Instant.now();
            var sumOfSequences = solve(springRecords);
            var end = Instant.now();
            LOGGER.info(String.format("Sum of valid sequences: %d in %d ns",
                    sumOfSequences, Duration.between(start, end).toNanos()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static long solve(List<SpringRecord> springRecords) {
        return springRecords
                .stream()
                .mapToLong(SpringRecord::validSequences)
                .sum();
    }
}
