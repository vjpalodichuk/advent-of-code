package com.capital7software.aoc.lib.geometry;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * A 2D Polygon with support for coloring the edges of the vertices.
 *
 * @param <T> The type of the coordinates in the vertices.
 */
public class Polygon2D<T extends Number & Comparable<T>> {
  private final List<Point2D<T>> vertices;
  private final List<String> edgeColors;

  /**
   * Instantiates a new Polygon2D with the list of vertices and hexadecimal color values.<br>
   * The order of the vertices and colors in the Lists is important as it determines the
   * orientation of this Polygon2D as well as the rendering order.
   *
   * @param vertices   The list of Point2Ds that represent the points of this Polygon2D.
   * @param edgeColors The list of hexadecimal colors for the edges of this Polygon2D.
   */
  public Polygon2D(@NotNull List<Point2D<T>> vertices, @NotNull List<String> edgeColors) {
    this.vertices = new ArrayList<>(vertices);
    this.edgeColors = new ArrayList<>(edgeColors);
  }

  /**
   * Instantiates an empty Polygon2D.
   */
  public Polygon2D() {
    this(new LinkedList<>(), new LinkedList<>());
  }

  /**
   * Returns true if the specified Point2D is in the list of vertices of this Polygon2D.
   *
   * @param point The point to test.
   * @return True if the specified Point2D is in the list of vertices of this Polygon2D.
   */
  public boolean contains(@NotNull Point2D<T> point) {
    return vertices.contains(point);
  }

  /**
   * Adds the specified hexadecimal color value to this Polygon2D.
   *
   * @param color The hexadecimal color value to add to this Polygon2D.
   * @return True if the color was added.
   */
  public boolean add(String color) {
    if (color != null && !color.isBlank()) {
      return edgeColors.add(color);
    }

    return false;
  }

  /**
   * Adds the specified point and color to this Polygon2D.
   *
   * @param point The Point2D to add.
   * @param color The hexadecimal color to add.
   * @return True if both the point and color were added to this Polygon2D.
   */
  public boolean add(@NotNull Point2D<T> point, String color) {
    add(point);
    return add(color);
  }

  /**
   * Adds the specified point to this Polygon2D.
   *
   * @param point The Point2D to add.
   * @return True if the point is added to this Polygon2D.
   */
  public boolean add(@NotNull Point2D<T> point) {
    return vertices.add(point);
  }

  /**
   * Returns a LineSegment2D from the specified index to its neighbor. The index parameter
   * must be a valid index into the List of vertices.
   *
   * @param index The 0-based index into the List of vertices.
   * @return A LineSegment2D from the specified index to its neighbor.
   */
  public @NotNull LineSegment2D<T> getSegment(int index) {
    return new LineSegment2D<>(vertices.get(index), vertices.get((index + 1) % vertices.size()));
  }

  /**
   * Calculates and returns the perimeter of this Polygon2D.
   *
   * @return The perimeter of this Polygon2D.
   */
  public double calculatePerimeter() {
    var perimeter = 0L;

    for (int i = 0; i < vertices.size(); i++) {
      perimeter += (long) getSegment(i).length();
    }
    return perimeter;
  }

  /**
   * Calculates and returns the area taken up by the edges of this Polygon2D.
   *
   * @return The area taken up by the edges of this Polygon2D.
   */
  public double calculateEdgeArea() {
    return calculatePerimeter() / 2 + 1;
  }

  /**
   * Calculates and returns the area inside this Polygon2D (doesn't include the edge area).
   *
   * @return The area inside this Polygon2D (doesn't include the edge area).
   */
  public double calculateInsideArea() {
    // Uses the Shoelace formula of:
    // = | 1/2 [ (x1y2 + x2y3 + … + xn-1yn + xny1) – (x2y1 + x3y2 + … + xnyn-1 + x1yn) ] |
    double area = 0.0;

    for (int i = 0; i < vertices.size(); i++) {
      area += getSegment(i).cross();
    }

    return Math.abs(area / 2); // Ensure the area is positive!
  }

  /**
   * Calculates and returns the total area of this Polygon2D (edge area and inside area).
   *
   * @return The total area of this Polygon2D (edge area and inside area).
   */
  public double calculateTotalArea() {
    double area = 0.0;

    for (int i = 0; i < vertices.size(); i++) {
      var segment = getSegment(i);
      area += segment.cross() + segment.length();
    }

    return Math.abs(area / 2) + 1; // Ensure the area is positive!
  }
}
