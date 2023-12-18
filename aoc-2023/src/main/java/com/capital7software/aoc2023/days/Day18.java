package com.capital7software.aoc2023.days;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class Day18 {

    private record Point2D(double x, double y) {
        public static final double EPSILON = 0.00001;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Point2D point2D)) return false;
            return Math.abs(x - point2D.x) <= EPSILON && Math.abs(y - point2D.y) <= EPSILON;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    private static class Polygon2D {
        private final List<Point2D> vertices;
        private final List<String> edgeColors;

        public Polygon2D(List<Point2D> vertices, List<String> edgeColors) {
            this.vertices = vertices;
            this.edgeColors = edgeColors;
        }

        public Polygon2D() {
            this(new LinkedList<>(), new LinkedList<>());
        }

        public boolean contains(Point2D point) {
            return vertices.contains(point);
        }

        public boolean add(String color) {
            if (color != null && !color.isBlank()) {
                return edgeColors.add(color);
            }

            return false;
        }

        public boolean add(Point2D point, String color) {
            add(point);
            return add(color);
        }

        public boolean add(Point2D point) {
            return vertices.add(point);
        }

        public double calculatePerimeter() {
            var perimeter = 0L;

            for (int i = 0; i < vertices.size(); i++) {
                LineSegment lineSegment;

                if (i == vertices.size() - 1) {
                    lineSegment = new LineSegment(vertices.get(i), vertices.get(0));
                } else {
                    lineSegment = new LineSegment(vertices.get(i), vertices.get(i + 1));
                }

                perimeter += lineSegment.length();
            }
            return perimeter;
        }

        public double calculateEdgeArea() {
            return calculatePerimeter() / 2 + 1;
        }

        public double calculateInsideArea() {
            // Uses the Shoelace formula of:
            // = | 1/2 [ (x1y2 + x2y3 + … + xn-1yn + xny1) – (x2y1 + x3y2 + … + xnyn-1 + x1yn) ] |
            double area = 0.0;

            int j = vertices.size() - 1; // Initialize j to be the last point that

            for (int i = 0; i < vertices.size(); i++) {
                var pointI = vertices.get(i);
                var pointJ = vertices.get(j);

                area += (pointI.x() * pointJ.y()) - (pointJ.x() * pointI.y());
                j = i;
            }

            return Math.abs(area / 2); // Ensure the area is positive!
        }

        public double calculateTotalArea() {
            return calculateInsideArea() + calculateEdgeArea();
        }
    }

    private enum Direction {
        UP(new Point2D(0, -1)),
        DOWN(new Point2D(0, 1)),
        LEFT(new Point2D(-1, 0)),
        RIGHT(new Point2D(1, 0));

        private final Point2D offset;

        Direction(Point2D offset) {
            this.offset = offset;
        }

        public static Direction fromLabel(String label) {
            return switch (label) {
                case "U", "3" -> UP;
                case "D", "1" -> DOWN;
                case "L", "2" -> LEFT;
                case "R", "0" -> RIGHT;
                default -> throw new RuntimeException("Unknown direction: " + label);
            };
        }

        public static String toNumericLabel(String alphaLabel) {
            return switch (alphaLabel) {
                case "U" -> "3";
                case "D" -> "1";
                case "L" -> "2";
                case "R" -> "0";
                default -> throw new RuntimeException("Unknown direction: " + alphaLabel);
            };
        }
    }

    public enum Orientation {
        COLLINEAR,
        CLOCKWISE,
        COUNTERCLOCKWISE
    }

    private record LineSegment(Point2D start, Point2D end) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof LineSegment that)) return false;
            return (start.equals(that.start) && end.equals(that.end) ||
                    (start.equals(that.end) && end.equals(that.start)));
        }

        @Override
        public int hashCode() {
            return Objects.hash(start, end);
        }

        public double length() {
            return LineSegment.length(start, end);
        }

        public static double length(Point2D a, Point2D b) {
            return Math.sqrt(Math.pow((b.x - a.x), 2) + Math.pow((b.y - a.y), 2));
        }


        /**
         * When this LineSegment and other LineSegment are on the same line and overlap, the point that
         * overlaps is returned based on the following criteria
         * <p>
         * If other's start is on this LineSegment, other's start is returned.
         * Else if other's end is on this LineSegment, other's end is returned.
         * Else if this LineSegment is totally inside other, then this start is returned.
         * Else null is returned.
         *
         * @param other The non-null LineSegment to test
         * @return The point of intersection or null if they don't intersect as defined above.
         */
        Point2D intersect(final LineSegment other) {
            if (!isOnSameLine(other)) {
                return null;
            }

            if (isPointOnSegment(other.start)) {
                return other.start;
            } else if (isPointOnSegment(other.end)) {
                return other.end;
            } else if (other.isPointOnSegment(start) && other.isPointOnSegment(end)) {
                return start;
            } else {
                return null;
            }
        }

        /**
         * Assumes that other is collinear to this LineSegment and checks if other falls on this LineSegment
         * @param other The other point to test
         * @return If other is on this LineSegment then true; else false.
         */
        public boolean isPointOnSegment(Point2D other) {
            var thisLength = length();
            var startToOther = LineSegment.length(start, other);
            var otherToEnd = LineSegment.length(other, end);

            return Math.abs(thisLength - (startToOther + otherToEnd)) <= Point2D.EPSILON;
        }

        /**
         * Returns true if the start and end point of the other LineSegment is collinear to this LineSegment.
         * @param other The LineSegment to test against.
         * @return If the other LineSegment is collinear to this LineSegment.
         */
        public boolean isOnSameLine(final LineSegment other) {
            return orientation(other.start) == Orientation.COLLINEAR &&
                    orientation(other.end) == Orientation.COLLINEAR;
        }

        /**
         * Returns the Orientation of other as it relates to this LineSegment.
         * @param other The point the orientation is calculated in relation to
         * @return The Orientation
         */
        public Orientation orientation(Point2D other) {
            double orientation = (end.y - start.y) * (other.x - end.x) -
                    (end.x - start .x) * (other.y - end.y);

            if (orientation <= Point2D.EPSILON) {
                return Orientation.COLLINEAR;
            }

            return orientation > 0 ? Orientation.CLOCKWISE : Orientation.COUNTERCLOCKWISE;
        }

    }

    private record VertexInfo(Direction direction, int length, String color) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof VertexInfo that)) return false;
            return length == that.length && direction == that.direction && color.equals(that.color);
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, length, color);
        }

        public Point2D getPoint(Point2D start) {
            var dxPoint = direction.offset;
            var newX = start.x() + dxPoint.x() * length;
            var newY = start.y() + dxPoint.y() * length;
            return new Point2D(newX, newY);
        }
    }

    private record Lagoon(Polygon2D polygon) {

        public static Lagoon build(Point2D initialVertex, Stream<String> stream, boolean colorsAsInstructions) {
            var polygon = new Polygon2D();

            polygon.add(initialVertex);

            var infos = stream.map(it -> Lagoon.parseLine(it, colorsAsInstructions)).filter(Objects::nonNull).toList();

            var previous = initialVertex;

            for (var info : infos) {
                var point = info.getPoint(previous);
                if (polygon.contains(point)) {
                    polygon.add(info.color());
                } else {
                    polygon.add(point, info.color());
                }
                previous = point;
            }

            return new Lagoon(polygon);
        }

        public static Lagoon build(Point2D initialVertex, Stream<String> stream) {
            return build(initialVertex, stream, false);
        }

        private static VertexInfo parseLine(String line, boolean colorsAsInstructions) {
            if (line == null || line.isBlank()) {
                return null;
            }

            var split = line.split(" ");

            if (!colorsAsInstructions) {
                var direction = Direction.fromLabel(split[0]);
                var length = Integer.parseInt(split[1]);
                var color = split[2].substring(1, split[2].length() - 1);

                return new VertexInfo(direction, length, color);
            } else {
                var toConvertDirectionAndLength = split[2].substring(1, split[2].length() - 1);
                var length = Integer.decode(toConvertDirectionAndLength
                        .substring(0, toConvertDirectionAndLength.length() - 1));
                var direction = Direction.fromLabel(toConvertDirectionAndLength
                        .substring(toConvertDirectionAndLength.length() - 1));
                var color = "#" + Integer.toHexString(Integer.parseInt(split[1])) + Direction.toNumericLabel(split[0]);

                return new VertexInfo(direction, length, color);
            }
        }

        public double calculateArea() {
            return polygon.calculateTotalArea();
        }
    }

    private static final String inputFilename = "inputs/input_day_18-01.txt";

    public static void main(String[] args) throws URISyntaxException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        var url = classloader.getResource(inputFilename);
        assert url != null;
        var path = Paths.get(url.toURI());

        part1(path);
        part2(path);
        part3();
    }

    private static void part1(Path path) {
        try (var stream = Files.lines(path)) {
            // Part 1
            System.out.println("Part 1: Start!");
            var lagoon = Lagoon.build(new Point2D(0, 0), stream);
            var start = Instant.now();
            var totalArea = lagoon.calculateArea();
            var end = Instant.now();
            System.out.println("Total area: " + (long) totalArea + " in " +
                    Duration.between(start, end).toNanos() + " ns");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void part2(Path path) {
        try (var stream = Files.lines(path)) {
            // Part 2
            System.out.println("Part 2: Start!");
            var lagoon = Lagoon.build(new Point2D(0, 0), stream, true);
            var start = Instant.now();
            var totalArea = lagoon.calculateArea();
            var end = Instant.now();
            System.out.println("Total area: " + (long) totalArea + " in " +
                    Duration.between(start, end).toNanos() + " ns");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void part3() {
        // Part 3
        System.out.println("Part 3: Start!");
        var line1 = new LineSegment(new Point2D(1, 1), new Point2D(1, 10));
        var line2 = new LineSegment(new Point2D(1, -2), new Point2D(1, 12));
        var line3 = new LineSegment(new Point2D(1, 2), new Point2D(1, 12));
        var line4 = new LineSegment(new Point2D(1, -2), new Point2D(1, 8));
        var line5 = new LineSegment(new Point2D(0, 2), new Point2D(5, 12));
        var line6 = new LineSegment(new Point2D(0, 2), new Point2D(0, 12));

        System.out.println("Line 1: " + line1);
        System.out.println("Line 2: " + line2);
        System.out.println("Line 3: " + line3);
        System.out.println("Line 4: " + line4);
        System.out.println("Line 5: " + line3);
        System.out.println("Line 6: " + line4);

        var start = Instant.now();
        var intersectionPoint = line1.intersect(line3);
        var end = Instant.now();
        System.out.println("Test Condition 1: Line 1 and Line 3 intersect at point: " + intersectionPoint + " in " +
                Duration.between(start, end).toNanos() + " ns");

        start = Instant.now();
        intersectionPoint = line1.intersect(line4);
        end = Instant.now();
        System.out.println("Test Condition 2: Line 1 and Line 4 intersect at point: " + intersectionPoint + " in " +
                Duration.between(start, end).toNanos() + " ns");

        start = Instant.now();
        intersectionPoint = line1.intersect(line2);
        end = Instant.now();
        System.out.println("Test Condition 3: Line 1 and Line 2 intersect at point: " + intersectionPoint + " in " +
                Duration.between(start, end).toNanos() + " ns");

        start = Instant.now();
        intersectionPoint = line1.intersect(line5);
        end = Instant.now();
        System.out.println("Test Condition 4: Line 1 and Line 5 intersect at point: " + intersectionPoint + " in " +
                Duration.between(start, end).toNanos() + " ns");

        start = Instant.now();
        intersectionPoint = line1.intersect(line6);
        end = Instant.now();
        System.out.println("Test Condition 4: Line 1 and Line 6 intersect at point: " + intersectionPoint + " in " +
                Duration.between(start, end).toNanos() + " ns");
    }
}
