package com.capital7software.aoc2023.days;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Day01 {
    private static final String inputFilename = "inputs/input_day_01-01.txt";
    private static final String ZERO = "0";
    private static final HashMap<String, Character> DIGITS_MAP = new HashMap<>() {{
        put("one", '1');
        put("two", '2');
        put("three", '3');
        put("four", '4');
        put("five", '5');
        put("six", '6');
        put("seven", '7');
        put("eight", '8');
        put("nine", '9');
    }};
    public static void main(String[] args) throws URISyntaxException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        var url = classloader.getResource(inputFilename);
        assert url != null;
        var path = Paths.get(url.toURI());
        // Open the input file and get a handle to the input stream
        try (var stream = Files.lines(path)) {
            var results = new ArrayList<String>();
            stream.forEach(it -> {
                // Process the line of input
                var result = findFirstAndLastDigits(it);

                if (result.length() == 2) {
                    results.add(result);
                } else {
                    System.out.println("Unexpected result: " + result);
                    results.add(ZERO);
                }
            });
            System.out.println("Processed " + results.size() + " lines");

            var sum = results
                    .stream()
                    .mapToInt(it -> {
                        System.out.println(it);
                        return Integer.parseInt(it);
                    })
                    .sum();

            System.out.println("Summation of all lines is: " + sum);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String findFirstAndLastDigits(String input) {
        if (input == null || input.isEmpty()) {
            return ZERO;
        }

        var results = stringToDigits(input);
        char first = results.get(0), last = results.get(results.size() - 1);

        return "" + first + last;
    }

    private static List<Character> stringToDigits(String input) {
        var results = new ArrayList<Character>();
        var chars = input.toCharArray();
        var keys = DIGITS_MAP.keySet().stream().toList();
        for (int i = 0; i < chars.length; i++) {
            if (Character.isDigit(chars[i])) {
                results.add(chars[i]);
            } else {
                var sub = input.substring(i);
                for (String key : keys) {
                    if (sub.startsWith(key)) {
                        results.add(DIGITS_MAP.get(key));
                        break;
                    }
                }
            }
        }

        return results;
    }
}
