package com.capital7software.aoc.aoc2023.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.analysis.CycleHeadquarters;

import java.time.Instant;
import java.util.List;
import java.util.logging.Logger;

/**
 * --- Day 20: Pulse Propagation ---<br><br>
 * With your help, the Elves manage to find the right parts and fix all of the machines. Now, they
 * just need to send the command to boot up the machines and get the sand flowing again.
 * <p><br>
 * The machines are far apart and wired together with long cables. The cables don't connect to the
 * machines directly, but rather to communication modules attached to the machines that perform
 * various initialization tasks and also act as communication relays.
 * <p><br>
 * Modules communicate using pulses. Each pulse is either a high pulse or a low pulse. When a module
 * sends a pulse, it sends that type of pulse to each module in its list of destination modules.
 * <p><br>
 * There are several different types of modules:
 * <p><br>
 * Flip-flop modules (prefix %) are either on or off; they are initially off. If a flip-flop module
 * receives a high pulse, it is ignored and nothing happens. However, if a flip-flop module receives
 * a low pulse, it flips between on and off. If it was off, it turns on and sends a high pulse.
 * If it was on, it turns off and sends a low pulse.
 * <p><br>
 * Conjunction modules (prefix &#38;) remember the type of the most recent pulse received from each of
 * their connected input modules; they initially default to remembering a low pulse for each input.
 * When a pulse is received, the conjunction module first updates its memory for that input. Then,
 * if it remembers high pulses for all inputs, it sends a low pulse; otherwise, it sends a high pulse.
 * <p><br>
 * There is a single broadcast module (named broadcaster). When it receives a pulse, it sends the
 * same pulse to all of its destination modules.
 * <p><br>
 * Here at Desert Machine CycleHeadquarters, there is a module with a single button on it called, aptly,
 * the button module. When you push the button, a single low pulse is sent directly to the broadcaster module.
 * <p><br>
 * After pushing the button, you must wait until all pulses have been delivered and fully handled
 * before pushing it again. Never push the button if modules are still processing pulses.
 * <p><br>
 * Pulses are always processed in the order they are sent. So, if a pulse is sent to modules a, b,
 * and c, and then module a processes its pulse and sends more pulses, the pulses sent to modules
 * b and c would have to be handled first.
 * <p><br>
 * The module configuration (your puzzle input) lists each module. The name of the module is preceded
 * by a symbol identifying its type, if any. The name is then followed by an arrow and a list of
 * its destination modules. For example:
 * <p><br>
 * <code>
 * broadcaster -> a, b, c
 * %a -> b
 * %b -> c
 * %c -> inv
 * &#38;inv -> a
 * </code>
 * <p><br>
 * In this module configuration, the broadcaster has three destination modules named a, b, and c.
 * Each of these modules is a flip-flop module (as indicated by the % prefix). a outputs to b which
 * outputs to c which outputs to another module named inv. inv is a conjunction module
 * (as indicated by the &#38; prefix) which, because it has only one input, acts like an inverter
 * (it sends the opposite of the pulse type it receives); it outputs to a.
 * <p><br>
 * By pushing the button once, the following pulses are sent:
 * <p><br>
 * <code>
 * button -low-> broadcaster
 * broadcaster -low-> a
 * broadcaster -low-> b
 * broadcaster -low-> c
 * a -high-> b
 * b -high-> c
 * c -high-> inv
 * inv -low-> a
 * a -low-> b
 * b -low-> c
 * c -low-> inv
 * inv -high-> a
 * </code>
 * <p><br>
 * After this sequence, the flip-flop modules all end up off, so pushing the button again repeats
 * the same sequence.
 * <p><br>
 * Here's a more interesting example:
 * <p><br>
 * <code>
 * broadcaster -> a
 * %a -> inv, con
 * &#38;inv -> b
 * %b -> con
 * &#38;con -> output
 * </code>
 * <p><br>
 * This module configuration includes the broadcaster, two flip-flops (named a and b), a single-input
 * conjunction module (inv), a multi-input conjunction module (con), and an untyped module named
 * output (for testing purposes). The multi-input conjunction module con watches the two flip-flop
 * modules and, if they're both on, sends a low pulse to the output module.
 * <p><br>
 * Here's what happens if you push the button once:
 * <p><br>
 * <code>
 * button -low-> broadcaster
 * broadcaster -low-> a
 * a -high-> inv
 * a -high-> con
 * inv -low-> b
 * con -high-> output
 * b -high-> con
 * con -low-> output
 * </code>
 * <p><br>
 * Both flip-flops turn on and a low pulse is sent to output! However, now that both flip-flops
 * are on and con remembers a high pulse from each of its two inputs, pushing the button a second
 * time does something different:
 * <p><br>
 * <code>
 * button -low-> broadcaster
 * broadcaster -low-> a
 * a -low-> inv
 * a -low-> con
 * inv -high-> b
 * con -high-> output
 * </code>
 * <p><br>
 * Flip-flop a turns off! Now, con remembers a low pulse from module a, and so it sends only a
 * high pulse to output.
 * <p><br>
 * Push the button a third time:
 * <p><br>
 * <code>
 * button -low-> broadcaster
 * broadcaster -low-> a
 * a -high-> inv
 * a -high-> con
 * inv -low-> b
 * con -low-> output
 * b -low-> con
 * con -high-> output
 * </code>
 * <p><br>
 * This time, flip-flop a turns on, then flip-flop b turns off. However, before b can turn off, the
 * pulse sent to con is handled first, so it briefly remembers all high pulses for its inputs and
 * sends a low pulse to output. After that, flip-flop b turns off, which causes con to update its
 * state and send a high pulse to output.
 * <p><br>
 * Finally, with a on and b off, push the button a fourth time:
 * <p><br>
 * <code>
 * button -low-> broadcaster
 * broadcaster -low-> a
 * a -low-> inv
 * a -low-> con
 * inv -high-> b
 * con -high-> output
 * </code>
 * <p><br>
 * This completes the cycle: a turns off, causing con to remember only low pulses and restoring all
 * modules to their original states.
 * <p><br>
 * To get the cables warmed up, the Elves have pushed the button 1000 times. How many pulses got sent as
 * a result (including the pulses sent by the button itself)?
 * <p><br>
 * In the first example, the same thing happens every time the button is pushed: 8 low pulses and 4 high
 * pulses are sent. So, after pushing the button 1000 times, 8000 low pulses and 4000 high pulses are sent.
 * Multiplying these together gives 32000000.
 * <p><br>
 * In the second example, after pushing the button 1000 times, 4250 low pulses and 2750 high pulses are
 * sent. Multiplying these together gives 11687500.
 * <p><br>
 * Consult your module configuration; determine the number of low pulses and high pulses that would be
 * sent after pushing the button 1000 times, waiting for all pulses to be fully handled after each push
 * of the button. What do you get if you multiply the total number of low pulses sent by the total
 * number of high pulses sent?
 * <p><br>
 * Your puzzle answer was 886701120.
 * <p><br>
 * --- Part Two ---<br><br>
 * The final machine responsible for moving the sand down to Island Island has a module attached named rx.
 * The machine turns on when a single low pulse is sent to rx.
 * <p><br>
 * Reset all modules to their default states. Waiting for all pulses to be fully handled after each button
 * press, what is the fewest number of button presses required to deliver a single low pulse to the module
 * named rx?
 * <p><br>
 * Your puzzle answer was 228134431501037.
 */
public class Day20 implements AdventOfCodeSolution {
    private static final Logger LOGGER = Logger.getLogger(Day20.class.getName());

    /**
     * Instantiates this Solution instance.
     */
    public Day20() {

    }

    @Override
    public String getDefaultInputFilename() {
        return "inputs/input_day_20-01.txt";
    }

    @Override
    public void runPart1(List<String> input) {
        var start = Instant.now();
        var answer = getPulseProduct(input, 1_000);
        var end = Instant.now();

        LOGGER.info(String.format("The product of the low and high pulses after 1,000 button presses is: %d",
                answer));
        logTimings(LOGGER, start, end);
    }

    @Override
    public void runPart2(List<String> input) {
        var start = Instant.now();
        var answer = getButtonPressesNeededToSendLowPulseToModule(
                input,
                "rx",
                1,
                10_000
        );
        var end = Instant.now();

        LOGGER.info(String.format("The fewest number of button presses to deliver a low pulse to module rx is: %d",
                answer));
        logTimings(LOGGER, start, end);
    }

    /**
     * Returns the sum of the accepted parts.
     *
     * @param input         The List of Strings to parse that define the workflows and part ratings.
     * @param buttonPresses The number of times to bush the button.
     * @return The sum of the accepted parts.
     */
    public long getPulseProduct(List<String> input, long buttonPresses) {
        var headquarters = CycleHeadquarters.buildCycleHeadquarters(input);
        headquarters.pushButton(buttonPresses);
        return headquarters.getPulseProduct();
    }

    /**
     * Returns the minimum number of button presses needed to send a low pulse to the specified module.
     * Or -1 if the maximum number of button presses was reached before a low pulse would be sent to the
     * specified module.
     *
     * @param input               The List of Strings to parse that define the workflows and part ratings.
     * @param id                  The ID of the module we want to send a low pulse to.
     * @param requiredSourceCount If the specified ID has more than one source, then how many of them have to
     *                            send a low pulse to the destination?
     * @param maxButtonPresses    The maximum number of button presses to perform.
     * @return The minimum number of button presses needed to send a low pulse to the specified module.
     * Or -1 if the maximum number of button presses was reached before a low pulse would be sent to the
     * specified module.
     */
    public long getButtonPressesNeededToSendLowPulseToModule(
            List<String> input,
            String id,
            int requiredSourceCount,
            long maxButtonPresses
    ) {
        var headquarters = CycleHeadquarters.buildCycleHeadquarters(input);
        return headquarters.buttonPressesNeededToSendLowPulseToModule(id, requiredSourceCount, maxButtonPresses);
    }
}
