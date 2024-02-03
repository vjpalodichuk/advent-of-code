package com.capital7software.aoc.aoc2023aoc.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.analysis.WorkflowManager;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * --- Day 19: Aplenty ---<br><br>
 * The Elves of Gear Island are thankful for your help and send you on your way.
 * They even have a hang glider that someone stole from Desert Island; since you're
 * already going that direction, it would help them a lot if you would use it to get
 * down there and return it to them.
 *
 * <p><br>
 * As you reach the bottom of the relentless avalanche of machine parts, you discover
 * that they're already forming a formidable heap. Don't worry, though - a group of
 * Elves is already here organizing the parts, and they have a system.
 *
 * <p><br>
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
 * Your puzzle answer was 432427.
 *
 * <p><br>
 * --- Part Two ---<br><br>
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
 *
 * <p><br>
 * Your puzzle answer was 143760172569135.
 */
public class Day19 implements AdventOfCodeSolution {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day19.class);

  /**
   * Instantiates this Solution instance.
   */
  public Day19() {

  }

  @Override
  public String getDefaultInputFilename() {
    return "inputs/input_day_19-01.txt";
  }

  @Override
  public void runPart1(List<String> input) {
    var start = Instant.now();
    var answer = sumOfRatingsOfAcceptedParts(input);
    var end = Instant.now();

    LOGGER.info("The minimum heat loss with minSteps 1 and maxSteps 2 is: {}", answer);
    logTimings(LOGGER, start, end);
  }

  @Override
  public void runPart2(List<String> input) {
    var start = Instant.now();
    var answer = distinctCombinationsOfAcceptedRatings(input);
    var end = Instant.now();

    LOGGER.info("The minimum heat loss with minSteps 4 and maxSteps 10 is: {}", answer);
    logTimings(LOGGER, start, end);
  }

  /**
   * Returns the sum of the accepted parts.
   *
   * @param input The List of Strings to parse that define the workflows and part ratings.
   * @return The sum of the accepted parts.
   */
  public long sumOfRatingsOfAcceptedParts(List<String> input) {
    var manager = WorkflowManager.buildWorkflowManager(input);
    manager.run("in");
    return manager.sumAccepted();
  }

  /**
   * Returns the total distinct combinations of the ratings that will be accepted by the elves.
   *
   * @param input The List of Strings to parse that define the workflows and part ratings.
   * @return The total distinct combinations of the ratings that will be accepted by the elves.
   */
  public long distinctCombinationsOfAcceptedRatings(List<String> input) {
    var manager = WorkflowManager.buildWorkflowManager(input);
    return manager.calculateCombinations("in");
  }
}
