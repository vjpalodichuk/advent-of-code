package com.capital7software.aoc.lib.analysis;

import com.capital7software.aoc.lib.util.Range;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

/**
 * To start, each part is rated in each of four categories:
 *
 * <p><br>
 * <code>
 * x: Extremely cool looking<br>
 * m: Musical (it makes a noise when you hit it)<br>
 * a: Aerodynamic<br>
 * s: Shiny<br>
 * </code>
 *
 * <p><br>
 * Then, each part is sent through a series of workflows that will ultimately accept
 * or reject the part. Each workflow has a name and contains a list of rules; each
 * rule specifies a condition and where to send the part if the condition is true.
 * The first rule that matches the part being considered is applied immediately,
 * and the part moves on to the destination described by the rule. (The last rule
 * in each workflow has no condition and always applies if reached.)
 *
 * <p><br>
 * Consider the workflow<br><br>
 * <code>
 * ex{x&#62;10:one,m&#60;20:two,a&#62;30:R,A}<br><br>
 * </code>
 * This workflow is named ex and contains four rules. If workflow ex were considering a specific
 * part, it would perform the following steps in order:
 *
 * <p><br>
 * <ol>
 *     <li>
 *         Rule "x&#62;10:one": If the part's x is more than 10, send the part to the workflow
 *         named one.
 *     </li>
 *     <li>
 *         Rule "m&#60;20:two": Otherwise, if the part's m is less than 20, send the part to the
 *         workflow named two.
 *     </li>
 *     <li>
 *         Rule "a&#62;30:R": Otherwise, if the part's a is more than 30, the part is immediately
 *         rejected (R).
 *     </li>
 *     <li>
 *         Rule "A": Otherwise, because no other rules matched the part, the part is immediately
 *         accepted (A).
 *     </li>
 * </ol>
 * If a part is sent to another workflow, it immediately switches to the start of
 * that workflow instead and never returns.
 * If a part is accepted (sent to A) or rejected (sent to R), the part immediately stops any
 * further processing.
 *
 * <p><br>
 * The system works, but it's not keeping up with the torrent of weird metal shapes.
 * The Elves ask if you can help sort a few parts and give you the list of workflows
 * and some part ratings (your puzzle input). For example:
 *
 * <p><br>
 * <code>
 * px{a&#60;2006:qkq,m&#62;2090:A,rfg}<br>
 * pv{a&#62;1716:R,A}<br>
 * lnx{m&#62;1548:A,A}<br>
 * rfg{s&#60;537:gd,x&#62;2440:R,A}<br>
 * qs{s&#62;3448:A,lnx}<br>
 * qkq{x&#60;1416:A,crn}<br>
 * crn{x&#62;2662:A,R}<br>
 * in{s&#60;1351:px,qqz}<br>
 * qqz{s&#62;2770:qs,m&#60;1801:hdj,R}<br>
 * gd{a&#62;3333:R,R}<br>
 * hdj{m&#62;838:A,pv}<br>
 * </code>
 *
 * <p><br>
 * <code>
 * {x=787,m=2655,a=1222,s=2876}<br>
 * {x=1679,m=44,a=2067,s=496}<br>
 * {x=2036,m=264,a=79,s=2244}<br>
 * {x=2461,m=1339,a=466,s=291}<br>
 * {x=2127,m=1623,a=2188,s=1013}<br>
 * </code>
 *
 * <p><br>
 * The workflows are listed first, followed by a blank line, then the ratings of the
 * parts the Elves would like you to sort. All parts begin in the workflow named in.
 * In this example, the five listed parts go through the following workflows:
 *
 * <p><br>
 * <code>
 * {x=787,m=2655,a=1222,s=2876}: in -> qqz -> qs -> lnx -> A<br>
 * {x=1679,m=44,a=2067,s=496}: in -> px -> rfg -> gd -> R<br>
 * {x=2036,m=264,a=79,s=2244}: in -> qqz -> hdj -> pv -> A<br>
 * {x=2461,m=1339,a=466,s=291}: in -> px -> qkq -> crn -> R<br>
 * {x=2127,m=1623,a=2188,s=1013}: in -> px -> rfg -> A<br>
 * </code>
 *
 * <p><br>
 * Ultimately, three parts are accepted. Adding up the x, m, a, and s rating for each
 * of the accepted parts gives 7540 for the part with x=787, 4623 for the part with x=2036,
 * and 6951 for the part with x=2127. Adding all the ratings for all the accepted
 * parts gives the sum total of 19114.
 *
 * <p><br>
 * Sort through all the parts you've been given; what do you get if you add together all the
 * rating numbers for all the parts that ultimately get accepted?
 *
 * <p><br>
 * Even with your help, the sorting process still isn't fast enough.
 *
 * <p><br>
 * One of the Elves comes up with a new plan: rather than sort parts individually through
 * all of these workflows, maybe you can figure out in advance which combinations of ratings
 * will be accepted or rejected.
 *
 * <p><br>
 * Each of the four ratings (x, m, a, s) can have an integer value ranging from a minimum of
 * 1 to a maximum of 4000. Of all possible distinct combinations of ratings, your job is to
 * figure out which ones will be accepted.
 *
 * <p><br>
 * In the above example, there are 167409079868000 distinct combinations of ratings that
 * will be accepted.
 *
 * <p><br>
 * Consider only your list of workflows; the list of part ratings that the Elves wanted you
 * to sort is no longer relevant. How many distinct combinations of ratings will be
 * accepted by the Elves' workflows?
 */
public class WorkflowManager {
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

      return rules.apply(Objects.requireNonNull(parts.poll()));
    }
  }

  private record WorkflowRule(
      @NotNull List<WorkflowPredicate> workflowPredicates
  ) implements Function<Part, WorkflowResult> {
    private record WorkflowPredicate(
        Predicate<Part> predicate,
        @NotNull String output,
        @NotNull String propertyCode,
        @NotNull String predicateCode,
        int predicateValue
    ) {
    }

    @Override
    public WorkflowResult apply(@NotNull Part part) {
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

    private static WorkflowRule build(String workflowRule) {
      if (workflowRule == null || workflowRule.isBlank()) {
        return null;
      }

      var stringRules = workflowRule
          .replace("{", "").replace("}", "").split(",");
      var predicates = new LinkedList<WorkflowPredicate>();
      for (var stringRule : stringRules) {
        var predicateAndResult = stringRule.split(":");

        // If it doesn't contain a :, then it is just the output
        if (predicateAndResult.length == 1) {
          predicates.add(
              new WorkflowPredicate(
                  null,
                  predicateAndResult[0],
                  "",
                  "",
                  0
              )
          );
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
          predicates.add(
              new WorkflowPredicate(
                  part -> partFunction.apply(part) < predicateValue,
                  output,
                  propertyCode,
                  predicateCode,
                  predicateValue
              )
          );
        } else {
          predicates.add(
              new WorkflowPredicate(
                  part -> partFunction.apply(part) > predicateValue,
                  output,
                  propertyCode,
                  predicateCode,
                  predicateValue
              )
          );
        }
      }

      return new WorkflowRule(predicates);
    }

    public static @NotNull Function<Part, Integer> getPartFunction(@NotNull String property) {
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

  private final ConcurrentLinkedDeque<Part> accepted;
  private final ConcurrentLinkedDeque<Part> rejected;
  private final Map<String, Workflow> workflowMap;

  private final ConcurrentLinkedDeque<Part> parts;

  private WorkflowManager(
      @NotNull Map<String, Workflow> workflowMap,
      @NotNull ConcurrentLinkedDeque<Part> parts
  ) {
    this.workflowMap = workflowMap;
    this.parts = parts;
    this.accepted = new ConcurrentLinkedDeque<>();
    this.rejected = new ConcurrentLinkedDeque<>();
  }

  /**
   * Returns true if this WorkflowManager has parts to process.
   *
   * @return True if this WorkflowManager has parts to process.
   */
  public boolean hasPartsToProcess() {
    return !parts.isEmpty();
  }

  /**
   * Returns true if this WorkflowManager has workflows that have work to process.
   *
   * @return True if this WorkflowManager has workflows that have work to process.
   */
  public boolean workflowsHaveWorkToProcess() {
    return workflowMap.values().stream().anyMatch(Workflow::hasWork);
  }

  /**
   * Builds and returns a new WorkflowManager that is loaded with the workflows and part ratings
   * from the specified List of String.
   *
   * @param input The List of Strings to parse into workflows and part ratings.
   * @return A new WorkflowManager that is loaded with the workflows and part ratings
   *     from the specified List of String.
   */
  public static @NotNull WorkflowManager buildWorkflowManager(List<String> input) {
    var processWorkflows = new AtomicBoolean(true);
    var workflowMap = new HashMap<String, Workflow>();
    var parts = new ConcurrentLinkedDeque<Part>();

    input.forEach(line -> {
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
        default -> throw new RuntimeException("Unknown part rating!");
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

  /**
   * Returns the sum of all the parts that were accepted after running the workflows.
   *
   * @return The sum of all the parts that were accepted after running the workflows.
   */
  public long sumAccepted() {
    return accepted.stream().mapToLong(Part::sum).sum();
  }

  /**
   * Returns the sum of all the parts that were rejected after running the workflows.
   *
   * @return The sum of all the parts that were rejected after running the workflows.
   */
  public long sumRejected() {
    return rejected.stream().mapToLong(Part::sum).sum();
  }

  /**
   * Each of the four ratings (x, m, a, s) can have an integer value ranging
   * from a minimum of 1 to a maximum of 4000. This method calculates and returns the distinct
   * combinations of ratings that will be accepted.
   *
   * @param startingWorkflow The ID of the starting workflow.
   * @return The distinct combinations of ratings that will be accepted.
   */
  public long calculateCombinations(@NotNull String startingWorkflow) {
    var rangeX = new Range<>(1L, 4_000L);
    var rangeM = new Range<>(1L, 4_000L);
    var rangeS = new Range<>(1L, 4_000L);
    var rangeA = new Range<>(1L, 4_000L);
    var workMap = new HashMap<String, Range<Long>>();
    workMap.put("x", rangeX);
    workMap.put("m", rangeM);
    workMap.put("s", rangeS);
    workMap.put("a", rangeA);

    var uniqueRanges = calculateCombinations(workMap, startingWorkflow);

    // We sum up the products of the ranges!
    return uniqueRanges.stream()
        .mapToLong(map -> map.values().stream()
            .mapToLong(Range::sizeInclusive)
            .reduce(1L, (product, sum) -> product * sum))
        .sum();
  }

  private LinkedList<Map<String, Range<Long>>> calculateCombinations(
      @NotNull Map<String, Range<Long>> ranges,
      @NotNull String idOrDestination
  ) {
    // Search until we hit a node that ends at A
    // Adjust the ranges as we go
    // If we hit A, we add that range to the list and return from that path
    // We continue until we have checked everything.
    var result = new LinkedList<Map<String, Range<Long>>>();

    // Stopping conditions
    // If any of the ranges are empty, we stop!
    if (ranges.values().stream().anyMatch(Range::isEmpty)) {
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
        List<Range<Long>> split = Range.split(currentRange, predicate.predicateValue, false);
        currentRanges.put(predicate.propertyCode, split.getFirst()); // low
        // Continue to recursively process the low half!
        result.addAll(calculateCombinations(currentRanges, predicate.output));
        // Continue processing rules with the high half!
        ranges = new HashMap<>(ranges);
        ranges.put(predicate.propertyCode, split.get(1)); // high
      } else if (predicate.predicateCode.equals(">")) {
        var currentRanges = new HashMap<>(ranges);
        var currentRange = currentRanges.get(predicate.propertyCode);
        // Pretty much the same as above but we add one to the predicateValue
        // to ensure a proper split!
        List<Range<Long>> split = Range.split(currentRange, predicate.predicateValue, true);
        currentRanges.put(predicate.propertyCode, split.get(1)); // high
        // Continue to recursively process the high half!
        result.addAll(calculateCombinations(currentRanges, predicate.output));
        // Continue processing rules with the low half!
        ranges = new HashMap<>(ranges);
        ranges.put(predicate.propertyCode, split.getFirst()); // low
      } else {
        // Moving to a new workflow
        result.addAll(calculateCombinations(ranges, predicate.output));
      }
    }

    return result;
  }

  /**
   * Runs all the workflows that this WorkflowManager manages.
   *
   * @param startingWorkflow The ID of the starting workflow.
   */
  public void run(@NotNull String startingWorkflow) {
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
      throw new RuntimeException(
          "Received a null WorkflowResult or null Part in the result: " + result);
    }

    switch (result.queueOrWorkflow) {
      case "A" -> accepted.offer(result.part);
      case "R" -> rejected.offer(result.part);
      default -> workflowMap.get(result.queueOrWorkflow).offer(result.part);
    }
  }
}
