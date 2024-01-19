package com.capital7software.aoc.lib.geometry;

/**
 * The Orientation2D of vertices / points.
 */
public enum Orientation2D {

    /**
     * The vertices / points have the same slope and lie on the same line!
     */
    COLLINEAR,
    /**
     * The vertices / points are ordered such that the second comes after the first.
     */
    CLOCKWISE,
    /**
     * The vertices / points are ordered such that the second comes before the first.
     */
    COUNTERCLOCKWISE
}

