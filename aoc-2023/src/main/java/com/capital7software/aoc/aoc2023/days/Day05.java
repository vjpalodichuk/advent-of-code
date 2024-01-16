package com.capital7software.aoc.aoc2023.days;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Day05 {
    private static class Seed {
        private final Long id;
        private Long soilId;
        private Long fertilizerId;
        private Long waterId;
        private Long lightId;
        private Long temperatureId;
        private Long humidityId;
        private Long locationId;

        public Seed(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }

        public Long getSoilId() {
            return soilId;
        }

        public void setSoilId(Long soilId) {
            this.soilId = soilId;
        }

        public Long getFertilizerId() {
            return fertilizerId;
        }

        public void setFertilizerId(Long fertilizerId) {
            this.fertilizerId = fertilizerId;
        }

        public Long getWaterId() {
            return waterId;
        }

        public void setWaterId(Long waterId) {
            this.waterId = waterId;
        }

        public Long getLightId() {
            return lightId;
        }

        public void setLightId(Long lightId) {
            this.lightId = lightId;
        }

        public Long getTemperatureId() {
            return temperatureId;
        }

        public void setTemperatureId(Long temperatureId) {
            this.temperatureId = temperatureId;
        }

        public Long getHumidityId() {
            return humidityId;
        }

        public void setHumidityId(Long humidityId) {
            this.humidityId = humidityId;
        }

        public Long getLocationId() {
            return locationId;
        }

        public void setLocationId(Long locationId) {
            this.locationId = locationId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
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

    private record LongRange(
            long start,
            long extent
    ) {
        public boolean contains(long value) {
            return value >= start && value < start + extent;
        }

        public long difference(long value) {
            if (!contains(value)) {
                throw new RuntimeException("The specified value must be contained within this range!");
            }

            return value - start;
        }

        /**
         *
         * @return The exclusive end of the range.
         */
        public long getEnd() {
            return start + extent;
        }

        /**
         *
         * @param start The inclusive starting value; must be less than or equal to ending value
         * @param end The exclusive ending value
         * @return A new LongRange
         */
        public static LongRange from(long start, long end) {
            return new LongRange(start, end - start);
        }

        public boolean isNotEmpty() {
            return getEnd() > start();
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            LongRange longRange = (LongRange) o;
            return start == longRange.start && extent == longRange.extent;
        }

        @Override
        public int hashCode() {
            return Objects.hash(start, extent);
        }

        @Override
        public String toString() {
            return "IntRange{" +
                    "start=" + start +
                    ", end=" + getEnd() +
                    ", extent=" + extent +
                    '}';
        }
    }

    private static class LongRangeMap {
        private static class Entry {
            private final LongRange destination;
            private final LongRange source;

            public Entry(long destinationStart, long sourceStart, long extent) {
                this.destination = new LongRange(destinationStart, extent);
                this.source = new LongRange(sourceStart, extent);
            }

            public long get(long sourceKey) {
                var diff = source.difference(sourceKey);
                return destination.start() + diff;
            }

            public boolean containsKey(long sourceKey) {
                return source.contains(sourceKey);
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Entry entry = (Entry) o;
                return destination.equals(entry.destination) && source.equals(entry.source);
            }

            @Override
            public int hashCode() {
                return Objects.hash(destination, source);
            }

            @Override
            public String toString() {
                return "Entry{" +
                        "destination=" + destination +
                        ", source=" + source +
                        '}';
            }
        }

        private final String id;
        private final List<Entry> ranges = new ArrayList<>();

        private LongRangeMap(String id) {
            this.id = id;
        }

        public void addRange(long destinationStart, long sourceStart, long extent) {
            ranges.add(new Entry(destinationStart, sourceStart, extent));
        }

        public long get(long sourceKey) {
            Optional<Entry> optional = ranges.stream().filter(it -> it.containsKey(sourceKey)).findFirst();

            // If no specific mapping then the source and destination keys are the same
            return optional.map(entry -> entry.get(sourceKey)).orElse(sourceKey);
        }

        /**
         *
         * @param sourceRanges The list of source ranges to process
         * @return A (possibly empty) List of LongRange destination ranges that satisfy the source input ranges.
         */
        public List<LongRange> get(List<LongRange> sourceRanges) {
            var results = new ArrayList<LongRange>();
            var sources = new ArrayList<>(sourceRanges);

            for (var entry : ranges) {
                var tempSources = new ArrayList<LongRange>();
                var entryDestStart = entry.destination.start();
                var entrySourceStart = entry.source.start();
                var entrySourceEnd = entry.source.getEnd();

                while (!sources.isEmpty()) {
                    var sourceRange = sources.remove(0);
                    var sourceStart = sourceRange.start();
                    var sourceEnd = sourceRange.getEnd();

                    // Calculate a before, intersection, and after range. Please note that these ranges are not
                    // mutually exclusive and therefore may overlap!
                    var beforeRange = LongRange.from(sourceStart, Math.min(entrySourceStart, sourceEnd));
                    var intersectionRange = LongRange.from(Math.max(sourceStart, entrySourceStart), Math.min(entrySourceEnd, sourceEnd));
                    var afterRange = LongRange.from(Math.max(entrySourceEnd, sourceStart), sourceEnd);

                    // If the beforeRange is not empty, then we need to ensure that we
                    // keep it when processing the next range
                    if (beforeRange.isNotEmpty()) {
                        tempSources.add(beforeRange);
                    }
                    // If the intersectionRange is not empty, then we will calculate and add the
                    // corresponding destination range to the results
                    if (intersectionRange.isNotEmpty()) {
                        results.add(LongRange.from(
                                intersectionRange.start() - entrySourceStart + entryDestStart,
                                intersectionRange.getEnd() - entrySourceStart + entryDestStart
                        ));
                    }
                    // If the afterRange is not empty, then we need to ensure that we keep it when processing the
                    // next range
                    if (afterRange.isNotEmpty()) {
                        tempSources.add(afterRange);
                    }
                }
                sources = tempSources;
            }
            results.addAll(sources);
            return results;
        }

        @Override
        public String toString() {
            return "IntRangeMap{" +
                    "id='" + id + '\'' +
                    ", ranges=" + ranges +
                    '}';
        }
    }
    private static final String inputFilename = "inputs/input_day_05-01.txt";

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

    public static void main(String[] args) throws URISyntaxException {
        var seeds = new ArrayList<Seed>();
        var seedToSoilMap = new LongRangeMap(SEED_TO_SOIL_MAP_START);
        var soilToFertilizerMap = new LongRangeMap(SOIL_TO_FERTILIZER_MAP_START);
        var fertilizerToWaterMap = new LongRangeMap(FERTILIZER_TO_WATER_MAP_START);
        var waterToLightMap = new LongRangeMap(WATER_TO_LIGHT_MAP_START);
        var lightToTemperatureMap = new LongRangeMap(LIGHT_TO_TEMPERATURE_MAP_START);
        var temperatureToHumidityMap = new LongRangeMap(TEMPERATURE_TO_HUMIDITY_MAP_START);
        var humidityToLocationMap = new LongRangeMap(HUMIDITY_TO_LOCATION_MAP_START);
        var processing = new AtomicReference<>("");

        var maps = new ArrayList<LongRangeMap>();
        maps.add(seedToSoilMap);
        maps.add(soilToFertilizerMap);
        maps.add(fertilizerToWaterMap);
        maps.add(waterToLightMap);
        maps.add(lightToTemperatureMap);
        maps.add(temperatureToHumidityMap);
        maps.add(humidityToLocationMap);

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        var url = classloader.getResource(inputFilename);
        assert url != null;
        var path = Paths.get(url.toURI());

        try (var stream = Files.lines(path)) {
            System.out.println("Loading seeds to plant and associated maps...");

            stream.forEach(it -> {
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

            System.out.println("Processing seeds...");

            seeds.forEach(seed -> populateSeedFromMaps(
                    maps,
                    seed
            ));

            System.out.println("Processed " + seeds.size() + " seeds!");
            System.out.println("Calculating the lowest location number...");

            // Part 1: Each Seed is its own!
            var minimumLocation = seeds
                    .stream()
                    .mapToLong(Seed::getLocationId)
                    .min();

            if (minimumLocation.isPresent()) {
                System.out.println("The lowest location number is: " + minimumLocation.getAsLong());
            } else {
                System.out.println("Unable to determine the lowest location number :-(");
            }

            // Part 2: Each even Seed is the start of a range and each odd Seed is the extent of the range
            System.out.println("Recalculating Seeds and determining the lowest location using ranges...");

            var minimumSeed = calculateSeedsAndFindMinimumRanges(
                    seeds,
                    maps
            );

            if (minimumSeed.isPresent()) {
                System.out.println("The lowest location number is: " + minimumSeed.get().locationId);
            } else {
                System.out.println("Unable to determine the lowest location number :-(");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Optional<Seed> calculateSeedsAndFindMinimumRanges(
            List<Seed> seeds,
            List<LongRangeMap> maps
    ) {
        var seedRanges = new ArrayList<LongRange>();

        for (int i = 0; i + 1 < seeds.size(); i += 2) {
            var first = seeds.get(i);
            var second = seeds.get(i + 1);
            seedRanges.add(new LongRange(first.getId(), second.getId()));
        }

        List<LongRange> minimumRanges = new ArrayList<>();

        for (var seedRange : seedRanges) {
            List<LongRange> ranges = new ArrayList<>();
            ranges.add(seedRange);
            for (var map : maps) {
                ranges = map.get(ranges);
            }
            var optional = ranges.stream().min(Comparator.comparing(LongRange::start));

            optional.ifPresent(minimumRanges::add);
        }

        var optional = minimumRanges.stream().min(Comparator.comparing(LongRange::start));

        if (optional.isPresent()) {
            var minimumSeed = new Seed(-1L);
            minimumSeed.setLocationId(optional.get().start());
            return Optional.of(minimumSeed);
        } else {
            return Optional.empty();
        }
    }

    private static void populateSeedFromMaps(
            List<LongRangeMap> maps,
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

    private static void parseMapLine(String input, LongRangeMap rangeMap) {
        if (input == null || input.isEmpty() || input.startsWith(rangeMap.id)) {
            return;
        }

        String[] values = input.split(VALUES_SPLIT);
        rangeMap.addRange(Long.parseLong(values[0]), Long.parseLong(values[1]), Long.parseLong(values[2]));
    }
}
