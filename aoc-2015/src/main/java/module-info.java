/**
 * The Advent of Code 2015 module for sharing solutions among various puzzles.
 *
 */
module com.capital7software.aoc.aoc2015aoc {
    requires java.logging;
    requires java.base;
    requires org.jetbrains.annotations;
    requires com.github.spotbugs.annotations;
    requires com.capital7software.aoc.lib;

    exports com.capital7software.aoc.aoc2015;
    exports com.capital7software.aoc.aoc2015.days;
}