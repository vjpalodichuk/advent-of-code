package com.capital7software.aoc2015.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.computer.SimpleComputer;

import java.time.Instant;
import java.util.List;

/**
 * --- Day 23: Opening the Turing Lock ---<br>
 * Little Jane Marie just got her very first computer for Christmas from some unknown benefactor.
 * It comes with instructions and an capital7software program, but the computer itself seems to be malfunctioning.
 * She's curious what the program does, and would like you to help her run it.
 * <p><br>
 * The manual explains that the computer supports two registers and six instructions
 * (truly, it goes on to remind the reader, a state-of-the-art technology).
 * The registers are named a and b, can hold any non-negative integer, and begin with a value of 0.
 * The instructions are as follows:
 * <p>
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
 * <p>
 * All three jump instructions work with an offset relative to that instruction.<br>
 * The offset is always written with a prefix + or - to indicate the direction of the jump
 * (forward or backward, respectively).
 * <p><br>
 * For capital7software, jmp +1 would simply continue with the next instruction, while jmp +0 would
 * continuously jump back to itself forever.
 * <p><br>
 * The program exits when it tries to run an instruction beyond the ones defined.
 * <p><br>
 * For capital7software, this program sets a to 2, because the jio instruction causes it to skip the tpl instruction:
 * <p><br>
 * inc a<br>
 * jio a, +2<br>
 * tpl a<br>
 * inc a<br>
 * <p><br>
 * What is the value in register b when the program in your puzzle input is finished executing?
 * <p>
 * Your puzzle answer was 184.
 * <p>
 * --- Part Two ---<br>
 * The unknown benefactor is very thankful for releasi-- er, helping little Jane Marie with her computer.
 * Definitely not to distract you, what is the value in register b after the program is
 * finished executing if register a starts as 1 instead?
 * <p>
 * Your puzzle answer was 231.
 * <p>
 */
public class Day23 implements AdventOfCodeSolution {
    @Override
    public String getDefaultInputFilename() {
        return "inputs/input_day_23-01.txt";
    }

    @Override
    public void runPart1(List<String> input) {
        var start = Instant.now();
        var lowest = runProgramAndGetValueInRegister(input, "b");
        var end = Instant.now();
        System.out.printf("The value in register b is: %d%n", lowest);
        printTiming(start, end);
    }

    @Override
    public void runPart2(List<String> input) {
        var start = Instant.now();
        var lowest = runProgramWithStartingRegisterAndValueAndGetValueInRegister(input, "a", 1L, "b");
        var end = Instant.now();
        System.out.printf("The value in register b is: %d%n", lowest);
        printTiming(start, end);
    }

    public long runProgramAndGetValueInRegister(List<String> input, String register) {
        var computer = new SimpleComputer();
        computer.loadProgram(input);
        computer.run();
        return computer.getRegister(register);
    }

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
