package com.capital7software.aoc.aoc2023aoc.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.graph.network.WeatherStation;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * --- Day 25: Snowverload ---<br><br>
 * Still somehow without snow, you go to the last place you haven't checked: the center of
 * Snow Island, directly below the waterfall.
 *
 * <p><br>
 * Here, someone has clearly been trying to fix the problem. Scattered everywhere are
 * hundreds of weather machines, almanacs, communication modules, hoof prints, machine
 * parts, mirrors, lenses, and so on.
 *
 * <p><br>
 * Somehow, everything has been wired together into a massive snow-producing apparatus,
 * but nothing seems to be running. You check a tiny screen on one of the communication
 * modules: Error 2023. It doesn't say what Error 2023 means, but it does have the phone
 * number for a support line printed on it.
 *
 * <p><br>
 * "Hi, you've reached Weather Machines And So On, Inc. How can I help you?" You explain
 * the situation.
 *
 * <p><br>
 * "Error 2023, you say? Why, that's a power overload error, of course! It means you have
 * too many components plugged in. Try unplugging some components and--" You explain that
 * there are hundreds of components here, and you're in a bit of a hurry.
 *
 * <p><br>
 * "Well, let's see how bad it is; do you see a big red reset button somewhere? It should
 * be on its own module. If you push it, it probably won't fix anything, but it'll report
 * how overloaded things are." After a minute or two, you find the reset button; it's so
 * big that it takes two hands just to get enough leverage to push it. Its screen then displays:
 *
 * <p><br>
 * <code>
 * <b>SYSTEM OVERLOAD!</b>
 * <br><br>
 * Connected components would require<br>
 * power equal to at least <b>100 stars!</b><br>
 * </code>
 *
 * <p><br>
 * "Wait, how many components did you say are plugged in? With that much equipment, you
 * could produce snow for an entire--" You disconnect the call.
 *
 * <p><br>
 * You have nowhere near that many stars - you need to find a way to disconnect at least
 * half of the equipment here, but it's already Christmas! You only have time to disconnect
 * three wires.
 *
 * <p><br>
 * Fortunately, someone left a wiring diagram (your puzzle input) that shows how the
 * components are connected. For example:
 *
 * <p><br>
 * <code>
 * jqt: rhn xhk nvd<br>
 * rsh: frs pzl lsr<br>
 * xhk: hfx<br>
 * cmg: qnr nvd lhk bvb<br>
 * rhn: xhk bvb hfx<br>
 * bvb: xhk hfx<br>
 * pzl: lsr hfx nvd<br>
 * qnr: nvd<br>
 * ntq: jqt hfx bvb xhk<br>
 * nvd: lhk<br>
 * lsr: lhk<br>
 * rzs: qnr cmg lsr rsh<br>
 * frs: qnr lhk lsr<br>
 * </code>
 *
 * <p><br>
 * Each line shows the name of a component, a colon, and then a list of other components
 * to which that component is connected. Connections aren't directional; abc: xyz and xyz:
 * abc both represent the same configuration. Each connection between two components is
 * represented only once, so some components might only ever appear on the left or right
 * side of a colon.
 *
 * <p><br>
 * In this example, if you disconnect the wire between hfx/pzl, the wire between bvb/cmg,
 * and the wire between nvd/jqt, you will divide the components into two separate,
 * disconnected groups:
 *
 * <p><br>
 * <ul>
 *     <li>
 *         9 components: cmg, frs, lhk, lsr, nvd, pzl, qnr, rsh, and rzs.
 *     </li>
 *     <li>
 *         6 components: bvb, hfx, jqt, ntq, rhn, and xhk.
 *     </li>
 * </ul>
 * Multiplying the sizes of these groups together produces 54.<br>
 *
 * <p><br>
 * Find the three wires you need to disconnect in order to divide the components into two
 * separate groups. What do you get if you multiply the sizes of these two groups together?
 *
 * <p><br>
 * Your puzzle answer was 598120.
 *
 * <p><br>
 * --- Part Two ---<br><br>
 * You climb over weather machines, under giant springs, and narrowly avoid a pile of
 * pipes as you find and disconnect the three wires.
 *
 * <p><br>
 * A moment after you disconnect the last wire, the big red reset button module makes a
 * small ding noise:
 *
 * <p><br>
 * <code>
 * System overload resolved!<br>
 * Power required is now <b>50 stars.</b><br>
 * </code>
 *
 * <p><br>
 * Out of the corner of your eye, you notice goggles and a loose-fitting hard hat peeking
 * at you from behind an ultra crucible. You think you see a faint glow, but before you
 * can investigate, you hear another small ding:
 *
 * <p><br>
 * <code>
 * Power required is now <b>49 stars.</b>
 * <br><br>
 * Please supply the necessary stars and<br>
 * push the button to restart the system.<br>
 * </code>
 */
public class Day25 implements AdventOfCodeSolution {
  private static final Logger LOGGER = LoggerFactory.getLogger(Day25.class);

  /**
   * Instantiates this Solution instance.
   */
  public Day25() {

  }

  @Override
  public String getDefaultInputFilename() {
    return "inputs/input_day_25-01.txt";
  }

  @Override
  public void runPart1(List<String> input) {
    var start = Instant.now();
    var answer = findMinimumCut(input, 3);
    var end = Instant.now();

    LOGGER.info("The product of the sets with the maximum product is: {}",
                answer);
    logTimings(LOGGER, start, end);
  }

  @Override
  public void runPart2(List<String> input) {
    LOGGER.info("Merry Christmas! Push the BIG RED button to continue!");
  }

  /**
   * Returns the maximum product of the minimum cut of the WeatherStation.
   *
   * @param input The List of Strings to parse into the components of the WeatherStation.
   * @param cuts  The number of cuts of the station to make.
   * @return The maximum product of the minimum cut of the WeatherStation.
   */
  public long findMinimumCut(List<String> input, int cuts) {
    var station = WeatherStation.buildWeatherStation(input);
    return WeatherStation.findMinimumCut(station, cuts);
  }
}
