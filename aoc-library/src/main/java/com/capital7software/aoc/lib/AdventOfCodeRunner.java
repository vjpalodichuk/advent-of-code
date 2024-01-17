package com.capital7software.aoc.lib;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

/**
 * Utility class for running Advent of Code Solutions.
 */
public abstract class AdventOfCodeRunner {
    private static final Logger LOGGER = Logger.getLogger(AdventOfCodeRunner.class.getName());

    private static Path path;

    /**
     * Instantiates a new instance of the runner.
     */
    public AdventOfCodeRunner() {

    }

    /**
     * Prints out usage information is invalid command line args are passed.
     */
    protected static void printUsage() {
        LOGGER.info("The first required argument is the name of the puzzle class to run.");
        LOGGER.info("Only the class name (and not the package) should be provided.");
        LOGGER.info("The second optional argument is the full path to a file to load as the input.");
        LOGGER.info("If you do not pass a file name the default input file will be used for that puzzle.");
    }

    /**
     * Run an AdventOfCodeSolution Class.
     *
     * @param packageName THe package name that contains the solution classes.
     * @param args The command-line arguments.
     */
    protected static void runSolution(@NotNull String packageName, String[] args) {
        if (args.length == 0 || args[0] == null || args[0].trim().isBlank()) {
            printUsage();
            return;
        }

        var className = packageName + args[0].trim();

        try {
            var clazz = Class.forName(className);

            var constructor = clazz.getConstructor();

            var instance = constructor.newInstance();

            if (instance instanceof AdventOfCodeSolution solution) {
                try {
                    String inputFilename;
                    if (args.length > 1 && args[1] != null && !args[1].trim().isBlank()) {
                        if (!Files.exists(Paths.get(args[1].trim()))) {
                            LOGGER.info(String.format("%s cannot be found!", args[1]));
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

                    LOGGER.info(String.format("Loading input data from: %s", path));
                    List<String> inputLines = Files.readAllLines(path);

                    // Part 1
                    LOGGER.info("Part 1: Start!");
                    solution.runPart1(inputLines);
                    // Part 2
                    LOGGER.info("Part 2: Start!");
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
}
