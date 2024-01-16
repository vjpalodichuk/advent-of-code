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
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Day06 {
    private static final String inputFilename = "inputs/input_day_06-01.txt";
    private static final String FIRST_SPLIT = ": ";
    private static final String VALUE_SPLIT = " ";

    public static void main(String[] args) throws URISyntaxException {
        List<String> sTimes = new ArrayList<>();
        List<String> sDistances = new ArrayList<>();
        AtomicBoolean loadTimes = new AtomicBoolean(true);

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        var url = classloader.getResource(inputFilename);
        assert url != null;
        var path = Paths.get(url.toURI());

        try (var stream = Files.lines(path)) {
            stream.forEach(it -> {
                // Process the line of input
                if (loadTimes.get()) {
                    sTimes.addAll(Arrays
                            .stream(it.split(FIRST_SPLIT)[1].split(VALUE_SPLIT))
                            .filter(item -> !item.isBlank())
                            .map(String::trim)
                            .toList());
                    loadTimes.set(false);
                } else {
                    sDistances.addAll(Arrays
                            .stream(it.split(FIRST_SPLIT)[1].split(VALUE_SPLIT))
                            .filter(item -> !item.isBlank())
                            .map(String::trim)
                            .toList());
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        var times = sTimes.stream().map(Integer::parseInt).toList();
        var distances = sDistances.stream().map(Integer::parseInt).toList();

        var result = 1;

        var bruteStart = Instant.now();
        for (int i = 0; i < times.size(); i++) {
            var holdTimes = calculateWinningHoldTimes(times.get(i), distances.get(i));
            result *= holdTimes.size();
        }
        var bruteEnd = Instant.now();

        System.out.println("Product of the number of winning hold times per race: " + result);
        System.out.println(("Time to calculate in nanoseconds: " + Duration.between(bruteStart, bruteEnd).toNanos()));

        // Part 2

        long longHoldTimes;
        var time = Long.parseLong(String.join("", sTimes));
        var distance = Long.parseLong(String.join("", sDistances));

        var formStart = Instant.now();
        longHoldTimes = calculateNumberOfWinningHoldTimesFormulaic(time, distance);
        var formFinish = Instant.now();
        System.out.println("Number of ways you can beat the record in this one race using quadratic equation: " + longHoldTimes);
        System.out.println(("Time to calculate in nanoseconds: " + Duration.between(formStart, formFinish).toNanos()));

        var iterStart = Instant.now();
        longHoldTimes = calculateNumberOfWinningHoldTimesIterative(time, distance);
        var iterFinish = Instant.now();
        System.out.println("Number of ways you can beat the record in this one race iteratively: " + longHoldTimes);
        System.out.println(("Time to calculate in nanoseconds: " + Duration.between(iterStart, iterFinish).toNanos()));
    }

    /**
     * For every millisecond the boat button is held, it will travel an additional 1 millimeter per millisecond
     * The distance traveled can be expressed as: distance = holdTime * (raceTime - holdTime)
     * This can be rearranged as: d = rh - h²; 0 = rh -h² - d; 0 = -h² + rh - d
     * Now all we do is change the = to > to switch this to a quadratic inequality: -h² + rh - d > 0
     * And now we can use the quadratic formula to find our min and max times that satisfy the inequality
     * and then the difference between them plus 1 will give us the total number of winning hold times!
     * <p>
     * Remember that the quadratic formula is: (-b ± √(b²-4ac))/(2a)
     * In our case, a = -holdTime, b = raceTime, c = -distance
     * <p>
     * So now we can use the quadratic equation to set up the inequality in terms of the holdTime as follows:
     * (-b - √(b²-4c))/(2) < a < (-b + √(b²-4c))/(2)
     * We can multiply everything by -1 and substitute in our variables to get the following:
     * raceTime - √(raceTime² - (4 * distance)))/(2) < holdTime < raceTime + √(raceTime² - (4 * distance)))/(2)
     * <p>
     * Since we will get Doubles as a result, we take the floor of the upper bound and the ceiling of the lower bound
     * and add one to the difference between the two to get our answer!!
     *
     * @param time The total time in the race
     * @param distance The distance to beat
     * @return The number of different hold / charge times that result in a distance greater than the specified distance
     */
    private static long calculateNumberOfWinningHoldTimesFormulaic(long time, long distance) {
        var root = Math.sqrt((time * time) - (4 * distance));
        var upper = (time + root) / 2;
        var lower = (time - root) / 2;

        return Double.valueOf(Math.floor(upper) - Math.ceil(lower)).longValue() + 1;
    }

    private static long calculateNumberOfWinningHoldTimesIterative(long time, long distance) {
        long earliestHoldTime = 0;
        long latestHoldTime = 0;

        for (long i = 0; i <= time; i++) {
            var distanceTravelled = i * (time - i);

            if (distanceTravelled > distance) {
                earliestHoldTime = i;
                break;
            }
        }

        for (long i = time; i >= 0; i--) {
            var distanceTravelled = i * (time - i);

            if (distanceTravelled > distance) {
                latestHoldTime = i;
                break;
            }
        }

        // Now we have the earliest and latest hold times. All times in between these two numbers (inclusive) are winning
        // hold times.

        return latestHoldTime - earliestHoldTime + 1;
    }

    /**
     * For each millisecond the button is pressed, the boat will go 1 millimeter per millisecond.
     *
     * @param time The amount of time in the race
     * @param distance The record distance to beat
     * @return The hold times that result in a distance greater than the current record
     */
    private static List<Integer> calculateWinningHoldTimes(int time, int distance) {
        var result = new ArrayList<Integer>();

        for (int i = 0; i <= time; i++) {
            var distanceTravelled = i * (time - i);

            if (distanceTravelled > distance) {
                result.add(i);
            }
        }

        return result;
    }
}
