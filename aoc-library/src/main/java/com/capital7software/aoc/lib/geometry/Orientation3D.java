package com.capital7software.aoc.lib.geometry;

/**
 * Represents the orientation of a LineSegment3D.
 */
public enum Orientation3D {
    /**
     * The LineSegment3D is aligned on the x-Axis horizontally.
     */
    HORIZONTAL_X,
    /**
     * The LineSegment3D is aligned on the y-Axis horizontally.
     */
    HORIZONTAL_Y,
    /**
     * The LineSegment3D is aligned on the z-Axis vertically.
     */
    VERTICAL_Z, // Used as the Height
    /**
     * The LineSegment3D is a single cube.
     */
    UNKNOWN // A single cube
}

