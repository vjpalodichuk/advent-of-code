package com.capital7software.aoc2023.days;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;

public class Day15 {
    private record HashString(String string) {

        public int hashAscii() {
                var characters = string.getBytes(StandardCharsets.US_ASCII);

                var hash = 0L;

                for (var character : characters) {
                    hash += character;
                    hash *= 17;
                    hash %= 256;
                }

                return (int) hash;
            }

            public static List<HashString> parse(Stream<String> stream) {
                var builder = new StringBuilder();

                stream.forEach(builder::append);

                var temp = builder.toString().replace("\n", "");
                return Arrays.stream(temp.split(",")).map(HashString::new).toList();
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof HashString that)) return false;
                return string.equals(that.string);
            }

    }

    private static class Lens{
        private final HashString label;
        private int focalLength;

        public Lens(HashString label, int focalLength) {
            this.label = label;
            this.focalLength = focalLength;
        }

        public Lens(String label) {
            this(new HashString(label), -1);
        }

        public void setFocalLength(int focalLength) {
            this.focalLength = focalLength;
        }

        public int hashAscii() {
            return label.hashAscii();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Lens lens)) return false;
            return label.equals(lens.label);
        }

        @Override
        public int hashCode() {
            return Objects.hash(label);
        }
    }

    private static class LensBox {
        private final int id;
        private final LinkedList<Lens> lenses;

        public LensBox(int id, LinkedList<Lens> lenses) {
            this.id = id;
            this.lenses = lenses;
        }

        public LensBox(int id) {
            this(id, new LinkedList<>());
        }

        public long focusingPower() {
            if (lenses.isEmpty()) {
                return 0;
            }

            var power = 0L;
            int count = 1;
            for (var lens : lenses) {
                power += (long) (id + 1) * count * lens.focalLength;
                count++;
            }

            return power;
        }

        public void remove(Lens lens) {
            lenses.remove(lens);
        }

        public void addOrReplace(Lens lens) {
            int index = lenses.indexOf(lens);

            if (index == -1) {
                lenses.add(lens);
            } else {
                lenses.set(index, lens);
            }
        }
    }

    private static class LensLibrary {
        private static final String SPLIT_REGEX = "[-=]";
        private final Map<Integer, LensBox> library = new HashMap<>();

        public void processLensString(String lensString) {
            var split = lensString.split(SPLIT_REGEX);
            var lens = new Lens(split[0]);
            var boxId = lens.hashAscii();
            var box = library.computeIfAbsent(boxId, LensBox::new);

            if (split.length == 1) {
                // Remove an existing lens from the box.
                box.remove(lens);
            } else if (split.length == 2) {
                lens.setFocalLength(Integer.parseInt(split[1]));
                box.addOrReplace(lens);
            } else {
                throw new RuntimeException("Unable to process lensString: " + lensString);
            }
        }

        public long focusingPower() {
            return library.values().stream().mapToLong(LensBox::focusingPower).sum();
        }

        public static LensLibrary parse(Stream<String> stream) {
            var library = new LensLibrary();

            var builder = new StringBuilder();

            stream.forEach(builder::append);

            var temp = builder.toString().replace("\n", "");
            Arrays.stream(temp.split(","))
                    .map(String::new)
                    .forEach(library::processLensString);

            return library;
        }
    }

    private static final String inputFilename = "inputs/input_day_15-01.txt";

    public static void main(String[] args) throws URISyntaxException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        var url = classloader.getResource(inputFilename);
        assert url != null;
        var path = Paths.get(url.toURI());
        part1(path);
        part2(path);
    }

    private static void part1(Path path) {
        try (var stream = Files.lines(path)) {
            // Part 1
            System.out.println("Part 1: Start!");
            var strings = HashString.parse(stream);
            var start = Instant.now();
            var sum = strings.stream().mapToInt(HashString::hashAscii).sum();
            var end = Instant.now();
            System.out.println("Total sum of ASCII initialization sequence hashes: " + sum + " in " +
                    Duration.between(start, end).toNanos() + " ns");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void part2(Path path) {
        try (var stream = Files.lines(path)) {
            // Part 2
            System.out.println("Part 2: Start!");
            var start = Instant.now();
            var library = LensLibrary.parse(stream);
            var focusingPower = library.focusingPower();
            var end = Instant.now();
            System.out.println("Total focusing power of the library is: " + focusingPower + " in " +
                    Duration.between(start, end).toNanos() + " ns");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
