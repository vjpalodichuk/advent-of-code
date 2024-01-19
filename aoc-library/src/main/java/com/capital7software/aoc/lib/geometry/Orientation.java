package com.capital7software.aoc.lib.geometry;

/**
 * The Orientation of vertices / points.
 */
public enum Orientation {

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

