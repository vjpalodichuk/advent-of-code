package com.capital7software.aoc.aoc2023.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.math.CamelCards;

import java.time.Instant;
import java.util.List;
import java.util.logging.Logger;

/**
 * --- Day 7: Camel Cards ---<br><br>
 * Your all-expenses-paid trip turns out to be a one-way, five-minute ride in an airship. (At least
 * it's a cool airship!) It drops you off at the edge of a vast desert and descends back to Island Island.
 * <p><br>
 * "Did you bring the parts?"
 * <p><br>
 * You turn around to see an Elf completely covered in white clothing, wearing goggles, and riding a large camel.
 * <p><br>
 * "Did you bring the parts?" she asks again, louder this time. You aren't sure what parts
 * she's looking for; you're here to figure out why the sand stopped.
 * <p><br>
 * "The parts! For the sand, yes! Come with me; I will show you." She beckons you onto the camel.
 * <p><br>
 * After riding a bit across the sands of Desert Island, you can see what look like very large
 * rocks covering half of the horizon. The Elf explains that the rocks are all along the part
 * of Desert Island that is directly above Island Island, making it hard to even get there.
 * Normally, they use big machines to move the rocks and filter the sand, but the machines have
 * broken down because Desert Island recently stopped receiving the parts they need to fix the machines.
 * <p><br>
 * You've already assumed it'll be your job to figure out why the parts stopped when she asks
 * if you can help. You agree automatically.
 * <p><br>
 * Because the journey will take a few days, she offers to teach you the game of Camel Cards.
 * Camel Cards is sort of similar to poker except it's designed to be easier to play while riding a camel.
 * <p><br>
 * In Camel Cards, you get a list of hands, and your goal is to order them based on the
 * strength of each hand. A hand consists of five cards labeled one of A, K, Q, J, T, 9, 8, 7, 6, 5, 4, 3, or 2.
 * The relative strength of each card follows this order, where A is the highest and 2 is the lowest.
 * <p><br>
 * Every hand is exactly one type. From strongest to weakest, they are:<br>
 * <p><br>
 * <b>Five of a kind</b>, where all five cards have the same label: AAAAA<br>
 * <b>Four of a kind</b>, where four cards have the same label and one card has a
 * different label: AA8AA<br>
 * <b>Full house</b>, where three cards have the same label, and the remaining two
 * cards share a different label: 23332<br>
 * <b>Three of a kind</b>, where three cards have the same label, and the remaining
 * two cards are each different from any other card in the hand: TTT98<br>
 * <b>Two pair</b>, where two cards share one label, two other cards share a second
 * label, and the remaining card has a third label: 23432<br>
 * <b>One pair</b>, where two cards share one label, and the other three cards have
 * a different label from the pair and each other: A23A4<br>
 * <b>High card</b>, where all cards' labels are distinct: 23456<br>
 * <p><br>
 * Hands are primarily ordered based on type; for example, every full house is stronger than any three of a kind.
 * <p><br>
 * If two hands have the same type, a second ordering rule takes effect. Start by comparing
 * the first card in each hand. If these cards are different, the hand with the stronger
 * first card is considered stronger. If the first card in each hand have the same label,
 * however, then move on to considering the second card in each hand. If they differ, the
 * hand with the higher second card wins; otherwise, continue with the third card in each
 * hand, then the fourth, then the fifth.
 * <p><br>
 * So, 33332 and 2AAAA are both four of a kind hands, but 33332 is stronger because its
 * first card is stronger. Similarly, 77888 and 77788 are both a full house, but 77888
 * is stronger because its third card is stronger (and both hands have the same first and second card).
 * <p><br>
 * To play Camel Cards, you are given a list of hands and their corresponding bid (your puzzle input). For example:
 * <p><br>
 * <code>
 * 32T3K 765<br>
 * T55J5 684<br>
 * KK677 28<br>
 * KTJJT 220<br>
 * QQQJA 483<br>
 * </code>
 * <p><br>
 * This example shows five hands; each hand is followed by its bid amount. Each hand wins an
 * amount equal to its bid multiplied by its rank, where the weakest hand gets rank 1, the
 * second-weakest hand gets rank 2, and so on up to the strongest hand. Because there are
 * five hands in this example, the strongest hand will have rank 5 and its bid will be multiplied by 5.
 * <p><br>
 * So, the first step is to put the hands in order of strength:
 * <p><br>
 * 32T3K is the only one pair and the other hands are all a stronger type, so it gets rank 1.<br>
 * KK677 and KTJJT are both two pair. Their first cards both have the same label, but the second
 * card of KK677 is stronger (K vs T), so KTJJT gets rank 2 and KK677 gets rank 3.<br>
 * T55J5 and QQQJA are both three of a kind. QQQJA has a stronger first card, so it gets
 * rank 5 and T55J5 gets rank 4.<br>
 * Now, you can determine the total winnings of this set of hands by adding up the result
 * of multiplying each hand's bid with its rank (765 * 1 + 220 * 2 + 28 * 3 + 684 * 4 + 483 * 5).
 * So the total winnings in this example are 6440.<br>
 * <p><br>
 * Find the rank of every hand in your set. What are the total winnings?
 * <p><br>
 * Your puzzle answer was 251287184.
 * <p><br>
 * --- Part Two ---<br><br>
 * To make things a little more interesting, the Elf introduces one additional rule. Now, J cards
 * are jokers - wildcards that can act like whatever card would make the hand the strongest type possible.
 * <p><br>
 * To balance this, J cards are now the weakest individual cards, weaker even than 2. The
 * other cards stay in the same order: A, K, Q, T, 9, 8, 7, 6, 5, 4, 3, 2, J.
 * <p><br>
 * J cards can pretend to be whatever card is best for the purpose of determining hand type;
 * for example, QJJQ2 is now considered four of a kind. However, for the purpose of breaking
 * ties between two hands of the same type, J is always treated as J, not the card it's pretending
 * to be: JKKK2 is weaker than QQQQ2 because J is weaker than Q.
 * <p><br>
 * Now, the above example goes very differently:
 * <p><br>
 * <code>
 * 32T3K 765<br>
 * T55J5 684<br>
 * KK677 28<br>
 * KTJJT 220<br>
 * QQQJA 483<br>
 * </code>
 * <p><br>
 * 32T3K is still the only one pair; it doesn't contain any jokers, so its strength doesn't increase.<br>
 * KK677 is now the only two pair, making it the second-weakest hand.<br>
 * T55J5, KTJJT, and QQQJA are now all four of a kind! T55J5 gets rank 3, QQQJA gets rank 4,
 * and KTJJT gets rank 5.<br>
 * <p><br>
 * With the new joker rule, the total winnings in this example are 5905.<br>
 * <p><br>
 * Using the new joker rule, find the rank of every hand in your set. What are the new total winnings?
 * <p><br>
 * Your puzzle answer was 250757288.
 *
 */
public class Day07 implements AdventOfCodeSolution {
    private static final Logger LOGGER = Logger.getLogger(Day07.class.getName());

    /**
     * Instantiates the solution instance.
     */
    public Day07() {

    }

    @Override
    public String getDefaultInputFilename() {
        return "inputs/input_day_07-01.txt";
    }

    @Override
    public void runPart1(List<String> input) {
        var start = Instant.now();
        var answer = calculateTotalWinnings(input, false);
        var end = Instant.now();

        LOGGER.info(String.format("The total winnings using Jacks is: %d%n", answer));
        logTimings(LOGGER, start, end);
    }

    @Override
    public void runPart2(List<String> input) {
        var start = Instant.now();
        var answer = calculateTotalWinnings(input, true);
        var end = Instant.now();

        LOGGER.info(String.format("The total winnings using Jokers is: %d%n", answer));
        logTimings(LOGGER, start, end);
    }

    /**
     * Calculates and returns the total winnings of the Hands specified in the List of Strings.
     * If joker is true, then Jacks are replaced with Jokers.
     *
     * @param input The List of Strings to parse.
     * @param joker If true, then Jacks are replaced with Jokers.
     * @return The total winnings of the Hands in this set.
     */
    public long calculateTotalWinnings(List<String> input, boolean joker) {
        var instance = CamelCards.buildCamelCards(input, joker);
        return instance.calculateTotalWinnings();
    }
}
