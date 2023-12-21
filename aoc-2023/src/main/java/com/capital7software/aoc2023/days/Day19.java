package com.capital7software.aoc2023.days;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Day19 {
    private record Part(int x, int m, int a, int s) {
        public long sum() {
            return x + m + a + s;
        }
    }

    private record WorkflowResult(Part part, String queueOrWorkflow) {
    }

    private static class Workflow implements Supplier<WorkflowResult> {
        private final String id;
        private final WorkflowRule rules;
        private final ConcurrentLinkedDeque<Part> parts;

        public Workflow(String id, WorkflowRule rules) {
            this.id = id;
            this.rules = rules;
            this.parts = new ConcurrentLinkedDeque<>();
        }

        void offer(Part part) {
            parts.offer(part);
        }

        boolean hasWork() {
            return !parts.isEmpty();
        }

        @Override
        public WorkflowResult get() {
            if (!hasWork()) {
                return null;
            }

            return rules.apply(parts.poll());
        }
    }

    private record WorkflowRule(List<WorkflowPredicate> workflowPredicates) implements Function<Part, WorkflowResult> {
        private record WorkflowPredicate(Predicate<Part> predicate, String output, String propertyCode, String predicateCode, int predicateValue) { }

        @Override
        public WorkflowResult apply(Part part) {
            WorkflowResult workflowResult = null;

            for (var predicate : workflowPredicates) {
                if (predicate.predicate != null) {
                    if (predicate.predicate.test(part)) {
                        workflowResult = new WorkflowResult(part, predicate.output);
                        break;
                    }
                } else {
                    workflowResult = new WorkflowResult(part, predicate.output);
                    break;
                }
            }

            return workflowResult;
        }

        public static WorkflowRule build(String workflowRule) {
            if (workflowRule == null || workflowRule.isBlank()) {
                return null;
            }

            var stringRules = workflowRule.replace("{", "").replace("}", "").split(",");
            var predicates = new LinkedList<WorkflowPredicate>();
            for (var stringRule : stringRules) {
                var predicateAndResult = stringRule.split(":");

                // If it doesn't contain a :, then it is just the output
                if (predicateAndResult.length == 1) {
                    predicates.add(new WorkflowPredicate(null, predicateAndResult[0], "", "", 0));
                    break;
                }
                // Else we have to process this rule somehow!
                var output = predicateAndResult[1];
                var predicateString = predicateAndResult[0];
                var propertyCode = predicateString.substring(0, 1);
                var predicateCode = predicateString.substring(1, 2);
                var partFunction = getPartFunction(propertyCode);
                var predicateValue = Integer.parseInt(predicateString.substring(2));

                if (predicateCode.equals("<")) {
                    predicates.add(new WorkflowPredicate(part -> partFunction.apply(part) < predicateValue, output, propertyCode, predicateCode, predicateValue));
                } else {
                    predicates.add(new WorkflowPredicate(part -> partFunction.apply(part) > predicateValue, output, propertyCode, predicateCode, predicateValue));
                }
            }

            return new WorkflowRule(predicates);
        }

        public static Function<Part, Integer> getPartFunction(String property) {
            switch (property) {
                case "a" -> {
                    return Part::a;
                }
                case "m" -> {
                    return Part::m;
                }
                case "s" -> {
                    return Part::s;
                }
                case "x" -> {
                    return Part::x;
                }
                default -> throw new RuntimeException("Unknown Part property: " + property);
            }
        }
    }

    private static class WorkflowManager {
        private final ConcurrentLinkedDeque<Part> accepted;
        private final ConcurrentLinkedDeque<Part> rejected;
        private final Map<String, Workflow> workflowMap;

        private final ConcurrentLinkedDeque<Part> parts;
        private WorkflowManager(Map<String, Workflow> workflowMap, ConcurrentLinkedDeque<Part> parts) {
            this.workflowMap = workflowMap;
            this.parts = parts;
            this.accepted = new ConcurrentLinkedDeque<>();
            this.rejected = new ConcurrentLinkedDeque<>();
        }

        public boolean hasPartsToProcess() {
            return !parts.isEmpty();
        }

        public boolean workflowsHaveWorkToProcess() {
            return workflowMap.values().stream().anyMatch(Workflow::hasWork);
        }

        public static WorkflowManager build(Stream<String> stream) {
            var processWorkflows = new AtomicBoolean(true);
            var workflowMap = new HashMap<String, Workflow>();
            var parts = new ConcurrentLinkedDeque<Part>();

            stream.forEach(line -> {
                if (processWorkflows.get()) {
                    if (line != null && !line.isBlank()) {
                        var workflow = parseWorkflow(line);

                        if (workflow == null || workflow.id == null || workflow.rules == null) {
                            throw new RuntimeException("Unable to create Workflow from input: " + line);
                        }

                        workflowMap.put(workflow.id, workflow);
                    } else {
                        processWorkflows.set(false);
                    }
                } else {
                    // Time to process the parts
                    var part = parsePart(line);

                    if (part == null) {
                        throw new RuntimeException("Unable to create Part from input: " + line);
                    }

                    parts.offer(part);
                }
            });

            return new WorkflowManager(workflowMap, parts);
        }

        private static Part parsePart(String line) {
            if (line == null || line.isBlank()) {
                return null;
            }

            var split = line.replace("{", "").replace("}", "").split(",");

            return partFromPieces(split);
        }

        private static Part partFromPieces(String[] pieces) {
            int x = 0;
            int m = 0;
            int a = 0;
            int s = 0;

            for (var piece : pieces) {
                var parts = piece.split("=");

                switch (parts[0]) {
                    case "a" -> a = Integer.parseInt(parts[1]);
                    case "m" -> m = Integer.parseInt(parts[1]);
                    case "s" -> s = Integer.parseInt(parts[1]);
                    case "x" -> x = Integer.parseInt(parts[1]);
                }
            }

            return new Part(x, m, a, s);
        }

        private static Workflow parseWorkflow(String line) {
            if (line == null || line.isBlank()) {
                return null;
            }

            var split = line.split("\\{");

            var id = split[0];
            var workflowRule = WorkflowRule.build(split[1]);

            if (id == null || id.isBlank() || workflowRule == null) {
                return null;
            }

            return new Workflow(id, workflowRule);
        }

        public long sumAccepted() {
            return accepted.stream().mapToLong(Part::sum).sum();
        }

        public long sumRejected() {
            return rejected.stream().mapToLong(Part::sum).sum();
        }

        public long calculateCombinations(String startingWorkflow) {
            var xRange = new Day10.Range(1, 4000);
            var mRange = new Day10.Range(1, 4000);
            var sRange = new Day10.Range(1, 4000);
            var aRange = new Day10.Range(1, 4000);
            var workMap = new HashMap<String, Day10.Range>();
            workMap.put("x", xRange);
            workMap.put("m", mRange);
            workMap.put("s", sRange);
            workMap.put("a", aRange);

            var uniqueRanges = calculateCombinations(workMap, startingWorkflow);

            // We sum up the products of the ranges!
            return uniqueRanges.stream()
                    .mapToLong(map -> map.values().stream()
                            .mapToLong(Day10.Range::count)
                            .reduce(1L, (product, sum) -> product * sum))
                    .sum();
        }

        /**
         * Splits this range in two distinct ranges based on the splitPoint.
         * May produce an empty range if splitPoint is outside of this range.
         * If existing range is 1 - 4000 and splitPoint is 2000 then this method will
         * produce two new ranges: 1 - 1999, 2000 - 40000 if high is false. If high is true, then
         * the ranges would be 1 - 2000, 2001 - 4000
         *
         * @param range The range to split
         * @param splitPoint The number to split the range on.
         * @param high Pass true to split on the high end, else the low end.
         * @return A list with two new ranges.
         */
        public List<Day10.Range> split(Day10.Range range, int splitPoint, boolean high) {
            var result = new LinkedList<Day10.Range>();
            var toSplit = high ? splitPoint + 1 : splitPoint;

            result.add(Day10.Range.of(range.low(), Math.min(toSplit - 1, range.high())));
            result.add(Day10.Range.of(Math.max(range.low(), toSplit), range.high()));
            return result;
        }

        private LinkedList<Map<String, Day10.Range>> calculateCombinations(
                Map<String, Day10.Range> ranges,
                String idOrDestination
        ) {
            // Search until we hit a node that ends at A
            // Adjust the ranges as we go
            // If we hit A, we add that range to the list and return from that path
            // We continue until we have checked everything.
            var result = new LinkedList<Map<String, Day10.Range>>();

            // Stopping conditions
            // If any of the ranges are empty, we stop!
            if (ranges.values().stream().anyMatch(Day10.Range::isEmpty)) {
                return result;
            } else if (idOrDestination.equals("A")) {
                // Just accept it!
                result.add(ranges);
                return result;
            } else if (idOrDestination.equals("R")) {
                // Just reject it!
                return result;
            }

            var workflow = workflowMap.get(idOrDestination);

            for (var predicate : workflow.rules.workflowPredicates) {
                if (predicate.predicateCode.equals("<")) {
                    var currentRanges = new HashMap<>(ranges);
                    var currentRange = currentRanges.get(predicate.propertyCode);

                    // Need to split the range!
                    List<Day10.Range> split = split(currentRange, predicate.predicateValue, false);
                    currentRanges.put(predicate.propertyCode, split.get(0)); // low
                    // Continue to recursively process the low half!
                    result.addAll(calculateCombinations(currentRanges, predicate.output));
                    // Continue processing rules with the high half!
                    ranges = new HashMap<>(ranges);
                    ranges.put(predicate.propertyCode, split.get(1)); // high
                } else if (predicate.predicateCode.equals(">")){
                    var currentRanges = new HashMap<>(ranges);
                    var currentRange = currentRanges.get(predicate.propertyCode);
                    // Pretty much the same as above but we add one to the predicateValue to ensure a proper split!
                    List<Day10.Range> split = split(currentRange, predicate.predicateValue, true);
                    currentRanges.put(predicate.propertyCode, split.get(1)); // high
                    // Continue to recursively process the high half!
                    result.addAll(calculateCombinations(currentRanges, predicate.output));
                    // Continue processing rules with the low half!
                    ranges = new HashMap<>(ranges);
                    ranges.put(predicate.propertyCode, split.get(0)); // low
                } else {
                    // Moving to a new workflow
                    result.addAll(calculateCombinations(ranges, predicate.output));
                }
            }

            return result;
        }

        public void run(String startingWorkflow) {
            while (hasPartsToProcess() || workflowsHaveWorkToProcess()) {
                if (hasPartsToProcess()) {
                    var part = parts.poll();

                    workflowMap.get(startingWorkflow).offer(part);
                }

                if (workflowsHaveWorkToProcess()) {
                    workflowMap.values()
                            .stream()
                            .map(Workflow::get)
                            .filter(Objects::nonNull)
                            .forEach(this::processResult);
                }
            }
        }

        private void processResult(WorkflowResult result) {
            if (result == null || result.part == null) {
                throw new RuntimeException("Received a null WorkflowResult or null Part in the result: " + result);
            }

            switch (result.queueOrWorkflow) {
                case "A" -> accepted.offer(result.part);
                case "R" -> rejected.offer(result.part);
                default -> workflowMap.get(result.queueOrWorkflow).offer(result.part);
            }
        }
    }

    private static final String inputFilename = "inputs/input_day_19-01.txt";

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
            var manager = WorkflowManager.build(stream);
            var start = Instant.now();
            manager.run("in");
            var acceptedSum = manager.sumAccepted();
            var rejectedSum = manager.sumRejected();
            var end = Instant.now();
            System.out.println("Sum of rejected parts: " + rejectedSum);
            System.out.println("Sum of accepted parts: " + acceptedSum + " in " +
                    Duration.between(start, end).toNanos() + " ns");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void part2(Path path) {
        try (var stream = Files.lines(path)) {
            // Part 2
            System.out.println("Part 2: Start!");
            var manager = WorkflowManager.build(stream);
            var start = Instant.now();
            var combinations = manager.calculateCombinations("in");
            var end = Instant.now();
            System.out.println("Total possible combinations: " + combinations + " in " +
                    Duration.between(start, end).toNanos() + " ns");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
