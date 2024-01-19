package com.capital7software.aoc.lib.math;

import com.capital7software.aoc.lib.collection.PriorityQueue;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The classic game of CamelCards!<br>
 * <p><br>
 * To play Camel Cards, you are given a list of hands and their corresponding bid (your puzzle input).
 * For example:
 * <p><br>
 * <code>
 * 32T3K 765<br>
 * T55J5 684<br>
 * KK677 28<br>
 * KTJJT 220<br>
 * QQQJA 483<br>
 * </code>
 * <p><br>
 * This example shows five hands; each hand is followed by its bid amount. Each hand wins an amount equal
 * to its bid multiplied by its rank, where the weakest hand gets rank 1, the second-weakest hand gets
 * rank 2, and so on up to the strongest hand. Because there are five hands in this example,
 * the strongest hand will have rank 5 and its bid will be multiplied by 5.
 * <p><br>
 * So, the first step is to put the hands in order of strength:
 * <p><br>
 * 32T3K is the only one pair and the other hands are all a stronger type, so it gets rank 1.
 * KK677 and KTJJT are both two pair. Their first cards both have the same label, but the second
 * card of KK677 is stronger (K vs T), so KTJJT gets rank 2 and KK677 gets rank 3.
 * T55J5 and QQQJA are both three of a kind. QQQJA has a stronger first card, so it gets
 * rank 5 and T55J5 gets rank 4.
 * Now, you can determine the total winnings of this set of hands by adding up the result of
 * multiplying each hand's bid with its rank (765 * 1 + 220 * 2 + 28 * 3 + 684 * 4 + 483 * 5).
 * So the total winnings in this example are 6440.
 * <p><br>
 * To make things a little more interesting, the Elf introduces an optional rule. Now,
 * J cards are jokers - wildcards that can act like whatever card would make the hand the strongest type possible.
 * <p><br>
 * To balance this, J cards are now the weakest individual cards, weaker even than 2.
 * The other cards stay in the same order: A, K, Q, T, 9, 8, 7, 6, 5, 4, 3, 2, J.
 * <p><br>
 * J cards can pretend to be whatever card is best for the purpose of determining hand type; for example,
 * QJJQ2 is now considered four of a kind. However, for the purpose of breaking ties between two hands of
 * the same type, J is always treated as J, not the card it's pretending to be: JKKK2 is weaker
 * than QQQQ2 because J is weaker than Q.
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
 * 32T3K is still the only one pair; it doesn't contain any jokers, so its strength doesn't increase.
 * KK677 is now the only two pair, making it the second-weakest hand.
 * T55J5, KTJJT, and QQQJA are now all four of a kind! T55J5 gets rank 3, QQQJA gets rank 4, and KTJJT gets rank 5.
 * With the new joker rule, the total winnings in this example are 5905.
 *
 */
public class CamelCards {

    /**
     * The various cards and their values.
     */
    public enum Card {
        /**
         * The Ace.
         */
        ACE('A', 13),
        /**
         * The King.
         */
        KING('K', 12),
        /**
         * The Queen.
         */
        QUEEN('Q', 11),
        /**
         * The Jack.
         */
        JACK('J', 10),
        /**
         * The Ten.
         */
        TEN('T', 9),
        /**
         * The Nine.
         */
        NINE('9', 8),
        /**
         * The Eight.
         */
        EIGHT('8', 7),
        /**
         * The Seven.
         */
        SEVEN('7', 6),
        /**
         * The Six.
         */
        SIX('6', 5),
        /**
         * The Five.
         */
        FIVE('5', 4),
        /**
         * The Four.
         */
        FOUR('4', 3),
        /**
         * The Three.
         */
        THREE('3', 2),
        /**
         * The Two.
         */
        TWO('2', 1),
        /**
         * The Joker.
         */
        JOKER('J', 0);

        private final char label;
        private final int value;

        Card(char label, int value) {
            this.label = label;
            this.value = value;
        }

        /**
         * Returns the label of the card.
         *
         * @return The label of the card.
         */
        public char getLabel() {
            return label;
        }

        /**
         * Returns the value of the card.
         *
         * @return The value of the card.
         */
        public int getValue() {
            return value;
        }

        @Override
        public @NotNull String toString() {
            return "Card{" +
                    "name=" + name() +
                    ", label=" + label +
                    ", value=" + value +
                    '}';
        }

        /**
         * Returns the Card instance that corresponds to the specified label.
         *
         * @param label The label of the Card to get.
         * @return The Card instance that corresponds to the specified label.
         */
        public static Card fromLabel(char label) {
            return Arrays
                    .stream(values())
                    .filter(it -> it.label == label)
                    .findFirst()
                    .orElse(null);
        }

        /**
         * Returns the Card instance that corresponds to the specified label. If joker is true
         * then jacks are filtered out as they share a label with jokers.
         *
         * @param label The label of the Card to get.
         * @param joker If true, then jacks are filtered out.
         * @return The Card instance that corresponds to the specified label.
         */
        public static Card fromLabel(char label, boolean joker) {
            if (joker) {
                return Arrays
                        .stream(values())
                        .filter(it -> !it.name().equals("JACK"))
                        .filter(it -> it.label == label)
                        .findFirst()
                        .orElse(null);
            }
            return fromLabel(label);
        }
    }

    /**
     * The available hand types and their strength.
     * Listed in the order of strength.
     */
    public enum HandType {
        /**
         * Five of a Kind.
         */
        FIVE_OF_A_KIND(7) {
            @Override
            public boolean isHandType(@NotNull Map<Integer, Integer> frequencyMap) {
                return frequencyMap.containsKey(5);
            }
        },
        /**
         * Four of a Kind.
         */
        FOUR_OF_A_KIND(6) {
            @Override
            public boolean isHandType(@NotNull Map<Integer, Integer> frequencyMap) {
                return frequencyMap.containsKey(4);
            }
        },
        /**
         * Full House.
         */
        FULL_HOUSE(5) {
            @Override
            public boolean isHandType(@NotNull Map<Integer, Integer> frequencyMap) {
                return frequencyMap.containsKey(3) && frequencyMap.containsKey(2);
            }
        },
        /**
         * Three of a Kind.
         */
        THREE_OF_A_KIND(4) {
            @Override
            public boolean isHandType(@NotNull Map<Integer, Integer> frequencyMap) {
                return frequencyMap.containsKey(3) && !frequencyMap.containsKey(2);
            }
        },
        /**
         * Two Pair.
         */
        TWO_PAIR(3) {
            @Override
            public boolean isHandType(@NotNull Map<Integer, Integer> frequencyMap) {
                return frequencyMap.getOrDefault(2, 0) == 2;
            }
        },
        /**
         * One Pair.
         */
        ONE_PAIR(2) {
            @Override
            public boolean isHandType(@NotNull Map<Integer, Integer> frequencyMap) {
                return frequencyMap.getOrDefault(2, 0) == 1 &&
                        frequencyMap.getOrDefault(3, 0) == 0;
            }
        },
        /**
         * High Card.
         */
        HIGH_CARD(1) {
            @Override
            public boolean isHandType(@NotNull Map<Integer, Integer> frequencyMap) {
                return frequencyMap.getOrDefault(1, 0) == 5;
            }
        };

        private final int strength;

        HandType(int strength) {
            this.strength = strength;
        }

        /**
         * Returns the HandType given the specified List of Cards. If joker is set to true, then Jacks are
         * filtered out in favor of Jokers.
         *
         * @param cards The List of five cards to determine the HandType for.
         * @param joker If true, then Jokers replace Jacks.
         * @return The HandType given the specified List of Cards
         */
        public static HandType calculateHandType(@NotNull List<Card> cards, boolean joker) {
            var cardMap = buildCardMap(cards);

            if (joker && cardMap.containsKey(Card.JOKER)) {
                // If we are playing with Jokers, then we remove the Jokers from the map and add their count to
                // the highest frequency card in the map!
                var jokerCount = cardMap.get(Card.JOKER);

                cardMap.remove(Card.JOKER);
                Card maxCard = Card.JOKER;
                int maxCount = 0;

                for (var entry : cardMap.entrySet()) {
                    var count = entry.getValue();
                    if (count > maxCount || (count == maxCount && entry.getKey().getValue() > maxCard.getValue())) {
                        maxCount = count;
                        maxCard = entry.getKey();
                    }
                }

                cardMap.put(maxCard, maxCount + jokerCount);
            }

            var map = buildFrequencyMap(cardMap);
            for (var value : values()) {
                if (value.isHandType(map)) {
                    return value;
                }
            }

            return null;
        }

        /**
         * Builds and returns a Map for the List of specified Cards where the key is the Card and the value
         * is the number of times that Card appears in the specified List of Cards.
         *
         * @param cards The List of Cards to build a Map of.
         * @return A Map for the List of specified Cards where the key is the Card and the value
         * is the number of times that Card appears in the specified List of Cards.
         */
        public static @NotNull Map<Card, Integer> buildCardMap(@NotNull List<Card> cards) {
            return cards.stream()
                    .distinct()
                    .collect(Collectors.toMap(
                            Function.identity(),
                            it -> Collections.frequency(cards, it)
                    ));
        }

        /**
         * Builds and returns a Map for the specified Card Map where the key is the frequency of a Card
         * and the value is how many times that frequency occurs in the specified Map.
         *
         * @param cardMap The Card Map to build a frequency Map of.
         * @return A Map for the specified Card Map where the key is the frequency of a Card
         * and the value is how many times that frequency occurs in the specified Map.
         */
        public static @NotNull Map<Integer, Integer> buildFrequencyMap(@NotNull Map<Card, Integer> cardMap) {
            var frequencies = cardMap.values();
            return frequencies.stream()
                    .distinct()
                    .collect(Collectors.toMap(
                            Function.identity(),
                            it -> Collections.frequency(frequencies, it)
                    ));
        }

        /**
         * Overridden by specific HandTypes. Returns true if the specified Frequency Map represents a
         * hand of this type.
         *
         * @param frequencyMap The Frequency Map to check the HandType of.
         * @return True if the specified Frequency Map represents a hand of this type.
         */
        public boolean isHandType(@NotNull Map<Integer, Integer> frequencyMap) { return false; }

        /**
         * Returns the HandType strength.
         *
         * @return The HandType strength.
         */
        public int getStrength() {
            return strength;
        }

        @Override
        public @NotNull String toString() {
            return "HandType{" +
                    "name=" + name() +
                    ", strength=" + strength +
                    '}';
        }
    }

    /**
     * Represents a Hand in a card game with a List of Cards and a bid (amount being wagered).
     */
    public static class Hand implements Comparable<Hand> {
        private final List<Card> cards;
        private final int bid;

        private final HandType handType;

        private final boolean joker;

        /**
         * Instantiates a new Hand with the specified List of Cards and the amount of bid.
         * Jokers replace Jacks if joker is set to True.
         *
         * @param cards The List of Cards that make up the Hand.
         * @param bid The wager on this Hand.
         * @param joker If true, then Jokers replace Jacks.
         */
        public Hand(@NotNull List<Card> cards, int bid, boolean joker) {
            this.cards = new ArrayList<>(cards);
            this.bid = bid;
            this.joker = joker;
            this.handType = HandType.calculateHandType(this.cards, this.joker);
        }

        /**
         * Returns an unmodifiable List of the Cards that make up this Hand.
         *
         * @return An unmodifiable List of the Cards that make up this Hand.
         */
        public @NotNull List<Card> getCards() {
            return Collections.unmodifiableList(cards);
        }

        /**
         * Returns the bid of this Hand.
         *
         * @return The bid of this Hand.
         */
        public int getBid() {
            return bid;
        }

        /**
         * Returns true if this Hand uses Jokers.
         *
         * @return True if this Hand uses Jokers.
         */
        public boolean getJoker() {
            return joker;
        }

        /**
         * Returns the HandType of this Hand.
         *
         * @return The HandType of this Hand.
         */
        public HandType getHandType() {
            return handType;
        }

        @Override
        public int compareTo(@NotNull Hand o) {
            var result = 0;

            if (handType.getStrength() > o.handType.getStrength()) {
                result = 1;
            } else if (handType.getStrength() < o.handType.getStrength()) {
                result = -1;
            } else {
                // Must compare the individual cards!
                for (int i = 0; i < cards.size(); i++) {
                    if (cards.get(i).getValue() > o.cards.get(i).getValue()) {
                        result = 1;
                        break;
                    } else if (cards.get(i).getValue() < o.cards.get(i).getValue()) {
                        result = -1;
                        break;
                    }
                }
            }
            return result;
        }

        @Override
        public @NotNull String toString() {
            return "Hand{" +
                    "cards=" + cards +
                    ", bid=" + bid +
                    ", joker=" + joker +
                    ", handType=" + handType +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Hand hand = (Hand) o;
            return getBid() == hand.getBid() && getJoker() == hand.getJoker() &&
                    Objects.equals(getCards(), hand.getCards()) && getHandType() == hand.getHandType();
        }

        @Override
        public int hashCode() {
            return Objects.hash(getCards(), getBid(), getJoker(), getHandType());
        }

        /**
         * Parses the specified String which contains the five Cards and bid in this format:<br>
         * CCCCC 111<br>
         * 32T3K 765<br>
         * Jokers are not used. Returns the Hand parsed from the Cards and bid String.
         *
         * @param handWithBid The String with the Cards and bid.
         * @return The Hand parsed from the Cards and bid String.
         */
        public static @NotNull Hand from(String handWithBid) {
            return from(handWithBid, false);
        }

        /**
         * Parses the specified String which contains the five Cards and bid in this format:<br>
         * CCCCC 111<br>
         * 32T3K 765<br>
         * Jokers replace Jacks if joker is true. Returns the Hand parsed from the Cards and bid String.
         * @param handWithBid The String with the Cards and bid.
         * @param joker If true, then Jokers replace Jacks in the returned Hand.
         * @return The Hand parsed from the Cards and bid String.
         */
        public static @NotNull Hand from(String handWithBid, boolean joker) {
            if (handWithBid == null || handWithBid.isBlank()) {
                throw new RuntimeException("Invalid handWithBid String: " + handWithBid);
            }

            var split = handWithBid.split(VALUE_SPLIT);

            return new Hand(parseCards(split[0], joker), Integer.parseInt(split[1]), joker);
        }

        /**
         * Parses the String of cards and returns a List of Cards. If joker is true, Jokers replace Jacks.
         * The format of the String is simply the five characters of the cards: <br>
         * T555J5<br>
         *
         * @param cards The String version of the cards to parse.
         * @param joker If true, Jokers replace Jacks.
         * @return A List of Cards.
         */
        public static @NotNull List<Card> parseCards(@NotNull String cards, boolean joker) {
            if (joker) {
                return cards
                        .chars()
                        .mapToObj(it -> Card.fromLabel((char)it, joker))
                        .filter(Objects::nonNull)
                        .toList();
            }
            return cards
                    .chars()
                    .mapToObj(it -> Card.fromLabel((char)it))
                    .filter(Objects::nonNull)
                    .toList();
        }
    }

    private static final String VALUE_SPLIT = " ";

    private final Queue<Hand> hands = new PriorityQueue<>();

    private CamelCards() {

    }

    /**
     * Parses the specified Strings Hands and returns a CamelCard instance loaded with the parsed Hands.
     * If joker is true, then Jacks are replaced with Jokers.
     *
     * @param input The List of String Hands to parse.
     * @param joker If true, then Jacks are replaced with Jokers.
     * @return A new CamelCards instance with the specified Hands loaded into it.
     */
    public static @NotNull CamelCards buildCamelCards(@NotNull List<String> input, boolean joker) {
        var camelCards = new CamelCards();

        input.stream()
                .map(it -> Hand.from(it, joker))
                .forEach(camelCards::addHand);

        return camelCards;
    }

    /**
     * Adds a new Hand to this CamelCards instance.
     *
     * @param hand The Hand to add to this CamelCards instance.
     */
    public void addHand(@NotNull Hand hand) {
        hands.add(hand);
    }

    /**
     * Returns the total winnings of this set of Hands. The total winnings
     * of this set of Hands is calculated by adding up the result of multiplying
     * each hand's bid with its rank (765 * 1 + 220 * 2 + 28 * 3 + 684 * 4 + 483 * 5).
     *
     * @return The total winnings of this set of Hands.
     */
    public long calculateTotalWinnings() {
        long sum = 0;
        int currentRank = 1;
        while (hands.peek() != null) {
            var hand = hands.poll();
            sum += ((long) currentRank * hand.getBid());
            currentRank++;
        }
        return sum;
    }
}
