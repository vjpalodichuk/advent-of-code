package com.capital7software.aoc.aoc2015.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.computer.SimpleComputer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.List;

/**
 * --- Day 23: Opening the Turing Lock ---<br><br>
 * Little Jane Marie just got her very first computer for Christmas from some unknown benefactor.
 * It comes with instructions and an example program, but the computer itself seems to be malfunctioning.
 * She's curious what the program does, and would like you to help her run it.
 * <p><br>
 * The manual explains that the computer supports two registers and six instructions
 * (truly, it goes on to remind the reader, a state-of-the-art technology).
 * The registers are named a and b, can hold any non-negative integer, and begin with a value of 0.
 * The instructions are as follows:
 * <ul>
 *     <li>
 *         hlf r sets register r to half its current value, then continues with the next instruction.
 *     </li>
 *     <li>
 *         tpl r sets register r to triple its current value, then continues with the next instruction.
 *     </li>
 *     <li>
 *         inc r increments register r, adding 1 to it, then continues with the next instruction.
 *     </li>
 *     <li>
 *         jmp offset is a jump; it continues with the instruction offset away relative to itself.
 *     </li>
 *     <li>
 *         jie r, offset is like jmp, but only jumps if register r is even ("jump if even").
 *     </li>
 *     <li>
 *         jio r, offset is like jmp, but only jumps if register r is 1 ("jump if one", not odd).
 *     </li>
 * </ul>
 * All three jump instructions work with an offset relative to that instruction.<br><br>
 * The offset is always written with a prefix + or - to indicate the direction of the jump
 * (forward or backward, respectively).
 * <p><br>
 * For example, jmp +1 would simply continue with the next instruction, while jmp +0 would
 * continuously jump back to itself forever.
 * <p><br>
 * The program exits when it tries to run an instruction beyond the ones defined.
 * <p><br>
 * For example, this program sets a to 2, because the jio instruction causes it to skip the tpl instruction:
 * <p><br>
 * <code>
 * inc a<br>
 * jio a, +2<br>
 * tpl a<br>
 * inc a<br>
 * </code>
 * <p><br>
 * What is the value in register b when the program in your puzzle input is finished executing?
 * <p><br>
 * Your puzzle answer was 184.
 * <p><br>
 * --- Part Two ---<br><br>
 * The unknown benefactor is very thankful for releasi-- er, helping little Jane Marie with her computer.
 * Definitely not to distract you, what is the value in register b after the program is
 * finished executing if register a starts as 1 instead?
 * <p><br>
 * Your puzzle answer was 231.
 */
public class Day23 implements AdventOfCodeSolution {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day23.class);

    /**
     * Instantiates the solution instance.
     */
    public Day23() {

    }

    @Override
    public String getDefaultInputFilename() {
        return "inputs/input_day_23-01.txt";
    }

    @Override
    public void runPart1(List<String> input) {
        var start = Instant.now();
        var lowest = runProgramAndGetValueInRegister(input, "b");
        var end = Instant.now();
        LOGGER.info("The value in register b is: {}", lowest);
        logTimings(LOGGER, start, end);
    }

    @Override
    public void runPart2(List<String> input) {
        var start = Instant.now();
        var lowest = runProgramWithStartingRegisterAndValueAndGetValueInRegister(
                input,
                "a",
                1L,
                "b"
        );
        var end = Instant.now();
        LOGGER.info("The value in register b is: {}", lowest);
        logTimings(LOGGER, start, end);
    }

    /**
     * Runs the specified program and returns the value in the specified register after the
     * program completes.
     *
     * @param input    The program to run.
     * @param register The register to retrieve the value of after the program runs.
     * @return The value in the specified register after the program completes.
     */
    public long runProgramAndGetValueInRegister(List<String> input, String register) {
        var computer = new SimpleComputer();
        computer.loadProgram(input);
        computer.run();
        return computer.getRegister(register);
    }

    /**
     * Runs the specified program and returns the value in the specified register after the
     * program completes. Prior to execution, the put register is updated with the specified value.
     *
     * @param input       The program to run.
     * @param registerPut The register to set the value of before running the program.
     * @param value       The value to put in the register.
     * @param registerGet The register to retrieve the value of after the program runs.
     * @return The value in the specified register after the program completes.
     */
    public long runProgramWithStartingRegisterAndValueAndGetValueInRegister(
            List<String> input,
            String registerPut,
            Long value,
            String registerGet
    ) {
        var computer = new SimpleComputer();
        computer.loadProgram(input);
        computer.setRegister(registerPut, value);
        computer.run();
        return computer.getRegister(registerGet);
    }
}
