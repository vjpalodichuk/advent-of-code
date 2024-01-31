package com.capital7software.aoc.lib.analysis;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

/**
 * The Elf leads you over to the pile of colorful cards. There, you discover dozens of
 * scratchcards, all with their opaque covering already scratched off. Picking one up, it
 * looks like each card has two lists of numbers separated by a vertical bar (|): a list
 * of winning numbers and then a list of numbers you have. You organize the information
 * into a table (your puzzle input).
 *
 * <p><br>
 * As far as the Elf has been able to figure out, you have to figure out which of the numbers
 * you have appeared in the list of winning numbers. The first match makes the card worth one
 * point and each match after the first doubles the point value of that card.
 *
 * <p><br>
 * For example:
 *
 * <p><br>
 * <code>
 * Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53<br>
 * Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19<br>
 * Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1<br>
 * Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83<br>
 * Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36<br>
 * Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11<br>
 * </code>
 *
 * <p><br>
 * In the above example, card 1 has five winning numbers (41, 48, 83, 86, and 17) and eight
 * numbers you have (83, 86, 6, 31, 17, 9, 48, and 53). Of the numbers you have, four of them
 * (48, 83, 17, and 86) are winning numbers! That means card 1 is worth 8 points (1 for the
 * first match, then doubled three times for each of the three matches after the first).
 *
 * <p><br>
 * <code>
 * Card 2 has two winning numbers (32 and 61), so it is worth 2 points.<br>
 * Card 3 has two winning numbers (1 and 21), so it is worth 2 points.<br>
 * Card 4 has one winning number (84), so it is worth 1 point.<br>
 * Card 5 has no winning numbers, so it is worth no points.<br>
 * Card 6 has no winning numbers, so it is worth no points.<br>
 * </code>
 *
 * <p><br>
 * So, in this example, the Elf's pile of scratchcards is worth 13 points.
 *
 * <p><br>
 * Take a seat in the large pile of colorful cards. How many points are they worth in total?
 *
 * <p><br>
 * Just as you're about to report your findings to the Elf, one of you realizes that the
 * rules have actually been printed on the back of every card this whole time.
 *
 * <p><br>
 * There's no such thing as "points". Instead, scratchcards only cause you to win more
 * scratchcards equal to the number of winning numbers you have.
 *
 * <p><br>
 * Specifically, you win copies of the scratchcards below the winning card equal to the
 * number of matches. So, if card 10 were to have 5 matching numbers, you would win one
 * copy each of cards 11, 12, 13, 14, and 15.
 *
 * <p><br>
 * Copies of scratchcards are scored like normal scratchcards and have the same card number
 * as the card they copied. So, if you win a copy of card 10, and it has 5 matching numbers,
 * it would then win a copy of the same cards that the original card 10 won:
 * cards 11, 12, 13, 14, and 15. This process repeats until none of the copies cause you
 * to win any more cards. (Cards will never make you copy a card past the end of the table.)
 *
 * <p><br>
 * This time, the above example goes differently:
 *
 * <p><br>
 * <code>
 * Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53<br>
 * Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19<br>
 * Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1<br>
 * Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83<br>
 * Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36<br>
 * Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11<br>
 * </code>
 *
 * <p><br>
 * Card 1 has four matching numbers, so you win one copy each of the next four cards:
 * cards 2, 3, 4, and 5.<br>
 * Your original card 2 has two matching numbers, so you win one copy each of cards 3 and 4.<br>
 * Your copy of card 2 also wins one copy each of cards 3 and 4.<br>
 * Your four instances of card 3 (one original and three copies) have two matching numbers,
 * so you win four copies each of cards 4 and 5.<br>
 * Your eight instances of card 4 (one original and seven copies) have one matching number,
 * so you win eight copies of card 5.<br>
 * Your fourteen instances of card 5 (one original and thirteen copies) have no matching
 * numbers and win no more cards.<br>
 * Your one instance of card 6 (one original) has no matching numbers and wins no more cards.<br>
 *
 * <p><br>
 * Once all the originals and copies have been processed, you end up with 1 instance of card 1, 2
 * instances of card 2, 4 instances of card 3, 8 instances of card 4, 14 instances of card 5,
 * and 1 instance of card 6. In total, this example pile of scratchcards causes you to ultimately
 * have 30 scratchcards!
 *
 * <p><br>
 * Process all the original and copied scratchcards until no more scratchcards are won. Including
 * the original set of scratchcards, how many total scratchcards do you end up with?
 */
public class ScratchCards {
  private static final String FIRST_SPLIT = ": ";
  private static final String NUMBERS_SPLIT = " \\| ";
  private static final String VALUE_SPLIT = " ";

  private record ScratchCard(
      int id,
      @NotNull Set<Integer> winningNumbers,
      @NotNull Set<Integer> cardNumbers
  ) {
    public int calculatePoints() {
      var intersection = getWinners();

      var count = intersection.size();
      var points = 0;

      if (count > 0) {
        points = (int) Math.pow(2, count - 1);
      }

      return points;
    }

    public @NotNull Set<Integer> getWinners() {
      var intersection = new HashSet<>(winningNumbers);
      intersection.retainAll(cardNumbers);
      return intersection;
    }

    @Override
    public String toString() {
      return "ScratchCard{"
          + "id=" + id
          + ", points=" + calculatePoints()
          + ", winners=" + getWinners()
          + ", winningNumbers=" + winningNumbers
          + ", cardNumbers=" + cardNumbers
          + '}';
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      ScratchCard that = (ScratchCard) o;
      return id == that.id;
    }

    @Override
    public int hashCode() {
      return Objects.hash(id);
    }
  }

  /**
   * Instantiates a new empty instance.
   */
  public ScratchCards() {

  }

  /**
   * Returns a List of the points for each card.
   *
   * @param input The cards to parse and get the points for.
   * @return A List of the points for each card.
   */
  public @NotNull List<Integer> getCardPoints(@NotNull List<String> input) {
    return input
        .stream()
        .map(this::convertToScratchCard)
        .filter(Objects::nonNull)
        .map(ScratchCard::calculatePoints)
        .toList();
  }

  /**
   * Returns a List of the number of copies for each original card and their copies.
   * The total does not include the original card itself!
   *
   * @param input The cards to parse and get the points for.
   * @return A List of the number of copies for each original card and their copies.
   */
  public @NotNull List<Integer> getTotalCopyCount(@NotNull List<String> input) {
    var cards = input
        .stream()
        .map(this::convertToScratchCard)
        .toList();
    var map = new HashMap<Integer, Integer>();

    return cards.stream().map(card -> getTotalCopyCount(card, cards, map)).toList();
  }

  private int getTotalCopyCount(
      @NotNull ScratchCard card,
      @NotNull List<ScratchCard> cards,
      @NotNull Map<Integer, Integer> cardMap
  ) {
    int size = cardMap.computeIfAbsent(card.id(), it -> card.getWinners().size());

    if (size == 0) {
      return 0;
    }

    var winCount = 0;

    for (int i = card.id(); i < card.id() + size; i++) {
      winCount++;
      winCount += getTotalCopyCount(cards.get(i), cards, cardMap);
    }

    return winCount;
  }

  private ScratchCard convertToScratchCard(String input) {
    if (input == null || input.isEmpty()) {
      return null;
    }

    String[] initialSplit = input.split(FIRST_SPLIT);

    if (initialSplit.length != 2) {
      throw new RuntimeException("Got more than two results from the initial split!");
    }

    var cardId = getCardId(initialSplit[0]);

    var numberSplits = initialSplit[1].split(NUMBERS_SPLIT);

    var winningNumbers = getNumbers(numberSplits[0]);
    var cardNumbers = getNumbers(numberSplits[1]);

    return new ScratchCard(cardId, winningNumbers, cardNumbers);
  }

  private @NotNull Set<Integer> getNumbers(@NotNull String input) {
    var splits = input.split(VALUE_SPLIT);

    return Arrays.stream(splits)
        .filter(it -> !it.trim().isEmpty())
        .map(Integer::parseInt)
        .collect(Collectors.toSet());
  }

  private int getCardId(@NotNull String input) {
    var splits = input.split(VALUE_SPLIT);

    var filtered = Arrays.stream(splits)
        .filter(it -> !it.trim().isEmpty())
        .toList();

    if (filtered.size() != 2) {
      throw new RuntimeException("Got more than two results from the card id split!");
    }

    return Integer.parseInt(filtered.get(1));
  }
}
