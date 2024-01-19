package com.capital7software.aoc.aoc2023.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.geometry.BrickBoard;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * --- Day 22: Sand Slabs ---<br><br>
 * Enough sand has fallen; it can finally filter water for Snow Island.
 * <p><br>
 * Well, almost.
 * <p><br>
 * The sand has been falling as large compacted bricks of sand, piling up to form an
 * impressive stack here near the edge of Island Island. In order to make use of the
 * sand to filter water, some of the bricks will need to be broken apart - nay,
 * disintegrated - back into freely flowing sand.
 * <p><br>
 * The stack is tall enough that you'll have to be careful about choosing which bricks to
 * disintegrate; if you disintegrate the wrong brick, large portions of the stack could
 * topple, which sounds pretty dangerous.
 * <p><br>
 * The Elves responsible for water filtering operations took a snapshot of the bricks while
 * they were still falling (your puzzle input) which should let you work out which bricks
 * are safe to disintegrate. For example:
 * <p><br>
 * <code>
 * 1,0,1~1,2,1<br>
 * 0,0,2~2,0,2<br>
 * 0,2,3~2,2,3<br>
 * 0,0,4~0,2,4<br>
 * 2,0,5~2,2,5<br>
 * 0,1,6~2,1,6<br>
 * 1,1,8~1,1,9<br>
 * </code>
 * <p><br>
 * Each line of text in the snapshot represents the position of a single brick at the time
 * the snapshot was taken. The position is given as two x,y,z coordinates - one for each
 * end of the brick - separated by a tilde (~). Each brick is made up of a single straight
 * line of cubes, and the Elves were even careful to choose a time for the snapshot that
 * had all the free-falling bricks at integer positions above the ground, so the whole
 * snapshot is aligned to a three-dimensional cube grid.
 * <p><br>
 * A line like 2,2,2~2,2,2 means that both ends of the brick are at the same coordinate -
 * in other words, that the brick is a single cube.
 * <p><br>
 * Lines like 0,0,10~1,0,10 or 0,0,10~0,1,10 both represent bricks that are two cubes in volume,
 * both oriented horizontally. The first brick extends in the x direction, while the second
 * brick extends in the y direction.
 * <p><br>
 * A line like 0,0,1~0,0,10 represents a ten-cube brick which is oriented vertically. One end of
 * the brick is the cube located at 0,0,1, while the other end of the brick is located directly
 * above it at 0,0,10.
 * <p><br>
 * The ground is at z=0 and is perfectly flat; the lowest z value a brick can have is therefore 1.
 * So, 5,5,1~5,6,1 and 0,2,1~0,2,5 are both resting on the ground, but 3,3,2~3,3,3 was above
 * the ground at the time of the snapshot.
 * <p><br>
 * Because the snapshot was taken while the bricks were still falling, some bricks will still
 * be in the air; you'll need to start by figuring out where they will end up. Bricks are
 * magically stabilized, so they never rotate, even in weird situations like where a long
 * horizontal brick is only supported on one end. Two bricks cannot occupy the same position,
 * so a falling brick will come to rest upon the first other brick it encounters.
 * <p><br>
 * Here is the same example again, this time with each brick given a letter, so it can be m
 * arked in diagrams:
 * <p><br>
 * <code>
 * 1,0,1~1,2,1   &#60;- A<br>
 * 0,0,2~2,0,2   &#60;- B<br>
 * 0,2,3~2,2,3   &#60;- C<br>
 * 0,0,4~0,2,4   &#60;- D<br>
 * 2,0,5~2,2,5   &#60;- E<br>
 * 0,1,6~2,1,6   &#60;- F<br>
 * 1,1,8~1,1,9   &#60;- G<br>
 * </code>
 * <p><br>
 * At the time of the snapshot, from the side so the x axis goes left to right, these bricks
 * are arranged like this:
 * <p><br>
 * <code>
 * &nbsp;x<br>
 * 012<br>
 * .G. 9<br>
 * .G. 8<br>
 * ... 7<br>
 * FFF 6<br>
 * ..E 5 z<br>
 * D.. 4<br>
 * CCC 3<br>
 * BBB 2<br>
 * .A. 1<br>
 * --- 0<br>
 * </code>
 * <p><br>
 * Rotating the perspective 90 degrees so the y axis now goes left to right, the same
 * bricks are arranged like this:
 * <p><br>
 * <code>
 * &nbsp;y<br>
 * 012<br>
 * .G. 9<br>
 * .G. 8<br>
 * ... 7<br>
 * .F. 6<br>
 * EEE 5 z<br>
 * DDD 4<br>
 * ..C 3<br>
 * B.. 2<br>
 * AAA 1<br>
 * --- 0<br>
 * </code>
 * <p><br>
 * Once all the bricks fall downward as far as they can go, the stack looks like this,
 * where ? means bricks are hidden behind other bricks at that location:
 * <p><br>
 * <code>
 * &nbsp;x<br>
 * 012<br>
 * .G. 6<br>
 * .G. 5<br>
 * FFF 4<br>
 * D.E 3 z<br>
 * ??? 2<br>
 * .A. 1<br>
 * --- 0<br>
 * </code>
 * <p><br>
 * Again from the side:
 * <p><br>
 * <code>
 * &nbsp;y<br>
 * 012<br>
 * .G. 6<br>
 * .G. 5<br>
 * .F. 4<br>
 * ??? 3 z<br>
 * B.C 2<br>
 * AAA 1<br>
 * --- 0<br>
 * </code>
 * <p><br>
 * Now that all the bricks have settled, it becomes easier to tell which bricks are supporting which other bricks:
 * <p><br>
 * <code>
 * Brick A is the only brick supporting bricks B and C.<br>
 * Brick B is one of two bricks supporting brick D and brick E.<br>
 * Brick C is the other brick supporting brick D and brick E.<br>
 * Brick D supports brick F.<br>
 * Brick E also supports brick F.<br>
 * Brick F supports brick G.<br>
 * Brick G isn't supporting any bricks.<br>
 * </code>
 * <p><br>
 * Your first task is to figure out which bricks are safe to disintegrate. A brick can be safely
 * disintegrated if, after removing it, no other bricks would fall further directly downward.
 * Don't actually disintegrate any bricks - just determine what would happen if, for each brick,
 * only that brick were disintegrated. Bricks can be disintegrated even if they're completely
 * surrounded by other bricks; you can squeeze between bricks if you need to.
 * <p><br>
 * In this example, the bricks can be disintegrated as follows:
 * <p><br>
 * <code>
 * Brick A cannot be disintegrated safely; if it were disintegrated, bricks B and C would both fall.<br>
 * Brick B can be disintegrated; the bricks above it (D and E) would still be supported by brick C.<br>
 * Brick C can be disintegrated; the bricks above it (D and E) would still be supported by brick B.<br>
 * Brick D can be disintegrated; the brick above it (F) would still be supported by brick E.<br>
 * Brick E can be disintegrated; the brick above it (F) would still be supported by brick D.<br>
 * Brick F cannot be disintegrated; the brick above it (G) would fall.<br>
 * Brick G can be disintegrated; it does not support any other bricks.<br>
 * </code>
 * <p><br>
 * So, in this example, 5 bricks can be safely disintegrated.
 * <p><br>
 * Figure how the blocks will settle based on the snapshot. Once they've settled, consider
 * disintegrating a single brick; how many bricks could be safely chosen as the one to get disintegrated?
 * <p><br>
 * Your puzzle answer was 530.
 * <p><br>
 * --- Part Two ---<br><br>
 * Disintegrating bricks one at a time isn't going to be fast enough. While it might sound dangerous,
 * what you really need is a chain reaction.
 * <p><br>
 * You'll need to figure out the best brick to disintegrate. For each brick, determine how many other
 * bricks would fall if that brick were disintegrated.
 * <p><br>
 * Using the same example as above:
 * <p><br>
 * <code>
 * Disintegrating brick A would cause all 6 other bricks to fall.<br>
 * Disintegrating brick F would cause only 1 other brick, G, to fall.<br>
 * Disintegrating any other brick would cause no other bricks to fall.<br>
 * </code>
 * <p><br>
 * So, in this example, the sum of the number of other bricks that would fall as
 * a result of disintegrating each brick is 7.
 * <p><br>
 * For each brick, determine how many other bricks would fall if that brick were disintegrated.
 * What is the sum of the number of other bricks that would fall?
 * <p><br>
 * Your puzzle answer was 93292.
 */
public class Day22 implements AdventOfCodeSolution {
    private static final Logger LOGGER = Logger.getLogger(Day22.class.getName());

    /**
     * Instantiates this Solution instance.
     */
    public Day22() {

    }

    @Override
    public String getDefaultInputFilename() {
        return "inputs/input_day_22-01.txt";
    }

    @Override
    public void runPart1(List<String> input) {
        var start = Instant.now();
        var answer = totalNumberOfBricksThatCanBeSafelyDisintegrated(input);
        var end = Instant.now();

        LOGGER.info(String.format("Total number of bricks that can be safely disintegrated is: %d",
                answer));
        logTimings(LOGGER, start, end);
    }

    @Override
    public void runPart2(List<String> input) {
        var start = Instant.now();
        var answer = sumOfFallingBricksInAChainReaction(input);
        var end = Instant.now();

        LOGGER.info(String.format("Total number of bricks that can be safely disintegrated is: %d",
                answer));
        logTimings(LOGGER, start, end);
    }

    /**
     * Returns the total number of Bricks that can be disintegrated without causing any other
     * Bricks to fall.
     *
     * @param input The List of Strings to parse into Bricks for the BrickBoard.
     * @return The total number of Bricks that can be disintegrated without causing any other
     * Bricks to fall.
     */
    public long totalNumberOfBricksThatCanBeSafelyDisintegrated(List<String> input) {
        var board = BrickBoard.buildBrickBoard(input);
        board.fallDownward();
        return board.safeToDisintegrate().size();
    }

    /**
     * Returns the sum of the Bricks that would fall in a chain-reaction.
     *
     * @param input The List of Strings to parse into Bricks for the BrickBoard.
     * @return The sum of the Bricks that would fall in a chain-reaction.
     */
    public long sumOfFallingBricksInAChainReaction(List<String> input) {
        var board = BrickBoard.buildBrickBoard(input);
        board.fallDownward();
        return board
                .chainReaction()
                .values()
                .stream()
                .mapToLong(Set::size)
                .sum();
    }
}
