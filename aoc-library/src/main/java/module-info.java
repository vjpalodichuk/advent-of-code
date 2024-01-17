/**
 * The Advent of Code module for sharing solutions among various puzzles.
 *
 */
module com.capital7software.aoc.lib {
    requires java.logging;
    requires java.base;
    requires org.jetbrains.annotations;
    requires com.github.spotbugs.annotations;

    exports com.capital7software.aoc.lib;
    exports com.capital7software.aoc.lib.analysis;
    exports com.capital7software.aoc.lib.circuit.board;
    exports com.capital7software.aoc.lib.circuit.gate;
    exports com.capital7software.aoc.lib.circuit.signal;
    exports com.capital7software.aoc.lib.circuit.wire;
    exports com.capital7software.aoc.lib.collection;
    exports com.capital7software.aoc.lib.computer;
    exports com.capital7software.aoc.lib.crypt;
    exports com.capital7software.aoc.lib.geometry;
    exports com.capital7software.aoc.lib.graph;
    exports com.capital7software.aoc.lib.graph.constaint;
    exports com.capital7software.aoc.lib.graph.cycle;
    exports com.capital7software.aoc.lib.graph.network;
    exports com.capital7software.aoc.lib.graph.parser;
    exports com.capital7software.aoc.lib.graph.path;
    exports com.capital7software.aoc.lib.grid;
    exports com.capital7software.aoc.lib.math;
    exports com.capital7software.aoc.lib.string;
    exports com.capital7software.aoc.lib.util;
}