package com.capital7software.aoc.lib.util;

import com.capital7software.aoc.lib.math.MathOperations;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * The RangeMap can be used to quickly and easily calculate mappings between multiple source and destination
 * Ranges of numbers.
 *
 * @param id The unique ID of this RangeMap
 * @param ranges The List of source and destination Ranges.
 * @param <T> The type of the value held in the Ranges.
 */
public record RangeMap<T extends Number & Comparable<T>>(
        @NotNull String id,
        @NotNull List<Entry<T>> ranges
) {
    /**
     * An entry in the RangeMap for a source and destination Range pair of the same size.
     *
     * @param source      The source Range of this Entry.
     * @param destination The destination Range of this Entry.
     * @param <T>         The type of the value in the Ranges.
     */
    public record Entry<T extends Number & Comparable<T>>(
            @NotNull Range<T> source,
            @NotNull Range<T> destination
    ) {

        /**
         * Instantiates a new Entry and creates the Ranges based on the
         * specified values.
         *
         * @param destinationStart The start of the destination Range.
         * @param sourceStart      The start of the source Range.
         * @param extent           The extent of both Ranges; used to calculate
         *                         the end of the Ranges.
         */
        public Entry(@NotNull T destinationStart, @NotNull T sourceStart, @NotNull T extent) {
            this(
                    Range.from(sourceStart, extent),
                    Range.from(destinationStart, extent)
            );
        }

        /**
         * Returns the destination key based on the specified source key.
         *
         * @param sourceKey The source key we are getting a destination key for.
         * @return The destination key based on the specified source key.
         */
        public @NotNull T get(@NotNull T sourceKey) {
            var diff = source.distanceFromStart(sourceKey);
            return MathOperations.add(destination.start(), diff);
        }

        /**
         * Returns true if the specified sourceKey is within the source Range of
         * this Entry.
         *
         * @param sourceKey The key to check.
         * @return True if the specified sourceKey is within the source Range of
         * this Entry.
         */
        public boolean containsKey(@NotNull T sourceKey) {
            return source.contains(sourceKey);
        }
    }

    /**
     * Instantiates a new RangeMap with the specified ID and entries.
     * 
     * @param id The ID of this RangeMap.
     * @param ranges The entries of this RangeMap.
     */
    public RangeMap(@NotNull String id, @NotNull List<Entry<T>> ranges) {
        this.id = id;
        this.ranges = new ArrayList<>(ranges);
    }

    /**
     * Instantiates a new RangeMap with the specified ID and no entries.
     * 
     * @param id The ID of this RangeMap.
     */
    public RangeMap(@NotNull String id) {
        this(id, new ArrayList<>());
    }

    /**
     * Returns an unmodifiable List of the entries in this RangeMap.
     * 
     * @return An unmodifiable List of the entries in this RangeMap.
     */
    public @NotNull List<Entry<T>> ranges() {
        return Collections.unmodifiableList(ranges);
    }

    /**
     * Adds a new Range to this RangeMap with the specified values.
     * 
     * @param destinationStart The start of the destination Range.
     * @param sourceStart The start of the source Range.
     * @param extent The extent of both Ranges; used to calculate the end.
     * @return A new Range to this RangeMap with the specified values.
     */
    public boolean add(@NotNull T destinationStart, @NotNull T sourceStart, @NotNull T extent) {
        return ranges.add(new Entry<>(destinationStart, sourceStart, extent));
    }

    /**
     * Returns the destination key for the specified source key. 
     * Tf no specific mapping exists, then the source and 
     * destination keys are the same.
     * 
     * @param sourceKey The source Range key.
     * @return The destination key for the specified source key. 
     * Tf no specific mapping exists, then the source and
     * destination keys are the same.
     */
    public @NotNull T get(@NotNull T sourceKey) {
        Optional<Entry<T>> optional = ranges
                .stream()
                .filter(it -> it.containsKey(sourceKey))
                .findFirst();

        // If no specific mapping then the source and destination keys are the same
        return optional
                .map(entry -> entry.get(sourceKey))
                .orElse(sourceKey);
    }

    /**
     * Returns a (possibly empty) List of destination Ranges that satisfy the source input Ranges.
     *
     * @param sourceRanges The list of source Ranges to process.
     * @return A (possibly empty) List of destination Ranges that satisfy the source input Ranges.
     */
    public @NotNull List<Range<T>> get(@NotNull List<Range<T>> sourceRanges) {
        var results = new ArrayList<Range<T>>();
        var sources = new ArrayList<>(sourceRanges);

        for (var entry : ranges) {
            var tempSources = new ArrayList<Range<T>>();
            var entryDestStart = entry.destination.start();
            var entrySourceStart = entry.source.start();
            var entrySourceEnd = entry.source.end();

            while (!sources.isEmpty()) {
                var sourceRange = sources.removeFirst();
                var sourceStart = sourceRange.start();
                var sourceEnd = sourceRange.end();

                // Calculate a before, intersection, and after range. Please note that these ranges are not
                // mutually exclusive and therefore may overlap!
                var beforeRange = new Range<>(
                        sourceStart,
                        entrySourceStart.compareTo(sourceEnd) <= 0 ? entrySourceStart : sourceEnd
                );
                var intersectionRange = new Range<>(
                        entrySourceStart.compareTo(sourceStart) > 0 ? entrySourceStart : sourceStart,
                        entrySourceEnd.compareTo(sourceEnd) <= 0 ? entrySourceEnd : sourceEnd
                );
                var afterRange = new Range<>(
                        entrySourceEnd.compareTo(sourceStart) > 0 ? entrySourceEnd : sourceStart,
                        sourceEnd
                );

                // If the beforeRange is not empty, then we need to ensure that we
                // keep it when processing the next range
                if (beforeRange.isNotEmpty()) {
                    tempSources.add(beforeRange);
                }
                // If the intersectionRange is not empty, then we will calculate and add the
                // corresponding destination range to the results
                if (intersectionRange.isNotEmpty()) {
                    results.add(new Range<>(
                            MathOperations.add(
                                    MathOperations.subtract(
                                            intersectionRange.start(), entrySourceStart
                                    ),
                                    entryDestStart
                            ),
                            MathOperations.add(
                                    MathOperations.subtract(
                                            intersectionRange.end(), entrySourceStart
                                    ),
                                    entryDestStart
                            )
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
}
