package com.capital7software.aoc.lib.analysis;

import com.capital7software.aoc.lib.util.Range;
import com.capital7software.aoc.lib.util.RangeMap;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The almanac (your puzzle input) lists all the seeds that need to be planted. It
 * also lists what type of soil to use with each kind of seed, what type of fertilizer
 * to use with each kind of soil, what type of water to use with each kind of fertilizer,
 * and so on. Every type of seed, soil, fertilizer and so on is identified with a number,
 * but numbers are reused by each category - that is, soil 123 and fertilizer 123 aren't
 * necessarily related to each other.
 * <p><br>
 * For example:
 * <p><br>
 * <code>
 * seeds: 79 14 55 13<br>
 * </code>
 * <p><br>
 * <code>
 * seed-to-soil map:<br>
 * 50 98 2<br>
 * 52 50 48<br>
 * </code>
 * <p><br>
 * <code>
 * soil-to-fertilizer map:<br>
 * 0 15 37<br>
 * 37 52 2<br>
 * 39 0 15<br>
 * </code>
 * <p><br>
 * <code>
 * fertilizer-to-water map:<br>
 * 49 53 8<br>
 * 0 11 42<br>
 * 42 0 7<br>
 * 57 7 4<br>
 * </code>
 * <p><br>
 * <code>
 * water-to-light map:<br>
 * 88 18 7<br>
 * 18 25 70<br>
 * </code>
 * <p><br>
 * <code>
 * light-to-temperature map:<br>
 * 45 77 23<br>
 * 81 45 19<br>
 * 68 64 13<br>
 * </code>
 * <p><br>
 * <code>
 * temperature-to-humidity map:<br>
 * 0 69 1<br>
 * 1 0 69<br>
 * </code>
 * <p><br>
 * <code>
 * humidity-to-location map:<br>
 * 60 56 37<br>
 * 56 93 4<br>
 * </code>
 * <p><br>
 * The almanac starts by listing which seeds need to be planted:<br>
 * seeds 79, 14, 55, and 13.
 * <p><br>
 * The rest of the almanac contains a list of maps which describe how to convert numbers from a
 * source category into numbers in a destination category. That is, the section that starts with
 * seed-to-soil map: describes how to convert a seed number (the source) to a soil number
 * (the destination). This lets the gardener and his team know which soil to use with which seeds,
 * which water to use with which fertilizer, and so on.
 * <p><br>
 * Rather than list every source number and its corresponding destination number one by one, the
 * maps describe entire ranges of numbers that can be converted. Each line within a map contains
 * three numbers: the destination range start, the source range start, and the range length.
 * <p><br>
 * Consider again the example seed-to-soil map:
 * <p><br>
 * <code>
 * 50 98 2<br>
 * 52 50 48<br>
 * </code>
 * <p><br>
 * The first line has a destination range start of 50, a source range start of 98, and a range length of 2.
 * This line means that the source range starts at 98 and contains two values: 98 and 99.
 * The destination range is the same length, but it starts at 50, so its two values are 50 and 51.
 * With this information, you know that seed number 98 corresponds to soil number 50 and that
 * seed number 99 corresponds to soil number 51.
 * <p><br>
 * The second line means that the source range starts at 50 and contains 48 values: 50, 51, ..., 96, 97.
 * This corresponds to a destination range starting at 52 and also containing 48 values: 52, 53, ..., 98, 99.
 * So, seed number 53 corresponds to soil number 55.
 * <p><br>
 * Any source numbers that aren't mapped correspond to the same destination number.
 * So, seed number 10 corresponds to soil number 10.
 * <p><br>
 * So, the entire list of seed numbers and their corresponding soil numbers looks like this:
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
 * <p><br>
 * With this map, you can look up the soil number required for each initial seed number:
 * <p><br>
 * Seed number 79 corresponds to soil number 81.<br>
 * Seed number 14 corresponds to soil number 14.<br>
 * Seed number 55 corresponds to soil number 57.<br>
 * Seed number 13 corresponds to soil number 13.<br>
 * <p><br>
 * The gardener and his team want to get started as soon as possible, so they'd like to know the closest
 * location that needs a seed. Using these maps, find the lowest location number that corresponds to any
 * of the initial seeds. To do this, you'll need to convert each seed number through other categories
 * until you can find its corresponding location number. In this example, the corresponding types are:
 * <p><br>
 * Seed 79, soil 81, fertilizer 81, water 81, light 74, temperature 78, humidity 78, location 82.<br>
 * Seed 14, soil 14, fertilizer 53, water 49, light 42, temperature 42, humidity 43, location 43.<br>
 * Seed 55, soil 57, fertilizer 57, water 53, light 46, temperature 82, humidity 82, location 86.<br>
 * Seed 13, soil 13, fertilizer 52, water 41, light 34, temperature 34, humidity 35, location 35.<br>
 * <p><br>
 * So, the lowest location number in this example is 35.
 * <p><br>
 * What is the lowest location number that corresponds to any of the initial seed numbers?
 * <p><br>
 * Everyone will starve if you only plant such a small number of seeds. Re-reading the almanac, it looks
 * like the seeds: line actually describes ranges of seed numbers.
 * <p><br>
 * The values on the initial seeds: line come in pairs. Within each pair, the first value is the start
 * of the range and the second value is the length of the range. So, in the first line of the example above:
 * <p><br>
 * seeds: 79 14 55 13<br>
 * This line describes two ranges of seed numbers to be planted in the garden. The first range starts
 * with seed number 79 and contains 14 values: 79, 80, ..., 91, 92. The second range starts with seed
 * number 55 and contains 13 values: 55, 56, ..., 66, 67.
 * <p><br>
 * Now, rather than considering four seed numbers, you need to consider a total of 27 seed numbers.
 * <p><br>
 * In the above example, the lowest location number can be obtained from seed number 82, which
 * corresponds to soil 84, fertilizer 84, water 84, light 77, temperature 45, humidity 46, and
 * location 46. So, the lowest location number is 46.
 * <p><br>
 * Consider all the initial seed numbers listed in the ranges on the first line of the almanac.
 * What is the lowest location number that corresponds to any of the initial seed numbers?
 *
 */
public class SeedMapping {
    /**
     * A Seed that can grow!
     */
    public static class Seed {
        private final Long id;
        private Long soilId;
        private Long fertilizerId;
        private Long waterId;
        private Long lightId;
        private Long temperatureId;
        private Long humidityId;
        private Long locationId;

        /**
         * Instantiates a new Seed with the specified ID.
         *
         * @param id The ID of the new Seed.
         */
        public Seed(Long id) {
            this.id = id;
        }

        /**
         * Returns the ID of this Seed.
         *
         * @return The ID of this Seed.
         */
        public Long getId() {
            return id;
        }

        /**
         * Returns the ID of the Soil this Seed needs to be planted in.
         * @return The ID of the Soil this Seed needs to be planted in.
         */
        public Long getSoilId() {
            return soilId;
        }

        /**
         * Sets the Soil ID of this Seed to the specified value.
         *
         * @param soilId The new Soil ID for this Seed.
         */
        public void setSoilId(Long soilId) {
            this.soilId = soilId;
        }

        /**
         * Returns the ID of the Fertilizer that needs to be used on the Soil for this Seed.
         *
         * @return The ID of the Fertilizer that needs to be used on the Soil for this Seed.
         */
        public Long getFertilizerId() {
            return fertilizerId;
        }

        /**
         * Sets the Fertilizer ID of this Seed to the specified value.
         *
         * @param fertilizerId The new Fertilizer ID of this Seed.
         */
        public void setFertilizerId(Long fertilizerId) {
            this.fertilizerId = fertilizerId;
        }

        /**
         * Returns the ID of the Water that needs to be used to water this Seed.
         *
         * @return The ID of the Water that needs to be used to water this Seed.
         */
        public Long getWaterId() {
            return waterId;
        }

        /**
         * Sets the Water ID of this Seed to the specified value.
         *
         * @param waterId The new Water ID for this Seed.
         */
        public void setWaterId(Long waterId) {
            this.waterId = waterId;
        }

        /**
         * Returns the ID of the Light that needs to be used to light this Seed.
         *
         * @return The ID of the Light that needs to be used to light this Seed.
         */
        public Long getLightId() {
            return lightId;
        }

        /**
         * Sets the Light ID of this Seed to the specified value.
         *
         * @param lightId The new Light ID for this Seed.
         */
        public void setLightId(Long lightId) {
            this.lightId = lightId;
        }

        /**
         * Returns the ID of the Temperature that needs to be used to grow this Seed.
         *
         * @return The ID of the Temperature that needs to be used to grow this Seed.
         */
        public Long getTemperatureId() {
            return temperatureId;
        }

        /**
         * Sets the Temperature ID of this Seed to the specified value.
         *
         * @param temperatureId The new Temperature ID for this Seed.
         */
        public void setTemperatureId(Long temperatureId) {
            this.temperatureId = temperatureId;
        }

        /**
         * Returns the ID of the Humidity that needs to be used to grow this Seed.
         *
         * @return The ID of the Humidity that needs to be used to grow this Seed.
         */
        public Long getHumidityId() {
            return humidityId;
        }

        /**
         * Sets the Humidity ID of this Seed to the specified value.
         *
         * @param humidityId The new Humidity ID for this Seed.
         */
        public void setHumidityId(Long humidityId) {
            this.humidityId = humidityId;
        }

        /**
         * Returns the ID of the Location that needs to be used to grow this Seed.
         *
         * @return The ID of the Location that needs to be used to grow this Seed.
         */
        public Long getLocationId() {
            return locationId;
        }

        /**
         * Sets the Location ID of this Seed to the specified value.
         *
         * @param locationId The new Location ID for this Seed.
         */
        public void setLocationId(Long locationId) {
            this.locationId = locationId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Seed seed = (Seed) o;
            return getId().equals(seed.getId());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getId());
        }

        @Override
        public String toString() {
            return "Seed{" +
                    "id=" + id +
                    ", soilId=" + soilId +
                    ", fertilizerId=" + fertilizerId +
                    ", waterId=" + waterId +
                    ", lightId=" + lightId +
                    ", temperatureId=" + temperatureId +
                    ", humidityId=" + humidityId +
                    ", locationId=" + locationId +
                    '}';
        }
    }

    private static final String SEEDS_SPLIT = ": ";
    private static final String VALUES_SPLIT = " ";
    private static final String SEEDS_START = "seeds:";
    private static final String SEED_TO_SOIL_MAP_START = "seed-to-soil map:";
    private static final String SOIL_TO_FERTILIZER_MAP_START = "soil-to-fertilizer map:";
    private static final String FERTILIZER_TO_WATER_MAP_START = "fertilizer-to-water map:";
    private static final String WATER_TO_LIGHT_MAP_START = "water-to-light map:";
    private static final String LIGHT_TO_TEMPERATURE_MAP_START = "light-to-temperature map:";
    private static final String TEMPERATURE_TO_HUMIDITY_MAP_START = "temperature-to-humidity map:";
    private static final String HUMIDITY_TO_LOCATION_MAP_START = "humidity-to-location map:";

    private final List<Seed> seeds;
    private final List<RangeMap<Long>> mappings;


    private SeedMapping(@NotNull List<Seed> seeds, @NotNull List<RangeMap<Long>> mappings) {
        this.seeds = new ArrayList<>(seeds);
        this.mappings = new ArrayList<>(mappings);
    }

    /**
     * Parses the Seeds and mapping from the specified List of input Strings and returns a new
     * SeedMapping that has the parsed Seeds and mappings already populated.
     *
     * @param input The Seeds and mappings to parse.
     * @return A new SeedMapping instance loaded with the Seeds and mappings from the List of input Strings.
     */
    public static @NotNull SeedMapping buildSeedMapping(@NotNull List<String> input) {
        var seedToSoilMap = new RangeMap<Long>(SEED_TO_SOIL_MAP_START);
        var soilToFertilizerMap = new RangeMap<Long>(SOIL_TO_FERTILIZER_MAP_START);
        var fertilizerToWaterMap = new RangeMap<Long>(FERTILIZER_TO_WATER_MAP_START);
        var waterToLightMap = new RangeMap<Long>(WATER_TO_LIGHT_MAP_START);
        var lightToTemperatureMap = new RangeMap<Long>(LIGHT_TO_TEMPERATURE_MAP_START);
        var temperatureToHumidityMap = new RangeMap<Long>(TEMPERATURE_TO_HUMIDITY_MAP_START);
        var humidityToLocationMap = new RangeMap<Long>(HUMIDITY_TO_LOCATION_MAP_START);
        var processing = new AtomicReference<>("");
        final List<Seed> seeds = new ArrayList<>();
        final List<RangeMap<Long>> mappings = new ArrayList<>();

        mappings.add(seedToSoilMap);
        mappings.add(soilToFertilizerMap);
        mappings.add(fertilizerToWaterMap);
        mappings.add(waterToLightMap);
        mappings.add(lightToTemperatureMap);
        mappings.add(temperatureToHumidityMap);
        mappings.add(humidityToLocationMap);

        input.forEach(it -> {
            // Process the line of input
            if (it.startsWith(SEEDS_START)) {
                processing.set(SEEDS_START);
                parseSeeds(it, seeds);
            } else if (it.startsWith(SEED_TO_SOIL_MAP_START) || processing.get().startsWith(SEED_TO_SOIL_MAP_START)) {
                processing.set(SEED_TO_SOIL_MAP_START);
                if (it.isEmpty()) {
                    processing.set("");
                } else {
                    parseMapLine(it, seedToSoilMap);
                }
            } else if (it.startsWith(SOIL_TO_FERTILIZER_MAP_START) || processing.get().startsWith(SOIL_TO_FERTILIZER_MAP_START)) {
                processing.set(SOIL_TO_FERTILIZER_MAP_START);
                if (it.isEmpty()) {
                    processing.set("");
                } else {
                    parseMapLine(it, soilToFertilizerMap);
                }
            } else if (it.startsWith(FERTILIZER_TO_WATER_MAP_START) || processing.get().startsWith(FERTILIZER_TO_WATER_MAP_START)) {
                processing.set(FERTILIZER_TO_WATER_MAP_START);
                if (it.isEmpty()) {
                    processing.set("");
                } else {
                    parseMapLine(it, fertilizerToWaterMap);
                }
            } else if (it.startsWith(WATER_TO_LIGHT_MAP_START) || processing.get().startsWith(WATER_TO_LIGHT_MAP_START)) {
                processing.set(WATER_TO_LIGHT_MAP_START);
                if (it.isEmpty()) {
                    processing.set("");
                } else {
                    parseMapLine(it, waterToLightMap);
                }
            } else if (it.startsWith(LIGHT_TO_TEMPERATURE_MAP_START) || processing.get().startsWith(LIGHT_TO_TEMPERATURE_MAP_START)) {
                processing.set(LIGHT_TO_TEMPERATURE_MAP_START);
                if (it.isEmpty()) {
                    processing.set("");
                } else {
                    parseMapLine(it, lightToTemperatureMap);
                }
            } else if (it.startsWith(TEMPERATURE_TO_HUMIDITY_MAP_START) || processing.get().startsWith(TEMPERATURE_TO_HUMIDITY_MAP_START)) {
                processing.set(TEMPERATURE_TO_HUMIDITY_MAP_START);
                if (it.isEmpty()) {
                    processing.set("");
                } else {
                    parseMapLine(it, temperatureToHumidityMap);
                }
            } else if (it.startsWith(HUMIDITY_TO_LOCATION_MAP_START) || processing.get().startsWith(HUMIDITY_TO_LOCATION_MAP_START)) {
                processing.set(HUMIDITY_TO_LOCATION_MAP_START);
                if (it.isEmpty()) {
                    processing.set("");
                } else {
                    parseMapLine(it, humidityToLocationMap);
                }
            }
        });
        processing.set("");

        seeds.forEach(seed -> populateSeedFromMaps(
                mappings,
                seed
        ));

        return new SeedMapping(seeds, mappings);
    }

    /**
     * Returns an unmodifiable List of the Seeds in this instance.
     *
     * @return An unmodifiable List of the Seeds in this instance.
     */
    public List<Seed> getSeeds() {
        return Collections.unmodifiableList(seeds);
    }

    /**
     * Assumes the Seeds represent ranges where every even Seed is the start of a new
     * Range and every odd Seed is the extent of the Range. Calculates the Seed with the
     * lowest Location ID by using the calculated Seed based Ranges. The returns Seed will
     * contain the lowest location ID if it was able to calculate.
     *
     * @return An Optional Seed that contains the lowest location ID; if present!.
     */
    public Optional<Seed> calculateMinimumSeedLocation() {
        var seedRanges = new ArrayList<Range<Long>>();

        for (int i = 0; i + 1 < seeds.size(); i += 2) {
            var first = seeds.get(i);
            var second = seeds.get(i + 1);
            seedRanges.add(Range.from(first.getId(), second.getId()));
        }

        List<Range<Long>> minimumRanges = new ArrayList<>();

        for (var seedRange : seedRanges) {
            List<Range<Long>> ranges = new ArrayList<>();
            ranges.add(seedRange);
            for (var map : mappings) {
                ranges = map.get(ranges);
            }
            var optional = ranges.stream().min(Comparator.comparing(Range<Long>::start));

            optional.ifPresent(minimumRanges::add);
        }

        var optional = minimumRanges.stream().min(Comparator.comparing(Range<Long>::start));

        if (optional.isPresent()) {
            var minimumSeed = new Seed(-1L);
            minimumSeed.setLocationId(optional.get().start());
            return Optional.of(minimumSeed);
        } else {
            return Optional.empty();
        }
    }

    private static void populateSeedFromMaps(
            List<RangeMap<Long>> maps,
            Seed seed
    ) {
        seed.setSoilId(maps.get(0).get(seed.getId()));
        seed.setFertilizerId(maps.get(1).get(seed.getSoilId()));
        seed.setWaterId(maps.get(2).get(seed.getFertilizerId()));
        seed.setLightId(maps.get(3).get(seed.getWaterId()));
        seed.setTemperatureId(maps.get(4).get(seed.getLightId()));
        seed.setHumidityId(maps.get(5).get(seed.getTemperatureId()));
        seed.setLocationId(maps.get(6).get(seed.getHumidityId()));
    }

    private static void parseSeeds(String input, List<Seed> seeds) {
        if (input == null || input.isEmpty()) {
            return;
        }

        String split = input.split(SEEDS_SPLIT)[1];
        String[] values = split.split(VALUES_SPLIT);

        Arrays.stream(values)
                .map(it -> new Seed(Long.parseLong(it.trim())))
                .forEach(seeds::add);
    }

    private static void parseMapLine(String input, RangeMap<Long> rangeMap) {
        if (input == null || input.isEmpty() || input.startsWith(rangeMap.id())) {
            return;
        }

        String[] values = input.split(VALUES_SPLIT);
        rangeMap.add(Long.parseLong(values[0]), Long.parseLong(values[1]), Long.parseLong(values[2]));
    }
}
