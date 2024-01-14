package com.capital7software.aoc.lib.grid;

/**
 * The type of lighting instruction for laying out the ChristmasLights
 */
public enum LightInstructionType {
    TURN_ON("turn on"),
    TURN_OFF("turn off"),
    TOGGLE("toggle");

    private final String label;

    LightInstructionType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

