package com.capital7software.aoc.lib.graph.path;

/**
 * Used as a return value from the valid and invalid handlers used by the PathFinders.
 */
public enum PathfinderStatus {
  /**
   * Return this value to indicate to the Pathfinder to stop finding any more paths.
   */
  FINISHED,
  /**
   * Return this value to indicate to the Pathfinder to continue as it normally would.
   */
  CONTINUE,
  /**
   * Return this value to indicate to the Pathfinder to move to the next start Vertex.
   */
  NEXT_START
}
