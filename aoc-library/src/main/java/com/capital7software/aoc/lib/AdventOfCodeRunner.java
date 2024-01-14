package com.capital7software.aoc.lib;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class AdventOfCodeRunner {
    private static Path path;

    public static void main(String @NotNull [] args) {
        if (args.length == 0 || args[0] == null || args[0].trim().isBlank()) {
            printUsage();
            return;
        }

        var className = "com.capital7software.aoc2015.days." + args[0].trim();

        try {
            var clazz = Class.forName(className);

            var constructor = clazz.getConstructor();

            var instance = constructor.newInstance();

            if (instance instanceof AdventOfCodeSolution solution) {
                try {
                    String inputFilename;
                    if (args.length > 1 && args[1] != null && !args[1].trim().isBlank()) {
                        if (!Files.exists(Paths.get(args[1].trim()))) {
                            System.out.println(args[1] + " cannot be found!");
                            printUsage();
                        } else {
                            inputFilename = args[1].trim();
                            path = Paths.get(inputFilename);
                        }
                    } else {
                        inputFilename = solution.getDefaultInputFilename();
                        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
                        var url = classloader.getResource(inputFilename);
                        assert url != null;
                        path = Paths.get(url.toURI());
                    }
                    System.out.println("Loading input data from: " + path);
                    List<String> inputLines = Files.readAllLines(path);
                    // Part 1
                    System.out.println("Part 1: Start!");
                    solution.runPart1(inputLines);
                    System.out.println("Part 2: Start!");
                    solution.runPart2(inputLines);
                } catch (URISyntaxException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static void printUsage() {
        System.out.println("The first required argument is the name of the puzzle class to run.");
        System.out.println("Only the class name (and not the package) should be provided.");
        System.out.println("The second optional argument is the full path to a file to load as the input.");
        System.out.println("If you do not pass a file name the default input file will be used for that puzzle.");
    }
}
