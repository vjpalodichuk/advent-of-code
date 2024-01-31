package com.capital7software.aoc.lib.graph.path;

/**
 * Used as a return value from the valid and invalid handlers used by the PathFinders.
 */
public enum PathFinderStatus {
  /**
   * Return this value to indicate to the PathFinder to stop finding any more paths.
   */
  FINISHED,
  /**
   * Return this value to indicate to the PathFinder to continue as it normally would.
   */
  CONTINUE,
  /**
   * Return this value to indicate to the PathFinder to move to the next start Vertex.
   */
  NEXT_START
}
