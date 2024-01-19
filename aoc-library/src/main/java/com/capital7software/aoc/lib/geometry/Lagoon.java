package com.capital7software.aoc.lib.geometry;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/**
 * Thanks to your efforts, the machine parts factory is one of the first factories up and running
 * since the lavafall came back. However, to catch up with the large backlog of parts requests,
 * the factory will also need a large supply of lava for a while; the Elves have already started
 * creating a large lagoon nearby for this purpose.
 * <p><br>
 * However, they aren't sure the lagoon will be big enough; they've asked you to take a look at
 * the dig plan (your puzzle input). For example:
 * <p><br>
 * <code>
 * R 6 (#70c710)<br>
 * D 5 (#0dc571)<br>
 * L 2 (#5713f0)<br>
 * D 2 (#d2c081)<br>
 * R 2 (#59c680)<br>
 * D 2 (#411b91)<br>
 * L 5 (#8ceee2)<br>
 * U 2 (#caa173)<br>
 * L 1 (#1b58a2)<br>
 * U 2 (#caa171)<br>
 * R 2 (#7807d2)<br>
 * U 3 (#a77fa3)<br>
 * L 2 (#015232)<br>
 * U 2 (#7a21e3)<br>
 * </code>
 * <p><br>
 * The digger starts in a 1-meter cube hole in the ground. They then dig the specified
 * number of meters up (U), down (D), left (L), or right (R), clearing full 1-meter
 * cubes as they go. The directions are given as seen from above, so if "up" were north,
 * then "right" would be east, and so on. Each trench is also listed with the color that
 * the edge of the trench should be painted as an RGB hexadecimal color code.
 * <p><br>
 * When viewed from above, the above example dig plan would result in the following loop
 * of trench (#) having been dug out from otherwise ground-level terrain (.):
 * <p><br>
 * <code>
 * #######<br>
 * #.....#<br>
 * ###...#<br>
 * ..#...#<br>
 * ..#...#<br>
 * ###.###<br>
 * #...#..<br>
 * ##..###<br>
 * .#....#<br>
 * .######<br>
 * </code>
 * <p><br>
 * At this point, the trench could contain 38 cubic meters of lava. However, this is just
 * the edge of the lagoon; the next step is to dig out the interior so that it is one meter deep as well:
 * <p><br>
 * <code>
 * #######<br>
 * #######<br>
 * #######<br>
 * ..#####<br>
 * ..#####<br>
 * #######<br>
 * #####..<br>
 * #######<br>
 * .######<br>
 * .######<br>
 * </code>
 * <p><br>
 * Now, the lagoon can contain a much more respectable 62 cubic meters of lava. While the interior
 * is dug out, the edges are also painted according to the color codes in the dig plan.
 * <p><br>
 * The Elves are concerned the lagoon won't be large enough; if they follow their dig plan, how many
 * cubic meters of lava could it hold?
 * <p><br>
 * The Elves were right to be concerned; the planned lagoon would be much too small.
 * <p><br>
 * After a few minutes, someone realizes what happened; someone swapped the color and instruction
 * parameters when producing the dig plan. They don't have time to fix the bug; one of them asks
 * if you can extract the correct instructions from the hexadecimal codes.
 * <p><br>
 * Each hexadecimal code is six hexadecimal digits long. The first five hexadecimal digits encode
 * the distance in meters as a five-digit hexadecimal number. The last hexadecimal digit encodes
 * the direction to dig: 0 means R, 1 means D, 2 means L, and 3 means U.
 * <p><br>
 * So, in the above example, the hexadecimal codes can be converted into the true instructions:
 * <p><br>
 * <code>
 * #70c710 = R 461937<br>
 * #0dc571 = D 56407<br>
 * #5713f0 = R 356671<br>
 * #d2c081 = D 863240<br>
 * #59c680 = R 367720<br>
 * #411b91 = D 266681<br>
 * #8ceee2 = L 577262<br>
 * #caa173 = U 829975<br>
 * #1b58a2 = L 112010<br>
 * #caa171 = D 829975<br>
 * #7807d2 = L 491645<br>
 * #a77fa3 = U 686074<br>
 * #015232 = L 5411<br>
 * #7a21e3 = U 500254<br>
 * </code>
 * <p><br>
 * Digging out this loop and its interior produces a lagoon that can hold an impressive 952408144115
 * cubic meters of lava.
 * <p><br>
 * Convert the hexadecimal color codes into the correct instructions; if the Elves follow this new
 * dig plan, how many cubic meters of lava could the lagoon hold?
 *
 */
public class Lagoon {
    private record VertexInfo(@NotNull Direction direction, int length, @NotNull String color) {
        public Point2D<Double> getPoint(Point2D<Double> start) {
            var dxPoint = direction.delta();
            var newX = start.x() + dxPoint.x() * length;
            var newY = start.y() + dxPoint.y() * length;
            return new Point2D<>(newX, newY);
        }
    }

    private final Polygon2D<Double> polygon;

    /**
     * Instantiates a new Lagoon with the specified Polygon2D.
     *
     * @param polygon The Polygon2D that represents this Lagoon.
     */
    private Lagoon(Polygon2D<Double> polygon) {
        this.polygon = polygon;
    }

    /**
     * Builds a new Lagoon from the specified dig plan and the digging starts at the specified point. If
     * colorsAsInstructions is true then the colors are decoded as digging instructions.
     *
     * @param initialVertex        The initial point to start digging.
     * @param input                The List of Strings that contain the dig plan to parse.
     * @param colorsAsInstructions If true, then the colors in the dig plan are decoded as instructions.
     * @return A new Lagoon instance loaded with the specified dig plan.
     */
    public static @NotNull Lagoon buildLagoon(
            @NotNull Point2D<Double> initialVertex,
            @NotNull List<String> input,
            boolean colorsAsInstructions
    ) {
        var polygon = new Polygon2D<Double>();

        polygon.add(initialVertex);

        var infos = input
                .stream()
                .map(it -> Lagoon.parseLine(it, colorsAsInstructions))
                .filter(Objects::nonNull)
                .toList();

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

    /**
     * Calculates and returns the total area that is dug out.
     *
     * @return The total area that is dug out.
     */
    public double calculateArea() {
        return polygon.calculateTotalArea();
    }
}
