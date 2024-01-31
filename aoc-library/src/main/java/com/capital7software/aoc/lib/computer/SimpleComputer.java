package com.capital7software.aoc.lib.computer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The manual explains that the computer supports two registers and six instructions
 * (truly, it goes on to remind the reader, a state-of-the-art technology).
 * The registers are named a and b, can hold any non-negative integer, and begin with a value of 0.
 * The instructions are as follows:
 * <ul>
 *     <li>
 *         hlf r sets register r to half its current value, then continues with the
 *         next instruction.
 *     </li>
 *     <li>
 *         tpl r sets register r to triple its current value, then continues with the
 *         next instruction.
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
 *
 * <p><br>
 * For example, jmp +1 would simply continue with the next instruction, while jmp +0 would
 * continuously jump back to itself forever.
 *
 * <p><br>
 * The program exits when it tries to run an instruction beyond the ones defined.
 *
 * <p><br>
 * For example, this program sets a to 2, because the jio instruction causes it to skip
 * the tpl instruction:
 *
 * <p><br>
 * <code>
 * inc a<br>
 * jio a, +2<br>
 * tpl a<br>
 * inc a<br>
 * </code>
 *
 * <p><br>
 * What is the value in register b when the program in your puzzle input is finished executing?
 *
 * <p><br>
 * The unknown benefactor is very thankful for releasi-- er, helping little Jane Marie with
 * her computer. Definitely not to distract you, what is the value in register b after the
 * program is finished executing if register a starts as 1 instead?
 */
public class SimpleComputer {
  private final List<String> instructions;
  private final Map<String, Long> registers;

  /**
   * Instantiates a new Computer instance with no program loaded.
   */
  public SimpleComputer() {
    instructions = new ArrayList<>();
    registers = new HashMap<>();

    init();
  }

  /**
   * Initializes this Computer instance by clearing the loaded instructions and resetting the
   * registers back to zero.
   */
  public void init() {
    instructions.clear();
    registers.put("a", 0L);
    registers.put("b", 0L);
  }

  /**
   * Loads the specified instructions from the List of Strings.
   *
   * @param input The list of instructions.
   */
  public void loadProgram(List<String> input) {
    instructions.clear();
    instructions.addAll(input);
  }

  /**
   * Returns the value stored in the specified register.
   *
   * @param register The register to retrieve the value from.
   * @return The value stored in the specified register.
   */
  public Long getRegister(String register) {
    return registers.get(register);
  }

  /**
   * Sets the specified register to the specified value.
   *
   * @param register The register to set.
   * @param value    The value to store in the register.
   */
  public void setRegister(String register, Long value) {
    registers.put(register, value);
  }

  /**
   * Runs the loaded program.
   */
  public void run() {
    int i = 0;
    while (i < instructions.size()) {
      var instruction = instructions.get(i);

      if (instruction.startsWith("hlf")) {
        var register = instruction.substring(4).trim();
        registers.put(register, registers.get(register) / 2);
        i++;
      } else if (instruction.startsWith("tpl")) {
        var register = instruction.substring(4).trim();
        registers.put(register, registers.get(register) * 3);
        i++;
      } else if (instruction.startsWith("inc")) {
        var register = instruction.substring(4).trim();
        registers.put(register, registers.get(register) + 1);
        i++;
      } else if (instruction.startsWith("jmp")) {
        var direction = instruction.substring(4, 5).trim();
        var distance = Integer.parseInt(instruction.substring(5).trim());
        if (direction.equals("-")) {
          i -= distance;
        } else {
          i += distance;
        }
      } else {
        int distance = Integer.parseInt(instruction.substring(8).trim());
        if (instruction.startsWith("jie")) {
          var register = instruction.substring(4, 5).trim();
          var direction = instruction.substring(7, 8).trim();
          if (registers.get(register) % 2 == 0) {
            if (direction.equals("-")) {
              i -= distance;
            } else {
              i += distance;
            }
          } else {
            i++;
          }
        } else if (instruction.startsWith("jio")) {
          var register = instruction.substring(4, 5).trim();
          var direction = instruction.substring(7, 8).trim();
          if (registers.get(register) == 1) {
            if (direction.equals("-")) {
              i -= distance;
            } else {
              i += distance;
            }
          } else {
            i++;
          }
        }
      }
    }
  }
}
