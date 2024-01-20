package com.capital7software.aoc.aoc2023.days;

import com.capital7software.aoc.lib.AdventOfCodeSolution;
import com.capital7software.aoc.lib.analysis.ScratchCards;

import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * --- Day 4: Scratchcards ---<br><br>
 * The gondola takes you up. Strangely, though, the ground doesn't seem to be coming with you;
 * you're not climbing a mountain. As the circle of Snow Island recedes below you, an entire
 * new landmass suddenly appears above you! The gondola carries you to the surface of the new
 * island and lurches into the station.
 * <p><br>
 * As you exit the gondola, the first thing you notice is that the air here is much warmer
 * than it was on Snow Island. It's also quite humid. Is this where the water source is?
 * <p><br>
 * The next thing you notice is an Elf sitting on the floor across the station in what seems
 * to be a pile of colorful square cards.
 * <p><br>
 * "Oh! Hello!" The Elf excitedly runs over to you. "How may I be of service?" You ask about
 * water sources.
 * <p><br>
 * "I'm not sure; I just operate the gondola lift. That does sound like something we'd have,
 * though - this is Island Island, after all! I bet the gardener would know. He's on a
 * different island, though - er, the small kind surrounded by water, not the floating kind.
 * We really need to come up with a better naming scheme. Tell you what: if you can help me
 * with something quick, I'll let you borrow my boat and you can go visit the gardener.
 * I got all these scratchcards as a gift, but I can't figure out what I've won."
 * <p><br>
 * The Elf leads you over to the pile of colorful cards. There, you discover dozens of
 * scratchcards, all with their opaque covering already scratched off. Picking one up, it
 * looks like each card has two lists of numbers separated by a vertical bar (|): a list
 * of winning numbers and then a list of numbers you have. You organize the information
 * into a table (your puzzle input).
 * <p><br>
 * As far as the Elf has been able to figure out, you have to figure out which of the numbers
 * you have appeared in the list of winning numbers. The first match makes the card worth one
 * point and each match after the first doubles the point value of that card.
 * <p><br>
 * For example:
 * <p><br>
 * <code>
 * Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53<br>
 * Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19<br>
 * Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1<br>
 * Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83<br>
 * Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36<br>
 * Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11<br>
 * </code>
 * <p><br>
 * In the above example, card 1 has five winning numbers (41, 48, 83, 86, and 17) and eight
 * numbers you have (83, 86, 6, 31, 17, 9, 48, and 53). Of the numbers you have, four of them
 * (48, 83, 17, and 86) are winning numbers! That means card 1 is worth 8 points (1 for the
 * first match, then doubled three times for each of the three matches after the first).
 * <p><br>
 * <code>
 * Card 2 has two winning numbers (32 and 61), so it is worth 2 points.<br>
 * Card 3 has two winning numbers (1 and 21), so it is worth 2 points.<br>
 * Card 4 has one winning number (84), so it is worth 1 point.<br>
 * Card 5 has no winning numbers, so it is worth no points.<br>
 * Card 6 has no winning numbers, so it is worth no points.<br>
 * </code>
 * <p><br>
 * So, in this example, the Elf's pile of scratchcards is worth 13 points.
 * <p><br>
 * Take a seat in the large pile of colorful cards. How many points are they worth in total?
 * <p><br>
 * Your puzzle answer was 20117.
 * <p><br>
 * --- Part Two ---<br><br>
 * Just as you're about to report your findings to the Elf, one of you realizes that the
 * rules have actually been printed on the back of every card this whole time.
 * <p><br>
 * There's no such thing as "points". Instead, scratchcards only cause you to win more
 * scratchcards equal to the number of winning numbers you have.
 * <p><br>
 * Specifically, you win copies of the scratchcards below the winning card equal to the
 * number of matches. So, if card 10 were to have 5 matching numbers, you would win one
 * copy each of cards 11, 12, 13, 14, and 15.
 * <p><br>
 * Copies of scratchcards are scored like normal scratchcards and have the same card number
 * as the card they copied. So, if you win a copy of card 10, and it has 5 matching numbers,
 * it would then win a copy of the same cards that the original card 10 won:
 * cards 11, 12, 13, 14, and 15. This process repeats until none of the copies cause you
 * to win any more cards. (Cards will never make you copy a card past the end of the table.)
 * <p><br>
 * This time, the above example goes differently:
 * <p><br>
 * <code>
 * Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53<br>
 * Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19<br>
 * Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1<br>
 * Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83<br>
 * Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36<br>
 * Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11<br>
 * </code>
 * <p><br>
 * Card 1 has four matching numbers, so you win one copy each of the next four cards: cards 2, 3, 4, and 5.<br>
 * Your original card 2 has two matching numbers, so you win one copy each of cards 3 and 4.<br>
 * Your copy of card 2 also wins one copy each of cards 3 and 4.<br>
 * Your four instances of card 3 (one original and three copies) have two matching numbers,
 * so you win four copies each of cards 4 and 5.<br>
 * Your eight instances of card 4 (one original and seven copies) have one matching number,
 * so you win eight copies of card 5.<br>
 * Your fourteen instances of card 5 (one original and thirteen copies) have no matching
 * numbers and win no more cards.<br>
 * Your one instance of card 6 (one original) has no matching numbers and wins no more cards.<br>
 * <p><br>
 * Once all the originals and copies have been processed, you end up with 1 instance of card 1, 2
 * instances of card 2, 4 instances of card 3, 8 instances of card 4, 14 instances of card 5,
 * and 1 instance of card 6. In total, this example pile of scratchcards causes you to ultimately
 * have 30 scratchcards!
 * <p><br>
 * Process all the original and copied scratchcards until no more scratchcards are won. Including
 * the original set of scratchcards, how many total scratchcards do you end up with?
 * <p><br>
 * Your puzzle answer was 13768818.
 */
public class Day04 implements AdventOfCodeSolution {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day04.class);

    /**
     * Instantiates the solution instance.
     */
    public Day04() {

    }

    @Override
    public String getDefaultInputFilename() {
        return "inputs/input_day_04-01.txt";
    }

    @Override
    public void runPart1(List<String> input) {
        var start = Instant.now();
        var sum = getSumOfAllCardPoints(input);
        var end = Instant.now();

        LOGGER.info("The sum of all scratch card points is: {}", sum);
        logTimings(LOGGER, start, end);
    }

    @Override
    public void runPart2(List<String> input) {
        var start = Instant.now();
        var sum = getTotalNumberOfCards(input);
        var end = Instant.now();

        LOGGER.info("The total number of scratch card in hand is: {}", sum);
        logTimings(LOGGER, start, end);
    }

    /**
     * Returns the sum of all ScratchCard points from the specified cards to parse.
     *
     * @param input The cards to parse and calculate their points.
     * @return The sum of all ScratchCard points from the specified cards to parse.
     */
    public long getSumOfAllCardPoints(List<String> input) {
        var instance = new ScratchCards();

        return instance.getCardPoints(input).stream().mapToLong(it -> it).sum();
    }

    /**
     * Returns the total number of ScratchCards (originals + copies).
     *
     * @param input The cards to parse calculate copies of.
     * @return The total number of ScratchCards (originals + copies)
     */
    public long getTotalNumberOfCards(List<String> input) {
        var instance = new ScratchCards();
        var results = instance.getTotalCopyCount(input);
        return results.stream().mapToLong(it -> it).sum() + results.size();
    }
}
