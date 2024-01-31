package com.capital7software.aoc.lib.grid;

import lombok.Getter;

/**
 * The type of lighting instruction for laying out the ChristmasLights.
 */
@Getter
public enum LightInstructionType {
  /**
   * Instruction to turn on the lights.
   */
  TURN_ON("turn on"),
  /**
   * Instruction to turn off the lights.
   */
  TURN_OFF("turn off"),
  /**
   * Instruction to toggle the lights.
   */
  TOGGLE("toggle");

  /**
   * -- GETTER --
   *  Returns the label of this instance.
   *
   */
  private final String label;

  /**
   * Instantiates a new LightInstructionType with the specified label.
   *
   * @param label The friendly name fot this type.
   */
  LightInstructionType(String label) {
    this.label = label;
  }

}

